package com.xinshang.modular.biz.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.modular.biz.dto.ProjectDTO;
import com.xinshang.modular.biz.model.Project;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.modular.biz.vo.ProjectVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 发布招募信息申请 Mapper 接口
 * </p>
 *
 * @author lvyingkai
 * @since 2019-10-16
 */
@Repository
public interface ProjectMapper extends BaseMapper<Project> {

    /**
     * 获取最大的项目编号的序号
     * @return
     */
    Integer getMaxNumber();

    /**
     * 获取项目
     * @param param
     * @param page
     * @return
     */
    List<ProjectVO> listUp(@Param("param") ProjectDTO param, Page<ProjectVO> page);
}
