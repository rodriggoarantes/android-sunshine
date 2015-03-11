/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine.app.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    /*
        Students: Uncomment this test once you've written the code to create the Location
        table.  Note that you will have to have chosen the same column names that I did in
        my solution for this test to compile, so if you haven't yet done that, this is
        a good time to change your column names to match mine.

        Note that this only tests that the Location table has the correct columns, since we
        give you the code for the weather table.  This test does not look at the
     */
    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(WeatherContract.LocationEntry.TABLE_NAME);
        tableNameHashSet.add(WeatherContract.WeatherEntry.TABLE_NAME);

        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly", c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + WeatherContract.LocationEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(WeatherContract.LocationEntry._ID);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_CITY_NAME);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_COORD_LAT);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_COORD_LONG);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                locationColumnHashSet.isEmpty());
        db.close();
    }

    /*
        Students:  Here is where you will build code to test that we can insert and query the
        location database.  We've done a lot of work for you.  You'll want to look in TestUtilities
        where you can uncomment out the "createNorthPoleLocationValues" function.  You can
        also make use of the ValidateCurrentRecord function from within TestUtilities.
    */
    public void testLocationTable() {

        SQLiteDatabase db = new WeatherDbHelper(this.mContext).getWritableDatabase();

        long rowId = insertLocation();

        //Verifica que funcionou o insert
        assertTrue(rowId != -1);

        // Query the database and receive a Cursor back
        /*Cursor cursor = db.query(WeatherContract.LocationEntry.TABLE_NAME, //table
                                new String[]{WeatherContract.WeatherEntry.COLUMN_SHORT_DESC}, //colunas
                                WeatherContract.WeatherEntry.COLUMN_DATE + " > ? ", // where
                                new String[] {"1419019200"}, //where ARGS
                                null, //GroupBy
                                null, //Having
                                WeatherContract.WeatherEntry.COLUMN_DATE + " DESC "); //OrderBy*/

        Cursor cursor = db.query(WeatherContract.LocationEntry.TABLE_NAME, //table
                                    null, //colunas
                                    null, // where
                                    null, //where ARGS
                                    null, //GroupBy
                                    null, //Having
                                    null); //OrderBy

        // Move the cursor to a valid database row
        //Testa se foi retornado alguma linha na consulta ao banco de dados
        assertTrue("Erro: sem registros no banco de dados", cursor.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        TestUtilities.validateCurrentRecord("ERRO", cursor, TestUtilities.createNorthPoleLocationValues());

        //Verifica se a consulta trouxe mais de um registro, o que estaria errado devido somente um item foi inserido
        assertFalse("Erro: mais de um registro foi retornado do banco de dados", cursor.moveToNext());

        // Finally, close the cursor and database
        cursor.close();
        db.close();
    }

    /*
        Students:  Here is where you will build code to test that we can insert and query the
        database.  We've done a lot of work for you.  You'll want to look in TestUtilities
        where you can use the "createWeatherValues" function.  You can
        also make use of the validateCurrentRecord function from within TestUtilities.
     */
    public void testWeatherTable() {
        // First insert the location, and then use the locationRowId to insert
        // the weather. Make sure to cover as many failure cases as you can.
        long locationRowId = insertLocation();

        // First step: Get reference to writable database
        SQLiteDatabase db = new WeatherDbHelper(this.mContext).getWritableDatabase();
        // Create ContentValues of what you want to insert
        ContentValues weatherValues = TestUtilities.createWeatherValues(locationRowId);

        // Insert ContentValues into database and get a row ID back
        long rowId = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, weatherValues);

        //Verifica que funcionou o insert
        assertTrue(rowId != -1);

        // Query the database and receive a Cursor back
        /*Cursor cursor = db.query(WeatherContract.WeatherEntry.TABLE_NAME, //table
                null, //colunas
                WeatherContract.WeatherEntry.COLUMN_LOC_KEY + " = ? ", // where
                new String[] {String.valueOf(locationRowId)}, //where ARGS
                null, //GroupBy
                null, //Having
                null); //OrderBy*/

        Cursor weatherCursor = db.query(WeatherContract.WeatherEntry.TABLE_NAME, //table
                null, //colunas
                null, // where
                null, //where ARGS
                null, //GroupBy
                null, //Having
                null); //OrderBy

        // Move the cursor to a valid database row
        //Testa se foi retornado alguma linha na consulta ao banco de dados
        assertTrue("Erro: sem registros no banco de dados", weatherCursor.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        // Validate data in resulting Cursor with the original ContentValues
        TestUtilities.validateCurrentRecord("ERRO testWeatherTable()", weatherCursor, weatherValues);

        //Verifica se a consulta trouxe mais de um registro, o que estaria errado devido somente um item foi inserido
        assertFalse("Erro: mais de um registro foi retornado do banco de dados", weatherCursor.moveToNext());

        // Finally, close the cursor and database
        weatherCursor.close();
        db.close();
    }


    /*
        Students: This is a helper method for the testWeatherTable quiz. You can move your
        code from testLocationTable to here so that you can call this code from both
        testWeatherTable and testLocationTable.
     */
    public long insertLocation() {
        return TestUtilities.insertNorthPoleLocationValues(this.mContext);
    }
}
