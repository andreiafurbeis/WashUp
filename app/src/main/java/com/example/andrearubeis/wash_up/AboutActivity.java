package com.example.andrearubeis.wash_up;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurazione_stanza_tutorial);

        //nascondo la barra dell'app

        ActionBar barra = getSupportActionBar();
        barra.hide();



    }
}
