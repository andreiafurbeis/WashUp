package com.example.andrearubeis.wash_up;

import android.content.res.Resources;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utility {

    /**
     * Classe utilizzata per rendere l' interfaccia più scalabile con le varie risoluzioni
     *
     */


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView , int bottom_nav_height) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        View listItem = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        if(bottom_nav_height == 0) {
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + bottom_nav_height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void setButtonHeight(Button button , int bottom_nav_height , int upper_object) {

        ViewGroup.LayoutParams params = button.getLayoutParams();
        int display_height_for_button = Resources.getSystem().getDisplayMetrics().heightPixels - bottom_nav_height - upper_object;
        int button_height = display_height_for_button/7;
        params.height = button_height;
        button.setLayoutParams(params);
        Log.d("Utility","l'altezza del display è : " + button_height + "bottom : " + bottom_nav_height + "upper_object :" + upper_object);

    }
}