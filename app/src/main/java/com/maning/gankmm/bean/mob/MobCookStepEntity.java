package com.maning.gankmm.bean.mob;

import java.io.Serializable;

/**
 * Created by maning on 2017/6/8.
 * 烹饪步骤
 */

public class MobCookStepEntity implements Serializable {

    private String img;
    private String step;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return "MobCookStepEntity{" +
                "img='" + img + '\'' +
                ", step='" + step + '\'' +
                '}';
    }
}
