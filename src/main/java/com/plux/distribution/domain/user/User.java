package com.plux.distribution.domain.user;

public class User {
    private final UserId userId;
    private UserInfo userInfo;

    public User(UserId userId, UserInfo userInfo) {
        this.userId = userId;
        this.userInfo = userInfo;
    }

    public UserId getUserId() {
        return userId;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
