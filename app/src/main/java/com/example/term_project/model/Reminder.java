package com.example.term_project.model;

public class Reminder { //encapsulation
    private int id;
    private String title;
    private String time;
    private boolean isActive;
    //constructor
    public Reminder(int id, String title, String time, boolean isActive){
        this.id = id;
        this.title = title;
        this.time = time;
        this.isActive = isActive;
    }
    //getter and setter
    public int getId() {return id;}
    public void setId(int id) {this.id = id; }
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getTime() {return time;}
    public void setTime(String time) {this.time = time;}
    public boolean isActive() {return isActive;}
    public void setActive(boolean active) {this.isActive = active;}
}
