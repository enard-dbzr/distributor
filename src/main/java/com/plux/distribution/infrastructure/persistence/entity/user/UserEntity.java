package com.plux.distribution.infrastructure.persistence.entity.user;

import com.plux.distribution.core.user.domain.User;
import com.plux.distribution.core.user.domain.UserId;
import com.plux.distribution.core.user.domain.UserInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String email;
    private Integer age;
    private String city;
    private String hobby;

    public static UserEntity create(UserInfo userInfo) {
        var entity = new UserEntity();

        entity.setUserInfo(userInfo);

        return entity;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.name = userInfo.name();
        this.age = userInfo.age();
        this.city = userInfo.city();
        this.hobby = userInfo.hobby();
    }

    public User toModel() {
        return new User(new UserId(id), new UserInfo(name, email, age, city, hobby));
    }

    public Long getId() {
        return id;
    }
}
