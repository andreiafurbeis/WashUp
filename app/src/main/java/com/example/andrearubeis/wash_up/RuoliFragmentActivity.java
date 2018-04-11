package com.example.andrearubeis.wash_up;


        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.Bitmap;
        import android.graphics.drawable.BitmapDrawable;
        import android.graphics.drawable.Drawable;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;

        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.Toast;

        import android.widget.Button;
        import com.example.andrearubeis.wash_up.R;
        import com.google.gson.Gson;

        import java.util.ArrayList;

        import static android.content.Context.MODE_PRIVATE;

public class RuoliFragmentActivity extends Fragment {

    ImageView image;
    Context context;
    View vista;
    String profile_image;
    Button inquilino1;
    ListView list_view;
    ArrayList<Persona> vettorepersona;
    Button title_bar;
    SharedPreferences pref;
    Persona temp_persona;


    public static RuoliFragmentActivity newInstance() {
        RuoliFragmentActivity fragment = new RuoliFragmentActivity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_ruoli, container, false);


        //Persona persona = getArguments().getParcelable("persona");

        pref = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = pref.getString("persona", "");
        temp_persona = gson.fromJson(json, Persona.class);

        if(temp_persona == null) {
            Log.d("ConfigurazioneStanze" , "L'oggetto appena scaricato dalle SharedPreference é NULL");
        }

        //View view = getView();


        /*String var1 ;
        var1 = persona.getNome();

        // title_bar = vista.findViewById(R.id.fragment_ruoli_title_bar);
        //title_bar.setText(var1);


        String profile_image = persona.getProfileImage();
        vettorepersona = new ArrayList<Persona>();
        vettorepersona.add(persona);
        vettorepersona.add(persona);
        vettorepersona.add(persona);
        vettorepersona.add(persona);
        vettorepersona.add(persona);
        vettorepersona.add(persona);
        vettorepersona.add(persona);
        vettorepersona.add(persona);


        */


        //DEVE ARRIVARE UN ARRAYLIST DI PERSONE






        list_view = (ListView) vista.findViewById(R.id.fragment_ruoli_list_view);
        ArrayList<Persona> coinquilini = temp_persona.getCoinquilini();
        coinquilini.add(new Persona(temp_persona.getNome() , temp_persona.getCognome() , temp_persona.getMail() , temp_persona.getProfileImage() , temp_persona.getIdHome() , null));


        AdapterRuoli adapter = new AdapterRuoli(getActivity(),coinquilini);

        list_view.setAdapter(adapter);


        /*list_view.setOnItemClickListener(listView.OnListItemClick() {

        });*/




        return vista;
    }


      /*public void onActivityCreated(Bundle savedInstanceState){

        super.onActivityCreated(savedInstanceState);
        context = getActivity();


        Persona persona = getArguments().getParcelable("persona");

        View view = getView();


        String var1 ;
        var1 = persona.getNome();

       // title_bar = vista.findViewById(R.id.fragment_ruoli_title_bar);
        //title_bar.setText(var1);


        String profile_image = persona.getProfileImage();
       vettorepersona = new ArrayList<Persona>();
       vettorepersona.add(persona);
       vettorepersona.add(persona);
        vettorepersona.add(persona);
        vettorepersona.add(persona);
        vettorepersona.add(persona);
        vettorepersona.add(persona);
        vettorepersona.add(persona);
        vettorepersona.add(persona);












        listview = (ListView) vista.findViewById(R.id.fragment_ruoli_list_view);
          AdapterRuoli adapter = new AdapterRuoli(getActivity(),vettorepersona);
        listview.setAdapter(adapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("RuoliFragment" , "e stato cliccato un bottone");
            }
        });*/

        /*listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                Intent intent = new Intent(getActivity(), RuoliInquilino.class);
                Bundle bundle = new Bundle();
                intent.putExtra("nome_persona",vettorepersona.get(position) );
                Log.d("log2","SONO IN RUOLIFRAGMENTACTIVITY,DEVO LANCIARE STARTACTIVITY(intent)");
                startActivity(intent);

                Log.d("log2","SONO IN RUOLIFRAGMENTACTIVITY,STARTACTIVITY(intent) ");

                 return;


            }
        });*/
















    //}


    /*public Drawable getDrawable(String image) {
        Drawable image_drawable;
        String[] image_path = image.split(" ");


        Toast.makeText(getActivity(), "Sto mettendo l'immagine con il metodo nuovo", Toast.LENGTH_SHORT).show();
        ImageManager manager = new ImageManager(getActivity());
        Bitmap image_bitmap = manager.loadImageFromStorage(image_path[0], image_path[1]);
        Log.d("OptionFragment" , "il path è : " + image_path[0] + "   " + image_path[1]);
        Bitmap image_bitmap_scaled = Bitmap.createScaledBitmap(image_bitmap , ViewGroup.LayoutParams.MATCH_PARENT , getResources().getDisplayMetrics().heightPixels/6,true);
        image_drawable = new BitmapDrawable(getResources(), image_bitmap);


        return image_drawable;
    }*/



}