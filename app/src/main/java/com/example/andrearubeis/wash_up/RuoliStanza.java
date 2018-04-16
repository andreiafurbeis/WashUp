package com.example.andrearubeis.wash_up;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by nicoloricci on 27/12/17.
 */

public class RuoliStanza extends AppCompatActivity {

    TextView text_data;
    //Button button_immagine;
    ImageView image_stanza;
    TextView nome_stanza;
    Stanza temp;

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
        nome_stanza = (TextView) findViewById(R.id.ruoli_x_stanza_name);

        Intent intent = getIntent();

        //nome_stanza = intent.getStringExtra("nome_stanza");
        //image_stanza = intent.getStringExtra("image_stanza");
        //id_home = intent.getStringExtra("home_id");

        temp = new Stanza( intent.getStringExtra("image_stanza") , intent.getStringExtra("nome_stanza") , intent.getStringExtra("home_id"));


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

        String [] image_path = temp.getImageStanza().split(" ");

        Log.d("immagine","RuoliStanza : La stringa dell'immagine é : " + temp.getImageStanza());


        /*if(image_path[0].equals("D")){

            image_drawable = getResources().getDrawable(Integer.parseInt(image_path[1]));

        }else{

            //Toast.makeText(getApplicationContext(),"Sto mettendo l'immagine con il metodo nuovo" ,Toast.LENGTH_SHORT).show();
            //ImageManager manager = new ImageManager(getApplicationContext());
            ImageManager manager = new ImageManager(getApplicationContext());

            Bitmap image_bitmap = manager.loadImageFromStorage(image_path[1],image_path[2]);
            //Bitmap image_bitmap_scaled = Bitmap.createScaledBitmap(image_bitmap , ViewGroup.LayoutParams.MATCH_PARENT , getResources().getDisplayMetrics().heightPixels/6,true);
            image_drawable = new BitmapDrawable(getResources() , image_bitmap);

        }*/

        nome_stanza.setText(temp.getNameStanza());
        Picasso.get().load(temp.getImageStanza()).into(image_stanza,new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {


            }

            @Override
            public void onError(Exception e) {

            }

        });

        //button_immagine.setText(temp.getNameStanza());
        //button_immagine.setBackground(image_drawable);


    }

}
