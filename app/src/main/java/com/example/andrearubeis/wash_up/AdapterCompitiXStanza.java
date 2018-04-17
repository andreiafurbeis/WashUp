package com.example.andrearubeis.wash_up;

/**
 * Created by nicolo on 12/04/18.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import android.content.Context;
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
import android.widget.TextView;

import java.util.ArrayList;
/**
 * Created by nicolo on 12/04/18.
 */






public class AdapterCompitiXStanza extends ArrayAdapter<Compito> {

    Context context;
    ArrayList<Compito> data;

    private static LayoutInflater inflater = null;

    public AdapterCompitiXStanza(Context context, ArrayList<Compito> data ) {
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
        View vi = convertView;
        if (vi == null){
            vi = inflater.inflate(R.layout.row_checkable_compito, null);
        }
        CheckBox cb = vi.findViewById(R.id.row_checkable_compito_check_box);
        cb.setText(data.get(position).getDescrizione());


        return vi;
    }
}
