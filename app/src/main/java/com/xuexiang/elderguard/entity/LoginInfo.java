package com.xuexiang.elderguard.entity;

public class LoginInfo {
    private EgUser egUser;

    private String token;

    public EgUser getEgUser() {
        return egUser;
    }

    public LoginInfo setEgUser(EgUser egUser) {
        this.egUser = egUser;
        return this;
    }

    public String getToken() {
        return token;
    }

    public LoginInfo setToken(String token) {
        this.token = token;
        return this;
    }
}
