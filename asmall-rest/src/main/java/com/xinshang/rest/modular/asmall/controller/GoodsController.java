package com.xinshang.rest.modular.asmall.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.factory.PageFactory;
import com.xinshang.rest.modular.asmall.dto.GoodsSpecsQueryDTO;
import com.xinshang.rest.modular.asmall.dto.ItemDTO;
import com.xinshang.rest.modular.asmall.dto.ItemQueryDTO;
import com.xinshang.rest.modular.asmall.model.*;
import com.xinshang.rest.modular.asmall.service.*;
import com.xinshang.rest.modular.asmall.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * 商品管理
 * @author sunhao
 */
@RestController
@RequestMapping("/goods")
@Api(value = "商品管理",tags = "商品相关接口")
@AllArgsConstructor
@Slf4j
public class GoodsController {

    private final IItemService itemService;
    private final ISpecsAttributeService specsAttributeService;
    private final IItemSpecsService iItemSpecsService;

    @ApiOperation(value = "获取商品列表")
    @PostMapping("showProduct")
    public R<Page<ListItemVO>> showGoods(@RequestBody ItemQueryDTO itemQueryDTO) {
        Page<Item> page = itemQueryDTO.defaultPage();
        EntityWrapper<Item> wrapper = new EntityWrapper<>();
        wrapper.eq("status", 1);
        wrapper.eq("audit_status",2);
        wrapper.eq(itemQueryDTO.getCid() != null,"cid",itemQueryDTO.getCid());
        wrapper.like(itemQueryDTO.getTitle() != null,"title",  itemQueryDTO.getTitle());
        wrapper.eq(itemQueryDTO.getCategoryId() != null,"category_id",itemQueryDTO.getCategoryId());
        if(itemQueryDTO.getCid() != null){
            wrapper.orderBy("price", true);
        }else{
            wrapper.orderBy("created", false);
        }

        Page<Item> itemPage = itemService.selectPage(page, wrapper);
        List<Item> records = itemPage.getRecords();
        List<ListItemVO> listItemVOS = new ArrayList<>(records.size());

        for (Item record : records) {
            ListItemVO livo = new ListItemVO();
            BeanUtil.copyProperties(record,livo);
            listItemVOS.add(livo);
        }

        Page<ListItemVO> newPage = new Page<>();
        itemPage.setRecords(null);
        BeanUtil.copyProperties(itemPage,newPage);
        newPage.setRecords(listItemVOS);
        return R.ok(newPage);
    }

    @ApiOperation(value = "获取商品详情")
    @GetMapping("showProductById/{itemNumber}")
    public R<ItemVO> showGoodsBy(@PathVariable("itemNumber") String itemNumber) {
        Item item = itemService.selectOne(new EntityWrapper<Item>().eq("item_number",itemNumber));
        ItemVO ivo = new ItemVO();
        if(item==null){
            return R.ok(ivo);
        }
        BeanUtil.copyProperties(item,ivo);
        ivo.setBannerDetailList(new ArrayList<>(Arrays.asList(item.getDetailBanner().split(","))));
        //如果有详情视频，就把视频封面放到详情banner的第一个
        if(StrUtil.isNotEmpty(ivo.getInfoVideoImage())){
            ivo.getBannerDetailList().add(0,ivo.getInfoVideoImage());
        }

        //查询商品规格
        List<ItemSpecs> ItemSpecss = iItemSpecsService.selectList(new EntityWrapper<ItemSpecs>().eq("item_no", item.getItemNumber()));
        //没有规格
        if(ItemSpecss.size()==0){
            return R.ok(ivo);
        }

        //获取分类属性信息
        List<String> attrInfos =  Arrays.asList(item.getAttrInfo().split("\\|"));

        Map<String,String[]> attrInfosMap = new HashMap<>();

        for (String attrInfo : attrInfos) {
            String id = attrInfo.split(":")[0];
            String[] values = attrInfo.split(":")[1].split(",");
            attrInfosMap.put(id,values);
        }

        //查询所有使用分类属性对象
        List<SpecsAttribute> specsAttributes =  specsAttributeService.selectList(new EntityWrapper<SpecsAttribute>().in("id",attrInfosMap.keySet()));
        //保存返回前端规格属性
        List<SpecsAttributeVO> specsAttributeVOs = new ArrayList<>(specsAttributes.size());

        //保存返回前端规格属性值
        List<SpecsAttributeValueVO> specsAttributeValueVOS = new ArrayList<>();
        for (SpecsAttribute specsAttribute : specsAttributes) {
            SpecsAttributeVO savo = new SpecsAttributeVO();
            BeanUtil.copyProperties(specsAttribute,savo);
            specsAttributeVOs.add(savo);
        }

        //获取该分类下所有的属性值
        Iterator<Map.Entry<String, String[]>> iterator = attrInfosMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String[]> next = iterator.next();
            String key = next.getKey();
            String[] values = next.getValue();
            for (int i = 0; i < values.length; i++) {
                SpecsAttributeValueVO specsAttributeValueVO = new SpecsAttributeValueVO();
                specsAttributeValueVO.setAttributeValue(values[i]);
                specsAttributeValueVO.setPid(Integer.parseInt(key));
                specsAttributeValueVOS.add(specsAttributeValueVO);
            }
        }

