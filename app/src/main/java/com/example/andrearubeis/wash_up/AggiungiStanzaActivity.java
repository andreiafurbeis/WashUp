package com.example.andrearubeis.wash_up;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.andrearubeis.wash_up.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class AggiungiStanzaActivity extends AppCompatActivity {


    ImageView stanza_image;



    Button title_bar;
    Button aggiungi;
    Button gestisci_compiti;
    EditText edit_nome_stanza;
    final Activity call = this;
    Bitmap imageBitmap;
    int flag_new_image;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE_REQUEST = 2;
    Intent intent;
    public ImageManager manager_image;
    String filePath;
    String fileName;
    private URL url;
    String image_real_path;
    Persona temp_persona;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_stanza);
        ActionBar barra = getSupportActionBar();
        barra.hide();


        title_bar = (Button) findViewById(R.id.aggiungi_stanza_title_bar);
        stanza_image = (ImageView) findViewById(R.id.aggiungi_stanza_profile_image);
        aggiungi = (Button) findViewById(R.id.aggiungi_stanza_button_aggiungi);
        edit_nome_stanza = (EditText)findViewById(R.id.aggiungi_stanza_nome_stanza);
        gestisci_compiti = (Button) findViewById(R.id.aggiungi_stanza_button_compiti);
        intent = getIntent();


        /*if(intent.hasExtra("persona")) {
            temp_persona = intent.getParcelableExtra("persona");
        }else{
            temp_persona = intent.getParcelableExtra("persona2");
            Log.d("AggiungiStanza", "sto assegnando persona2 ");
            if(temp_persona == null ) {
                Log.d("AggiungiStanza", "tempPersona NULL ");

            }

        }*/

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = pref.getString("persona", "");
        temp_persona = gson.fromJson(json, Persona.class);

        if(temp_persona == null) {
            Log.d("ConfigurazioneStanze" , "L'oggetto appena scaricato dalle SharedPreference é NULL");
        }

        ControllaCampi(temp_persona);



        if(intent.hasExtra("update")) {
            aggiungi.setText("Modifica");
            title_bar.setText("Modifica Stanza");
            inizializzazione(); //INIZIALIZZA L'ACTIVITY CON TUTTE LE INFORMAZIONI DELLA STANZA PRECEDENTEMENTE CREATA
        }else{
            title_bar.setText("Aggiungi Stanza");
        }

        if(intent.hasExtra("compiti")) {
            temp_persona.setCompitiStanza(intent.getIntExtra("indice_stanza", -2),intent.<Compito>getParcelableArrayListExtra("compiti"));
            ArrayList<Compito> compiti = intent.<Compito>getParcelableArrayListExtra("compiti");
            //Log.d("AggiungiStanza", "Il vettore compiti é lungo : " + compiti.size());

        }

        if(intent.hasExtra("indice_stanza")) {
            int indice = intent.getIntExtra("indice_stanza", -2);
            if(temp_persona.getCompitiStanza(indice) == null) {
                Log.d("AggiungiStanza", "Il vettore compiti é NULL e l'indice é : " + indice);

            }
        }








        aggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Globals g = Globals.getInstance();



                if(intent.hasExtra("update")) {

                    //MODALITÁ DI MODIFICA , IN QUESTO CASO LA STANZA É GIÁ STATA CREATA , MA VUOLE ESSERE MODIFICATA

                    /*String home_id = intent.getStringExtra("home_id");
                    String nome_stanza = intent.getStringExtra("nome_stanza");*/

                    Stanza temp = new Stanza(null,intent.getStringExtra("nome_stanza"),temp_persona.getIdHome());
                    //String temp_url = g.getDomain() + "update_room.php?home_id=" + home_id + "&nuovo_nome_stanza=" + edit_nome_stanza.getText() + "&nome_stanza=" + nome_stanza + "";
                    String temp_url = g.getDomain() + "update_room.php?home_id=" + temp.getIdCasa() + "&nuovo_nome_stanza=" + edit_nome_stanza.getText() + "&nome_stanza=" + temp.getNameStanza() + "";
                    //Log.d("immagine_click", "il path dell'immagine é : " + filePath.toString());
                    if (flag_new_image == 1) {
                        filePath = manager_image.getImagePath();
                        fileName = manager_image.getImageName();
                        temp_url = temp_url + "&image_stanza=P " + filePath + " " + fileName;
                    }


                    try {
                        url = new URL(temp_url);
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "Creazione URL non riuscita", Toast.LENGTH_SHORT).show();
                    }


                    try {
                        new TaskAsincrono(getApplicationContext(), url, new TaskCompleted() {
                            @Override
                            public void onTaskComplete(Object resp) {


                                String result = getStringFromInputStream((InputStream) resp);

                                if (flag_new_image == 1) {   //DA MODIFICARE IN QUANTO NON É DETTO CHE VENGA CAMBIATA PER FORZA L'IMMAGINA , PUÓ ESSERE CAMBI ANCHE SOLO IL NOME
                                    Toast.makeText(getApplicationContext(), "Stanza aggiornata con successo", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Stanza aggiunta con successo", Toast.LENGTH_SHORT).show();
                                }

                                onBackPressed();


                            }
                        }).execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }else{

                    //MODALITÁ DI AGGIUNTA , LA STANZA DEVE ANCORA ESSERE AGGIUNTA AL DB

                    //String home_id = intent.getStringExtra("home_id");
                    Stanza temp = new Stanza(null,null,temp_persona.getIdHome());
                    if(flag_new_image == 1) {
                        filePath = manager_image.getImagePath();
                        fileName = manager_image.getImageName();

                        String temp_url = g.getDomain() + "update_room.php?home_id=" + temp.getIdCasa() + "&nuovo_nome_stanza=" + edit_nome_stanza.getText() + "&image_stanza=P " + filePath + " " + fileName;

                        try {
                            url = new URL(temp_url);
                        } catch (IOException e) {
                            Toast.makeText(getApplicationContext(), "Creazione URL non riuscita", Toast.LENGTH_SHORT).show();
                        }

                        try {
                            new TaskAsincrono(getApplicationContext(), url , new TaskCompleted() {
                                @Override
                                public void onTaskComplete(Object resp) {


                                    String result = getStringFromInputStream((InputStream) resp);


                                    Toast.makeText(getApplicationContext(), "Stanza aggiunta con successo", Toast.LENGTH_SHORT).show();


                                    onBackPressed();


                                }
                            }).execute().get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }


                    }else{
                        Toast.makeText(getApplicationContext(), "Devi scegliere un immagine prima di aggiungere la Stanza", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });



        stanza_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageChooser image_chooser = new ImageChooser(AggiungiStanzaActivity.this , call);
                //image_chooser.selectImageRoom();
                image_chooser.selectImage();
            }
        });

        gestisci_compiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent_compiti = new Intent(AggiungiStanzaActivity.this,
                        ModificaCompitiActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("nome_stanza" , edit_nome_stanza.getText().toString());


                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                Gson gson = new Gson();
                String json = gson.toJson(temp_persona);
                editor.putString("persona", json);


                // Save the changes in SharedPreferences
                editor.commit();


                //temp_persona = getIntent().getParcelableExtra("persona");



                //bundle.putParcelable("persona" , temp_persona);
                //intent.putExtra("home_id",g.getIdString());
                //intent.putExtra("update",1);
                intent_compiti.putExtras(bundle);
                if(temp_persona == null) {
                    Log.d("AggiungiStanza" , "La persona é NULL" );
                }
                startActivity(intent_compiti);
                finish();



            }
        });





    }



    private void ControllaCampi(Persona temp_persona) {
        if(temp_persona.getStanze() == null) {
            Log.d("AggiungiStanza","Stanze NULL");
        }
        if(temp_persona.getIdHome() == null) {
            Log.d("AggiungiStanza","Stanze NULL");
        }
        if(temp_persona.getCompiti() == null) {
            Log.d("AggiungiStanza" , "Compiti NULL");
        }
        int indice = intent.getIntExtra("indice_stanza",-2);
        /*if(temp_persona.getCompitiStanza(indice) == null) {
            Log.d("AggiungiStanza" , "Compiti Stanza selezionata NULL");

        }*/
    }




    //GESTISCE L'ALERT DIALOG DELLA SCELTA DELL'IMMAGINE , IN QUESTO CASO PUÓ ESSERE SOLAMENTE SCELTA DALLA GALLERIA
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getData()!=null) {
            filePath = data.getData();

            Log.d("Immagine_chooser","il path reale dell'immagine é : " + image_real_path);
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Log.d("Immagine_chooser","il path dell'immagine é : " + filePath);
                stanza_image.setImageBitmap(imageBitmap);
                flag_new_image = 1;             //L'IMMAGINE É STATA MODIFICATA
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/


    /*@Override
    public void onActivityResult(int requestCode , int resultCode , Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 20 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            String partFileName = currentDateFormat();
            storeCameraPhotoInSDCard(bitmap , partFileName);

            flag_new_image = 1;

            String storeFileName = "photo_" + partFileName + ".jpg";
            Bitmap mBitmap = getImageFileFromSDCard(storeFileName);

            stanza_image.setImageBitmap(mBitmap);

        }
    }*/

    @Override
    public void onActivityResult(int requestCode , int resultCode , Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        Bitmap bitmap_image;

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            bitmap_image = (Bitmap) data.getExtras().get("data");
            flag_new_image = 1;
            manager_image = new ImageManager(getApplicationContext());

            manager_image.imageManager(bitmap_image);

            stanza_image.setImageBitmap(manager_image.getImageBitmap());

        }

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getData()!=null) {
            Uri URIfilePath = data.getData();
            try {
                bitmap_image = MediaStore.Images.Media.getBitmap(getContentResolver(), URIfilePath);
                //imageManager(imageBitmap);

                flag_new_image = 1;

                manager_image = new ImageManager(getApplicationContext());

                manager_image.imageManager(bitmap_image);

                //Log.d("Immagine" , "il path dell' immmagine è : " + manager.getImagePath());


                stanza_image.setImageBitmap(manager_image.getImageBitmap());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }







    public void inizializzazione() {
        Intent intent = getIntent();
        //String home_id = intent.getStringExtra("home_id");
        String home_id = temp_persona.getIdHome();
        String nome_stanza = intent.getStringExtra("nome_stanza");

        flag_new_image = 0; //l'immagine non é stata cambiata

        edit_nome_stanza.setText(nome_stanza);

        Globals g = Globals.getInstance();
        URL url = null;
        try {
            url = new URL(g.getDomain() + "get_stanza_image.php?home_id=" + home_id + "&nome_stanza=" + nome_stanza);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        new TaskAsincrono(getApplicationContext(), url , new TaskCompleted() {
            @Override
            public void onTaskComplete(Object resp) {

                String result = getStringFromInputStream((InputStream) resp);

                String JSON_image_stanza = readJSON(result);
                Drawable image_drawable = null;

                if(JSON_image_stanza != null) {

                    //mettere immagine stanza DB

                    String[] path = JSON_image_stanza.split(" ");

                    if(path[0].equals("D")) {
                        image_drawable = getResources().getDrawable(Integer.parseInt(path[1]));
                    }else{

                        ImageManager manager = new ImageManager(getApplicationContext());
                        Bitmap image_bitmap = manager.loadImageFromStorage(path[1],path[2]);
                        image_drawable = new BitmapDrawable(getResources(), image_bitmap);

                    }



                    ImageView view_stanza_image = (ImageView) findViewById(R.id.aggiungi_stanza_profile_image);
                    view_stanza_image.setImageDrawable(image_drawable);


                }else{

                    //nessuna immagine presente

                }
                //readStream((String) resp);
            }
        }).execute();


    }

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

    public String readJSON (String jsonString) {

        String id=null;

        try {


            jsonString = "{\"info\":"+ jsonString + "}";
            Log.w("INFORMATION", jsonString);
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray info = jsonObj.getJSONArray("info");
            JSONObject c = info.getJSONObject(0);
            id = c.getString("stanza_image");

            //Log.w("INFORMAIONE" , image_stanza);

            //Bitmap immagine = stringToBitmap(image_stanza);



        }catch (Exception e){

        }

        return id;

    }




    /*@Override
    protected void onResume() {
        super.onResume();
        Log.d("AggiungiStanzaActivity" , "Sono nell'OnResume");
        Intent intent = getIntent();
        temp_persona = intent.getParcelableExtra("persona");
    }*/





}