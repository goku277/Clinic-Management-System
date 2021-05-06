package com.goku.clinicmanagement2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class PreviousScheduledb extends SQLiteOpenHelper {
    public PreviousScheduledb(@Nullable Context context) {
        super(context, "previousrescheduledb", null,1);
    }

    public void insertData(String previousschedule1) {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("previousschedule", previousschedule1);
        db.insert("previousreschedule",null,cv);
        db.close();
    }

    public void delete() {
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete("previousreschedule", null, null);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query= "create table previousreschedule(previousschedule text);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}