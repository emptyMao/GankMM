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
public class RecycleCarListAdapter extends RecyclerView.Adapter<RecycleCarListAdapter.MyViewHolder> {

    private Context context;
    private List<MobCarEntity> mDatas;
    private LayoutInflater layoutInflater;

    public RecycleCarListAdapter(Context context, List<MobCarEntity> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        layoutInflater = LayoutInflater.from(this.context);
    }

    public void updateDatas(List<MobCarEntity> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    public int getLetterPosition(String letter) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getPinyin().substring(0, 1).equals(letter)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflate = layoutInflater.inflate(R.layout.item_car_list, parent, false);

        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {
        viewHolder.tvTitle.setVisibility(View.GONE);

        final MobCarEntity mobCarEntity = mDatas.get(position);
        String pinyin = mobCarEntity.getPinyin().substring(0, 1);
        String name = mobCarEntity.getName();
        viewHolder.tvName.setText(name);
        if (position == 0) {
            viewHolder.tvTitle.setVisibility(View.VISIBLE);
            viewHolder.tvTitle.setText(pinyin);
        } else {
            MobCarEntity mobCarEntity2 = mDatas.get(position - 1);
            String pinyin2 = mobCarEntity2.getPinyin().substring(0, 1);
            if (!pinyin2.equals(pinyin)) {
                viewHolder.tvTitle.setVisibility(View.VISIBLE);
                viewHolder.tvTitle.setText(pinyin);
            }
        }

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
