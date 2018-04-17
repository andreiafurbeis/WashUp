package com.example.andrearubeis.wash_up;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tooltip.Tooltip;

public class AggiungiStanzaTutorial extends AppCompatActivity {

    Button modifica_compiti;
    Button skip;
    EditText stanza;
    String nome_stanza = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_stanza_tutorial);
        //Nascondi AppBar
        ActionBar barra = getSupportActionBar();
        barra.hide();


        stanza = (EditText) findViewById(R.id.aggiungi_stanza_tutorial_nome_stanza);



        modifica_compiti = (Button) findViewById(R.id.aggiungi_stanza_tutorial_button_compiti);


        Tooltip tooltip_modifica_compiti  = new Tooltip.Builder(modifica_compiti)
                .setText(" Assegna un nome alla stanza e poi clicca qui per aggiungere una lista di compiti da svolgere")
                .setBackgroundColor(Color.parseColor("#ff669900"))
                .setTextColor(Color.WHITE)


                .setGravity(Gravity.BOTTOM)
                .show();

        modifica_compiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AggiungiStanzaTutorial.this, ModificaCompitiTutorial.class);
                Bundle args = new Bundle();
                nome_stanza = stanza.getText().toString();
                Log.d("AggiungiStanzaTutorial","il nome della stanza e' : " + nome_stanza);
                args.putString("nome",nome_stanza);
                intent.putExtras(args);
                startActivity(intent);
            }

        });







    }
}
