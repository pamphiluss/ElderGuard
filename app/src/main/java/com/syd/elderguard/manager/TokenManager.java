package com.syd.elderguard.manager;

import com.alibaba.fastjson.JSONObject;
import com.syd.elderguard.entity.EgUser;
import com.syd.elderguard.utils.TokenUtils;


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
