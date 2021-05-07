
package com.xuexiang.elderguard.fragment.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xuexiang.elderguard.R;
import com.xuexiang.elderguard.core.BaseFragment;
import com.xuexiang.elderguard.fragment.AboutFragment;
import com.xuexiang.elderguard.fragment.SettingsFragment;
import com.xuexiang.elderguard.manager.TokenManager;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xaop.consts.PermissionConsts;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.impl.IProgressResponseCallBack;
import com.xuexiang.xhttp2.subsciber.ProgressLoadingSubscriber;
import com.xuexiang.xhttp2.utils.Utils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xutil.app.PathUtils;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.file.FileUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.xuexiang.elderguard.utils.DemoDataProvider.getUserImgUrl;


@Page(anim = CoreAnim.none)
public class ProfileFragment extends BaseFragment implements SuperTextView.OnSuperTextViewClickListener {
    @BindView(R.id.riv_head_pic)
    RadiusImageView rivHeadPic;
    @BindView(R.id.menu_settings)
    SuperTextView menuSettings;
    @BindView(R.id.menu_about)
    SuperTextView menuAbout;
    @BindView(R.id.mine_pic)
    SuperTextView minePic;
    private static final int REQUEST_CODE_SELECT_PICTURE = 2000;

    String mPicturePath;
    Uri mPictureUri;

    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_default_head);
        Glide.with(rivHeadPic.getContext())
                .load(getUserImgUrl(TokenManager.getInstance().getLoginUser()))
                .apply(options)
                .into(rivHeadPic);
    }

    @Override
    protected void initListeners() {
        menuSettings.setOnSuperTextViewClickListener(this);
        menuAbout.setOnSuperTextViewClickListener(this);

    }

    @SingleClick
    @Override
    public void onClick(SuperTextView view) {
        switch (view.getId()) {
            case R.id.menu_settings:
                openNewPage(SettingsFragment.class);
                break;
            case R.id.menu_about:
                openNewPage(AboutFragment.class);
                break;
            case R.id.mine_pic:
                selectPicture();
                uploadPicture();
            default:
                break;
        }
    }

    @Permission(PermissionConsts.STORAGE)
    private void selectPicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_SELECT_PICTURE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_SELECT_PICTURE) {
            mPictureUri = data.getData();
            mPicturePath = PathUtils.getFilePathByUri(getContext(), data.getData());
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_default_head);
            Glide.with(this)
                    .load(data.getData())
                    .apply(options)
                    .into(minePic.getRightIconIV());
        }
    }

    @SuppressLint("CheckResult")
    @Permission(PermissionConsts.STORAGE)
    private void uploadPicture() {

    }
}
