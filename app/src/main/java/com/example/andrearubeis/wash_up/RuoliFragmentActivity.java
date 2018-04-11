package com.example.andrearubeis.wash_up;


        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v7.app.AlertDialog;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;

        import com.example.andrearubeis.wash_up.R;

public class RuoliFragmentActivity extends Fragment {

    //Button add_compito;
    View rootView;


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

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        //add_compito = rootView.findViewById(R.id.fragment_ruoli_aggiungi_compito);


        /*add_compito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });*/

        return inflater.inflate(R.layout.fragment_ruoli, container, false);
    }



}