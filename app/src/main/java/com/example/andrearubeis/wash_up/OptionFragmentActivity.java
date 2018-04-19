package com.example.andrearubeis.wash_up;

/**
 * Created by andrearubeis on 22/12/17.
 */


        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.Bitmap;
        import android.graphics.drawable.BitmapDrawable;
        import android.graphics.drawable.Drawable;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.provider.MediaStore;
        import android.support.v4.app.Fragment;
        import android.support.v7.app.AlertDialog;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.andrearubeis.wash_up.R;
        import com.google.gson.Gson;
        import com.squareup.picasso.Picasso;

        import java.io.File;
        import java.io.IOException;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.concurrent.ExecutionException;

        import okhttp3.MediaType;
        import okhttp3.MultipartBody;
        import okhttp3.RequestBody;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

        import static android.app.Activity.RESULT_OK;
        import static android.content.Context.MODE_PRIVATE;


public class OptionFragmentActivity extends Fragment {

    //dichiarazione variabili di classe

    Button log_out;
    Button modifica_casa;
    Button contact_us;
    Button about;
    Button aggiungi_inquilino;
    Button abbandona_casa;
    TextView nome_persona;
    ImageView profile_image;
    Context context;
    View vista;
    SharedPreferences pref;
    Persona temp_persona;


    public static OptionFragmentActivity newInstance() {
        OptionFragmentActivity fragment = new OptionFragmentActivity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        context = getActivity();

        vista = inflater.inflate(R.layout.fragment_option, container, false);

        //inizializzazione Button
        nome_persona = (TextView) vista.findViewById(R.id.fragment_option_text_name);
        about = (Button) vista.findViewById(R.id.fragment_option_button_about);
        modifica_casa = (Button) vista.findViewById(R.id.fragment_option_button_modifica_casa);
        aggiungi_inquilino = (Button) vista.findViewById(R.id.fragment_option_button_aggiungi_inquilino);
        abbandona_casa = (Button) vista.findViewById(R.id.fragment_option_button_log_out_home);
        profile_image = (ImageView) vista.findViewById(R.id.fragment_option_profile_image);
        log_out = (Button) vista.findViewById(R.id.fragment_option_button_log_out);
        contact_us = (Button) vista.findViewById(R.id.fragment_option_button_contact_us);




        pref = getActivity().getSharedPreferences("persona", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString("persona", "");
        temp_persona = gson.fromJson(json, Persona.class);


        if(temp_persona == null) {
            Log.d("ConfigurazioneStanze" , "L'oggetto appena scaricato dalle SharedPreference é NULL");
        }

        Log.d("OptionFragment" , temp_persona.getNome() + " " +  temp_persona.getCognome() + " " + temp_persona.getProfileImage());



        //inizializza l'activity con
        inizializzaInterfaccia();

        //azione pulsante about
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(),AboutActivity.class);
                ricalcolaCompiti();
            }
        });

        //azione pulsante modifica_casa
        modifica_casa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                //bundle.putParcelable("persona" , getArguments().getParcelable("persona"));
                Intent intent = new Intent(getActivity(),
                        ConfigurazioneStanzaActivity.class);
                intent.putExtras(bundle);
                Log.d("OptionFragment","Sto per Andare in Configurazione Stanze ");
                startActivity(intent);
            }
        });

        //azione pulsante aggiungi_inquilino
        aggiungi_inquilino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                //bundle.putParcelable("persona" , getArguments().getParcelable("persona"));
                Intent intent = new Intent(getActivity(), AggiungiInquilinoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        //cambio immagine profilo
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dispatchTakePictureIntent();
                //selectImage();
                ImageChooser img = new ImageChooser( context , OptionFragmentActivity.this.getActivity());
                img.selectImage();

            }
        });


        //azione pulsante abbandona_casa
        abbandona_casa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitFromHome();
            }
        });


        //azione pulsante log_out
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });


        //azione pulsante contact_us
        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "some@email.address" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, ""));
            }
        });


        return vista;

    }


    private void inizializzaInterfaccia() {
        nome_persona.setText(temp_persona.getNome() + " " + temp_persona.getCognome());
        Picasso.get().load(temp_persona.getProfileImage()).into(profile_image,new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {


            }

            @Override
            public void onError(Exception e) {

            }

        });

    }




    private void updateCompiti() {
        URL url=null;
        Globals g = Globals.getInstance();
        String temp_url = g.getDomain() + "assegna_compiti.php?id_home=" + g.getIdString();

        try {
            url = new URL(temp_url);
        } catch (IOException e) {
            Toast.makeText(context, "Creazione URL non riuscita", Toast.LENGTH_SHORT).show();
        }

        try {
            new TaskAsincrono(context, url , new TaskCompleted() {
                @Override
                public void onTaskComplete(Object resp) {





                    Toast.makeText(context, "Compiti ricalcolati con successo", Toast.LENGTH_SHORT).show();


                }
            }).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }



    public void ricalcolaCompiti() {

        final CharSequence[] items = { "Si",  context.getString(R.string.annulla) };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sei sicuro di voler ricalcolare i compiti ?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if(items[item].equals("Si")) {

                    //Toast.makeText(context, "hai cliccato si, ora ti mando al login", Toast.LENGTH_SHORT).show();

                    updateCompiti();
                }



                if (items[item].equals(context.getString(R.string.annulla))) {
                    dialog.dismiss();
                    //Toast.makeText(context, "hai cliccato annulla, torna sul fragment option", Toast.LENGTH_SHORT).show();


                }
                //Toast.makeText(getApplicationContext(), "hai cliccato su un elemento del dialog", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }




    public void logOut() {

        final CharSequence[] items = { "Si",  context.getString(R.string.annulla) };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sei sicuro di voler tornare al login ?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if(items[item].equals("Si")) {

                    //Toast.makeText(context, "hai cliccato si, ora ti mando al login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);

                }



                if (items[item].equals(context.getString(R.string.annulla))) {
                    dialog.dismiss();
                    //Toast.makeText(context, "hai cliccato annulla, torna sul fragment option", Toast.LENGTH_SHORT).show();


                }
                //Toast.makeText(getApplicationContext(), "hai cliccato su un elemento del dialog", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    //sei sicuro di voler uscire??


    public void exitFromHome() {

        final CharSequence[] items = { "Si",  context.getString(R.string.annulla) };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sei sicuro di voler uscire dalla casa ?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if(items[item].equals("Si")) {

                    //Toast.makeText(context, "hai cliccato si, ora ti mando al login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);

                }



                if (items[item].equals(context.getString(R.string.annulla))) {
                    dialog.dismiss();
                    //Toast.makeText(context, "hai cliccato annulla, torna sul fragment option", Toast.LENGTH_SHORT).show();


                }
                //Toast.makeText(getApplicationContext(), "hai cliccato su un elemento del dialog", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

}