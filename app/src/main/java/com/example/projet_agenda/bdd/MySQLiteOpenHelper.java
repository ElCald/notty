package com.example.projet_agenda.bdd;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    //attribut
    private String creation="create table agenda ("
            + "num INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "titre TEXT NOT NULL,"
            + "contenu TEXT NOT NULL,"
            + "tag TEXT NOT NULL,"
            + "date TEXT NOT NULL,"
            + "image TEXT NOT NULL);";

    //const
    public MySQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //methodes
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {//si changement de bdd
        sqLiteDatabase.execSQL(creation);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {//si changement de version
    }
}

