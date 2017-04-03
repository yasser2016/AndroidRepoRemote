package com.yasser.android.networkdb;

import android.content.ContentValues;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class AddEditLookup extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout prefixTxtInputLayout,suffixInputLayout,typeInputLayout,
            manufacturerInputLayout;
    private FloatingActionButton saveFAB;
    String TAG ="AddEditLookup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_lookup);
        prefixTxtInputLayout=(TextInputLayout) findViewById(R.id.prefixTxtInputLayout);
        suffixInputLayout=(TextInputLayout) findViewById(R.id.suffixInputLayout);
        typeInputLayout=(TextInputLayout) findViewById(R.id.typeInputLayout);
        manufacturerInputLayout=(TextInputLayout) findViewById(R.id.manufacturerInputLayout);

        saveFAB = (FloatingActionButton) findViewById(R.id.saveFAB);
        saveFAB.show();
        saveFAB.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveFAB:
                InputMethodManager inputMethodManager=  (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
                saveLookups();
                finish();
                break;
        }

    }

    private void saveLookups() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.LOOKUP_TAG_PREFIX, prefixTxtInputLayout.getEditText().getText().toString());
        contentValues.put(DbHelper.LOOKUP_TAG_SUFFIX, suffixInputLayout.getEditText().getText().toString());
        contentValues.put(DbHelper.LOOKUP_EQU_TYPE, typeInputLayout.getEditText().getText().toString());
        contentValues.put(DbHelper.LOOKUP_MANUFACTURER, manufacturerInputLayout.getEditText().getText().toString());
        this.getContentResolver().insert(NetworkContentProvider.CONTENT_LOOKUP_URI, contentValues);
    }
}
