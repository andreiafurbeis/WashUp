package com.example.andrearubeis.wash_up;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrearubeis.wash_up.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity  {

    private TaskAsincrono httpRequest = null;
    EditText username;
    EditText password;
    Button invia;
    TextView log;
    Button registrazione;
    InputStream in = null;
    URL url = null;
    ArrayList<Persona> coinquilini_global;
    SharedPreferences pref;
    Persona temp_persona;
    View parentView;
    JSONReader json_reader;

    //private final String domain_url = "http://192.168.0.24/";   //dominio portatile
    //private final String domain_url = "http://192.168.1.100/";  //dominio fisso
    //private final String domain_url = "http://rubeisandrea.myqnapcloud.com/"; //domanio nas
    //private final String domain_url = "http://washit.dek4.net/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Nascondere AppBar

        ActionBar barra = getSupportActionBar();
        barra.hide();


        username = (EditText) findViewById(R.id.login_text_username);
        password = (EditText) findViewById(R.id.login_text_password);
        invia = (Button) findViewById(R.id.login_button_login);
        registrazione = (Button) findViewById(R.id.login_button_registrazione);
        parentView = (View) findViewById(R.id.login_parent_view);

        json_reader = new JSONReader(getApplicationContext());

        invia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginRequest();
            }
        });

        registrazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Registrazione.class);
                startActivity(intent);
            }
        });

    }


    /**
     * Il file JSON di risposta é cosí composto :
     *
     * 1/0 ---> 1                                                                                                           se login effettuato , 0 altrimenti
     * [{ "id" : x  }]                                                                                                      se "abita" giá da qualche parte , ritorna l'id della casa
     * [{ "nome_stanza" : "nome" , "stanza_image" : "url immagine presente sul server" }                                    se "abita" giá da qualche parta , ritorna un ArrayList delle Stanze presenti in casa
     * [{ "nome" : "nome_persona" ,"cognome": "cognome_persona" , "profile_image" : "url immagine presente sul server" }]    ritorna i dati della persona loggata
     */



    private void loginRequest() {
            String usr = username.getText().toString();
            String pwd = md5(password.getText().toString());
            String risultato = null;
            Globals g = Globals.getInstance();

            //CREAZIONE URL
            try {
                String url_temp = g.getDomain() + "login.php?usr=" + usr + "&psw=" + pwd;
                //Toast.makeText(getApplicationContext(),url_temp ,Toast.LENGTH_SHORT).show();

                url = new URL(url_temp);
            }catch(IOException e){
                Toast.makeText(getApplicationContext(),"Creazione URL non riuscita",Toast.LENGTH_SHORT).show();
            }

            g.setMail(usr.toString());


            try {
                new TaskAsincrono(getApplicationContext(), url , new TaskCompleted() {
                    @Override
                    public void onTaskComplete(Object resp) {


                        String result = json_reader.getStringFromInputStream((InputStream) resp);

                        //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();

                        String[] parts = result.split("<br>");

                        Log.d("MainActivity" , "La stringa risultante é : " + result);

                        Globals g = Globals.getInstance();

                        if(parts[0].equals("1")) {
                            //INIZIALIZZAZIONE OGGETTO PERSONA GLOBALE IN TUTTA L'APP ATTRAVERSO SharedPreferences
                            temp_persona = new Persona(null , null , username.getText().toString() , null , null , null);

                            //LETTURA PARTE ID CASA DA JSON
                            //String JSON_id = readJSON(parts[1]);

                            //Metodo nuovo readJSON
                            String JSON_id = json_reader.readJSONId(parts[1]);

                            //LETTURA PARTE INFO PERSONA LOGGATA DA JSON
                            //readJSONPersona(parts[3]);

                            //Metodo nuovo readJSONPersona
                            Persona dati_da_salvare = json_reader.readJSONPersona(parts[3]);
                            temp_persona.setNome(dati_da_salvare.getNome());
                            temp_persona.setCognome(dati_da_salvare.getCognome());
                            temp_persona.setProfileImage(dati_da_salvare.getProfileImage());



                            //SALVATAGGIO temp_persona IN SharedPreferences
                            pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.remove("persona");
                            editor.apply();
                            Gson gson = new Gson();
                            String json = gson.toJson(temp_persona);
                            editor.putString("persona", json);
                            // Save the changes in SharedPreferences
                            editor.commit(); // commit changes


                            //Toast.makeText(getApplicationContext(),g.getIdString(),Toast.LENGTH_SHORT).show();


                            if(JSON_id!=null) {
                                //SE LA PERSONA LOGGATA ABITA GIÁ IN QUALCHE CASA

                                temp_persona.setIdHome(JSON_id);
                                //SETTO L'ID DELLA CASA NELLA VARIABILE GLOBALS ACCESSIBILE DA TUTTE LE ACTIVITY
                                g.setIdString(JSON_id);


                                //AGGIUNGO LE STANZE DELLA CASA AI DATI GLOBALI
                                temp_persona.setStanze(json_reader.readJSONStanze(parts[2]));

                                //AGGIUNGO TUTTI I COINQUILINI AI DATI DELLA CASA E PROCEDO CON IL LOGIN
                                getCoinquilini();


                            }else {

                                //SE LA PERSONA CREATA DEVE CREARE UNA NUOVA CASA

                                //Toast.makeText(getApplicationContext(),g.getIdString(),Toast.LENGTH_SHORT).show();


                                Intent intent = new Intent(MainActivity.this, ConfigurazioneStanzaTutorial.class);
                                startActivity(intent);
                            }


                        }else{


                            Toast.makeText(getApplicationContext(),"Login non riuscito",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
    }




    /**
     * Il file JSON di risposta é cosí composto :
     *
     * [{ "nome" : "nome_persona" , "cognome" : "cognome_persona" , "mail" : "mail_persona" , "profile_image" : "url immagine presente sul server" } , ...
     *
     *
     */

    public void getCoinquilini() {

        URL url=null;
        Globals g = Globals.getInstance();

        try {
            url = new URL(g.getDomain() + "get_coinquilini.php?home_id="+temp_persona.getIdHome()+"&mail="+temp_persona.getMail());
        }catch(IOException e){
            Toast.makeText(getApplicationContext(),"Creazione URL non riuscita",Toast.LENGTH_SHORT).show();
        }

        new TaskAsincrono(getApplicationContext(), url , new TaskCompleted() {
            @Override
            public void onTaskComplete(Object resp) {

                Globals g = Globals.getInstance();

                String result = json_reader.getStringFromInputStream((InputStream) resp);

                //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();

                temp_persona.setCoinquilini(json_reader.readJSONCoinquilini(result , temp_persona.getIdHome()));

                getLogged();

            }
        }).execute();



    }



    private void getLogged() {

        //AGGIORNO INFORMAZIONI SU SharedPreferences
        pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("persona");
        editor.apply();
        Gson gson = new Gson();
        String json = gson.toJson(temp_persona);
        Log.d("MainActivity" , "La persona si chiama " + temp_persona.getNome());
        editor.putString("persona", json);
        // Save the changes in SharedPreferences
        editor.commit(); // commit changes



        Intent intent = new Intent(MainActivity.this, bottom_activity.class);
        startActivity(intent);
    }




    /**
     *
     * @param in
     * @return codifica MD5 della stringa
     */
    private String md5(String in) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (int i = 0; i < len; i++) {
                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 0x0f, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); }
        return null;
    }



}
