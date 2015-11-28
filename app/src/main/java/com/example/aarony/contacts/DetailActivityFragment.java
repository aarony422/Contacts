package com.example.aarony.contacts;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
            ((TextView) rootView.findViewById(R.id.detail_name_textview)).setText("Name: " + contact.name);
            ((TextView) rootView.findViewById(R.id.detail_company_textview)).setText("Company: " + contact.company);
        }
        return rootView;
    }
}
