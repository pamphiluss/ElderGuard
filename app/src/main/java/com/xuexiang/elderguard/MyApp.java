package com.xuexiang.elderguard;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Process;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.xuexiang.elderguard.utils.CustomPushReceiver;
import com.xuexiang.elderguard.utils.PushPlatformUtils;
import com.xuexiang.elderguard.utils.SettingSPUtils;
import com.xuexiang.elderguard.utils.sdkinit.ANRWatchDogInit;
import com.xuexiang.elderguard.utils.sdkinit.UMengInit;
import com.xuexiang.elderguard.utils.sdkinit.XBasicLibInit;
import com.xuexiang.elderguard.utils.sdkinit.XUpdateInit;
import com.xuexiang.keeplive.KeepLive;
import com.xuexiang.keeplive.config.ForegroundNotification;
import com.xuexiang.keeplive.config.ForegroundNotificationClickListener;
import com.xuexiang.keeplive.config.KeepLiveService;
import com.xuexiang.xhttp2.XHttpSDK;
import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.core.dispatcher.impl.Android26PushDispatcherImpl;
import com.xuexiang.xutil.app.AppUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.List;


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
        initKeepLive();

        if (shouldInitPush()) {
            initPush();
        }
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

    /**
     * 初始化保活
     */
    private void initKeepLive() {
        //定义前台服务的默认样式。即标题、描述和图标
        ForegroundNotification notification = new ForegroundNotification("推送服务", "推送服务正在运行中...", R.mipmap.ic_launcher,
                //定义前台服务的通知点击事件
                new ForegroundNotificationClickListener() {
                    @Override
                    public void onNotificationClick(Context context, Intent intent) {
                        ToastUtils.toast("点击了通知");
                        AppUtils.launchApp(getPackageName());
                    }
                })
                //要想不显示通知，可以设置为false，默认是false
                .setIsShow(true);
        //启动保活服务
        KeepLive.startWork(this, KeepLive.RunMode.ENERGY, notification,
                //你需要保活的服务，如socket连接、定时任务等，建议不用匿名内部类的方式在这里写
                new KeepLiveService() {
                    /**
                     * 运行中
                     * 由于服务可能会多次自动启动，该方法可能重复调用
                     */
                    @Override
                    public void onWorking() {
                        Log.e("xuexiang", "onWorking");
                    }

                    /**
                     * 服务终止
                     * 由于服务可能会被多次终止，该方法可能重复调用，需同onWorking配套使用，如注册和注销broadcast
                     */
                    @Override
                    public void onStop() {
                        Log.e("xuexiang", "onStop");
                    }
                }
        );
    }

    /**
     * 初始化推送
     */
    private void initPush() {
        XPush.debug(BuildConfig.DEBUG);

        PushPlatformUtils.initPushClient(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Android8.0静态广播注册失败解决方案一：动态注册
//            XPush.registerPushReceiver(new CustomPushReceiver());

            //Android8.0静态广播注册失败解决方案二：修改发射器
            XPush.setIPushDispatcher(new Android26PushDispatcherImpl(CustomPushReceiver.class));
        }

        XPush.register();
    }

    /**
     * @return 是否需要注册推送
     */
    private boolean shouldInitPush() {
        //只在主进程中注册(注意：umeng推送，除了在主进程中注册，还需要在channel中注册)
        String currentProcessName = getCurrentProcessName();
        String mainProcessName = BuildConfig.APPLICATION_ID;
        return mainProcessName.equals(currentProcessName) || mainProcessName.concat(":channel").equals(currentProcessName);
    }

    /**
     * 获取当前进程名称
     */
    public String getCurrentProcessName() {
        int currentProcessId = Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
            if (runningAppProcess.pid == currentProcessId) {
                return runningAppProcess.processName;
            }
        }
        return null;
    }

}
