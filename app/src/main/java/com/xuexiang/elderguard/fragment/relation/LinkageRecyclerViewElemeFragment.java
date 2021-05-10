package com.xuexiang.elderguard.fragment.relation;

import android.view.View;
import android.view.ViewGroup;

import com.kunminx.linkage.LinkageRecyclerView;
import com.kunminx.linkage.adapter.viewholder.LinkagePrimaryViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryViewHolder;
import com.kunminx.linkage.bean.BaseGroupedItem;
import com.xuexiang.elderguard.R;
import com.xuexiang.elderguard.core.BaseFragment;
import com.xuexiang.elderguard.fragment.relation.custom.CustomLinkagePrimaryAdapterConfig;
import com.xuexiang.elderguard.fragment.relation.eleme.ElemeGroupedItem;
import com.xuexiang.elderguard.fragment.relation.eleme.ElemeSecondaryAdapterConfig;
import com.xuexiang.elderguard.utils.DataProvider;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import butterknife.BindView;

@Page(name = "通讯录")
public class LinkageRecyclerViewElemeFragment extends BaseFragment implements CustomLinkagePrimaryAdapterConfig.OnPrimaryItemClickListener, ElemeSecondaryAdapterConfig.OnSecondaryItemClickListener {

    @BindView(R.id.linkage)
    LinkageRecyclerView linkage;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_linkage_recyclerview;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("切换") {
            @SingleClick
            @Override
            public void performAction(View view) {
                if (linkage != null) {
                    linkage.setGridMode(!linkage.isGridMode());
                }
            }
        });
        return titleBar;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        System.out.println("**************");
        System.out.println(DataProvider.getElemeGroupItems().toString());
        linkage.init(DataProvider.getElemeGroupItems(), new CustomLinkagePrimaryAdapterConfig(this), new ElemeSecondaryAdapterConfig(this));
    }

    @Override
    public void onPrimaryItemClick(LinkagePrimaryViewHolder holder, View view, String title) {
        SnackbarUtils.Short(view, title).show();
    }

    @Override
    public void onSecondaryItemClick(LinkageSecondaryViewHolder holder, ViewGroup view, BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {
        SnackbarUtils.Short(view, item.info.getTitle()).show();
    }

    @Override
    public void onGoodAdd(View view, BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {

    }
}
