package com.example.andrearubeis.wash_up;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Vector;

public class Globals {

    private static Globals instance;

    // Global variable
    private int home_id;
    private String login_email;
    private String domain = "http://washit.dek4.net/";
    private String id_string;
    private ArrayList<Stanza> vectorStanze;
    private Persona info_utente;
    /*private LayoutInflater inflater;
    private ViewGroup container;
    private View vista;*/


    // Restrict the constructor from being instantiated
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

    public ArrayList<Stanza> getStanze() {
        return this.vectorStanze;
    }

    public void setStanze(ArrayList<Stanza> vector){
        this.vectorStanze = vector;
    }

    public void setInfoUtente (Persona new_info_utente) {
        this.info_utente = new_info_utente;
    }

    public Persona getInfoUtente () {
        return this.info_utente;
    }

    /*public void setInflater(LayoutInflater new_inflater) {
        this.inflater = new_inflater;
    }

    public LayoutInflater getInflater() {
        return this.inflater;
    }

    public void setContainer(ViewGroup new_container) {
        this.container = new_container;
    }

    public ViewGroup getContainer() {
        return this.container;
    }

    public void setVista(View new_view) {
        this.vista = new_view;
    }

    public View getVista() {
        return this.vista;
    }*/

    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }
}