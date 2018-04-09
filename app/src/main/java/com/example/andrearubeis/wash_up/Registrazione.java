package com.example.andrearubeis.wash_up;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.andrearubeis.wash_up.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Registrazione extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText email;
    private Button registrati;
    private ImageView foto_profilo;
    public ImageManager manager_image = null;

    private Uri filePath;

    public static final String UPLOAD_KEY_IMAGE = "image";
    public static final String UPLOAD_KEY_PASS = "psw";
    public static final String UPLOAD_KEY_MAIL = "mail";
    public static final String UPLOAD_KEY_USER = "usr";



    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE_REQUEST = 2;
    final Activity call = this;
    int flag_new_image = 0;


    //private final String domain_url = "http://192.168.0.24/";   //dominio portatile
    //private final String domain_url = "http://192.168.1.100/";  //dominio fisso
    //private final String domain_url = "http://rubeisandrea.myqnapcloud.com/"; //domanio nas
    //private final String domain_url = "http://washit.dek4.net/registrazione.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        //Nascondere AppBar
        ActionBar barra = getSupportActionBar();
        barra.hide();





        username = findViewById(R.id.registrazione_username);
        password = findViewById(R.id.registrazione_password);
        email = findViewById(R.id.registrazione_mail);
        registrati = findViewById(R.id.registrazione_button_registrati);
        foto_profilo = findViewById(R.id.profile_image);

        //immagine profilo di default
        //imageBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.mipmap.ic_launcher);


        registrati.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String usr = username.getText().toString().trim();
                    //String pwd = md5(password.getText().toString()).trim();
                    String mail = email.getText().toString().trim();

                    if(!usr.equals("") && !password.getText().toString().trim().equals("") && !mail.equals("") && flag_new_image!=0) {

                        //Invio richiesta di registrazione
                        registrationRequest();


                    }else{
                        ImageChooser dialog = new ImageChooser(Registrazione.this , call);
                        if(flag_new_image == 1) {
                            Toast.makeText(getApplicationContext(), "Non si possono lasciare campi vuoti", Toast.LENGTH_SHORT).show();


                            dialog.noImageSelected(flag_new_image);

                        }else{
                            Toast.makeText(getApplicationContext() , "Devi impostare un immagine di profilo" , Toast.LENGTH_SHORT).show();

                            dialog.noImageSelected(flag_new_image);

                        }
                    }
                }
            });

        foto_profilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dispatchTakePictureIntent();
                //selectImage();
                ImageChooser img = new ImageChooser(Registrazione.this , call);
                img.selectImage();
                photoFlag();

            }
        });

    }


    public void photoFlag() {
        flag_new_image = 1;
    }




    //GESTORE ALLERT DIALOG

    @Override
    public void onActivityResult(int requestCode , int resultCode , Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        Bitmap bitmap_image;

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            bitmap_image = (Bitmap) data.getExtras().get("data");

            manager_image = new ImageManager(getApplicationContext());

            manager_image.imageManager(bitmap_image);

            foto_profilo.setImageBitmap(manager_image.getImageBitmap());

        }

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getData()!=null) {
            filePath = data.getData();
            try {
                bitmap_image = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //imageManager(imageBitmap);

                manager_image = new ImageManager(getApplicationContext());

                manager_image.imageManager(bitmap_image);

                //Log.d("Immagine" , "il path dell' immmagine è : " + manager.getImagePath());


                foto_profilo.setImageBitmap(manager_image.getImageBitmap());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    //Richiesta di registrazione , invio dati al DB
    private void registrationRequest(){
        class registrationRequest extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Registrazione.this, "Upload Immagine", "Attendi...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equals("1")){
                    Toast.makeText(getApplicationContext(),"Registrazione avvenuta con successo",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Errore durante la registrazione",Toast.LENGTH_LONG).show();
                }
                onBackPressed();
            }

            @Override
            protected String doInBackground(Bitmap... params) {

                HashMap<String,Object> data = new HashMap<>();
                data.put(UPLOAD_KEY_IMAGE, manager_image.getImagePath() + " " + manager_image.getImageName());
                data.put(UPLOAD_KEY_PASS,md5(password.getText().toString()));
                data.put(UPLOAD_KEY_MAIL,email.getText().toString());
                data.put(UPLOAD_KEY_USER,username.getText().toString());


                Globals g = Globals.getInstance();


                String result = rh.sendPostRequest(g.getDomain()+"registrazione.php",data);

                return result;
            }
        }

        registrationRequest ui = new registrationRequest();
        ui.execute();
    }


    //Convertitore MD5
    private String md5(String in) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (int i = 0; i < len; i++) {
                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 0x0f, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); }
        return null;
    }
}
