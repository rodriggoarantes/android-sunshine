package com.example.android.sunshine.app.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.app.R;
import com.example.android.sunshine.app.Utility;
import com.example.android.sunshine.app.fragment.ForecastFragment;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {

    private static final String LOG_TAG = ForecastAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private static final int VIEW_TYPE_COUNT = 2;

    /**
     * Construtor
     * @param context
     * @param c
     * @param flags
     */
    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = R.layout.list_item_forecast;

        if (viewType == VIEW_TYPE_TODAY) {
            layoutId = R.layout.list_item_forecast_today;
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //OBTEM O OBJETO GUARDADO NA TAG DA VIEW
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Read weather icon ID from cursor
        int weatherId = cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID);

        // Use placeholder image for now
        int viewType = getItemViewType(cursor.getPosition());
        int resourceIdIcon = 0;
        if (viewType == VIEW_TYPE_TODAY) {
            //Art Colors
            resourceIdIcon = Utility.getArtResourceForWeatherCondition(weatherId);
        } else {
            //BlackAndWhite
            resourceIdIcon = Utility.getIconResourceForWeatherCondition(weatherId);
        }

        viewHolder.iconView.setImageResource(resourceIdIcon);

        // Read date from cursor
        long dateInMilis = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        viewHolder.dateView.setText(Utility.getFriendlyDayString(context, dateInMilis));

        // Read weather forecast from cursor
        String description = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        viewHolder.descriptionView.setText(description);

        // Read user preference for metric or imperial temperature units
        boolean isMetric = Utility.isMetric(context);

        // Read high temperature from cursor
        double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        viewHolder.highTempView.setText(Utility.formatTemperature(context, high, isMetric));

        double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
        viewHolder.lowTempView.setText(Utility.formatTemperature(context, low, isMetric));
    }




    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView descriptionView;
        public final TextView highTempView;
        public final TextView lowTempView;

        public ViewHolder(View v) {
            iconView = (ImageView) v.findViewById(R.id.list_item_icon);
            dateView = (TextView) v.findViewById(R.id.list_item_date_textview);
            descriptionView = (TextView) v.findViewById(R.id.list_item_forecast_textview);
            highTempView = (TextView) v.findViewById(R.id.list_item_high_textview);
            lowTempView = (TextView) v.findViewById(R.id.list_item_lower_textview);
        }
    }
}
