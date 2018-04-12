package com.example.andrearubeis.wash_up;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterCompiti extends ArrayAdapter<Compito>{

        Context context;
        ArrayList<Compito> data;
        private static LayoutInflater inflater = null;

        public AdapterCompiti(Context context, ArrayList<Compito> data) {
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
                vi = inflater.inflate(R.layout.row_compito, parent ,false);
            }
            //TextView text = (TextView) vi.findViewById(R.id.text);
            Button row = (Button) vi.findViewById(R.id.row_compito_descrizione);
            row.setText("-  " + data.get(position).getDescrizione());
            return vi;
        }
}
