package com.example.TFGtraining.User;

import com.example.TFGtraining.Comments.Comment;
import com.example.TFGtraining.Event.Event;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "USER_ROLE", discriminatorType=DiscriminatorType.STRING)
@Entity
@Table(name="Users")
public abstract class User {
    @Id
    @Column(nullable=false)
    private String username;
    @Column(nullable=false)
    private String password;
    @Column(nullable=false, unique = true)
    private String token;
    @Column
    private String description;
    @Column
    private Integer age;
    @Column
    private Date sinceDate;
    @JsonIgnore
    @OneToMany(mappedBy="userCreator", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<Event> events;
    @OneToMany(mappedBy="user", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<Comment> comments;

    protected User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public abstract String getType();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getSinceDate() {
        return sinceDate;
    }

    public void setSinceDate(Date sinceDate) {
        this.sinceDate = sinceDate;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
