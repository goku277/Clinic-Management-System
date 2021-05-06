package com.goku.clinicmanagement2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class PatientImageUrldb extends SQLiteOpenHelper {
    Context context;
    public PatientImageUrldb(@Nullable Context context) {
        super(context, "patienturldb", null, 1);
        this.context= context;
    }

    public void delete() {
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete("patienturl", null, null);
    }

    public void insertData(String url1) {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("url", url1);
        db.insert("patienturl",null,cv);
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query= "create table patienturl(url text);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}