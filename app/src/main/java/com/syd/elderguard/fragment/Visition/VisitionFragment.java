package com.syd.elderguard.fragment.Visition;

import android.annotation.SuppressLint;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.syd.elderguard.base.broccoli.BroccoliSimpleDelegateAdapter;
import com.syd.elderguard.base.delegate.SimpleDelegateAdapter;
import com.syd.elderguard.base.delegate.SingleDelegateAdapter;
import com.syd.elderguard.core.BaseFragment;
import com.syd.elderguard.core.http.subscriber.TipRequestSubscriber;
import com.syd.elderguard.entity.EgVisit;
import com.syd.elderguard.manager.TokenManager;
import com.syd.elderguard.utils.DataProvider;
import com.syd.elderguard.utils.XToastUtils;
import com.syd.elderguard.R;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xhttp2.utils.TypeUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import me.samlss.broccoli.Broccoli;

import static com.syd.elderguard.utils.DataProvider.getVisitImgUrl;


@Page(anim = CoreAnim.none)
public class VisitionFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    List<EgVisit> egVisits;
    private SimpleDelegateAdapter<EgVisit> mEgVisitAdapter;

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
        return R.layout.fragment_visit;
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

        //轮播条
        SingleDelegateAdapter bannerAdapter = new SingleDelegateAdapter(R.layout.include_head_view_banner) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
                SimpleImageBanner banner = holder.findViewById(R.id.sib_simple_usage);
                banner.setSource(DataProvider.getBannerList())
                        .setOnItemClickListener((view, item, position1) -> XToastUtils.toast("headBanner position--->" + position1)).startScroll();
            }
        };


        SingleDelegateAdapter titleAdapter = new SingleDelegateAdapter(R.layout.adapter_title_item) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
                holder.text(R.id.tv_title, "访问数据");
                holder.text(R.id.tv_action, "查看陌生人访问");
                holder.click(R.id.tv_action, v -> openNewPage(StrVisitionFragment.class));
            }
        };


        mEgVisitAdapter = new BroccoliSimpleDelegateAdapter<EgVisit>(R.layout.adapter_visit_card_view_list_item, new LinearLayoutHelper(), DataProvider.getEmptyVisitInfo()) {

            @Override
            protected void onBindData(RecyclerViewHolder holder, EgVisit model, int position) {
                if (model != null) {
                    holder.text(R.id.visit_name, model.getVisitName());
                    holder.text(R.id.visit_time, model.getCrdate());
                    ImageView picture = holder.findViewById(R.id.visit_image);
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.ic_launcher);
                    Glide.with(picture.getContext())
                            .load(getVisitImgUrl(model))
                            .apply(options)
                            .into(picture);

                }
            }

            @Override
            protected void onBindBroccoli(RecyclerViewHolder holder, Broccoli broccoli) {
                broccoli.addPlaceholders(
                        holder.findView(R.id.visit_name),
                        holder.findView(R.id.visit_time),
                        holder.findView(R.id.visit_image)
                );
            }
        };

        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        delegateAdapter.addAdapter(bannerAdapter);
        delegateAdapter.addAdapter(titleAdapter);
        delegateAdapter.addAdapter(mEgVisitAdapter);

        recyclerView.setAdapter(delegateAdapter);
    }

    @Override
    protected void initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            getVisitList();
            refreshLayout.getLayout().postDelayed(() -> {
                mEgVisitAdapter.refresh(egVisits);
                refreshLayout.finishRefresh();
            }, 1000);
        });

        refreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
    }


    @SuppressLint("CheckResult")
    private void getVisitList() {
        if (TokenManager.getInstance().getLoginUser() == null) {
            egVisits = null;
        }
        Observable<List<EgVisit>> observable = XHttp.get("/visit/getAllVisitByUser")
                .params("userId", TokenManager.getInstance().getLoginUser().getId())
                .syncRequest(false)
                .onMainThread(true)
                .execute(TypeUtils.getListType(EgVisit.class));

        observable.subscribeWith(new TipRequestSubscriber<List<EgVisit>>() {
            @Override
            protected void onSuccess(List<EgVisit> response) {
                if (response != null && response.size() > 0) {
                    egVisits = response;
                    XToastUtils.info("成功");
                } else {
                    egVisits.clear();
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
