package com.yasser.android.networkdb;

import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
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
import java.util.Calendar;

public class AddEditEquipment extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        View.OnFocusChangeListener{
    DbBackend dbBackend;
    CursorLoader cursorLoader;

    public static final String Bundle_ID = "com.yasser.android.networkdb.key7";
    private TextInputLayout equipmentDateTxtInputLayout, tagMiddleInputLayout, equSerialInputLayout;
    private TextInputEditText equipmentDateEditTxt, tagMiddleEditTxt, equSerialEditTxt;
    private Spinner spinnerEquType, spinnerTagPrefix, spinnerTagSuffix,
            spinnerEquManufacturer;
    private LinearLayout tagPrefixLinearLayout, tagSuffixLinearLayout;
    private FloatingActionButton saveEquFAB;
    public static final int EQU_ITEMS_ADD_LOADER = 18;
    DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    Uri addEquUri;
    String bundleID, spinnerNoOfTagsValue, noOfTags;
    String TAG = "AddEditEquipment";
    int noOfTagsInteger, spinnerNoOfTagsValueInt;
    Bundle bundle5;
    Cursor cursor;

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
        loadSpinnerEquManufacturerData();
        loadSpinnerTagSuffixData();
        loadSpinnerTagPrefixData();
        loadSpinnerEquTypeData();
        saveEquFAB = (FloatingActionButton) findViewById(R.id.saveFloatingActionButton2);
        saveEquFAB.show();
        saveEquFAB.setOnClickListener(saveEquButtonClicked);
        bundle5 = this.getIntent().getExtras();
        if (bundle5 != null) {
            bundleID = bundle5.getString(Bundle_ID);
            Log.d(TAG, "on create bundle: "+" "+ bundleID);
            addEquUri = Uri.parse(NetworkContentProvider.CONTENT_SITE_EQU + "/" + bundleID);
            Log.d(TAG, "addEquUri: "+" "+ addEquUri);
        }
        getLoaderManager().initLoader(EQU_ITEMS_ADD_LOADER,null,this);

    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        cursorLoader = new CursorLoader(this,NetworkContentProvider.CONTENT_SITE_EQU, null, null, null, null);
        return cursorLoader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    // responds to event generated when user saves a site
    private final View.OnClickListener saveEquButtonClicked =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // hide the virtual keyboard
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    saveEquipment();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            };


    private void saveEquipment() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.EQU_SITE_NAME_ID, bundleID);
        contentValues.put(DbHelper.EQU_DATE, equipmentDateTxtInputLayout.getEditText().getText().toString());
        contentValues.put(DbHelper.EQU_TYPE, spinnerEquType.getSelectedItem().toString());
        contentValues.put(DbHelper.EQU_MANUFACTURER, spinnerEquManufacturer.getSelectedItem().toString());
        contentValues.put(DbHelper.EQU_TAG_PREFIX, spinnerTagPrefix.getSelectedItem().toString());
        contentValues.put(DbHelper.EQU_TAG_MIDDLE, tagMiddleInputLayout.getEditText().getText().toString());
        contentValues.put(DbHelper.EQU_TAG_SUFFIX, spinnerTagSuffix.getSelectedItem().toString());
        contentValues.put(DbHelper.EQU_SERIAL, equSerialInputLayout.getEditText().getText().toString());
        this.getContentResolver().insert(NetworkContentProvider.CONTENT_SITE_EQU, contentValues);
    }



    private void loadSpinnerTagPrefixData() {

        String[] spinnerLists = dbBackend.loadSpinnerTagPrefixData(AddEditEquipment.this);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(AddEditEquipment.this,
                android.R.layout.simple_spinner_item, spinnerLists);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTagPrefix.setAdapter(spinnerAdapter);
    }
    private void loadSpinnerTagSuffixData() {

        String[] spinnerLists = dbBackend.loadSpinnerTagSuffixData(AddEditEquipment.this);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(AddEditEquipment.this,
                android.R.layout.simple_spinner_item, spinnerLists);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTagSuffix.setAdapter(spinnerAdapter);
    }
    private void loadSpinnerEquManufacturerData() {

        String[] spinnerLists = dbBackend.loadSpinnerEquManufacturerData(AddEditEquipment.this);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(AddEditEquipment.this,
                android.R.layout.simple_spinner_item, spinnerLists);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEquManufacturer.setAdapter(spinnerAdapter);
    }
    private void loadSpinnerEquTypeData() {

        String[] spinnerLists = dbBackend.loadSpinnerEquTypeData(AddEditEquipment.this);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(AddEditEquipment.this,
                android.R.layout.simple_spinner_item, spinnerLists);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEquType.setAdapter(spinnerAdapter);
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
}
