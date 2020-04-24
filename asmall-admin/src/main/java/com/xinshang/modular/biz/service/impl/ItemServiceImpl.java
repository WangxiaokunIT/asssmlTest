package com.xinshang.modular.biz.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.core.shiro.ShiroKit;
import com.xinshang.core.shiro.ShiroUser;
import com.xinshang.core.support.DateTime;
import com.xinshang.modular.biz.dao.BannerMapper;
import com.xinshang.modular.biz.dao.PostageMapper;
import com.xinshang.modular.biz.dto.ItemDTO;
import com.xinshang.modular.biz.model.Banner;
import com.xinshang.modular.biz.model.Item;
import com.xinshang.modular.biz.dao.ItemMapper;
import com.xinshang.modular.biz.model.ItemSpecs;
import com.xinshang.modular.biz.model.Postage;
import com.xinshang.modular.biz.service.IItemService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.modular.biz.service.IItemSpecsService;
import com.xinshang.modular.biz.vo.ItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author sunhao
 * @since 2019-10-17
 */
@Service
@Slf4j
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements IItemService {

    @Autowired
    private  ItemMapper itemMapper;

    @Autowired
    private BannerMapper bannerMapper;

    @Autowired
    private PostageMapper postageMapper;

    @Autowired
    private IItemSpecsService iItemSpecsService;

    @Override
    public boolean alertItemState(Long id, Integer state, Integer bannerState) {

        Item item = itemMapper.selectById(id);
        item.setStatus(state);
        item.setUpdated(new Date());
        //上架下架商品，需要重新审核
        item.setAuditStatus(0);
        //更新首页轮播图为不显示状态
        List<Banner> bannerList = bannerMapper.selectList(new EntityWrapper<Banner>().eq("item_number", item.getItemNumber()));
        for (Banner banner:bannerList){
            banner.setState(bannerState);
            bannerMapper.update(banner,new EntityWrapper<Banner>().eq("item_number",item.getItemNumber()));
        }
        if (itemMapper.updateById(item) != 1) {
            return false;
        }

        /* //删除购物车缓存信息
        deleteAllCartRedisByItemId(id);*/
        return true;
    }

    @Override
    public List<ItemVO> showItemNum() {
        return itemMapper.showItemNum();
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public void add(Item item) {
        log.info("准备添加商品:{}",item);
        //规格属性
        String attrInfo = item.getAttrInfo();
        //规格信息
        String sourceSpecsValue = item.getSpecsValue();
        item.insert();
        if(StrUtil.isNotEmpty(attrInfo) && StrUtil.isNotEmpty(sourceSpecsValue)){
            String[] attrInfos = attrInfo.split("\\|");
            //规格属性信息
            String[] specsValues = sourceSpecsValue.split("\\|");
            for (int i = 0; i < specsValues.length; i++) {
                String[] specsValue = specsValues[i].split(",");
                String[] newSpecsValues = new String[attrInfos.length];
                //调用copy方法
                System.arraycopy(specsValue,0,newSpecsValues,0,attrInfos.length);
                ItemSpecs is = new ItemSpecs();
                is.setSpecsValues(ArrayUtil.join(newSpecsValues, ","));
                is.setItemId(item.getId());
                is.setItemNo(item.getItemNumber());
                is.setPrice(new BigDecimal(specsValue[attrInfos.length]));
                is.setVipDiscount(new BigDecimal(specsValue[attrInfos.length+1]));
                is.setStock(Integer.valueOf(specsValue[attrInfos.length+2]));
                is.setStockWarning(Integer.valueOf(specsValue[attrInfos.length+3]));
                is.setSpecsNo(specsValue[attrInfos.length+4]);
                is.setImage(specsValue[attrInfos.length+5]);
                is.insert();
            }
        }
        //添加邮费信息
        String postageArea = item.getPostageArea();
        String postageFee = StrUtil.isEmpty(item.getPostageFee())?"0":item.getPostageFee();
        String[] splitArea = postageArea.split(":");
        String[] splitFee = postageFee.split(":");
        for (int i = 0; i < splitArea.length; i++) {
            if (i == 0) {
                continue;
            }
            Postage postage = new Postage();
            ShiroUser user = ShiroKit.getUser();
            postage.setCreated(new DateTime());
            postage.setUserid(user.id);
            postage.setArea(splitArea[i]);
            postage.setFreight(new BigDecimal(splitFee[i]));
            postage.setItemNumber(item.getItemNumber());
            postageMapper.insert(postage);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Item item) {
        log.info("准备添加商品:{}",item);
        String attrInfo = item.getAttrInfo();
        String sourceSpecsValue = item.getSpecsValue();
        updateById(item);
        //清空原有规格
        iItemSpecsService.delete(new EntityWrapper<ItemSpecs>().eq("item_no",item.getItemNumber()));
        //插入新的规格
        if(StrUtil.isNotEmpty(attrInfo) && StrUtil.isNotEmpty(sourceSpecsValue)){
            String[] attrInfos = attrInfo.split("\\|");
            //规格属性信息
            String[] specsValues = sourceSpecsValue.split("\\|");
            for (int i = 0; i < specsValues.length; i++) {
                String[] specsValue = specsValues[i].split(",");
                String[] newSpecsValues = new String[attrInfos.length];
                //调用copy方法
                System.arraycopy(specsValue,0,newSpecsValues,0,attrInfos.length);
                ItemSpecs is = new ItemSpecs();
                is.setSpecsValues(ArrayUtil.join(newSpecsValues, ","));
                is.setItemId(item.getId());
                is.setItemNo(item.getItemNumber());
                is.setPrice(new BigDecimal(specsValue[attrInfos.length]));
                is.setVipDiscount(new BigDecimal(specsValue[attrInfos.length+1]));
                is.setStock(Integer.valueOf(specsValue[attrInfos.length+2]));
                is.setStockWarning(Integer.valueOf(specsValue[attrInfos.length+3]));
                is.setSpecsNo(specsValue[attrInfos.length+4]);
                is.setImage(specsValue[attrInfos.length+5]);
                is.insert();
            }
        }
        //修改运费
        String postageArea = item.getPostageArea();
        String postageFee = StrUtil.isEmpty(item.getPostageFee())?"0":item.getPostageFee();

        String[] splitArea = postageArea.split(":");
        String[] splitFee = postageFee.split(":");
        //删除旧的运费规则
        postageMapper.delete(new EntityWrapper<Postage>().eq("item_number",item.getItemNumber()));
        for (int i = 0; i < splitArea.length; i++) {
            if (i == 0) {
                continue;
            }
            Postage postage = new Postage();
            ShiroUser user = ShiroKit.getUser();
            postage.setCreated(new DateTime());
            postage.setUserid(user.id);
            postage.setArea(splitArea[i]);
            postage.setFreight(new BigDecimal(splitFee[i]));
            postage.setItemNumber(item.getItemNumber());
            postageMapper.insert(postage);
        }

    }

    @Override
    public Page<Item> selectItemPage(Page<Item> page, ItemDTO item) {
       List<Item> list =  itemMapper.selectItemPage(page,item);
        page.setRecords(list);
        return page;
    }
}
