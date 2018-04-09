package com.example.andrearubeis.wash_up;

/**
 * Created by andrearubeis on 28/12/17.
 */

public class Id {

    private static Id instance;

    // Global variable
    private String home_id;

    // Restrict the constructor from being instantiated
    private Id(){}

    public void setHome_id(String new_id) {
        this.home_id = new_id;
    }

    public String getHome_id() {
        return this.getHome_id();
    }

    public static synchronized Id getInstance(){
        if(instance==null){
            instance=new Id();
        }
        return instance;
    }
}
