package com.example.andrearubeis.wash_up;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nicolo on 09/04/18.
 */



public class Compito implements Parcelable {


    private String id;
    private String descrizione;
    private String stanza;
    private String id_casa;




    public Compito (String descrizione , String stanza , String id_casa) {
        this.descrizione = descrizione;
        this.stanza = stanza;
        this.id_casa = id_casa;
    }

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

    protected Compito(Parcel in) {
        id = in.readString();
        descrizione = in.readString();
        stanza = in.readString();
        id_casa = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(descrizione);
        dest.writeString(stanza);
        dest.writeString(id_casa);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Compito> CREATOR = new Parcelable.Creator<Compito>() {
        @Override
        public Compito createFromParcel(Parcel in) {
            return new Compito(in);
        }

        @Override
        public Compito[] newArray(int size) {
            return new Compito[size];
        }
    };
}

/*public class Compito {


    private String id;
    private String descrizione;
    private String stanza;
    private String id_casa;




    public Compito (String descrizione , String stanza , String id_casa) {
        this.descrizione = descrizione;
        this.stanza = stanza;
        this.id_casa = id_casa;
    }

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
*/