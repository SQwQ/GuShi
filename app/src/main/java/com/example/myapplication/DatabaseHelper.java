package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by User on 2/28/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "poemtable";
    private static final String COL1 = "_id";
    private static final String COL2 = "weblink";
    private static final String COL3 = "title";
    private static final String COL4 = "author";
    private static final String COL5 = "poem";


    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 +" TEXT UNIQUE, " + COL3 + " TEXT, " + COL4 + " TEXT, " + COL5 + " TEXT)" ;
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String webID, String title, String author, String poem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, webID);
        contentValues.put(COL3, title);
        contentValues.put(COL4, author);
        contentValues.put(COL5, poem);

        Log.d(TAG, "addData: Adding " + title + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns all the data from database
     * @return
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor search(String ...keyword) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL3 + " LIKE '%" + keyword[0] + "%'";
            Cursor data = db.rawQuery(query, null);
            return data;
        } catch (SQLiteException e) {
            System.out.println("non-existent entry");
            return null;
        }
    }

    /**
     * Returns only the ID that matches the name passed in
     * @param link
     * @return
     */
    public Cursor getItemID(String link){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + link + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public boolean checkDB(String name)  {
        /*
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        String sql ="SELECT ID FROM "+ TABLE_NAME +" WHERE weblink=" + name;
        cursor= db.rawQuery(sql,null);
        */
        Cursor cursor = getItemID(name);

        if(cursor.getCount()>0){
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
            //PID Not Found
        }

    }

    /**
     * Updates the name field
     * @param newName
     * @param id
     * @param oldName
     */
    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }

    /**
     * Delete from database
     * @param id
     *
     */
    public void deletePoem(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL2 + " = '" + id + "'" ;
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + id + " from database.");
        db.execSQL(query);
    }

}

