package com.example.andrearubeis.wash_up;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        CheckBox cb = vi.findViewById(R.id.row_checkable_compito_check_box);
        cb.setText(data.get(position).getDescrizione());
        if(data.get(position).getSvolto() == 1) {
            cb.setChecked(true);
        }


        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

               updateCompito(data.get(position).getId(),isChecked);

            }
        });

        return vi;

    }



    public void updateCompito(String id_compito , final Boolean checkStatus) {
        URL url=null;
        Globals g = Globals.getInstance();
        int flag= -1;
        if(checkStatus == true) {
            flag = 1;
        }else{
            flag= 0;
        }

        try {
            String url_temp = g.getDomain() + "update_compito_svolto.php?id_compito="+id_compito+"&stato="+flag;

            url = new URL(url_temp);
        }catch(IOException e){
            Toast.makeText(context,"Creazione URL non riuscita",Toast.LENGTH_SHORT).show();
        }

        try {
            new TaskAsincrono(context, url , new TaskCompleted() {
                @Override
                public void onTaskComplete(Object resp) {

                    if(checkStatus == true) {

                        Toast.makeText(context, "Il compito é stato segnato come svolto", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Il compito é stato segnato come non svolto", Toast.LENGTH_SHORT).show();

                    }




                }
            }).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
