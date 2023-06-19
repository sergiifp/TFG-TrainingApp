package com.example.TFGtraining.DTO;


import java.util.Date;

public class CommentDTO {

    private Long id;
    private String user;
    private Date creationDate;
    private String message;

    public CommentDTO(Long id, String username, Date creationDate, String message) {
        this.id = id;
        this.user = username;
        this.creationDate = creationDate;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
