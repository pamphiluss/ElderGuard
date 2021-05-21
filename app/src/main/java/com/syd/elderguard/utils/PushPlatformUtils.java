package com.syd.elderguard.utils;

import android.app.Application;

import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.core.IPushClient;
import com.xuexiang.xpush.jpush.JPushClient;

public final class PushPlatformUtils {

    private PushPlatformUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化推送平台
     *
     * @param application
     */
    public static void initPushClient(Application application) {
        XPush.init(application, new JPushClient());

    }


    /**
     * 切换推送平台
     *
     * @param platformCode
     */
    public static void switchPushClient(int platformCode) {
        //先注销当前推送平台
        XPush.unRegister();
        //设置新的推送平台
        XPush.setIPushClient(getPushClientByPlatformCode(platformCode));
        //注册推送
        XPush.register();
        SettingSPUtils.getInstance().setPushPlatformCode(platformCode);
    }

    public static IPushClient getPushClientByPlatformCode(int platformCode) {
        return new JPushClient();
    }


}
