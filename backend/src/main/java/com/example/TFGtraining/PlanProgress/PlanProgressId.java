package com.example.TFGtraining.PlanProgress;

import com.example.TFGtraining.Athlete.Athlete;
import com.example.TFGtraining.TrainingPlan.TrainingPlan;

import java.io.Serializable;

public class PlanProgressId implements Serializable {

    private Athlete athlete;
    private TrainingPlan plan;

    public PlanProgressId(Athlete athlete, TrainingPlan plan) {
        this.athlete = athlete;
        this.plan = plan;
    }

    private PlanProgressId() {
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
