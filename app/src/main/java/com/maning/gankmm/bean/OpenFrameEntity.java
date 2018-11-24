package com.maning.gankmm.bean;

/**
 * Created by maning on 2017/7/15.
 */

public class OpenFrameEntity {

    public OpenFrameEntity(String frameName, String frameGithubUrl) {
        this.frameName = frameName;
        this.frameGithubUrl = frameGithubUrl;
    }

    private String frameName;
    private String frameGithubUrl;

    public String getFrameName() {
        return frameName;
    }

    public void setFrameName(String frameName) {
        this.frameName = frameName;
    }

    public String getFrameGithubUrl() {
        return frameGithubUrl;
    }

    public void setFrameGithubUrl(String frameGithubUrl) {
        this.frameGithubUrl = frameGithubUrl;
    }

    @Override
    public String toString() {
        return "OpenFrameEntity{" +
                "frameName='" + frameName + '\'' +
                ", frameGithubUrl='" + frameGithubUrl + '\'' +
                '}';
    }
}
