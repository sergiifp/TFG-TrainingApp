package com.example.TFGtraining.DTO;


import com.example.TFGtraining.Event.Event;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EventDTO {

    private Long id;
    private String title;
    private Date sinceDate;
    private Date eventDate;
    private String description;
    private Map<String,String> user = new HashMap<String,String>();

    public EventDTO(Event event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.sinceDate = event.getSinceDate();
        this.eventDate = event.getEventDate();
        this.description = event.getDescription();
        user.put("username", event.getUserCreator().getUsername());
        if (event.getUserCreator().getType().equals("Athlete")) {
            user.put("role", "Athlete");
        }
        else{
            user.put("role","Trainer");
        }
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

    public Map<String, String> getUser() {
        return user;
    }

    public void setUser(Map<String, String> user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
