package com.example.android.sunshine.app.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by rodrigo on 27/02/15.
 */
public class WeatherDataParse {

    /**
     * Given a string of the form returned by the api call:
     * http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7
     * retrieve the maximum temperature for the day indicated by dayIndex
     * (Note: 0-indexed, so 0 would refer to the first day).
     */
    public static double getMaxTemperatureForDay(String weatherJsonStr, int dayIndex) throws JSONException {
        JSONObject json = new JSONObject(weatherJsonStr);
        JSONArray array = json.getJSONArray("list");
        JSONObject dia = array.getJSONObject(dayIndex);
        JSONObject temp = dia.getJSONObject("temp");
        return temp.getDouble("max");
    }

}
