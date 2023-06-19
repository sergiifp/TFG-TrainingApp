package com.example.TFGtraining.DTO;

import java.time.LocalTime;

public class RunningSeriesDTO {
    private Long id;
    private Integer distance;
    private Integer returnDistance;
    private LocalTime restTime;
    private Integer repetitions;
    private Integer fc;

    public RunningSeriesDTO(Long id, Integer distance, Integer returnDistance, LocalTime restTime, Integer repetitions, Integer fc) {
        this.id = id;
        this.distance = distance;
        this.returnDistance = returnDistance;
        this.restTime = restTime;
        this.repetitions = repetitions;
        this.fc = fc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getReturnDistance() {
        return returnDistance;
    }

    public void setReturnDistance(Integer returnDistance) {
        this.returnDistance = returnDistance;
    }

    public LocalTime getRestTime() {
        return restTime;
    }

    public void setRestTime(LocalTime restTime) {
        this.restTime = restTime;
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public Integer getFc() {
        return fc;
    }

    public void setFc(Integer fc) {
        this.fc = fc;
    }
}
