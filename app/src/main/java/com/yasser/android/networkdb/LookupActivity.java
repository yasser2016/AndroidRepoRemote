package com.yasser.android.networkdb;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class LookupActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        View.OnClickListener{
    String TAG ="LookupActivity";
    private TableLayout tableLayout,layoutHeader;
    private TableRow newRow,rowHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookup);
        layoutHeader = (TableLayout) findViewById(R.id.table3);
        tableLayout = (TableLayout) findViewById(R.id.table4);

        getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader;
                cursorLoader = new CursorLoader(getBaseContext(),
                        NetworkContentProvider.CONTENT_LOOKUP_URI, // Uri of contact to display
                        null, // null projection returns all columns
                        null, // null selection returns all rows
                        null, // no selection arguments
                        null); // sort order
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data!=null&data.moveToFirst()) {
            for (int i = 0; i < data.getCount(); i++) {
                newRow = new TableRow(LookupActivity.this);

                TextView hTypeCol = new TextView(LookupActivity.this);
                TextView hPrefixCol = new TextView(LookupActivity.this);
                TextView hSuffixCol = new TextView(LookupActivity.this);
                TextView hManufacturerCol = new TextView(LookupActivity.this);

                hTypeCol.setPadding(10,5,5,5);
                hPrefixCol.setPadding(10,5,5,5);
                hSuffixCol.setPadding(10,5,5,5);
                hManufacturerCol.setPadding(10,5,5,5);

                hTypeCol.setMinWidth(200);
                hPrefixCol.setMinWidth(200);
                hSuffixCol.setMinWidth(200);
                hManufacturerCol.setMinWidth(200);

                hTypeCol.setText(data.getString(data.getColumnIndex(DbHelper.LOOKUP_EQU_TYPE)));
                hPrefixCol.setText(data.getString(data.getColumnIndex(DbHelper.LOOKUP_TAG_PREFIX)));
                hSuffixCol.setText(data.getString(data.getColumnIndex(DbHelper.LOOKUP_TAG_SUFFIX)));
                hManufacturerCol.setText(data.getString(data.getColumnIndex(DbHelper.LOOKUP_MANUFACTURER)));

                newRow.addView(hTypeCol);
                newRow.addView(hPrefixCol);
                newRow.addView(hSuffixCol);
                newRow.addView(hManufacturerCol);

                newRow.setId(i+1);
                newRow.setClickable(true);

                newRow.setOnClickListener(LookupActivity.this);
                tableLayout.addView(newRow);


                data.moveToNext();
            }

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle menu item selections
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_insert:
                Intent intent = new Intent(LookupActivity.this,AddEditLookup.class);
                startActivityForResult(intent,0);
                return true;
            case R.id.action_delete:
                deleteLookups();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void deleteLookups(){
        confirmDelete.show(getFragmentManager(),"confirm delete");
    }

    private final DialogFragment confirmDelete = new DialogFragment(){
        @Override
        public Dialog onCreateDialog(Bundle bundle){
            AlertDialog.Builder builder = new AlertDialog.Builder(LookupActivity.this);
            builder.setTitle(R.string.confirm_title);
            builder.setMessage(R.string.confirm_message_lookup);

            builder.setPositiveButton(R.string.button_delete,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int button) {

                            LookupActivity.this.getContentResolver().delete(NetworkContentProvider.CONTENT_LOOKUP_URI,null,null);

                        }
                    });
            builder.setNegativeButton(R.string.button_cancel, null);
            return builder.create();
        }
    };

    @Override
    public void onClick(View v) {

    }
}
