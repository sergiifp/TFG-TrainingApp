package com.example.TFGtraining.TrainingWeek;

import com.example.TFGtraining.Comments.Comment;
import com.example.TFGtraining.TrainingPlan.TrainingPlan;
import com.example.TFGtraining.TrainingSession.TrainingSession;
import jakarta.persistence.*;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

@Entity
public class TrainingWeek {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String description;
    @OneToMany(mappedBy = "trainingWeek", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @OrderBy("id ASC")
    private List<TrainingSession> trainingSessions;
    @ManyToOne
    @JoinColumn(name = "trainingplan_id", referencedColumnName = "id")
    private TrainingPlan trainingPlan;
    @OneToMany(mappedBy = "trainingWeek", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @OrderBy("creationDate ASC")
    private List<Comment> comments;

    public TrainingWeek() {
        trainingSessions = new ArrayList<>();
    }

    public TrainingWeek(Long id, String description) {
        this.id = id;
        this.description = description;
        trainingSessions = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TrainingSession> getTrainingSessions() {
        return trainingSessions;
    }

    public void setTrainingSessions(List<TrainingSession> trainingSessions) {
        this.trainingSessions = trainingSessions;
    }

    public TrainingPlan getTrainingPlan() {
        return trainingPlan;
    }

    public void setTrainingPlan(TrainingPlan trainingPlan) {
        this.trainingPlan = trainingPlan;
    }
}
