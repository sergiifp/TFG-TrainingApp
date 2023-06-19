package com.example.TFGtraining.Comments;

import com.example.TFGtraining.TrainingWeek.TrainingWeek;
import com.example.TFGtraining.User.User;
import jakarta.persistence.*;

import java.util.Date;
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName="username")
    private User user;
    @ManyToOne
    @JoinColumn(name = "trainingWeek", referencedColumnName="id")
    private TrainingWeek trainingWeek;
    private Date creationDate;
    private String message;

    public Comment(TrainingWeek trainingWeek, User user, String message, Date date) {
        this.trainingWeek = trainingWeek;
        this.user = user;
        this.message = message;
        this.creationDate = date;
    }

    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TrainingWeek getTrainingWeek() {
        return trainingWeek;
    }

    public void setTrainingWeek(TrainingWeek trainingWeek) {
        this.trainingWeek = trainingWeek;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
