package com.example.aarony.contacts;

/**
 * Created by aarony on 11/28/15.
 */
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<Contact> {
    public ContactAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Contact contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_contacts, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.list_item_name_textview);
        TextView tvWphone = (TextView) convertView.findViewById(R.id.list_item_Wphone_textview);

        // Populate the data into the template view using the data object
        tvName.setText(contact.name);
        tvWphone.setText(contact.Wphone);
        ImageLoader IL = (ImageLoader) new ImageLoader(convertView, getContext(), contact, false).execute(convertView, contact.smallImageURL);

        /*
        // check if image is already in database
        DBHandler dbHandler = new DBHandler(getContext(), null, null, 1);
        byte[] byteArr = dbHandler.getImg(contact.employeeID, false);

        if (byteArr == null) {
            ImageLoader IL = (ImageLoader) new ImageLoader(convertView, getContext(), contact, false).execute(convertView, contact.smallImageURL);
        } else {
            ImageView profilepic = (ImageView) convertView.findViewById(R.id.list_item_profile);
            profilepic.setImageBitmap(BitmapFactory.decodeByteArray(byteArr, 0, byteArr.length));
        }
        */

        // Return the completed view to render on screen
        return convertView;
    }
}

