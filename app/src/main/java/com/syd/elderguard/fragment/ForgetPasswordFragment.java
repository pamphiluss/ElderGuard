package com.syd.elderguard.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.syd.elderguard.activity.LoginActivity;
import com.syd.elderguard.core.BaseFragment;
import com.syd.elderguard.core.http.api.ApiService;
import com.syd.elderguard.core.http.subscriber.TipRequestSubscriber;
import com.syd.elderguard.core.webview.AgentWebActivity;
import com.syd.elderguard.entity.LoginInfo;
import com.syd.elderguard.utils.SettingUtils;
import com.syd.elderguard.utils.Utils;
import com.syd.elderguard.utils.XToastUtils;
import com.syd.elderguard.R;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.XHttpProxy;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xhttp2.subsciber.ProgressLoadingSubscriber;
import com.xuexiang.xhttp2.subsciber.impl.IProgressLoader;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

import static com.syd.elderguard.core.webview.AgentWebFragment.KEY_URL;


@Page(anim = CoreAnim.none)
public class ForgetPasswordFragment extends BaseFragment {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_phone_number_fg)
    MaterialEditText etPhoneNumberFg;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_password_fg)
    MaterialEditText etPasswordFg;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_password_fg_ag)
    MaterialEditText etPasswordFgAg;
    private IProgressLoader mIProgressLoader;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_forger_password;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle()
                .setImmersive(true);
        titleBar.setBackgroundColor(Color.TRANSPARENT);
        titleBar.setTitle("");
        titleBar.setLeftImageDrawable(ResUtils.getVectorDrawable(getContext(), R.drawable.ic_login_close));
        titleBar.setActionTextColor(ThemeUtils.resolveColor(Objects.requireNonNull(getContext()), R.attr.colorAccent));
        return titleBar;
    }

    @Override
    protected void initViews() {

        //隐私政策弹窗
        if (!SettingUtils.isAgreePrivacy()) {
            Utils.showPrivacyDialog(getContext(), (dialog, which) -> {
                dialog.dismiss();
                SettingUtils.setIsAgreePrivacy(true);
            });
        }
    }

    @SuppressLint("NonConstantResourceId")
    @SingleClick
    @OnClick({R.id.btn_fg, R.id.tv_user_protocol, R.id.tv_privacy_protocol})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getContext(), AgentWebActivity.class);
        switch (view.getId()) {
            case R.id.btn_fg:
                if (etPhoneNumberFg.validate()) {
                    if (etPasswordFg.validate()) {
                        judgeCorrect(etPhoneNumberFg.getEditValue(), etPasswordFg.getEditValue());
                    }
                }
                break;
            case R.id.tv_user_protocol:
                intent.putExtra(KEY_URL, "file:///android_asset/agreement.html");
                Objects.requireNonNull(getContext()).startActivity(intent);
                break;
            case R.id.tv_privacy_protocol:
                intent.putExtra(KEY_URL, "file:///android_asset/privacy.html");
                Objects.requireNonNull(getContext()).startActivity(intent);
                break;
            default:
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @SuppressLint("CheckResult")

    private void judgeCorrect(String phone, String password) {
        if (StringUtils.isEmpty(phone)) {
            ToastUtils.toast("用户名不能为空！");
        } else if (StringUtils.isEmpty(password)) {
            ToastUtils.toast("密码不能为空！");
        }
        final boolean[] flag = {false};
        XHttpProxy.proxy(ApiService.IAuthorization.class)
                .login(phone, password)
                .subscribeWith(new ProgressLoadingSubscriber<LoginInfo>(mIProgressLoader) {

                    @Override
                    protected void onSuccess(LoginInfo loginInfo) {
                        flag[0] = loginInfo.getEgUser() != null;
                        if (flag[0] && etPasswordFgAg.validate()) {
                            updateUser(etPhoneNumberFg.getEditValue(), etPasswordFgAg.getEditValue());
                            ActivityUtils.startActivity(LoginActivity.class);
                        } else XToastUtils.info("密码或者账号错误");
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtils.toast("访问错误");
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void updateUser(String phone, String password) {
        if (StringUtils.isEmpty(phone)) {
            ToastUtils.toast("用户名不能为空！");
        } else if (StringUtils.isEmpty(password)) {
            ToastUtils.toast("密码不能为空！");
        }
        Observable<Boolean> observable = XHttp.post("/user/changerUserPassword")
                .params("phone", phone)
                .params("password", password)
                .syncRequest(false)
                .onMainThread(true)
                .execute(Boolean.class);
        observable.subscribeWith(new TipRequestSubscriber<Boolean>() {
            @SuppressLint("CheckResult")
            @Override
            protected void onSuccess(Boolean response) {
                if (response) {
                    XToastUtils.info("更改成功");
                    ActivityUtils.startActivity(LoginActivity.class);

                } else {
                    XToastUtils.info("更改失败");
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

