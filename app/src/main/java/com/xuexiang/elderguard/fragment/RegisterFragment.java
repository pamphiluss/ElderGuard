package com.xuexiang.elderguard.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.xuexiang.elderguard.R;
import com.xuexiang.elderguard.core.BaseFragment;
import com.xuexiang.elderguard.core.webview.AgentWebActivity;
import com.xuexiang.elderguard.entity.EgUser;
import com.xuexiang.elderguard.entity.LoginInfo;
import com.xuexiang.elderguard.utils.SettingUtils;
import com.xuexiang.elderguard.utils.Utils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xhttp2.subsciber.ProgressDialogLoader;
import com.xuexiang.xhttp2.subsciber.impl.IProgressLoader;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

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
    @OnClick({R.id.btn_register, R.id.tv_user_protocol, R.id.tv_privacy_protocol})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getContext(), AgentWebActivity.class);
        switch (view.getId()) {
            case R.id.btn_register:
                // TODO: 注册逻辑
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


    /**
     * 注册成功的处理
     */
    private void onLoginSuccess(LoginInfo loginInfo) {
        // TODO:注册成功
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

