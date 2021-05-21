package com.syd.elderguard.utils;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.xuexiang.xrouter.facade.service.SerializationService;
import com.xuexiang.xrouter.launcher.XRouter;

public final class RouterUtils {

    private RouterUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static Bundle getBundle(String key, Object value) {
        Bundle bundle = new Bundle();
        bundle.putString(key, XRouter.getInstance().navigation(SerializationService.class).object2Json(value));
        return bundle;
    }

    /**
     * 注入依赖
     *
     * @param target
     */
    public static void inject(@NonNull Object target) {
        XRouter.getInstance().inject(target);
    }

}
