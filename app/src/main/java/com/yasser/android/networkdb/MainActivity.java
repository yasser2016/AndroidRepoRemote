package com.yasser.android.networkdb;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> ,ListView.OnItemClickListener {


    private android.support.v7.widget.SearchView searchView;
    private MenuItem searchMenuItem;
    private ListView listView;
    private SimpleCursorAdapter cursorAdapter;
    private Uri siteUri,siteEquipmentUri;
    String TAG ="SiteList";
    String siteID;
    Toast toast;
    final String[] from=new String[]{
            DbHelper._ID,
            DbHelper.SITE_NAME,
            DbHelper.SITE_CODE
    };
    final int[] to=new int[]{
            R.id.id,
            R.id.name,
            R.id.code
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=(ListView) findViewById(R.id.list1);
        listView.setEmptyView(findViewById(R.id.empty));
        cursorAdapter=new SimpleCursorAdapter(this,R.layout.activity_view_record,null,from,to,0);
        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(this);
        FloatingActionButton addButton =
                (FloatingActionButton) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent addEditIntent = new Intent(MainActivity.this, AddEditSites.class);
                startActivity(addEditIntent);
            }
        });
        registerForContextMenu(listView);

        try {
            //the following implementation is for instant search
            final Cursor cursor = getContentResolver().query(
                    NetworkContentProvider.CONTENT_SITE_URI,
                    null,
                    null,
                    null,
                    DbHelper.SITE_NAME + " ASC"
            );
            cursorAdapter.setStringConversionColumn( cursor.getColumnIndex(from[0]) );
            int numColumns = cursor.getCount();
            toast.makeText(this, this.getResources().getString(R.string.site_numbers) + numColumns, Toast.LENGTH_LONG).show();
        } catch (Exception e) {

        }
        cursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {

                if (constraint == null || constraint.length() == 0) {
                    return getContentResolver().query(
                            NetworkContentProvider.CONTENT_SITE_URI,
                            null,
                            null,
                            null,
                            DbHelper.SITE_NAME + " ASC"
                    );
                }
                else {
                    return getContentResolver().query(
                            NetworkContentProvider.CONTENT_SITE_URI,
                            null,
                            DbHelper.SITE_NAME + " LIKE ?",
                            new String[] { "%"+constraint+"%" },
                            DbHelper.SITE_NAME + " ASC"
                    );
                }
            }
        });

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection={
                DbHelper._ID,
                DbHelper.SITE_NAME,
                DbHelper.SITE_CODE
        };
        CursorLoader cursorLoader = new CursorLoader(this, NetworkContentProvider.CONTENT_SITE_URI, projection,
                null, null, DbHelper.SITE_NAME + " ASC");
        return cursorLoader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) listView.getItemAtPosition(position);
        String listItemID = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper._ID));
        Bundle bundle = new Bundle();
        bundle.putString(EquipmentActivity.LIST1_ID, listItemID);
        Intent intent = new Intent(MainActivity.this, EquipmentActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        Log.d(TAG, "onCreate: " + listItemID);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        this.getMenuInflater().inflate(R.menu.sites_list_context_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        int position=info.position;
        Cursor cursor = (Cursor) listView.getItemAtPosition(position);
        siteID = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper._ID));
        siteUri = Uri.parse(NetworkContentProvider.CONTENT_SITE_URI + "/" + siteID);
        siteEquipmentUri = Uri.parse(NetworkContentProvider.CONTENT_SITE_URI + "/siteEquipment" + "/" + siteID);
        switch (item.getItemId()) {
            case R.id.menu_item_delete_site:
                deleteSite();
                return true;
            case R.id.menu_item_update_site:
                updateSiteData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void deleteSite(){
        confirmDelete.show(getFragmentManager(),"confirm delete");
    }

    private final DialogFragment confirmDelete = new DialogFragment(){
        @Override
        public Dialog onCreateDialog(Bundle bundle){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.confirm_title_site);
            builder.setMessage(R.string.confirm_message_site);

            builder.setPositiveButton(R.string.button_delete,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int button) {

                            MainActivity.this.getContentResolver().delete(siteUri, null, null);
                            MainActivity.this.getContentResolver().delete(siteEquipmentUri,null,null);
                        }
                    });
            builder.setNegativeButton(R.string.button_cancel, null);
            return builder.create();
        }
    };

    private void updateSiteData() {

        Bundle bundle6 = new Bundle();
        Log.d(TAG, "updateSiteData: " + siteID);
        bundle6.putString(UpdateSites.listItemID5,siteID);
        Log.d(TAG, "updateSiteData: " + bundle6);
        Intent updateSiteIntent = new Intent(MainActivity.this, UpdateSites.class);
        updateSiteIntent.putExtras(bundle6);
        startActivity(updateSiteIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.search);
        searchView=(android.support.v7.widget.SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this,
                SearchableActivity.class)));
        searchView.setIconifiedByDefault(true);

        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextChange(String newText)
            {
                // this is your adapter that will be filtered
                cursorAdapter.getFilter().filter(newText);
                System.out.println("on text chnge text: "+newText);
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                // this is your adapter that will be filtered
                cursorAdapter.getFilter().filter(query);
                System.out.println("on query submit: "+query);
                return true;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_lookup:
                Intent i = new Intent(MainActivity.this, LookupActivity.class);
                startActivity(i);
                return true;
            case R.id.menu_pdfEquipment:
                Intent intentPdfEquipment = new Intent(MainActivity.this, PdfEquipment.class);
                startActivity(intentPdfEquipment);
                return true;
            case R.id.menu_about:
                displayAboutDialog();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void displayAboutDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(
                MainActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle(this.getResources().getString(R.string.about));
        LayoutInflater inflater = getLayoutInflater();
        View inflaterLayout = inflater.inflate(R.layout.dialog_about_activity, null);

        // Setting Dialog Message
        ImageView image= (ImageView) inflaterLayout.findViewById(R.id.imageView2);
        image.setImageResource(R.drawable.yasser_20160501);
        TextView textView = (TextView) inflaterLayout.findViewById(R.id.textView2);

        // Setting Icon to Dialog
        alertDialog.setView(inflaterLayout);
        // Setting OK Button
        alertDialog.setButton(Dialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
              alertDialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
