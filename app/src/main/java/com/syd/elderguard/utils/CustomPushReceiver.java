
package com.syd.elderguard.utils;

import android.content.Context;
import android.content.Intent;

import com.syd.elderguard.activity.ShowPushInfoActivity;
import com.xuexiang.xpush.core.receiver.impl.XPushReceiver;
import com.xuexiang.xpush.entity.XPushCommand;
import com.xuexiang.xpush.entity.XPushMsg;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.app.IntentUtils;
import com.xuexiang.xutil.tip.ToastUtils;


public class CustomPushReceiver extends XPushReceiver {

    @Override
    public void onNotificationClick(Context context, XPushMsg msg) {
        super.onNotificationClick(context, msg);
        //打开自定义的Activity
        Intent intent = IntentUtils.getIntent(context, ShowPushInfoActivity.class, null, true);
        intent.putExtra("key_param_msg", msg.getKeyValue().get("filename"));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityUtils.startActivity(intent);

    }

    @Override
    public void onCommandResult(Context context, XPushCommand command) {
        super.onCommandResult(context, command);
        ToastUtils.toast(command.getDescription());
    }

}
