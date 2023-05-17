package edu.uw.tcss450.kylerr10.chatapp.ui.weather;

import android.graphics.drawable.Icon;
import android.view.View;

import java.io.Serializable;

import edu.uw.tcss450.kylerr10.chatapp.R;

/**
 * Represents the data for a {@link edu.uw.tcss450.kylerr10.chatapp.ui.weather.DailyWeatherCardFragment}
 * in the {@link edu.uw.tcss450.kylerr10.chatapp.ui.weather.WeatherFragment}. Is used by the
 * {@link edu.uw.tcss450.kylerr10.chatapp.ui.home.DailyWeatherCardRecyclerViewAdapter}.
 *
 * @author Jasper Newkirk
 */
public class DailyForecast implements Serializable {
    // Fields are temporary and unused.
    private String day = "Today";
    private int temperatureHigh = 99;
    private int temperatureLow = 99;
    private String condition = "Sunny";

    public String getDay() {
        return day;
    }

    public String getTemperatureHigh() {
        return temperatureHigh + "°";
    }

    public String getTemperatureLow() {
        return temperatureLow + "°";
    }

    public Icon getCondition(View view) {
        return this.condition.contains("Cloudy")
                ? Icon.createWithResource(view.getContext(), R.drawable.ic_cloudy_onsecondarycontainer_24dp)
                : Icon.createWithResource(view.getContext(), R.drawable.ic_sunny_onsecondarycontainer_24dp);
    }

    public DailyForecast(String day, int temperatureHigh, int temperatureLow, String condition) {
        this.day = day;
        this.temperatureHigh = temperatureHigh;
        this.temperatureLow = temperatureLow;
        this.condition = condition;
    }
}