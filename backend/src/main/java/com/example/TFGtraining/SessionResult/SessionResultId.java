package com.example.TFGtraining.SessionResult;

import com.example.TFGtraining.Athlete.Athlete;
import com.example.TFGtraining.TrainingSession.TrainingSession;

public class SessionResultId {
    private TrainingSession session;
    private Athlete athlete;

    public SessionResultId(TrainingSession session, Athlete athlete) {
        this.session = session;
        this.athlete = athlete;
    }

    private SessionResultId() {
    }

    public TrainingSession getSession() {
        return session;
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
