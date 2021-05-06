package com.goku.clinicmanagement2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Referencedb extends SQLiteOpenHelper {
    public Referencedb(@Nullable Context context) {
        super(context, "referenceiddb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query= "create table referenceid(reference text);";
        db.execSQL(query);
    }

    public void insertData(String reference1) {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("reference", reference1);
        db.insert("referenceid",null,cv);
        db.close();
    }

    public void delete() {
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete("referenceid", null, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
