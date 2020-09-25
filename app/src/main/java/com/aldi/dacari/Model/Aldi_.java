package com.aldi.dacari.Model;

public class Aldi_ {

    private String id;
    private String name;
    private String username;
    private String imageURL;
    private String jobtitle;
    private String address;
    private String status;
    private String search;

    public Aldi_(String id, String name, String username, String imageURL, String jobtitle, String address, String status, String search) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.imageURL = imageURL;
        this.jobtitle = jobtitle;
        this.address = address;
        this.status = status;
        this.search = search;
    }

    public Aldi_() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getJobtitle(){
        return jobtitle;
    }

    public void setJobtitle(String jobtitle){
        this.jobtitle = jobtitle;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
