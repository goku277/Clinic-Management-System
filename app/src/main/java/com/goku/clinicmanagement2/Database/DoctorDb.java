package com.goku.clinicmanagement2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

public class DoctorDb extends SQLiteOpenHelper {
    public DoctorDb(@Nullable Context context) {
        super(context, "doctordb", null, 1);
    }

    public void insertData(String name1, String specialization1, String mobile1, String fees1) {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("name", name1);
        cv.put("specialization", specialization1);
        cv.put("mobile", mobile1);
        cv.put("fees", fees1);
        db.insert("doctor",null,cv);
        db.close();
    }

    public void delete() {
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete("doctor", null, null);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query= "create table doctor(name text, specialization text, mobile text, fees text);";
        db.execSQL(query);
    }

    public Set<String> getAllEntries() {
        Set<String> entryset= new HashSet<>();
        String data="";

        SQLiteDatabase db= this.getWritableDatabase();

        String query = "select * from doctor";
        Cursor c1 = db.rawQuery(query, null);

        if (c1!=null && c1.getCount() > 0) {
            if (c1.moveToFirst()) {
                do {
                    data+= c1.getString(0) + " " + c1.getString(1) + " " + c1.getString(2) + " " + c1.getString(3) + " ";
                    entryset.add(data);
                    data="";
                } while (c1.moveToNext());
            }
        }
        return entryset;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
