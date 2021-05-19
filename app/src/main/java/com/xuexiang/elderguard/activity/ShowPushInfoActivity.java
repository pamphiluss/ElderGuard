package com.xuexiang.elderguard.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.xuexiang.elderguard.core.BaseActivity;
import com.xuexiang.elderguard.fragment.ShowPushInfoFragment;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xutil.display.Colors;


public class ShowPushInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = new Bundle();
        String KEY_PARAM_MSG = "key_param_msg";
        bundle.putString(KEY_PARAM_MSG, getIntent().getStringExtra("key_param_msg"));
        openPage(ShowPushInfoFragment.class, bundle);
    }

    @Override
    protected boolean isSupportSlideBack() {
        return false;
    }

    @Override
    protected void initStatusBarStyle() {
        StatusBarUtils.initStatusBarStyle(this, false, Colors.WHITE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return KeyboardUtils.onDisableBackKeyDown(keyCode) && super.onKeyDown(keyCode, event);
    }

}
