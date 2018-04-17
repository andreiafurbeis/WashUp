package com.example.andrearubeis.wash_up;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by nicolo on 13/04/18.
 */







public class AdapterRuoliStanza extends ArrayAdapter<Compito> {

    Context context;
    ArrayList<Compito> data;

    private static LayoutInflater inflater = null;

    public AdapterRuoliStanza(Context context, ArrayList<Compito> data ) {
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = inflater.inflate(R.layout.row_ruoli_x_stanza, null);
        }

        TextView tv = (TextView) view.findViewById(R.id.row_ruoli_stanza_text_view_nome);
        tv.setText(data.get(position).getDescrizione());
        //tv.setCompoundDrawablesWithIntrinsicBounds(getDrawable(data.get(position).getStanza()),null,null,null);
        ImageView iv = (ImageView)  view.findViewById(R.id.row_ruoli_stanza_imageview);


        Picasso.get().load(data.get(position).getStanza()).into(iv,new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {


            }

            @Override
            public void onError(Exception e) {

            }

        });


        return view;
    }




}






