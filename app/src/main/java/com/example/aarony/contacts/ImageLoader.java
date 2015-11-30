package com.example.aarony.contacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ImageLoader extends AsyncTask<Object, String, Bitmap> {

    private View view;
    private Bitmap bitmap = null;
    public View rootview;
    private Context mycontext;
    private Contact mycontact;
    private Boolean large;

    public ImageLoader(View rootview, Context context, Contact contact, Boolean large) {
        this.rootview = rootview;
        this.mycontext = context;
        this.mycontact = contact;
        this.large = large;
    }

    public ImageLoader() {
        super();
    }

    @Override
    protected Bitmap doInBackground(Object... params) {
        view = (View) params[0];
        String uri = (String)params[1];
        try {
            InputStream in = new java.net.URL(uri).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            //Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null && view != null) {
            ImageView profilepic = (ImageView) view.findViewById(R.id.list_item_profile);
            profilepic.setImageBitmap(bitmap);

            /*
            // put img in database
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            DBHandler dbHandler = new DBHandler(mycontext, null, null, 1);
            if (large) {
                dbHandler.addlarge(mycontact.employeeID, byteArray);
            } else {
                dbHandler.addsmall(mycontact.employeeID, byteArray);
            }
            */
        }
    }
}
