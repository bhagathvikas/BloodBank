package com.example.bloodbank.model;

import android.widget.Spinner;

public class UserDetails {
    private String username, bloodgroup, email, phonenumber,image,userid, pid, date, time;


    public UserDetails() {

    }


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public UserDetails(String username, String bloodgroup, String email, String phonenumber, String image, String userid, String pid, String date, String time){
        this.username = username;
        this.bloodgroup = bloodgroup;
        this.email = email;
        this.image = image;
        this.userid = userid;
        this.pid = pid;
        this.time = time;
        this.date = date;

        this.phonenumber = phonenumber;
    }
    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getUserid() {
        return userid;
    }
    public void setImage(String image) {
        this.image = image;
    }




    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    public String getImage() {
        return image;
    }


}
