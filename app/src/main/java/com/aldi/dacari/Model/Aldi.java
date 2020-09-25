package com.aldi.dacari.Model;

public class Aldi {

    private String id;
    private String name;
    private String username;
    private String imageURL;
    private String handphone;
    private String address;
    private String status;
    private String search;

    public Aldi(String id, String name, String username, String imageURL, String handphone, String address, String status, String search) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.imageURL = imageURL;
        this.handphone = handphone;
        this.address = address;
        this.status = status;
        this.search = search;
    }

    public Aldi() {

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

    public String getHandphone(){
        return handphone;
    }

    public void setHandphone(String handphone){
        this.handphone = handphone;
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
