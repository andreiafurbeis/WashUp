package com.example.andrearubeis.wash_up;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ModificaCompitiActivity extends AppCompatActivity{

    Button title_bar;
    Button button_aggiungi;
    LinearLayout linear;
    Persona temp_persona;
    ListView listview;
    Stanza temp;
    int indice_stanza;
    ArrayList<Compito> compiti=new ArrayList<Compito>();
    ArrayList<Compito> new_compiti = new ArrayList<Compito>();

    AdapterCompiti adapter;




    SharedPreferences pref;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_compiti);
        button_aggiungi = (Button) findViewById(R.id.modifica_compiti_button_add);

        title_bar = (Button) findViewById(R.id.modifica_compiti_title_bar);
        linear = findViewById(R.id.modifica_compiti_linear);

        listview = (ListView) findViewById(R.id.modifica_compiti_listview);





        ActionBar barra = getSupportActionBar();
        barra.hide();
        Intent intent = getIntent();
        temp = new Stanza (null, intent.getStringExtra("nome_stanza") ,null);


        pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = pref.getString("persona", "");
        temp_persona = gson.fromJson(json, Persona.class);

        /*if(temp_persona == null) {
            Log.d("ConfigurazioneStanze" , "L'oggetto appena scaricato dalle SharedPreference é NULL");
        }*/

        title_bar.setText(title_bar.getText() + " " + temp.getNameStanza());
        indice_stanza = temp_persona.getIndiceStanza(getIntent().getStringExtra("nome_stanza"));

        compiti = temp_persona.getCompitiStanza(indice_stanza);

        Log.d("ModificaCompiti" , "ci sono : " + compiti.size() + "compiti in questa stanza ");


        adapter = new AdapterCompiti(this , compiti);


        addCompitiToListView();





        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                removeItemFromList(position);

                return true;
            }

        });








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

        //Log.d("ModificaCompiti" , "Sono denro alla crezione della Dialog");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.aggiungi_compito_dialog , null);
        builder.setView(dialogView)
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


                        Log.d("ModificaCompiti" , "La persona si chiama " + temp_persona.getNome() );
                        Compito compito = new Compito(descrizione.getText().toString(),temp.getNameStanza(),temp_persona.getIdHome());

                        if(compiti == null) {
                            Log.d("ModificaCompiti" , "Crezione nuovo ArrayList indice stanza = " + indice_stanza );

                            compiti = new ArrayList<Compito>();
                        }
                        new_compiti.add(compito);
                        compiti.add(compito);
                        addCompitiToListView();
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


    @Override
    public void onBackPressed() {

        //Aggiornare DataBase
        if(new_compiti.size() != 0 ) {
            updateInfoCompiti(new_compiti);
            updateInfoCompitiLocal(indice_stanza,new_compiti);
        }
        Intent intent = new Intent(ModificaCompitiActivity.this , AggiungiStanzaActivity.class);
        Bundle bundle = new Bundle();



        SharedPreferences.Editor editor = pref.edit();
        editor.remove("persona");
        editor.apply();
        Gson gson = new Gson();
        String json = gson.toJson(temp_persona);
        editor.putString("persona", json);


        // Save the changes in SharedPreferences
        editor.commit();




        //bundle.putParcelableArrayList("compiti" , compiti_global);
        /*if(compiti == null) {
            Log.d("ModificaCompiti" , "I compiti sono NULL");
        }else{
            //Log.d("ModificaCompiti" , "Array compiti ha dimensione : " + temp_persona.getStanze().get(indice_stanza).getCompiti().size());
        }*/
        bundle.putString("nome_stanza" , temp.getNameStanza());
        bundle.putInt("update",1);
        bundle.putInt("indice_stanza",indice_stanza);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void updateInfoCompitiLocal(int indice_stanza , ArrayList<Compito> compiti) {

        temp_persona.setCompitiStanza(indice_stanza , compiti);
    }




    private void addCompitiToListView() {
        //Log.d("ModificaCompiti" , "imposto nuovo Adapter" );

        //adapter = new AdapterCompiti(this , compiti);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();



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


    private void updateInfoCompiti(ArrayList<Compito> compiti) {
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
    }



    protected void removeItemFromList(int position) {
        final int deletePosition = position;

        AlertDialog.Builder alert = new AlertDialog.Builder(
                ModificaCompitiActivity.this);

        alert.setTitle("Cancella");
        alert.setMessage("Vuoi cancellare il compito dalla stanza " + getIntent().getStringExtra("nome_stanza") + " ?");
        alert.setPositiveButton("Si",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeCompitoFromDB(compiti.get(deletePosition));
                compiti.remove(deletePosition );

                temp_persona.setCompitiStanza(indice_stanza,compiti);

                pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.remove("persona");
                editor.apply();
                Gson gson = new Gson();
                String json = gson.toJson(temp_persona);
                editor.putString("persona", json);
                // Save the changes in SharedPreferences
                editor.commit(); // commit changes



                adapter.notifyDataSetChanged();
                adapter.notifyDataSetInvalidated();


            }
        });
        alert.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();

    }

    private void removeCompitoFromDB(Compito compito_da_rimuovere) {
        Globals g = Globals.getInstance();
        String temp_url = g.getDomain()+"remove_compito.php?id_compito=" + compito_da_rimuovere.getId();
        URL url = null;
        try {
            url = new URL(temp_url);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Creazione URL non riuscita", Toast.LENGTH_SHORT).show();
        }

        new TaskAsincrono(getApplicationContext(), url  , new TaskCompleted() {
            @Override
            public void onTaskComplete(Object resp) {


                Toast.makeText(getApplicationContext(), "Compito cancellato con successo", Toast.LENGTH_SHORT).show();


            }
        }).execute();
    }







}


