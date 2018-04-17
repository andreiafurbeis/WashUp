package com.example.andrearubeis.wash_up;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by andrearubeis on 28/12/17.
 */



public class Stanza implements Parcelable {

    private String nome;
    private String stanza_image;
    private String id_casa;
    private ArrayList<Compito> compiti;


    public Stanza(String image, String nome) {
        this.nome = nome;
        this.stanza_image = image;
        this.compiti = new ArrayList<Compito>();
    }

    public Stanza(String image, String nome, String id) {
        this.id_casa = id;
        this.nome = nome;
        this.stanza_image = image;
        this.compiti = new ArrayList<Compito>();
    }

    public String getIdCasa() {
        return this.id_casa;
    }

    public void setIdCasa(String new_id) {
        this.id_casa = new_id;
    }

    public void setNameStanza(String new_name) {
        this.nome = new_name;
    }

    public void setImageStanza(String new_image) {
        this.stanza_image = new_image;
    }

    public String getNameStanza() {
        return this.nome;
    }

    public String getImageStanza() {
        return this.stanza_image;
    }

    public ArrayList<Compito> getCompiti() {
        return this.compiti;
    }

    public void setCompiti(ArrayList<Compito> new_compiti) {
        this.compiti = new_compiti;
    }

    public String toString() {
        return this.nome;
    }


    protected Stanza(Parcel in) {
        nome = in.readString();
        stanza_image = in.readString();
        id_casa = in.readString();
        if (in.readByte() == 0x01) {
            compiti = new ArrayList<Compito>();
            in.readList(compiti, Compito.class.getClassLoader());
        } else {
            compiti = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(stanza_image);
        dest.writeString(id_casa);
        if (compiti == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(compiti);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Stanza> CREATOR = new Parcelable.Creator<Stanza>() {
        @Override
        public Stanza createFromParcel(Parcel in) {
            return new Stanza(in);
        }

        @Override
        public Stanza[] newArray(int size) {
            return new Stanza[size];
        }
    };
}