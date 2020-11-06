package com.example.bloodbank.model;

public class Donor {
    private String donorname,bloodgroup,email, phonenumber, image, userid,date, time,pid;


    public Donor(){

    }
  public Donor(String donorname, String bloodgroup, String email, String phonenumber, String image, String userid, String pid, String date, String time) {
      this.donorname = donorname;
      this.bloodgroup = bloodgroup;
      this.email = email;
      this.image = image;
      this.userid = userid;
      this.pid = pid;
      this.time = time;
      this.date = date;

      this.phonenumber = phonenumber;
  }

    public String getDonorname() {
        return donorname;
    }

    public void setDonorname(String donorname) {
        this.donorname = donorname;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
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

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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



}
