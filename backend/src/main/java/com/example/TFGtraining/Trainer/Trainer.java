package com.example.TFGtraining.Trainer;

import com.example.TFGtraining.Club.Club;
import com.example.TFGtraining.TrainingPlan.TrainingPlan;
import com.example.TFGtraining.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("T")
public class Trainer extends User {
    @Column
    private String typeOfActivityPlans;

    @ElementCollection
    private List<String> languages;

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "trainer")
    private List<TrainingPlan> trainingPlans;

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "trainerCreator")
    private List<Club> createdClubs;

    public Trainer(String typeOfActivityPlans, List<String> languages) {
        this.typeOfActivityPlans = typeOfActivityPlans;
        this.languages = languages;
    }

    public Trainer() {
        this.typeOfActivityPlans = "road";
    }

    public String getType(){
        return "Trainer";
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

    public List<TrainingPlan> getTrainingPlans() {
        return trainingPlans;
    }

    public void setTrainingPlans(List<TrainingPlan> trainingPlans) {
        this.trainingPlans = trainingPlans;
    }

    public void addTrainingPlan(TrainingPlan trainingPlan){
        this.trainingPlans.add(trainingPlan);
    }

    public List<Club> getCreatedClubs() {
        return createdClubs;
    }

    public void setCreatedClubs(List<Club> createdClubs) {
        this.createdClubs = createdClubs;
    }
}
