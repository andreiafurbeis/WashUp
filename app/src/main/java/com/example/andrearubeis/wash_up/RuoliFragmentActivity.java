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
        list_view = (ListView) vista.findViewById(R.id.fragment_ruoli_list_view);


        pref = getActivity().getSharedPreferences("persona", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = pref.getString("persona", "");
        temp_persona = gson.fromJson(json, Persona.class);


        /*if(temp_persona == null) {
            Log.d("RuoliFragment" , "L'oggetto appena scaricato dalle SharedPreference é NULL");
        }*/

        //DEVE ARRIVARE UN ARRAYLIST DI PERSONE

        creaInterfaccia();

        return vista;
    }

    /**
     * Popola la ListView di FragmentRuoli
     */
    private void creaInterfaccia() {
        ArrayList<Persona> coinquilini = temp_persona.getCoinquilini();
        if(coinquilini == null) {
            coinquilini = new ArrayList<Persona>();
            //Log.d("RuoliFragment" , "Il vettore coinquilini é null");

        }
        Persona loggata = new Persona(temp_persona.getNome() , temp_persona.getCognome() , temp_persona.getMail() , temp_persona.getProfileImage() , temp_persona.getIdHome() , null);
        loggata.setCompiti(temp_persona.getCompiti());
        /*if(temp_persona.getCompiti() == null) {
            Log.d("RuoliFragment" , "Il vettore compiti della persona loggata é null");
        }*/
        //coinquilini.add(loggata);
        if(coinquilini.size() != 0) {
            coinquilini.add(coinquilini.get(0)); //aggiunge l' inquilino loggato come primo membro della lista in modo da trovarlo in cima , faccio il cambio di posizione in modo da non alzare troppo la complessità
            coinquilini.set(0,loggata);
        }else{
            coinquilini.add(loggata);
        }



        AdapterRuoli adapter = new AdapterRuoli(getActivity() ,coinquilini);

        list_view.setAdapter(adapter);
        Utility.setListViewHeightBasedOnChildren(list_view , getArguments().getInt("bottom_height"));
    }



}