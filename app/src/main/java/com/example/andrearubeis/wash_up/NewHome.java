package com.example.andrearubeis.wash_up;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.andrearubeis.wash_up.R;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewHome extends AppCompatActivity {



    private Button continua;

    private String id_home;
    Intent main_activity_intent;
    Persona temp_persona;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home);
        ActionBar barra = getSupportActionBar();
        barra.hide();




        continua = (Button) findViewById(R.id.new_home_button_continua);
        continua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    main_activity_intent = getIntent();
                    temp_persona = main_activity_intent.getParcelableExtra("persona");

                    creationNewHome();
                    Thread.sleep(500);
                    Globals g = Globals.getInstance();
                    g.setIdString(id_home);
                    temp_persona.setIdHome(id_home);
                    Toast.makeText(getApplicationContext(), id_home, Toast.LENGTH_SHORT).show();

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("persona" , temp_persona);
                    if(temp_persona == null ) {
                        Log.d("NewHome" , "L'oggetto Persona é NULL");
                    }
                    Intent intent = new Intent(NewHome.this, ConfigurazioneStanzaActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                catch(Exception e) {
                    Log.d("NewHome" , "Eccezione catturata nell'OnCreate");
                }

            }
        });


    }

    private void creationNewHome() {

        AsyncTask<Integer, Void, Void> asyncTask = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... movieIds) {

                OkHttpClient client = new OkHttpClient();
                Globals g = Globals.getInstance();
                Request request = new Request.Builder()
                        .url("http://washit.dek4.net/new_home_creation.php?usr=" + g.getMail())
                        .build();
                try {
                    Response response = client.newCall(request).execute();


                    NewHome.this.id_home = response.body().string();


                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

            }
        };

        asyncTask.execute();
    }
}
