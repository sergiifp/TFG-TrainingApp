package com.example.TFGtraining.Event;

import com.example.TFGtraining.Club.Club;
import com.example.TFGtraining.User.User;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private Date sinceDate;
    @Column
    private Date eventDate;
    @ManyToOne
    @JoinColumn(name="club_id", referencedColumnName="id")
    private Club club;
    @ManyToOne
    @JoinColumn(name="user_username", referencedColumnName="username")
    private User userCreator;

    public Event() {
    }

    public Event(String title, String description, Date eventDate) {
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.sinceDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getSinceDate() {
        return sinceDate;
    }

    public void setSinceDate(Date sinceDate) {
        this.sinceDate = sinceDate;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public User getUserCreator() {
        return userCreator;
    }

    public void setUserCreator(User userCreator) {
        this.userCreator = userCreator;
    }
}
