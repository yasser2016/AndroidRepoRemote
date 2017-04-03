package com.yasser.android.networkdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Yasser on 3/27/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "networkdb.db";
    private static final int DB_VERSION = 5;

    public static final String TABLE_SITES = "table_sites";
    public static final String TABLE_LOOKUP = "table_lookup";
    public static final String TABLE_SITE_EQUIPMENT = "site_equipment";

    public static final String _ID = "_id";
    public static final String SITE_NAME = "site_name";
    public static final String SITE_CODE = "site_code";
    public static final String SITE_LAUNCH_DATE = "site_launch_date";

    public static final String EQU_ID = "equ_id";
    public static final String EQU_CREATED_AT = "equ_created_at";
    public static final String EQU_SITE_NAME_ID = "equ_site_name_id";
    public static final String EQU_DATE = "equ_date";
    public static final String EQU_TYPE= "equ_type";
    public static final String EQU_TAG_PREFIX = "equ_tag_prefix";
    public static final String EQU_TAG_MIDDLE = "equ_tag_middle";
    public static final String EQU_TAG_SUFFIX = "equ_tag_suffix";
    public static final String EQU_MANUFACTURER = "equ_manufacturer";
    public static final String EQU_SERIAL = "equ_serial";

    public static final String LOOKUP_ID = "lookup_id";
    public static final String LOOKUP_TAG_PREFIX = "lookup_tag_prefix";
    public static final String LOOKUP_TAG_SUFFIX = "lookup_tag_suffix";
    public static final String LOOKUP_MANUFACTURER = "lookup_manufacturer";
    public static final String LOOKUP_EQU_TYPE = "lookup_equ_type";

    private static final String CREATE_TABLE_SITES = " CREATE TABLE " + TABLE_SITES + " ( " +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SITE_NAME + " TEXT, " +
            SITE_CODE + " TEXT, " + SITE_LAUNCH_DATE + " TEXT " + " ); ";

    private static final String CREATE_TABLE_SITE_EQUIPMENT = " CREATE TABLE " + TABLE_SITE_EQUIPMENT +
            " ( " + EQU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            EQU_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            EQU_SITE_NAME_ID + " INTEGER REFERENCES " + TABLE_SITES + " , " +
            EQU_DATE + " TEXT, " + EQU_TYPE + " TEXT, " +
            EQU_MANUFACTURER + " TEXT, " + EQU_TAG_PREFIX + " TEXT, " +
            EQU_TAG_MIDDLE + " TEXT, " + EQU_TAG_SUFFIX + " TEXT, " +
            EQU_SERIAL + " TEXT " + " ); ";

    private static final String CREATE_TABLE_LOOKUP = " CREATE TABLE " + TABLE_LOOKUP + " ( " +
            LOOKUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LOOKUP_TAG_PREFIX + " TEXT, " +
            LOOKUP_TAG_SUFFIX + " TEXT, " + LOOKUP_EQU_TYPE + " TEXT, " +
            LOOKUP_MANUFACTURER + " TEXT " + " ); ";




    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SITES);
        db.execSQL(CREATE_TABLE_LOOKUP);
        db.execSQL(CREATE_TABLE_SITE_EQUIPMENT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL(" DROP TABLE IF EXISTS " + TABLE_SITES);
            db.execSQL(" DROP TABLE IF EXISTS " + TABLE_LOOKUP);
            db.execSQL(" DROP TABLE IF EXISTS " + TABLE_SITE_EQUIPMENT);
            onCreate(db);
        }

    }
}
