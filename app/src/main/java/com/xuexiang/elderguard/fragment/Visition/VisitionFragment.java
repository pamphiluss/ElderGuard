/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.elderguard.fragment.Visition;

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
import com.xuexiang.elderguard.R;
import com.xuexiang.elderguard.adapter.base.broccoli.BroccoliSimpleDelegateAdapter;
import com.xuexiang.elderguard.adapter.base.delegate.SimpleDelegateAdapter;
import com.xuexiang.elderguard.adapter.base.delegate.SingleDelegateAdapter;
import com.xuexiang.elderguard.core.BaseFragment;
import com.xuexiang.elderguard.core.http.subscriber.TipRequestSubscriber;
import com.xuexiang.elderguard.entity.EgVisit;
import com.xuexiang.elderguard.utils.DemoDataProvider;
import com.xuexiang.elderguard.utils.XToastUtils;
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

import static com.xuexiang.elderguard.utils.DemoDataProvider.getVisitImgUrl;


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
                banner.setSource(DemoDataProvider.getBannerList())
                        .setOnItemClickListener((view, item, position1) -> XToastUtils.toast("headBanner position--->" + position1)).startScroll();
            }
        };


        SingleDelegateAdapter titleAdapter = new SingleDelegateAdapter(R.layout.adapter_title_item) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
                holder.text(R.id.tv_title, "访问数据");
                holder.text(R.id.tv_action, "排序");
                holder.click(R.id.tv_action, v -> XToastUtils.toast("排序"));
            }
        };


        mEgVisitAdapter = new BroccoliSimpleDelegateAdapter<EgVisit>(R.layout.adapter_visit_card_view_list_item, new LinearLayoutHelper(), DemoDataProvider.getEmptyVisitInfo()) {

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
        Observable<List<EgVisit>> observable = XHttp.get("/visit/getAllVisitByUser")
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
