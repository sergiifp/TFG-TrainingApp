package com.example.TFGtraining.DTO;

import com.example.TFGtraining.Club.Club;
import com.example.TFGtraining.Trainer.Trainer;


public class ClubDTO {

    private long id;
    private String name;
    private String trainerCreator;
    private Integer enrolledAthletes;
    private Integer customPlans;
    private Integer events;

    public ClubDTO(Club club) {
        this.id = club.getId();
        this.name = club.getName();
        this.trainerCreator = club.getTrainerCreator().getUsername();
        this.enrolledAthletes = club.getEnrolledAthletes().size();
        this.customPlans = club.getCustomPlans().size();
        this.events = club.getEvents().size();
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

    public String getTrainerCreator() {
        return trainerCreator;
    }

    public void setTrainerCreator(String trainerCreator) {
        this.trainerCreator = trainerCreator;
    }

    public Integer getEnrolledAthletes() {
        return enrolledAthletes;
    }

    public void setEnrolledAthletes(Integer enrolledAthletes) {
        this.enrolledAthletes = enrolledAthletes;
    }

    public Integer getCustomPlans() {
        return customPlans;
    }

    public void setCustomPlans(Integer customPlans) {
        this.customPlans = customPlans;
    }

    public Integer getEvents() {
        return events;
    }

    public void setEvents(Integer events) {
        this.events = events;
    }
}
