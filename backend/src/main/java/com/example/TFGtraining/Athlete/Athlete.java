package com.example.TFGtraining.Athlete;

import com.example.TFGtraining.Club.Club;
import com.example.TFGtraining.PlanProgress.PlanProgress;
import com.example.TFGtraining.SessionResult.SessionResult;
import com.example.TFGtraining.TrainingPlan.TrainingPlan;
import com.example.TFGtraining.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Entity
@DiscriminatorValue("A")
public class Athlete extends User {

    @Column
    private Integer fcMax;
    @Column
    private Integer weight;
    @Column
    private Integer height;
    @JsonIgnore
    @OneToMany(mappedBy="athlete", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<PlanProgress> enrolledPlans;
    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name="club_athlete",
            joinColumns = @JoinColumn(name = "athlete_username"),
            inverseJoinColumns = @JoinColumn(name = "club_id"))
    private List<Club> enrolledClubs;

    @JsonIgnore
    @OneToMany(mappedBy="athlete", cascade = {CascadeType.PERSIST})
    private List<SessionResult> results;
    @ManyToMany(mappedBy="likedBy")
    private List<TrainingPlan> likedPlans;

    public Athlete() {
    }
    @PreRemove
    private void deleteLiked(){
        for(TrainingPlan t: likedPlans){
            t.getLikedBy().remove(this);
        }
    }

    public String getType(){
        return "Athlete";
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
    @Lazy(value = true)
    public List<PlanProgress> getEnrolledPlans() {
        return enrolledPlans;
    }

    public void setEnrolledPlans(List<PlanProgress> enrolledPlans) {
        this.enrolledPlans = enrolledPlans;
    }

    @Lazy(value = true)
    public List<Club> getEnrolledClubs() {
        return enrolledClubs;
    }

    public void setEnrolledClubs(List<Club> enrolledClubs) {
        this.enrolledClubs = enrolledClubs;
    }
    @Lazy(value = true)
    public List<SessionResult> getResults() {
        return results;
    }

    public void setResults(List<SessionResult> results) {
        this.results = results;
    }

    public List<TrainingPlan> getLikedPlans() {
        return likedPlans;
    }

    public void setLikedPlans(List<TrainingPlan> likedPlans) {
        this.likedPlans = likedPlans;
    }
}
