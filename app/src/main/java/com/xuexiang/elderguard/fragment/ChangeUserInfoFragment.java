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
import com.xuexiang.elderguard.activity.MainActivity;
import com.xuexiang.elderguard.entity.EgUser;
import com.xuexiang.elderguard.manager.TokenManager;
import com.xuexiang.rxutil2.lifecycle.RxLifecycle;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xaop.consts.PermissionConsts;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.DownloadProgressCallBack;
import com.xuexiang.xhttp2.callback.ProgressLoadingCallBack;
import com.xuexiang.xhttp2.callback.impl.IProgressResponseCallBack;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xhttp2.subsciber.ProgressDialogLoader;
import com.xuexiang.xhttp2.subsciber.ProgressLoadingSubscriber;
import com.xuexiang.xhttp2.subsciber.impl.IProgressLoader;
import com.xuexiang.xhttp2.utils.Utils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.utils.TitleBar;
import com.xuexiang.xrouter.launcher.XRouter;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.app.PathUtils;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.display.HProgressDialogUtils;
import com.xuexiang.xutil.file.FileUtils;
import com.xuexiang.xutil.net.JsonUtil;
import com.xuexiang.xutil.tip.ToastUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.xuexiang.elderguard.utils.DataProvider.getUserImgUrl;


@Page(name = "个人信息")
public class ChangeUserInfoFragment extends XPageFragment {
    private static final int REQUEST_CODE_SELECT_PICTURE = 2000;


    EgUser egUser;
    @BindView(R.id.et_user_name)
    EditText mEtUserName;
    @BindView(R.id.et_sex)
    EditText mEtSex;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_mail)
    EditText mEtMail;
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
                saveUser(view);
            }
        });
        return titleBar;
    }

    @SingleClick
    private void saveUser(View view) {
        if (checkUser()) {
            mIProgressLoader.updateMessage("保存中...");
            XHttp.post("/user/uploadInfo")
                    .upJson(JsonUtil.toJson(egUser))
                    .execute(new ProgressLoadingCallBack<Boolean>(mIProgressLoader) {
                        @Override
                        public void onSuccess(Boolean response) {
                            if (response) {
                                mIsEditSuccess = true;
                                ToastUtils.toast("保存成功！");
                                TokenManager.getInstance().setLoginUser(egUser);
                                ActivityUtils.startActivity(MainActivity.class);
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
        }
    }

    private boolean checkUser() {
        if (StringUtils.isEmpty(mEtUserName.getText().toString())) {
            ToastUtils.toast("用户名不能为空！");
            return false;
        } else if (StringUtils.isEmpty(mEtSex.getText().toString())) {
            ToastUtils.toast("性别不能为空！");
            return false;
        } else {
            egUser.setUsername(mEtUserName.getText().toString());
            egUser.setGender(mEtSex.getText().toString());
            egUser.setPhone(mEtPhone.getText().toString());
            egUser.setMail(mEtMail.getText().toString());
            return true;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_user;
    }

    @Override
    protected void initViews() {
        egUser = TokenManager.getInstance().getLoginUser();
        mIProgressLoader = new ProgressDialogLoader(getContext());

        mEtUserName.setText(StringUtils.getString(egUser.getUsername()));
        mEtSex.setText(StringUtils.getString(egUser.getGender()));
        mEtPhone.setText(StringUtils.getString(egUser.getPhone()));
        mEtMail.setText(String.valueOf(egUser.getMail()));

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.visit);
        Glide.with(getContext())
                .load(getUserImgUrl(egUser))
                .apply(options)
                .into(mIvPicture);
    }


    @Override
    protected void initListeners() {


    }

    @OnClick({R.id.iv_picture, R.id.btn_upload, R.id.btn_download})
    @SingleClick
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_picture:
                selectPicture();
                break;
            case R.id.btn_upload:
                uploadPicture();
                break;
            case R.id.btn_download:
                downloadPicture();
                break;
            default:
                break;
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

    @SuppressLint("CheckResult")
    @Permission(PermissionConsts.STORAGE)
    private void uploadPicture() {
        if (StringUtils.isEmpty(mPicturePath)) {
            ToastUtils.toast("请先选择需要上传的图片!");
            selectPicture();
            return;
        }
        mIProgressLoader.updateMessage("上传中...");
        if (Utils.isScopedStorageMode() && Utils.isPublicPath(mPicturePath)) {
            XHttp.post("/user/uploadUserPicture")
                    .params("userId", egUser.getId())
                    .uploadFile("file", getInputStreamByUri(mPictureUri), FileUtils.getFileByPath(mPicturePath).getName(), new IProgressResponseCallBack() {
                        @Override
                        public void onResponseProgress(long bytesWritten, long contentLength, boolean done) {

                        }
                    }).execute(Boolean.class)
                    .compose(RxLifecycle.with(this).<Boolean>bindToLifecycle())
                    .subscribeWith(new ProgressLoadingSubscriber<Boolean>(mIProgressLoader) {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            mIsEditSuccess = true;
                            egUser.setImage(FileUtils.getFileByPath(mPicturePath).getName());
                            ToastUtils.toast("图片上传" + (aBoolean ? "成功" : "失败") + "！");
                        }
                    });
        } else {
            XHttp.post("/user/uploadUserPicture")
                    .params("userId", egUser.getId())
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
                            egUser.setImage(FileUtils.getFileByPath(mPicturePath).getName());
                            ToastUtils.toast("图片上传" + (aBoolean ? "成功" : "失败") + "！");
                        }
                    });
        }
    }

    private InputStream getInputStreamByUri(Uri uri) {
        try {
            return XUtil.getContext().getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 下载图片
     */
    @Permission(PermissionConsts.STORAGE)
    private void downloadPicture() {
        if (StringUtils.isEmpty(egUser.getImage())) {
            ToastUtils.toast("未上传头像！");
            return;
        }

        XHttp.downLoad("/file/downloadFile/" + egUser.getImage())
                .isUseBaseUrl(true) //设置了这个之后，baseUrl才起作用。如果下载的地址是绝对地址，就可以设置为false。
                .savePath(PathUtils.getExtPicturesPath())
                .execute(new DownloadProgressCallBack<String>() {
                    @Override
                    public void onStart() {
                        HProgressDialogUtils.showHorizontalProgressDialog(getContext(), "图片下载中...", true);
                    }

                    @Override
                    public void onError(ApiException e) {
                        e.printStackTrace();
                        ToastUtils.toast(e.getMessage());
                        HProgressDialogUtils.cancel();
                    }

                    @Override
                    public void update(long downLoadSize, long totalSize, boolean done) {
                        HProgressDialogUtils.setMax(totalSize);
                        HProgressDialogUtils.setProgress(downLoadSize);
                    }

                    @Override
                    public void onComplete(String path) {
                        ToastUtils.toast("图片下载成功, 保存路径:" + path);
                        HProgressDialogUtils.cancel();
                    }
                });

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
