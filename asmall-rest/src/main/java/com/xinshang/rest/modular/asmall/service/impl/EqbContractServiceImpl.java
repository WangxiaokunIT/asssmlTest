package com.xinshang.rest.modular.asmall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.xinshang.rest.common.util.ConvertUpMoney;
import com.xinshang.rest.common.util.OssUtil;
import com.xinshang.rest.common.util.SealUtil;
import com.xinshang.rest.common.util.eqb.*;
import com.xinshang.rest.common.util.wk.helper.*;
import com.xinshang.rest.common.util.wk.helper.exception.DefineException;
import com.xinshang.rest.config.properties.EqbProperties;
import com.xinshang.rest.config.properties.OssProperties;
import com.xinshang.rest.modular.asmall.dao.EqbContractMapper;
import com.xinshang.rest.modular.asmall.dao.JoininMapper;
import com.xinshang.rest.modular.asmall.dto.CreateContractRequestDTO;
import com.xinshang.rest.modular.asmall.model.*;
import com.xinshang.rest.modular.asmall.service.IEqbContractService;
import com.xinshang.rest.modular.asmall.service.IEqbCzkeyService;
import com.xinshang.rest.modular.asmall.service.IJoininService;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
@AllArgsConstructor
public class EqbContractServiceImpl extends ServiceImpl<EqbContractMapper, EqbContract> implements IEqbContractService {

    private  final JoininMapper joininMapper;
    private final EqbProperties eqbProperties;

