package com.yasser.android.networkdb;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class AddEditSites extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        View.OnClickListener{
    private TextInputLayout nameInputLayout,codeInputLayout;
    private TextInputEditText nameEditTxt,codeEditTxt;
    String TAG = "AddEditSites";
    FloatingActionButton saveSitesFAB;
    private CursorLoader cursorLoader;
    private Handler handler = new Handler();
    private Toast mToastToShow;
    String siteCode,t1;
    int toastDurationInMilliSeconds = 10000;
    CountDownTimer toastCountDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_sites);

        nameInputLayout = (TextInputLayout) findViewById(R.id.siteNameTxtInputLayout);
        codeInputLayout = (TextInputLayout) findViewById(R.id.siteCodeTxtInputLayout);

        nameEditTxt = (TextInputEditText) findViewById(R.id.siteNameEditTxt);
        codeEditTxt = (TextInputEditText) findViewById(R.id.siteCodeEditTxt);

        saveSitesFAB = (FloatingActionButton) findViewById(R.id.saveSitesFAB);
        saveSitesFAB.setOnClickListener(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection={
                DbHelper._ID,
                DbHelper.SITE_NAME,
                DbHelper.SITE_CODE
        };
        cursorLoader = new CursorLoader(this, NetworkContentProvider.CONTENT_SITE_URI, projection,
                null, null, DbHelper.SITE_NAME + "ASC");
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null & data.moveToFirst()) {
            nameInputLayout.getEditText().setText(data.getString(data.getColumnIndex(DbHelper.SITE_NAME)));
            codeInputLayout.getEditText().setText(data.getString(data.getColumnIndex(DbHelper.SITE_CODE)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                saveSitesName();
                Intent mainActivity = new Intent(AddEditSites.this, MainActivity.class);
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainActivity);
    }

    private void saveSitesName() {
        boolean confirm = false;
        siteCode = codeInputLayout.getEditText().getText().toString();
        ContentValues contentValues = new ContentValues();
        String[] projection = {DbHelper.SITE_CODE};
        Cursor cursor = getContentResolver().query(NetworkContentProvider.CONTENT_SITE_URI,
                projection, null, null, null);
        if (cursor != null) {
            for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
                t1 = cursor.getString(cursor.getColumnIndex(DbHelper.SITE_CODE));
                if (t1.equals(siteCode)) {
                    mToastToShow = Toast.makeText(this, R.string.site_exist, Toast.LENGTH_LONG);
                    toastCountDown = new CountDownTimer(toastDurationInMilliSeconds,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            mToastToShow.show();
                        }
                        @Override
                        public void onFinish() {
                            mToastToShow.cancel();
                        }
                    };
                    mToastToShow.show();
                    toastCountDown.start();
                    confirm = true;
                }
            }
        }
        if (!confirm) {
            contentValues.put(DbHelper.SITE_NAME, nameInputLayout.getEditText().getText().toString());
            contentValues.put(DbHelper.SITE_CODE, codeInputLayout.getEditText().getText().toString());
            this.getContentResolver().insert(NetworkContentProvider.CONTENT_SITE_URI, contentValues);
        }
    }




}
