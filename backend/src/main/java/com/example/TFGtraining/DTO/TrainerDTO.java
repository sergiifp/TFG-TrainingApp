package com.example.TFGtraining.DTO;

import com.example.TFGtraining.Trainer.Trainer;

import java.util.Date;
import java.util.List;

public class TrainerDTO {
    private String username;
    private String description;
    private Integer age;
    private Date sinceDate;

    private String typeOfActivityPlans;
    private List<String> languages;

    public TrainerDTO(Trainer trainer) {
        username = trainer.getUsername();
        description = trainer.getDescription();
        age = trainer.getAge();
        sinceDate = trainer.getSinceDate();
        typeOfActivityPlans = trainer.getTypeOfActivityPlans();
        languages = trainer.getLanguages();
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

    public String getTypeOfActivityPlans() {
        return typeOfActivityPlans;
    }

    public void setTypeOfActivityPlans(String typeOfActivityPlans) {
        this.typeOfActivityPlans = typeOfActivityPlans;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }
}
