package com.maning.gankmm.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maning.gankmm.R;
import com.maning.gankmm.skin.SkinManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by maning on 16/5/17.
 */
public class RecycleCitysAdapter extends RecyclerView.Adapter<RecycleCitysAdapter.MyViewHolder> {

    private Context context;
    private List<String> mDatas;
    private LayoutInflater layoutInflater;
    private int currentPosition = -1;
    //标记：0-左边，1：右边
    private int flag = 0;

    public RecycleCitysAdapter(Context context, List<String> mDatas, int flag) {
        this.context = context;
        this.flag = flag;
        this.mDatas = mDatas;
        layoutInflater = LayoutInflater.from(this.context);
    }

    public void updateDatas(List<String> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflate = layoutInflater.inflate(R.layout.item_city_choose, parent, false);

        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {

        viewHolder.tv_title.setText(mDatas.get(position));

        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPosition = position;
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, position);
                    notifyDataSetChanged();
                }
            });
        }

        if (flag == 0) {
            viewHolder.tv_title.setTextColor(context.getResources().getColor(R.color.black_text2_color));
            viewHolder.item_bg.setBackgroundResource(R.color.itemGrayBg);
            viewHolder.line.setVisibility(View.GONE);
            if (currentPosition == position) {
                viewHolder.tv_title.setTextColor(context.getResources().getColor(R.color.main_color));
                viewHolder.item_bg.setBackgroundResource(R.color.white);
                viewHolder.line.setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.item_bg.setBackgroundResource(R.color.white);
            viewHolder.line.setVisibility(View.GONE);
        }

    }

    public String getPositionValue(int position) {
        return mDatas.get(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_title)
        TextView tv_title;
        @Bind(R.id.line)
        TextView line;
        @Bind(R.id.item_bg)
        RelativeLayout item_bg;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }
}
