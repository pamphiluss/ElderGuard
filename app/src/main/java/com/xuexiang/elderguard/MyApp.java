package com.xuexiang.elderguard;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.xuexiang.elderguard.utils.SettingSPUtils;
import com.xuexiang.elderguard.utils.sdkinit.ANRWatchDogInit;
import com.xuexiang.elderguard.utils.sdkinit.UMengInit;
import com.xuexiang.elderguard.utils.sdkinit.XBasicLibInit;
import com.xuexiang.elderguard.utils.sdkinit.XUpdateInit;
import com.xuexiang.xhttp2.XHttpSDK;


public class MyApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //解决4.x运行崩溃的问题
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLibs();
    }

    /**
     * 初始化基础库
     */
    private void initLibs() {
        XBasicLibInit.init(this);

        XUpdateInit.init(this);

        XHttpSDK.init(this);
        XHttpSDK.debug("XHttp");  //需要调试的时候执行
        XHttpSDK.setBaseUrl(SettingSPUtils.getInstance().getApiURL());  //设置网络请求的基础地址
        //运营统计数据运行时不初始化
        if (!MyApp.isDebug()) {
            UMengInit.init(this);
        }

        //ANR监控
        ANRWatchDogInit.init();
    }


    /**
     * @return 当前app是否是调试开发模式
     */
    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }


}
