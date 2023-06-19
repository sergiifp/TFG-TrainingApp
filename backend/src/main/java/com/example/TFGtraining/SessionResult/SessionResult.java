package com.example.TFGtraining.SessionResult;

import com.example.TFGtraining.Athlete.Athlete;
import com.example.TFGtraining.RunningSession.RunningSession;
import com.example.TFGtraining.TrainingSession.TrainingSession;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@IdClass(SessionResultId.class)
@Table
public class SessionResult {

    @Column
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime finalTime;
    @Column
    private Integer finalKm;
    @Id
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "session_id", referencedColumnName = "id")
    private TrainingSession session;
    @Id
    @ManyToOne
    @JoinColumn(name = "athlete_username", referencedColumnName = "username")
    private Athlete athlete;

    public SessionResult(Athlete athlete, TrainingSession trainingSession, Integer finalKm, LocalTime finalTime) {
        this.athlete = athlete;
        this.session = trainingSession;
        this.finalKm = finalKm;
        this.finalTime = finalTime;
    }

    public SessionResult() {
    }

    public LocalTime getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(LocalTime finalTime) {
        this.finalTime = finalTime;
    }

    public Integer getFinalKm() {
        return finalKm;
    }

    public void setFinalKm(Integer finalKm) {
        this.finalKm = finalKm;
    }

    public TrainingSession getSession() {
        return session;
    }

    public void setSession(RunningSession session) {
        this.session = session;
    }

    public void setSession(TrainingSession session) {
        this.session = session;
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }
}
