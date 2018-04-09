package com.example.andrearubeis.wash_up;

/**
 * Created by nicolo on 09/04/18.
 */

public class Compito {


    private String id;
    private String descrizione;
    private String stanza;
    private String id_casa;






    public Compito(String id, String descrizione, String stanza, String id_casa){

        this.id = id;
        this.descrizione = descrizione;
        this.stanza = stanza;
        this.id_casa = id_casa;

    }


    public String getDescrizione() {
        return descrizione;
    }

    public String getId() {
        return id;
    }

    public String getId_casa() {
        return id_casa;
    }

    public String getStanza() {
        return stanza;
    }


    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setId_casa(String id_casa) {
        this.id_casa = id_casa;
    }

    public void setStanza(String stanza) {
        this.stanza = stanza;
    }
}
