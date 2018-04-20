package com.example.andrearubeis.wash_up;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class AdapterRuoli extends ArrayAdapter<Persona> {

    Context context;
    ArrayList<Persona> data;
    private static LayoutInflater inflater = null;

    public AdapterRuoli(Context context, ArrayList<Persona> data) {
        super(context, R.layout.item_persona , data);
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
       return data.size();
    }

    @Override
    public Persona getItem(int position) {
        return  data.get(position);
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        View rowView = convertView;

        if(rowView == null){
            rowView = inflater.inflate(R.layout.item_persona, parent, false);

        }
        ImageView image_inquilino = (ImageView) rowView.findViewById(R.id.item_persona_imageview);
        Button campanella = (Button) rowView.findViewById(R.id.item_persona_button_bell);
        TextView name_inquilino = (TextView) rowView.findViewById(R.id.item_persona_text_view_nome);
        name_inquilino.setText(data.get(position).getNome());

        Log.d("AdapterRuoli","il link dell'immagine é : " + data.get(position).getProfileImage());


        Picasso.get().load(data.get(position).getProfileImage()).into(image_inquilino,new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {


            }

            @Override
            public void onError(Exception e) {

            }

        });

        if(data.get(position).getCompiti() != null) {
            Log.d("AdapterRuoli", "INFO COMPITI : " + data.get(position).getCompiti().toString());
        }else{
            Log.d("AdapterRuoli", "L'utente " + data.get(position).getMail() + "ha il vettore compiti NULL");

        }

        campanella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AdapterRuoli","e stata cliccata la campanella");
                ArrayList<Persona> coinquilini = data.get(position).getCoinquilini();
                String mail_corrente = data.get(position).getMail();
                Log.d("AdapterRuoli","e stata cliccata la campanella");
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { mail_corrente });
                intent.putExtra(Intent.EXTRA_SUBJECT, "REFFATI");
                intent.putExtra(Intent.EXTRA_TEXT, "Devi pulire, controlla su WashUp");
                context.startActivity(Intent.createChooser(intent, ""));
            }
        });

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AdapterRuoli" , "e stato cliccato un bottone");
                Log.d("AdapterRuoli", "INFO COMPITI : " + data.get(position).getCompiti().toString());
                Intent intent = new Intent(context, RuoliInquilino.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("persona" , data.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });

        return rowView;

    }



}
