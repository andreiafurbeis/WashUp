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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.tooltip.Tooltip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ModificaCompitiTutorial extends AppCompatActivity{

    Button title_bar;
    Button button_aggiungi;
    LinearLayout linear;
    Persona temp_persona;
    ListView listview;
    Stanza temp;
    ArrayList<Compito> compiti=new ArrayList<Compito>();
    ArrayList<Compito> new_compiti = new ArrayList<Compito>();
    TextView tv;
    AdapterCompiti adapter;




    SharedPreferences pref;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_compiti_tutorial);
        button_aggiungi = (Button) findViewById(R.id.modifica_compiti_tutorial_button_add);

        title_bar = (Button) findViewById(R.id.modifica_compiti_tutorial_title_bar);
        linear = findViewById(R.id.modifica_compiti_tutorial_linear);
        tv = (TextView) findViewById(R.id.modifica_compiti_tutorial_text1);
        listview = (ListView) findViewById(R.id.modifica_compiti_tutorial_listview);


        ActionBar barra = getSupportActionBar();
        barra.hide();
        Intent intent = getIntent();
        String nome_stanza = intent.getStringExtra("nome_stanza");
        temp = new Stanza(null, nome_stanza, null);
        //Log.d("ModificaCompitiTutorial", "IL NOME DELLA STANZA E'" + nome_stanza);

        //temp_persona = getIntent().getParcelableExtra("persona");

        pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = pref.getString("persona", "");
        temp_persona = gson.fromJson(json, Persona.class);

        /*if (temp_persona == null) {
            Log.d("ConfigurazioneStanze", "L'oggetto appena scaricato dalle SharedPreference é NULL");
        }*/

        title_bar.setText(title_bar.getText() + " " + temp.getNameStanza());
        //indice_stanza = getIndiceStanza(temp_persona.getStanze() , getIntent().getStringExtra("nome_stanza"));
        //indice_stanza = temp_persona.getIndiceStanza(getIntent().getStringExtra("nome_stanza"));


        Compito compito1 = new Compito(null, "lava", null, null);
        Compito compito2 = new Compito(null, "stira", null, null);
        Compito compito3 = new Compito(null, "asciuga", null, null);
        Compito compito4 = new Compito(null, "stendi", null, null);
        compiti.add(compito1);
        compiti.add(compito2);
        compiti.add(compito3);
        compiti.add(compito4);

        adapter = new AdapterCompiti(this,compiti);



        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                removeItemFromList(position);


                return true;
            }

        });

        addCompitiToListView();




        final Tooltip tooltip_aggiungi_compito  = new Tooltip.Builder(button_aggiungi)
                .setText("Aggiungi un nuovo compito")
                .setBackgroundColor(Color.parseColor("#ff669900"))
                .setTextColor(Color.WHITE)


                .setGravity(Gravity.TOP)
                .show();



        button_aggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
                tooltip_aggiungi_compito.dismiss();





            }
        });


    }


        private void createDialog() {

            //Log.d("ModificaCompiti" , "Sono denro alla crezione della Dialog");

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
                            //Log.d("ModificaCompiti" , "La decrizione é : " + testo);

                            if(temp_persona == null) {
                                //Log.d("ModificaCompiti" , "La persona é NULL" );

                            }


                            //Log.d("ModificaCompiti" , "La persona si chiama " + temp_persona.getNome() )
                            Compito compito = new Compito(descrizione.getText().toString(),temp.getNameStanza(),temp_persona.getIdHome());

                            //ArrayList<Compito> compiti = temp_persona.getCompitiStanza(indice_stanza);
                        /*if(compiti == null) {
                            Log.d("ModificaCompiti" , "Crezione nuovo ArrayList indice stanza = " + indice_stanza );

                            compiti = new ArrayList<Compito>();
                        }*/
                            new_compiti.add(compito);
                            compiti.add(compito);
                            //temp_persona.setCompiti(compiti);
                            //temp_persona.setCompitiStanza(indice_stanza,compiti);
                            addCompitiToListView();

                            tv = findViewById(R.id.modifica_compiti_tutorial_text1);
                            tv.setVisibility(View.VISIBLE);


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





    protected void removeItemFromList(int position) {
        final int deletePosition = position;

        AlertDialog.Builder alert = new AlertDialog.Builder(
                ModificaCompitiTutorial.this);

        alert.setTitle("Cancella");
        alert.setMessage("Vuoi cancellare il compito dalla stanza " + getIntent().getStringExtra("nome_stanza") + " ?");
        alert.setPositiveButton("Si",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                compiti.remove(deletePosition );



                adapter.notifyDataSetChanged();
                adapter.notifyDataSetInvalidated();



               finetutorial();


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


    private void addCompitiToListView() {
        //Log.d("ModificaCompiti" , "imposto nuovo Adapter" );

        //adapter = new AdapterCompiti(this , compiti);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();



    }

        //compiti = temp_persona.getCompitiStanza(indice_stanza);

        //Log.d("ModificaCompiti" , "ci sono : " + compiti.size() + "compiti in questa stanza ");



   public void finetutorial(){
       AlertDialog.Builder alert_end = new AlertDialog.Builder(
               ModificaCompitiTutorial.this);



       alert_end.setTitle("Tutorial Terminato");
       alert_end.setMessage("Il tutorial e' terminato , clicca ok e procedi con la creazione della tua casa."  );
       alert_end.setPositiveButton("OK", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
               Intent intent_dialog = new Intent(ModificaCompitiTutorial.this,NewHome.class);
               startActivity(intent_dialog);
           }
       });


       try {
           Thread.sleep(500);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }

       alert_end.show();

   }

    }







