package com.example.andrearubeis.wash_up;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class JSONReader {

    Context context;
    Persona temp_persona;

    public JSONReader ( Context context ) {
        this.context = context;
    }


    /**
     *
     * @param jsonString
     * @return
     */
    public ArrayList<Compito> readJSONCompiti(String jsonString) {

        ArrayList <Compito> vectorCompiti= new ArrayList<Compito>();


        try {


            jsonString = "{\"compiti\":"+ jsonString + "}";
            //Log.w("INFORMATION", jsonString);
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray compiti = jsonObj.getJSONArray("compiti");
            for(int i = 0; i< compiti.length(); i++) {
                JSONObject c = compiti.getJSONObject(i);
                Compito compito = new Compito(c.getString("id_compito") , c.getString("descrizione") , c.getString("stanza") , c.getString("id_casa"));
                if(c.has("svolto") == true) {
                    //Log.d("JSONReader","Sto assegnando il flag svolto con valore : " + c.getString("svolto") + "al compito con descrizione : " + c.getString("descrizione"));
                    compito.setSvolto(Integer.parseInt(c.getString("svolto")));
                }else{
                    //Log.d("JSONReader","svolto é NULL");

                }
                vectorCompiti.add(compito);

            }

        }catch (Exception e){
            Log.d("ConfigurazioneStanze" , "Eccezione catturata nel ReadJSONCompiti ");
        }


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



    public ArrayList<Compito> readJSONCompitiXStanza(String jsonString) {

        ArrayList <Compito> vectorCompitiXStanza= new ArrayList<Compito>();

        try {

            jsonString = "{\"ruoliXstanza\":"+ jsonString + "}";
            Log.w("JSONReader", jsonString);
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray json_coinquilini = jsonObj.getJSONArray("ruoliXstanza");

            for(int i = 0; i< json_coinquilini.length(); i++) {

                JSONObject c = json_coinquilini.getJSONObject(i);
                //dentro stanza ho l'immagine , dentro a descrizione la descrizione
                Compito compito_temp = new Compito(c.getString("descrizione") , c.getString("profile_image") , null);
                //Stanza stanza = new Stanza(c.getString("stanza_image"),c.getString("nome_stanza"));
                vectorCompitiXStanza.add(compito_temp);

            }

            return vectorCompitiXStanza;

        }catch (Exception e){

            Log.d("ConfigurazioneStanze" , "Eccezione catturata nel ReadJSON");

        }
        return null;

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
            temp.setCompiti(readJSONCompiti(c.getString("compiti")));
            if(temp.getCompiti() != null) {
                Log.d("JSONReader","il vettore compiti é lungo " + temp.getCompiti().size());
            }else{
                Log.d("JSONReader","il vettore compiti é null");

            }

            Log.d("JSONReader", "ProfileImage é : " + temp.getProfileImage());

            return temp;

        }catch (Exception e){

            Log.w("Exception" , "Eccezione durante lettura JSON Login");

        }

        return null;

    }


    /**
     *
     * @param jsonString
     * @param id_home
     * @return ArrayList di persone contenente tutti i coinquilini dell'utente loggato (non contine l'utente stesso)
     */
    public ArrayList<Persona> readJSONCoinquilini (String jsonString , String id_home ) {

        ArrayList<Persona> coinquilini= new ArrayList<Persona>();

        SharedPreferences pref = context.getSharedPreferences("persona", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = pref.getString("persona", "");
        temp_persona = gson.fromJson(json, Persona.class);
        Log.d("BottomActivity" , "Aggiorno TEMP_PERSONA");
        if(temp_persona == null) {
            Log.d("ConfigurazioneStanze" , "L'oggetto appena scaricato dalle SharedPreference é NULL");
        }


        try {

            jsonString = "{\"coinquilini\":"+ jsonString + "}";
            Log.w("JSONReader", jsonString);
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray json_coinquilini = jsonObj.getJSONArray("coinquilini");

            for(int i = 0; i< json_coinquilini.length(); i++) {

                JSONObject c = json_coinquilini.getJSONObject(i);
                if(!c.getString("mail").equals(temp_persona.getMail())) {
                    Persona persona_temp = new Persona(c.getString("nome"), c.getString("cognome"), c.getString("mail"), c.getString("profile_image"), id_home, null);
                    //Stanza stanza = new Stanza(c.getString("stanza_image"),c.getString("nome_stanza"));
                    persona_temp.setCompiti(readJSONCompiti(c.getString("compiti")));
                    coinquilini.add(persona_temp);
                }else{
                    temp_persona.setCompiti(readJSONCompiti(c.getString("compiti")));
                    temp_persona.setProfileImage(c.getString("profile_image"));

                    //reload temp_persona
                    pref = context.getSharedPreferences("persona", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.remove("persona");
                    editor.apply();
                    gson = new Gson();
                    json = gson.toJson(temp_persona);
                    editor.putString("persona", json);
                    // Save the changes in SharedPreferences
                    editor.commit(); // commit changes



                }

            }

            return coinquilini;

        }catch (Exception e){

            Log.d("ConfigurazioneStanze" , "Eccezione catturata nel ReadJSON");

        }

        return null;

    }


    /**
     *
     * @param jsonString
     * @return Stringa dell'URL della foto della stanza
     */
    public String readJSONStanzaImage (String jsonString) {

        String url=null;

        try {


            jsonString = "{\"info\":"+ jsonString + "}";
            Log.w("INFORMATION", jsonString);
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray info = jsonObj.getJSONArray("info");
            JSONObject c = info.getJSONObject(0);
            url = c.getString("stanza_image");



        }catch (Exception e){

        }

        return url;

    }


    /**
     *
     * @param is
     * @return Trasforma l'InputStream in Stringa
     */
    public String getStringFromInputStream(InputStream is) {

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
    }



}