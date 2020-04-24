package com.xinshang.modular.biz.dao;

import com.xinshang.modular.biz.model.Examine;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.modular.biz.vo.ExamineVO;

import java.util.List;

/**
 * <p>
 * 审核记录表 Mapper 接口
 * </p>
 *
 * @author lvyingkai
 * @since 2019-10-18
 */
public interface ExamineMapper extends BaseMapper<Examine> {


    List<ExamineVO>selectlistById(String itemId);

    List<ExamineVO>selectlistByIdDesc(String itemId);


}
