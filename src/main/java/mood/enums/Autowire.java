/*
 * Copyright 2002-2009 the original author or authors.
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

package mood.enums;
/**
 * <p>Description: mood-vertx-enums Autowire</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * @Description: 用于自动setter注入
 * @version: 1.0
 */
public enum Autowire {

	/**
	 * 默认
	 */
	NO(0),

	/**
	 * 通过名字注入
	 */
	BY_NAME(1),

	/**
	 * 通过类型注入
	 */
	BY_TYPE(2);


	private final int value;


	Autowire(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public boolean isAutowire() {
		return (this == BY_NAME || this == BY_TYPE);
	}

}
