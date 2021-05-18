package com.xuexiang.elderguard.adapter.base;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scwang.smartrefresh.layout.adapter.SmartRecyclerAdapter;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.xuexiang.elderguard.R;
import com.xuexiang.elderguard.entity.EgAcquaintance;

import static com.xuexiang.elderguard.utils.DataProvider.getAcqVisitImgUrl;

public class AcqAdapter extends SmartRecyclerAdapter<EgAcquaintance> {

    private SmartViewHolder.OnViewItemClickListener mOnViewItemClickListener;

    public AcqAdapter() {
        super(R.layout.adapter_member_view_list_item);
    }


    @Override
    protected void onBindViewHolder(SmartViewHolder holder, EgAcquaintance model, int position) {
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

    public AcqAdapter setItemViewOnClickListener(SmartViewHolder.OnViewItemClickListener onViewItemClickListener) {
        mOnViewItemClickListener = onViewItemClickListener;
        return this;
    }

}
