package com.example.andrearubeis.wash_up;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.andrearubeis.wash_up.R;
import com.google.gson.Gson;

public class bottom_activity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    SharedPreferences pref;
    Persona temp_persona;
    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar barra = getSupportActionBar();

        barra.hide();
        setContentView(R.layout.activity_bottom_activity);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);




        //Intent intent = getIntent();

        Bundle args = new Bundle();

        //Persona temp_persona = intent.getParcelableExtra("persona");

        pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = pref.getString("persona", "");
        temp_persona = gson.fromJson(json, Persona.class);

        if(temp_persona == null) {
            Log.d("ConfigurazioneStanze" , "L'oggetto appena scaricato dalle SharedPreference é NULL");
        }

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


    @Override
    protected void onStart() {
        super.onStart();
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        //Intent intent;
                        //Persona temp_persona;
                        Bundle args;
                        switch (item.getItemId()) {
                            case R.id.navigation_home:


                                selectedFragment = HomeFragmentActivity.newInstance();
                                //intent = getIntent();
                                //temp_persona = intent.getParcelableExtra("persona");
                                args = new Bundle();
                                args.putParcelableArrayList("stanze" ,temp_persona.getStanze());
                                args.putString("id" , temp_persona.getIdHome());
                                selectedFragment.setArguments(args);

                                break;


                            case R.id.navigation_options:


                                selectedFragment = OptionFragmentActivity.newInstance();
                                //intent = getIntent();
                                args = new Bundle();
                                //temp_persona = intent.getParcelableExtra("persona");
                                args.putString("id" , temp_persona.getIdHome());
                                args.putString("nome" , temp_persona.getNome());
                                args.putString("cognome" , temp_persona.getCognome());
                                args.putString("profile_image" , temp_persona.getProfileImage());
                                selectedFragment.setArguments(args);

                                break;


                            case R.id.navigation_ruoli:


                                selectedFragment = RuoliFragmentActivity.newInstance();


                                //intent = getIntent();
                                args = new Bundle();
                                //temp_persona = intent.getParcelableExtra("persona");
                                args.putParcelableArrayList("stanze" ,temp_persona.getStanze());
                                args.putString("id" , temp_persona.getIdHome());
                                //args.putParcelable("persona", temp_persona);
                                selectedFragment.setArguments(args);

                                break;


                        }

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_activity_bottom, selectedFragment);
                        transaction.commit();
                        return true;

                    }
                });
    }

    @Override
    public void onBackPressed() {
        logOut();
    }

    public void logOut() {

        final CharSequence[] items = { "si",  selectedFragment.getContext().getString(R.string.annulla) };
        AlertDialog.Builder builder = new AlertDialog.Builder(selectedFragment.getContext());
        builder.setTitle("Sei sicuro di voler tornare al login ?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if(items[item].equals("si")) {

                    //Toast.makeText(context, "hai cliccato si, ora ti mando al login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(selectedFragment.getContext(), MainActivity.class);
                    startActivity(intent);

                }



                if (items[item].equals(selectedFragment.getContext().getString(R.string.annulla))) {
                    dialog.dismiss();
                    //Toast.makeText(context, "hai cliccato annulla, torna sul fragment option", Toast.LENGTH_SHORT).show();


                }
                //Toast.makeText(getApplicationContext(), "hai cliccato su un elemento del dialog", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

}
