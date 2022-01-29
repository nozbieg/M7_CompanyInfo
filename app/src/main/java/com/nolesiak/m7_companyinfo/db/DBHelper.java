package com.nolesiak.m7_companyinfo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "companyInfo.db";

    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table if not exists companyInfo " +
                        "(id integer primary key, name text, nip text,regon text,city text, province text)");
    }


    public boolean insertCompanyInfo (String name, String nip, String regon, String city,String province) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("nip", nip);
        contentValues.put("regon", regon);
        contentValues.put("city", city);
        contentValues.put("province", province);

        db.insert("companyInfo", null, contentValues);
        return true;
    }
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        onCreate(database);
    }

    public Cursor getCompanyInfoByNip(String nip){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from companyInfo where nip="+nip, null);
        return res;
    }
}