        ivo.setAttributeValueList(specsAttributeValueVOS);
        ivo.setAttributeList(specsAttributeVOs);

        return R.ok(ivo);
    }


    @ApiOperation(value = "获取新品上市列表")
    @PostMapping("listNewArrival")
    public R<Page<ListItemVO>> listNewGoods(@RequestBody PageFactory pageFactory) {
        Page<Item> page = pageFactory.defaultPage();
        EntityWrapper<Item> wrapper = new EntityWrapper<>();
        wrapper.eq("status", 1);
        wrapper.eq("audit_status", 2);
        wrapper.eq("cid", 1);
        wrapper.orderBy("created", false);
        Page<Item> itemPage = itemService.selectPage(page, wrapper);
        List<Item> records = itemPage.getRecords();
        List<ListItemVO> listItemVOS = new ArrayList<>(records.size());

        for (Item record : records) {
            ListItemVO livo = new ListItemVO();
            BeanUtil.copyProperties(record,livo);
            listItemVOS.add(livo);
        }

        Page<ListItemVO> newPage = new Page<>();
        itemPage.setRecords(null);
        BeanUtil.copyProperties(itemPage,newPage);
        newPage.setRecords(listItemVOS);
        return R.ok(newPage);
    }


    @ApiOperation(value = "获取热销专区列表")
    @PostMapping("listSellWell")
    public R<Page<ListItemVO>> listSellWell(@RequestBody PageFactory pageFactory) {
        Page<Item> page = pageFactory.defaultPage();
        EntityWrapper<Item> wrapper = new EntityWrapper<>();
        wrapper.eq("status", 1);
        wrapper.eq("audit_status",2);
        wrapper.eq("cid",1);
        wrapper.orderBy("numAll", false);
        Page<Item> itemPage = itemService.selectPage(page, wrapper);
        List<Item> records = itemPage.getRecords();
        List<ListItemVO> listItemVOS = new ArrayList<>(records.size());

        for (Item record : records) {
            ListItemVO livo = new ListItemVO();
            BeanUtil.copyProperties(record,livo);
            listItemVOS.add(livo);
        }

        Page<ListItemVO> newPage = new Page<>();
        itemPage.setRecords(null);
        BeanUtil.copyProperties(itemPage,newPage);
        newPage.setRecords(listItemVOS);
        return R.ok(newPage);
    }

    /**
     *
     * @param goodsSpecsQueryDTO
     * @return
     */
    @ApiOperation(value = "获取商品规格详情")
    @PostMapping("showProductSpecs")
    public R<ItemSpecsVO> showProductSpecs(@RequestBody GoodsSpecsQueryDTO goodsSpecsQueryDTO) {
        ItemSpecsVO isvo = new ItemSpecsVO();
        if(StrUtil.isEmpty(goodsSpecsQueryDTO.getSpecs())){
            Item item = itemService.selectOne(new EntityWrapper<Item>().eq("item_number", goodsSpecsQueryDTO.getItemNumber()));
            isvo.setImages(item!=null&&item.getImage()!=null ?item.getImage().split(","):null);
            isvo.setVipDiscount(item!=null&&item.getVipDiscount()!=null?item.getVipDiscount():null);
            isvo.setPrice(item!=null&&item.getPrice()!=null?item.getPrice():null);
            isvo.setStock(item!=null&&item.getNum()!=null?item.getNum():null);
            isvo.setItemNo(goodsSpecsQueryDTO.getItemNumber());
            return R.ok(isvo);
        }
        ItemSpecs is = iItemSpecsService.selectOne(new EntityWrapper<ItemSpecs>().eq("item_no",goodsSpecsQueryDTO.getItemNumber()).eq("specs_values",goodsSpecsQueryDTO.getSpecs()));


        if(is!=null){
           BeanUtil.copyProperties(is,isvo);
           isvo.setImages(is.getImage().split(","));
        }
        return R.ok(isvo);
    }

}
