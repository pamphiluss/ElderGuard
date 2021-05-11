package com.xuexiang.elderguard.fragment;

import android.annotation.SuppressLint;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.elderguard.R;
import com.xuexiang.elderguard.adapter.base.broccoli.BroccoliSimpleDelegateAdapter;
import com.xuexiang.elderguard.adapter.base.delegate.SimpleDelegateAdapter;
import com.xuexiang.elderguard.core.BaseFragment;
import com.xuexiang.elderguard.core.http.subscriber.TipRequestSubscriber;
import com.xuexiang.elderguard.entity.EgAcquaintance;
import com.xuexiang.elderguard.entity.EgRelationship;
import com.xuexiang.elderguard.manager.TokenManager;
import com.xuexiang.elderguard.utils.DataProvider;
import com.xuexiang.elderguard.utils.XToastUtils;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xhttp2.utils.TypeUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xrouter.annotation.AutoWired;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import me.samlss.broccoli.Broccoli;

import static com.xuexiang.elderguard.utils.DataProvider.getAcqVisitImgUrl;


@Page(name = "成员")
public class RelationMemberFragment extends BaseFragment {

    @AutoWired
    EgRelationship relationship;
    @BindView(R.id.recyclerView_str)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout_str)
    SmartRefreshLayout refreshLayout;
    List<EgAcquaintance> egAcquaintances;
    private SimpleDelegateAdapter<EgAcquaintance> egAcquaintanceSimpleDelegateAdapter;

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
        return R.layout.fragment_member;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(getContext());
        recyclerView.setLayoutManager(virtualLayoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);

        egAcquaintanceSimpleDelegateAdapter = new BroccoliSimpleDelegateAdapter<EgAcquaintance>(R.layout.adapter_member_view_list_item, new LinearLayoutHelper(), DataProvider.getEmptyAcqVisitInfo()) {

            @Override
            protected void onBindData(RecyclerViewHolder holder, EgAcquaintance model, int position) {
                if (model != null) {
                    holder.text(R.id.acq_name, model.getName());
                    holder.text(R.id.acq_nickname, model.getNickname());
                    ImageView picture = holder.findViewById(R.id.acq_image);
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.ic_launcher);
                    Glide.with(picture.getContext())
                            .load(getAcqVisitImgUrl(model))
                            .apply(options)
                            .into(picture);

                }
            }

            @Override
            protected void onBindBroccoli(RecyclerViewHolder holder, Broccoli broccoli) {
                broccoli.addPlaceholders(
                        holder.findView(R.id.acq_name),
                        holder.findView(R.id.acq_nickname),
                        holder.findView(R.id.acq_image)
                );
            }
        };

        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        delegateAdapter.addAdapter(egAcquaintanceSimpleDelegateAdapter);

        recyclerView.setAdapter(delegateAdapter);
    }

    @Override
    protected void initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            getVisitList();
            refreshLayout.getLayout().postDelayed(() -> {
                egAcquaintanceSimpleDelegateAdapter.refresh(egAcquaintances);
                refreshLayout.finishRefresh();
            }, 1000);
        });

        refreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
    }


    @SuppressLint("CheckResult")
    private void getVisitList() {
        Observable<List<EgAcquaintance>> observable = XHttp.get("/Acq/getAcqByUserAndRelation")
                .params("userId", TokenManager.getInstance().getLoginUser().getId())
                .params("relathionId", 1)
                .syncRequest(false)
                .onMainThread(true)
                .execute(TypeUtils.getListType(EgAcquaintance.class));

        observable.subscribeWith(new TipRequestSubscriber<List<EgAcquaintance>>() {
            @Override
            protected void onSuccess(List<EgAcquaintance> response) {
                if (response != null && response.size() > 0) {
                    egAcquaintances = response;
                    XToastUtils.info("成功");
                } else {
                    egAcquaintances.clear();
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
