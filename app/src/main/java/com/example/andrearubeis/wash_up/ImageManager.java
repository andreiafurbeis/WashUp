package com.example.andrearubeis.wash_up;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by andrearubeis on 01/04/18.
 */

public class ImageManager {

    private Context context;
    private String image_path;
    private String image_name;
    private Bitmap image;
    private Uri image_uri;

    public ImageManager(Context ctx) {

        this.context = ctx;

    }

    public void imageManager(Bitmap image) {


        String partFileName = currentDateFormat();

        String path = saveToInternalStorage(image , partFileName);


        setImagePath(path);

        //Log.d("Immagine" , "il path dell' immagine è : " + path);

        //Toast.makeText(this.context,"Siamo dentro all'Activity Result",Toast.LENGTH_LONG).show();

        String storeFileName = "photo_" + partFileName + ".jpg";

        setImageName(storeFileName);

        Bitmap immagine_da_utilizzare = loadImageFromStorage(path , storeFileName);

        setImageBitmap(immagine_da_utilizzare);

       // Log.d("Immagine" , "Il nomde dell' immagine è :  " + storeFileName);

    }

    public Bitmap loadImageFromStorage(String path , String name)
    {

        Bitmap b= null;

        try {
            File f=new File(path, name);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return b;
    }



    private String saveToInternalStorage(Bitmap bitmapImage , String name){
        ContextWrapper cw = new ContextWrapper(this.context);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath=new File(directory,"photo_" + name + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public Uri getImageUri() {
        return this.image_uri;
    }

    public void setImageUri(Uri new_image_uri) {
        this.image_uri = new_image_uri;
    }

    public String getImagePath() {
        return this.image_path;
    }

    public void setImagePath(String new_path) {
        this.image_path = new_path;
    }

    public Bitmap getImageBitmap() {
        return this.image;
    }

    public void setImageBitmap(Bitmap image_bitmap) {
        this.image = image_bitmap;
    }

    public String getImageName() {
        return this.image_name;
    }

    public void setImageName(String new_name) {
        this.image_name = new_name;
    }



    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }


}
