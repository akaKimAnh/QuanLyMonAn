package com.kimanh.myapplication.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Spinner;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String Database_name = "food.db";
    public static final String Table_name = "food_table";
    public static final String Table_user = "allusers";
    public static final String col_mamonan = "mamonan";
    public static final String col_tenmonan = "tenmonan";
    public static final String col_diachi = "diachi";
    public static final String col_chongoi = "chongoi";

    public static final String col_loai = "loai";
    public static final String col_image = "anhmonan";


    public DatabaseHelper(@Nullable Context context) {
        super(context, Database_name, null, 2);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Table_name +" (mamonan TEXT PRIMARY KEY , tenmonan TEXT , diachi TEXT,chongoi TEXT,loai TEXT,anhmonan TEXT)");
        db.execSQL("create table " +Table_user + "(tendangnhap TEXT PRIMARY KEY , matkhau TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Table_name);
        db.execSQL("drop Table if exists "+ Table_user );

        onCreate(db);
    }
    public boolean insertData(String mamonan,String tenmonan,String diachi,String chongoi,String loai,String anhmonan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(col_mamonan, mamonan);
        cv.put(col_tenmonan, tenmonan);
        cv.put(col_diachi, diachi);
        cv.put(col_chongoi, chongoi);
        cv.put(col_loai, loai);
        cv.put(col_image, anhmonan);
        Long result = db.insert(Table_name, null, cv);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor searchFoods(String tenmonan){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+Table_name+" where " + col_tenmonan +" Like '%"+tenmonan+"%'" ,null);
        return cursor;
    }
    public Cursor Showdata()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+Table_name,null);
        return cursor;
    }
    public boolean update(String mamonan,String tenmonan,String diachi,String chongoi,String loai,String anhmonan)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(col_mamonan,mamonan);
        cv.put(col_tenmonan,tenmonan);
        cv.put(col_diachi,diachi);
        cv.put(col_chongoi,chongoi);
        cv.put(col_loai,loai);
        cv.put(col_image,anhmonan);
        db.update(Table_name,cv,"mamonan = ?",new String[] { mamonan });
        return true;
    }
    public Integer delete(String mamonan)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Table_name,"mamonan = ?",new String[] {mamonan});
    }
    public Boolean insertDataUser(String tendangnhap, String matkhau) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tendangnhap", tendangnhap);
        contentValues.put("matkhau", matkhau);
        long result = db.insert(Table_user, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public Boolean checkTendangnhap(String tendangnhap) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + Table_user + " where tendangnhap =?", new String[]{tendangnhap});

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkTendangnhapMatkhau(String tendangnhap, String matkhau) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + Table_user + " where tendangnhap = ? and matkhau = ? ", new String[]{tendangnhap, matkhau});

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

}

