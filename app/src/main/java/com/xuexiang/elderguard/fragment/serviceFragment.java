
package com.xuexiang.elderguard.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.TextUtils;

import com.mylhyl.circledialog.BaseCircleDialog;
import com.mylhyl.circledialog.CircleDialog;
import com.xuexiang.elderguard.adapter.base.CheckedAdapter;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageSimpleListFragment;

import java.util.List;


@Page(name = "快捷服务")
public class serviceFragment extends XPageSimpleListFragment {
    private BaseCircleDialog dialogFragment;

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("添加手机号");
        lists.add("添加邮箱");
        lists.add("快速呼叫");
        lists.add("快速邮件");
        return lists;
    }

    @Override
    protected void onItemClick(int position) {
        switch (position) {
            case 0:
                dialogFragment = new CircleDialog.Builder()
                        //.setTypeface(typeface)
                        .setCanceledOnTouchOutside(false)
                        .setCancelable(true)
                        .setTitle("添加手机号")
                        .setSubTitle("请输入要添加手机号码")
                        .setInputHint("请输入手机号码")
                        .setInputHeight(30)
//                        .setInputShowKeyboard(true)
                        .setInputEmoji(true)
                        .setInputCounter(10)
//                        .setInputCounter(20, (maxLen, currentLen) -> maxLen - currentLen + "/" + maxLen)
                        .configInput(params -> {

                            params.styleText = Typeface.BOLD;
                        })
                        .setNegative("取消", null)
                        .setPositiveInput("确定", (text, v) -> {
                            if (TextUtils.isEmpty(text)) {
                                v.setError("请输入内容");
                                return false;
                            } else {
                                return true;
                            }
                        })
                        .show(getFragmentManager());
                break;
            case 1:
                dialogFragment = new CircleDialog.Builder()
                        //.setTypeface(typeface)
                        .setCanceledOnTouchOutside(false)
                        .setCancelable(true)
                        .setTitle("添加邮箱")
                        .setSubTitle("请输入要添加邮箱地址")
                        .setInputHint("请输入邮箱")
                        .setInputHeight(30)
//                        .setInputShowKeyboard(true)
                        .setInputEmoji(true)
                        .setInputCounter(10)
//                        .setInputCounter(20, (maxLen, currentLen) -> maxLen - currentLen + "/" + maxLen)
                        .configInput(params -> {

                            params.styleText = Typeface.BOLD;
                        })
                        .setNegative("取消", null)
                        .setPositiveInput("确定", (text, v) -> {
                            if (TextUtils.isEmpty(text)) {
                                v.setError("请输入内容");
                                return false;
                            } else {
                                return true;
                            }
                        })
                        .show(getFragmentManager());
                break;
            case 2:
                final String[] objectsR = {"13365156108", "17606107109", "13366786577"};
                final CheckedAdapter checkedAdapterR = new CheckedAdapter(getContext(), objectsR, true);

                new CircleDialog.Builder()
                        // .setTypeface(typeface)
                        .setMaxHeight(0.5f)
                        .configDialog(params -> params.backgroundColorPress = Color.CYAN)
                        .setTitle("号码列表")
                        .setSubTitle("单选")
                        .configItems(params -> params.bottomMargin = 12)
                        .setItems(checkedAdapterR, (parent, view15, position15, id) -> {
                            checkedAdapterR.toggle(position15, objectsR[position15]);
                            return false;
                        })
                        .setPositive("确定", v -> {
                            String phoneNumber = checkedAdapterR.getSaveChecked().toString();
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        })
                        .show(getFragmentManager());
                break;
            case 3:
                final String[] objectsM = {"1349619363@qq.com", "1349619360@qq.com", "mlcc8143@126.com"};
                final CheckedAdapter checkedAdapterM = new CheckedAdapter(getContext(), objectsM, true);

                new CircleDialog.Builder()
                        // .setTypeface(typeface)
                        .setMaxHeight(0.5f)
                        .configDialog(params -> params.backgroundColorPress = Color.CYAN)
                        .setTitle("号码列表")
                        .setSubTitle("单选")
                        .configItems(params -> params.bottomMargin = 12)
                        .setItems(checkedAdapterM, (parent, view15, position15, id) -> {
                            checkedAdapterM.toggle(position15, objectsM[position15]);
                            return false;
                        })
                        .setPositive("确定", v -> {
                            String emailNumber = checkedAdapterM.getSaveChecked().toString();
                            Uri uri = Uri.parse("mailto:" + emailNumber.substring(3, emailNumber.length() - 1));
                            Intent data = new Intent(Intent.ACTION_SENDTO);
                            data.setData(uri);
                            data.putExtra(Intent.EXTRA_SUBJECT, "老人卫士");
                            data.putExtra(Intent.EXTRA_TEXT, "求救！！！");
                            startActivity(Intent.createChooser(data, "请选择邮件类应用"));
                        })
                        .show(getFragmentManager());
                break;
            default:
                break;
        }
    }
}
