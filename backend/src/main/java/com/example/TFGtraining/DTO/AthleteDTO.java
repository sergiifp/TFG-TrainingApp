package com.example.TFGtraining.DTO;

import com.example.TFGtraining.Athlete.Athlete;
import java.util.Date;

public class AthleteDTO {
    private String username;
    private String description;
    private Integer age;
    private Date sinceDate;

    private Integer fcMax;
    private Integer weight;
    private Integer height;

    private Integer enrolledPlans;
    private Integer sessionsDone;
    private Integer totalKm;

    public AthleteDTO(Athlete athlete, Integer enrolledPlans, Integer sessionsDone, Integer totalKm) {
        username = athlete.getUsername();
        description = athlete.getDescription();
        age = athlete.getAge();
        sinceDate = athlete.getSinceDate();
        fcMax = athlete.getFcMax();
        weight = athlete.getWeight();
        height = athlete.getHeight();
        this.enrolledPlans = enrolledPlans;
        this.sessionsDone = sessionsDone;
        this.totalKm = totalKm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getSinceDate() {
        return sinceDate;
    }

    public void setSinceDate(Date sinceDate) {
        this.sinceDate = sinceDate;
    }

    public Integer getFcMax() {
        return fcMax;
    }

    public void setFcMax(Integer fcMax) {
        this.fcMax = fcMax;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getEnrolledPlans() {
        return enrolledPlans;
    }

    public void setEnrolledPlans(Integer enrolledPlans) {
        this.enrolledPlans = enrolledPlans;
    }

    public Integer getSessionsDone() {
        return sessionsDone;
    }

    public void setSessionsDone(Integer sessionsDone) {
        this.sessionsDone = sessionsDone;
    }

    public Integer getTotalKm() {
        return totalKm;
    }

    public void setTotalKm(Integer totalKm) {
        this.totalKm = totalKm;
    }
}
