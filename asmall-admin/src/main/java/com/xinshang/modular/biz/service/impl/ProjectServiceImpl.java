package com.xinshang.modular.biz.service.impl;

import com.allinpay.yunst.sdk.bean.YunRequest;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.config.properties.AllinPayCodeProperties;
import com.xinshang.config.properties.AllinPayProperties;
import com.xinshang.config.properties.SystemProperties;
import com.xinshang.constant.Constants;
import com.xinshang.core.enums.BizTypeEnum;
import com.xinshang.core.enums.UserTypeEnum;
import com.xinshang.core.shiro.ShiroKit;
import com.xinshang.core.shiro.ShiroUser;
import com.xinshang.core.util.AllinPayUtil;
import com.xinshang.core.util.NoUtil;
import com.xinshang.core.util.RedisUtil;
import com.xinshang.modular.biz.dao.ProjectMapper;
import com.xinshang.modular.biz.dto.*;
import com.xinshang.modular.biz.model.Examine;
import com.xinshang.modular.biz.model.Joinin;
import com.xinshang.modular.biz.model.Project;
import com.xinshang.modular.biz.model.Supplier;
import com.xinshang.modular.biz.service.*;
import com.xinshang.modular.biz.utils.MoneyFormatTester;
import com.xinshang.modular.biz.vo.ProjectVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 发布招募信息申请 服务实现类
 * </p>
 *
 * @author lvyingkai
 * @since 2019-10-16
 */
