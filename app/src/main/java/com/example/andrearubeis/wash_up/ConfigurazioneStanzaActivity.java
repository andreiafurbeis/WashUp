package com.example.andrearubeis.wash_up;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.andrearubeis.wash_up.R;

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
    LinearLayout configurazione_linear;
    Bitmap imageBitmap;
    HashMap<String,Object> data;
    String stringaJSON;
    ArrayList<Stanza> vectorStanze;
    //URL url = null;
    Intent new_home_intent;
    Persona temp_persona;

    Button buttonStanza[];

    //String loveloBlack = "R.font.lovelo_black;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurazione_stanze);


        //Nascondi AppBar
        ActionBar barra = getSupportActionBar();
        barra.hide();

        add = (Button) findViewById(R.id.configurazione_button_add);
        continua = (Button) findViewById(R.id.configurazione_button_continua);
        configurazione_linear = (LinearLayout) findViewById(R.id.configurazione_linear);

        new_home_intent = getIntent();

        temp_persona = new_home_intent.getParcelableExtra("persona");



        //INIZIALIZZAZIONE DATABASE CON STANZE STANDARD
        inizializationNewHome();


        Log.w("INFORMATION" , "sto per entrare nella configurazione della nuova classe");
        //INIZIALIZZAZIONE INTERFACCIA DINAMICA
        inizializationInterface();

        Globals g = Globals.getInstance();
        g.setInfoUtente(temp_persona);


        //Log.w("INFORMATION" , "l'oggetto persona contiene : " + temp_persona.getNome() + " " + g.getStanze().toString());
       // Log.w("INFORMATION" , "l'oggetto persona contiene : " + temp_persona.getNome() + " " + temp_persona.getStanze().toString());


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Globals g = Globals.getInstance();
                Intent intent = new Intent(ConfigurazioneStanzaActivity.this,
                        AggiungiStanzaActivity.class);
                intent.putExtra("home_id",g.getIdString());
                startActivity(intent);
            }
        });
        continua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(ConfigurazioneStanzaActivity.this,
                        bottom_activity.class);
                startActivity(intent);*/

                Globals g = Globals.getInstance();
                temp_persona = g.getInfoUtente();

                Log.w("INFORMATION" , "l'oggetto persona contiene : " + temp_persona.getNome() + " " + vectorStanze.toString());

                LoadHome home = new LoadHome(temp_persona.getIdHome(), g.getDomain() , getApplicationContext());
                home.setStanze(vectorStanze);
                temp_persona.setStanze(vectorStanze);
                //g.setStanze(home.getVectorStanze());



                Bundle bundle = new Bundle();
                Intent intent = new Intent(ConfigurazioneStanzaActivity.this, bottom_activity.class);
                //bundle.putString("id" , temp_persona.getIdHome());
                bundle.putParcelable("persona" , temp_persona);

                Log.w("INFORMATION" , "l'oggetto persona contiene : " + temp_persona.getNome() + " " + temp_persona.getStanze().get(0).getNameStanza() + " " + temp_persona.getStanze().get(0).getImageStanza());

                intent.putExtras(bundle);

                //intent.putExtra("stanze" , parts[2]);


                //intent.putParcelableArrayListExtra("stanze" , home.getStanze());

                if(home.getStanze() == null) {
                    Toast.makeText(getApplicationContext(),"ConfigurazioneStanze : il vettore è NULL ",Toast.LENGTH_SHORT).show();
                    Log.d("ConfigurazioneStanze" , "Il vettore tornato è NULL");
                }else{
                    Log.d("ConfigurazioneStanze" , "Il vettore tornato è : " + home.getStanze().toString());
                }

                startActivity(intent);





            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("immagine_back" , "Sono nell'OnResume");
        //inizializationInterface();
    }

    private void inizializationNewHome(){
        class inizializationNewHome extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ConfigurazioneStanzaActivity.this, "Creazione Stanze Base", "Attendi...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
            }

            @Override
            protected String doInBackground(Bitmap... params) {

                Globals g = Globals.getInstance();
                String result = rh.sendPostRequest(g.getDomain()+"starting_rooms.php",data);


                return result;
            }
        }

        Globals g = Globals.getInstance();

        inizializationNewHome ui = null;

        Stanza temp = new Stanza( "D " + R.drawable.bagno , "bagno" , g.getIdString());

        //inizializzazione bagno

        data = setHashMap(temp);




        ui = new inizializationNewHome();

        try {
            ui.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }




        //inizializzazione cucina

        temp = new Stanza( "D " + R.drawable.cucina , "cucina" , g.getIdString());

        data = setHashMap(temp);



        ui = new inizializationNewHome();

        try {
            ui.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



        //INIZIALIZZAZIONE CAMERA

        temp = new Stanza( "D " + R.drawable.camera , "camera" , g.getIdString());



        data = setHashMap(temp);

        ui = new inizializationNewHome();
        try {
            ui.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    public HashMap setHashMap (Stanza temp) {
        HashMap data = new HashMap<>();
        data.put("nome_stanza",temp.getNameStanza());
        data.put("image",temp.getImageStanza());
        data.put("home_id",temp.getIdCasa());
        return data;
    }



    public void inizializationInterface() {

        URL url=null;
        Globals g = Globals.getInstance();

        try {
            url = new URL(g.getDomain() + "get_stanze.php?home_id="+g.getIdString());
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
                vectorStanze.add(stanza);

            }

        }catch (Exception e){
            Log.d("ConfigurazioneStanze" , "Eccezione catturata nel ReadJSON");
        }

        Globals g = Globals.getInstance();
        g.setStanze(vectorStanze);

        creaInterfaccia();


    }



    //CREAZIONE DI TUTTI I BUTTON IN MODO DINAMICO , CON ANCHE L'ASSEGNAMENTO DEGLI ONCLICK
    public void creaInterfaccia(){

        configurazione_linear = (LinearLayout) findViewById(R.id.configurazione_linear);
        if(configurazione_linear.getChildCount() > 0) {
            configurazione_linear.removeAllViews();
        }
        buttonStanza = new  Button[vectorStanze.size()];
        Drawable image_drawable = null;



        for( int i=0; i < vectorStanze.size(); i++){
            buttonStanza[i] = new Button(getApplicationContext());
            final Stanza stanzaCorrente = vectorStanze.get(i);
            buttonStanza[i].setText(stanzaCorrente.getNameStanza());
            //BitmapDrawable drawableCorrente = new BitmapDrawable(getApplicationContext().getResources(),stanzaCorrente.getImage());
            String image = stanzaCorrente.getImageStanza();
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


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getResources().getDisplayMetrics().heightPixels/6);



            //params.height = 50;
            //buttonStanza[i].setLayoutParams(params);

            //buttonStanza[i].setHeight(mGestureThreshold);
            buttonStanza[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Globals g = Globals.getInstance();
                    Intent intent = new Intent(ConfigurazioneStanzaActivity.this,
                            AggiungiStanzaActivity.class);
                    intent.putExtra("nome_stanza",stanzaCorrente.getNameStanza());
                    intent.putExtra("home_id",g.getIdString());
                    intent.putExtra("update",1);
                    startActivity(intent);
                }
            });

            //buttonStanza[i].setTypeface(Typeface.createFromAsset(getAssets(),loveloBlack));
            configurazione_linear.addView(buttonStanza[i],params);

        }

    }





}
