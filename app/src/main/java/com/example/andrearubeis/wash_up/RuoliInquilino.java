package com.example.andrearubeis.wash_up;

/**
 * Created by nicoloricci on 27/12/17.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.andrearubeis.wash_up.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
public class RuoliInquilino extends AppCompatActivity {




    Button title ;
    Persona temp_persona ;
    ArrayList<Compito> compiti;
    LinearLayout linear;
    ListView list_stanze;
    ListView list_compiti;
    ImageView profile_image;
    ArrayList<String> stanze_da_sistemare;
    ArrayList<Stanza> stanze;
    Persona global_temp_persona;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruoli_x_inquilino);

        ActionBar barra = getSupportActionBar();
        barra.hide();


        Intent intent = getIntent();


        profile_image = (ImageView) findViewById(R.id.ruoli_x_inquilino_image);

        temp_persona = intent.getParcelableExtra("persona");
        compiti = temp_persona.getCompiti();

        Log.d("RuoliInquilino" , "Questo inquilino ha da svolgere : " + compiti.size() + " compiti");


        SharedPreferences pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = pref.getString("persona", "");
        global_temp_persona = gson.fromJson(json, Persona.class);
        Log.d("BottomActivity" , "Aggiorno TEMP_PERSONA");
        if(global_temp_persona == null) {
            Log.d("ConfigurazioneStanze" , "L'oggetto appena scaricato dalle SharedPreference é NULL");
        }


        title = (Button) findViewById(R.id.ruoli_x_inquilino_title);
        title.setText(temp_persona.getNome());
        //title.setCompoundDrawablesRelative(immagine_profilo_drawable,null,null,null);

        Picasso.get().load(temp_persona.getProfileImage()).into(profile_image , new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {


            }

            @Override
            public void onError(Exception e) {

            }

        });

        /*stanze_da_sistemare = new ArrayList<String>();
        if(compiti != null) {
            for (Compito c : compiti) {
                String nome_stanza_temp = c.getStanza();
                if (!stanze_da_sistemare.contains(nome_stanza_temp)) {
                    stanze_da_sistemare.add(nome_stanza_temp);
                    Log.d("RuoliInquilino","é stata aggiunta la stanza " + nome_stanza_temp);

                }
            }
        }else{
            Log.d("RuoliInquilino","il vettore compiti e' null");
        }


        int size = stanze_da_sistemare.size();
        Log.d("RuoliInquilino", "il numero di stanze e': " + size);




        stanze = new ArrayList<Stanza>();

        for(String s: stanze_da_sistemare){
            ArrayList<Compito> compitixstanza = new ArrayList<Compito>();
            for(Compito c : compiti){
                if(c.getStanza().equals(s)){
                    Log.d("RuoliInquilino" , "Aggiungo il compito : " +c.getDescrizione());
                    compitixstanza.add(c);
                }
            }

            Stanza stanza_temp = new Stanza(null,s);
            stanza_temp.setCompiti(compitixstanza);
            stanze.add(stanza_temp);

        }*/

        ArrayList<Compito> struttura_temporanea_per_inizializzazione= new ArrayList<Compito>();
        String nome_stanza_in_corso = "";
        for(int i = 0 ; i < temp_persona.getCompiti().size() ; i++) {
            String nome_stanza_da_aggiungere = temp_persona.getCompiti().get(i).getStanza();
            //Log.d("RuoliInquilino","Sto per aggiungere un compiton in  : " +nome_stanza_da_aggiungere);
            int indice_stanza = global_temp_persona.getIndiceStanza(nome_stanza_da_aggiungere);


            if(!nome_stanza_da_aggiungere.equals(nome_stanza_in_corso)) {
                struttura_temporanea_per_inizializzazione.add(new Compito(null,global_temp_persona.getStanze().get(indice_stanza).getImageStanza(),temp_persona.getCompiti().get(i).getStanza()));
                Log.d("RuoliInquilino","Sto aggiungendo la riga : " + temp_persona.getCompiti().get(i).getStanza());
            }
            struttura_temporanea_per_inizializzazione.add(new Compito(temp_persona.getCompiti().get(i).getDescrizione(),null,null));
            nome_stanza_in_corso = nome_stanza_da_aggiungere;
        }


        list_stanze = findViewById(R.id.ruoli_x_inquilino_list);
        AdapterStanzeRuoli  adapter = new AdapterStanzeRuoli(this, struttura_temporanea_per_inizializzazione);
        list_stanze.setAdapter(adapter);



    }

}
