package com.example.andrearubeis.wash_up;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by andrearubeis on 15/12/17.
 */




public class Persona implements Parcelable {

    private String nome;
    private String cognome;
    private String password;
    private String mail;
    private String profile_image;
    private Date nascita;
    private String id_home;
    private ArrayList<Stanza> stanze;
    private ArrayList<Compito> compiti;
    private ArrayList<Persona> coinquilini;


    public Persona(){

    }

    public Persona(String mail, String password) {
        this.mail = mail;
        this.password = password;
    }

    public Persona(String nome, String cognome, String mail, String profile_image, String id, ArrayList<Stanza> stanze) {
        this.nome = nome;
        this.cognome = cognome;
        this.mail = mail;
        this.profile_image = profile_image;
        this.id_home = id;
        this.stanze = stanze;
        this.coinquilini=new ArrayList<Persona>();
    }



    public void setCompiti(ArrayList<Compito> new_compiti) {
        this.compiti = new_compiti;
    }

    public ArrayList<Compito> getCompiti() {
        return this.compiti;
    }


    public void setCoinquilini(ArrayList<Persona> new_coinquilini) {
        this.coinquilini = new_coinquilini;
    }

    public ArrayList<Persona> getCoinquilini() {
        return this.coinquilini;
    }


    public String getNome() {
        return this.nome;
    }

    public String getCognome() {
        return this.cognome;
    }

    public String getPassword() {
        return this.password;
    }

    public String getMail() {
        return this.mail;
    }

    public void setNome(String newNome) {
        this.nome = newNome;
    }

    public void setCognome(String newCognome) {
        this.cognome = newCognome;
    }

    public void setProfileImage(String new_profile_image) {
        this.profile_image = new_profile_image;
    }

    public String getProfileImage() {
        return this.profile_image;
    }

    public String getIdHome() {
        return this.id_home;
    }

    public void setIdHome(String new_id_home) {
        this.id_home = new_id_home;
    }

    public ArrayList<Stanza> getStanze() {
        return this.stanze;
    }

    public void setStanze(ArrayList<Stanza> new_stanze) {
        this.stanze = new_stanze;
    }

    public String toString() {
        return this.getNome() + " " + this.getCognome();
    }

    public void setCompitiStanza(int indice, ArrayList<Compito> compiti) {
        Stanza stanza = this.getStanze().get(indice);
        stanza.setCompiti(compiti);
    }

    public ArrayList<Compito> getCompitiStanza(int indice) {
        Stanza stanza = this.getStanze().get(indice);
        return stanza.getCompiti();
    }

    public void setCompitiInquilino(int indice , ArrayList<Compito> compiti) {
        Persona inquilino = this.getCoinquilini().get(indice);
        inquilino.setCompiti(compiti);
    }

    public ArrayList<Compito> getCompitiInquilino(int indice) {
        Persona inquilino = this.getCoinquilini().get(indice);
        return inquilino.getCompiti();
    }

    public int getIndiceInquilino(String email) {
        int position = -1;
        for(int i = 0 ; i < this.coinquilini.size() ; i++ ) {
            if (email.equals(this.coinquilini.get(i).getMail())) {
                position = i;
                return position;
            }
        }
        return position;
    }

    public int getIndiceStanza(String name_stanza) {
        int position = -1;
        for (int i = 0; i < this.stanze.size(); i++) {
            if (name_stanza.equals(this.stanze.get(i).getNameStanza())) {
                position = i;
                return position;
            }
        }
        return position;
    }

    protected Persona(Parcel in) {
        nome = in.readString();
        cognome = in.readString();
        password = in.readString();
        mail = in.readString();
        profile_image = in.readString();
        long tmpNascita = in.readLong();
        nascita = tmpNascita != -1 ? new Date(tmpNascita) : null;
        id_home = in.readString();
        if (in.readByte() == 0x01) {
            stanze = new ArrayList<Stanza>();
            in.readList(stanze, Stanza.class.getClassLoader());
        } else {
            stanze = null;
        }
        if (in.readByte() == 0x01) {
            compiti = new ArrayList<Compito>();
            in.readList(compiti, Compito.class.getClassLoader());
        } else {
            compiti = null;
        }
        if (in.readByte() == 0x01) {
            coinquilini = new ArrayList<Persona>();
            in.readList(coinquilini, Persona.class.getClassLoader());
        } else {
            coinquilini = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(cognome);
        dest.writeString(password);
        dest.writeString(mail);
        dest.writeString(profile_image);
        dest.writeLong(nascita != null ? nascita.getTime() : -1L);
        dest.writeString(id_home);
        if (stanze == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(stanze);
        }
        if (compiti == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(compiti);
        }
        if (coinquilini == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(coinquilini);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Persona> CREATOR = new Parcelable.Creator<Persona>() {
        @Override
        public Persona createFromParcel(Parcel in) {
            return new Persona(in);
        }

        @Override
        public Persona[] newArray(int size) {
            return new Persona[size];
        }
    };
}





