package com.xinshang.modular.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.constant.Constants;
import com.xinshang.core.common.constant.factory.MutiStrFactory;
import com.xinshang.core.common.exception.BizExceptionEnum;
import com.xinshang.core.exception.SystemException;
import com.xinshang.core.util.RedisUtil;
import com.xinshang.modular.system.dao.DictMapper;
import com.xinshang.modular.system.model.Dict;
import com.xinshang.modular.system.service.IDictService;
import com.xinshang.modular.system.vo.DictVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

/**
 * @author zhangjiajia
 * @date 2018年11月23日 11:37:59
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {

    @Autowired
    private DictMapper dictMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void addDict(String dictCode,String dictName,String dictRemark, String dictValues) {
        //判断有没有该字典
        List<Dict> dicts = dictMapper.selectList(new EntityWrapper<Dict>().eq("code", dictCode).and().eq("parent_id", 0));
        if (dicts != null && dicts.size() > 0) {
            throw new SystemException(BizExceptionEnum.DICT_EXISTED.getMessage());
        }

        //解析dictValues
        List<Map<String, String>> items = MutiStrFactory.parseKeyValue(dictValues);

        //添加字典
        Dict dict = new Dict();
        dict.setName(dictName);
        dict.setCode(dictCode);
        dict.setRemark(dictRemark);
        dict.setSortNum(0);
        dict.setParentId(0);
        this.dictMapper.insert(dict);

        //添加字典条目
        for (Map<String, String> item : items) {
            String code = item.get(MutiStrFactory.MUTI_STR_CODE);
            String name = item.get(MutiStrFactory.MUTI_STR_NAME);
            String num = item.get(MutiStrFactory.MUTI_STR_NUM);
            Dict itemDict = new Dict();
            itemDict.setParentId(dict.getId());
            itemDict.setCode(code);
            itemDict.setName(name);

            try {
                itemDict.setSortNum(Integer.valueOf(num));
            } catch (NumberFormatException e) {
                throw new SystemException(BizExceptionEnum.DICT_MUST_BE_NUMBER.getMessage());
            }
            this.dictMapper.insert(itemDict);
        }
        //清除ecache缓存
        redisUtil.del(Constants.asmall_SYSTEM_DICT_KEY);
    }

    @Override
    public void editDict(Integer dictId,String dictCode, String dictName,String dictRemark, String dicts) {
        //删除之前的字典
        this.delteDict(dictId);
        //重新添加新的字典
        this.addDict(dictCode,dictName,dictRemark, dicts);
        //清除ecache缓存
        redisUtil.del(Constants.asmall_SYSTEM_DICT_KEY);
    }

    @Override
    public void delteDict(Integer dictId) {
        //删除这个字典的子词典
        Wrapper<Dict> dictEntityWrapper = new EntityWrapper<>();
        dictEntityWrapper = dictEntityWrapper.eq("parent_id", dictId);
        dictMapper.delete(dictEntityWrapper);
        Dict dict = dictMapper.selectById(dictId);
        //删除这个词典
        dictMapper.deleteById(dictId);
        //清除ecache缓存
        redisUtil.del(Constants.asmall_SYSTEM_DICT_KEY);
    }


    @Override
    public List<Dict> selectByParentCode(String code) {
        return this.baseMapper.selectByParentCode(code);
    }


    @Override
    public List<Map<String, Object>> list(String conditiion) {
        return this.baseMapper.list(conditiion);
    }

    @Override
    public List<DictVo> selectByNames(String codes) {
        String[] str = codes.split(",");
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < str.length; k++) {
            if(k == str.length - 1) {
                sb.append("'").append(str[k]).append("'");
            }else {
                sb.append("'").append(str[k]).append("',");
            }
        }
        return this.baseMapper.selectByNames(sb.toString());
    }


}
