/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.elderguard.manager;

import com.alibaba.fastjson.JSONObject;
import com.xuexiang.elderguard.entity.EgUser;
import com.xuexiang.elderguard.utils.TokenUtils;

/**
 * token管理器
 *
 * @author xuexiang
 * @since 2018/8/6 上午11:45
 */
public class TokenManager {

    private static TokenManager sInstance;

    private String mToken = "";

    private String mSign = "";

    /**
     * 当前登录的用户
     */
    private EgUser mLoginUser;

    private TokenManager() {

    }

    public static TokenManager getInstance() {
        if (sInstance == null) {
            synchronized (TokenManager.class) {
                if (sInstance == null) {
                    sInstance = new TokenManager();
                }
            }
        }
        return sInstance;
    }

    public TokenManager setToken(String token) {
        mToken = token;
        return this;
    }

    public String getToken() {
        return mToken;
    }

    public TokenManager setSign(String sign) {
        mSign = sign;
        return this;
    }

    public String getSign() {
        return mSign;
    }

    public EgUser getLoginUser() {
        if (mLoginUser == null)
            return JSONObject.parseObject(TokenUtils.getEgUser(), EgUser.class);;
        return mLoginUser;
    }

    public boolean isUserLogined() {
        return mLoginUser != null;
    }

    public TokenManager setLoginUser(EgUser loginUser) {
        mLoginUser = loginUser;
        return this;
    }
}
