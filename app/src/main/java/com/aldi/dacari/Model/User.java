package com.aldi.dacari.Model;

public class User {

    private String id;
    private String name;
    private String lastname;
    private String username;
    private String imageURL;
    private String handphone;
    private String address;
    private String status;
    private String search;
    private String ktp;
    private String nik;

    public User(String id,String name, String lastname, String username, String imageURL, String handphone, String address, String status, String search, String ktp, String nik) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.imageURL = imageURL;
        this.handphone = handphone;
        this.address = address;
        this.status = status;
        this.search = search;
        this.ktp = ktp;
        this.nik = nik;
    }

    public User() {

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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public String getKtp() {
        return ktp;
    }

    public void setKtp(String ktp) {
        this.ktp = ktp;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }
}
