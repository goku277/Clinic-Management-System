package com.goku.clinicmanagement2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

public class DoctorSessionDb extends SQLiteOpenHelper {
    public DoctorSessionDb(@Nullable Context context) {
        super(context, "doctorsessiondb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query= "create table doctorsession(timings text);";
        db.execSQL(query);
    }

    public void delete() {
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete("doctorsession", null, null);
    }

    public Set<String> getAllEntries() {
        Set<String> entryset= new HashSet<>();
        String data="";

        SQLiteDatabase db= this.getWritableDatabase();

        String query = "select * from doctorsession";
        Cursor c1 = db.rawQuery(query, null);

        if (c1!=null && c1.getCount() > 0) {
            if (c1.moveToFirst()) {
                do {
                    data+= c1.getString(0);
                    entryset.add(data);
                    data="";
                } while (c1.moveToNext());
            }
        }
        return entryset;
    }

    public void insertData(String timings1) {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("timings", timings1);
        db.insert("doctorsession",null,cv);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
