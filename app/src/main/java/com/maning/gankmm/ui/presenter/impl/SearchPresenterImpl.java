package com.maning.gankmm.ui.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

import com.maning.gankmm.bean.SearchBean;
import com.maning.gankmm.http.GankApi;
import com.maning.gankmm.http.MyCallBack;
import com.maning.gankmm.ui.iView.ISearchView;
import com.maning.gankmm.ui.presenter.ISearchPresenter;

import java.util.List;

/**
 * Created by maning on 2017/3/1.
 */

public class SearchPresenterImpl extends BasePresenterImpl<ISearchView> implements ISearchPresenter {

    private Context context;
    private int pageSize = 20;
    private int pageIndex = 1;
    private String keyWord;
    private List<SearchBean> gankSearchList;

    private MyCallBack httpCallBack = new MyCallBack() {
        @Override
        public void onSuccessList(int what, List results) {
            if (mView == null) {
                return;
            }
            if (what == 0x001) {
                mView.hideBaseProgressDialog();
                gankSearchList = results;
                if (gankSearchList != null && gankSearchList.size() > 0) {
                    mView.setSearchList(gankSearchList);
                }
                //判断是不是可以使用上啦加载更多功能
                if (gankSearchList == null || gankSearchList.size() == 0 || gankSearchList.size() < pageIndex * pageSize) {
                    mView.setLoadMoreEnabled(false);
                } else {
                    mView.setLoadMoreEnabled(true);
                }
                pageIndex = 1;
                pageIndex++;
                mView.overRefresh();
            } else if (what == 0x002) {
                gankSearchList.addAll(results);
                if (gankSearchList != null && gankSearchList.size() > 0) {
                    mView.setSearchList(gankSearchList);
                }
                pageIndex++;
                mView.overRefresh();
            }
        }

        @Override
        public void onSuccess(int what, Object result) {

        }

        @Override
        public void onFail(int what, String result) {
            if (mView == null) {
                return;
            }
            mView.hideBaseProgressDialog();
            mView.overRefresh();
            if (!TextUtils.isEmpty(result)) {
                mView.showToast(result);
            }
        }
    };

    public SearchPresenterImpl(Context context, ISearchView iSearchView) {
        this.context = context;
        attachView(iSearchView);
    }

    @Override
    public void searchDatas(String keyWords) {
        if (mView == null) {
            return;
        }

        //开始搜索
        if (TextUtils.isEmpty(keyWords)) {
            mView.showToast("关键字不能为空");
            return;
        }
        keyWord = keyWords;
        //获取数据
        mView.showBaseProgressDialog("搜索中...");
        GankApi.getSearchData(keyWord, "all", pageSize, pageIndex, 0x001, httpCallBack);

    }

    @Override
    public void loadMoreDatas() {
        GankApi.getSearchData(keyWord, "all", pageSize, pageIndex, 0x002, httpCallBack);
    }

    @Override
    public void itemClick(int position) {

    }
}
