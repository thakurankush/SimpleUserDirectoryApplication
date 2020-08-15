package com.ankush.simpleuserdirectoryapplication;

public class uploadData {
    public String name;
    public String imageurl,pass,age,phone,mail,address,edu;

    public uploadData()
    {

    }

    public uploadData(String name,String imageurl,String pass,String age,String phone,String mail,String address,String edu) {
        this.name = name;
        this.imageurl = imageurl;
        this.pass = pass;
        this.age = age;
        this.phone = phone;
        this.mail = mail;
        this.address = address;
        this.edu = edu;
    }

    public String getName() {
        return name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getPass() {
        return pass;
    }

    public String getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }

    public String getAddress() {
        return address;
    }

    public String getEdu() {
        return edu;
    }
}
