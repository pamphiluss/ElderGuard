package com.xuexiang.elderguard.adapter.base;

import com.scwang.smartrefresh.layout.adapter.SmartRecyclerAdapter;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.xuexiang.elderguard.R;
import com.xuexiang.elderguard.entity.EgRelationship;

public class RelationAdapter extends SmartRecyclerAdapter<EgRelationship> {

    private SmartViewHolder.OnViewItemClickListener mOnViewItemClickListener;

    public RelationAdapter() {
        super(R.layout.adapter_relation_item);
    }


    @Override
    protected void onBindViewHolder(SmartViewHolder holder, EgRelationship model, int position) {
        if (model != null) {

            holder.text(R.id.visit_time_rela, model.getCrdate());
            holder.text(R.id.visit_name_rela, model.getName());
        }
    }

    public RelationAdapter setItemViewOnClickListener(SmartViewHolder.OnViewItemClickListener onViewItemClickListener) {
        mOnViewItemClickListener = onViewItemClickListener;
        return this;
    }

}
