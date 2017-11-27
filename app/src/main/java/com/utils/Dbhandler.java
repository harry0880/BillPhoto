package com.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Base64;

import com.billphoto.CardGetSet;

import java.util.ArrayList;

/**
 * Created by harpreetsingh on 21/11/17.
 */

public class Dbhandler extends SQLiteOpenHelper implements DbConstant{
    public Dbhandler(Context context) {
        super(context, DbConstant.DbName, null, DbConstant.DbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_Table_Data);
        db.execSQL(Create_Table_Snap);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public long saveData(ContentValues cv)
    {
        SQLiteDatabase db=getWritableDatabase();

       return db.insert(T_Tbl1,null,cv);
    }

    public long saveImg(ContentValues cv)
    {
        SQLiteDatabase db=getWritableDatabase();

        return db.insert(T_snap,null,cv);
    }


    public ArrayList<CardGetSet> getCardData()
    {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cr=db.rawQuery("select * from " + T_Tbl1 + ";", null);
        cr.moveToFirst();
        ArrayList<CardGetSet> arr=new ArrayList<>();
        if(cr.getCount()>0)
        {
            do {
                arr.add(new CardGetSet(cr.getString(0),cr.getString(1),cr.getString(2)));
            }while(cr.moveToNext());

        }
        else {
            return arr;
        }
        db.close();
        return arr;
    }

    public  String getDatabaseField(String id)
    {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cr=db.rawQuery("select * from "+T_Tbl1+" where "+C_ID+"= "+id+";",null);
        cr.moveToFirst();
        String data=cr.getString(1)+"#"+cr.getString(2)+"#"+cr.getString(3);
        return data;
    }

    public Bitmap getImgField(String id)
    {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cr=db.rawQuery("select * from "+T_snap+" where "+C_ID+"= "+id+";",null);
        cr.moveToFirst();
        byte[] arr= Base64.decode(cr.getString(1),Base64.DEFAULT);
        Bitmap  decodedByte = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        return decodedByte;
    }
}
