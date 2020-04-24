package com.xinshang.modular.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.timevale.esign.sdk.tech.bean.result.FileDigestSignResult;
import com.xinshang.config.properties.EqbProperties;
import com.xinshang.core.util.SealUtil;
import com.xinshang.core.util.eqb.*;
import com.xinshang.modular.biz.dao.EqbContractMapper;
import com.xinshang.modular.biz.model.*;
import com.xinshang.modular.biz.service.IEqbContractService;
import com.xinshang.modular.biz.service.IJoininService;
import com.xinshang.modular.biz.utils.ConvertUpMoney;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-10-23
 */
@Service
@Slf4j
public class EqbContractServiceImpl extends ServiceImpl<EqbContractMapper, EqbContract> implements IEqbContractService {

    @Autowired
    private IJoininService iJoininService;

    @Autowired
    private EqbProperties eqbProperties;
    /**
     * 创建合同
     * @param projectId
     * @return
     */
    @Override
    public boolean createContract(Long projectId) {
        EqbContract eqbContract ;
        List<EqbContract> eqbContractList = new ArrayList<>();

        //查询项目和投资人信息
        ContractInfo contractInfo = iJoininService.joinListByProjectId(projectId);
        if(contractInfo == null ){
            log.info("项目未找到投资人相关信息");
            return false;
        }
        //循环生成合同
        //获取投资人列表
        List<JoininInfo> joininInfos = contractInfo.getJoininInfos();
        Map<String,Object> resultMap = BeanUtil.beanToMap(contractInfo);
        for (JoininInfo joininInfo :joininInfos){
            resultMap.putAll(BeanUtil.beanToMap(joininInfo));
            resultMap.put("ftlPath",eqbProperties.getTemplatePath()+PDFUtil.HTML_FTL);
            resultMap.put("ftlFile","dlxs.ftl");
            resultMap.put("filename",contractInfo.getNumber()+joininInfo.getCardNumber()+"_temporary");
            resultMap.put("chinese",ConvertUpMoney.toChinese(joininInfo.getInvestmentAmount().toString()));

            //生成html文件
            String htmlFile = eqbProperties.getTemplatePath()+PDFUtil.HTML;

            try {
                htmlFile = PDFUtil.configurationg(resultMap,htmlFile);
            } catch (Exception e) {
                log.error("html保存失败"+ JSONUtil.parse(joininInfos));
            }
            //生成PDF文件
            String pdfFile = htmlFile.replace("html","pdf");
            try{
                AgreementUtil.toPdf(htmlFile, pdfFile);
            }catch(Exception e){
                log.error("pdf保存失败，".concat(e.getMessage()),e);
                return false;
            }
            //增加公司和法人的签章
            try {
                SealUtil.imagePdf(eqbProperties.getAisaiSeal(),eqbProperties.getLegalSeal(),pdfFile,pdfFile.replace("_temporary",""));
            }catch (Exception ex){
                log.info("保存pdf失败:"+pdfFile);
                log.error("pdf保存失败，".concat(ex.getMessage()),ex);
            }
            pdfFile= pdfFile.replace("_temporary","");
            String fileName = contractInfo.getNumber()+joininInfo.getCardNumber()+".pdf";
            //E签宝存证
            //合同实体
            eqbContract= new EqbContract();
            if(eqbcz(pdfFile,joininInfo,eqbContract,fileName)){
                eqbContract.setType(1);//默认合同
                eqbContract.setProjectId(projectId);//项目id
                eqbContract.setMemberId(joininInfo.getCustomId().longValue());//客户id

                eqbContract.setCreateTime(new Date());
                eqbContract.setUpdateTime(new Date());
                eqbContractList.add(eqbContract);
            }else{
                log.info("生成合同失败："+joininInfo.getCardNumber());
            }
        }
                if(eqbContractList!=null && eqbContractList.size()>0){
            //批量插入生成的合同
            this.insertBatch(eqbContractList);
        }
        return true;
    }

    /**
     * 创建e签宝合同
     */
    private boolean eqbcz(String pdfFile , JoininInfo joininInfo , EqbContract  eqbContract ,String fileName){

        return true;
    }

    private boolean verification(String opsResp){
        if(StringUtils.isEmpty(opsResp)){
            return false;
        }else{
            /**
             * 比较时间
             */
            String timeout = ToolsHelper.stampToDate(opsResp);

            String nowtime = DateUtil.now(); //DateUtils.formatTime2();

            if(timeout.compareTo(nowtime)>0){
                return true;
            }else{
                return  false;
            }
        }
    }
}
