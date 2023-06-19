package com.example.TFGtraining.TrainingPlan;

import com.example.TFGtraining.Athlete.Athlete;
import com.example.TFGtraining.Club.Club;
import com.example.TFGtraining.PlanProgress.PlanProgress;
import com.example.TFGtraining.Trainer.Trainer;
import com.example.TFGtraining.TrainingWeek.TrainingWeek;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class TrainingPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String typeOfActivity;
    @Column
    private String Objective;
    @Column
    private String description;
    @OneToMany(mappedBy = "trainingPlan", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<TrainingWeek> trainingWeeks;
    @ManyToOne
    @JoinColumn(name = "trainer_username", referencedColumnName = "username")
    private Trainer trainer;
    @JsonIgnore
    @OneToMany(mappedBy="plan")
    private List<PlanProgress> clientsProgress;
    @ManyToOne
    @JoinColumn(name = "club_id", referencedColumnName = "id")
    private Club ownerClub;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name="likes",
            joinColumns = @JoinColumn(name = "trainingPlan_id"),
            inverseJoinColumns = @JoinColumn(name = "athlete_username"))
    private List<Athlete> likedBy;



    public TrainingPlan() {
        trainingWeeks = new ArrayList<>();
        clientsProgress = new ArrayList<>();
        likedBy = new ArrayList<>();
    }

    public TrainingPlan(Long id, String name, String typeOfActivity, String objective, String description) {
        this.id = id;
        this.name = name;
        this.typeOfActivity = typeOfActivity;
        this.Objective = objective;
        this.description = description;
        this.trainingWeeks = new ArrayList<>();
        this.clientsProgress = new ArrayList<>();
        this.likedBy = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeOfActivity() {
        return typeOfActivity;
    }

    public void setTypeOfActivity(String typeOfActivity) {
        this.typeOfActivity = typeOfActivity;
    }

    public String getObjective() {
        return Objective;
    }

    public void setObjective(String objective) {
        Objective = objective;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TrainingWeek> getTrainingWeeks() {
        return trainingWeeks;
    }

    public void setTrainingWeeks(List<TrainingWeek> trainingWeeks) {
        this.trainingWeeks = trainingWeeks;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public List<PlanProgress> getClientsProgress() {
        return clientsProgress;
    }

    public void setClientsProgress(List<PlanProgress> clientsProgress) {
        this.clientsProgress = clientsProgress;
    }

    public Club getOwnerClub() {
        return ownerClub;
    }

    public void setOwnerClub(Club ownerClub) {
        this.ownerClub = ownerClub;
    }

    public List<Athlete> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(List<Athlete> likedBy) {
        this.likedBy = likedBy;
    }
}
