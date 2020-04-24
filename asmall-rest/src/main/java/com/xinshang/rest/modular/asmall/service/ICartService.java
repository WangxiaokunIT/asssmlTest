package com.xinshang.rest.modular.asmall.service;

import com.xinshang.rest.common.util.R;
import com.xinshang.rest.modular.asmall.model.Cart;

/**
 * <p>
 * 购物车信息
 * </p>
 *
 * @author wangxiaokun
 * @since 2019-10-17
 */
public interface ICartService {
    R addCart(Cart cart);

    R delCart(Cart cart);

    R getCartList(Long userId);

    R editCart(Cart cart);
}
