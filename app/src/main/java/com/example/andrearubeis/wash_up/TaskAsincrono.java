package com.example.andrearubeis.wash_up;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by andrearubeis on 15/12/17.
 */

public class TaskAsincrono extends AsyncTask {

    private Context context;
    private TaskCompleted callBack = null;
    private Persona search;
    private URL url;

    //private final String domain_url = "http://192.168.0.24/";   //dominio portatile
    //private final String domain_url = "http://192.168.1.100/";  //dominio fisso
    //private final String domain_url = "http://rubeisandrea.myqnapcloud.com/"; //domanio nas
    //private final String domain_url = "http://washit.dek4.net/";  //dominio server


    public TaskAsincrono (Context contesto, URL url , TaskCompleted delegate ) {
        this.context = contesto;
        this.callBack = delegate;
        this.url = url;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        callBack.onTaskComplete(o);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    /**
     *
     * @param objects
     * @return risposta http ricevuta dal db eseguita in background
     */
    @Override
    protected Object doInBackground(Object[] objects)  {

        URL url = this.url;
        HttpURLConnection urlConnection = null;
        InputStream in = null;
        //Log.d("AsyncTask" , "Sono dentro al doInBackground");
        try {
            //url = new URL(domain_url + "login.php?usr=" + this.search.getMail() + "&psw=" + this.search.getPassword());
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());
        }

        catch (IOException e) {
            String err_response = "Connection to DB failed.";
            in = new ByteArrayInputStream(err_response.getBytes());
        }

        finally {
            urlConnection.disconnect();
        }
        return in;
    }




}
