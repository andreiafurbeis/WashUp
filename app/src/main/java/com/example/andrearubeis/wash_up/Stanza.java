package com.example.andrearubeis.wash_up;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by andrearubeis on 28/12/17.
 */

/*public class Stanza {

    private String nome;
    private String stanza_image;

    public Stanza(String image , String nome) {
        this.nome=nome;
        this.stanza_image=image;
    }

    public void setName(String name) {
        this.nome = name;
    }

    public void setImage(String image) {
        this.stanza_image = image;
    }

    public String getName() {
        return this.nome;
    }

    public String getImage() {
        return this.stanza_image;
    }

    public String toString() {
        return this.nome;
    }


}*/


public class Stanza implements Parcelable {

    private String nome;
    private String stanza_image;
    private String id_casa;


    public Stanza(String image , String nome) {
        this.nome=nome;
        this.stanza_image=image;
    }

    public Stanza(String image , String nome , String id) {
        this.id_casa = id;
        this.nome = nome;
        this.stanza_image = image;
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

    public String toString() {
        return this.nome;
    }




    protected Stanza(Parcel in) {
        nome = in.readString();
        stanza_image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(stanza_image);
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
