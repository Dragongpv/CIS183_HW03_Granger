package com.example.cis183_hw03_granger;

public class StudentInfo
{
    String fname;
    String lname;
    String username;
    String email;
    String age;
    String gpa;
    String major;

    public StudentInfo(String fname, String lname, String username, String email, String age, String gpa, String major)
    {
        this.fname = fname;
        this.lname = lname;
        this.username = username;
        this.email = email;
        this.age = age;
        this.gpa = gpa;
        this.major = major;
    }
    public StudentInfo(String fname, String lname, String email, String age, String gpa, String major)
    {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.age = age;
        this.gpa = gpa;
        this.major = major;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getId() {
        return username;
    }

    public void setId(String id) {
        this.username = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
