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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterStanze extends ArrayAdapter<Stanza> {

    Context context;
    ArrayList<Stanza> data;
    private static LayoutInflater inflater = null;

    public AdapterStanze(Context context, ArrayList<Stanza> data) {
        super(context, R.layout.row_stanza , data);
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Stanza getItem(int position) {
        return  data.get(position);
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if(rowView == null){
            rowView = inflater.inflate(R.layout.row_stanza, parent, false);

        }

        TextView nome_stanza = (TextView) rowView.findViewById(R.id.row_stanza_name);

        nome_stanza.setText(data.get(position).getNameStanza());

        ImageView image_stanza = (ImageView) rowView.findViewById(R.id.row_stanza_image);

        Picasso.get().load(data.get(position).getImageStanza()).fit().centerCrop().into(image_stanza,new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {


            }

            @Override
            public void onError(Exception e) {

            }

        });





        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!context.getClass().getName().equals("com.example.andrearubeis.wash_up.bottom_activity") ) {
                    Globals g = Globals.getInstance();
                    Intent intent = new Intent(context,
                            AggiungiStanzaActivity.class);
                    Bundle bundle = new Bundle();
                    Log.d("AdapterStanze" , "Sono dentro all'OnClick della ListView di ConfigurazioneStanze");
                    bundle.putString("nome_stanza",data.get(position).getNameStanza());
                    bundle.putString("home_id",g.getIdString());
                    bundle.putInt("update",1);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    ((Activity)context).finish();

                } else {
                    Globals g = Globals.getInstance();
                    Intent intent = new Intent(context,
                            RuoliStanza.class);
                    Log.d("AdapterStanze" , "Sono dentro all'OnClick della ListView di HomeFragment");

                    Bundle bundle = new Bundle();
                    Log.d("AdapterStanze","il vettore compiti di " + data.get(position).getNameStanza() + " ha " + data.get(position).getCompiti().size());
                    bundle.putParcelable("stanza",data.get(position));
                    bundle.putString("home_id",g.getIdString());
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }


            }
        });

        return rowView;

    }





}
