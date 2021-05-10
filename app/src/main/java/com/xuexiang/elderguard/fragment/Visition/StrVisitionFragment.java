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
import com.xuexiang.elderguard.entity.EgStranger;
import com.xuexiang.elderguard.utils.DataProvider;
import com.xuexiang.elderguard.utils.XToastUtils;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xhttp2.utils.TypeUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import me.samlss.broccoli.Broccoli;

import static com.xuexiang.elderguard.utils.DataProvider.getStrVisitImgUrl;


@Page(anim = CoreAnim.none)
public class StrVisitionFragment extends BaseFragment {

    @BindView(R.id.recyclerView_str)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout_str)
    SmartRefreshLayout refreshLayout;
    List<EgStranger> egStrangers;
    private SimpleDelegateAdapter<EgStranger> mEgVisitAdapter;

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
        return R.layout.fragment_strvisit;
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

        mEgVisitAdapter = new BroccoliSimpleDelegateAdapter<EgStranger>(R.layout.adapter_strvisit_card_view_list_item, new LinearLayoutHelper(), DataProvider.getEmptyStrVisitInfo()) {

            @Override
            protected void onBindData(RecyclerViewHolder holder, EgStranger model, int position) {
                if (model != null) {
                    holder.text(R.id.visit_name_str, model.getStrangername());
                    holder.text(R.id.visit_time_str, model.getCrdate());
                    ImageView picture = holder.findViewById(R.id.visit_image_str);
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.ic_launcher);
                    Glide.with(picture.getContext())
                            .load(getStrVisitImgUrl(model))
                            .apply(options)
                            .into(picture);

                }
            }

            @Override
            protected void onBindBroccoli(RecyclerViewHolder holder, Broccoli broccoli) {
                broccoli.addPlaceholders(
                        holder.findView(R.id.visit_name_str),
                        holder.findView(R.id.visit_time_str),
                        holder.findView(R.id.visit_image_str)
                );
            }
        };

        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        delegateAdapter.addAdapter(mEgVisitAdapter);

        recyclerView.setAdapter(delegateAdapter);
    }

    @Override
    protected void initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            getVisitList();
            refreshLayout.getLayout().postDelayed(() -> {
                mEgVisitAdapter.refresh(egStrangers);
                refreshLayout.finishRefresh();
            }, 1000);
        });

        refreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
    }


    @SuppressLint("CheckResult")
    private void getVisitList() {
        Observable<List<EgStranger>> observable = XHttp.get("/strvisit/getAllStrVisitByUser")
                .syncRequest(false)
                .onMainThread(true)
                .execute(TypeUtils.getListType(EgStranger.class));

        observable.subscribeWith(new TipRequestSubscriber<List<EgStranger>>() {
            @Override
            protected void onSuccess(List<EgStranger> response) {
                if (response != null && response.size() > 0) {
                    egStrangers = response;
                    XToastUtils.info("成功");
                } else {
                    egStrangers.clear();
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
