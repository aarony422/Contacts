package com.example.aarony.contacts;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ArrayAdapter<String> contactAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // fake contact data
        String[] contactArray = {
                "Aaron Young -- 7348343820",
                "Ruben Ulloa -- 7739640497",
                "Aakash Patel -- 5555555555",
                "Meghan McCreary -- 5555555555",
                "Aaron Young -- 7348343820",
                "Ruben Ulloa -- 7739640497",
                "Aakash Patel -- 5555555555",
                "Meghan McCreary -- 5555555555",
                "Aaron Young -- 7348343820",
                "Ruben Ulloa -- 7739640497",
                "Aakash Patel -- 5555555555",
                "Meghan McCreary -- 5555555555",
                "Aaron Young -- 7348343820",
                "Ruben Ulloa -- 7739640497",
                "Aakash Patel -- 5555555555",
                "Meghan McCreary -- 5555555555",
                "Aaron Young -- 7348343820",
                "Ruben Ulloa -- 7739640497",
                "Aakash Patel -- 5555555555",
                "Meghan McCreary -- 5555555555"
        };

        List<String> contactList = new ArrayList<String>(Arrays.asList(contactArray));

        contactAdapter = new ArrayAdapter<String>(
                // current context
                getActivity(),
                // ID of list item layout
                R.layout.list_item_contacts,
                // id of textview to populate
                R.id.list_item_contacts_textview,
                // Data
                contactList
        );

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_contact);
        listView.setAdapter(contactAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String contact = contactAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, contact);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
