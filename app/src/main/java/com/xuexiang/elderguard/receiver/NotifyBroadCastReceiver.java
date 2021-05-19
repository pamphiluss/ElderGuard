package com.xuexiang.elderguard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xuexiang.xutil.tip.ToastUtils;


public class NotifyBroadCastReceiver extends BroadcastReceiver {

    public final static String ACTION_SUBMIT = "com.xuexiang.elderguard.receiver.ACTION_SUBMIT";
    public final static String ACTION_CANCEL = "com.xuexiang.elderguard.receiver.ACTION_CANCEL";
    public final static String ACTION_REPLY = "com.xuexiang.elderguard.receiver.ACTION_REPLY";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION_SUBMIT.equals(action)) {
            ToastUtils.toast("ACTION_SUBMIT");
        } else if (ACTION_CANCEL.equals(action)) {
            ToastUtils.toast("ACTION_CANCEL");
        } else if (ACTION_REPLY.equals(action)) {
            ToastUtils.toast("ACTION_REPLY");
        }
    }
}
