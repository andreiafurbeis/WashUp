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
    LinearLayout configurazione_linear;
    Bitmap imageBitmap;
    //HashMap<String,Object> data;
    String data;
    String stringaJSON;
    ArrayList<Stanza> vectorStanze;
    //URL url = null;
    Intent new_home_intent;
    Persona temp_persona;
    int i;
    Target target;
    String image;
    Drawable image_drawable = null;

    Button buttonStanza[];

    SharedPreferences pref;

    //String loveloBlack = "R.font.lovelo_black;


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
        configurazione_linear = (LinearLayout) findViewById(R.id.configurazione_linear);

        new_home_intent = getIntent();

        pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);

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

                Intent new_home_intent = getIntent();

                Globals g = Globals.getInstance();

                Log.w("INFORMATION" , "l'oggetto persona contiene : " + temp_persona.getNome() + " " + vectorStanze.toString());



                Intent intent = new Intent(ConfigurazioneStanzaActivity.this, bottom_activity.class);

                Log.w("INFORMATION" , "l'oggetto persona contiene : " + temp_persona.getNome() + " " + temp_persona.getStanze().get(0).getNameStanza() + " " + temp_persona.getStanze().get(0).getImageStanza());


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

    private void inizializationNewHome(){

        Globals g = Globals.getInstance();

        //INIZIALIZZAZIONE BAGNO
        //Stanza bagno = new Stanza( "D " + R.drawable.bagno , "bagno" , g.getIdString());
        Stanza bagno = new Stanza( g.getDomain()+"bagno.png", "bagno" , g.getIdString());

        //INIZIALIZZAZIONE CUCINA
        //Stanza cucina = new Stanza( "D " + R.drawable.cucina , "cucina" , g.getIdString());
        Stanza cucina = new Stanza( g.getDomain()+"cucina.png", "cucina" , g.getIdString());

        //INIZIALIZZAZIONE CAMERA

        //Stanza camera = new Stanza( "D " + R.drawable.camera , "camera" , g.getIdString());
        Stanza camera = new Stanza( g.getDomain()+"camera.png", "camera" , g.getIdString());


        data = getURLData(bagno , cucina , camera);


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



    public String getURLData (Stanza temp1 , Stanza temp2 , Stanza temp3) {

        //temp_persona = getIntent().getParcelableExtra("persona");
        String url_data = "?home_id="+temp_persona.getIdHome()+"&image1="+temp1.getImageStanza()+"&image2="+temp2.getImageStanza()+"&image3="+temp3.getImageStanza();
        return url_data;

    }



    public void inizializationInterface() {

        URL url=null;
        Globals g = Globals.getInstance();
        //temp_persona = getIntent().getParcelableExtra("persona");

        /*if(temp_persona.getCompitiStanza(1) != null) {
            Log.d("ConfigurazioneStanze", "Dentro InizializationInterface :  ci sono : " + temp_persona.getCompitiStanza(1).size() + "compiti in questa stanza ");
        }*/

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

                readJSON(result);

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





    public void readJSON (String jsonString ) {

        //new_home_intent = this.getIntent();
        //temp_persona = new_home_intent.getParcelableExtra("persona");

        /*if(temp_persona.getCompitiStanza(1) != null) {
            Log.d("ConfigurazioneStanze", "All'inizio di ReadJSON :  ci sono : " + temp_persona.getCompitiStanza(1).size() + "compiti in questa stanza ");
        }*/

        vectorStanze= new ArrayList<Stanza>();

        try {


            jsonString = "{\"stanze\":"+ jsonString + "}";
            Log.w("INFORMATION", jsonString);
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray stanze = jsonObj.getJSONArray("stanze");
            for(int i = 0; i< stanze.length(); i++) {
                JSONObject c = stanze.getJSONObject(i);



                //Log.w("INFORMAIONE" , image_stanza);



                Stanza stanza = new Stanza(c.getString("stanza_image"),c.getString("nome_stanza"));
                stanza.setCompiti(readJSONCompiti(c.getString("compiti")));
                vectorStanze.add(stanza);

            }

        }catch (Exception e){
            Log.d("ConfigurazioneStanze" , "Eccezione catturata nel ReadJSON");
        }

        Globals g = Globals.getInstance();
        g.setStanze(vectorStanze);

        temp_persona.setStanze(vectorStanze);

        /*if(temp_persona.getCompitiStanza(1) != null) {
            Log.d("ConfigurazioneStanze", "readJson : ci sono : " + temp_persona.getCompitiStanza(1).size() + "compiti in questa stanza ");
        }*/

        creaInterfaccia();


    }


    public ArrayList<Compito> readJSONCompiti(String jsonString) {
        /*if(temp_persona.getCompitiStanza(1) != null) {
            Log.d("ConfigurazioneStanze", "All'inizio di ReadJSON :  ci sono : " + temp_persona.getCompitiStanza(1).size() + "compiti in questa stanza ");
        }*/

        ArrayList <Compito> vectorCompiti= new ArrayList<Compito>();


        try {


            jsonString = "{\"compiti\":"+ jsonString + "}";
            //jsonString = "{" + jsonString + "}";
            Log.w("INFORMATION", jsonString);
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray compiti = jsonObj.getJSONArray("compiti");
            for(int i = 0; i< compiti.length(); i++) {
                JSONObject c = compiti.getJSONObject(i);



                //Log.w("INFORMAIONE" , image_stanza);



                //Stanza stanza = new Stanza(c.getString("stanza_image"),c.getString("nome_stanza"));
                Compito compito = new Compito(c.getString("id_compito") , c.getString("descrizione") , c.getString("stanza") , c.getString("id_casa"));
                vectorCompiti.add(compito);

            }

        }catch (Exception e){
            Log.d("ConfigurazioneStanze" , "Eccezione catturata nel ReadJSONCompiti");
        }

        Globals g = Globals.getInstance();
        //g.setStanze(vectorStanze);

        //temp_persona.setStanze(vectorStanze);

        /*if(temp_persona.getCompitiStanza(1) != null) {
            Log.d("ConfigurazioneStanze", "readJson : ci sono : " + temp_persona.getCompitiStanza(1).size() + "compiti in questa stanza ");
        }*/

        return vectorCompiti;

    }



    //CREAZIONE DI TUTTI I BUTTON IN MODO DINAMICO , CON ANCHE L'ASSEGNAMENTO DEGLI ONCLICK
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





        if(vectorStanze != null ) {
            Log.d("ConfigurazioneStanze" , "Il vector ha : " + vectorStanze.size() + " elementi");
        }else{
            Log.d("ConfigurazioneStanze" , "Il vector é NULL");
        }


        AdapterStanze adapter = new AdapterStanze(getApplicationContext() , vectorStanze);


        if(adapter == null ) {
            Log.d("ConfigurazioneStanze" , "L'adapter é NULL");
        }

        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();








        /*

        buttonStanza = new  Button[vectorStanze.size()];






        for(i=0; i < vectorStanze.size(); i++){

            buttonStanza[i] = new Button(getApplicationContext());
            final Stanza stanzaCorrente = vectorStanze.get(i);
            buttonStanza[i].setText(stanzaCorrente.getNameStanza());
            //BitmapDrawable drawableCorrente = new BitmapDrawable(getApplicationContext().getResources(),stanzaCorrente.getImage());
            image = stanzaCorrente.getImageStanza();
            String [] image_path = image.split(" ");

            Log.d("immagine","La stringa dell'immagine é : " + image);

            if(image_path[0].equals("D")){

                image_drawable = getResources().getDrawable(Integer.parseInt(image_path[1]));

            }else{

                Toast.makeText(getApplicationContext(),"Sto mettendo l'immagine con il metodo nuovo" ,Toast.LENGTH_SHORT).show();
                ImageManager manager = new ImageManager(getApplicationContext());
                Bitmap image_bitmap = manager.loadImageFromStorage(image_path[1],image_path[2]);
                //Bitmap image_bitmap_scaled = Bitmap.createScaledBitmap(image_bitmap , ViewGroup.LayoutParams.MATCH_PARENT , getResources().getDisplayMetrics().heightPixels/6,true);
                image_drawable = new BitmapDrawable(getResources() , image_bitmap);

            }



            buttonStanza[i].setBackground(image_drawable);

            Log.d("ConfigStanze","Dopo Picasso");

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getResources().getDisplayMetrics().heightPixels/6);



            //params.height = 50;
            //buttonStanza[i].setLayoutParams(params);

            //buttonStanza[i].setHeight(mGestureThreshold);
            buttonStanza[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Globals g = Globals.getInstance();
                    Intent intent = new Intent(ConfigurazioneStanzaActivity.this,
                            AggiungiStanzaActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putString("nome_stanza",stanzaCorrente.getNameStanza());
                    bundle.putString("home_id",g.getIdString());
                    bundle.putInt("update",1);
                    //bundle.putParcelable("persona" , temp_persona);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            });

            //buttonStanza[i].setTypeface(Typeface.createFromAsset(getAssets(),loveloBlack));
            configurazione_linear.addView(buttonStanza[i],params);



        }*/

    }





}
