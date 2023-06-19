package com.example.TFGtraining.PlanProgress;

import com.example.TFGtraining.Athlete.Athlete;
import com.example.TFGtraining.TrainingPlan.TrainingPlan;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;


@Entity
@IdClass(PlanProgressId.class)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"trainer_username","plan_id"})})
public class PlanProgress {
    @Column
    private Integer currentDay;
    @Column
    private Integer currentSession;

    @Id
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="trainer_username", referencedColumnName = "username")
    private Athlete athlete;

    @Id
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="plan_id", referencedColumnName = "id")
    private TrainingPlan plan;

    public PlanProgress(Athlete athlete, TrainingPlan plan) {
        this.athlete = athlete;
        this.plan = plan;
        this.currentDay = 0;
        this.currentSession = 0;
    }

    public PlanProgress() {
    }

    public Integer getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(Integer currentDay) {
        this.currentDay = currentDay;
    }

    public Integer getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Integer currentSession) {
        this.currentSession = currentSession;
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public TrainingPlan getPlan() {
        return plan;
    }

    public void setPlan(TrainingPlan plan) {
        this.plan = plan;
    }
}
