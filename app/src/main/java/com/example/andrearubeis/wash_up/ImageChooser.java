package com.example.andrearubeis.wash_up;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.res.Resources;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Resource;


public class ImageChooser {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE_REQUEST = 2;

    Context context;
    Activity activity;

    public ImageChooser (Context ctx, Activity act) {
        this.context = ctx;
        this.activity = act;
    }

    public void selectImage() {
        final CharSequence[] items = { context.getString(R.string.scatta_foto), context.getString(R.string.scegli_galleria), context.getString(R.string.annulla) };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.aggiungi_foto_profilo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if(items[item].equals(context.getString(R.string.scegli_galleria))) {
                    showFileChooser();
                }

                if(items[item].equals(context.getString(R.string.scatta_foto))) {
                    dispatchTakePictureIntent();
                }

                if (items[item].equals(context.getString(R.string.annulla))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void noImageSelected(int flag_image) {
        final CharSequence[] items = { context.getString(R.string.ok)};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.attenzione));
        if (flag_image == 0) {
            builder.setMessage(R.string.manca_immagine);
        }else{
            builder.setMessage(R.string.no_campi_vuoti);
        }
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.show();
    }



    /*public void selectImageRoom() {
        final CharSequence[] items = { context.getString(R.string.scegli_galleria), context.getString(R.string.annulla) };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.aggiungi_foto_profilo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if(items[item].equals(context.getString(R.string.scegli_galleria))) {
                    showFileChooser();
                    //Toast.makeText(context, "hai cliccato scegli galleria", Toast.LENGTH_SHORT).show();
                }

                if (items[item].equals(context.getString(R.string.annulla))) {
                    dialog.dismiss();
                    //Toast.makeText(context, "hai cliccato annulla", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), "hai cliccato su un elemento del dialog", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }*/

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

}
