package com.example.TFGtraining.DTO;

import java.util.Date;

public class BriefTrainerDTO {

    private String username;
    private Integer age;
    private Date sinceDate;
    private String typeOfActivityPlans;

    public BriefTrainerDTO(String username, Integer age, Date sinceDate, String typeOfActivityPlans) {
        this.username = username;
        this.age = age;
        this.sinceDate = sinceDate;
        this.typeOfActivityPlans = typeOfActivityPlans;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getSinceDate() {
        return sinceDate;
    }

    public void setSinceDate(Date sinceDate) {
        this.sinceDate = sinceDate;
    }

    public String getTypeOfActivityPlans() {
        return typeOfActivityPlans;
    }

    public void setTypeOfActivityPlans(String typeOfActivityPlans) {
        this.typeOfActivityPlans = typeOfActivityPlans;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
