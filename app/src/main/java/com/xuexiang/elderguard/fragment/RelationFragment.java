package com.xuexiang.elderguard.fragment;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gturedi.views.StatefulLayout;
import com.mylhyl.circledialog.BaseCircleDialog;
import com.mylhyl.circledialog.CircleDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xuexiang.elderguard.R;
import com.xuexiang.elderguard.activity.LoginActivity;
import com.xuexiang.elderguard.adapter.base.RelationAdapter;
import com.xuexiang.elderguard.core.http.subscriber.TipRequestSubscriber;
import com.xuexiang.elderguard.entity.EgRelationship;
import com.xuexiang.elderguard.manager.TokenManager;
import com.xuexiang.elderguard.utils.RouterUtils;
import com.xuexiang.elderguard.utils.XToastUtils;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xhttp2.utils.TypeUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.utils.TitleBar;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;


@Page(name = "白名单")
public class RelationFragment extends XPageFragment implements SmartViewHolder.OnItemLongClickListener, SmartViewHolder.OnViewItemClickListener, SmartViewHolder.OnItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_stateful)
    StatefulLayout mLlStateful;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    private BaseCircleDialog dialogFragment;
    private RelationAdapter mRelationAdapter;

    @Override
    protected TitleBar initTitleBar() {
        TitleBar titleBar = super.initTitleBar().setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        titleBar.addAction(new TitleBar.TextAction("添加") {
            @Override
            public void performAction(View view) {
                assert getFragmentManager() != null;
                dialogFragment = new CircleDialog.Builder()
                        //.setTypeface(typeface)
                        .setCanceledOnTouchOutside(false)
                        .setCancelable(true)
                        .setTitle("添加关系")
                        .setSubTitle("请输入要添加关系名称")
                        .setInputHint("请输入关系名称")
                        .setInputHeight(30)
//                        .setInputShowKeyboard(true)
                        .setInputEmoji(true)
                        .setInputCounter(10)
//                        .setInputCounter(20, (maxLen, currentLen) -> maxLen - currentLen + "/" + maxLen)
                        .configInput(params -> {

                            params.styleText = Typeface.BOLD;
                        })
                        .setNegative("取消", null)
                        .setPositiveInput("确定", (text, v) -> {
                            if (TextUtils.isEmpty(text)) {
                                v.setError("请输入内容");
                                return false;
                            } else {
                                uploadRelation(text, TokenManager.getInstance().getLoginUser().getId());
                                return true;
                            }
                        })
                        .show(getFragmentManager());
            }
        });
        return titleBar;
    }

    @SuppressLint("CheckResult")
    public void uploadRelation(String text, int id){
        if (StringUtils.isEmpty(text)) {
            ToastUtils.toast("关系名不能为空！");
            return;
        }
        Observable<Boolean> observable = XHttp.post("/relation/addRelation")
                .params("relation", text)
                .params("userId", id)
                .syncRequest(false)
                .onMainThread(true)
                .execute(Boolean.class);
        observable.subscribeWith(new TipRequestSubscriber<Boolean>() {
            @SuppressLint("CheckResult")
            @Override
            protected void onSuccess(Boolean response) {
                if (response != null) {
                    XToastUtils.info("添加成功");

                } else {
                    XToastUtils.info("添加失败");
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
    public void onItemClick(View itemView, int position) {
        // TODO:

        openPageForResult(RelationMeFragment.class, RouterUtils.getBundle("relation", mRelationAdapter.getItem(position)), 1000);
    }

    @Override
    public void onItemLongClick(View itemView, int position) {

    }

    @Override
    public void onViewItemClick(View view, int position) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_relation;
    }

    @Override
    protected void initViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mRelationAdapter = new RelationAdapter());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void initListeners() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final @NonNull RefreshLayout refreshLayout) {
                getRelationList(refreshLayout);
            }
        });

        mRelationAdapter.setOnItemClickListener(this);
        mRelationAdapter.setOnItemLongClickListener(this);
        mRelationAdapter.setItemViewOnClickListener(this);
        mRefreshLayout.autoRefresh();
    }

    /**
     * 获取用户信息
     *
     * @param refreshLayout
     */
    @SuppressLint("CheckResult")
    private void getRelationList(@NonNull final RefreshLayout refreshLayout) {
        Observable<List<EgRelationship>> observable = XHttp.get("/relation/getAllRelation")
                .syncRequest(false)
                .onMainThread(true)
                .execute(TypeUtils.getListType(EgRelationship.class));

        observable.subscribeWith(new TipRequestSubscriber<List<EgRelationship>>() {
            @Override
            protected void onSuccess(List<EgRelationship> response) {
                refreshLayout.finishRefresh(true);
                if (response != null && response.size() > 0) {
                    mRelationAdapter.refresh(response);
                    mLlStateful.showContent();
                } else {
                    mLlStateful.showEmpty();
                }
            }

            @Override
            public void onError(ApiException e) {
                refreshLayout.finishRefresh(false);
                mLlStateful.showError(e.getMessage(), null);
            }
        });
    }
}
