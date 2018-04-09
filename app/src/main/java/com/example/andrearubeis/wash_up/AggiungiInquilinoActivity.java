package com.example.andrearubeis.wash_up;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AggiungiInquilinoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_inquilino);

        //nasconde actionbar
        ActionBar barra = getSupportActionBar();
        barra.hide();



    }
}
