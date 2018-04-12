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
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ModificaCompitiActivity extends AppCompatActivity{

    Button title_bar;
    Button button_aggiungi;
    LinearLayout linear;
    Persona temp_persona;
    //ListView listview;
    Stanza temp;
    ArrayList<Compito> compiti_global;
    int indice_stanza;
    ArrayList<Compito> compiti=null;
    SwipeMenuListView listView;
    AdapterCompiti adapter;



    SharedPreferences pref;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_compiti);
        button_aggiungi = (Button) findViewById(R.id.modifica_compiti_button_add);

        title_bar = (Button) findViewById(R.id.modifica_compiti_title_bar);
        linear = findViewById(R.id.modifica_compiti_linear);

        //listview = (ListView) findViewById(R.id.modifica_compiti_listview);

        listView = (SwipeMenuListView) findViewById(R.id.listView);




        ActionBar barra = getSupportActionBar();
        barra.hide();
        Intent intent = getIntent();
        //String nome_stanza = intent.getStringExtra("nome_stanza");
        temp = new Stanza (null, intent.getStringExtra("nome_stanza") ,null);

        //temp_persona = getIntent().getParcelableExtra("persona");

        pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = pref.getString("persona", "");
        temp_persona = gson.fromJson(json, Persona.class);

        if(temp_persona == null) {
            Log.d("ConfigurazioneStanze" , "L'oggetto appena scaricato dalle SharedPreference é NULL");
        }

        title_bar.setText(title_bar.getText() + " " + temp.getNameStanza());
        //indice_stanza = getIndiceStanza(temp_persona.getStanze() , getIntent().getStringExtra("nome_stanza"));
        indice_stanza = temp_persona.getIndiceStanza(getIntent().getStringExtra("nome_stanza"));

        compiti = temp_persona.getCompitiStanza(indice_stanza);

        //Log.d("ModificaCompiti" , "ci sono : " + compiti.size() + "compiti in questa stanza ");

        addCompiti(compiti,temp_persona);



        button_aggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();

            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                //create an action that will be showed on swiping an item in the list
                SwipeMenuItem item1 = new SwipeMenuItem(
                        getApplicationContext());
                item1.setBackground(new ColorDrawable(Color.DKGRAY));
                // set width of an option (px)
                item1.setWidth(200);
                item1.setTitle("Action 1");
                item1.setTitleSize(18);
                item1.setTitleColor(Color.WHITE);
                menu.addMenuItem(item1);

                SwipeMenuItem item2 = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                item2.setBackground(new ColorDrawable(Color.RED));
                item2.setWidth(200);
                item2.setTitle("Action 2");
                item2.setTitleSize(18);
                item2.setTitleColor(Color.WHITE);
                menu.addMenuItem(item2);
            }
        };
        //set MenuCreator
        listView.setMenuCreator(creator);
        // set SwipeListener
        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Compito value = adapter.getItem(position);
                switch (index) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "Action 1 for "+ value.getDescrizione() , Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "Action 2 for "+ value.getDescrizione() , Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }});




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


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        //Aggiornare DataBase
        updateInfoCompiti(compiti_global);
        Intent intent = new Intent(ModificaCompitiActivity.this , AggiungiStanzaActivity.class);
        Bundle bundle = new Bundle();
        //bundle.putParcelable("persona" , temp_persona);

        temp_persona.setCompitiStanza(indice_stanza,compiti_global);

        SharedPreferences.Editor editor = pref.edit();
        editor.remove("persona");
        editor.apply();
        Gson gson = new Gson();
        String json = gson.toJson(temp_persona);
        editor.putString("persona", json);


        // Save the changes in SharedPreferences
        editor.commit();




        //bundle.putParcelableArrayList("compiti" , compiti_global);
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
    }



    private void addCompiti(ArrayList<Compito> compiti , Persona utente) {
        Log.d("ModificaCompiti" , "imposto nuovo Adapter" );

        adapter = new AdapterCompiti(this , compiti);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();





        //Aggiornare dati locali

        compiti_global = compiti;

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


