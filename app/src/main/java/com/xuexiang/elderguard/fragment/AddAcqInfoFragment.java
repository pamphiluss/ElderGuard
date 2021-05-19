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
import com.xuexiang.elderguard.entity.EgAcquaintance;
import com.xuexiang.elderguard.manager.TokenManager;
import com.xuexiang.rxutil2.lifecycle.RxLifecycle;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xaop.consts.PermissionConsts;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.ProgressLoadingCallBack;
import com.xuexiang.xhttp2.callback.impl.IProgressResponseCallBack;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xhttp2.subsciber.ProgressDialogLoader;
import com.xuexiang.xhttp2.subsciber.ProgressLoadingSubscriber;
import com.xuexiang.xhttp2.subsciber.impl.IProgressLoader;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.utils.TitleBar;
import com.xuexiang.xrouter.annotation.AutoWired;
import com.xuexiang.xrouter.launcher.XRouter;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.app.PathUtils;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.file.FileUtils;
import com.xuexiang.xutil.net.JsonUtil;
import com.xuexiang.xutil.tip.ToastUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


@Page(name = "添加成员")
public class AddAcqInfoFragment extends XPageFragment {
    private static final int REQUEST_CODE_SELECT_PICTURE = 2000;


    @AutoWired(name = "key_param_msg")
    int egRelationshipId;
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
    @BindView(R.id.et_acq_rel)
    EditText mEtRela;
    @BindView(R.id.iv_picture)
    ImageView mIvPicture;
    EgAcquaintance egAcquaintance;
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
                saveAcq(view);
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
        ToastUtils.toast(String.valueOf(egRelationshipId));
        mEtRela.setText(String.valueOf(egRelationshipId));
    }

    @OnClick({R.id.iv_picture})
    @SingleClick
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_picture:
                selectPicture();
                break;
            default:
                break;
        }
    }


    @Override
    protected void initListeners() {


    }

    private boolean checkAcq() {
        if (StringUtils.isEmpty(mEtUserName.getText().toString())) {
            ToastUtils.toast("用户名不能为空！");
            return false;
        } else if (StringUtils.isEmpty(mEtSex.getText().toString())) {
            ToastUtils.toast("性别不能为空！");
            return false;
        } else if (StringUtils.isEmpty(mEtNikeName.getText().toString())) {
            ToastUtils.toast("成员昵称不能为空！");
            return false;
        } else if (StringUtils.isEmpty(mEtPhone.getText().toString())) {
            ToastUtils.toast("手机号不能为空！");
            return false;
        } else if (StringUtils.isEmpty(mPicturePath)) {
            ToastUtils.toast("图片不能为空！");
            return false;
        } else {
            egAcquaintance = new EgAcquaintance();
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            egAcquaintance.setName(mEtUserName.getText().toString());
            egAcquaintance.setNickname(mEtNikeName.getText().toString());
            egAcquaintance.setGender(mEtSex.getText().toString());
            egAcquaintance.setPhone(mEtPhone.getText().toString());
            egAcquaintance.setMail(mEtMail.getText().toString());
            egAcquaintance.setRelationshipid(egRelationshipId);
            egAcquaintance.setImage(Objects.requireNonNull(FileUtils.getFileByPath(mPicturePath)).getName());
            egAcquaintance.setUserid(TokenManager.getInstance().getLoginUser().getId());
            egAcquaintance.setCrdate(sdf.format(d));
            return true;
        }
    }

    @SuppressLint("CheckResult")
    @SingleClick
    private void saveAcq(View view) {
        if (checkAcq()) {
            mIProgressLoader.updateMessage("保存中...");
            XHttp.post("/Acq/uploadInfo")
                    .upJson(JsonUtil.toJson(egAcquaintance))
                    .execute(new ProgressLoadingCallBack<Boolean>(mIProgressLoader) {
                        @Override
                        public void onSuccess(Boolean response) {
                            if (response) {
                                mIsEditSuccess = true;
                                ToastUtils.toast("保存成功！");
                            } else {
                                ToastUtils.toast("保存失败！");
                            }
                        }

                        @Override
                        public void onError(ApiException e) {
                            super.onError(e);
                            ToastUtils.toast("编辑失败：" + e.getMessage());
                        }
                    });
            XHttp.post("/Acq/uploadPic")
                    .params("personName", egAcquaintance.getName() + "_" + TokenManager.getInstance().getLoginUser().getId())
                    .params("personId", egAcquaintance.getNickname())
                    .uploadFile("file", FileUtils.getFileByPath(mPicturePath), new IProgressResponseCallBack() {
                        @Override
                        public void onResponseProgress(long bytesWritten, long contentLength, boolean done) {

                        }
                    }).execute(Boolean.class)
                    .compose(RxLifecycle.with(this).<Boolean>bindToLifecycle())
                    .subscribeWith(new ProgressLoadingSubscriber<Boolean>(mIProgressLoader) {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            mIsEditSuccess = true;
                            ToastUtils.toast("图片上传成功！");
                        }
                    });
        }
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


    private InputStream getInputStreamByUri(Uri uri) {
        try {
            return XUtil.getContext().getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
