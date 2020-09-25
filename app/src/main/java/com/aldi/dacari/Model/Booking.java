package com.aldi.dacari.Model;

public class Booking {

    private String booking;
    private String sender;
    private String receiver;

    public Booking(String booking, String sender, String receiver){
        this.booking = booking;
        this.sender = sender;
        this.receiver = receiver;
    }

    public Booking(){
    }

    public String getBooking(){
        return booking;
    }

    public void setBooking(String message){
        this.booking = message;
    }

    public String getSender(){
        return sender;
    }
    public void setSender(String sender){
        this.sender = sender;
    }

    public String getReceiver(){
        return receiver;
    }

    public void setReceiver(String receiver){
        this.receiver = receiver;
    }

}
