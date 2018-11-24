package com.maning.gankmm.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maning.gankmm.R;
import com.maning.gankmm.bean.mob.MobCarItemEntity;
import com.maning.gankmm.listeners.OnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by maning on 16/5/17.
 */
public class RecycleCarItemAdapter extends RecyclerView.Adapter<RecycleCarItemAdapter.MyViewHolder> {

    private Context context;
    private List<MobCarItemEntity> mDatas;
    private LayoutInflater layoutInflater;

    public RecycleCarItemAdapter(Context context, List<MobCarItemEntity> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        layoutInflater = LayoutInflater.from(this.context);
    }

    public void updateDatas(List<MobCarItemEntity> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflate = layoutInflater.inflate(R.layout.item_car_item, parent, false);

        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {

        MobCarItemEntity mobCarItemEntity = mDatas.get(position);

        viewHolder.tvGuidePrice.setText(mobCarItemEntity.getGuidePrice());
        viewHolder.tvSeriesName.setText(mobCarItemEntity.getSeriesName());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickLitener != null) {
                    mOnItemClickLitener.onItemClick(view, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_guidePrice)
        TextView tvGuidePrice;
        @Bind(R.id.tv_seriesName)
        TextView tvSeriesName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private OnItemClickListener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

}
