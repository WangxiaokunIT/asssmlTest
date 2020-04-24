package com.xinshang.rest.modular.asmall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.common.util.RedisUtil;
import com.xinshang.rest.modular.asmall.dto.ItemAndSpecsDTO;
import com.xinshang.rest.modular.asmall.model.*;
import com.xinshang.rest.modular.asmall.service.ICartService;
import com.xinshang.rest.modular.asmall.service.IItemService;
import com.xinshang.rest.modular.asmall.service.IMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车服务
 */
@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IItemService iItemService;

    @Autowired
    private IMemberService iMemberService;

    private static String CART_PRE = "CART";

    /**
     * 功能描述: 添加购物车
     *
     * @Param: [cart]
     * @Return: com.xinshang.rest.common.util.R
     * @Auther: wangxiaokun
     * @Date: 2019/10/26 10:51
     * @Description:
     * @Modify:
     */
    @Override
    public R addCart(Cart cart) {
        //客户id
        Long memberId = cart.getMemberId();
        //商品编号
        String itemNumber = cart.getItemNumber();
        //规格编号
        String itemSpecsNo = cart.getItemSpecsNo();

        if(StrUtil.isEmpty(itemSpecsNo)){
            itemSpecsNo="nothingness";
        }
        //购买数量
        int num = cart.getProductNum();

        boolean flag = redisUtil.hHasKey(CART_PRE + ":" + memberId, itemNumber + "_"+itemSpecsNo);

        //如果存在数量相加
        if (flag) {
            String json = (String) redisUtil.hget(CART_PRE + ":" + memberId, itemNumber + "_"+itemSpecsNo);
            CartProduct cartProduct = new Gson().fromJson(json,CartProduct.class);
            cartProduct.setProductNum(cartProduct.getProductNum() + num);
            redisUtil.hset(CART_PRE + ":" + memberId, itemNumber + "_"+itemSpecsNo, new Gson().toJson(cartProduct));
            return R.ok();
        }

        //如果不存在，根据商品id取商品信息
        ItemAndSpecsDTO itemAndSpecsDTO = iItemService.selectByItemNumberOrSpecsNo(itemNumber, itemSpecsNo);
        if(itemAndSpecsDTO==null){
            return R.failed("商品不存在或已下架");
        }
        CartProduct cartProduct= new CartProduct();
        //有此规格
        if(itemAndSpecsDTO.getItemSpecsList().size()>0){
            ItemSpecs itemSpecs = itemAndSpecsDTO.getItemSpecsList().get(0);

            //商品图片
            cartProduct.setProductImg(itemSpecs.getImage().split(",")[0]);
            //vip价格
            cartProduct.setVipDiscount(itemSpecs.getVipDiscount());
            //价格
            cartProduct.setPrice(itemSpecs.getPrice());
            //规格编号
            cartProduct.setItemSpecsNo(itemSpecs.getSpecsNo());
            //规格
            cartProduct.setItemSpecsValues(itemSpecs.getSpecsValues());
        }else{
            //商品图片
            cartProduct.setProductImg(itemAndSpecsDTO.getImage());
            //vip价格
            cartProduct.setVipDiscount(itemAndSpecsDTO.getVipDiscount());
            //价格
            cartProduct.setPrice(itemAndSpecsDTO.getPrice());
        }

        //商品名称
        cartProduct.setProductName(itemAndSpecsDTO.getTitle());
        //添加数量
        cartProduct.setProductNum(num);
        //商品编号
        cartProduct.setItemNumber(itemAndSpecsDTO.getItemNumber());
        //运费
        cartProduct.setFreight(itemAndSpecsDTO.getFreight());

        redisUtil.hset(CART_PRE + ":" + memberId, itemNumber + "_"+itemSpecsNo, new Gson().toJson(cartProduct));

        return R.ok();
    }

    /**
     * 功能描述: 删除购物车
     *
     * @Param: [cart]
     * @Return: com.xinshang.rest.common.util.R
     * @Auther: wangxiaokun
     * @Date: 2019/10/26 12:18
     * @Description:
     * @Modify:
     */
    @Override
    public R delCart(Cart cart) {
        //客户id
        Long memberId = cart.getMemberId();
        //商品编号
        String itemNumber = cart.getItemNumber();
        //规格编号
        String itemSpecsNo = cart.getItemSpecsNo();
        if(StrUtil.isBlank(itemSpecsNo)){
            itemSpecsNo="nothingness";
        }
        redisUtil.hdel(CART_PRE + ":" + memberId, itemNumber + "_"+itemSpecsNo);
        return R.ok();
    }

    /**
     * 功能描述: 获取购物车列表
     *
     * @Param: [userId]
     * @Return: com.xinshang.rest.common.util.R
     * @Auther: wangxiaokun
     * @Date: 2019/10/26 12:18
     * @Description:
     * @Modify:
     */
    @Override
    public R getCartList(Long userId) {
        List<Object> listCart = redisUtil.hvals(CART_PRE + ":" + userId);
        List<CartProduct> list = new ArrayList<>();
        for (Object json : listCart) {
            CartProduct cartProduct = new Gson().fromJson((String) json,CartProduct.class);
            list.add(cartProduct);
        }
        return R.ok(list);
    }

    /**
     * 功能描述: 编辑购物车
     *
     * @Param: [cart]
     * @Return: com.xinshang.rest.common.util.R
     * @Auther: wangxiaokun
     * @Date: 2019/10/26 12:19
     * @Description:
     * @Modify:
     */
    @Override
    public R editCart(Cart cart) {
        //客户id
        Long memberId = cart.getMemberId();
        //商品编号
        String itemNumber = cart.getItemNumber();
        //规格编号
        String itemSpecsNo = cart.getItemSpecsNo();

        int num = cart.getProductNum();

        String json =  (String) redisUtil.hget(CART_PRE + ":" + memberId, itemNumber + "_"+itemSpecsNo);
        if(json==null){
            return R.ok("购物车不存在此产品");
        }
        CartProduct cartProduct = new Gson().fromJson(json,CartProduct.class);
        cartProduct.setProductNum(num);

        redisUtil.hset(CART_PRE + ":" + memberId, itemNumber + "_"+itemSpecsNo, new Gson().toJson(cartProduct));
        return R.ok();
    }
}
