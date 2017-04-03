package com.yasser.android.networkdb;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class NetworkContentProvider extends ContentProvider {
    DbHelper dbHelper;
    public static final String AUTHORITY = "com.yasser.android.networkdb";
    public static final String PATH_SITES = DbHelper.TABLE_SITES;
    public static final String PATH_LOOKUP = DbHelper.TABLE_LOOKUP;
    public static final String PATH_SITE_EQU = DbHelper.TABLE_SITE_EQUIPMENT;
    public static final Uri CONTENT_SITE_URI =
            Uri.parse("content://" + AUTHORITY + "/" + PATH_SITES);
    public static final Uri CONTENT_LOOKUP_URI =
            Uri.parse("content://" + AUTHORITY + "/" + PATH_LOOKUP);
    public static final Uri CONTENT_SITE_EQU =
            Uri.parse("content://" + AUTHORITY + "/" + PATH_SITE_EQU);

    private static final int ALL_SITES = 10;
    private static final int SITE_ID = 20;
    private static final int ALL_LOOKUPS = 50;
    private static final int LOOKUP_ID = 60;
    private static final int SITE_EQUIPMENT = 70;
    private static final int EQUIPMENT_CUSTOM_URI = 80;
    private static final int ALL_EQUIPMENT_AND_SITE = 90;
    private static final int ALL_EQU = 100;
    private static final int EQU_ID = 110;
    private static final int SITE_EQU = 120;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY,PATH_SITES,ALL_SITES);
        sUriMatcher.addURI(AUTHORITY,PATH_SITES + "/#",SITE_ID);
        sUriMatcher.addURI(AUTHORITY,PATH_SITE_EQU,ALL_EQU);
        sUriMatcher.addURI(AUTHORITY,PATH_SITE_EQU + "/#",EQU_ID);
        sUriMatcher.addURI(AUTHORITY,PATH_LOOKUP,ALL_LOOKUPS);
        sUriMatcher.addURI(AUTHORITY,PATH_LOOKUP + "/#",LOOKUP_ID);
        sUriMatcher.addURI(AUTHORITY,PATH_SITE_EQU + "/equipmentCustomUri" + "/#", EQUIPMENT_CUSTOM_URI);
        sUriMatcher.addURI(AUTHORITY,PATH_SITE_EQU + "/allEquAndSites" + "/",ALL_EQUIPMENT_AND_SITE);
        sUriMatcher.addURI(AUTHORITY,PATH_SITE_EQU + "/siteEqu" + "/#",SITE_EQU);
    }
    public NetworkContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        Cursor cursor = null;
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int uriType = sUriMatcher.match(uri);
        int rowsDeleted = 0;
        String id = uri.getLastPathSegment();
        switch (uriType) {
            case ALL_SITES:
                break;
            case SITE_ID:
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqLiteDatabase.delete(DbHelper.TABLE_SITES,
                            DbHelper._ID + "=" + id,null);
                } else {
                    rowsDeleted = sqLiteDatabase.delete(DbHelper.TABLE_SITES,
                            DbHelper._ID + "=" + id + " and " + selection, selectionArgs);}

                break;
            case ALL_EQU:
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqLiteDatabase.delete(DbHelper.TABLE_SITE_EQUIPMENT,
                            DbHelper.EQU_SITE_NAME_ID + "=" + id,null);
                } else {
                    rowsDeleted = sqLiteDatabase.delete(DbHelper.TABLE_SITE_EQUIPMENT,
                            DbHelper.EQU_SITE_NAME_ID + "=" + id + " and " + selection, selectionArgs);}
                break;
            case EQU_ID:
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqLiteDatabase.delete(DbHelper.TABLE_SITE_EQUIPMENT,
                            DbHelper.EQU_ID + "=" + id,null);
                } else {
                    rowsDeleted = sqLiteDatabase.delete(DbHelper.TABLE_SITE_EQUIPMENT,
                            DbHelper.EQU_ID + "=" + id + " and " + selection, selectionArgs);}
                break;
            case SITE_EQU:
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqLiteDatabase.delete(DbHelper.TABLE_SITE_EQUIPMENT,
                            DbHelper.EQU_SITE_NAME_ID + "=" + id,null);
                } else {
                    rowsDeleted = sqLiteDatabase.delete(DbHelper.TABLE_SITE_EQUIPMENT,
                            DbHelper.EQU_SITE_NAME_ID + "=" + id + " and " + selection, selectionArgs);}
                break;
            case ALL_LOOKUPS:
                rowsDeleted = sqLiteDatabase.delete(DbHelper.TABLE_LOOKUP, null, null);
                break;


            default:
                throw new IllegalArgumentException("Unknown Uri" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;

    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        long rowID = 0;
        Uri _uri = null;
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case ALL_SITES:
                rowID = sqLiteDatabase.insert(DbHelper.TABLE_SITES,null, values);
                if (rowID > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_SITE_URI, rowID);
                }
                break;
            case SITE_ID:
                break;
            case ALL_LOOKUPS:
                rowID = sqLiteDatabase.insert(DbHelper.TABLE_LOOKUP, " ", values);
                if (rowID > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_LOOKUP_URI, rowID);
                }
                break;
            case EQU_ID:
                break;
            case ALL_EQU:
                rowID = sqLiteDatabase.insert(DbHelper.TABLE_SITE_EQUIPMENT, "", values);
                if (rowID > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_SITE_EQU, rowID);
                }
                break;
            default:
                throw new SQLException("Failed to add record: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return _uri;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbHelper = new DbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = null;
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        sqLiteDatabase = dbHelper.getReadableDatabase();
        queryBuilder.setTables(DbHelper.TABLE_SITES + "," +
                DbHelper.TABLE_SITE_EQUIPMENT + "," + DbHelper.TABLE_LOOKUP);

        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case ALL_SITES:
                queryBuilder.setTables(DbHelper.TABLE_SITES);
                cursor = queryBuilder.query(sqLiteDatabase, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SITE_ID:
                String rowID = uri.getLastPathSegment();
                queryBuilder.setTables(DbHelper.TABLE_SITES);
                queryBuilder.appendWhere(DbHelper._ID + "=" + rowID);
                cursor = queryBuilder.query(sqLiteDatabase,projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ALL_LOOKUPS:
                queryBuilder.setTables(DbHelper.TABLE_LOOKUP);
                cursor = queryBuilder.query(sqLiteDatabase,projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case EQUIPMENT_CUSTOM_URI:
                String rowCustomID = uri.getLastPathSegment();
                queryBuilder.setTables(DbHelper.TABLE_SITE_EQUIPMENT);
                queryBuilder.appendWhere(DbHelper.EQU_ID + "=" + rowCustomID);
                cursor = queryBuilder.query(sqLiteDatabase,projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ALL_EQUIPMENT_AND_SITE:
                queryBuilder.setTables(DbHelper.TABLE_SITE_EQUIPMENT + " INNER JOIN " +
                        DbHelper.TABLE_SITES + " ON " + DbHelper.TABLE_SITE_EQUIPMENT + " . " + DbHelper.EQU_SITE_NAME_ID +
                        " = " + DbHelper.TABLE_SITES + " . " + DbHelper._ID);
                cursor = queryBuilder.query(sqLiteDatabase,projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ALL_EQU:
                queryBuilder.setTables(DbHelper.TABLE_SITE_EQUIPMENT);
                cursor = queryBuilder.query(sqLiteDatabase,projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case EQU_ID:
                queryBuilder.setTables(DbHelper.TABLE_SITE_EQUIPMENT + " INNER JOIN " +
                        DbHelper.TABLE_SITES + " ON " + DbHelper.TABLE_SITE_EQUIPMENT + " . " + DbHelper.EQU_SITE_NAME_ID +
                        " = " + DbHelper.TABLE_SITES + " . " + DbHelper._ID);
                queryBuilder.appendWhere(DbHelper.EQU_SITE_NAME_ID + "=" + uri.getPathSegments().get(1));
                cursor = queryBuilder.query(sqLiteDatabase,projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SITE_EQU:
                queryBuilder.setTables(DbHelper.TABLE_SITE_EQUIPMENT + " INNER JOIN " +
                DbHelper.TABLE_SITES + " ON " + DbHelper.TABLE_SITE_EQUIPMENT + "." + DbHelper.EQU_SITE_NAME_ID +
                "=" + DbHelper.TABLE_SITES + "." + DbHelper._ID);
                String siteID = uri.getLastPathSegment();
                queryBuilder.appendWhere(DbHelper.EQU_SITE_NAME_ID + "=" + siteID);
                cursor = queryBuilder.query(sqLiteDatabase, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int rowsAffected = 0;
        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case ALL_SITES:
                break;
            case SITE_ID:
                String rowID = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsAffected = sqLiteDatabase.update(
                            DbHelper.TABLE_SITES,
                            values, DbHelper._ID + "=" + rowID, null);
                } else {
                    rowsAffected = sqLiteDatabase.update(
                            DbHelper.TABLE_SITES,
                            values, selection + " and " + DbHelper._ID + "="
                                    + rowID, selectionArgs);
                }
                break;
            case ALL_EQU:
                rowsAffected = sqLiteDatabase.update(DbHelper.TABLE_SITE_EQUIPMENT,
                        values, selection, selectionArgs);
                break;
            case SITE_EQU:
                String rowSiteEquID = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsAffected = sqLiteDatabase.update(
                            DbHelper.TABLE_SITE_EQUIPMENT,
                            values, DbHelper.EQU_ID + "=" + rowSiteEquID, null);
                } else {
                    rowsAffected = sqLiteDatabase.update(
                            DbHelper.TABLE_SITE_EQUIPMENT,
                            values, selection + " and " + DbHelper.EQU_ID + "="
                                    + rowSiteEquID, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException(
                        "Unknown or Invalid URI " + uri);


        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;

    }
}
