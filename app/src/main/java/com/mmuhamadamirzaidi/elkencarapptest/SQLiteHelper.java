package com.mmuhamadamirzaidi.elkencarapptest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){

        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String manufacturer, String name, String price, String plat, byte[] image){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "INSERT INTO RECORD VALUES(NULL, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, manufacturer);
        statement.bindString(2, name);
        statement.bindString(3, price);
        statement.bindString(4, plat);

        statement.bindBlob(5, image);

        statement.executeInsert();
    }

    public void updateData(String manufacturer, String name, String price, String plat, byte[] image, int id){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE RECORD SET manufacturer=?, name=?, price=?, plat=?, image=? WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, manufacturer);
        statement.bindString(2, name);
        statement.bindString(3, price);
        statement.bindString(4, plat);

        statement.bindBlob(5, image);

        statement.bindDouble(6, (double)id);

        statement.execute();
        database.close();
    }

    public void deleteData(int id){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM RECORD WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
