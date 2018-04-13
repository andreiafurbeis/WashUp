package com.example.andrearubeis.wash_up;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrearubeis.wash_up.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity  {

    private TaskAsincrono httpRequest = null;
    EditText username;
    EditText password;
    Button invia;
    TextView log;
    Button registrazione;
    InputStream in = null;
    URL url = null;
    ArrayList<Persona> coinquilini_global;
    SharedPreferences pref;
    Persona temp_persona;
    View parentView;

    //private final String domain_url = "http://192.168.0.24/";   //dominio portatile
    //private final String domain_url = "http://192.168.1.100/";  //dominio fisso
    //private final String domain_url = "http://rubeisandrea.myqnapcloud.com/"; //domanio nas
    //private final String domain_url = "http://washit.dek4.net/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Nascondere AppBar

        ActionBar barra = getSupportActionBar();
        barra.hide();

        username = (EditText) findViewById(R.id.login_text_username);
        password = (EditText) findViewById(R.id.login_text_password);
        invia = (Button) findViewById(R.id.login_button_login);
        registrazione = (Button) findViewById(R.id.login_button_registrazione);
        //log = (TextView) findViewById(R.id.testo);

        parentView = (View) findViewById(R.id.login_parent_view);

        invia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usr = username.getText().toString();
                String pwd = md5(password.getText().toString());
                String risultato = null;
                Globals g = Globals.getInstance();
                try {
                    String url_temp = g.getDomain() + "login.php?usr=" + usr + "&psw=" + pwd;
                    Toast.makeText(getApplicationContext(),url_temp ,Toast.LENGTH_SHORT).show();

                    url = new URL(url_temp);
                }catch(IOException e){
                    Toast.makeText(getApplicationContext(),"Creazione URL non riuscita",Toast.LENGTH_SHORT).show();
                }

                g.setMail(usr.toString());


                try {
                    new TaskAsincrono(getApplicationContext(), url , new TaskCompleted() {
                        @Override
                        public void onTaskComplete(Object resp) {


                            String result = getStringFromInputStream((InputStream) resp);

                            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();

                            String[] parts = result.split("<br>");

                            Log.d("MainActivity" , "La stringa risultante é : " + result);

                            Globals g = Globals.getInstance();

                            if(parts[0].equals("1")) {

                                temp_persona = new Persona(null , null , username.getText().toString() , null , null , null);





                                String JSON_id = readJSON(parts[1]);



                                readJSONPersona(parts[3]);

                                pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.remove("persona");
                                editor.apply();

                                Gson gson = new Gson();
                                String json = gson.toJson(temp_persona);
                                editor.putString("persona", json);


                                // Save the changes in SharedPreferences
                                editor.commit(); // commit changes
                                uploadImage();

                                Toast.makeText(getApplicationContext(),g.getIdString(),Toast.LENGTH_SHORT).show();


                                if(JSON_id!=null) {
                                    temp_persona.setIdHome(JSON_id);
                                    g.setIdString(JSON_id);
                                    LoadHome home = new LoadHome(JSON_id , g.getDomain() , getApplicationContext());
                                    home.readJSON(parts[2]);
                                    temp_persona.setStanze(home.getStanze());
                                    //g.setStanze(home.getVectorStanze());

                                    getCoinquilini(temp_persona);
                                    //Log.d("MainActivity" , "vivi con : " + coinquilini_global.size() + "coinquilini");

                                    /*temp_persona.setCoinquilini(coinquilini_global);

                                    pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);
                                    editor = pref.edit();
                                    editor.remove("persona");
                                    editor.apply();
                                    gson = new Gson();
                                    json = gson.toJson(temp_persona);
                                    Log.d("MainActivity" , "La persona si chiama " + temp_persona.getNome());

                                    editor.putString("persona", json);



                                    // Save the changes in SharedPreferences
                                    editor.commit(); // commit changes



                                    Intent intent = new Intent(MainActivity.this, bottom_activity.class);
                                    //Bundle bundle = new Bundle();
                                    //bundle.putParcelable("persona" , temp_persona);
                                    //bundle.putString("id" , JSON_id);

                                    //intent.putExtra("id" , JSON_id);


                                    //intent.putExtra("stanze" , parts[2]);


                                    //intent.putParcelableArrayListExtra("stanze" , home.getStanze());

                                    //intent.putExtras(bundle);

                                    if(home.getStanze() == null) {
                                        Toast.makeText(getApplicationContext(),"MainActivity : il vettore è NULL ",Toast.LENGTH_SHORT).show();
                                        Log.d("MainActivity-LoadHome" , "Il vettore tornato è NULL");
                                    }else{
                                        Log.d("MainActivity-LoadHome" , "Il vettore tornato è : " + home.getStanze().toString());
                                    }

                                    startActivity(intent);*/

                                    /*Bundle args = new Bundle();
                                    //args.putParcelable("stanze", home.getVectorStanze());
                                    args.putParcelableArrayList("stanze" , home.getVectorStanze());
                                    args.putString("id" , JSON_id);

                                    HomeFragmentActivity fragment = new HomeFragmentActivity();
                                    fragment.setArguments(args);
                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.frame_activity_bottom, fragment);
                                    transaction.commit();*/



                                }else {



                                    Toast.makeText(getApplicationContext(),g.getIdString(),Toast.LENGTH_SHORT).show();

                                    //Bundle bundle = new Bundle();
                                    //bundle.putParcelable("persona" , temp_persona);


                                    Intent intent = new Intent(MainActivity.this, NewHome.class);
                                    //intent.putExtras(bundle);
                                    startActivity(intent);
                                }


                            }else{


                                Toast.makeText(getApplicationContext(),"Login non riuscito",Toast.LENGTH_SHORT).show();
                            }
                            //readStream((String) resp);
                        }
                    }).execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        registrazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        Registrazione.class);
                startActivity(intent);
            }
        });

    }

    public void getCoinquilini(Persona persona) {

        URL url=null;
        Globals g = Globals.getInstance();

        try {
            url = new URL(g.getDomain() + "get_coinquilini.php?home_id="+persona.getIdHome()+"&mail="+persona.getMail());
        }catch(IOException e){
            Toast.makeText(getApplicationContext(),"Creazione URL non riuscita",Toast.LENGTH_SHORT).show();
        }

        new TaskAsincrono(getApplicationContext(), url , new TaskCompleted() {
            @Override
            public void onTaskComplete(Object resp) {

                Globals g = Globals.getInstance();

                String result = getStringFromInputStream((InputStream) resp);

                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();

                readJSONCoinquilini(result , g.getIdString());

            }
        }).execute();



    }



    //Si occupa di leggere il file JSON di risposta
    public String readJSON (String jsonString) {

        String id=null;
        if(!jsonString.equals("[]")) {
            try {

                jsonString = "{\"info\":" + jsonString + "}";
                Log.w("INFORMATION", jsonString);
                JSONObject jsonObj = new JSONObject(jsonString);
                JSONArray info = jsonObj.getJSONArray("info");
                JSONObject c = info.getJSONObject(0);
                id = c.getString("id");

            } catch (Exception e) {
                Log.w("Exception", "Eccezione durante lettura JSON Login");
            }
            return id;
        } else {
            return null;
        }



    }

    public void readJSONPersona (String jsonString) {

        String nome=null;
        String cognome=null;
        String profile_image=null;

        try {

            jsonString = "{\"persona\":"+ jsonString + "}";
            Log.w("INFORMATION", jsonString);
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray info = jsonObj.getJSONArray("persona");
            JSONObject c = info.getJSONObject(0);
            //nome = c.getString("nome");
            //cognome = c.getString("cognome");
            //profile_image = c.getString("profile_image");
            temp_persona.setCognome(c.getString("cognome"));
            temp_persona.setNome(c.getString("nome"));
            temp_persona.setProfileImage(c.getString("profile_image"));

            Log.d("MainActivity", "ProfileImage é : " + temp_persona.getProfileImage());


        }catch (Exception e){
            Log.w("Exception" , "Eccezione durante lettura JSON Login");
        }

    }



    public void readJSONCoinquilini (String jsonString , String id_home ) {

        //new_home_intent = this.getIntent();
        //temp_persona = new_home_intent.getParcelableExtra("persona");

        coinquilini_global= new ArrayList<Persona>();

        try {


            jsonString = "{\"coinquilini\":"+ jsonString + "}";
            Log.w("INFORMATION", jsonString);
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray stanze = jsonObj.getJSONArray("coinquilini");
            for(int i = 0; i< stanze.length(); i++) {
                JSONObject c = stanze.getJSONObject(i);



                //Log.w("INFORMAIONE" , image_stanza);


                Persona persona_temp = new Persona(c.getString("nome") , c.getString("cognome") , c.getString("mail") , c.getString("profile_image") , id_home , null );
                //Stanza stanza = new Stanza(c.getString("stanza_image"),c.getString("nome_stanza"));
                coinquilini_global.add(persona_temp);

            }

        }catch (Exception e){
            Log.d("ConfigurazioneStanze" , "Eccezione catturata nel ReadJSON");
        }

        //Globals g = Globals.getInstance();
        //g.setStanze(vectorStanze);


        getLogged();

    }

    private void getLogged() {
        temp_persona.setCoinquilini(coinquilini_global);

        pref = getApplicationContext().getSharedPreferences("persona", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("persona");
        editor.apply();
        Gson gson = new Gson();
        String json = gson.toJson(temp_persona);
        Log.d("MainActivity" , "La persona si chiama " + temp_persona.getNome());

        editor.putString("persona", json);



        // Save the changes in SharedPreferences
        editor.commit(); // commit changes



        Intent intent = new Intent(MainActivity.this, bottom_activity.class);
        //Bundle bundle = new Bundle();
        //bundle.putParcelable("persona" , temp_persona);
        //bundle.putString("id" , JSON_id);

        //intent.putExtra("id" , JSON_id);


        //intent.putExtra("stanze" , parts[2]);


        //intent.putParcelableArrayListExtra("stanze" , home.getStanze());

        //intent.putExtras(bundle);

        /*if(home.getStanze() == null) {
            Toast.makeText(getApplicationContext(),"MainActivity : il vettore è NULL ",Toast.LENGTH_SHORT).show();
            Log.d("MainActivity-LoadHome" , "Il vettore tornato è NULL");
        }else{
            Log.d("MainActivity-LoadHome" , "Il vettore tornato è : " + home.getStanze().toString());
        }*/

        startActivity(intent);
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




    /*private void uploadInfoUser() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        // Change base URL to your upload server URL.

        ApiService service = (ApiService) new Retrofit.Builder().baseUrl("http://192.168.0.234:3000").client(client).build().create(Service.class);


        File file = new File(temp_persona.getProfileImage());

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

        retrofit2.Call<okhttp3.ResponseBody> req = service.postImage(body, name);
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Do Something
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }*/

    private void uploadImage() {

        /**
         * Progressbar to Display if you need
         */
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(getString(R.string.caricamento_dati));
        progressDialog.show();

        //Create Upload Server Client
        ApiService service = RetroClient.getApiService();

        //File creating from selected URL
        File file = new File(temp_persona.getProfileImage());

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);

        Call<Result> resultCall = service.uploadImage(body);
        Log.d("MainActivity","Sto provando a fare l'upload");
        // finally, execute the request
        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                progressDialog.dismiss();

                // Response Success or Fail
                if (response.isSuccessful()) {
                    if (response.body().getResult().equals("success"))
                        Snackbar.make(parentView,"Upload Success", Snackbar.LENGTH_LONG).show();
                    else
                        Snackbar.make(parentView, "Upload Fail", Snackbar.LENGTH_LONG).show();

                } else {
                    Snackbar.make(parentView, "Upload Fail", Snackbar.LENGTH_LONG).show();
                }

                /**
                 * Update Views
                 */
                //imagePath = "";
                //textView.setVisibility(View.VISIBLE);
                //imageView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }




}
