package com.xinshang.modular.biz.service.impl;

import com.xinshang.modular.biz.model.Examine;
import com.xinshang.modular.biz.dao.ExamineMapper;
import com.xinshang.modular.biz.service.IExamineService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.modular.biz.vo.ExamineVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 审核记录表 服务实现类
 * </p>
 *
 * @author lvyingkai
 * @since 2019-10-18
 */
@Service
public class ExamineServiceImpl extends ServiceImpl<ExamineMapper, Examine> implements IExamineService {

    @Autowired
    private ExamineMapper examineMapper;
    /**
     * 改装查找方法
     *
     * @param itemId
     * @return
     */
    @Override
    public List<ExamineVO> selectListById(String itemId) {

        return examineMapper.selectlistById(itemId);
    }

    @Override
    public List<ExamineVO> selectlistByIdDesc(String itemId) {
        return examineMapper.selectlistByIdDesc(itemId);
    }
}
