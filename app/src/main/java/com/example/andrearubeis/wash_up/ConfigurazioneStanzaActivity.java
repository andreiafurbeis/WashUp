package com.example.andrearubeis.wash_up;

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
import android.os.Handler;
import android.os.Looper;
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
import com.squareup.picasso.Target;

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


public class ConfigurazioneStanzaActivity extends AppCompatActivity{

    Button add;
    Button continua;
    ListView listview;
    Persona temp_persona;
    JSONReader reader_json;
    SharedPreferences pref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurazione_stanze);


        //Nascondi AppBar
        ActionBar barra = getSupportActionBar();
        barra.hide();

        listview = (ListView) findViewById(R.id.configurazione_listview);
        add = (Button) findViewById(R.id.configurazione_button_add);
        continua = (Button) findViewById(R.id.configurazione_button_continua);


        pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);

        reader_json = new JSONReader(getApplicationContext());

        Gson gson = new Gson();
        String json = pref.getString("persona", "");
        temp_persona = gson.fromJson(json, Persona.class);
        Log.d("ConfigurazioneStanze" , "La persona si chiama " + temp_persona.getNome() + temp_persona.getMail() + temp_persona.getProfileImage());

        if(temp_persona == null) {
            Log.d("ConfigurazioneStanze" , "L'oggetto appena scaricato dalle SharedPreference é NULL");
        }



        //INIZIALIZZAZIONE DATABASE CON STANZE STANDARD
        inizializationNewHome();


        Log.w("INFORMATION" , "sto per entrare nella configurazione della nuova classe");


        //INIZIALIZZAZIONE INTERFACCIA DINAMICA
        inizializationInterface();

        Globals g = Globals.getInstance();
        g.setInfoUtente(temp_persona);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Globals g = Globals.getInstance();
                Intent intent = new Intent(ConfigurazioneStanzaActivity.this,
                        AggiungiStanzaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("home_id" , g.getIdString());
                //bundle.putParcelable("persona" ,temp_persona);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });



        continua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Log.w("INFORMATION" , "l'oggetto persona contiene : " + temp_persona.getNome() + " " + temp_persona.getStanze().toString());

                Intent intent = new Intent(ConfigurazioneStanzaActivity.this, bottom_activity.class);

                //Log.w("INFORMATION" , "l'oggetto persona contiene : " + temp_persona.getNome() + " " + temp_persona.getStanze().get(0).getNameStanza() + " " + temp_persona.getStanze().get(0).getImageStanza());


                if(temp_persona.getStanze() == null) {
                    Toast.makeText(getApplicationContext(),"ConfigurazioneStanze : il vettore è NULL ",Toast.LENGTH_SHORT).show();
                    Log.d("ConfigurazioneStanze" , "Il vettore tornato è NULL");
                }else{
                    Log.d("ConfigurazioneStanze" , "Il vettore tornato è : " + temp_persona.getStanze().toString());
                }

                startActivity(intent);



            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("immagine_back" , "Sono nell'OnResume");
        inizializationInterface();
    }


    /**
     * Crezione delle stanze Standard (bagno,cucina,camera)
     */
    private void inizializationNewHome(){

        Globals g = Globals.getInstance();

        //INIZIALIZZAZIONE BAGNO
        Stanza bagno = new Stanza( g.getDomain()+"bagno.png", "bagno" , g.getIdString());

        //INIZIALIZZAZIONE CUCINA
        Stanza cucina = new Stanza( g.getDomain()+"cucina.png", "cucina" , g.getIdString());

        //INIZIALIZZAZIONE CAMERA
        Stanza camera = new Stanza( g.getDomain()+"camera.png", "camera" , g.getIdString());


        String data = getURLData(bagno , cucina , camera);


        addStanza(data);


    }


    /**
     * Nel caso fosse una nuova casa , aggiunge le stanze di default
     * @param data
     */
    private void addStanza(String data) {
        URL url=null;
        Globals g = Globals.getInstance();

        String temp_url = g.getDomain()+"starting_rooms.php"+data;
        Log.d("ConfigurazioneStanza", "Questo é l'URL per l'inizializzazione : " + temp_url);
        try {
            url = new URL(temp_url);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Creazione URL non riuscita", Toast.LENGTH_SHORT).show();
        }


        try {
            new TaskAsincrono(getApplicationContext(), url, new TaskCompleted() {
                @Override
                public void onTaskComplete(Object resp) {


                }
            }).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }


    /**
     *
     * @param temp1
     * @param temp2
     * @param temp3
     * @return la stringa URL con cui inviare la richiesta di aggiunta stanze standard al server
     */
    public String getURLData (Stanza temp1 , Stanza temp2 , Stanza temp3) {

        String url_data = "?home_id="+temp_persona.getIdHome()+"&image1="+temp1.getImageStanza()+"&image2="+temp2.getImageStanza()+"&image3="+temp3.getImageStanza();
        return url_data;

    }


    /**
     * Avvia la creazione dinamica dell'interfaccia , prima fa la richiesta HTTP poi chiama la funzione creaInterfaccia()
     */
    public void inizializationInterface() {

        URL url=null;
        Globals g = Globals.getInstance();


        try {
            url = new URL(g.getDomain() + "get_stanze.php?home_id="+temp_persona.getIdHome());
        }catch(IOException e){
            Toast.makeText(getApplicationContext(),"Creazione URL non riuscita",Toast.LENGTH_SHORT).show();
        }

        new TaskAsincrono(getApplicationContext(), url , new TaskCompleted() {
            @Override
            public void onTaskComplete(Object resp) {


                String result = getStringFromInputStream((InputStream) resp);

                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();

                temp_persona.setStanze(reader_json.readJSONStanze(result));

                creaInterfaccia();

            }
        }).execute();
    }



    /**
     *
     * Popolamento della ListView con tutte le stanze della casa e assegnamento OnClick
     */
    public void creaInterfaccia(){


        pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("persona");
        editor.apply();
        Gson gson = new Gson();
        String json = gson.toJson(temp_persona);
        editor.putString("persona", json);
        // Save the changes in SharedPreferences
        editor.commit(); // commit changes





        if(temp_persona.getStanze() != null ) {
            Log.d("ConfigurazioneStanze" , "Il vector ha : " + temp_persona.getStanze().size() + " elementi");
        }else{
            Log.d("ConfigurazioneStanze" , "Il vector é NULL");
        }


        AdapterStanze adapter = new AdapterStanze(getApplicationContext() , temp_persona.getStanze());


        if(adapter == null ) {
            Log.d("ConfigurazioneStanze" , "L'adapter é NULL");
        }

        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