@Slf4j
@Service
@AllArgsConstructor
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {

    
    private final ProjectMapper projectMapper;

    private final IRepayPlanService iRepayPlanService;

    private final ISupplierService supplierService;

    private final IExamineService examineService;

    private final AllinPayCodeProperties allinPayCodeProperties;

    private final AllinPayProperties allinPayProperties;

    private final IJoininService joininService;

    private final SystemProperties systemProperties;
    
    private RedisUtil redisUtil;

    @Override
    public String getMaxNumber() {
        Integer maxNumber = projectMapper.getMaxNumber();
        int number = (maxNumber == null ? 0 : maxNumber) + 1;
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return date + String.format("%03d", number);
    }

    /**
     *                 { code: 1, name: '申请中'},
     *                 { code: 2, name: '招募中'},
     *                 { code: 3, name: '未通过'},
     *                 { code: 4, name: '招募结束'},
     *                 { code: 5, name: '招募完成'},
     *                 { code: 6, name: '提现申请中'},
     *                 { code: 7, name: '招募待还'},
     *                 { code: 8, name: '招募已还'}
     * @param param
     * @param page
     * @return
     */
    @Override
    public List<ProjectVO> listUp(ProjectDTO param, Page<ProjectVO> page) {

        if(param.getPageType() != null && param.getState() == null && StringUtils. isBlank(param.getEndTime()) && StringUtils. isBlank(param.getStartTime())
           && StringUtils. isBlank(param.getProjectName()) && StringUtils. isBlank(param.getProjectNumber()) && StringUtils. isBlank(param.getSupplierName())) {
            switch (param.getPageType()) {
                // 招募信息申请
                case 1:
                    param.setStates("1,3");
                    break;
                // 招募信息审核
                case 2:
                    param.setStates("1");
                    break;
                // 招募完成审核
                case 3:
                    param.setStates("2,4");
                    break;
                // 招募提现申请
                case 4:
                    param.setStates("5");
                    break;
                // 招募提现审核
                case 5:
                    param.setStates("6");
                    break;
                // 招募还款
                case 6:
                    param.setStates("7");
                    break;
                default:
                    break;
            }
        }
        return projectMapper.listUp(param, page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateState(Project project, String content) {



        if(project.getState() == 3) {
            // 审核不通过
            // 修改供应商的已用余额
            Supplier supplier = supplierService.selectById(project.getSupplierId());
            supplier.setUsedLoanAmount(supplier.getUsedLoanAmount().subtract(project.getMaxMoney()));
            supplierService.updateById(supplier);
        } else if(project.getState() == 1) {
            // 申请中
            // 修改供应商的已用余额
            Supplier supplier = supplierService.selectById(project.getSupplierId());
            supplier.setUsedLoanAmount(supplier.getUsedLoanAmount().add(project.getMaxMoney()));
            supplierService.updateById(supplier);
        }

        // 更新招募信息
        updateProject(project);

        // 保存审核信息
        ShiroUser user = ShiroKit.getUser();

        Examine examine = new Examine();
        examine.setCreateTime(new Date());
        examine.setProjectId(project.getId());
        examine.setState(project.getState());
        examine.setType(1);
        examine.setUserId(Long.valueOf(user.getId()));
        examine.setUserName(user.getName());
        // 审核说明
        if(StringUtils.isNotBlank(content)) {
            examine.setRemarks(content);
        }
        // 插入审核记录
        examineService.insert(examine);
    }

    @Override
    public void updateProject(Project project) {
        if(project.getState() != 1) {
            project.setDetails(null);
        }
        project.setUnit("月");
        if(project.getState() == 7) {


            // 获取当前时间
            LocalDate today = LocalDate.now();
            // 增加一天
            LocalDate startTime = today.plusDays(1);
            // 算出最后一天
            LocalDate endTime = startTime.plusMonths(project.getRecruitmentCycle());

            ZoneId zone = ZoneId.systemDefault();
            Instant sTime = startTime.atStartOfDay().atZone(zone).toInstant();
            project.setStartRecordTime(Date.from(sTime));

            Instant eTime = endTime.atStartOfDay().atZone(zone).toInstant();
            project.setEndRecordTime(Date.from(eTime));

            // 获取加盟列表
            List<Joinin> joininList = joininService.selectList(new EntityWrapper<Joinin>().eq("project_id", project.getId()));
            BigDecimal totalSum = new BigDecimal(0.00);
            for (Joinin joinin : joininList) {
                totalSum = totalSum.add(joinin.getInvestmentAmount());
            }
            // 返还可用额度
            Supplier supplier = supplierService.selectById(project.getSupplierId());
            supplier.setUsedLoanAmount(supplier.getUsedLoanAmount().subtract(project.getMaxMoney().subtract(totalSum)));
            supplierService.updateById(supplier);

            // 生成还款计划
            iRepayPlanService.createplan(project);
        }
        project.setUpdateTime(new Date());
        projectMapper.updateById(project);


    }

    @Override
    public void thawMoney(Project project) {

        // 批次号
        String orderId = NoUtil.generateCode(BizTypeEnum.BATCH_NUMBER, UserTypeEnum.SUPPLIER, project.getSupplierId());

        redisUtil.set("OrderService_batchAgentPay_" + project.getId(), orderId);
        redisUtil.set("OrderService_batchAgentPay_" + orderId, project.getId());


        Supplier supplier = supplierService.selectById(project.getSupplierId());
        log.info("批次订单号：{}", orderId);
//        log.info("业务代码：{},{}",allinPayCodeProperties.getBusinessCode(), supplier.getId());

        // 获取加盟列表
        EntityWrapper<Joinin> wrapper = new EntityWrapper<>();
        wrapper.eq("project_id", project.getId());
        List<Joinin> joininList = joininService.selectList(wrapper);

        // 批量代付列表
        List<BatchPaymentDTO> batchPaymentDTOList = new ArrayList<>();

        // 获取批次的数量
        Integer num = joininList.size()/100 + (joininList.size()%100 == 0 ? 0 : 1);
        int batchNum = 100;
        // 保存批次的订单号
        List<String> orderIds = new ArrayList<>();
        for (int k = 0; k < num; k++) {
            List<PaymentOrderDTO> paymentOrderDTOS = new ArrayList<>();
            // 单个批次总金额
            long amount_z = 0;
            String ids = "";
            // 加盟详情
            for (int y = k * batchNum; y < batchNum; y++) {
                if(y == joininList.size()){
                    break;
                }
                Joinin joinin = joininList.get(y);
                if("".equals(ids)) {
                    ids += joinin.getJoinId();
                }else {
                    ids += "," + joinin.getJoinId();
                }
                PaymentOrderDTO paymentOrderDTO = new PaymentOrderDTO();
                long amount = MoneyFormatTester.bigDecimal2Long(joinin.getInvestmentAmount());
                amount_z += amount;
                 paymentOrderDTO.setAmount(amount);
                paymentOrderDTO.setBizOrderNo(joinin.getBizOrderNo());
                paymentOrderDTOS.add(paymentOrderDTO);
            }

            // 单次订单号
            String orderPay = NoUtil.generateCode(BizTypeEnum.AGENCY_PAYMENT, UserTypeEnum.SUPPLIER, project.getSupplierId());
            redisUtil.set("OrderService_signalAgentPay_" + orderPay, project.getId());
            redisUtil.set("OrderService_signalAgentPay_" + orderPay + "_" + project.getId(), ids);
            orderIds.add(orderPay);

            BatchPaymentDTO batchPaymentDTO = new BatchPaymentDTO();
            batchPaymentDTO.setBizOrderNo(orderPay);
            batchPaymentDTO.setBizUserId(supplier.getBizUserId());
            batchPaymentDTO.setAccountSetNo(allinPayProperties.getAccountSetNo());
            batchPaymentDTO.setBackUrl(systemProperties.getProjectUrl() +  "/allinPayAsynRespNotice/payment");
            batchPaymentDTO.setAmount(amount_z);
            batchPaymentDTO.setFee(0L);
            batchPaymentDTO.setCollectPayList(paymentOrderDTOS);
            batchPaymentDTOList.add(batchPaymentDTO);
        }

        // 保存订单集合
        redisUtil.set("OrderService_signalAgentPay_" + project.getId(), orderIds);

        //同步到通联
        YunRequest request = new YunRequest("OrderService", "batchAgentPay");
        request.put("bizBatchNo", orderId);
        request.put("tradeCode", allinPayCodeProperties.getBusinessCode());
        request.put("batchPayList", batchPaymentDTOList);
        log.info("批量代付到通联:{}",request);
        Optional<AllinPayResponseDTO<BatchPaymentRespDTO>> response = AllinPayUtil.request(request,BatchPaymentRespDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(Constants.SUCCESS_CODE.equals(response.get().getStatus()),()->{
            log.warn("调用批量代付接口失败:{}",response);
            redisUtil.del("OrderService_batchAgentPay_" + project.getId());
            redisUtil.del("OrderService_batchAgentPay_" + orderId);
            redisUtil.del("OrderService_signalAgentPay_" + project.getId());
            for (String orderId_ : orderIds) {
                redisUtil.del("OrderService_signalAgentPay_" + orderId_);
                redisUtil.del("OrderService_signalAgentPay_" + orderId_ + "_" + project.getId());
            }

            return "调用批量代付接口失败";
        });

    }



}
