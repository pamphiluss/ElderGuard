package com.xuexiang.elderguard.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.xuexiang.elderguard.R;
import com.xuexiang.elderguard.activity.ForgetPasswordActivity;
import com.xuexiang.elderguard.activity.MainActivity;
import com.xuexiang.elderguard.activity.RegisterActivity;
import com.xuexiang.elderguard.core.BaseFragment;
import com.xuexiang.elderguard.core.http.api.ApiService;
import com.xuexiang.elderguard.core.webview.AgentWebActivity;
import com.xuexiang.elderguard.entity.EgUser;
import com.xuexiang.elderguard.entity.LoginInfo;
import com.xuexiang.elderguard.manager.TokenManager;
import com.xuexiang.elderguard.utils.RandomUtils;
import com.xuexiang.elderguard.utils.SettingUtils;
import com.xuexiang.elderguard.utils.TokenUtils;
import com.xuexiang.elderguard.utils.Utils;
import com.xuexiang.elderguard.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xhttp2.XHttpProxy;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xhttp2.subsciber.ProgressDialogLoader;
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

import static com.xuexiang.elderguard.core.webview.AgentWebFragment.KEY_URL;


/**
 * 登录页面
 */
@Page(anim = CoreAnim.none)
public class LoginFragment extends BaseFragment {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_phone_number)
    MaterialEditText etPhoneNumber;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_password)
    MaterialEditText etPassword;
    private IProgressLoader mIProgressLoader;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle()
                .setImmersive(true);
        titleBar.setBackgroundColor(Color.TRANSPARENT);
        titleBar.setTitle("");
        titleBar.setLeftImageDrawable(ResUtils.getVectorDrawable(getContext(), R.drawable.ic_login_close));
        titleBar.setActionTextColor(ThemeUtils.resolveColor(Objects.requireNonNull(getContext()), R.attr.colorAccent));
        titleBar.addAction(new TitleBar.TextAction(R.string.title_jump_login) {
            @Override
            public void performAction(View view) {
                onLoginSuccess(new LoginInfo().setEgUser(new EgUser()));
            }
        });
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
        mIProgressLoader = new ProgressDialogLoader(getContext(), "登录中...");
    }

    @SuppressLint("NonConstantResourceId")
    @SingleClick
    @OnClick({R.id.btn_login, R.id.tv_register, R.id.tv_forget_password, R.id.tv_user_protocol, R.id.tv_privacy_protocol})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getContext(), AgentWebActivity.class);
        switch (view.getId()) {
            case R.id.btn_login:
                if (etPhoneNumber.validate()) {
                    if (etPassword.validate()) {
                        loginByPassWord(etPhoneNumber.getEditValue(), etPassword.getEditValue());
                    }
                }
                break;
            case R.id.tv_register:
                XToastUtils.info("注册");
//                ActivityUtils.startActivity(RegisterActivity.class);
                ActivityUtils.startActivity(RegisterActivity.class);
                break;
            case R.id.tv_forget_password:
                XToastUtils.info("忘记密码");
                ActivityUtils.startActivity(ForgetPasswordActivity.class);
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
    private void loginByPassWord(String phone, String password) {
        if (StringUtils.isEmpty(phone)) {
            ToastUtils.toast("用户名不能为空！");
            return;
        } else if (StringUtils.isEmpty(password)) {
            ToastUtils.toast("密码不能为空！");
            return;
        }
        XHttpProxy.proxy(ApiService.IAuthorization.class)
                .login(phone, password)
                .subscribeWith(new ProgressLoadingSubscriber<LoginInfo>(mIProgressLoader) {

                    @Override
                    protected void onSuccess(LoginInfo loginInfo) {
                        ToastUtils.toast("登录成功！");
                        TokenManager.getInstance()
                                .setToken(loginInfo.getToken())
                                .setLoginUser(loginInfo.getEgUser());
                        onLoginSuccess(loginInfo);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        ToastUtils.toast(e.getDisplayMessage());
                        ToastUtils.toast(e.getDetailMessage());
                    }
                });

    }

    /**
     * 登录成功的处理
     */
    private void onLoginSuccess(LoginInfo loginInfo) {
        String token = RandomUtils.getRandomNumbersAndLetters(16);
        if (TokenUtils.handleLoginSuccess(token,loginInfo)) {
            popToBack();
            ActivityUtils.startActivity(MainActivity.class);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

