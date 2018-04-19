package com.example.andrearubeis.wash_up;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by nicoloricci on 27/12/17.
 */

public class RuoliStanza extends AppCompatActivity {

    TextView text_data;
    TextView nome_stanza;
    ImageView image_stanza;
    Stanza temp;
    ListView list;
    JSONReader reader_json;

    //String image_stanza;
    //String nome_stanza;
    //String id_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruoli_x_stanza);

        ActionBar barra = getSupportActionBar();
        barra.hide();

        text_data = (TextView) findViewById(R.id.ruoli_x_stanza_date_bar);
        image_stanza = (ImageView) findViewById(R.id.ruoli_x_stanza_image);
        nome_stanza = (TextView) findViewById(R.id.ruoli_x_stanza_name) ;

        reader_json = new JSONReader(getApplicationContext());


        Intent intent = getIntent();

        //temp = new Stanza( intent.getStringExtra("image_stanza") , intent.getStringExtra("nome_stanza") , intent.getStringExtra("home_id"));
        //nome_stanza = intent.getStringExtra("nome_stanza");
        //image_stanza = intent.getStringExtra("image_stanza");
        //id_home = intent.getStringExtra("home_id");



        //creo un ArrayList provvisorio per vedere se tutto funziona
        //campo stanza e' l'immagine della persona che deve fare il copito
        //uso un'immagine profilo di default


        /*String img_profilo = "\"/data/user/0/com.example.andrearubeis.wash_up/app_imageDir photo_20180413_09_48_28.jpg\"";


        ArrayList<Compito> compiti = new ArrayList<Compito>();
        Compito cmp = new Compito(null,"lava",img_profilo,null);
        Compito cmp1 = new Compito(null,"baadsas",img_profilo,null);
        Compito cmp2 = new Compito(null,"bagndsdsdo",img_profilo,null);
        Compito cmp3 = new Compito(null,"bagdsdsno",img_profilo,null);
        Compito cmp4 = new Compito(null,"bagddddno",img_profilo,null);

        compiti.add(cmp);
        compiti.add(cmp1);
        compiti.add(cmp2);
        compiti.add(cmp3);
        compiti.add(cmp4);*/








        //temp = new Stanza( intent.getStringExtra("image_stanza") , intent.getStringExtra("nome_stanza") , intent.getStringExtra("home_id"));
        temp = intent.getParcelableExtra("stanza");

        list = findViewById(R.id.ruoli_x_stanza_list);
        if (temp.getCompiti()!= null){
            Log.d("RuoliStanza","IL VETTORE A: " + temp.getCompiti().size());
        }else{
            Log.d("RuoliStanza","IL VETTORE E' NULL");

        }

        //DA RIFARE , I COMPITI DA STAMPARE QUI DENTRO DEVO PRENDERLI DAL DATABASE , QUERY PER PRENDERE I COMPITI DA ASSEGNAMENTI (AVRO' UN FILE JSON DA GESTIRE CON ANCHE L' IMMAGINE DELL' INQUILINO CHE DEVE SVOLGERLO)

        AdapterRuoliStanza adapter = new AdapterRuoliStanza(this,temp.getCompiti());
        list.setAdapter(adapter);








    }


    @Override
    protected void onStart() {
        super.onStart();

        inizializzazioneInterface();


    }


    private void inizializzazioneInterface() {

        Drawable image_drawable = null;

        String [] giorni_settimana = {"Domenica" , "Lunedí" , "Martedí" , "Mercoledí" , "Giovedí" , "Venerdí" , "Sabato"};
        String [] mesi = {"Gennaio" , "Febbraio" , "Marzo" , "Aprile" , "Maggio" , "Giugno" , "Luglio" , "Agosto" , "Settembre" , "Ottobre" , "Novembre" , "Dimcebre"};
        GregorianCalendar gc = new GregorianCalendar();

        String data_odierna = giorni_settimana[gc.get(Calendar.DAY_OF_WEEK)-1] + " " + gc.get(Calendar.DAY_OF_MONTH) + " " + mesi[gc.get(Calendar.MONTH)];
        text_data.setText(data_odierna);



        Log.d("immagine","RuoliStanza : La stringa dell'immagine é : " + temp.getImageStanza());


        nome_stanza.setText(temp.getNameStanza());
        Picasso.get().load(temp.getImageStanza()).into(image_stanza,new com.squareup.picasso.Callback(){

            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

            }
        });


        //getInfoCompitiStanze();


    }


    /**
     * Richiesta al DataBase per avere un file JSON composto da { Immagine profilo Utente , Descrizione Compito }
     */
    /*private void getInfoCompitiStanze () {

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


                    String result = reader_json.getStringFromInputStream((InputStream) resp);

                    //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();




                }
            }).execute();


    }*/

}
