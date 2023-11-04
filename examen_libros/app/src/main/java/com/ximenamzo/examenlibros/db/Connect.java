package com.ximenamzo.examenlibros.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Connect extends SQLiteOpenHelper {
    public static final Integer APPVERSION = 2;
    public Connect(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (int i=0; i<3; i++) { db.execSQL(Variables.CREAR_TABLA[i]); }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i=0; i<3; i++) { db.execSQL(Variables.ELIMINAR_TABLA[i]); }
        onCreate(db);
    }
}
