package com.example.andrearubeis.wash_up;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.andrearubeis.wash_up.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class bottom_activity extends AppCompatActivity {


    public static final String UPLOAD_KEY_IMAGE = "image";
    public static final String UPLOAD_KEY_MAIL = "mail";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE_REQUEST = 2;

    ImageManager manager_image;




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
                                args.putParcelable("persona" , temp_persona);
                                args.putString("mail" , temp_persona.getMail());
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
                                args.putParcelable("persona" , temp_persona);
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

    @Override
    public void onActivityResult(int requestCode , int resultCode , Intent data) {
        super.onActivityResult(requestCode,resultCode,data);



        Uri filePath;

        Bitmap bitmap_image;

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            bitmap_image = (Bitmap) data.getExtras().get("data");

            manager_image = new ImageManager(getApplicationContext());

            manager_image.imageManager(bitmap_image);

            Log.d("Bottom" , "Siamo dentro REQUEST_IMAGE_CAPTURE il path della nuova immagine é : " + manager_image.getImagePath() + " " + manager_image.getImageName());



            //DEVO ANDARE NEL FRAGMET OPZIONI E AGGIONARE L'IMMAGINE

            updateOptionFragmentImage(manager_image.getImagePath() + " " + manager_image.getImageName());


            //profile_image.setImageBitmap(manager_image.getImageBitmap());

        }

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getData()!=null) {
            filePath = data.getData();
            try {
                bitmap_image = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //imageManager(imageBitmap);

                manager_image = new ImageManager(getApplicationContext());

                manager_image.imageManager(bitmap_image);

                //Log.d("Immagine" , "il path dell' immmagine è : " + manager.getImagePath());


                //DEVO ANDARE NEL FRAGMENT OPZIONI E AGGIORNARE L"IMMAGINE

                //profile_image.setImageBitmap(manager_image.getImageBitmap());

                updateOptionFragmentImage(manager_image.getImagePath() + " " + manager_image.getImageName());

                //updateInfoUserRequest();

            } catch (IOException e) {
                Log.d("Bottom" , "Eccezione catturata in PICK_IMAGE_REQUEST");
                e.printStackTrace();
            }
        }
    }


    private void updateOptionFragmentImage(String new_profile_image) {

        Fragment selectedFragment = OptionFragmentActivity.newInstance();
        Intent intent = getIntent();
        Bundle args = new Bundle();
        Persona temp_persona = intent.getParcelableExtra("persona");
        temp_persona.setProfileImage(new_profile_image);
        args.putParcelable("persona" , temp_persona);
        args.putString("mail" , temp_persona.getMail());
        args.putString("id" , temp_persona.getIdHome());
        args.putString("nome" , temp_persona.getNome());
        args.putString("cognome" , temp_persona.getCognome());
        args.putString("profile_image" , temp_persona.getProfileImage());
        selectedFragment.setArguments(args);

        updateInfoUserRequest(temp_persona.getMail(), temp_persona.getProfileImage());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_activity_bottom, selectedFragment);
        transaction.commit();

    }

    private void updateInfoUserRequest(String mail , String profile_image) {
        URL url=null;
        Globals g = Globals.getInstance();

        String temp_url = g.getDomain()+"update_info_user.php";
        try {
            url = new URL(temp_url+"?mail="+mail+"&image="+profile_image);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Creazione URL non riuscita", Toast.LENGTH_SHORT).show();
        }


        try {
            new TaskAsincrono(getApplicationContext(), url, new TaskCompleted() {
                @Override
                public void onTaskComplete(Object resp) {


                }
            }).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }




}
