
package com.syd.elderguard.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.TextUtils;

import com.mylhyl.circledialog.BaseCircleDialog;
import com.mylhyl.circledialog.CircleDialog;
import com.syd.elderguard.base.CheckedAdapter;
import com.syd.elderguard.core.http.subscriber.TipRequestSubscriber;
import com.syd.elderguard.manager.TokenManager;
import com.syd.elderguard.utils.XToastUtils;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xhttp2.utils.TypeUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageSimpleListFragment;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;


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
                        .setInputCounter(30)
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
                                addPhone(text, TokenManager.getInstance().getLoginUser().getId());
                                return true;
                            }
                        })
                        .show(getFragmentManager());
                break;
            case 1:
                assert getFragmentManager() != null;
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
                        .setInputCounter(30)
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
                                addMail(text, TokenManager.getInstance().getLoginUser().getId());
                                return true;
                            }
                        })
                        .show(getFragmentManager());
                break;
            case 2:
                Observable<List<String>> observable = XHttp.get("/phone/getPhoneById")
                        .params("userId", TokenManager.getInstance().getLoginUser().getId())
                        .syncRequest(false)
                        .onMainThread(true)
                        .execute(TypeUtils.getListType(String.class));

                observable.subscribeWith(new TipRequestSubscriber<List<String>>() {
                    @Override
                    protected void onSuccess(List<String> response) {
                        final String[] objectsM = response.toArray(new String[response.size()]);
                        final CheckedAdapter checkedAdapterR = new CheckedAdapter(Objects.requireNonNull(getContext()), objectsM, true);

                        assert getFragmentManager() != null;
                        new CircleDialog.Builder()
                                // .setTypeface(typeface)
                                .setMaxHeight(0.5f)
                                .configDialog(params -> params.backgroundColorPress = Color.CYAN)
                                .setTitle("号码列表")
                                .setSubTitle("单选")
                                .configItems(params -> params.bottomMargin = 12)
                                .setItems(checkedAdapterR, (parent, view15, position15, id) -> {
                                    checkedAdapterR.toggle(position15, objectsM[position15]);
                                    return false;
                                })
                                .setPositive("确定", v -> {
                                    String phoneNumber = checkedAdapterR.getSaveChecked().toString();
                                    ToastUtils.toast(phoneNumber.substring(3));
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber.substring(2)));
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                })
                                .show(getFragmentManager());
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtils.toast("获取错误");
                    }
                });
                break;
            case 3:
                Observable<List<String>> observables = XHttp.get("/mail/getMailById")
                        .params("userId", TokenManager.getInstance().getLoginUser().getId())
                        .syncRequest(false)
                        .onMainThread(true)
                        .execute(TypeUtils.getListType(String.class));

                observables.subscribeWith(new TipRequestSubscriber<List<String>>() {
                    @Override
                    protected void onSuccess(List<String> response) {
                        final String[] objectsM = response.toArray(new String[response.size()]);
                        final CheckedAdapter checkedAdapterM = new CheckedAdapter(Objects.requireNonNull(getContext()), objectsM, true);

                        assert getFragmentManager() != null;
                        new CircleDialog.Builder()
                                // .setTypeface(typeface)
                                .setMaxHeight(0.5f)
                                .configDialog(params -> params.backgroundColorPress = Color.CYAN)
                                .setTitle("邮件列表")
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
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtils.toast("获取错误");
                    }
                });
                break;
            default:
                break;
        }
    }

    @SuppressLint("CheckResult")
    public void addPhone(String text, int id) {
        if (StringUtils.isEmpty(text)) {
            ToastUtils.toast("手机号不能为空！");
            return;
        }
        Observable<Boolean> observable = XHttp.post("/phone/addPhone")
                .params("phone", text)
                .params("userId", id)
                .syncRequest(false)
                .onMainThread(true)
                .execute(Boolean.class);
        observable.subscribeWith(new TipRequestSubscriber<Boolean>() {
            @SuppressLint("CheckResult")
            @Override
            protected void onSuccess(Boolean response) {
                if (response != null) {
                    XToastUtils.info("添加成功");

                } else {
                    XToastUtils.info("添加失败");
                }
            }

            @SuppressLint("CheckResult")
            @Override
            public void onError(ApiException e) {
                XToastUtils.info("访问错误");
            }
        });
    }


    @SuppressLint("CheckResult")
    public void addMail(String text, int id) {
        if (StringUtils.isEmpty(text)) {
            ToastUtils.toast("邮箱不能为空！");
            return;
        }
        Observable<Boolean> observable = XHttp.post("/mail/addMail")
                .params("mail", text)
                .params("userId", id)
                .syncRequest(false)
                .onMainThread(true)
                .execute(Boolean.class);
        observable.subscribeWith(new TipRequestSubscriber<Boolean>() {
            @SuppressLint("CheckResult")
            @Override
            protected void onSuccess(Boolean response) {
                if (response != null) {
                    XToastUtils.info("添加成功");

                } else {
                    XToastUtils.info("添加失败");
                }
            }

            @SuppressLint("CheckResult")
            @Override
            public void onError(ApiException e) {
                XToastUtils.info("访问错误");
            }
        });
    }


}
