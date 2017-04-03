package com.yasser.android.networkdb;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.support.design.widget.TextInputEditText;

public class UpdateSites extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,View.OnClickListener{
    public static final String listItemID5 = "com.yasser.android.networkdb.key5";
    public static final int UPDATE_SITES_LOADER = 30;
    private TextInputLayout nameInputLayout,codeInputLayout;
    private TextInputEditText nameEditTxt,codeEditTxt;
    private FloatingActionButton updateSiteFAB;
    String TAG = "UpdateSite";
    CursorLoader cursorLoader1;
    Bundle bundle7;
    String itemID5;
    Uri siteUri2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sites);

        nameInputLayout = (TextInputLayout) findViewById(R.id.siteNameTxtInputLayout);
        codeInputLayout = (TextInputLayout) findViewById(R.id.siteCodeTxtInputLayout);

        nameEditTxt = (TextInputEditText) findViewById(R.id.siteNameEditTxt);
        codeEditTxt = (TextInputEditText) findViewById(R.id.siteCodeEditTxt);

        updateSiteFAB = (FloatingActionButton) findViewById(R.id.saveSitesFAB);
        updateSiteFAB.setOnClickListener(this);

        bundle7 = this.getIntent().getExtras();
        if (bundle7 != null) {
            itemID5 = bundle7.getString(listItemID5);
            siteUri2 = Uri.parse(NetworkContentProvider.CONTENT_SITE_URI + "/" + itemID5);
        }
        Log.d(TAG, "onCreate: " + itemID5);
        Log.d(TAG, "onCreate: " + siteUri2);
        getLoaderManager().initLoader(UPDATE_SITES_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // create an appropriate CursorLoader based on the id argument;
        // only one Loader in this fragment, so the switch is unnecessary
        switch (id) {
            case UPDATE_SITES_LOADER:
                return new CursorLoader(getBaseContext(),
                        siteUri2, // Uri of contact to display
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
                nameInputLayout.getEditText().setText(data.getString(data.getColumnIndex(DbHelper.SITE_NAME)));
                Log.d(TAG, "onLoadFinished: " + nameEditTxt);
                codeInputLayout.getEditText().setText(data.getString(data.getColumnIndex(DbHelper.SITE_CODE)));
            } catch (Exception e) {

            }

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        saveSiteUpdate();
        finish();

    }

    private void saveSiteUpdate() {
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(DbHelper._ID, itemID5);
            contentValues.put(DbHelper.SITE_NAME, nameInputLayout.getEditText().getText().toString());
            contentValues.put(DbHelper.SITE_CODE, codeInputLayout.getEditText().getText().toString());

            this.getContentResolver().update(siteUri2, contentValues, null, null);
        } catch (Exception e) {

        }
    }
}
