package com.maning.gankmm.ui.iView;

import com.maning.gankmm.bean.AppUpdateInfo;
import com.maning.gankmm.bean.WeatherBeseEntity;

/**
 * Created by maning on 16/6/21.
 */
public interface IMainView {

    void showToast(String msg);

    void showAppUpdateDialog(AppUpdateInfo appUpdateInfo);

    void initWeatherInfo(WeatherBeseEntity.WeatherBean weatherEntity);

    void updateLocationInfo(String provinceName, String cityName);

}
