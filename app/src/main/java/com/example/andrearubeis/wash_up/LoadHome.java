package com.example.andrearubeis.wash_up;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

public class LoadHome {

    ArrayList<Stanza> Stanze;
    String id_home;
    String url;
    Context context;

    public LoadHome (String new_id_home , String domain , Context new_ctx) {
        this.id_home = new_id_home;
        this.url = domain;
        this.context = new_ctx;
        this.Stanze = new ArrayList<Stanza>();
    }

    /*public void inizializationHome() {

        URL url=null;
        //Globals g = Globals.getInstance();

        try {
            url = new URL(this.url + "get_stanze.php?home_id="+ this.id_home);
        }catch(IOException e){
            //Toast.makeText(getApplicationContext(),"Creazione URL non riuscita",Toast.LENGTH_SHORT).show();
        }

        try {
            new TaskAsincrono(this.context, url , new TaskCompleted() {
                @Override
                public void onTaskComplete(Object resp) {


                    String result = getStringFromInputStream((InputStream) resp);

                    //Toast.makeText(,result,Toast.LENGTH_SHORT).show();

                    readJSON(result);

                }
            }).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }*/


    /*private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }*/





    public void readJSON (String jsonString) {

        ArrayList<Stanza> vectorStanze = new ArrayList<Stanza>();

        try {


            jsonString = "{\"stanze\":"+ jsonString + "}";
            Log.w("INFORMATION Load Home", jsonString);
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray stanze = jsonObj.getJSONArray("stanze");
            for(int i = 0; i< stanze.length(); i++) {
                JSONObject c = stanze.getJSONObject(i);
                String nome_stanza = c.getString("nome_stanza");
                String image_stanza = c.getString("stanza_image");


                //Log.w("INFORMAIONE" , image_stanza);



                Stanza stanza = new Stanza(image_stanza,nome_stanza);
                vectorStanze.add(stanza);

            }

            if(vectorStanze == null) {
                Toast.makeText(this.context,"LoadHome : il vettore è NULL ",Toast.LENGTH_SHORT).show();
            }else{
                Log.d("LoadHome" ,  vectorStanze.toString());
            }

        }catch (Exception e){

        }

        this.setStanze(vectorStanze);

    }


    public ArrayList<Stanza> readJSONWhitReturn (String jsonString) {

        ArrayList<Stanza> vectorStanze = new ArrayList<Stanza>();

        try {


            jsonString = "{\"stanze\":"+ jsonString + "}";
            Log.w("INFORMATION Load Home", jsonString);
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray stanze = jsonObj.getJSONArray("stanze");
            for(int i = 0; i< stanze.length(); i++) {
                JSONObject c = stanze.getJSONObject(i);
                String nome_stanza = c.getString("nome_stanza");
                String image_stanza = c.getString("stanza_image");


                //Log.w("INFORMAIONE" , image_stanza);



                Stanza stanza = new Stanza(image_stanza,nome_stanza);
                vectorStanze.add(stanza);

            }

            if(vectorStanze == null) {
                Toast.makeText(this.context,"LoadHome : il vettore è NULL ",Toast.LENGTH_SHORT).show();
            }else{
                Log.d("LoadHome" ,  vectorStanze.toString());
            }

        }catch (Exception e){

        }

        return vectorStanze;

    }

    public void setStanze(ArrayList<Stanza> new_vectorStanze) {
        this.Stanze = new_vectorStanze;
    }

    public ArrayList<Stanza> getStanze() {
        return this.Stanze;
    }
}
