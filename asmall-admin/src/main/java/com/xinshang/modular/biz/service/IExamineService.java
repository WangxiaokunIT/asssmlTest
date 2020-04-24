package com.xinshang.modular.biz.service;

import com.xinshang.modular.biz.model.Examine;
import com.baomidou.mybatisplus.service.IService;
import com.xinshang.modular.biz.vo.ExamineVO;

import java.util.List;

/**
 * <p>
 * 审核记录表 服务类
 * </p>
 *
 * @author lvyingkai
 * @since 2019-10-18
 */
public interface IExamineService extends IService<Examine> {

    /**
     * 改装查找方法
     * @param itemId
     * @return
     */
    List<ExamineVO> selectListById (String itemId);

    /**
     * 倒序查找方法
     */
     List<ExamineVO> selectlistByIdDesc (String itemId);
}
