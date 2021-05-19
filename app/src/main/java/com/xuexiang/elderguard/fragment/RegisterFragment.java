package com.xuexiang.elderguard.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.xuexiang.elderguard.R;
import com.xuexiang.elderguard.activity.LoginActivity;
import com.xuexiang.elderguard.core.BaseFragment;
import com.xuexiang.elderguard.core.http.subscriber.TipRequestSubscriber;
import com.xuexiang.elderguard.core.webview.AgentWebActivity;
import com.xuexiang.elderguard.utils.SettingUtils;
import com.xuexiang.elderguard.utils.Utils;
import com.xuexiang.elderguard.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xhttp2.subsciber.ProgressDialogLoader;
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

import static com.xuexiang.elderguard.core.webview.AgentWebFragment.KEY_URL;


/**
 * 注册页面
 */
@Page(anim = CoreAnim.none)
public class RegisterFragment extends BaseFragment {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_phone_number_rg)
    MaterialEditText etPhoneNumberRg;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_password_rg)
    MaterialEditText etPasswordRg;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_password_rg_ag)
    MaterialEditText etPasswordRgAg;
    private IProgressLoader mIProgressLoader;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
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
        mIProgressLoader = new ProgressDialogLoader(getContext(), "注册中...");
    }

    @SuppressLint({"NonConstantResourceId", "CheckResult"})
    @SingleClick
    @OnClick({R.id.btn_register, R.id.tv_user_protocol, R.id.tv_privacy_protocol})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getContext(), AgentWebActivity.class);
        switch (view.getId()) {
            case R.id.btn_register:
                if (etPhoneNumberRg.validate()) {
                    if (etPasswordRg.validate()) {
                        if (etPasswordRg.getEditValue().equals(etPasswordRgAg.getEditValue())) {
                            Observable<Boolean> observable = XHttp.post("/user/getPhone")
                                    .params("phone", etPhoneNumberRg.getEditValue())
                                    .syncRequest(false)
                                    .onMainThread(true)
                                    .execute(Boolean.class);
                            observable.subscribeWith(new TipRequestSubscriber<Boolean>() {
                                @SuppressLint("CheckResult")
                                @Override
                                protected void onSuccess(Boolean response) {
                                    if (response) {
                                        RegisterByPassWord(etPhoneNumberRg.getEditValue(), etPasswordRg.getEditValue());

                                    } else {
                                        XToastUtils.info("手机号重复");
                                    }
                                }

                                @SuppressLint("CheckResult")
                                @Override
                                public void onError(ApiException e) {
                                    XToastUtils.info("访问错误");
                                }
                            });


                        } else XToastUtils.info("前后密码不一致");
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

    @SuppressLint("CheckResult")
    private void RegisterByPassWord(String phone, String password) {
        if (StringUtils.isEmpty(phone)) {
            ToastUtils.toast("用户名不能为空！");
            return;
        } else if (StringUtils.isEmpty(password)) {
            ToastUtils.toast("密码不能为空！");
            return;
        }
        Observable<Boolean> observable = XHttp.post("/user/register")
                .params("phone", phone)
                .params("password", password)
                .syncRequest(false)
                .onMainThread(true)
                .execute(Boolean.class);
        observable.subscribeWith(new TipRequestSubscriber<Boolean>() {
            @SuppressLint("CheckResult")
            @Override
            protected void onSuccess(Boolean response) {
                if (response != null) {
                    XToastUtils.info("注册成功");
                    ActivityUtils.startActivity(LoginActivity.class);

                } else {
                    XToastUtils.info("注册失败");
                }
            }

            @SuppressLint("CheckResult")
            @Override
            public void onError(ApiException e) {
                XToastUtils.info("访问错误");
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

