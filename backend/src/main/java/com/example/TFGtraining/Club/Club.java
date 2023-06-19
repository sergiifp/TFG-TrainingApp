package com.example.TFGtraining.Club;

import com.example.TFGtraining.Athlete.Athlete;
import com.example.TFGtraining.Event.Event;
import com.example.TFGtraining.Trainer.Trainer;
import com.example.TFGtraining.TrainingPlan.TrainingPlan;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String name;
    @JsonIgnore
    @Column
    private String password;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="trainer_username", referencedColumnName="username")
    private Trainer trainerCreator;
    @JsonIgnore
    @ManyToMany(mappedBy = "enrolledClubs")
    private List<Athlete> enrolledAthletes;
    @JsonIgnore
    @OneToMany(mappedBy="ownerClub")
    private List<TrainingPlan> customPlans;
    @JsonIgnore
    @OneToMany(mappedBy="club", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<Event> events;

    public Club(String name, String password, Trainer user) {
        this.name = name;
        this.password = password;
        this.trainerCreator = user;
    }

    public Club() {
        enrolledAthletes = new ArrayList<>();
        customPlans = new ArrayList<>();
        events = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Trainer getTrainerCreator() {
        return trainerCreator;
    }

    public void setTrainerCreator(Trainer trainerCreator) {
        this.trainerCreator = trainerCreator;
    }

    public List<Athlete> getEnrolledAthletes() {
        return enrolledAthletes;
    }

    public void setEnrolledAthletes(List<Athlete> enrolledAthletes) {
        this.enrolledAthletes = enrolledAthletes;
    }

    @PreRemove
    private void removeAthletesEnrolled(){
        for (Athlete ath: this.enrolledAthletes){
            ath.getEnrolledClubs().remove(this);
        }
    }

    public List<TrainingPlan> getCustomPlans() {
        return customPlans;
    }

    public void setCustomPlans(List<TrainingPlan> customPlans) {
        this.customPlans = customPlans;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
