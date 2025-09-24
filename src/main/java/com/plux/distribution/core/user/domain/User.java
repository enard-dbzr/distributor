package com.plux.distribution.core.user.domain;

public class User {
    private final UserId id;
    private UserInfo userInfo;

    public User(UserId id, UserInfo userInfo) {
        this.id = id;
        this.userInfo = userInfo;
    }

    public UserId getId() {
        return id;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
