package com.yasser.android.networkdb;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class EquipmentActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,View.OnClickListener,View.OnLongClickListener{

    public static final String LIST1_ID="com.yasser.android.networkdb.key2";

    private static final int EQUIPMENT_ITEMS_LOADER = 3; // identifies the Loader
    String TAG ="EquipmentActivity";
    private TableLayout tableLayout,layoutHeader;
    private TableRow newRow,rowHeader;
    private TextView lastTxtView,secondTxtView;
    private String lastTxtValue,secondTxtValue;
    String listID;
    Uri listURI,siteEquURI;
    Bundle bundle4,bundle2;
    Cursor cursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_items);
        layoutHeader = (TableLayout) findViewById(R.id.table1);
        tableLayout = (TableLayout) findViewById(R.id.table2);

        bundle2 = this.getIntent().getExtras();
        if (bundle2 != null) {
            listID = bundle2.getString(LIST1_ID);
            listURI = Uri.parse(NetworkContentProvider.CONTENT_SITE_EQU + "/" + listID);
            siteEquURI = Uri.parse(NetworkContentProvider.CONTENT_SITE_EQU + "/siteEqu" + "/" + listID);
        }

        if (listID != null) {
            bundle4 = new Bundle();
            bundle4.putString(AddEditEquipment.Bundle_ID, listID);
        }
        Log.d(TAG, "on create bundle: "+" "+ listID);
        Log.d(TAG, "listURI: "+" "+ listURI);
        Log.d(TAG, "bundle4: "+" "+ bundle4);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            if (NavUtils.getParentActivityName(this) != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        getLoaderManager().initLoader(EQUIPMENT_ITEMS_LOADER,null,this);
    }
    private void createHeaders() {
        rowHeader = new TableRow(EquipmentActivity.this);
        View hDivider = new View(this);
        hDivider.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
        hDivider.setBackgroundColor(Color.rgb(51, 51, 51));

        TextView hDateCol = new TextView(EquipmentActivity.this);
        TextView hTypeCol = new TextView(EquipmentActivity.this);
        TextView hPrefixCol = new TextView(EquipmentActivity.this);
        TextView hMiddleCol = new TextView(EquipmentActivity.this);
        TextView hSuffixCol = new TextView(EquipmentActivity.this);
        TextView hManufacturerCol = new TextView(EquipmentActivity.this);
        TextView hSerialCol = new TextView(EquipmentActivity.this);
        TextView hTimeStampCol = new TextView(EquipmentActivity.this);

        hDateCol.setPadding(5,5,5,5);
        hTypeCol.setPadding(10,5,5,5);
        hPrefixCol.setPadding(10,5,5,5);
        hMiddleCol.setPadding(10,5,5,5);
        hSuffixCol.setPadding(10,5,5,5);
        hManufacturerCol.setPadding(10,5,5,5);
        hSerialCol.setPadding(10,5,5,5);
        hTimeStampCol.setPadding(10,5,5,5);

        hDateCol.setMinWidth(300);
        hTypeCol.setMinWidth(200);
        hPrefixCol.setMinWidth(200);
        hMiddleCol.setMinWidth(200);
        hSuffixCol.setMinWidth(200);
        hManufacturerCol.setMinWidth(200);
        hSerialCol.setMinWidth(200);
        hTimeStampCol.setMinWidth(200);

        hDateCol.setText(R.string.tag_date);
        hTypeCol.setText(R.string.equ_type);
        hPrefixCol.setText(R.string.tag_prefix);
        hMiddleCol.setText(R.string.tag_middle);
        hSuffixCol.setText(R.string.tag_suffix);
        hManufacturerCol.setText(R.string.equ_manufacturer);
        hSerialCol.setText(R.string.equ_serial);
        hTimeStampCol.setText(R.string.time_stamp);

        rowHeader.addView(hDateCol);
        rowHeader.addView(hTypeCol);
        rowHeader.addView(hPrefixCol);
        rowHeader.addView(hMiddleCol);
        rowHeader.addView(hSuffixCol);
        rowHeader.addView(hManufacturerCol);
        rowHeader.addView(hSerialCol);
        rowHeader.addView(hTimeStampCol);

        layoutHeader.addView(rowHeader);
        layoutHeader.addView(hDivider);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // create an appropriate CursorLoader based on the id argument;
        // only one Loader in this fragment, so the switch is unnecessary
        CursorLoader cursorLoader;
        String[] projection = {
                DbHelper.EQU_SITE_NAME_ID,
                DbHelper.EQU_DATE,
                DbHelper.EQU_TAG_PREFIX,
                DbHelper.EQU_TAG_MIDDLE,
                DbHelper.EQU_TAG_SUFFIX,
                DbHelper.EQU_MANUFACTURER,
                DbHelper.EQU_TYPE,
                DbHelper.EQU_SERIAL,
                DbHelper.EQU_CREATED_AT
        };

        switch (id) {
            case EQUIPMENT_ITEMS_LOADER:
                cursorLoader = new CursorLoader(this,
                        listURI, // Uri of contact to display
                        projection, // null projection returns all columns
                        null, // null selection returns all rows
                        null, // no selection arguments
                        null); // sort order
                break;
            default:
                cursorLoader = null;
                break;
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data!=null && data.moveToFirst()) {
            for (int i = 0; i < data.getCount(); i++) {
                newRow = new TableRow(EquipmentActivity.this);

                TextView hDateCol = new TextView(EquipmentActivity.this);
                TextView hTypeCol = new TextView(EquipmentActivity.this);
                TextView hPrefixCol = new TextView(EquipmentActivity.this);
                TextView hMiddleCol = new TextView(EquipmentActivity.this);
                TextView hSuffixCol = new TextView(EquipmentActivity.this);
                TextView hManufacturerCol = new TextView(EquipmentActivity.this);
                TextView hSerialCol = new TextView(EquipmentActivity.this);
                TextView hTimeStampCol = new TextView(EquipmentActivity.this);

                hDateCol.setPadding(5,5,5,5);
                hTypeCol.setPadding(10,5,5,5);
                hPrefixCol.setPadding(10,5,5,5);
                hMiddleCol.setPadding(10,5,5,5);
                hSuffixCol.setPadding(10,5,5,5);
                hManufacturerCol.setPadding(10,5,5,5);
                hSerialCol.setPadding(10,5,5,5);
                hTimeStampCol.setPadding(10,5,5,5);

                hDateCol.setMinWidth(300);
                hTypeCol.setMinWidth(400);
                hPrefixCol.setMinWidth(200);
                hMiddleCol.setMinWidth(200);
                hSuffixCol.setMinWidth(200);
                hManufacturerCol.setMinWidth(200);
                hSerialCol.setMinWidth(200);
                hTimeStampCol.setMinWidth(400);

                hDateCol.setText(data.getString(data.getColumnIndex(DbHelper.EQU_DATE)));
                hTypeCol.setText(data.getString(data.getColumnIndex(DbHelper.EQU_TYPE)));
                hPrefixCol.setText(data.getString(data.getColumnIndex(DbHelper.EQU_TAG_PREFIX)));
                hMiddleCol.setText(data.getString(data.getColumnIndex(DbHelper.EQU_TAG_MIDDLE)));
                hSuffixCol.setText(data.getString(data.getColumnIndex(DbHelper.EQU_TAG_SUFFIX)));
                hManufacturerCol.setText(data.getString(data.getColumnIndex(DbHelper.EQU_MANUFACTURER)));
                hSerialCol.setText(data.getString(data.getColumnIndex(DbHelper.EQU_SERIAL)));
                hTimeStampCol.setText(data.getString(data.getColumnIndex(DbHelper.EQU_CREATED_AT)));

                newRow.addView(hDateCol);
                newRow.addView(hTypeCol);
                newRow.addView(hPrefixCol);
                newRow.addView(hMiddleCol);
                newRow.addView(hSuffixCol);
                newRow.addView(hManufacturerCol);
                newRow.addView(hSerialCol);
                newRow.addView(hTimeStampCol);

                newRow.setId(i+1);
                newRow.setClickable(true);

                newRow.setOnClickListener(EquipmentActivity.this);
                newRow.setOnLongClickListener(EquipmentActivity.this);
                tableLayout.addView(newRow);


                data.moveToNext();
            }

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        //Starts a new or restarts an existing Loader in this manager
        getLoaderManager().restartLoader(EQUIPMENT_ITEMS_LOADER, null, this);
    }
    @Override
    public void onPause() {
        super.onPause();
        //Starts a new or restarts an existing Loader in this manager
        tableLayout.removeAllViewsInLayout();
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    // display this fragment's menu items
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
            case android.R.id.home:
                if (NavUtils.getParentActivityName(this) != null) {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            case R.id.action_insert:
                Intent intent=new Intent(EquipmentActivity.this,AddEditEquipment.class);
                intent.putExtras(bundle4);
                startActivityForResult(intent,0);
                return true;
            case R.id.action_delete:
                deleteEquipment();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void deleteEquipment(){
        confirmDelete.show(getFragmentManager(),"confirm delete");
    }

    private final DialogFragment confirmDelete = new DialogFragment(){
        @Override
        public Dialog onCreateDialog(Bundle bundle){
            AlertDialog.Builder builder = new AlertDialog.Builder(EquipmentActivity.this);
            builder.setTitle(R.string.confirm_title);
            builder.setMessage(R.string.confirm_message_equ);

            builder.setPositiveButton(R.string.button_delete,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int button) {

                          EquipmentActivity.this.getContentResolver().delete(siteEquURI,null,null);

                        }
                    });
            builder.setNegativeButton(R.string.button_cancel, null);
            return builder.create();
        }
    };

    @Override
    public void onClick(View v) {
        int clicked_id = v.getId();
        try {
            TableRow row = (TableRow) tableLayout.getChildAt(clicked_id-1);
            TextView lastTxtView = (TextView) row.getChildAt(7);
            lastTxtValue = lastTxtView.getText().toString();
            Log.d(TAG, "onClick: "+" "+ lastTxtValue);
        }catch (Exception e){
            Log.e(TAG, "not created yet", e);  }
        new AlertDialog.Builder(EquipmentActivity.this)
                .setTitle(R.string.confirm_title)
                .setMessage(R.string.confirm_message_equ)
                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int which) {
                        updateData(listURI, lastTxtValue);

                    }
                })
                .setNegativeButton(R.string.delete,new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogInterface, int which) {
                        deleteData(listURI, lastTxtValue);
                    }
                })
                .setNeutralButton(R.string.button_cancel,new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .show();


    }
    private void deleteData(Uri uri,String lastTxtValue1) {
        String f1,f2;
        int position;
        cursor = getContentResolver().query(uri,null,null,null,null);
        if (cursor != null) {
            for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
                f1 = cursor.getString(cursor.getColumnIndex(DbHelper.EQU_CREATED_AT));
                if (f1.equals(lastTxtValue1)) {
                    f2 = cursor.getString(cursor.getColumnIndex(DbHelper.EQU_ID));
                    Uri uri1 = Uri.parse(NetworkContentProvider.CONTENT_SITE_EQU + "/" + f2);
                    getContentResolver().delete(uri1,null, null);
                }

            }
        }


    }
    private void updateData(Uri uri,String lastTxtValue2) {
        String f1,f2,t1;
        cursor = getContentResolver().query(uri,null,null,null,null);
        if (cursor != null) {
            for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
                f1 = cursor.getString(cursor.getColumnIndex(DbHelper.EQU_CREATED_AT));
                if (f1.equals(lastTxtValue2)) {
                    f2 = cursor.getString(cursor.getColumnIndex(DbHelper.EQU_ID));
                    Log.d(TAG, "f2: "+" "+ f2);
                    Uri uri1=Uri.parse(NetworkContentProvider.CONTENT_SITE_EQU + "/" + f2);
                    Log.d(TAG, "uri1: "+" "+ uri1);
                    bundle4 = new Bundle();
                    bundle4.putString(UpdateEquipment.listItemID3,f2);
                    bundle4.putString(UpdateEquipment.listItemID4,listID);
                    Intent updateIntent = new Intent(EquipmentActivity.this,UpdateEquipment.class);
                    updateIntent.putExtras(bundle4);
                    startActivity(updateIntent);

                }

            }
        }


    }




    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
