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
        list = findViewById(R.id.ruoli_x_stanza_list);


        reader_json = new JSONReader(getApplicationContext());


        Intent intent = getIntent();

        temp = intent.getParcelableExtra("stanza");

    }


    @Override
    protected void onStart() {
        super.onStart();

        inizializzazioneInterface();


    }


    private void inizializzazioneInterface() {

        String [] giorni_settimana = {"Domenica" , "Lunedí" , "Martedí" , "Mercoledí" , "Giovedí" , "Venerdí" , "Sabato"};
        String [] mesi = {"Gennaio" , "Febbraio" , "Marzo" , "Aprile" , "Maggio" , "Giugno" , "Luglio" , "Agosto" , "Settembre" , "Ottobre" , "Novembre" , "Dimcebre"};
        GregorianCalendar gc = new GregorianCalendar();

        String data_odierna = giorni_settimana[gc.get(Calendar.DAY_OF_WEEK)-1] + " " + gc.get(Calendar.DAY_OF_MONTH) + " " + mesi[gc.get(Calendar.MONTH)];
        text_data.setText(data_odierna);



        //Log.d("immagine","RuoliStanza : La stringa dell'immagine é : " + temp.getImageStanza());


        nome_stanza.setText(temp.getNameStanza());
        Picasso.get().load(temp.getImageStanza()).fit().centerCrop().into(image_stanza,new com.squareup.picasso.Callback(){

            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

            }
        });


        getInfoCompitiStanze();


    }


    /**
     * Richiesta al DataBase per avere un file JSON composto da { Immagine profilo Utente , Descrizione Compito }
     */
    private void getInfoCompitiStanze () {

            URL url=null;
            Globals g = Globals.getInstance();


            try {
                url = new URL(g.getDomain() + "get_ruoli_x_stanza.php?home_id="+g.getIdString()+"&nome_stanza="+temp.getNameStanza());
            }catch(IOException e){
                Toast.makeText(getApplicationContext(),"Creazione URL non riuscita",Toast.LENGTH_SHORT).show();
            }

            new TaskAsincrono(getApplicationContext(), url , new TaskCompleted() {
                @Override
                public void onTaskComplete(Object resp) {


                    String result = reader_json.getStringFromInputStream((InputStream) resp);



                    //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();

                    ArrayList<Compito> temp_interface = reader_json.readJSONCompitiXStanza(result);

                    listViewPopolation(temp_interface);

                }
            }).execute();


    }

    private void listViewPopolation (ArrayList<Compito> compiti) {
        AdapterRuoliStanza adapter = new AdapterRuoliStanza(this,compiti);
        list.setAdapter(adapter);
    }

}
