package com.xinshang.modular.biz.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.modular.biz.model.Supplier;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.modular.biz.vo.SupplierVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 供应商 Mapper 接口
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-10-21
 */
@Repository
public interface SupplierMapper extends BaseMapper<Supplier> {

    /**
     * 模糊查询供应商列表
     * @param search
     * @return
     */
    List<Supplier> searchSupplier(@Param("search")String search);

    /**
     * 分页查询供应商信息
     * @param page
     * @param supplier
     * @return
     */
    List<SupplierVO> selectPageInfo(@Param("page") Page<SupplierVO> page, @Param("supplier") Supplier supplier);
}
