package com.example.aarony.contacts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;

public class ImageLoader extends AsyncTask<Object, String, Bitmap> {

    private View view;
    private Bitmap bitmap = null;


    @Override
    protected Bitmap doInBackground(Object... params) {
        view = (View) params[0];
        String uri = (String)params[1];
        try {
            InputStream in = new java.net.URL(uri).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null && view != null) {
            ImageView profilepic = (ImageView) view.findViewById(R.id.list_item_profile);
            profilepic.setImageBitmap(bitmap);
        }
    }
}
