package com.goku.clinicmanagement2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Rescheduledb extends SQLiteOpenHelper {
    Context context;
    public Rescheduledb(@Nullable Context context) {
        super(context, "rescheduledb", null, 1);
        this.context= context;
    }

    public void insertData(String reschedule1) {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("re_schedule", reschedule1);
        db.insert("reschedule",null,cv);
        db.close();
    }

    public void delete() {
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete("reschedule", null, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query= "create table reschedule(re_schedule text);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
