package com.example.aarony.contacts;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    public void onStart() {
        super.onStart();
        updateContacts() ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        contactAdapter = new ArrayAdapter<String>(
                // current context
                getActivity(),
                // ID of list item layout
                R.layout.list_item_contacts,
                // id of textview to populate
                R.id.list_item_contacts_textview,
                // Data
                new ArrayList<String>()
        );

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // attach adapter to the listview
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

    private void updateContacts() {
        FetchContactsTask FCT = new FetchContactsTask();
        FCT.execute();
    }

    public class FetchContactsTask extends AsyncTask<String, Void, String[]> {
        private final String LOG_TAG = FetchContactsTask.class.getSimpleName();

        @Override
        protected void onPostExecute(String[] results) {
            if (results != null) {
                contactAdapter.clear();
                for (String contactstr : results) {
                    contactAdapter.add(contactstr);
                }
            }
        }

        private String[] getContactDataFromJson(String contactJsonStr) throws JSONException{

            JSONArray contacts = new JSONArray(contactJsonStr);
            String[] rtncontact = new String[contacts.length()];
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject contact = contacts.getJSONObject(i);
                String name = contact.getString("name");
                String bd = contact.getString("birthdate");

                rtncontact[i] = new String(name + "--" + bd);
            }
            for (String s: rtncontact) {
                Log.v(LOG_TAG, "contact entry: " + s);
            }

            return rtncontact;
        }



        @Override
        protected String[] doInBackground(String... params) {
            // get JSON data from https://solstice.applauncher.com/external/contacts.json

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Store JSON response as a string
            String contactJsonStr = null;

            try {
                // create the URL for query
                String solURL = "https://solstice.applauncher.com/external/contacts.json";
                URL url = new URL(solURL);

                // create http GET request
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into string
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // stream was empty.
                    return null;
                }
                contactJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Contact JSON String: " + contactJsonStr);
            } catch (IOException e) {
                Log.e("MainActivity Fragment", "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("MainActivity Fragment", "Error closing stream", e);
                    }
                }
            }

            try {
                return getContactDataFromJson(contactJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }
    }
}
