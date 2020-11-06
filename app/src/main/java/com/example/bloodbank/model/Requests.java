package com.example.bloodbank.model;

public class Requests {

    private String Name,Age,PhoneNumber,Hospital,BloodGroup,pid, date, time ;

    public Requests(){


    }
    public Requests(String Name, String Age, String PhoneNumber, String Hospital, String Gender, String BloodGroup, String pid, String date, String time){
        this.Name = Name;
        this.Age = Age;
        this.Hospital = Hospital;
        this.PhoneNumber = PhoneNumber;

        this.BloodGroup = BloodGroup;
        this.pid = pid;
        this.date = date;
        this.time = time;


    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getHospital() {
        return Hospital;
    }

    public void setHospital(String hospital) {
        Hospital = hospital;
    }



    public String getBloodGroup() {
        return BloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
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
