package com.example.TFGtraining.RunningSession;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
public class RunningSeries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Integer distance;
    @Column
    private Integer returnDistance;
    @Column
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime restTime;
    @Column
    private Integer repetitions;
    @Column
    private Integer fc;

    public RunningSeries() {
    }

    public RunningSeries(Long id, Integer distance, Integer returnDistance, LocalTime restTime, Integer repetitions, Integer fc) {
        this.id = id;
        this.distance = distance;
        this.returnDistance = returnDistance;
        this.restTime = restTime;
        this.repetitions = repetitions;
        this.fc = fc;
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

    public LocalTime getRestTime() {
        return restTime;
    }

    public void setRestTime(LocalTime restTime) {
        this.restTime = restTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
