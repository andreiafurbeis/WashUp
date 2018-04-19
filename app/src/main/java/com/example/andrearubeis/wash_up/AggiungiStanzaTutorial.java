package com.example.andrearubeis.wash_up;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.tooltip.Tooltip;

public class AggiungiStanzaTutorial extends AppCompatActivity {

    Button gestisci_compiti;
    Button skip;
    EditText stanza;
    String nome_stanza = "";
    Persona temp_persona;
    SharedPreferences pref;
    JSONReader reader_json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_stanza_tutorial);
        //Nascondi AppBar
        ActionBar barra = getSupportActionBar();
        barra.hide();



        //SharedPreferences
        pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString("persona", "");
        temp_persona = gson.fromJson(json, Persona.class);



        stanza = (EditText) findViewById(R.id.aggiungi_stanza_tutorial_nome_stanza);



        gestisci_compiti = (Button) findViewById(R.id.aggiungi_stanza_tutorial_button_compiti);


        Tooltip tooltip_modifica_compiti  = new Tooltip.Builder(gestisci_compiti)
                .setText(" Assegna un nome alla stanza e poi clicca qui per aggiungere una lista di compiti da svolgere")
                .setBackgroundColor(Color.parseColor("#ff669900"))
                .setTextColor(Color.WHITE)


                .setGravity(Gravity.BOTTOM)
                .show();

        gestisci_compiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent_compiti = new Intent(AggiungiStanzaTutorial.this,
                        ModificaCompitiTutorial.class);
                Bundle bundle = new Bundle();
                String nome_stanza = stanza.getText().toString();
                bundle.putString("nome_stanza" , nome_stanza);

                intent_compiti.putExtras(bundle);
                if(temp_persona == null) {
                    Log.d("AggiungiStanza" , "La persona é NULL" );
                }
                startActivity(intent_compiti);
                finish();

            }
        });







    }
}
