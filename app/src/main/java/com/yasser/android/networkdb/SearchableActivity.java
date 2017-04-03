package com.yasser.android.networkdb;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class SearchableActivity extends ListActivity {
    String TAG = "SearchableActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        handleIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent newIntent) {
        setIntent(newIntent);
        handleIntent(newIntent);
    }

    private void handleIntent(Intent intent) {
        String query = "";
        String intentAction = intent.getAction();
        if (Intent.ACTION_SEARCH.equals(intentAction)) {
            query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, "Search received: " + query, Toast.LENGTH_LONG)
                    .show();
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        String wildcardQuery = "%" + query + "%";
        Cursor cursor =
                managedQuery(
                        NetworkContentProvider.CONTENT_SITE_URI,
                        null,
                        DbHelper.SITE_NAME + " LIKE ? OR "
                                + DbHelper.SITE_CODE + " LIKE ?",
                        new String[] { wildcardQuery, wildcardQuery }, null);
        ListAdapter adapter =
                new SimpleCursorAdapter(
                        this,
                        android.R.layout.simple_list_item_1,
                        cursor,
                        new String[] { DbHelper.SITE_NAME },
                        new int[] { android.R.id.text1 },0);
        setListAdapter(adapter);
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
       /* String rowId = String.valueOf(id);
        Intent activitiesActivity = new Intent(getApplicationContext(), SiteVisitActivities.class);
        Bundle bundle = new Bundle();
        bundle.putString(SiteVisitActivities.ROW_ID, rowId);
        activitiesActivity.putExtras(bundle);
        startActivity(activitiesActivity);
        Log.d(TAG, "onItemClick: "+" "+ bundle);*/
    }
}
