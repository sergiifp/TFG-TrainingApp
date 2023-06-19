package com.example.TFGtraining.DTO;

import com.example.TFGtraining.SessionResult.SessionResult;

import java.time.LocalTime;

public class SessionResultDTO {
    private LocalTime finalTime;
    private Integer finalKm;
    private Long trainingSessionId;

    public SessionResultDTO(SessionResult session) {
        this.finalKm = session.getFinalKm();
        this.finalTime = session.getFinalTime();
        this.trainingSessionId = session.getSession().getId();
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

    public Long getTrainingSessionId() {
        return trainingSessionId;
    }

    public void setTrainingSessionId(Long trainingSessionId) {
        this.trainingSessionId = trainingSessionId;
    }
}
