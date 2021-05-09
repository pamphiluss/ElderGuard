package com.xuexiang.elderguard.core.http.api;

import com.xuexiang.elderguard.entity.LoginInfo;
import com.xuexiang.xhttp2.annotation.NetMethod;

import io.reactivex.Observable;

public class ApiService {


    public interface IAuthorization {


        @NetMethod(parameterNames = {"phone", "password"}, url = "/authorization/login/", accessToken = false)
        Observable<LoginInfo> login(String phone, String password);
    }

}
