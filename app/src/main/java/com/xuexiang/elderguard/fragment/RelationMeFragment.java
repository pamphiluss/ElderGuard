package com.xuexiang.elderguard.fragment;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gturedi.views.StatefulLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xuexiang.elderguard.R;
import com.xuexiang.elderguard.adapter.base.AcqAdapter;
import com.xuexiang.elderguard.core.http.subscriber.TipRequestSubscriber;
import com.xuexiang.elderguard.entity.EgAcquaintance;
import com.xuexiang.elderguard.entity.EgRelationship;
import com.xuexiang.elderguard.manager.TokenManager;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xhttp2.utils.TypeUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.utils.TitleBar;
import com.xuexiang.xrouter.annotation.AutoWired;
import com.xuexiang.xrouter.launcher.XRouter;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;


@Page(name = "成员")
public class RelationMeFragment extends XPageFragment implements SmartViewHolder.OnItemLongClickListener, SmartViewHolder.OnViewItemClickListener, SmartViewHolder.OnItemClickListener {

    @AutoWired
    EgRelationship relation;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_stateful)
    StatefulLayout mLlStateful;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private AcqAdapter acqAdapter;

    @Override
    public void onItemClick(View itemView, int position) {
        // TODO:
    }

    @Override
    public void onItemLongClick(View itemView, int position) {

    }

    @Override
    public void onViewItemClick(View view, int position) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(acqAdapter = new AcqAdapter());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

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

            }
        });
        titleBar.addAction(new TitleBar.TextAction("添加") {
            @Override
            public void performAction(View view) {
                openPage(AddAcqInfoFragment.class);
            }
        });
        return titleBar;
    }

    @Override
    protected void initListeners() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final @NonNull RefreshLayout refreshLayout) {
                getAcqList(refreshLayout);
            }
        });

        acqAdapter.setOnItemClickListener(this);
        acqAdapter.setOnItemLongClickListener(this);
        acqAdapter.setItemViewOnClickListener(this);
        mRefreshLayout.autoRefresh();
    }

    @SuppressLint("CheckResult")
    private void getAcqList(@NonNull final RefreshLayout refreshLayout) {
        Observable<List<EgAcquaintance>> observable = XHttp.get("/Acq/getAcqByUserAndRelation")
                .params("userId", TokenManager.getInstance().getLoginUser().getId())
                .params("relathionId", relation.getRelationid())
                .syncRequest(false)
                .onMainThread(true)
                .execute(TypeUtils.getListType(EgAcquaintance.class));

        observable.subscribeWith(new TipRequestSubscriber<List<EgAcquaintance>>() {
            @Override
            protected void onSuccess(List<EgAcquaintance> response) {
                refreshLayout.finishRefresh(true);
                if (response != null && response.size() > 0) {
                    acqAdapter.refresh(response);
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
