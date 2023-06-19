package com.example.TFGtraining.DTO;

import java.time.LocalTime;

public class WeightSessionDTO extends TrainingSessionDTO{
    private String load;

    public WeightSessionDTO(Long id, String description, LocalTime time, String load) {
        super(id, description, time);
        this.load = load;
    }

    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }
}
