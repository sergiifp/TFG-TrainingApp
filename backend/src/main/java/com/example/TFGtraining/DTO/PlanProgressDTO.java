package com.example.TFGtraining.DTO;

import com.example.TFGtraining.PlanProgress.PlanProgress;
import com.example.TFGtraining.SessionResult.SessionResult;

import java.util.ArrayList;
import java.util.List;

public class PlanProgressDTO {

    private String username;
    private List<SessionResultDTO> sessionResults = new ArrayList<>();

    public PlanProgressDTO(PlanProgress planProgress, List<SessionResult> sessionResults) {
        this.username = planProgress.getAthlete().getUsername();
        for (SessionResult result : sessionResults){
            this.sessionResults.add(new SessionResultDTO(result));
        }
    }

    public List<SessionResultDTO> getSessionResults() {
        return sessionResults;
    }

    public void setSessionResults(List<SessionResultDTO> sessionResults) {
        this.sessionResults = sessionResults;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
