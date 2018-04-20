package com.example.andrearubeis.wash_up;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Vector;

public class Globals {

    private static Globals instance;

    private int home_id;
    private String login_email;
    private String domain = "http://washit.dek4.net/";
    private String id_string;



    private Globals(){}

    public void setHome(int id){
        this.home_id=id;
    }

    public int getHome(){
        return this.home_id;
    }

    public void setIdString(String newId) {
        this.id_string=newId;
    }

    public String getIdString() {
        return this.id_string;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setMail(String mail) {
        this.login_email=mail;
    }

    public String getMail() {
        return this.login_email;
    }


    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }
}