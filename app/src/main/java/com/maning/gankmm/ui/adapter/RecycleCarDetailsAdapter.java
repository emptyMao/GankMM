package com.maning.gankmm.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.maning.gankmm.R;
import com.maning.gankmm.bean.mob.MobCarDetailsEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by maning on 16/5/17.
 */
public class RecycleCarDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<MobCarDetailsEntity.DetailItem> mDatas;
    private LayoutInflater layoutInflater;
    private MobCarDetailsEntity mobCarDetailsEntity;

    public RecycleCarDetailsAdapter(Context context, List<MobCarDetailsEntity.DetailItem> mDatas, MobCarDetailsEntity mobCarDetailsEntity) {
        this.context = context;
        this.mDatas = mDatas;
        this.mobCarDetailsEntity = mobCarDetailsEntity;
        layoutInflater = LayoutInflater.from(this.context);
    }

    public void updateDatas(List<MobCarDetailsEntity.DetailItem> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0) {
            View inflate = layoutInflater.inflate(R.layout.item_car_detail_image, parent, false);
            return new MyViewHolder2(inflate);
        }
        View inflate = layoutInflater.inflate(R.layout.item_car_details, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolderBase, final int position) {
        if (viewHolderBase instanceof MyViewHolder2) {
            MyViewHolder2 viewHolder2 = (MyViewHolder2) viewHolderBase;


            RequestOptions options = new RequestOptions();
            options.centerCrop();
            options.placeholder(R.drawable.pic_gray_bg);
            Glide.with(context).asBitmap().load(mobCarDetailsEntity.getCarImage())
                    .apply(options)
                    .into(viewHolder2.ivCar);

        } else if (viewHolderBase instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) viewHolderBase;

            viewHolder.rlType.setVisibility(View.GONE);
            viewHolder.ivValue.setVisibility(View.GONE);
            viewHolder.tvValue.setVisibility(View.VISIBLE);

            MobCarDetailsEntity.DetailItem detailItem = mDatas.get(position-1);

            String type = detailItem.getType();
            String name = detailItem.getName();

            // 0：未配置；1：标配。
            String value = detailItem.getValue();
            if ("0".equals(value)) {
                viewHolder.ivValue.setVisibility(View.VISIBLE);
                viewHolder.tvValue.setVisibility(View.GONE);
                viewHolder.ivValue.setBackgroundResource(R.drawable.gank_icon_car_circle2);
            } else if ("1".equals(value)) {
                viewHolder.ivValue.setVisibility(View.VISIBLE);
                viewHolder.tvValue.setVisibility(View.GONE);
                viewHolder.ivValue.setBackgroundResource(R.drawable.gank_icon_car_circle2);
            }

            viewHolder.tvName.setText(name);
            viewHolder.tvValue.setText(value);

            if (position == 1) {
                viewHolder.rlType.setVisibility(View.VISIBLE);
                viewHolder.tvType.setText(type);
            } else {
                MobCarDetailsEntity.DetailItem detailItem2 = mDatas.get(position - 2);
                String type2 = detailItem2.getType();
                if (!type2.equals(type)) {
                    viewHolder.rlType.setVisibility(View.VISIBLE);
                    viewHolder.tvType.setText(type);
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return mDatas.size() + 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_type)
        TextView tvType;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_value)
        TextView tvValue;
        @Bind(R.id.rl_type)
        RelativeLayout rlType;
        @Bind(R.id.iv_value)
        ImageView ivValue;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class MyViewHolder2 extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_car)
        ImageView ivCar;

        public MyViewHolder2(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
