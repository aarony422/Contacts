package com.example.aarony.contacts;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

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
public class DetailLoader extends AsyncTask<Void, Void, Void> {
    private final String LOG_TAG = DetailLoader.class.getSimpleName();
    public Contact contact = null;
    public ImageLoader imgnext = null;


    public DetailLoader(Contact contact) {
        this.contact = contact;
    }

    public void setNext(ImageLoader next) {
        this.imgnext=next;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (imgnext != null) {
            imgnext.execute(imgnext.rootview, contact.largeImageURL);
        }
        // update detail information
        ((TextView) imgnext.rootview.findViewById(R.id.detail_email_textview)).setText(contact.email);
        ((TextView) imgnext.rootview.findViewById(R.id.detail_street_textview)).setText(contact.street);
        ((TextView) imgnext.rootview.findViewById(R.id.detail_citystate_textview)).setText(contact.city + ", " + contact.state);
        ((TextView) imgnext.rootview.findViewById(R.id.detail_zipcountry_textview)).setText(contact.zip + ", " + contact.country);
        ((TextView) imgnext.rootview.findViewById(R.id.detail_website_textview)).setText(contact.website);
    }

    private Void addContactDataFromJson(String contactJsonStr) throws JSONException {

        JSONObject contactObj = new JSONObject(contactJsonStr);
        Log.v(LOG_TAG, contactObj.toString());

        contact.favorite = contactObj.getBoolean("favorite");
        contact.largeImageURL = contactObj.getString("largeImageURL");
        contact.email = (contactObj.has("email")) ? contactObj.getString("email") : "";
        contact.website = (contactObj.has("website")) ? contactObj.getString("website") : "";

        // get address JSON object
        JSONObject address = contactObj.getJSONObject("address");
        contact.street = (address.has("street")) ? address.getString("street") : "";
        contact.city = (address.has("city")) ? address.getString("city") : "";
        contact.state = (address.has("state")) ? address.getString("state") : "";
        contact.country = (address.has("country")) ? address.getString("country") : "";
        contact.zip = (address.has("zip")) ? address.getString("zip") : "";

        return null;
    }


    @Override
    protected Void doInBackground(Void... params) {
        // get JSON data from https://solstice.applauncher.com/external/contacts.json

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Store JSON response as a string
        String contactJsonStr = null;

        try {
            // create the URL for query
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
            return addContactDataFromJson(contactJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
}
