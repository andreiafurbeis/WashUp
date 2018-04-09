package com.example.andrearubeis.wash_up;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.andrearubeis.wash_up.R;

public class bottom_activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar barra = getSupportActionBar();

        barra.hide();
        setContentView(R.layout.activity_bottom_activity);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        Intent intent;
                        Persona temp_persona;
                        Bundle args;
                        switch (item.getItemId()) {
                            case R.id.navigation_home:


                                selectedFragment = HomeFragmentActivity.newInstance();
                                intent = getIntent();
                                temp_persona = intent.getParcelableExtra("persona");
                                args = new Bundle();
                                args.putParcelableArrayList("stanze" ,temp_persona.getStanze());
                                args.putString("id" , temp_persona.getIdHome());
                                selectedFragment.setArguments(args);

                                break;


                            case R.id.navigation_options:


                                selectedFragment = OptionFragmentActivity.newInstance();
                                intent = getIntent();
                                args = new Bundle();
                                temp_persona = intent.getParcelableExtra("persona");
                                args.putString("id" , temp_persona.getIdHome());
                                args.putString("nome" , temp_persona.getNome());
                                args.putString("cognome" , temp_persona.getCognome());
                                args.putString("profile_image" , temp_persona.getProfileImage());
                                selectedFragment.setArguments(args);

                                break;


                            case R.id.navigation_ruoli:


                                selectedFragment = RuoliFragmentActivity.newInstance();


                                intent = getIntent();
                                args = new Bundle();
                                temp_persona = intent.getParcelableExtra("persona");
                                args.putParcelableArrayList("stanze" ,temp_persona.getStanze());
                                args.putString("id" , temp_persona.getIdHome());
                                selectedFragment.setArguments(args);

                                break;


                        }

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_activity_bottom, selectedFragment);
                        transaction.commit();
                        return true;

                    }
                });

        //Manually displaying the first fragment - one time only
        /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_activity_bottom, HomeFragmentActivity.newInstance());
        transaction.commit();*/


        Intent intent = getIntent();

        Bundle args = new Bundle();

        Persona temp_persona = intent.getParcelableExtra("persona");

        args.putParcelableArrayList("stanze" ,temp_persona.getStanze());

        args.putString("id" , temp_persona.getIdHome());

        HomeFragmentActivity fragment = new HomeFragmentActivity();
        fragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_activity_bottom, fragment);
        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }





}
