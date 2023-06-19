package com.example.TFGtraining.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ModifyTrainerDTO {
    @NotEmpty
    private String description;
    @NotNull
    private Integer age;
    private String password;
    @NotNull
    private String typeOfActivityPlans;
    @NotNull
    private List<String> languages;

    public ModifyTrainerDTO() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
