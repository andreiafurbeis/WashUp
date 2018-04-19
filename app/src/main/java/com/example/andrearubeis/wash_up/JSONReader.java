package com.example.andrearubeis.wash_up;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class JSONReader {

    Context context;

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
            Log.w("INFORMATION", jsonString);
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray compiti = jsonObj.getJSONArray("compiti");
            for(int i = 0; i< compiti.length(); i++) {
                JSONObject c = compiti.getJSONObject(i);
                Compito compito = new Compito(c.getString("id_compito") , c.getString("descrizione") , c.getString("stanza") , c.getString("id_casa"));
                vectorCompiti.add(compito);

            }

        }catch (Exception e){
            Log.d("ConfigurazioneStanze" , "Eccezione catturata nel ReadJSONCompiti");
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

        try {

            jsonString = "{\"coinquilini\":"+ jsonString + "}";
            Log.w("JSONReader", jsonString);
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray json_coinquilini = jsonObj.getJSONArray("coinquilini");

            for(int i = 0; i< json_coinquilini.length(); i++) {

                JSONObject c = json_coinquilini.getJSONObject(i);
                Persona persona_temp = new Persona(c.getString("nome") , c.getString("cognome") , c.getString("mail") , c.getString("profile_image") , id_home , null );
                //Stanza stanza = new Stanza(c.getString("stanza_image"),c.getString("nome_stanza"));
                persona_temp.setCompiti(readJSONCompiti(c.getString("compiti")));
                coinquilini.add(persona_temp);

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

            //Log.w("INFORMAIONE" , image_stanza);

            //Bitmap immagine = stringToBitmap(image_stanza);



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