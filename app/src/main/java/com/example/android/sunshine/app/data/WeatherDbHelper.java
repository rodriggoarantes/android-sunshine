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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.sunshine.app.data.WeatherContract.LocationEntry;
import com.example.android.sunshine.app.data.WeatherContract.WeatherEntry;

/**
 * Manages a local database for weather data.
 */
public class WeatherDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "weather.db";

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final StringBuilder sqlWeather = new StringBuilder(" ");
        sqlWeather.append("CREATE TABLE ").append(WeatherEntry.TABLE_NAME).append(" ( ");
        sqlWeather.append(WeatherEntry._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        sqlWeather.append(WeatherEntry.COLUMN_LOC_KEY).append(" INTEGER NOT NULL, ");
        sqlWeather.append(WeatherEntry.COLUMN_DATE).append(" INTEGER NOT NULL, ");
        sqlWeather.append(WeatherEntry.COLUMN_SHORT_DESC).append(" TEXT NOT NULL, ");
        sqlWeather.append(WeatherEntry.COLUMN_WEATHER_ID).append(" INTEGER NOT NULL,");
        sqlWeather.append(WeatherEntry.COLUMN_MIN_TEMP).append(" REAL NOT NULL, ");
        sqlWeather.append(WeatherEntry.COLUMN_MAX_TEMP).append(" REAL NOT NULL, ");
        sqlWeather.append(WeatherEntry.COLUMN_HUMIDITY).append(" REAL NOT NULL, ");
        sqlWeather.append(WeatherEntry.COLUMN_PRESSURE).append(" REAL NOT NULL, ");
        sqlWeather.append(WeatherEntry.COLUMN_WIND_SPEED).append(" REAL NOT NULL, ");
        sqlWeather.append(WeatherEntry.COLUMN_DEGREES).append(" REAL NOT NULL, ");

        // Set up the location column as a foreign key to location table.
        sqlWeather.append(" FOREIGN KEY (").append(WeatherEntry.COLUMN_LOC_KEY).append(") REFERENCES ");
            sqlWeather.append(LocationEntry.TABLE_NAME).append(" (").append(LocationEntry._ID).append(") , ");
            sqlWeather.append(" UNIQUE (").append(WeatherEntry.COLUMN_DATE).append(", ");
            sqlWeather.append(WeatherEntry.COLUMN_LOC_KEY).append(" ) ON CONFLICT REPLACE ");
        sqlWeather.append(" ); ");


        final StringBuilder sqlLocation = new StringBuilder(" ");
        sqlLocation.append("CREATE TABLE ").append(LocationEntry.TABLE_NAME).append(" ( ");
        sqlLocation.append(LocationEntry._ID).append(" INTEGER PRIMARY KEY , ");
        sqlLocation.append(LocationEntry.COLUMN_LOCATION_SETTING).append(" TEXT UNIQUE NOT NULL , ");
        sqlLocation.append(LocationEntry.COLUMN_CITY_NAME).append(" TEXT NOT NULL , ");
        sqlLocation.append(LocationEntry.COLUMN_COORD_LAT).append(" REAL NOT NULL , ");
        sqlLocation.append(LocationEntry.COLUMN_COORD_LONG).append(" REAL NOT NULL ");
        sqlLocation.append(" ); ");

        sqLiteDatabase.execSQL(sqlWeather.toString());
        sqLiteDatabase.execSQL(sqlLocation.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
