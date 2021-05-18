package com.xuexiang.elderguard.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xuexiang.elderguard.R;
import com.xuexiang.elderguard.entity.EgUser;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xaop.consts.PermissionConsts;
import com.xuexiang.xhttp2.subsciber.ProgressDialogLoader;
import com.xuexiang.xhttp2.subsciber.impl.IProgressLoader;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.utils.TitleBar;
import com.xuexiang.xrouter.annotation.AutoWired;
import com.xuexiang.xrouter.launcher.XRouter;
import com.xuexiang.xutil.app.PathUtils;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;


@Page(name = "添加成员")
public class AddAcqInfoFragment extends XPageFragment {
    private static final int REQUEST_CODE_SELECT_PICTURE = 2000;

    @AutoWired
    EgUser egUser;
    @BindView(R.id.et_acq_name)
    EditText mEtUserName;
    @BindView(R.id.et_acq_sex)
    EditText mEtSex;
    @BindView(R.id.et_acq_phone)
    EditText mEtPhone;
    @BindView(R.id.et_acq_mail)
    EditText mEtMail;
    @BindView(R.id.et_acq_nickname)
    EditText mEtNikeName;
    @BindView(R.id.iv_picture)
    ImageView mIvPicture;

    String mPicturePath;
    Uri mPictureUri;

    private IProgressLoader mIProgressLoader;

    private boolean mIsEditSuccess;

    @Override
    protected void initArgs() {
        super.initArgs();
        XRouter.getInstance().inject(this);
    }

    @Override
    protected TitleBar initTitleBar() {
        TitleBar titleBar = super.initTitleBar().setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsEditSuccess) {
                    setFragmentResult(RESULT_OK, null);
                }
                popToBack();
            }
        });
        titleBar.addAction(new TitleBar.TextAction("保存") {
            @Override
            public void performAction(View view) {


            }
        });
        return titleBar;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_acq;
    }

    @Override
    protected void initViews() {
        mIProgressLoader = new ProgressDialogLoader(getContext());

    }


    @Override
    protected void initListeners() {


    }


    @Permission(PermissionConsts.STORAGE)
    private void selectPicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 2000);
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
                    .placeholder(R.drawable.visit);
            Glide.with(this)
                    .load(data.getData())
                    .apply(options)
                    .into(mIvPicture);
        }
    }


    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsEditSuccess) {
                setFragmentResult(RESULT_OK, null);
            }
        }
        return false;
    }


}
