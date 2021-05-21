
package com.syd.elderguard.fragment.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.syd.elderguard.core.BaseFragment;
import com.syd.elderguard.fragment.ChangeUserInfoFragment;
import com.syd.elderguard.fragment.RelationFragment;
import com.syd.elderguard.fragment.SettingsFragment;
import com.syd.elderguard.manager.TokenManager;
import com.syd.elderguard.R;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xutil.app.PathUtils;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.syd.elderguard.utils.DataProvider.getUserImgUrl;


@Page(anim = CoreAnim.none)
public class ProfileFragment extends BaseFragment implements SuperTextView.OnSuperTextViewClickListener {
    @BindView(R.id.riv_head_pic)
    RadiusImageView rivHeadPic;
    @BindView(R.id.menu_setting)
    SuperTextView menuSettings;
    @BindView(R.id.menu_info)
    SuperTextView menu_Info;
    @BindView(R.id.menu_context)
    SuperTextView menu_context;
    @BindView(R.id.menu_pic)
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
        menu_Info.setOnSuperTextViewClickListener(this);
        menu_context.setOnSuperTextViewClickListener(this);
        minePic.setOnSuperTextViewClickListener(this);
        menuSettings.setOnSuperTextViewClickListener(this);

    }

    @SingleClick
    @Override
    public void onClick(SuperTextView view) {
        switch (view.getId()) {
            case R.id.menu_pic:
            case R.id.menu_info:
                openNewPage(ChangeUserInfoFragment.class, "user", TokenManager.getInstance().getLoginUser());
                break;
            case R.id.menu_context:
                openNewPage(RelationFragment.class);
                break;

            case R.id.menu_setting:
                openNewPage(SettingsFragment.class);
                break;
            default:
                break;
        }
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

}
