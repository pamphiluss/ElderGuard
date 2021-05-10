package com.xuexiang.elderguard.fragment;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xuexiang.elderguard.R;
import com.xuexiang.elderguard.adapter.base.RelationAdapter;
import com.xuexiang.elderguard.core.http.subscriber.TipRequestSubscriber;
import com.xuexiang.elderguard.entity.EgRelationship;
import com.xuexiang.elderguard.utils.RouterUtils;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xhttp2.utils.TypeUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.gturedi.views.StatefulLayout;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;


@Page(name = "通讯录")
public class RelationFragment extends XPageFragment implements SmartViewHolder.OnItemLongClickListener, SmartViewHolder.OnViewItemClickListener, SmartViewHolder.OnItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_stateful)
    StatefulLayout mLlStateful;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private RelationAdapter mRelationAdapter;

    @Override
    public void onItemClick(View itemView, int position) {
        // TODO:
        openPageForResult(LoginFragment.class, RouterUtils.getBundle("book", mRelationAdapter.getItem(position)), 1000);
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
