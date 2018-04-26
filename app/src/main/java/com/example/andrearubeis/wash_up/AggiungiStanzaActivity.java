package com.example.andrearubeis.wash_up;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
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
import com.squareup.picasso.Picasso;

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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AggiungiStanzaActivity extends AppCompatActivity {


    ImageView stanza_image;


    ImageView view_stanza_image;
    Button title_bar;
    Button aggiungi;
    Button gestisci_compiti;
    EditText edit_nome_stanza;
    final Activity call = this;
    int flag_new_image;
    Button elimina_stanza;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE_REQUEST = 2;
    Intent intent;
    public ImageManager manager_image;
    String filePath;
    String fileName;
    private URL url;
    String image_real_path;
    Persona temp_persona;
    SharedPreferences pref;
    JSONReader reader_json;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_stanza);
        ActionBar barra = getSupportActionBar();
        barra.hide();

        elimina_stanza = (Button) findViewById(R.id.aggiungi_stanza_elimina_stanza);
        title_bar = (Button) findViewById(R.id.aggiungi_stanza_title_bar);
        stanza_image = (ImageView) findViewById(R.id.aggiungi_stanza_profile_image);
        aggiungi = (Button) findViewById(R.id.aggiungi_stanza_button_aggiungi);
        edit_nome_stanza = (EditText)findViewById(R.id.aggiungi_stanza_nome_stanza);
        gestisci_compiti = (Button) findViewById(R.id.aggiungi_stanza_button_compiti);
        view_stanza_image = (ImageView) findViewById(R.id.aggiungi_stanza_profile_image);

        reader_json = new JSONReader(getApplicationContext());

        intent = getIntent();


        //SharedPreferences
        pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString("persona", "");
        temp_persona = gson.fromJson(json, Persona.class);

        if(intent.hasExtra("update")) {
            aggiungi.setText("Modifica");
            title_bar.setText("Modifica Stanza");
            inizializzazione(); //INIZIALIZZA L'ACTIVITY CON TUTTE LE INFORMAZIONI DELLA STANZA PRECEDENTEMENTE CREATA
        }else{
            title_bar.setText("Aggiungi Stanza");
            elimina_stanza.setVisibility(View.GONE);
            gestisci_compiti.setVisibility(View.GONE);
        }


        // elimina_stanza una volta cliccato chiama in fondo a qesta classe removeStanzaFromDB(Stanza s)
        elimina_stanza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //creo una Stanza temp da usare come input di removeStanzaFromDB(Stanza s)
                Stanza stanza_temp = new Stanza(null,intent.getStringExtra("nome_stanza"),intent.getStringExtra("id_casa"));
                removeStanza(stanza_temp);

            }
        });




        if(intent.hasExtra("indice_stanza")) {
            int indice = intent.getIntExtra("indice_stanza", -2);
            /*if(temp_persona.getCompitiStanza(indice) == null) {
                Log.d("AggiungiStanza", "Il vettore compiti é NULL e l'indice é : " + indice);

            }*/
        }









        aggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                aggiungiStanza();

            }
        });



        stanza_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImageChooser image_chooser = new ImageChooser(AggiungiStanzaActivity.this , call);
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

                intent_compiti.putExtras(bundle);
                /*if(temp_persona == null) {
                    Log.d("AggiungiStanza" , "La persona é NULL" );
                }*/
                startActivity(intent_compiti);
                finish();

            }
        });

    }



    public void aggiungiStanza() {

        Globals g = Globals.getInstance();

        if(intent.hasExtra("update")) {

            //MODALITÁ DI MODIFICA , IN QUESTO CASO LA STANZA É GIÁ STATA CREATA , MA VUOLE ESSERE MODIFICATA
            Stanza temp = new Stanza(null,intent.getStringExtra("nome_stanza"),temp_persona.getIdHome());
            String temp_url = g.getDomain() + "update_room.php?home_id=" + temp.getIdCasa() + "&nuovo_nome_stanza=" + edit_nome_stanza.getText() + "&nome_stanza=" + temp.getNameStanza() + "";

            if (flag_new_image == 1) {

                filePath = manager_image.getImagePath();
                fileName = manager_image.getImageName();
                temp_url = temp_url + "&image_stanza=P " + g.getDomain() + fileName;
                uploadImage();

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

                        String result = reader_json.getStringFromInputStream((InputStream) resp);

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

            Stanza temp = new Stanza(null,null,temp_persona.getIdHome());

            if(flag_new_image == 1) {

                filePath = manager_image.getImagePath();
                fileName = manager_image.getImageName();
                String temp_url = g.getDomain() + "update_room.php?home_id=" + temp.getIdCasa() + "&nuovo_nome_stanza=" + edit_nome_stanza.getText() + "&image_stanza=" + g.getDomain() +  fileName;
                uploadImage();

                try {

                    url = new URL(temp_url);

                } catch (IOException e) {

                    Toast.makeText(getApplicationContext(), "Creazione URL non riuscita", Toast.LENGTH_SHORT).show();

                }

                try {

                    new TaskAsincrono(getApplicationContext(), url , new TaskCompleted() {
                        @Override
                        public void onTaskComplete(Object resp) {

                            String result = reader_json.getStringFromInputStream((InputStream) resp);
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

    private void removeStanza(final Stanza temp_stanza) {
        final CharSequence[] items = { "Si",  getApplicationContext().getString(R.string.annulla) };
        AlertDialog.Builder builder = new AlertDialog.Builder(AggiungiStanzaActivity.this);
        builder.setTitle("Sei sicuro di voler eliminare la stanza : " + temp_stanza.getNameStanza() + " ?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if(items[item].equals("Si")) {

                    removeStanzaFromDB(temp_stanza);
                    onBackPressed();

                }



                if (items[item].equals(getApplicationContext().getString(R.string.annulla))) {
                    dialog.dismiss();


                }
            }
        });
        builder.show();
    }


    private void uploadImage() {




        ApiService service = RetroClient.getApiService();



        String imageString = manager_image.getImagePath()+"/"+manager_image.getImageName();

        //Log.d("AggiungiStanzaUpload" , "il path dell' immagine è : " + imageString);

        File file = new File(imageString);


        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);

        Call<Result> resultCall = service.uploadImage(body);
        //Log.d("MainActivity","Sto provando a fare l'upload");
        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {



                /*if (response.isSuccessful()) {
                    if (response.body().getResult().equals("success")) {
                        Log.d("RegistrazioneUpload", "Upload Success , il path sul server è : " + response.body().getValue());

                    }else {
                        Log.d("RegistrazioneUpload", "Upload Fail");

                    }
                } else {
                    Log.d("RegistrazioneUpload" , "Upload Fail");

                }*/



            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }



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
                flag_new_image = 1;
                manager_image = new ImageManager(getApplicationContext());
                manager_image.imageManager(bitmap_image);
                stanza_image.setImageBitmap(manager_image.getImageBitmap());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }







    public void inizializzazione() {

        Intent intent = getIntent();
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

                String result = reader_json.getStringFromInputStream((InputStream) resp);

                String JSON_image_stanza = reader_json.readJSONStanzaImage(result);

                if(JSON_image_stanza != null) {

                    Picasso.get().load(JSON_image_stanza).fit().centerCrop().into(view_stanza_image,new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }

                    });


                }else{

                    //nessuna immagine presente

                }

            }
        }).execute();


    }


    private void removeStanzaFromDB(Stanza stanza_da_rimuovere) {
        Globals g = Globals.getInstance();
        String temp_url = g.getDomain()+"remove_stanza.php?id_home=" + g.getIdString() + "&nome_stanza=" + stanza_da_rimuovere.getNameStanza();
        //Log.d("AggiungiStanza","L'URL é : " + temp_url);
        URL url = null;
        try {
            url = new URL(temp_url);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Creazione URL non riuscita", Toast.LENGTH_SHORT).show();
        }

        new TaskAsincrono(getApplicationContext(), url  , new TaskCompleted() {
            @Override
            public void onTaskComplete(Object resp) {


                Toast.makeText(getApplicationContext(), "Stanza eliminata con successo", Toast.LENGTH_SHORT).show();


            }
        }).execute();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AggiungiStanzaActivity.this , ConfigurazioneStanzaActivity.class);
        startActivity(intent);
        finish();
    }
}