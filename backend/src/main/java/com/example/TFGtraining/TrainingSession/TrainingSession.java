package com.example.TFGtraining.TrainingSession;

import com.example.TFGtraining.RunningSession.RunningSession;
import com.example.TFGtraining.SessionResult.SessionResult;
import com.example.TFGtraining.TrainingWeek.TrainingWeek;
import com.example.TFGtraining.WeightSession.WeightSession;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.List;


@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TRAINING_TYPE", discriminatorType=DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "typeOfTraining")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RunningSession.class, name = "runningSession"),
        @JsonSubTypes.Type(value = WeightSession.class, name = "weightSession")
})
@Entity
public abstract class TrainingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime time;
    @Column
    private String description;
    @JsonIgnore
    @OneToMany(mappedBy = "session",cascade = {CascadeType.REMOVE})
    private List<SessionResult> results;
    @ManyToOne
    @JoinColumn(name = "trainingweek_id", referencedColumnName = "id")
    private TrainingWeek trainingWeek;

    public TrainingSession(Long id, LocalTime time, String description) {
        this.id = id;
        this.time = time;
        this.description = description;
    }

    public TrainingSession() {
    }

    public abstract String getTypeOfSession();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SessionResult> getResults() {
        return results;
    }

    public void setResults(List<SessionResult> results) {
        this.results = results;
    }

    public TrainingWeek getTrainingWeek() {
        return trainingWeek;
    }

    public void setTrainingWeek(TrainingWeek trainingWeek) {
        this.trainingWeek = trainingWeek;
    }
}
