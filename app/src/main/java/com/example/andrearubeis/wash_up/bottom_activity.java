package com.example.andrearubeis.wash_up;

import android.content.Context;
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
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.andrearubeis.wash_up.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
    Fragment fragment;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE_REQUEST = 2;
    ImageManager manager_image;
    JSONReader reader_json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar barra = getSupportActionBar();

        barra.hide();
        setContentView(R.layout.activity_bottom_activity);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

        //Log.d("BottomActivity" , "BottomActivity ricaricata");

        Bundle args = new Bundle();
        reader_json = new JSONReader(getApplicationContext());

        pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = pref.getString("persona", "");
        temp_persona = gson.fromJson(json, Persona.class);

    }


    @Override
    protected void onStart() {
        super.onStart();
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Bundle args;
                        switch (item.getItemId()) {
                            case R.id.navigation_home:

                                globalVariableReload();
                                selectedFragment = HomeFragmentActivity.newInstance();
                                args = new Bundle();
                                args.putParcelableArrayList("stanze" ,temp_persona.getStanze());
                                args.putString("id" , temp_persona.getIdHome());
                                args.putInt("bottom_height" , bottomNavigationView.getHeight());
                                selectedFragment.setArguments(args);

                                break;


                            case R.id.navigation_options:

                                globalVariableReload();

                                selectedFragment = OptionFragmentActivity.newInstance();
                                args = new Bundle();
                                args.putInt("bottom_height" , bottomNavigationView.getHeight());

                                selectedFragment.setArguments(args);

                                break;


                            case R.id.navigation_ruoli:

                                globalVariableReload();

                                selectedFragment = RuoliFragmentActivity.newInstance();

                                args = new Bundle();
                                args.putParcelableArrayList("stanze" ,temp_persona.getStanze());
                                args.putString("id" , temp_persona.getIdHome());
                                args.putInt("bottom_height" , bottomNavigationView.getHeight());
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

    @Override
    protected void onResume() {
        super.onResume();
        Bundle args = new Bundle();
        args.putParcelableArrayList("stanze" ,temp_persona.getStanze());
        args.putString("id" , temp_persona.getIdHome());
        args.putInt("bottom_height" , bottomNavigationView.getHeight());

        selectedFragment = HomeFragmentActivity.newInstance();
        selectedFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_activity_bottom, selectedFragment);
        transaction.commit();
    }

    public void logOut() {

        final CharSequence[] items = { "Si",  selectedFragment.getContext().getString(R.string.annulla) };
        AlertDialog.Builder builder = new AlertDialog.Builder(selectedFragment.getContext());
        builder.setTitle("Sei sicuro di voler tornare al login ?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if(items[item].equals("Si")) {

                    Intent intent = new Intent(selectedFragment.getContext(), MainActivity.class);
                    startActivity(intent);

                }



                if (items[item].equals(selectedFragment.getContext().getString(R.string.annulla))) {
                    dialog.dismiss();


                }
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

                manager_image = new ImageManager(getApplicationContext());

                manager_image.imageManager(bitmap_image);

                //Log.d("Immagine" , "il path dell' immmagine è : " + manager.getImagePath());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Log.d("BottomActivity","Adesso faccio l'upload");





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
            //Toast.makeText(getApplicationContext(),url_temp ,Toast.LENGTH_SHORT).show();

            url = new URL(url_temp);
        }catch(IOException e){
            Toast.makeText(getApplicationContext(),"Creazione URL non riuscita",Toast.LENGTH_SHORT).show();
        }


        //Log.d("BottomActivity","Provo a modificare Dati Utente DB con url : " + url.toString());


        try {
            new TaskAsincrono(getApplicationContext(), url , new TaskCompleted() {
                @Override
                public void onTaskComplete(Object resp) {

                    //Log.d("BottomActivity","Dati utente aggiornati sul DB");

                }
            }).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    private void globalVariableReload() {
        pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = pref.getString("persona", "");
        temp_persona = gson.fromJson(json, Persona.class);
        //Log.d("BottomActivity" , "Aggiorno TEMP_PERSONA");
        /*if(temp_persona == null) {
            Log.d("ConfigurazioneStanze" , "L'oggetto appena scaricato dalle SharedPreference é NULL");
        }*/
        URL url=null;
        Globals g = Globals.getInstance();

        try {
            String url_temp = g.getDomain() + "reload_info.php?mail=" + temp_persona.getMail() + "&home_id=" + temp_persona.getIdHome();
            //Toast.makeText(getApplicationContext(),url_temp ,Toast.LENGTH_SHORT).show();

            url = new URL(url_temp);
        }catch(IOException e){
            Toast.makeText(getApplicationContext(),"Creazione URL non riuscita",Toast.LENGTH_SHORT).show();
        }

        try {
            new TaskAsincrono(getApplicationContext(), url , new TaskCompleted() {
                @Override
                public void onTaskComplete(Object resp) {

                    String result = reader_json.getStringFromInputStream((InputStream) resp);

                    //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();

                    String[] parts = result.split("<br>");
                    reloadInfo(parts);



                }
            }).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }







    }



    public void reloadInfo(String [] parts) {

        temp_persona.setStanze(reader_json.readJSONStanze(parts[0]));
        temp_persona.setCoinquilini(reader_json.readJSONCoinquilini(parts[1] , temp_persona.getIdHome()));
        Persona dati_da_salvare = reader_json.readJSONPersona(parts[2]);
        temp_persona.setCompiti(dati_da_salvare.getCompiti());
        pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("persona");
        editor.apply();
        Gson gson = new Gson();
        String json = gson.toJson(temp_persona);
        editor.putString("persona", json);
        editor.commit(); // commit changes
    }





    private void uploadImage() {


        ApiService service = RetroClient.getApiService();



        String imageString = manager_image.getImagePath()+"/"+manager_image.getImageName();

        //Log.d("AggiungiStanzaUpload" , "il path dell' immagine è : " + imageString);

        File file = new File(imageString);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);

        Call<Result> resultCall = service.uploadImage(body);
        //Log.d("BottomActivity","Sto provando a fare l'upload");
        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {


                if (response.isSuccessful()) {
                    if (response.body().getResult().equals("success")) {
                        //Log.d("OptionUpload", "Upload Success , il path sul server è : " + response.body().getValue());

                        reloadUI();

                    }else {
                        //Log.d("RegistrazioneUpload", "Upload Fail");

                    }
                } else {
                    //Log.d("RegistrazioneUpload" , "Upload Fail");

                }


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
