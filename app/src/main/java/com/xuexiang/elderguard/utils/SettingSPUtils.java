package com.xuexiang.elderguard.utils;

import android.content.Context;

import com.xuexiang.elderguard.R;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.data.BaseSPUtil;


public class SettingSPUtils extends BaseSPUtil {

    private static SettingSPUtils sInstance;

    private SettingSPUtils(Context context) {
        super(context);
    }

    public static SettingSPUtils getInstance() {
        if (sInstance == null) {
            synchronized (SettingSPUtils.class) {
                if (sInstance == null) {
                    sInstance = new SettingSPUtils(XUtil.getContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取服务器地址
     *
     * @return
     */
    public String getApiURL() {
        return getString(getString(R.string.service_api_key), getString(R.string.default_service_api));
    }

    /**
     * 获取服务器地址
     *
     * @return
     */
    public boolean setApiURL(String apiUrl) {
        return putString(getString(R.string.service_api_key), apiUrl);
    }


    private final String KEY_SELECT_PUSH_PLATFORM = "key_select_push_platform";


    public int getPushPlatformCode() {
        return getInt(KEY_SELECT_PUSH_PLATFORM, 0);
    }

    public void setPushPlatformCode(int platformCode) {
        putInt(KEY_SELECT_PUSH_PLATFORM, platformCode);
    }

}
