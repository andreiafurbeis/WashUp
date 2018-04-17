package com.example.andrearubeis.wash_up;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.tooltip.Tooltip;

public class ConfigurazioneStanzaTutorial extends AppCompatActivity {


    Button aggiungi_stanza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurazione_stanza_tutorial);

        //unica azione possibile dell'activity: premere '+' e creare una nuova stanza ------> passo all'activity AggiungiStanzaTutorial

        aggiungi_stanza = (Button) findViewById(R.id.configurazione_tutorial_button_add);
        Tooltip tooltip_aggiungi_stanza  = new Tooltip.Builder(aggiungi_stanza)
                .setText("Clicca qui per aggiungere una nuova stanza")
                .setBackgroundColor(Color.parseColor("#ff669900"))
                .setTextColor(Color.WHITE)


                .setGravity(Gravity.TOP)
                .show();
        aggiungi_stanza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfigurazioneStanzaTutorial.this, AggiungiStanzaTutorial.class);
                startActivity(intent);
            }
        });

    }
}
