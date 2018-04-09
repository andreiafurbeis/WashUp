package com.example.andrearubeis.wash_up;

/**
 * Created by andrearubeis on 22/12/17.
 */


        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
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

        import java.io.IOException;
        import java.util.HashMap;

        import static android.app.Activity.RESULT_OK;


public class OptionFragmentActivity extends Fragment {

    //dichiarazione variabili di classe

    Button log_out;
    Button modifica_casa;
    Button contact_us;
    Button aggiungi_inquilino;
    Button abbandona_casa;
    TextView nome_persona;
    ImageView profile_image;
    Context context;
    View vista;



    Drawable img_profilo;
    CharSequence nome_profilo;

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

        String nome = getArguments().getString("nome");
        String cognome = getArguments().getString("cognome");
        String image_profile = getArguments().getString("profile_image");

        Log.d("OptionFragment" , nome + " " +  cognome + " " + image_profile);


        profile_image = (ImageView) vista.findViewById(R.id.fragment_option_profile_image);

        Drawable image_drawable = getDrawable(image_profile);

        profile_image.setImageDrawable(image_drawable);


        nome_persona = (TextView) vista.findViewById(R.id.fragment_option_text_name);
        nome_persona.setText(nome + " " + cognome);

        //azione pulsante modifica_casa
        modifica_casa = (Button) vista.findViewById(R.id.fragment_option_button_modifica_casa);
        modifica_casa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),
                        ConfigurazioneStanzaActivity.class);
                startActivity(intent);
            }
        });



        //azione pulsante aggiungi_inquilino
        aggiungi_inquilino = (Button) vista.findViewById(R.id.fragment_option_button_aggiungi_inquilino);
        aggiungi_inquilino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AggiungiInquilinoActivity.class);
                startActivity(intent);
            }
        });


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
        //prendi da imagechooser l'alert dialog

        abbandona_casa = (Button) vista.findViewById(R.id.fragment_option_button_log_out_home);
        abbandona_casa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitFromHome();
            }
        });


        //azione pulsante log_out

        log_out = (Button) vista.findViewById(R.id.fragment_option_button_log_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });


        //azione pulsante contact_us

        contact_us = (Button) vista.findViewById(R.id.fragment_option_button_contact_us);

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


    public void logOut() {

        final CharSequence[] items = { "si",  context.getString(R.string.annulla) };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sei sicuro di voler tornare al login ?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if(items[item].equals("si")) {

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

        final CharSequence[] items = { "si",  context.getString(R.string.annulla) };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sei sicuro di voler uscire dalla casa ?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if(items[item].equals("si")) {

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


    public Drawable getDrawable(String image) {
        Drawable image_drawable;
        String[] image_path = image.split(" ");


            Toast.makeText(getActivity(), "Sto mettendo l'immagine con il metodo nuovo", Toast.LENGTH_SHORT).show();
            ImageManager manager = new ImageManager(getActivity());
            Bitmap image_bitmap = manager.loadImageFromStorage(image_path[0], image_path[1]);
            Log.d("OptionFragment" , "il path è : " + image_path[0] + "   " + image_path[1]);
            Bitmap image_bitmap_scaled = Bitmap.createScaledBitmap(image_bitmap , ViewGroup.LayoutParams.MATCH_PARENT , getResources().getDisplayMetrics().heightPixels/6,true);
            image_drawable = new BitmapDrawable(getResources(), image_bitmap);


        return image_drawable;
    }

}