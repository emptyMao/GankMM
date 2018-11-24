package com.maning.gankmm.ui.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.maning.gankmm.R;
import com.maning.gankmm.app.MyApplication;
import com.maning.gankmm.bean.AppUpdateInfo;
import com.maning.gankmm.bean.WeatherBeseEntity;
import com.maning.gankmm.constant.Constants;
import com.maning.gankmm.http.GankApi;
import com.maning.gankmm.http.MyCallBack;
import com.maning.gankmm.ui.iView.IMainView;
import com.maning.gankmm.ui.presenter.IMainPresenter;
import com.maning.gankmm.utils.LocationUtils;
import com.maning.gankmm.utils.MySnackbar;
import com.maning.gankmm.utils.MyToast;
import com.maning.gankmm.utils.NetUtils;
import com.maning.gankmm.utils.PermissionUtils;
import com.maning.gankmm.utils.SharePreUtil;
import com.socks.library.KLog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by maning on 16/6/21.
 */
public class MainPresenterImpl extends BasePresenterImpl<IMainView> implements IMainPresenter {

    private Context context;

    private String cityName;

    private MyCallBack httpCallBack = new MyCallBack() {
        @Override
        public void onSuccessList(int what, List results) {
            if (mView == null) {
                return;
            }
            if (results == null) {
                return;
            }
            switch (what) {
                case 0x003:
                    List<WeatherBeseEntity.WeatherBean> weathers = results;
                    if (weathers.size() > 0) {
                        WeatherBeseEntity.WeatherBean resultBean = weathers.get(0);
                        if (resultBean != null) {
                            mView.initWeatherInfo(resultBean);
                        }
                    }
                    break;
            }
        }

        @Override
        public void onSuccess(int what, Object result) {
            if (mView == null) {
                return;
            }
            switch (what) {
                case 0x001:
                    if (result == null) {
                        return;
                    }
                    //获取当前APP的版本号
                    int newVersion;
                    AppUpdateInfo appUpdateInfo;
                    try {
                        appUpdateInfo = (AppUpdateInfo) result;
                        newVersion = Integer.parseInt(appUpdateInfo.getBuild());
                    } catch (Exception e) {
                        newVersion = 1;
                        appUpdateInfo = new AppUpdateInfo();
                    }
                    if (MyApplication.getVersionCode() < newVersion) {
                        SharePreUtil.saveBooleanData(context, Constants.SPAppUpdate + MyApplication.getVersionCode(), true);
                        //需要版本更新
                        if (mView != null) {
                            mView.showAppUpdateDialog(appUpdateInfo);
                        }
                    }

                    break;
            }
        }

        @Override
        public void onFail(int what, String result) {
            if (!TextUtils.isEmpty(result)) {
                mView.showToast(result);
            }
        }
    };
    private String provinceName;


    public MainPresenterImpl(Context context, IMainView iMainView) {
        this.context = context;
        attachView(iMainView);
    }

    @Override
    public void initFeedBack() {

    }

    @Override
    public void initAppUpdate() {
        if (NetUtils.hasNetWorkConection(context)) {
            //版本更新
            GankApi.getAppUpdateInfo(0x001, httpCallBack);
        }
    }

    @Override
    public void getLocationInfo() {
        PermissionUtils.checkLocationPermission(context, new PermissionUtils.PermissionCallBack() {
            @Override
            public void onGranted() {
                //高德获取位置信息
                requestLocationInfo();
            }

            @Override
            public void onDenied() {
                mView.showToast("获取定位权限失败，请前往设置页面打开定位权限");
            }
        });

    }

    @Override
    public void getCitys() {
        GankApi.getCitys(0x002, httpCallBack);
    }

    @Override
    public void destroyLocation() {
        //销毁定位客户端，同时销毁本地定位服务。
        LocationUtils.destory();
    }

    @Override
    public void initDatas() {
        SharePreUtil.saveIntData(context, "未知", R.drawable.icon_weather_none);
        SharePreUtil.saveIntData(context, "晴", R.drawable.icon_weather_sunny);
        SharePreUtil.saveIntData(context, "阴", R.drawable.icon_weather_cloudy);
        SharePreUtil.saveIntData(context, "多云", R.drawable.icon_weather_cloudy);
        SharePreUtil.saveIntData(context, "少云", R.drawable.icon_weather_cloudy);
        SharePreUtil.saveIntData(context, "晴间多云", R.drawable.icon_weather_cloudytosunny);
        SharePreUtil.saveIntData(context, "局部多云", R.drawable.icon_weather_cloudy);
        SharePreUtil.saveIntData(context, "雨", R.drawable.icon_weather_rain);
        SharePreUtil.saveIntData(context, "小雨", R.drawable.icon_weather_rain);
        SharePreUtil.saveIntData(context, "中雨", R.drawable.icon_weather_rain);
        SharePreUtil.saveIntData(context, "大雨", R.drawable.icon_weather_rain);
        SharePreUtil.saveIntData(context, "阵雨", R.drawable.icon_weather_rain);
        SharePreUtil.saveIntData(context, "雷阵雨", R.drawable.icon_weather_thunderstorm);
        SharePreUtil.saveIntData(context, "霾", R.drawable.icon_weather_haze);
        SharePreUtil.saveIntData(context, "雾", R.drawable.icon_weather_fog);
        SharePreUtil.saveIntData(context, "雨夹雪", R.drawable.icon_weather_snowrain);
    }


    private void requestLocationInfo() {
        LocationUtils.getLocation(context, new LocationUtils.OnLocationListener() {
            @Override
            public void onSuccess(AMapLocation aMapLocation) {
                //获取城市
                cityName = aMapLocation.getCity();
                if (cityName.endsWith("市")) {
                    cityName = cityName.substring(0, cityName.length() - 1);
                }
                provinceName = aMapLocation.getProvince();
                if (provinceName.endsWith("省") || provinceName.endsWith("市")) {
                    provinceName = provinceName.substring(0, provinceName.length() - 1);
                }
                getCityWeather(provinceName, cityName);
            }

            @Override
            public void onFail(int errorCode, String errorMsg) {
                mView.showToast(errorMsg);
            }

            @Override
            public void onComplete() {

            }
        });
    }


    @Override
    public void getCityWeather(String provinceName, String cityName) {
        mView.updateLocationInfo(provinceName, cityName);
        GankApi.getCityWeather(cityName, provinceName, 0x003, httpCallBack);
    }

}
