package com.maning.gankmm.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maning.gankmm.R;
import com.maning.gankmm.bean.mob.MobCarEntity;
import com.maning.gankmm.listeners.OnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by maning on 16/5/17.
 */
public class RecycleCarListSonAdapter extends RecyclerView.Adapter<RecycleCarListSonAdapter.MyViewHolder> {

    private Context context;
    private List<MobCarEntity.SonBean> mDatas;
    private LayoutInflater layoutInflater;

    public RecycleCarListSonAdapter(Context context, List<MobCarEntity.SonBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflate = layoutInflater.inflate(R.layout.item_car_list, parent, false);

        return new MyViewHolder(inflate);
    }

    public MobCarEntity.SonBean getItemData(int position) {
        return mDatas.get(position);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {
        viewHolder.tvTitle.setVisibility(View.GONE);

        MobCarEntity.SonBean sonBean = mDatas.get(position);
        String type = sonBean.getType();
        String car = sonBean.getCar();

        if (position == 0) {
            viewHolder.tvTitle.setVisibility(View.VISIBLE);
            viewHolder.tvTitle.setText(car);
        } else {
            MobCarEntity.SonBean sonBean2 = mDatas.get(position - 1);
            if (!car.equals(sonBean2.getCar())) {
                viewHolder.tvTitle.setVisibility(View.VISIBLE);
                viewHolder.tvTitle.setText(car);
            }
        }

        viewHolder.tvName.setText(type);
        viewHolder.tvName.setOnClickListener(new View.OnClickListener() {
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

        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_name)
        TextView tvName;

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
