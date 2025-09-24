package com.plux.distribution.core.workflow.application.frame.settings.user;

import com.plux.distribution.core.workflow.application.utils.JsonDataSerializer;
import com.plux.distribution.core.user.domain.UserInfo;
import org.apache.commons.validator.routines.EmailValidator;

public class UserBuilder {

    private String name;
    private String email;
    private Integer age;
    private String city;
    private String hobby;

    public void setName(String name) {
        this.name = name;
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws IllegalArgumentException {
        if (email != null && !EmailValidator.getInstance().isValid(email)) {
            throw new IllegalArgumentException("Invalid email");
        }
        this.email = email;
    }


    @SuppressWarnings("unused")
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @SuppressWarnings("unused")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @SuppressWarnings("unused")
    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public UserInfo buildUserInfo() {
        if (name == null) {
            throw new IllegalStateException("name required");
        }

        return new UserInfo(name, email, age, city, hobby);
    }

    public static class Serializer extends JsonDataSerializer<UserBuilder> {

        public Serializer() {
            super(UserBuilder.class);
        }
    }
}
