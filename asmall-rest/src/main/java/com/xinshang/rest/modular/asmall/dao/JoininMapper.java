package com.xinshang.rest.modular.asmall.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

import com.xinshang.rest.modular.asmall.model.ContractInfo;
import com.xinshang.rest.modular.asmall.model.Joinin;
import com.xinshang.rest.modular.asmall.vo.JoininVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 加盟表 Mapper 接口
 * </p>
 *
 * @author zhoushuai
 * @since 2019-10-24
 */
@Repository
public interface JoininMapper extends BaseMapper<Joinin> {

    /**
     * 获取加盟信息
     * @param
     * @return
     */
    List<JoininVO> showJoinin( Long memberId,Page<JoininVO> page);

    ContractInfo joinListByProjectId(Long projectId);

}
