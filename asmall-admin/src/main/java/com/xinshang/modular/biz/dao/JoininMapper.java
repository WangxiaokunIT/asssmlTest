package com.xinshang.modular.biz.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.modular.biz.dto.JoininDTO;
import com.xinshang.modular.biz.model.ContractInfo;
import com.xinshang.modular.biz.model.Joinin;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.modular.biz.model.JoininInfo;
import com.xinshang.modular.biz.model.RepayPlan;
import com.xinshang.modular.biz.vo.JoininVO;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 加盟表 Mapper 接口
 * </p>
 *
 * @author zhoushuai
 * @since 2019-10-23
 */
@Repository
public interface JoininMapper extends BaseMapper<Joinin> {
    /**
     * 根据条件获取审核信息
     * @param param
     * @param page
     * @return
     */
    List<JoininInfo> joinList(@Param("param") JoininDTO param, Page<JoininInfo> page);


    ContractInfo joinListByProjectId(@Param("projectId") Long projectId);

    /**
     * 根据招募id获取加盟信息
     * @param projectId
     * @return
     */
    List<JoininVO> showJoinin(@Param("projectId") Long projectId);


}
