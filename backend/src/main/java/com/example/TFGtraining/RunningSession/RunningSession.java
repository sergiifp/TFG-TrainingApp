package com.example.TFGtraining.RunningSession;

import com.example.TFGtraining.TrainingSession.TrainingSession;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("R")
public class RunningSession extends TrainingSession {
    @Column
    private String type;
    @Column
    private Integer positiveSlope;
    @Column
    private Integer negativeSlope;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "runningsession_id", referencedColumnName = "id")
    private List<RunningSeries> runningSeries;

    public RunningSession() {
        runningSeries = new ArrayList<>();
    }

    public RunningSession(Long id, LocalTime time, String description, String type, Integer positiveSlope, Integer negativeSlope) {
        super(id,time,description);
        this.type = type;
        this.positiveSlope = positiveSlope;
        this.negativeSlope = negativeSlope;
        runningSeries = new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPositiveSlope() {
        return positiveSlope;
    }

    public void setPositiveSlope(Integer positiveSlope) {
        this.positiveSlope = positiveSlope;
    }

    public Integer getNegativeSlope() {
        return negativeSlope;
    }

    public void setNegativeSlope(Integer negativeSlope) {
        this.negativeSlope = negativeSlope;
    }

    public List<RunningSeries> getRunningSeries() {
        return runningSeries;
    }

    public void setRunningSeries(List<RunningSeries> runningSeries) {
        this.runningSeries = runningSeries;
    }

    @Override
    public String getTypeOfSession() {
        return "Running";
    }

}
