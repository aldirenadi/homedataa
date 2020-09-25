package com.aldi.dacari.Model;

public class Project {

    private String id;
    private String projectid;
    private String ownerid;
    private String type;
    private String nama;
    private String title;
    private String description;
    private String time;
    private String bids;
    private String bidno;
    private String budget;
    private String skill;
    private String track_record;
    private String image;
    private String foto_user;

    public Project(String id, String projectid, String ownerid, String type, String title, String description, String time, String bids, String bidno, String budget, String skill, String nama, String track_record, String image, String foto_user) {
        this.id = id;
        this.projectid = projectid;
        this.ownerid = ownerid;
        this.type = type;
        this.title = title;
        this.description = description;
        this.time = time;
        this.bids = bids;
        this.bidno = bidno;
        this.budget = budget;
        this.skill = skill;
        this.nama = nama;
        this.track_record = track_record;
        this.image = image;
        this.foto_user = foto_user;
    }

    public Project(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBids() {
        return bids;
    }

    public void setBids(String bids) {
        this.bids = bids;
    }

    public String getBidno() {
        return bidno;
    }

    public void setBidno(String bidno) {
        this.bidno = bidno;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTrack_record() {
        return track_record;
    }

    public void setTrack_record(String track_record) {
        this.track_record = track_record;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFoto_user() {
        return foto_user;
    }

    public void setFoto_user(String foto_user) {
        this.foto_user = foto_user;
    }
}
