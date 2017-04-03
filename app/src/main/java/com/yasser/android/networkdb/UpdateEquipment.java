package com.yasser.android.networkdb;

import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class UpdateEquipment extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,View.OnClickListener,
        View.OnFocusChangeListener{
    public static final String listItemID3 = "com.yasser.android.networkdb.key3";
    public static final String listItemID4 = "com.yasser.android.networkdb.key4";
    private TextInputLayout equipmentDateTxtInputLayout, tagMiddleInputLayout, equSerialInputLayout;
    private TextInputEditText equipmentDateEditTxt, tagMiddleEditTxt, equSerialEditTxt;
    private Spinner spinnerEquType, spinnerTagPrefix, spinnerTagSuffix,
            spinnerEquManufacturer;
    private LinearLayout tagPrefixLinearLayout, tagSuffixLinearLayout;
    private FloatingActionButton saveEquFAB;
    private static final int EQU_ITEMS_UPDATE_LOADER = 12;
    DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    String TAG = "UpdateEquipment";
    CursorLoader cursorLoader1;
    Bundle bundle5;
    String itemID3,itemID4,spPrefix,spSuffix,spType,spManufacturer;
    Uri equipmentUri,siteUri1,equipmentCustomUri;
    DbBackend dbBackend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_equipment);

        equipmentDateTxtInputLayout = (TextInputLayout) findViewById(R.id.equipmentDateTxtInputLayout);
        tagMiddleInputLayout = (TextInputLayout) findViewById(R.id.tagMiddleInputLayout);
        equSerialInputLayout = (TextInputLayout) findViewById(R.id.equSerialInputLayout);

        equipmentDateEditTxt = (TextInputEditText) findViewById(R.id.equipmentDateEditTxt);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        tagPrefixLinearLayout = (LinearLayout) findViewById(R.id.tagPrefixLinearLayout);
        tagSuffixLinearLayout = (LinearLayout) findViewById(R.id.tagSuffixLinearLayout);
        spinnerEquType = (Spinner) findViewById(R.id.spinnerEquType);
        spinnerTagPrefix = (Spinner) findViewById(R.id.spinnerTagPrefix);
        spinnerTagSuffix = (Spinner) findViewById(R.id.spinnerTagSuffix);
        spinnerEquManufacturer = (Spinner) findViewById(R.id.spinnerEquManufacturer);

        equipmentDateEditTxt.setOnClickListener(this);
        saveEquFAB = (FloatingActionButton) findViewById(R.id.saveFloatingActionButton2);
        saveEquFAB.show();
        saveEquFAB.setOnClickListener(this);

        bundle5 = this.getIntent().getExtras();
        if (bundle5 != null) {
            itemID3 = bundle5.getString(listItemID3);
            itemID4 = bundle5.getString(listItemID4);
            equipmentUri = Uri.parse(NetworkContentProvider.CONTENT_SITE_EQU + "/" + itemID3);
            equipmentCustomUri = Uri.parse(NetworkContentProvider.CONTENT_SITE_EQU + "/" +
                    "equipmentCustomUri" + "/" + itemID3);
            siteUri1 = Uri.parse(NetworkContentProvider.CONTENT_SITE_URI + "/" + itemID4);
        }
        Log.d(TAG, "onCreate equipmentID: " + itemID3);
        Log.d(TAG, "onCreate equipmentUri: " + equipmentUri);
        Log.d(TAG, "onCreate siteUri1: " + siteUri1);
        Log.d(TAG, "onCreate equipmentCustomUri: " + equipmentCustomUri);
        getLoaderManager().initLoader(EQU_ITEMS_UPDATE_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case EQU_ITEMS_UPDATE_LOADER:
                return new CursorLoader(getBaseContext(),
                        equipmentCustomUri, // Uri of contact to display
                        null, // null projection returns all columns
                        null, // null selection returns all rows
                        null, // no selection arguments
                        null); // sort order
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            try {
            equipmentDateTxtInputLayout.getEditText().setText(data.getString(data.getColumnIndex(DbHelper.EQU_DATE)));
            tagMiddleInputLayout.getEditText().setText(data.getString(data.getColumnIndex(DbHelper.EQU_TAG_MIDDLE)));
            equSerialInputLayout.getEditText().setText(data.getString(data.getColumnIndex(DbHelper.EQU_SERIAL)));
            spPrefix = data.getString(data.getColumnIndex(DbHelper.EQU_TAG_PREFIX));
            spSuffix = data.getString(data.getColumnIndex(DbHelper.EQU_TAG_SUFFIX));
            spType = data.getString(data.getColumnIndex(DbHelper.EQU_TYPE));
            spManufacturer = data.getString(data.getColumnIndex(DbHelper.EQU_MANUFACTURER));
                loadSpinnerTagPrefixData(spPrefix);
                loadSpinnerEquTypeData(spType);
                loadSpinnerTagSuffixData(spSuffix);
                loadSpinnerEquManufacturerData(spManufacturer);
            } catch (Exception e) {

            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveFloatingActionButton2:
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                updateEquipmentData();
                finish();
                break;
            case R.id.equipmentDateEditTxt:
                datePickerDialog.show();
                break;
        }
    }

    private void updateEquipmentData() {
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(DbHelper.EQU_SITE_NAME_ID, itemID4);
            contentValues.put(DbHelper.EQU_DATE, equipmentDateTxtInputLayout.getEditText().getText().toString());
            contentValues.put(DbHelper.EQU_TYPE, spinnerEquType.getSelectedItem().toString());
            contentValues.put(DbHelper.EQU_MANUFACTURER, spinnerEquManufacturer.getSelectedItem().toString());
            contentValues.put(DbHelper.EQU_TAG_PREFIX, spinnerTagPrefix.getSelectedItem().toString());
            contentValues.put(DbHelper.EQU_TAG_MIDDLE, tagMiddleInputLayout.getEditText().getText().toString());
            contentValues.put(DbHelper.EQU_TAG_SUFFIX, spinnerTagSuffix.getSelectedItem().toString());
            contentValues.put(DbHelper.EQU_SERIAL, equSerialInputLayout.getEditText().getText().toString());

            this.getContentResolver().update(equipmentUri, contentValues, null, null);
        } catch (Exception e) {

        }
    }
    private void loadSpinnerTagPrefixData(String spinnerSelection) {
        String B ;
        String[] spinnerLists = dbBackend.loadSpinnerTagPrefixData(UpdateEquipment.this);
        List<String> str1;
        str1 = Arrays.asList(spinnerLists);
        for (int i = 0;i<str1.size();i++) {
            String A = str1.get(i);
            Log.d(TAG, "A: " + A);
            if (A.equals(spinnerSelection)) {
                B = str1.get(i);
                ArrayList<String> strList = new ArrayList<>();
                strList.add(B);
                Log.d(TAG, "strList: " + strList);
                int j=1;
                for (int index = 0; index < str1.size(); index++) {
                    String S = str1.get(index);
                    strList.add(S);
                }
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(UpdateEquipment.this,
                        android.R.layout.simple_spinner_item, strList);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTagPrefix.setAdapter(spinnerAdapter);
            }
        }
    }
    private void loadSpinnerTagSuffixData(String spinnerSelection) {
        String B ;
        String[] spinnerLists = dbBackend.loadSpinnerTagSuffixData(UpdateEquipment.this);
        List<String> str1;
        str1 = Arrays.asList(spinnerLists);
        for (int i = 0;i<str1.size();i++) {
            String A = str1.get(i);
            Log.d(TAG, "A: " + A);
            if (A.equals(spinnerSelection)) {
                B = str1.get(i);
                ArrayList<String> strList = new ArrayList<>();
                strList.add(B);
                Log.d(TAG, "strList: " + strList);
                int j=1;
                for (int index = 0; index < str1.size(); index++) {
                  String S = str1.get(index);
                    strList.add(S);
                }
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(UpdateEquipment.this,
                        android.R.layout.simple_spinner_item, strList);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTagSuffix.setAdapter(spinnerAdapter);
            }
        }
    }
    private void loadSpinnerEquManufacturerData(String spinnerSelection) {
        String B ;
        String[] spinnerLists = dbBackend.loadSpinnerEquManufacturerData(UpdateEquipment.this);
        List<String> str1;
        str1 = Arrays.asList(spinnerLists);
        for (int i = 0;i<str1.size();i++) {
            String A = str1.get(i);
            Log.d(TAG, "A: " + A);
            if (A.equals(spinnerSelection)) {
                B = str1.get(i);
                ArrayList<String> strList = new ArrayList<>();
                strList.add(B);
                Log.d(TAG, "strList: " + strList);
                int j=1;
                for (int index = 0; index < str1.size(); index++) {
                    String S = str1.get(index);
                    strList.add(S);
                }
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(UpdateEquipment.this,
                        android.R.layout.simple_spinner_item, strList);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerEquManufacturer.setAdapter(spinnerAdapter);
            }
        }
    }
    private void loadSpinnerEquTypeData(String spinnerSelection) {
        String B ;
        String[] spinnerLists = dbBackend.loadSpinnerEquTypeData(UpdateEquipment.this);
        List<String> str1;
        str1 = Arrays.asList(spinnerLists);
        for (int i = 0;i<str1.size();i++) {
            String A = str1.get(i);
            Log.d(TAG, "A: " + A);
            if (A.equals(spinnerSelection)) {
                B = str1.get(i);
                ArrayList<String> strList = new ArrayList<>();
                strList.add(B);
                Log.d(TAG, "strList: " + strList);
                int j=1;
                for (int index = 0; index < str1.size(); index++) {
                    String S = str1.get(index);
                    strList.add(S);
                }
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(UpdateEquipment.this,
                        android.R.layout.simple_spinner_item, strList);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerEquType.setAdapter(spinnerAdapter);
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus) {
            // Show your calender here
            setDateTimeField();
        } else {
            // Hide your calender here
            datePickerDialog.hide();
        }
    }
    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                equipmentDateEditTxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
