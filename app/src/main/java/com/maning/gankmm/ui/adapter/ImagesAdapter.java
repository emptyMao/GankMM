package com.maning.gankmm.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.maning.gankmm.R;
import com.maning.gankmm.ui.view.PinchImageView;
import com.maning.gankmm.ui.view.ProgressWheel;

import java.util.ArrayList;

/**
 * Created by maning on 16/6/21.
 * 图片浏览
 */
public class ImagesAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<String> mDatas;
    private LayoutInflater layoutInflater;
    private View mCurrentView;

    public ImagesAdapter(Context mContext, ArrayList<String> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        layoutInflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        mCurrentView = (View) object;
    }

    public View getPrimaryItem() {
        return mCurrentView;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        final String imageUrl = mDatas.get(position);
        View inflate = layoutInflater.inflate(R.layout.item_show_image, container, false);
        final ImageView imageView = (ImageView) inflate.findViewById(R.id.imageView);
        final ProgressWheel progressbar = (ProgressWheel) inflate.findViewById(R.id.progressbar);
                Glide
                .with(mContext)
                .load(imageUrl)
                .thumbnail(0.2f)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressbar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressbar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
        container.addView(inflate);

        return inflate;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public Bitmap getCurrentImageViewBitmap() {
        View currentItem = getPrimaryItem();
        if (currentItem == null) {
            return null;
        }
        PinchImageView imageView = (PinchImageView) currentItem.findViewById(R.id.imageView);
        if (imageView == null) {
            return null;
        }
        imageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
        imageView.setDrawingCacheEnabled(false);
        return bitmap;
    }
}
