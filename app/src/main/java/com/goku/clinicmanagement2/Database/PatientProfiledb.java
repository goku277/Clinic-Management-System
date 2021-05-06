package com.goku.clinicmanagement2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class PatientProfiledb extends SQLiteOpenHelper {
    Context context;
    public PatientProfiledb(@Nullable Context context) {
        super(context, "patientprofiledb", null, 1);
        this.context= context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query= "create table patientprofile(name text, age text, mobile text);";
        db.execSQL(query);
    }

    public void delete() {
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete("patientprofile", null, null);
    }

    public void insertData(String name, String age, String mobile) {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("name", name);
        cv.put("age", age);
        cv.put("mobile", mobile);
        db.insert("patientprofile",null,cv);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
