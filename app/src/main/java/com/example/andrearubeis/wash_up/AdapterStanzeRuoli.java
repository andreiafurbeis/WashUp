package com.example.andrearubeis.wash_up;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
/**
 * Created by nicolo on 12/04/18.
 */






public class AdapterStanzeRuoli extends ArrayAdapter<Compito> {

    Context context;
    ArrayList<Compito> data;


    private static LayoutInflater inflater = null;

    public AdapterStanzeRuoli(Context context, ArrayList<Compito> data ) {
        super(context , 0 , data );
        this.context = context;
        this.data = data;


        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Compito getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public int getItemViewType(int position) {
        if(data.get(position).getStanza()==null) {  //stanza contiene in questo caso l'immagine della stanza
            return 1; //la riga rappresenta un compito
        }
        return 0; //la riga rappresenta il nome della stanza
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(this.getItemViewType(position) == 0) {
            if (vi == null) {
                vi = inflater.inflate(R.layout.row_stanza, parent, false);
            }
            TextView title = vi.findViewById(R.id.row_stanza_name);
            ImageView image = vi.findViewById(R.id.row_stanza_image);
            Picasso.get().load(data.get(position).getStanza()).into(image , new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {


                }

                @Override
                public void onError(Exception e) {

                }

            });
            Log.d("AdapterStanzeRuoli", "sono nel getview di " + data.get(position).getId_casa());
            title.setText(data.get(position).getId_casa());
            return vi;
        }
        if (vi == null){
            vi = inflater.inflate(R.layout.row_checkable_compito, parent, false);
        }
        Log.d("AdapterCompitiXStanza" , "sto aggiungendo alla listview il compito : " + data.get(position).getDescrizione()); //id_casa in questo caso contiene la descrizione del compito
        CheckBox cb = vi.findViewById(R.id.row_checkable_compito_check_box);
        cb.setText(data.get(position).getDescrizione());

        return vi;

    }
}
