package com.maning.gankmm.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.maning.gankmm.R;
import com.maning.gankmm.skin.SkinManager;
import com.maning.gankmm.ui.base.BaseActivity;
import com.maning.gankmm.ui.fragment.collect.CollectFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的收藏页面
 */
public class CollectActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    private CollectFragment mCollectFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        ButterKnife.bind(this);

        initMyToolBar();

        initContentView();
    }

    private void initContentView() {

        // 开启一个Fragment事务
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mCollectFragment = CollectFragment.newInstance();
        fragmentTransaction.add(R.id.frame_content, mCollectFragment);
        fragmentTransaction.commit();
    }


    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "我的收藏", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "我的收藏", R.drawable.gank_ic_back_night);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
