package com.example.andrearubeis.wash_up;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.example.andrearubeis.wash_up.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class bottom_activity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    SharedPreferences pref;
    Persona temp_persona;
    Fragment selectedFragment = null;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE_REQUEST = 2;
    ImageManager manager_image;

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

    @Override
    public void onActivityResult(int requestCode , int resultCode , Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        Globals g = Globals.getInstance();
        Bitmap bitmap_image;

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            bitmap_image = (Bitmap) data.getExtras().get("data");
            manager_image = new ImageManager(getApplicationContext());

            manager_image.imageManager(bitmap_image);


        }

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getData()!=null) {
            Uri URIfilePath = data.getData();
            try {
                bitmap_image = MediaStore.Images.Media.getBitmap(getContentResolver(), URIfilePath);
                //imageManager(imageBitmap);

                manager_image = new ImageManager(getApplicationContext());

                manager_image.imageManager(bitmap_image);

                //Log.d("Immagine" , "il path dell' immmagine è : " + manager.getImagePath());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("BottomActivity","Adesso faccio l'upload");





        temp_persona.setProfileImage(g.getDomain()+manager_image.getImageName());

        pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("persona");
        editor.apply();
        Gson gson = new Gson();
        String json = gson.toJson(temp_persona);
        editor.putString("persona", json);


        // Save the changes in SharedPreferences
        editor.commit(); // commit changes


        updateProfileImageDB();



        uploadImage();
    }

    private void updateProfileImageDB() {
        Globals g = Globals.getInstance();
        URL url = null;

        try {
            String url_temp = g.getDomain() + "update_info_user.php?mail=" + temp_persona.getMail() + "&image=" + g.getDomain()+manager_image.getImageName();
            Toast.makeText(getApplicationContext(),url_temp ,Toast.LENGTH_SHORT).show();

            url = new URL(url_temp);
        }catch(IOException e){
            Toast.makeText(getApplicationContext(),"Creazione URL non riuscita",Toast.LENGTH_SHORT).show();
        }


        Log.d("BottomActivity","Provo a modificare Dati Utente DB con url : " + url.toString());


        try {
            new TaskAsincrono(getApplicationContext(), url , new TaskCompleted() {
                @Override
                public void onTaskComplete(Object resp) {

                    Log.d("BottomActivity","Dati utente aggiornati sul DB");

                }
            }).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }






    private void uploadImage() {

        /**
         * Progressbar to Display if you need
         */
        /*final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage(getString(R.string.caricamento_dati));
        progressDialog.show();*/

        //Create Upload Server Client
        ApiService service = RetroClient.getApiService();

        //File creating from selected URL

        String imageString = manager_image.getImagePath()+"/"+manager_image.getImageName();

        Log.d("AggiungiStanzaUpload" , "il path dell' immagine è : " + imageString);

        File file = new File(imageString);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);

        Call<Result> resultCall = service.uploadImage(body);
        Log.d("BottomActivity","Sto provando a fare l'upload");
        // finally, execute the request
        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                //progressDialog.dismiss();

                // Response Success or Fail
                if (response.isSuccessful()) {
                    if (response.body().getResult().equals("success")) {
                        //Snackbar.make(parentView,"Upload Success", Snackbar.LENGTH_LONG).show();
                        Log.d("OptionUpload", "Upload Success , il path sul server è : " + response.body().getValue());

                        reloadUI();

                        //setImagePicasso();

                    }else {
                        //Snackbar.make(parentView, "Upload Fail", Snackbar.LENGTH_LONG).show();
                        Log.d("RegistrazioneUpload", "Upload Fail");

                    }
                } else {
                    //Snackbar.make(parentView, "Upload Fail", Snackbar.LENGTH_LONG).show();
                    Log.d("RegistrazioneUpload" , "Upload Fail");

                }




                //Update Views

                //imagePath = "";
                //textView.setVisibility(View.VISIBLE);
                //imageView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                //progressDialog.dismiss();
            }
        });
    }


    private void reloadUI() {
        selectedFragment = OptionFragmentActivity.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_activity_bottom, selectedFragment);
        transaction.commit();
    }
}
