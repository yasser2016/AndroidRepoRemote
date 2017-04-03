package com.yasser.android.networkdb;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by Yasser on 3/28/2017.
 */

public class DbBackend {


    public static String[] loadSpinnerTagPrefixData(Context context){

        String[] projection = {DbHelper.LOOKUP_ID, DbHelper.LOOKUP_TAG_PREFIX};
        Cursor cursor = context.getContentResolver().query(NetworkContentProvider.CONTENT_LOOKUP_URI, projection,
                null, null, DbHelper.LOOKUP_TAG_PREFIX + " ASC");
        ArrayList<String> spinnerContent = new ArrayList<String>();
        if(cursor != null && cursor.moveToFirst()) {
            do {
                String word = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.LOOKUP_TAG_PREFIX));
                if (word.trim().length() != 0) {
                    spinnerContent.add(word);
                }

            } while (cursor.moveToNext());
        }
            String[] allSpinner = new String[spinnerContent.size()];
            allSpinner = spinnerContent.toArray(allSpinner);
        cursor.close();

        return allSpinner;
    }
    public static String[] loadSpinnerTagSuffixData(Context context){

        String[] projection = {DbHelper.LOOKUP_ID, DbHelper.LOOKUP_TAG_SUFFIX};
        Cursor cursor = context.getContentResolver().query(NetworkContentProvider.CONTENT_LOOKUP_URI, projection,
                null, null, DbHelper.LOOKUP_TAG_SUFFIX + " ASC");
        ArrayList<String> spinnerContent = new ArrayList<String>();
        if(cursor != null && cursor.moveToFirst()) {
            do {
                String word = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.LOOKUP_TAG_SUFFIX));
                if (word.trim().length() != 0) {
                    spinnerContent.add(word);
                }
            } while (cursor.moveToNext());
        }
        String[] allSpinner = new String[spinnerContent.size()];
        allSpinner = spinnerContent.toArray(allSpinner);
        cursor.close();
        return allSpinner;
    }
    public static String[] loadSpinnerEquManufacturerData(Context context){

        String[] projection = {DbHelper.LOOKUP_ID, DbHelper.LOOKUP_MANUFACTURER};
        Cursor cursor = context.getContentResolver().query(NetworkContentProvider.CONTENT_LOOKUP_URI, projection,
                null, null, DbHelper.LOOKUP_MANUFACTURER + " ASC");
        ArrayList<String> spinnerContent = new ArrayList<String>();
        if(cursor != null && cursor.moveToFirst()) {
            do {
                String word = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.LOOKUP_MANUFACTURER));
                if (word.trim().length() != 0) {
                    spinnerContent.add(word);
                }
            } while (cursor.moveToNext());
        }
        String[] allSpinner = new String[spinnerContent.size()];
        allSpinner = spinnerContent.toArray(allSpinner);
        cursor.close();
        return allSpinner;
    }
    public static String[] loadSpinnerEquTypeData(Context context){

        String[] projection = {DbHelper.LOOKUP_ID, DbHelper.LOOKUP_EQU_TYPE};
        Cursor cursor = context.getContentResolver().query(NetworkContentProvider.CONTENT_LOOKUP_URI, projection,
                null, null, DbHelper.LOOKUP_EQU_TYPE + " ASC");
        ArrayList<String> spinnerContent = new ArrayList<String>();
        if(cursor != null && cursor.moveToFirst()) {
            do {
                String word = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.LOOKUP_EQU_TYPE));
                if (word.trim().length() != 0) {
                    spinnerContent.add(word);
                }
            } while (cursor.moveToNext());
        }
        String[] allSpinner = new String[spinnerContent.size()];
        allSpinner = spinnerContent.toArray(allSpinner);
        cursor.close();
        return allSpinner;
    }

}
