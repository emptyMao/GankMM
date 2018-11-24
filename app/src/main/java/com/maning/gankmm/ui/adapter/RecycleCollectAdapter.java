package com.maning.gankmm.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ldoublem.thumbUplib.ThumbUpView;
import com.maning.gankmm.R;
import com.maning.gankmm.bean.GankEntity;
import com.maning.gankmm.constant.Constants;
import com.maning.gankmm.db.CollectDao;
import com.maning.gankmm.utils.MySnackbar;
import com.socks.library.KLog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by maning on 16/5/17.
 */
public class RecycleCollectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<GankEntity> commonDataResults;
    private LayoutInflater layoutInflater;

    public RecycleCollectAdapter(Context context, List<GankEntity> commonDataResults) {
        this.context = context;
        this.commonDataResults = commonDataResults;
        layoutInflater = LayoutInflater.from(this.context);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void updateDatas(List<GankEntity> commonDataResults) {
        this.commonDataResults = commonDataResults;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View inflate = layoutInflater.inflate(R.layout.item_collect_pic, parent, false);
            return new MyViewHolder(inflate);
        } else {
            View inflate = layoutInflater.inflate(R.layout.item_collect, parent, false);
            return new MyViewHolder1(inflate);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

        final GankEntity resultsEntity = commonDataResults.get(position);

        if (viewHolder instanceof MyViewHolder1) {
            final MyViewHolder1 myViewHolder1 = (MyViewHolder1) viewHolder;

            myViewHolder1.tvShowTitle.setText(resultsEntity.getDesc());
            myViewHolder1.tvShowWho.setText(resultsEntity.getWho());
            myViewHolder1.tvShowTime.setText(resultsEntity.getPublishedAt().split("T")[0]);

            //图片展示
            String imageUrl = "";
            List<String> images = resultsEntity.getImages();
            if (images != null && images.size() > 0) {
                imageUrl = images.get(0);
            }
            if (TextUtils.isEmpty(imageUrl)) {
                myViewHolder1.ivShow.setVisibility(View.GONE);
            } else {
                myViewHolder1.ivShow.setVisibility(View.VISIBLE);
                RequestOptions options = new RequestOptions();
                options.centerCrop();
                options.placeholder(R.drawable.pic_gray_bg);
                Glide.with(context)
                        .load(imageUrl)
                        .into(myViewHolder1.ivShow);
            }

            //查询是否存在收藏
            boolean isCollect = new CollectDao().queryOneCollectByID(resultsEntity.get_id());
            if (isCollect) {
                myViewHolder1.btnCollect.setLike();
            } else {
                myViewHolder1.btnCollect.setUnlike();
            }

            myViewHolder1.btnCollect.setOnThumbUp(new ThumbUpView.OnThumbUp() {
                @Override
                public void like(boolean like) {
                    if (like) {
                        boolean insertResult = new CollectDao().insertOneCollect(resultsEntity);
                        if (insertResult) {
                            MySnackbar.makeSnackBarBlack(myViewHolder1.tvShowTime, "收藏成功");
                        } else {
                            MySnackbar.makeSnackBarRed(myViewHolder1.tvShowTime, "收藏失败");
                        }
                    } else {
                        boolean deleteResult = new CollectDao().deleteOneCollect(resultsEntity.get_id());
                        if (deleteResult) {
                            MySnackbar.makeSnackBarBlack(myViewHolder1.tvShowTime, "取消收藏成功");
                        } else {
                            MySnackbar.makeSnackBarRed(myViewHolder1.tvShowTime, "取消收藏失败");
                        }
                    }
                }
            });


        } else if (viewHolder instanceof MyViewHolder) {
            final MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

            myViewHolder.tvShowWho.setText(resultsEntity.getWho());
            myViewHolder.tvShowTime.setText(resultsEntity.getPublishedAt().split("T")[0]);

            String url = resultsEntity.getUrl();
            KLog.i("图片地址：" + url);
            //图片显示
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            options.placeholder(R.drawable.pic_gray_bg);
            Glide.with(context)
                    .load(url)
                    .apply(options)
                    .into(myViewHolder.image);

            //查询是否存在收藏
            boolean isCollect = new CollectDao().queryOneCollectByID(resultsEntity.get_id());
            if (isCollect) {
                myViewHolder.btnCollect.setLike();
            } else {
                myViewHolder.btnCollect.setUnlike();
            }

            myViewHolder.btnCollect.setOnThumbUp(new ThumbUpView.OnThumbUp() {
                @Override
                public void like(boolean like) {
                    if (like) {
                        boolean insertResult = new CollectDao().insertOneCollect(resultsEntity);
                        if (insertResult) {
                            MySnackbar.makeSnackBarBlack(myViewHolder.tvShowTime, "收藏成功");
                        } else {
                            MySnackbar.makeSnackBarRed(myViewHolder.tvShowTime, "收藏失败");
                        }
                    } else {
                        boolean deleteResult = new CollectDao().deleteOneCollect(resultsEntity.get_id());
                        if (deleteResult) {
                            MySnackbar.makeSnackBarBlack(myViewHolder.tvShowTime, "取消收藏成功");
                        } else {
                            MySnackbar.makeSnackBarRed(myViewHolder.tvShowTime, "取消收藏失败");
                        }
                    }
                }
            });

        }

        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, position);
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (Constants.FlagWelFare.equals(commonDataResults.get(position).getType())) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return commonDataResults.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tvShowWho)
        TextView tvShowWho;
        @Bind(R.id.tvShowTime)
        TextView tvShowTime;
        @Bind(R.id.btn_collect)
        ThumbUpView btnCollect;
        @Bind(R.id.image)
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    static class MyViewHolder1 extends RecyclerView.ViewHolder {

        @Bind(R.id.tvShowWho)
        TextView tvShowWho;
        @Bind(R.id.tvShowTitle)
        TextView tvShowTitle;
        @Bind(R.id.tvShowTime)
        TextView tvShowTime;
        @Bind(R.id.btn_collect)
        ThumbUpView btnCollect;
        @Bind(R.id.iv_show)
        ImageView ivShow;

        public MyViewHolder1(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }
}
