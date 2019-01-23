package com.school.schooldb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JsonIgnore
    private User user;

    @NotEmpty(message = "Must enter a course title")
    private String title;

    @NotEmpty(message = "Must enter a course description")
    private String description;

    @NotEmpty(message = "Must enter a estimated time")
    private String estimatedTime;

    @NotEmpty(message = "Must enter a materials needed for the course")
    private String materialsNeeded;

    public Course() {
    }

    public Course(User user, @NotEmpty(message = "Must enter a course title") String title, @NotEmpty(message = "Must" +
            " enter a course description") String description, @NotEmpty(message = "Must enter a estimated time")
                          String estimatedTime, @NotEmpty(message = "Must enter a materials needed for the course")
            String
                          materialsNeeded) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.estimatedTime = estimatedTime;
        this.materialsNeeded = materialsNeeded;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getMaterialsNeeded() {
        return materialsNeeded;
    }

    public void setMaterialsNeeded(String materialsNeeded) {
        this.materialsNeeded = materialsNeeded;
    }
}
