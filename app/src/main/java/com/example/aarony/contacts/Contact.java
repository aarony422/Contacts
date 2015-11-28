package com.example.aarony.contacts;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by aarony on 11/28/15.
 */
@SuppressWarnings("serial")
public class Contact implements Serializable{
    public String name;
    public String company;
    //public String detailsURL;
    //public String smallImageURL;
    //public String birthdate;
    //public HashMap<String, String> phonenums;

    public Contact(String name, String company) { //String detailsURL String smallImageURL, String birthdate, HashMap<String, String> phonenums) {
        this.name = name;
        this.company = company;
        //this.detailsURL = detailsURL;
        //this.smallImageURL = smallImageURL;
        //this.birthdate = birthdate;
        //this.phonenums = phonenums;
    }

    public String getName() {
        return this.name;
    }

    public String getCompany() {
        return this.company;
    }




}
