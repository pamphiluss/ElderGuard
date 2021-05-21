package com.syd.elderguard.fragment;

import com.syd.elderguard.core.BaseFragment;
import com.syd.elderguard.utils.TokenUtils;
import com.syd.elderguard.R;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xutil.XUtil;

import butterknife.BindView;

@Page(name = "设置")
public class SettingsFragment extends BaseFragment implements SuperTextView.OnSuperTextViewClickListener {

    @BindView(R.id.menu_logout)
    SuperTextView menuLogout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initViews() {
        menuLogout.setOnSuperTextViewClickListener(this);
    }

    @SingleClick
    @Override
    public void onClick(SuperTextView superTextView) {
        switch (superTextView.getId()) {
            case R.id.menu_logout:
                DialogLoader.getInstance().showConfirmDialog(
                        getContext(),
                        getString(R.string.lab_logout_confirm),
                        getString(R.string.lab_yes),
                        (dialog, which) -> {
                            dialog.dismiss();
                            XUtil.getActivityLifecycleHelper().exit();
                            TokenUtils.handleLogoutSuccess();
                        },
                        getString(R.string.lab_no),
                        (dialog, which) -> dialog.dismiss()
                );
                break;
            default:
                break;
        }
    }
}
