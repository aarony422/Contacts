package com.example.aarony.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aarony on 11/29/15.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactDB.db";
    private static final String TABLE_CONTACTS = "contacts";

    // column names
    private static final String CONTACT = "employeeID";
    private static final String SMALL = "smallimg";
    private static final String LARGE = "largeimg";

    /*
    public String name;
    public int employeeID;
    public String company;
    public String detailsURL;
    public String smallImageURL;
    public String birthdate;
    public String Wphone;
    public String Hphone;
    public String Mphone;

    // details
    public boolean favorite;
    public String largeImageURL;
    public String email;
    public String website;
    // address
    public String street;
    public String city;
    public String state;
    public String country;
    public String zip;
     */

    public DBHandler (Context context, String name, SQLiteDatabase.CursorFactory factory,
                     int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
        String CREATE_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "(" + "employeeID INTEGER PRIMARY KEY " +
                "name TEXT, company TEXT, detailsURL TEXT, smallImageURL TEXT, birthdate TEXT, Wphone TEXT, " +
                "Hphone TEXT, Mphone TEXT, favorite INTEGER, largeImageURL TEXT, email TEXT, website TEXT, " +
                "street TEXT, city TEXT, state TEXT, country TEXT, zip TEXT)";
        */
        String CREATE_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "(" + CONTACT + " INTEGER PRIMARY KEY, " +
                                SMALL + " BLOB, " + LARGE + " BLOB);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public void addsmall(int employeeID, byte[] image) throws SQLiteException {
        ContentValues cv = new ContentValues();
        cv.put(CONTACT, employeeID);
        cv.put(SMALL, image);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_CONTACTS, null, cv);
        db.close();
    }

    public void addlarge(int employeeID, byte[] image) throws SQLiteException {
        ContentValues cv = new ContentValues();
        cv.put(LARGE, image);
        SQLiteDatabase db = this.getWritableDatabase();
        String where = CONTACT + "=?";
        String[] whereArgs = new String[] {String.valueOf(employeeID)};
        db.update(TABLE_CONTACTS, cv, where, whereArgs);
        db.close();
    }

    public byte[] getImg(int employeeID, Boolean large) {
        String query = "SELECT * FROM " + TABLE_CONTACTS + " WHERE employeeID = " + String.valueOf(employeeID) + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int pos = (large) ? 2 : 1;

        byte[] byteArr = null;
        if (cursor.moveToFirst()) {
            byteArr = cursor.getBlob(pos);
        }
        return byteArr;

    }

    /*
    public void addContact (Contact contact) {
        ContentValues values = new ContentValues();
        values.put("employeeID", contact.employeeID);
        values.put("name", contact.name);
        values.put("company", contact.company);
        values.put("detailsURL", contact.detailsURL);
        values.put("smallImageURL", contact.smallImageURL);
        values.put("birthdate", contact.birthdate);
        values.put("Wphone", contact.Wphone);
        values.put("Hphone", contact.Hphone);
        values.put("Mphone", contact.Mphone);
        if (contact.favorite == true) {
            values.put("favorite", 1);
        } else {
            values.put("favorite", 0);
        }
        values.put("largeImageURL", contact.largeImageURL);
        values.put("email", contact.email);
        values.put("website", contact.website);
        values.put("street", contact.street);
        values.put("city", contact.city);
        values.put("state", contact.state);
        values.put("country", contact.country);
        values.put("zip", contact.zip);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }
    */
    /*

    public Contact findContact(int employeeID) {
        String query = "Select * FROM " + TABLE_CONTACTS + " WHERE employeeID = " + String.valueOf(employeeID);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Contact contact = new Contact();

        if (cursor.moveToFirst()) {
            // set contact object
            cursor.moveToFirst();
            contact.employeeID = Integer.parseInt(cursor.getString(0));
            contact.name = cursor.getString(1);
            contact.company = cursor.getString(2);
            contact.detailsURL = cursor.getString(3);
            contact.smallImageURL = cursor.getString(4);
            contact.birthdate =  cursor.getString(5);
            contact.Wphone = cursor.getString(6);
            contact.Hphone = cursor.getString(7);
            contact.Mphone = cursor.getString(8);

            int fav = Integer.parseInt(cursor.getString(9));
            contact.favorite = (fav == 1) ? true : false;

            contact.largeImageURL = cursor.getString(10);
            contact.email = cursor.getString(11);
            contact.website = cursor.getString(12);
            contact.street = cursor.getString(13);
            contact.city = cursor.getString(14);
            contact.state = cursor.getString(15);
            contact.country = cursor.getString(16);
            contact.zip = cursor.getString(17);

        } else {
            contact = null;
        }
        db.close();
        return contact;
    }
    */
}
