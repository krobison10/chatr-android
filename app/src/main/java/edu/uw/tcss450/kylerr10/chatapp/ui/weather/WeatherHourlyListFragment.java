package edu.uw.tcss450.kylerr10.chatapp.ui.weather;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.kylerr10.chatapp.R;

public class WeatherHourlyListFragment extends Fragment {

    private WeatherHourlyListViewModel mViewModel;

    public static WeatherHourlyListFragment newInstance() {
        return new WeatherHourlyListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_hourly_list, container, false);
    }

}