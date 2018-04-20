package com.example.andrearubeis.wash_up;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AttendiInvitoActivity extends AppCompatActivity {



    Button log_in;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendi_invito);
        ActionBar barra = getSupportActionBar();
        barra.hide();

        log_in = findViewById(R.id.attendi_invito_log_in);
        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendiInvitoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });



    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AttendiInvitoActivity.this, ConfigurazioneStanzaActivity.class);
        startActivity(intent);
    }
}
