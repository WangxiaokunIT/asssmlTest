package com.xinshang.rest.modular.asmall.controller;

import com.xinshang.rest.common.util.R;
import com.xinshang.rest.modular.asmall.model.Category;
import com.xinshang.rest.modular.asmall.service.IItemCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author sunha
 * @since 2019/10/249:32
 */
@RestController
@RequestMapping("/itemCategory")
@Api(value = "商品分类", tags = "商品分类相关接口")
@AllArgsConstructor
@Slf4j
public class ItemCategoryController {
    private final IItemCategoryService itemCategoryService;
    @ApiOperation(value = "查询全部商品分类")
    @GetMapping("getCategory")
    public R getClassify(HttpServletRequest request) {
        List<Category> categoryTree= itemCategoryService.loadTree();
        return R.ok(categoryTree);
    }
}
