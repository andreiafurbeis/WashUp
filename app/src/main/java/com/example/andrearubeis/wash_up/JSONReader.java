package com.example.andrearubeis.wash_up;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONReader {

    //String id_home;
    Context context;

    public JSONReader ( Context context ) {
        //this.id_home = id;
        this.context = context;
    }



    public ArrayList<Compito> readJSONCompiti(String jsonString) {
        /*if(temp_persona.getCompitiStanza(1) != null) {
            Log.d("ConfigurazioneStanze", "All'inizio di ReadJSON :  ci sono : " + temp_persona.getCompitiStanza(1).size() + "compiti in questa stanza ");
        }*/

        ArrayList <Compito> vectorCompiti= new ArrayList<Compito>();


        try {


            jsonString = "{\"compiti\":"+ jsonString + "}";
            //jsonString = "{" + jsonString + "}";
            Log.w("INFORMATION", jsonString);
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray compiti = jsonObj.getJSONArray("compiti");
            for(int i = 0; i< compiti.length(); i++) {
                JSONObject c = compiti.getJSONObject(i);



                //Log.w("INFORMAIONE" , image_stanza);



                //Stanza stanza = new Stanza(c.getString("stanza_image"),c.getString("nome_stanza"));
                Compito compito = new Compito(c.getString("id_compito") , c.getString("descrizione") , c.getString("stanza") , c.getString("id_casa"));
                vectorCompiti.add(compito);

            }

        }catch (Exception e){
            Log.d("ConfigurazioneStanze" , "Eccezione catturata nel ReadJSONCompiti");
        }

        Globals g = Globals.getInstance();
        //g.setStanze(vectorStanze);

        //temp_persona.setStanze(vectorStanze);

        /*if(temp_persona.getCompitiStanza(1) != null) {
            Log.d("ConfigurazioneStanze", "readJson : ci sono : " + temp_persona.getCompitiStanza(1).size() + "compiti in questa stanza ");
        }*/

        return vectorCompiti;

    }


    /**
     *
     * @param jsonString Stringa JSON [{ "nome_stanza" : "nome" , "stanza_image" : "url immagine presente sul server" } , ...
     * @return Stanze della casa in cui abita la persona
     */
    public ArrayList<Stanza> readJSONStanze (String jsonString) {

        ArrayList<Stanza> vectorStanze = new ArrayList<Stanza>();

        try {


            jsonString = "{\"stanze\":"+ jsonString + "}";
            Log.w("JSONReader", jsonString);
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray stanze = jsonObj.getJSONArray("stanze");
            for(int i = 0; i< stanze.length(); i++) {
                JSONObject c = stanze.getJSONObject(i);
                String nome_stanza = c.getString("nome_stanza");
                String image_stanza = c.getString("stanza_image");



                //Log.w("INFORMAIONE" , image_stanza);



                Stanza stanza = new Stanza(image_stanza,nome_stanza);
                stanza.setCompiti(readJSONCompiti(c.getString("compiti")));
                vectorStanze.add(stanza);

            }

            if(vectorStanze == null) {
                Toast.makeText(this.context,"JSONReader : il vettore è NULL ",Toast.LENGTH_SHORT).show();
            }else{
                Log.d("JSONReader" ,  vectorStanze.toString());
            }

        }catch (Exception e){

        }

        return vectorStanze;

    }


    /**
     *
     * @param jsonString Stringa JSON [{ "id" : x  }]
     * @return id della casa in cui abita la persona
     */
    public String readJSONId (String jsonString) {

        String id=null;

        if(!jsonString.equals("[]")) {
            try {

                jsonString = "{\"info\":" + jsonString + "}";
                Log.w("JSONReader", jsonString);
                JSONObject jsonObj = new JSONObject(jsonString);
                JSONArray info = jsonObj.getJSONArray("info");
                JSONObject c = info.getJSONObject(0);
                id = c.getString("id");

            } catch (Exception e) {
                Log.w("Exception", "Eccezione durante lettura JSON Login");
            }
            return id;
        } else {
            return null;
        }

    }


    /**
     *
     * @param jsonString Stringa JSON [{ "nome" : "nome_persona" ,"cognome": "cognome_persona" , "profile_image" : "url immagine presente sul server" }]
     * @return informazioni della persona che ha loggato
     */
    public Persona readJSONPersona (String jsonString ) {

        String nome=null;
        String cognome=null;
        String profile_image=null;
        Persona temp = new Persona();

        try {

            jsonString = "{\"persona\":"+ jsonString + "}";
            Log.w("JSONReader", jsonString);
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray info = jsonObj.getJSONArray("persona");
            JSONObject c = info.getJSONObject(0);
            //nome = c.getString("nome");
            //cognome = c.getString("cognome");
            //profile_image = c.getString("profile_image");
            temp.setCognome(c.getString("cognome"));
            temp.setNome(c.getString("nome"));
            temp.setProfileImage(c.getString("profile_image"));

            Log.d("JSONReader", "ProfileImage é : " + temp.getProfileImage());

            return temp;

        }catch (Exception e){
            Log.w("Exception" , "Eccezione durante lettura JSON Login");
        }

        return null;

    }

    public ArrayList<Persona> readJSONCoinquilini (String jsonString , String id_home ) {

        //new_home_intent = this.getIntent();
        //temp_persona = new_home_intent.getParcelableExtra("persona");

        ArrayList<Persona> coinquilini= new ArrayList<Persona>();

        try {


            jsonString = "{\"coinquilini\":"+ jsonString + "}";
            Log.w("JSONReader", jsonString);
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray json_coinquilini = jsonObj.getJSONArray("coinquilini");
            for(int i = 0; i< json_coinquilini.length(); i++) {
                JSONObject c = json_coinquilini.getJSONObject(i);



                //Log.w("INFORMAIONE" , image_stanza);


                Persona persona_temp = new Persona(c.getString("nome") , c.getString("cognome") , c.getString("mail") , c.getString("profile_image") , id_home , null );
                //Stanza stanza = new Stanza(c.getString("stanza_image"),c.getString("nome_stanza"));
                coinquilini.add(persona_temp);

            }

            return coinquilini;

        }catch (Exception e){
            Log.d("ConfigurazioneStanze" , "Eccezione catturata nel ReadJSON");
        }

        //Globals g = Globals.getInstance();
        //g.setStanze(vectorStanze);


        return null;

    }


}