package com.example.andrearubeis.wash_up;

/**
 * Created by nicoloricci on 27/12/17.
 */

import android.app.ProgressDialog;
import android.content.Intent;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruoli_x_inquilino);

        ActionBar barra = getSupportActionBar();
        barra.hide();


        Intent intent = getIntent();


        profile_image = (ImageView) findViewById(R.id.ruoli_x_inquilino_image);

        temp_persona = intent.getParcelableExtra("persona");
        compiti = temp_persona.getCompiti();
        //compiti = new ArrayList<Compito>();

        /*Compito cmp = new Compito(null,"lava","camera",null);
        Compito cmp1 = new Compito(null,"baadsas","salotto",null);
        Compito cmp2 = new Compito(null,"bagndsdsdo","cucina",null);
        Compito cmp3 = new Compito(null,"bagdsdsno","bagno",null);
        Compito cmp4 = new Compito(null,"bagddddno","bagno",null);

        compiti.add(cmp);
        compiti.add(cmp1);
        compiti.add(cmp2);
        compiti.add(cmp3);
        compiti.add(cmp4);
*/

        //String immagine_profilo = temp_persona.getProfileImage();
        //Drawable immagine_profilo_drawable = getDrawable(immagine_profilo) ;

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

        stanze_da_sistemare = new ArrayList<String>();
        if(compiti != null) {
            for (Compito c : compiti) {
                String stanza_temp = c.getStanza();
                if (stanze_da_sistemare.contains(stanza_temp)) {

                } else {
                    stanze_da_sistemare.add(stanza_temp);
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
                if(c.getStanza()==s){
                    compitixstanza.add(c);
                }
            }

            Stanza stanza_temp = new Stanza(null,s);
            stanza_temp.setCompiti(compitixstanza);
            stanze.add(stanza_temp);

        }


        list_stanze = findViewById(R.id.ruoli_x_inquilino_list);
        AdapterStanze  adapter = new AdapterStanze(this, stanze);
        list_stanze.setAdapter(adapter);



    }
















    public Drawable getDrawable(String image) {
        Drawable image_drawable;
        String[] image_path = image.split(" ");


        Toast.makeText(getApplicationContext(), "Sto mettendo l'immagine con il metodo nuovo", Toast.LENGTH_SHORT).show();
        ImageManager manager = new ImageManager(getApplicationContext());
        Bitmap image_bitmap = manager.loadImageFromStorage(image_path[0], image_path[1]);
        Log.d("OptionFragment" , "il path è : " + image_path[0] + "   " + image_path[1]);
        Bitmap image_bitmap_scaled = Bitmap.createScaledBitmap(image_bitmap , ViewGroup.LayoutParams.MATCH_PARENT , getResources().getDisplayMetrics().heightPixels/6,true);
        image_drawable = new BitmapDrawable(getResources(), image_bitmap);


        return image_drawable;
    }






}
