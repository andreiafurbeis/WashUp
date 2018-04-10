package com.example.andrearubeis.wash_up;

import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class AggiungiInquilinoActivity extends AppCompatActivity {

    Button aggiungi_inquilino;
    EditText mail_new_inquilino;
    Persona temp_persona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_inquilino);

        aggiungi_inquilino = findViewById(R.id.aggiungi_inquilino_button_aggiungi);
        mail_new_inquilino  = findViewById(R.id.aggiungi_inquilino_plain_text_mail);
        temp_persona = getIntent().getParcelableExtra("persona");


        aggiungi_inquilino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateHomeNewInquilino(mail_new_inquilino.getText().toString() , temp_persona.getIdHome());

            }
        });

        //nasconde actionbar
        ActionBar barra = getSupportActionBar();
        barra.hide();



    }


    private void updateHomeNewInquilino(String mail , String id_home) {
        URL url=null;
        Globals g = Globals.getInstance();

        String temp_url = g.getDomain()+"add_new_inquilino.php";
        try {
            url = new URL(temp_url+"?mail="+mail+"&id_home="+id_home);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Creazione URL non riuscita", Toast.LENGTH_SHORT).show();
        }


        try {
            new TaskAsincrono(getApplicationContext(), url, new TaskCompleted() {
                @Override
                public void onTaskComplete(Object resp) {

                    String result = getStringFromInputStream((InputStream) resp);

                    if(result.equals("0")) {
                        Toast.makeText(getApplicationContext() , "Errore durante l'aggiunta del nuovo inquilino" , Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(getApplicationContext() , "Nuovo inquilino aggiunto con successo" , Toast.LENGTH_SHORT).show();
                    }

                    onBackPressed();

                }
            }).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


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

}