    /**
     * 创建合同
     * @param
     * @return
     */
    @Override
    public EqbContract createContract(CreateContractRequestDTO createContractRequestDTO, Member member) {
        EqbContract eqbContract = null;

        //查询项目和投资人信息
        ContractInfo contractInfo = joininMapper.joinListByProjectId(Long.valueOf(createContractRequestDTO.getProjectId()));
        if(contractInfo == null ){
            log.info("项目未找到投资人相关信息");
            return null;
        }
        //循环生成合同
        //获取投资人列表
        JoininInfo joininInfo = new JoininInfo();
        joininInfo.setCustomId(member.getId());
        joininInfo.setInvestmentAmount(createContractRequestDTO.getMoney());
        joininInfo.setCardNumber(member.getCardNumber());
        joininInfo.setEmail(member.getEmail());
        joininInfo.setUsername(member.getRealName());
        joininInfo.setPhone(member.getPhone());
        Map<String,Object> resultMap = BeanUtil.beanToMap(contractInfo);
        resultMap.putAll(BeanUtil.beanToMap(joininInfo, false, true));
        resultMap.put("ftlPath",eqbProperties.getTemplatePath()+ PDFUtil.HTML_FTL);
        resultMap.put("ftlFile","dlxs.ftl");
        resultMap.put("filename",contractInfo.getNumber()+joininInfo.getCardNumber()+"_temporary");
        resultMap.put("chinese", ConvertUpMoney.toChinese(joininInfo.getInvestmentAmount().toString()));

        //生成html文件
        String htmlFile = eqbProperties.getTemplatePath()+PDFUtil.HTML;

        try {
            htmlFile = PDFUtil.configurationg(resultMap,htmlFile);
        } catch (Exception e) {
            log.error("html保存失败"+ JSONUtil.parse(joininInfo));
        }
        //生成PDF文件
        String pdfFile = htmlFile.replace("html","pdf");
        try{
            AgreementUtil.toPdf(htmlFile, pdfFile);
        }catch(Exception e){
            log.error("pdf保存失败，".concat(e.getMessage()),e);
            return null;
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
        if(eqbcz(pdfFile,joininInfo,fileName,eqbContract)){
            eqbContract.setType(1);//默认合同
            eqbContract.setProjectId(Long.valueOf(createContractRequestDTO.getProjectId()));//项目id
            eqbContract.setMemberId(joininInfo.getCustomId().longValue());//客户id
            eqbContract.setStatus(0);
            eqbContract.setCreateTime(new Date());
            eqbContract.setUpdateTime(new Date());
            insert(eqbContract);
            //eqbContractList.add(eqbContract);
        }else{
            log.info("生成合同失败："+joininInfo.getCardNumber());
        }
/*
        if(eqbContractList!=null && eqbContractList.size()>0){
            //批量插入生成的合同
            this.insertBatch(eqbContractList);
        }
*/
        return eqbContract;
    }

    /**
     * 合同签署成功回调方法
     * @param eqbDTO
     * @return
     */
    @Override
    public Boolean eqbCallBack(EqbDTO eqbDTO, OssProperties ossProperties) throws DefineException, IOException {

        log.info("---------------------签署完成后，通知回调，平台方进行签署流程归档 start-----------------------------");
        SignHelper.archiveSignFlow(eqbDTO.getFlowId());
        log.info("---------------------归档后，获取文件下载地址 start-----------------------------");
        JSONObject jsonObject = SignHelper.downloadFlowDoc(eqbDTO.getFlowId());
        Object codeobj = jsonObject.get("code");
        if(codeobj != null ){
            int code = Integer.parseInt(codeobj.toString());
            if(code == 0){
                JSONObject dataJson = jsonObject.getJSONObject("data");
                JSONArray dataArray = dataJson.getJSONArray("docs");
                dataJson  = dataArray.getJSONObject(0);
                String fileUrl = dataJson.getString("fileUrl");
                EntityWrapper<EqbContract> entityWrapper = new EntityWrapper<>();
                entityWrapper.eq("sign_id",eqbDTO.getFlowId());
                EqbContract eqbContract = this.selectOne(entityWrapper);
                eqbContract.setStatus(1);
                InputStream in =OssUtil.getNetUrlHttps(fileUrl);
                MultipartFile multipartFile = new MockMultipartFile(eqbDTO.getFlowId()+".pdf",eqbDTO.getFlowId()+".pdf","", in);
                fileUrl = FileUtil.uploadFilePath(multipartFile, ossProperties);
                eqbContract.setContractUrl(fileUrl);
                this.updateById(eqbContract);
            }else{
                log.info("获取下载地址失败");
                return false;
            }
        }else{
            return false;
        }
        return true;
    }

    /**
     * 创建e签宝合同
     */
    private boolean eqbcz(String pdfFile,JoininInfo joininInfo,String fileName,EqbContract eqbContract){
        log.info("pdfFile:{},joininInfo:{},eqbContract:{}",pdfFile,joininInfo,eqbContract);
        try {
            String filePath = pdfFile;

            log.info("---------------------获取token start------------------------------");
            TokenHelper.getTokenData();

            log.info("---------------------创建个人账号start-------------------------------");
            JSONObject personAcctJson = AccountHelper.createPersonAcct(joininInfo.getCardNumber(), joininInfo.getUsername(), null, joininInfo.getCardNumber(), joininInfo.getPhone(), joininInfo.getEmail());
            String acctId = personAcctJson.getString("accountId");

//			log.info("---------------------创建个人模板印章start-------------------------------");
            JSONObject personSealJson = SealHelper.createPersonTemplateSeal(acctId, "个人印章", "RED", null, null, "YGYJFCS");
            String personSealId = personAcctJson.getString("sealId");

            log.info("---------------------创建机构账号start----------------------------------");
            JSONObject orgAcctJson = AccountHelper.createOrgAcct("BBBB", acctId, "山东爱赛维斯网络技术服务股份有限公司", null, "52227058XT51M4AL62");
            String orgId = orgAcctJson.getString("orgId");

            log.info("---------------------创建机构印章start----------------------------------");
            JSONObject orgSealJson = SealHelper.createOrgTemplateSeal(orgId, "爱赛商城公司印章", "BLUE", null, null, null, null, "TEMPLATE_ROUND", "STAR");
            String orgSealId = orgSealJson.getString("sealId");

            log.info("---------------------通过上传方式创建文件start-----------------------------");
            JSONObject uploadJson = FileTemplateHelper.createFileByUpload(filePath, fileName, orgId);
            String uploadUrl = uploadJson.getString("uploadUrl");
            String fileId = uploadJson.getString("fileId");

            log.info("---------------------文件流上传文件start---------------------------------");
            FileTemplateHelper.streamUpload(filePath, uploadUrl);

            log.info("---------------------签署流程创建 start---------------------------------");
            //可通过进行组装入参，具体使用中根据实际情况传参
            //SignParamUtil.createSignFlowStart()
            JSONObject flowJson = SignHelper.createSignFlow();
            String flowId = flowJson.getString("flowId");

            log.info("---------------------流程文档添加 start---------------------------------");
            SignHelper.addFlowDoc(flowId, fileId);

/*
            log.info("---------------------添加平台自动盖章签署区 start---------------------------");
            SignHelper.addPlatformAutoSignArea(flowId, Lists.newArrayList(fileId), Lists.newArrayList("e6ecb2fd-e4ec-4c04-be97-85c3d46a960e"));//orgSealId

*/
            log.info("---------------------添加手动盖章签署区 start-----------------------------");
            SignHelper.addSignerHandSignArea(flowId, Lists.newArrayList(fileId), Lists.newArrayList(acctId), null);

            log.info("---------------------签署流程开启 start-----------------------------");
            SignHelper.startSignFlow(flowId);

            JSONObject jsonObject = SignHelper.qrySignUrl(flowId,acctId,"0",null);

            if(jsonObject!=null){
                eqbContract.setContractUrl(jsonObject.get("url").toString());
            }
            eqbContract.setSignId(flowId);

        } catch (DefineException e) {
            e.getE().printStackTrace();
        }
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

    /**
     * 获取网络图片文件流
     * @param destUrl
     * @return
     */
    public InputStream getInputStream(String destUrl) {
        InputStream inputStream = null;
        URLConnection urlConnection = null;
        URL url = null;
        try {
            url = new URL(destUrl);
            urlConnection = url.openConnection();
            inputStream = urlConnection.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }
}
