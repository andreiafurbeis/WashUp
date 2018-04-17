package com.example.andrearubeis.wash_up;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ModificaCompitiTutorial extends AppCompatActivity {

    Button title_bar;
    Button button_aggiungi;
    LinearLayout linear;
    Persona temp_persona;
    ListView listview;
    Stanza temp;
    ArrayList<Compito> compiti_global;
    int indice_stanza;
    ArrayList<Compito> compiti=null;


    SharedPreferences pref;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_compiti_tutorial);
        button_aggiungi = (Button) findViewById(R.id.modifica_compiti_tutorial_button_add);

        title_bar = (Button) findViewById(R.id.modifica_compiti_tutorial_title_bar);
        linear = findViewById(R.id.modifica_compiti_tutorial_linear);

        listview = (ListView) findViewById(R.id.modifica_compiti_tutorial_listview);



        ActionBar barra = getSupportActionBar();
        barra.hide();
        Intent intent = getIntent();
        //String nome_stanza = intent.getStringExtra("nome_stanza");
        temp = new Stanza (null, intent.getStringExtra("nome") ,null);

        //temp_persona = getIntent().getParcelableExtra("persona");

        pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = pref.getString("persona", "");
        temp_persona = gson.fromJson(json, Persona.class);

        if(temp_persona == null) {
            Log.d("ConfigurazioneStanze" , "L'oggetto appena scaricato dalle SharedPreference é NULL");
        }

        title_bar.setText(title_bar.getText() + " " + temp.getNameStanza());
        indice_stanza = getIndiceStanza(temp_persona.getStanze() , getIntent().getStringExtra("nome_stanza"));

        compiti = temp_persona.getCompitiStanza(indice_stanza);
        addCompiti(compiti,temp_persona);

        button_aggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();


            }
        });




    }


    @Override
    protected void onStart() {
        super.onStart();

    }


    private void createDialog() {

        Log.d("RuoliFragment" , "Sono denro alla crezione della Dialog");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View dialogView = inflater.inflate(R.layout.aggiungi_compito_dialog , null);
        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.aggiungi, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // aggiungi compito
                        EditText descrizione = dialogView.findViewById(R.id.compiti_dialog_descrizione);
                        String testo = descrizione.getText().toString();
                        Log.d("ModificaCompiti" , "La decrizione é : " + testo);

                        if(temp_persona == null) {
                            Log.d("ModificaCompiti" , "La persona é NULL" );

                        }


                        //Log.d("ModificaCompiti" , "La persona si chiama " + temp_persona.getNome() )
                        Compito compito = new Compito(descrizione.getText().toString(),temp.getNameStanza(),temp_persona.getIdHome());

                        //ArrayList<Compito> compiti = temp_persona.getCompitiStanza(indice_stanza);
                        if(compiti == null) {
                            Log.d("ModificaCompiti" , "Crezione nuovo ArrayList indice stanza = " + indice_stanza );

                            compiti = new ArrayList<Compito>();
                        }
                        compiti.add(compito);
                        //temp_persona.setCompiti(compiti);
                        //temp_persona.setCompitiStanza(indice_stanza,compiti);
                        addCompiti(compiti,temp_persona);
                    }
                })
                .setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



   /* public void onBackPressed() {
        //super.onBackPressed();

        //Aggiornare DataBase
        updateInfoCompiti(compiti_global);
        Intent intent = new Intent(ModificaCompitiActivity.this , AggiungiStanzaActivity.class);
        Bundle bundle = new Bundle();
        //bundle.putParcelable("persona" , temp_persona);

        SharedPreferences.Editor editor = pref.edit();

        Gson gson = new Gson();
        String json = gson.toJson(temp_persona);
        editor.putString("persona", json);


        // Save the changes in SharedPreferences
        editor.commit();




        bundle.putParcelableArrayList("compiti" , compiti_global);
        if(compiti_global == null) {
            Log.d("ModificaCompiti" , "I compiti sono NULL");
        }else{
            //Log.d("ModificaCompiti" , "Array compiti ha dimensione : " + temp_persona.getStanze().get(indice_stanza).getCompiti().size());
        }
        bundle.putString("nome_stanza" , temp.getNameStanza());
        bundle.putInt("update",1);
        bundle.putInt("indice_stanza",indice_stanza);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }*/



    private void addCompiti(ArrayList<Compito> compiti , Persona utente) {
        Log.d("ModificaCompiti" , "imposto nuovo Adapter" );

        AdapterCompiti adapter = new AdapterCompiti(this , compiti );
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();





        //Aggiornare dati locali

        compiti_global = compiti;
        temp_persona = utente;
    }


    private String ArrayListToStringCompiti(ArrayList<Compito> compiti) {
        String result="";

        for(int i = 0 ; i < compiti.size() ; i++) {
            if(i == 0) {
                result = compiti.get(i).getDescrizione();
            } else {
                result = result + "-" + compiti.get(i).getDescrizione();
            }
        }

        return result;
    }


    /*private void updateInfoCompiti(ArrayList<Compito> compiti) {
        URL url=null;
        Globals g = Globals.getInstance();
        String temp_url = g.getDomain() + "set_compiti.php?id_home=" + g.getIdString() + "&name_stanza=" + temp.getNameStanza() + "&compiti= " + ArrayListToStringCompiti(compiti);

        try {
            url = new URL(temp_url);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Creazione URL non riuscita", Toast.LENGTH_SHORT).show();
        }

        try {
            new TaskAsincrono(getApplicationContext(), url , new TaskCompleted() {
                @Override
                public void onTaskComplete(Object resp) {





                    Toast.makeText(getApplicationContext(), "Compiti aggiunti con successo", Toast.LENGTH_SHORT).show();


                }
            }).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }*/


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

    private int getIndiceStanza(ArrayList<Stanza> stanze , String name_stanza) {
        int position = -1;
        for(int i = 0 ; i < stanze.size() ; i++) {
            if ( name_stanza.equals(stanze.get(i).getNameStanza()) ) {
                position = i;
            }
        }
        return position;
    }

}



