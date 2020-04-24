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
 *  数据重载
 * @author Administrator
 * @date  2019年1月10日 18:56:55
 */
public interface ILoader {

	/**
	 * 单参数
	 * @param code
	 * @return
	 */
	Object load(String code);

	/**
	 * 多参数
	 * @param tableName
	 * @param relationColumn
	 * @param targetColumn
	 * @return
	 */
	Object load(String tableName,String relationColumn,String targetColumn);
}
