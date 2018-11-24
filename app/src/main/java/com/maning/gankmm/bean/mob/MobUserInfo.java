package com.maning.gankmm.bean.mob;

import java.io.Serializable;

/**
 * Created by maning on 2017/7/12.
 */

public class MobUserInfo implements Serializable{


    private String userName;
    private String userPsd;
    private String userEmail;
    private String token;
    private String uid;
    private String avatarLocal;
    private String avatarNet;
    private String sex;
    private String birth;
    private String signature;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPsd() {
        return userPsd;
    }

    public void setUserPsd(String userPsd) {
        this.userPsd = userPsd;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAvatarLocal() {
        return avatarLocal;
    }

    public void setAvatarLocal(String avatarLocal) {
        this.avatarLocal = avatarLocal;
    }

    public String getAvatarNet() {
        return avatarNet;
    }

    public void setAvatarNet(String avatarNet) {
        this.avatarNet = avatarNet;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "MobUserInfo{" +
                "userName='" + userName + '\'' +
                ", userPsd='" + userPsd + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", token='" + token + '\'' +
                ", uid='" + uid + '\'' +
                ", avatarLocal='" + avatarLocal + '\'' +
                ", avatarNet='" + avatarNet + '\'' +
                ", sex='" + sex + '\'' +
                ", birth='" + birth + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
