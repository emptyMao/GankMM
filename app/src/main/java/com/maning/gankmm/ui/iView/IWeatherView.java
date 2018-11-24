package com.maning.gankmm.ui.iView;

import com.maning.gankmm.bean.AppUpdateInfo;
import com.maning.gankmm.bean.CalendarInfoEntity;
import com.maning.gankmm.bean.WeatherBeseEntity;

/**
 * Created by maning on 16/6/21.
 */
public interface IWeatherView {

    void showToast(String msg);

    void initWeatherInfo(WeatherBeseEntity.WeatherBean weatherEntity);

    void overRefresh();

    void updateCalendarInfo(CalendarInfoEntity calendarInfoEntity);

}
