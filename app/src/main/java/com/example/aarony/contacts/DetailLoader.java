package com.example.aarony.contacts;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by aarony on 11/28/15.
 */
public class DetailLoader extends AsyncTask<Object, Void, Void> {
    private final String LOG_TAG = DetailLoader.class.getSimpleName();
    protected Contact contact = null;

    private Void addContactDataFromJson(String contactJsonStr) throws JSONException {

        JSONArray contacts = new JSONArray(contactJsonStr);
        for (int i = 0; i < contacts.length(); i++) {
            JSONObject contactObj = contacts.getJSONObject(i);
            Log.v(LOG_TAG, contactObj.toString());

            contact.favorite = contactObj.getBoolean("favorite");
            contact.largeImageURL = contactObj.getString("largeImageURL");
            contact.email = (contactObj.has("email")) ? contactObj.getString("email") : "";
            contact.website = (contactObj.has("website")) ? contactObj.getString("website") : "";

            // get address JSON object
            JSONObject address = contactObj.getJSONObject("address");
            contact.street = (address.has("street")) ? address.getString("street") : "";
            contact.city = (address.has("city")) ? address.getString("city") : "";
            contact.country = (address.has("country")) ? address.getString("country") : "";
            contact.zip = (address.has("zip")) ? address.getString("zip") : "";

        }
        return null;
    }


    @Override
    protected Void doInBackground(Object... params) {
        // get JSON data from https://solstice.applauncher.com/external/contacts.json

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Store JSON response as a string
        String contactJsonStr = null;

        try {
            // create the URL for query
            contact = (Contact) params[0];
            String solURL = contact.detailsURL;
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
            Log.v(LOG_TAG, contactJsonStr);
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
            addContactDataFromJson(contactJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
}
