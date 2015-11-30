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
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ContactAdapter contactAdapter;

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

        ArrayList<Contact> contactArr = new ArrayList<Contact>();
        contactAdapter = new ContactAdapter(getActivity(), contactArr);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // attach adapter to the listview
        ListView listView = (ListView) rootView.findViewById(R.id.listview_contact);
        listView.setAdapter(contactAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Contact contact = contactAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("Contact", (Serializable) contact);
                startActivity(intent);
            }
        });


        return rootView;
    }

    private void updateContacts() {
        FetchContactsTask FCT = new FetchContactsTask();
        FCT.execute();
    }

    public class FetchContactsTask extends AsyncTask<String, Void, Contact[]> {
        private final String LOG_TAG = FetchContactsTask.class.getSimpleName();

        @Override
        protected void onPostExecute(Contact[] results) {
            if (results != null) {
                contactAdapter.clear();
                // add to database
                for (Contact contact : results) {
                    contactAdapter.add(contact);
                }
            }
        }

        private Contact[] getContactDataFromJson(String contactJsonStr) throws JSONException{

            JSONArray contacts = new JSONArray(contactJsonStr);
            Contact[] rtncontact = new Contact[contacts.length()];
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject contact = contacts.getJSONObject(i);
                String name = contact.getString("name");
                String company = contact.getString("company");
                String detailsURL = contact.getString("detailsURL");
                String smallImageURL = contact.getString("smallImageURL");
                String birthdate = contact.getString("birthdate");
                int employeeID = contact.getInt("employeeID");

                // get phone JSON object
                JSONObject phones = contact.getJSONObject("phone");
                String Wphone = (phones.has("work")) ? phones.getString("work") : "";
                String Hphone = (phones.has("home")) ? phones.getString("home") : "";
                String Mphone = (phones.has("mobile")) ? phones.getString("mobile") : "";

                rtncontact[i] = new Contact(name, company, detailsURL, smallImageURL, birthdate, Wphone, Hphone, Mphone, employeeID);

            }

            return rtncontact;
        }


        @Override
        protected Contact[] doInBackground(String... params) {
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
                Contact[] contacts = getContactDataFromJson(contactJsonStr);
                return contacts;
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }
    }
}
