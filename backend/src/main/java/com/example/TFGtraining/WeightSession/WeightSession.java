package com.example.TFGtraining.WeightSession;

import com.example.TFGtraining.TrainingSession.TrainingSession;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalTime;

@Entity
@DiscriminatorValue("W")
public class WeightSession extends TrainingSession{
    @Column
    private String load;
    public WeightSession() {
    }

    public WeightSession(Long id, LocalTime time, String description, String load) {
        super(id,time,description);
        this.load = load;
    }

    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }

    @Override
    public String getTypeOfSession() {
        return "Weight";
    }
}
