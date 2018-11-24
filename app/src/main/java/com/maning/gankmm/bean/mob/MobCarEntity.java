package com.maning.gankmm.bean.mob;

import java.io.Serializable;
import java.util.List;

/**
 * Created by maning on 2017/6/6.
 */

public class MobCarEntity implements Serializable{


    /**
     * name : AC Schnitzer
     * son : [{"car":"AC Schnitzer","type":"AC Schnitzer X5"}]
     */

    private String name;
    private List<SonBean> son;

    private String pinyin;
    private boolean showSon = false;

    public boolean isShowSon() {
        return showSon;
    }

    public void setShowSon(boolean showSon) {
        this.showSon = showSon;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SonBean> getSon() {
        return son;
    }

    public void setSon(List<SonBean> son) {
        this.son = son;
    }

    public static class SonBean implements Serializable{
        /**
         * car : AC Schnitzer
         * type : AC Schnitzer X5
         */

        private String car;
        private String type;

        public String getCar() {
            return car;
        }

        public void setCar(String car) {
            this.car = car;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
