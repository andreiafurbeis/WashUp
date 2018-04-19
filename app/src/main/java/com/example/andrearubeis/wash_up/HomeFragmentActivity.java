package com.example.andrearubeis.wash_up;


        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.drawable.BitmapDrawable;
        import android.graphics.drawable.Drawable;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.Toast;

        import com.example.andrearubeis.wash_up.R;

        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.io.BufferedInputStream;
        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.Vector;

public class HomeFragmentActivity extends Fragment {


    View rootView;
    LinearLayout linear;
    ListView listview;
    Persona temp_persona;



    public static HomeFragmentActivity newInstance() {
        HomeFragmentActivity fragment = new HomeFragmentActivity();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        Log.d("precedenza" , "Sono dentro all'OnCreate");


    }

    @Override
    public void onResume() {
        super.onResume();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //PROVA


        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        listview = (ListView) rootView.findViewById(R.id.fragment_home_listview);

        //Find the layout with the id you gave in on the xml
        linear = (LinearLayout) rootView.findViewById(R.id.fragment_home_linear);

        ArrayList<Stanza> vectorStanze = getArguments().getParcelableArrayList("stanze");

        if(vectorStanze == null) {
            Toast.makeText(getActivity(),"Home Fragment : Il vettore delle stanze è NULL" ,Toast.LENGTH_SHORT).show();

        }else{
            Log.d("HomeFragmentActivity" , vectorStanze.toString());
        }

        creaInterfaccia(vectorStanze);


        return rootView;

    }





    public void creaInterfaccia(ArrayList<Stanza> vectorStanze){

        if(vectorStanze == null) {
            Toast.makeText(getActivity(),"Home Fragment : Il vettore delle stanze è NULL" ,Toast.LENGTH_SHORT).show();

        }


        if(vectorStanze != null ) {
            Log.d("HomeFragment" , "Il vector ha : " + vectorStanze.size() + " elementi");
        }else{
            Log.d("HomeFragment" , "Il vector é NULL");
        }


        AdapterStanze adapter = new AdapterStanze(getActivity(), vectorStanze);


        if( adapter == null ) {
            Log.d("HomeFragment" , "L'adapter é NULL");
        }

        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }


}