package com.xuexiang.elderguard.fragment;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xuexiang.elderguard.R;
import com.xuexiang.elderguard.core.http.subscriber.TipRequestSubscriber;
import com.xuexiang.elderguard.entity.EgStranger;
import com.xuexiang.elderguard.entity.EgVisit;
import com.xuexiang.elderguard.utils.XToastUtils;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xhttp2.subsciber.ProgressDialogLoader;
import com.xuexiang.xhttp2.subsciber.impl.IProgressLoader;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.utils.TitleBar;
import com.xuexiang.xrouter.annotation.AutoWired;
import com.xuexiang.xrouter.launcher.XRouter;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import butterknife.BindView;
import io.reactivex.Observable;

import static com.xuexiang.elderguard.utils.DataProvider.getStrImgUrl;
import static com.xuexiang.elderguard.utils.DataProvider.getViImgUrl;


@Page(name = "推送内容")
public class ShowPushInfoFragment extends XPageFragment {

    @BindView(R.id.view_type)
    TextView visitType;
    @BindView(R.id.et_name)
    TextView vistName;
    @BindView(R.id.et_time)
    TextView visitTime;
    @BindView(R.id.push_iv_picture)
    ImageView mIvPicture;
    EgVisit egVisit;
    EgStranger egStranger;
    private IProgressLoader mIProgressLoader;
    public final static String KEY_PARAM_MSG = "key_param_msg";
    @AutoWired(name = KEY_PARAM_MSG)
    String picName;

    String[] PicList;


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
                popToBack();
            }
        });
        return titleBar;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.test;
    }

    @Override
    protected void initViews() {
        ToastUtils.toast(picName);
        PicList = picName.split("_");
        mIProgressLoader = new ProgressDialogLoader(getContext());
        if (PicList[0].equals("str")) {
            visitType.setText("陌生人");
            getStrVisit(picName);

        } else {
            visitType.setText("熟人");
            getVisit(picName);

        }


    }


    @Override
    protected void initListeners() {


    }


    @SuppressLint("CheckResult")
    private void getVisit(String filename) {
        Observable<EgVisit> observable = XHttp.post("/visit/getVisitByFilename")
                .params("filename", filename)
                .syncRequest(false)
                .onMainThread(true)
                .execute(EgVisit.class);

        observable.subscribeWith(new TipRequestSubscriber<EgVisit>() {
            @SuppressLint("CheckResult")
            @Override
            protected void onSuccess(EgVisit response) {
                if (response != null) {
                    egVisit = response;
                    vistName.setText(StringUtils.getString(egVisit.getVisitName()));
                    visitTime.setText(egVisit.getCrdate());
                    XToastUtils.info("成功");
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.ic_launcher);
                    Glide.with(getContext())
                            .load(getViImgUrl(egVisit))
                            .apply(options)
                            .into(mIvPicture);
                } else {
                    egVisit = null;
                    XToastUtils.info("失败");
                }
            }

            @SuppressLint("CheckResult")
            @Override
            public void onError(ApiException e) {
                XToastUtils.info("访问错误");
            }
        });

    }

    @SuppressLint("CheckResult")
    private void getStrVisit(String filename) {
        Observable<EgStranger> observable = XHttp.post("/strvisit/getStrVisitByFilename")
                .params("filename", filename)
                .syncRequest(false)
                .onMainThread(true)
                .execute(EgStranger.class);

        observable.subscribeWith(new TipRequestSubscriber<EgStranger>() {
            @Override
            protected void onSuccess(EgStranger response) {
                if (response != null) {
                    egStranger = response;
                    vistName.setText(StringUtils.getString(egStranger.getStrangername()));
                    visitTime.setText(egStranger.getCrdate());
                    XToastUtils.info("成功");
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.ic_launcher);
                    Glide.with(getContext())
                            .load(getStrImgUrl(egStranger))
                            .apply(options)
                            .into(mIvPicture);
                } else {
                    egStranger = null;
                    XToastUtils.info("失败");
                }
            }

            @Override
            public void onError(ApiException e) {
                XToastUtils.info("访问错误");
            }
        });
    }


}
