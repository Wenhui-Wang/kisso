/**
 * Copyright (c) 2011-2014, hubin (243194995@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.kisso.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.SSOToken;
import com.baomidou.kisso.Token;
import com.baomidou.kisso.TokenCache;
import com.baomidou.kisso.common.encrypt.AES;
import com.baomidou.kisso.common.encrypt.Encrypt;
import com.baomidou.kisso.exception.KissoException;

/**
 * <p>
 * 反射工具类
 * </p>
 * 
 * @author hubin
 * @Date 2014-6-27
 */
public class ReflectUtil {

	private final static Logger logger = LoggerFactory.getLogger(ReflectUtil.class);

	private static Encrypt encrypt = null;

	private static TokenCache tokenCache = null;

	/**
	 * 反射初始化
	 */
	public static void init() {
		getConfigEncrypt();
		getConfigTokenCache();
	}

	/**
	 * 反射获取自定义Encrypt
	 * 
	 * @return
	 */
	public static Encrypt getConfigEncrypt() {

		if (encrypt != null) {
			return encrypt;
		}

		/**
		 * 判断是否自定义 Encrypt 默认 AES
		 */
		if ("".equals(SSOConfig.getEncryptClass())) {
			encrypt = new AES();
		} else {
			try {
				Class<?> tc = Class.forName(SSOConfig.getEncryptClass());
				try {
					if (tc.newInstance() instanceof Encrypt) {
						encrypt = (Encrypt) tc.newInstance();
					} else {
						throw new KissoException(SSOConfig.getEncryptClass() + " not instanceof Encrypt.");
					}
				} catch (InstantiationException e) {
					logger.error("getConfigEncrypt error: ", e);
				} catch (IllegalAccessException e) {
					logger.error("getConfigEncrypt error: ", e);
				}
			} catch (ClassNotFoundException e) {
				throw new KissoException(SSOConfig.getEncryptClass() + " not found.", e);
			}
		}
		return encrypt;
	}

	/**
	 * 反射获取自定义Token
	 * 
	 * @return
	 */
	public static Token getConfigToken() {
		/**
		 * 判断是否自定义 Token 默认 SSOToken
		 */
		Token token = null;
		if ("".equals(SSOConfig.getTokenClass())) {
			token = new SSOToken();
		} else {
			try {
				Class<?> tc = Class.forName(SSOConfig.getTokenClass());
				try {
					if (tc.newInstance() instanceof Token) {
						token = (Token) tc.newInstance();
					} else {
						throw new KissoException(SSOConfig.getTokenClass() + " not instanceof Token.");
					}
				} catch (InstantiationException e) {
					logger.error("getConfigEncrypt error: ", e);
				} catch (IllegalAccessException e) {
					logger.error("getConfigEncrypt error: ", e);
				}
			} catch (ClassNotFoundException e) {
				throw new KissoException(SSOConfig.getEncryptClass() + " not found.", e);
			}
		}
		return token;
	}

	/**
	 * 反射获取自定义TokenCache
	 * 
	 * @return
	 */
	public static TokenCache getConfigTokenCache() {

		if (tokenCache != null) {
			return tokenCache;
		}

		/**
		 * 反射获得缓存类
		 */
		String cacheClass = SSOConfig.getTokenCacheClass();
		if (!"".equals(cacheClass)) {
			try {
				Class<?> tc = Class.forName(cacheClass);
				try {
					if (tc.newInstance() instanceof TokenCache) {
						tokenCache = (TokenCache) tc.newInstance();
					} else {
						throw new KissoException(SSOConfig.getTokenCacheClass() + " not instanceof TokenCache.");
					}
				} catch (InstantiationException e) {
					logger.error("getConfigEncrypt error: ", e);
				} catch (IllegalAccessException e) {
					logger.error("getConfigEncrypt error: ", e);
				}
			} catch (ClassNotFoundException e) {
				throw new KissoException(SSOConfig.getEncryptClass() + " not found.", e);
			}
		}
		
		return tokenCache;
	}
}