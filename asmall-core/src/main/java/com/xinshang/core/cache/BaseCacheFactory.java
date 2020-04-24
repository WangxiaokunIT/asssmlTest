/**
 * Copyright (c) 2015-2017, Chill Zhuang 庄骞 (smallchill@163.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xinshang.core.cache;


/**
 * 缓存工厂基类
 */
public abstract class BaseCacheFactory implements ICache {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String cacheName, Object key, ILoader iLoader,String args) {
		Object data = get(cacheName, key);
		if (data == null) {
			data = iLoader.load(args);
			put(cacheName, key, data);
		}
		return (T) data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String cacheName, Object key, Class<? extends ILoader> iLoaderClass,String args) {
		Object data = get(cacheName, key);
		if (data == null) {
			try {
				ILoader dataLoader = iLoaderClass.newInstance();
				data = dataLoader.load(args);
				put(cacheName, key, data);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return (T) data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String cacheName, Object key, Class<? extends ILoader> iLoaderClass,String args,String args1,String args2) {
		Object data = get(cacheName, key);
		if (data == null) {
			try {
				ILoader dataLoader = iLoaderClass.newInstance();
				data = dataLoader.load(args,args1,args2);
				put(cacheName, key, data);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return (T) data;
	}

}
