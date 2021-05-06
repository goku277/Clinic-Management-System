package com.goku.clinicmanagement2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DoctorImagedb extends SQLiteOpenHelper {
    Context context;
    public DoctorImagedb(@Nullable Context context) {
        super(context, "doctorimagedb", null, 1);
        this.context= context;
    }

    public void insertData(String url1) {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("url", url1);
        db.insert("doctorimage",null,cv);
        db.close();
    }

    public void delete() {
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete("doctorimage", null, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query= "create table doctorimage(url text);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}