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


    //Vector<Stanza> vectorStanze;
    String stringaJSON;
    //LinearLayout configurazione_linear;
    Button buttonStanza[];
    View rootView;
    LinearLayout linear;
    ListView listview;



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

        //Globals g = Globals.getInstance();

        ArrayList<Stanza> vectorStanze = getArguments().getParcelableArrayList("stanze");

        if(vectorStanze == null) {
            Toast.makeText(getActivity(),"Home Fragment : Il vettore delle stanze è NULL" ,Toast.LENGTH_SHORT).show();

        }else{
            Log.d("HomeFragmentActivity" , vectorStanze.toString());
        }

        creaInterfaccia(vectorStanze);


        return rootView;








        //return inflater.inflate(R.layout.fragment_home, container, false);

        //return g.getVista();

    }







    //CREAZIONE DI TUTTI I BUTTON IN MODO DINAMICO , CON ANCHE L'ASSEGNAMENTO DEGLI ONCLICK
    /*public void creaInterfaccia(LinearLayout layout , ArrayList<Stanza> vectorStanze){






        //Globals g = Globals.getInstance();
        //configurazione_linear = (LinearLayout) vista.findViewById(R.id.fragment_home_linear);


        if(vectorStanze == null) {
            Toast.makeText(getActivity(),"Home Fragment : Il vettore delle stanze è NULL" ,Toast.LENGTH_SHORT).show();

        }

        buttonStanza = new Button[vectorStanze.size()];
        Drawable image_drawable = null;



        for( int i=0; i < vectorStanze.size(); i++){
            //buttonStanza[i] = new Button(getApplicationContext());
            buttonStanza[i] = new Button(getActivity());
            //final Stanza stanzaCorrente = vectorStanze.elementAt(i);
            final Stanza stanzaCorrente = vectorStanze.get(i);
            buttonStanza[i].setText(stanzaCorrente.getName());
            //BitmapDrawable drawableCorrente = new BitmapDrawable(getApplicationContext().getResources(),stanzaCorrente.getImage());
            String image = stanzaCorrente.getImage();
            String [] image_path = image.split(" ");

            Log.d("immagine","Home_Fragment : La stringa dell'immagine é : " + image);

            if(image_path[0].equals("D")){

                image_drawable = getResources().getDrawable(Integer.parseInt(image_path[1]));

            }else{

                //Toast.makeText(getApplicationContext(),"Sto mettendo l'immagine con il metodo nuovo" ,Toast.LENGTH_SHORT).show();
                //ImageManager manager = new ImageManager(getApplicationContext());
                ImageManager manager = new ImageManager(getActivity());

                Bitmap image_bitmap = manager.loadImageFromStorage(image_path[1],image_path[2]);
                //Bitmap image_bitmap_scaled = Bitmap.createScaledBitmap(image_bitmap , ViewGroup.LayoutParams.MATCH_PARENT , getResources().getDisplayMetrics().heightPixels/6,true);
                image_drawable = new BitmapDrawable(getResources() , image_bitmap);

            }



            buttonStanza[i].setBackground(image_drawable);


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getResources().getDisplayMetrics().heightPixels/6);



            //params.height = 50;
            //buttonStanza[i].setLayoutParams(params);

            //buttonStanza[i].setHeight(mGestureThreshold);
            buttonStanza[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Globals g = Globals.getInstance();
                    Intent intent = new Intent(getActivity(),
                            AggiungiStanzaActivity.class);
                    intent.putExtra("nome_stanza",stanzaCorrente.getName());
                    intent.putExtra("home_id",g.getIdString());
                    intent.putExtra("update",1);
                    startActivity(intent);
                }
            });




            //buttonStanza[i].setTypeface(Typeface.createFromAsset(getAssets(),loveloBlack));

            layout.addView(buttonStanza[i], params);


        }*/


        public void creaInterfaccia(ArrayList<Stanza> vectorStanze){





            //Globals g = Globals.getInstance();
            //configurazione_linear = (LinearLayout) vista.findViewById(R.id.fragment_home_linear);


            if(vectorStanze == null) {
                Toast.makeText(getActivity(),"Home Fragment : Il vettore delle stanze è NULL" ,Toast.LENGTH_SHORT).show();

            }


            if(vectorStanze != null ) {
                Log.d("ConfigurazioneStanze" , "Il vector ha : " + vectorStanze.size() + " elementi");
            }else{
                Log.d("ConfigurazioneStanze" , "Il vector é NULL");
            }


            AdapterStanze adapter = new AdapterStanze(getActivity(), vectorStanze);


            if(adapter == null ) {
                Log.d("ConfigurazioneStanze" , "L'adapter é NULL");
            }

            listview.setAdapter(adapter);
            adapter.notifyDataSetChanged();


            /*

            buttonStanza = new Button[vectorStanze.size()];
            Drawable image_drawable = null;



            for( int i=0; i < vectorStanze.size(); i++){
                //buttonStanza[i] = new Button(getApplicationContext());
                buttonStanza[i] = new Button(getActivity());
                //final Stanza stanzaCorrente = vectorStanze.elementAt(i);
                final Stanza stanzaCorrente = vectorStanze.get(i);
                buttonStanza[i].setText(stanzaCorrente.getNameStanza());
                //BitmapDrawable drawableCorrente = new BitmapDrawable(getApplicationContext().getResources(),stanzaCorrente.getImage());
                String image = stanzaCorrente.getImageStanza();
                String [] image_path = image.split(" ");

                Log.d("immagine","Home_Fragment : La stringa dell'immagine é : " + image);

                if(image_path[0].equals("D")){

                    image_drawable = getResources().getDrawable(Integer.parseInt(image_path[1]));

                }else{

                    //Toast.makeText(getApplicationContext(),"Sto mettendo l'immagine con il metodo nuovo" ,Toast.LENGTH_SHORT).show();
                    //ImageManager manager = new ImageManager(getApplicationContext());
                    ImageManager manager = new ImageManager(getActivity());

                    Bitmap image_bitmap = manager.loadImageFromStorage(image_path[1],image_path[2]);
                    //Bitmap image_bitmap_scaled = Bitmap.createScaledBitmap(image_bitmap , ViewGroup.LayoutParams.MATCH_PARENT , getResources().getDisplayMetrics().heightPixels/6,true);
                    image_drawable = new BitmapDrawable(getResources() , image_bitmap);

                }



                buttonStanza[i].setBackground(image_drawable);


                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getResources().getDisplayMetrics().heightPixels/6);



                //params.height = 50;
                //buttonStanza[i].setLayoutParams(params);

                //buttonStanza[i].setHeight(mGestureThreshold);
                buttonStanza[i].setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Globals g = Globals.getInstance();
                        Intent intent = new Intent(getActivity(),
                                RuoliStanza.class);
                        intent.putExtra("nome_stanza",stanzaCorrente.getNameStanza());
                        intent.putExtra("image_stanza",stanzaCorrente.getImageStanza());
                        intent.putExtra("home_id",g.getIdString());
                        startActivity(intent);
                    }
                });




                //buttonStanza[i].setTypeface(Typeface.createFromAsset(getAssets(),loveloBlack));

                linear.addView(buttonStanza[i], params);


            }*/



        }


}