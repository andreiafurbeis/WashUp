package com.example.andrearubeis.wash_up;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.andrearubeis.wash_up.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewHome extends AppCompatActivity {



    private Button continua;

    private String id_home;
    Intent main_activity_intent;
    Persona temp_persona;
    SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home);
        ActionBar barra = getSupportActionBar();
        barra.hide();


        pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = pref.getString("persona", "");
        temp_persona = gson.fromJson(json, Persona.class);
        Log.d("NewHome" , "La persona si chiama " + temp_persona.getNome() + " con mail : " + temp_persona.getMail());

        if(temp_persona == null) {
            Log.d("NewHome" , "L'oggetto appena scaricato dalle SharedPreference é NULL");
        }


        continua = (Button) findViewById(R.id.new_home_button_continua);
        continua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    creationNewHome(temp_persona.getMail());

                }
                catch(Exception e) {
                    Log.d("NewHome" , "Eccezione catturata nell'OnCreate");
                }

            }
        });


    }


    /**
     * Crea la nuova casa e la aggiunge al database
     * @param mail
     */
    private void creationNewHome(String mail) {
        URL url=null;
        Globals g = Globals.getInstance();

        String temp_url = g.getDomain()+"new_home_creation.php";
        try {
            url = new URL(temp_url+"?usr="+mail);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Creazione URL non riuscita", Toast.LENGTH_SHORT).show();
        }


        try {
            new TaskAsincrono(getApplicationContext(), url, new TaskCompleted() {
                @Override
                public void onTaskComplete(Object resp) {
                    String result = getStringFromInputStream((InputStream) resp);
                    goToConfiguration(result);
                }
            }).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }


    @Override
    protected void onStart() {
        super.onStart();




    }


    /**
     * Imposta l'id della nuova casa nel file Persona globale e procede in ConfigurazioneStane
     * @param id
     */
    private void goToConfiguration(String id) {
        Globals g = Globals.getInstance();
        g.setIdString(id);

        temp_persona.setIdHome(id);

        //AGGIORNO INFORMAZIONI GLOBALI APLICAZIONE
        pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("persona");
        editor.apply();
        Gson gson = new Gson();
        String json = gson.toJson(temp_persona);
        editor.putString("persona", json);
        // Save the changes in SharedPreferences
        editor.commit(); // commit changes

        Toast.makeText(getApplicationContext(), id_home, Toast.LENGTH_SHORT).show();


        if(temp_persona == null ) {
            Log.d("NewHome" , "L'oggetto Persona é NULL");
        }

        Intent intent = new Intent(NewHome.this, ConfigurazioneStanzaActivity.class);
        startActivity(intent);
    }


    /**
     *
     * @param is
     * @return trasforma l'InputStream in Stringa
     */
    private static String getStringFromInputStream(InputStream is) {

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
