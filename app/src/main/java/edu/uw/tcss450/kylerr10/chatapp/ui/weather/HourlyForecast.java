package edu.uw.tcss450.kylerr10.chatapp.ui.weather;

import android.graphics.drawable.Icon;
import android.view.View;

import java.io.Serializable;

import edu.uw.tcss450.kylerr10.chatapp.R;

/**
 * Represents the data for a {@link edu.uw.tcss450.kylerr10.chatapp.ui.weather.HourlyWeatherCardFragment}
 * in the {@link edu.uw.tcss450.kylerr10.chatapp.ui.weather.WeatherFragment}. Is used by the
 * {@link edu.uw.tcss450.kylerr10.chatapp.ui.home.HourlyWeatherCardRecyclerViewAdapter}.
 *
 * @author Jasper Newkirk
 */
public class HourlyForecast implements Serializable {
    // Fields are temporary and unused.
    private int hour = 12;
    private int temperature = 99;
    private String condition = "Sunny";

    public String getHour() {
        return hour == 12 ? "12 PM" : hour > 11 ? (hour - 12) + " PM" : hour + " AM";
    }

    public String getTemperature() {
        return temperature + "°F";
    }

    public Icon getCondition(View view) {
        return this.condition.contains("Cloudy")
                ? Icon.createWithResource(view.getContext(), R.drawable.ic_cloudy_onsecondarycontainer_24dp)
                : Icon.createWithResource(view.getContext(), R.drawable.ic_sunny_onsecondarycontainer_24dp);
    }

    public HourlyForecast(int hour, int temperature, String condition) {
        this.hour = hour;
        this.temperature = temperature;
        this.condition = condition;
    }
}