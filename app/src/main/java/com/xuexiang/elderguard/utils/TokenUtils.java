/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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
 *
 */

package com.xuexiang.elderguard.utils;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.umeng.analytics.MobclickAgent;
import com.xuexiang.elderguard.activity.LoginActivity;
import com.xuexiang.elderguard.entity.EgUser;
import com.xuexiang.elderguard.entity.LoginInfo;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.common.StringUtils;

import org.json.JSONObject;

public final class TokenUtils {

    private static String sToken;
    private static String egUserJson;

    private static final String KEY_TOKEN = "com.syd.utils.KEY_TOKEN";
    private static final String KEY_USER = "com.syd.utils.USER";

    private TokenUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static final String KEY_PROFILE_CHANNEL = "github";

    /**
     * 初始化Token信息
     */
    public static void init(Context context) {
        MMKVUtils.init(context);
        sToken = MMKVUtils.getString(KEY_TOKEN, "");
    }

    public static void setToken(String token) {
        sToken = token;
        MMKVUtils.put(KEY_TOKEN, token);
    }

    public static void setEgUser(EgUser egUser) {
        egUserJson = JSON.toJSONString(egUser);
        MMKVUtils.put(KEY_USER, egUserJson);
    }

    public static void clearToken() {
        sToken = null;
        MMKVUtils.remove(KEY_TOKEN);
    }

    public static void clearEgUser() {
        egUserJson= "";
        MMKVUtils.remove(KEY_USER);
    }

    public static String getToken() {
        return sToken;
    }

    public static String getEgUser() {
        return MMKVUtils.getString(KEY_USER, "");
    }


    public static boolean hasToken() {
        System.out.println(MMKVUtils.getString(KEY_TOKEN,"111"));
        return MMKVUtils.containsKey(KEY_TOKEN);
    }

    public static boolean hasUser() {
        return MMKVUtils.containsKey(KEY_USER);
    }


    /**
     * 处理登录成功的事件
     *
     * @param token 账户信息
     */
    public static boolean handleLoginSuccess(String token, LoginInfo loginInfo) {
        if (!StringUtils.isEmpty(token)) {
            XToastUtils.success("登录成功！");
            MobclickAgent.onProfileSignIn(KEY_PROFILE_CHANNEL, token);
            setToken(token);
            setEgUser(loginInfo.getEgUser());
            return true;
        } else {
            XToastUtils.error("登录失败！");
            return false;
        }
    }

    /**
     * 处理登出的事件
     */
    public static void handleLogoutSuccess() {
        MobclickAgent.onProfileSignOff();
        //登出时，清除账号信息
        clearToken();
        XToastUtils.success("登出成功！");
        //跳转到登录页
        ActivityUtils.startActivity(LoginActivity.class);
    }

}
