package com.maning.gankmm.ui.presenter;

/**
 * Created by maning on 16/6/21.
 */
public interface IWeatherPresenter {

    void getCityWeather(String provinceName, String cityName);

    //万年历查询
    void getCalendarInfo();
}
