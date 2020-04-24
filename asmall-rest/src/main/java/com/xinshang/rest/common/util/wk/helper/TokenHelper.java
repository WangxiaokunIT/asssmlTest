package com.xinshang.rest.common.util.wk.helper;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.xinshang.rest.common.util.wk.helper.comm.HttpHelper;
import com.xinshang.rest.common.util.wk.helper.comm.JSONHelper;
import com.xinshang.rest.common.util.wk.helper.comm.LocalCacheHelper;
import com.xinshang.rest.common.util.wk.helper.constant.CacheKeyConstant;
import com.xinshang.rest.common.util.wk.helper.constant.ConfigConstant;
import com.xinshang.rest.common.util.wk.helper.enums.RequestType;
import com.xinshang.rest.common.util.wk.helper.exception.DefineException;

/**
 * @description 鉴权认证 辅助类
 * @author 宫清
 * @date 2019年7月19日 上午11:54:28
 * @since JDK1.7
 */
public class TokenHelper {

	/**
	 * 不允许外部创建实例
	 */
	private TokenHelper() {
	}

	
	
	// -------------------------------------------公有方法start-------------------------------------------------------

	/**
	 * @description 获取token
	 * 
	 *              实际使用中，可获取token后，放在redis缓存中，如果是非分布式架构，也可使用guava的LoadingCache或者自己构建本地缓存来存储该token，
	 *              缓存存储时，需要注意缓存失效截止时间expiresIn
	 * 
	 * @date 2019年7月19日 上午11:55:57
	 * @author 宫清
	 * @throws DefineException
	 */
	public static void getTokenData() throws DefineException {
		Object o = LocalCacheHelper.get(CacheKeyConstant.EXPIRESIN);
		if(o != null){
			/**
			 * 获取过期时间：如果过期则重新获取token
			 */
			Long expiresin = Long.valueOf(String.valueOf(o));
			Long now = DateUtil.current(false);
			if(now >=expiresin){
				refreshToken(LocalCacheHelper.get(CacheKeyConstant.REFRESH_TOKEN).toString());
			}
		}else{
			JSONObject json = HttpHelper.doCommHttp(RequestType.GET,
					ConfigConstant.getToken_URL(ConfigConstant.PROJECT_ID, ConfigConstant.PROJECT_SECRET), null);
			json = JSONHelper.castDataJson(json, JSONObject.class);

			// 模拟存放本地缓存
			toLocalCache(json);
		}
	}

	/**
	 * @description 刷新token
	 * 
	 *              刷新token后，新旧token共存时间为5分钟，实际使用中，若使用redis或者guava的LoadingCache或自建本地缓存的方式来存储token，请注意刷新缓存时间
	 * 
	 * @param refreshToken {@link String} 之前获取token时返回的refreshToken
	 * @date 2019年7月19日 上午11:55:57
	 * @author 宫清
	 * @throws DefineException
	 */
	public static JSONObject refreshToken(String refreshToken) throws DefineException {
		JSONObject json = HttpHelper.doCommHttp(RequestType.GET,
				ConfigConstant.refreshToken_URL(ConfigConstant.PROJECT_ID, refreshToken), null);
		json = JSONHelper.castDataJson(json, JSONObject.class);

		// 模拟存放本地缓存
		toLocalCache(json);

		return json;
	}
	
	// -------------------------------------------公有方法end---------------------------------------------------------

	// -------------------------------------------私有方法start-------------------------------------------------------

	/**
	 * @description 模拟存放本地缓存
	 *
	 * @param json
	 * @author 宫清
	 * @date 2019年7月20日 下午4:04:08
	 */
	private static void toLocalCache(JSONObject json) {
		LocalCacheHelper.put(CacheKeyConstant.TOKEN, json.getString("token"));
		LocalCacheHelper.put(CacheKeyConstant.EXPIRESIN, Long.valueOf(json.getString("expiresIn")));
		LocalCacheHelper.put(CacheKeyConstant.REFRESH_TOKEN, json.getString("refreshToken"));
	}

	// -------------------------------------------私有方法end---------------------------------------------------------
}
