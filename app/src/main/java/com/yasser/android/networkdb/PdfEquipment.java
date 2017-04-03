package com.yasser.android.networkdb;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PdfEquipment extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EQUIPMENT_PDF_LOADER = 4; // identifies the Loader
    String TAG = "PdfEquipment";
    private TableLayout tableLayout,tableLayout2,layoutHeader;
    private TableRow newRow, rowHeader;
    Cursor cursor = null;
    Uri allEqu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_equipment);
        layoutHeader = (TableLayout) findViewById(R.id.table3);
        tableLayout = (TableLayout) findViewById(R.id.table4);
        tableLayout2 = (TableLayout) findViewById(R.id.table4);
        allEqu = Uri.parse(NetworkContentProvider.CONTENT_SITE_EQU + "/allEquAndSites");

        getLoaderManager().initLoader(EQUIPMENT_PDF_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // create an appropriate CursorLoader based on the id argument;
        // only one Loader in this fragment, so the switch is unnecessary
        CursorLoader cursorLoader;

        switch (id) {
            case EQUIPMENT_PDF_LOADER:
                cursorLoader = new CursorLoader(getBaseContext(),
                        allEqu, // Uri of contact to display
                        null, // null projection returns all columns
                        null, // null selection returns all rows
                        null, // no selection arguments
                        DbHelper.SITE_NAME + " ASC"); // sort order
                break;
            default:
                cursorLoader = null;
                break;
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null & data.moveToFirst()) {
            for (int i = 0; i < data.getCount(); i++) {
                newRow = new TableRow(PdfEquipment.this);

                TextView hIdCol = new TextView(PdfEquipment.this);
                TextView hSiteNameCol = new TextView(PdfEquipment.this);
                TextView hDateCol = new TextView(PdfEquipment.this);
                TextView hTypeCol = new TextView(PdfEquipment.this);
                TextView hPrefixCol = new TextView(PdfEquipment.this);
                TextView hMiddleCol = new TextView(PdfEquipment.this);
                TextView hSuffixCol = new TextView(PdfEquipment.this);
                TextView hManufacturerCol = new TextView(PdfEquipment.this);
                TextView hSerialCol = new TextView(PdfEquipment.this);
                TextView hTimeStampCol = new TextView(PdfEquipment.this);

                hIdCol.setPadding(5, 5, 5, 5);
                hSiteNameCol.setPadding(5, 5, 5, 5);
                hDateCol.setPadding(5, 5, 5, 5);
                hTypeCol.setPadding(10, 5, 5, 5);
                hPrefixCol.setPadding(10, 5, 5, 5);
                hMiddleCol.setPadding(10, 5, 5, 5);
                hSuffixCol.setPadding(10, 5, 5, 5);
                hManufacturerCol.setPadding(10, 5, 5, 5);
                hSerialCol.setPadding(10, 5, 5, 5);
                hTimeStampCol.setPadding(10, 5, 5, 5);

                hIdCol.setMinWidth(300);
                hSiteNameCol.setMinWidth(400);
                hDateCol.setMinWidth(300);
                hTypeCol.setMinWidth(400);
                hPrefixCol.setMinWidth(200);
                hMiddleCol.setMinWidth(200);
                hSuffixCol.setMinWidth(200);
                hManufacturerCol.setMinWidth(200);
                hSerialCol.setMinWidth(200);
                hTimeStampCol.setMinWidth(400);

                hIdCol.setText(data.getString(data.getColumnIndex(DbHelper.EQU_SITE_NAME_ID)));
                hSiteNameCol.setText(data.getString(data.getColumnIndex(DbHelper.SITE_NAME)));
                hDateCol.setText(data.getString(data.getColumnIndex(DbHelper.EQU_DATE)));
                hTypeCol.setText(data.getString(data.getColumnIndex(DbHelper.EQU_TYPE)));
                hPrefixCol.setText(data.getString(data.getColumnIndex(DbHelper.EQU_TAG_PREFIX)));
                hMiddleCol.setText(data.getString(data.getColumnIndex(DbHelper.EQU_TAG_MIDDLE)));
                hSuffixCol.setText(data.getString(data.getColumnIndex(DbHelper.EQU_TAG_SUFFIX)));
                hManufacturerCol.setText(data.getString(data.getColumnIndex(DbHelper.EQU_MANUFACTURER)));
                hSerialCol.setText(data.getString(data.getColumnIndex(DbHelper.EQU_SERIAL)));
                hTimeStampCol.setText(data.getString(data.getColumnIndex(DbHelper.EQU_CREATED_AT)));

                newRow.addView(hIdCol);
                newRow.addView(hSiteNameCol);
                newRow.addView(hDateCol);
                newRow.addView(hTypeCol);
                newRow.addView(hPrefixCol);
                newRow.addView(hMiddleCol);
                newRow.addView(hSuffixCol);
                newRow.addView(hManufacturerCol);
                newRow.addView(hSerialCol);
                newRow.addView(hTimeStampCol);

                newRow.setId(i + 1);
                newRow.setClickable(true);
                tableLayout.addView(newRow);


                data.moveToNext();
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Starts a new or restarts an existing Loader in this manager
        getLoaderManager().restartLoader(EQUIPMENT_PDF_LOADER, null, this);
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
        inflater.inflate(R.menu.pdf_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle menu item selections
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_pdf:
                try {
                    createPdf();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void createPdf() throws FileNotFoundException, DocumentException {
        File myFile,pdfFolder;
        OutputStream output = null;
        //check if external storage is available and not read only
        if (!isExternalStorageWritable() || isExternalStorageReadable()) {
            pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), "Equipment");
            if (!pdfFolder.exists()) {
                pdfFolder.mkdir();
                Log.i(TAG, "Pdf Directory created");
            }

            //Create time stamp
            Date date = new Date() ;
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

            myFile = new File(pdfFolder + timeStamp + ".pdf");


            output = new FileOutputStream(myFile);
        }
        else {
            pdfFolder = new File(Environment.getExternalStorageDirectory(), "Equipment");
            if (!pdfFolder.exists()) {
                pdfFolder.mkdir();
                Log.i(TAG, "Pdf Directory created");
            }

            //Create time stamp
            Date date = new Date() ;
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

            myFile = new File(pdfFolder + timeStamp + ".pdf");


            output = new FileOutputStream(myFile);
        }

        //Step 1
        Document document = new Document(PageSize.A4.rotate());

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.open();

        PdfPTable table = new PdfPTable(10);
        table.setWidthPercentage(100);
        PdfPCell c1 = new PdfPCell(new Phrase("Id"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Site Name"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Date"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Type"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Tag_Prefix"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        c1 = new PdfPCell(new Phrase("Tag_Middle"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Tag_Suffix"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Manufacturer"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Serial No"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Time Stamp"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        table.setHeaderRows(1);

        for (int i = 0; i < tableLayout2.getChildCount(); i++) {
            TableRow mRow = (TableRow) tableLayout2.getChildAt(i);  //row number starts from zero
            for (int j=0;j <10;j++) {
                TextView mTextView = (TextView) mRow.getChildAt(j);  //column number starts from zero
                table.addCell(mTextView.getText().toString());
            }
        }

        document.add(table);

        //Step 5: Close the document
        document.close();

    }
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

}