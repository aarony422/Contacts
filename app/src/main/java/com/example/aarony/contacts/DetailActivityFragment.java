package com.example.aarony.contacts;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        if (intent != null && intent.hasExtra("Contact")) {
            Contact contact = (Contact)intent.getSerializableExtra("Contact");
            ((TextView) rootView.findViewById(R.id.detail_name_textview)).setText(contact.name);
            ((TextView) rootView.findViewById(R.id.detail_company_textview)).setText(contact.company);
            ((TextView) rootView.findViewById(R.id.detail_Wphone_textview)).setText(contact.Wphone);
            ((TextView) rootView.findViewById(R.id.detail_Hphone_textview)).setText(contact.Hphone);
            ((TextView) rootView.findViewById(R.id.detail_Mphone_textview)).setText(contact.Mphone);

            DetailLoader DL = (DetailLoader) new DetailLoader(contact);

            // check if img is in database
            // check if image is already in database
            /*
            DBHandler dbHandler = new DBHandler(getContext(), null, null, 1);
            byte[] byteArr = dbHandler.getImg(contact.employeeID, false);

            if (byteArr == null) {
                ImageLoader IL = (ImageLoader) new ImageLoader(rootView, getActivity(), contact, true);
                DL.setNext(IL);
            } else {
                ImageView profilepic = (ImageView) rootView.findViewById(R.id.list_item_profile);
                profilepic.setImageBitmap(BitmapFactory.decodeByteArray(byteArr, 0, byteArr.length));
            }
            */
            ImageLoader IL = (ImageLoader) new ImageLoader(rootView, getActivity(), contact, true);
            DL.setNext(IL);
            DL.execute();

        }
        return rootView;
    }
}
