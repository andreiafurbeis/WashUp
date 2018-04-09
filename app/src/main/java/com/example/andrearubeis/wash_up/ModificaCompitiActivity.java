package com.example.andrearubeis.wash_up;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ModificaCompitiActivity extends AppCompatActivity{

    Button title_bar;
    Button button_aggiungi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_compiti);
        button_aggiungi = (Button) findViewById(R.id.modifica_compiti_button_add);

        title_bar = (Button) findViewById(R.id.modifica_compiti_title_bar);

        ActionBar barra = getSupportActionBar();
        barra.hide();
        Intent intent = getIntent();
        //String nome_stanza = intent.getStringExtra("nome_stanza");
        Stanza temp = new Stanza (null, intent.getStringExtra("nome_stanza") ,null);

        title_bar.setText(title_bar.getText() + " " + temp.getNameStanza());



        button_aggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




    }


    @Override
    protected void onStart() {
            super.onStart();

    }

}


