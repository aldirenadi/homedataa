package com.aldi.dacari.Model;

public class Bid {

    private String id;
    private String paid;
    private String projectId;
    private String bidderId;
    private String imageURL;
    private String name;
    private String day;
    private String description;

    public Bid(String id, String paid, String projectId, String bidderId, String imageURL, String name,  String day, String description) {
        this.id = id;
        this.paid = paid;
        this.imageURL = imageURL;
        this.bidderId = bidderId;
        this.name = name;
        this.projectId = projectId;
        this.day = day;
        this.description = description;
    }

    public Bid(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBidderId() {
        return bidderId;
    }

    public void setBidderId(String bidderId) {
        this.bidderId = bidderId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
