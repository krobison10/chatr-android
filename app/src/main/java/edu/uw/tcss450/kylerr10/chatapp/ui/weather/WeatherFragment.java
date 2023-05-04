package edu.uw.tcss450.kylerr10.chatapp.ui.weather;

import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.kylerr10.chatapp.ui.home.DailyWeatherCardRecyclerViewAdapter;
import edu.uw.tcss450.kylerr10.chatapp.ui.home.HourlyWeatherCardRecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass responsible for relaying weather information to the user.
 * @author Jasper Newkirk
 */
public class WeatherFragment extends Fragment {

    private WeatherViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    /**
     * Events for when the fragment and its views are created.
     * Also handles building of the RecyclerView that displays hourly and daily forecasts.
     *
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentWeatherBinding binding = FragmentWeatherBinding.bind(requireView());

        // Create a list of dummy dailyForecasts and hourlyForecasts
        ArrayList<DailyForecast> dailyForecasts = new ArrayList<>();
        dailyForecasts.add(new DailyForecast("Today", 82, 71, "Sunny"));
        dailyForecasts.add(new DailyForecast("Tomorrow", 76, 69, "Partly Cloudy"));
        dailyForecasts.add(new DailyForecast("Saturday", 75, 68, "Partly Cloudy"));
        dailyForecasts.add(new DailyForecast("Sunday", 76, 69, "Partly Cloudy"));
        dailyForecasts.add(new DailyForecast("Monday", 80, 72, "Sunny"));
        dailyForecasts.add(new DailyForecast("Tuesday", 68, 60, "Cloudy"));
        dailyForecasts.add(new DailyForecast("Wednesday", 81, 70, "Sunny"));

        ArrayList<HourlyForecast> hourlyForecasts = new ArrayList<>();
        hourlyForecasts.add(new HourlyForecast(9, 48, "Cloudy"));
        hourlyForecasts.add(new HourlyForecast(10, 48, "Sunny"));
        hourlyForecasts.add(new HourlyForecast(11, 50, "Sunny"));
        hourlyForecasts.add(new HourlyForecast(12, 50, "Cloudy"));
        hourlyForecasts.add(new HourlyForecast(13, 52, "Cloudy"));
        hourlyForecasts.add(new HourlyForecast(14, 53, "Cloudy"));
        hourlyForecasts.add(new HourlyForecast(15, 54, "Cloudy"));
        hourlyForecasts.add(new HourlyForecast(16, 55, "Sunny"));
        hourlyForecasts.add(new HourlyForecast(17, 54, "Sunny"));
        hourlyForecasts.add(new HourlyForecast(18, 52, "Sunny"));
        hourlyForecasts.add(new HourlyForecast(19, 51, "Cloudy"));
        hourlyForecasts.add(new HourlyForecast(20, 50, "Cloudy"));
        hourlyForecasts.add(new HourlyForecast(21, 49, "Cloudy"));
        hourlyForecasts.add(new HourlyForecast(22, 48, "Cloudy"));
        hourlyForecasts.add(new HourlyForecast(23, 48, "Cloudy"));
        binding.recyclerViewWeatherDaily.setAdapter(
                new DailyWeatherCardRecyclerViewAdapter(dailyForecasts)
        );
        binding.recyclerViewWeatherHourly.setAdapter(
                new HourlyWeatherCardRecyclerViewAdapter(hourlyForecasts)
        );


    }
}