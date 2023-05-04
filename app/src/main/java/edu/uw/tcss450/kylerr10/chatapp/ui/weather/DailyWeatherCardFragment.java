package edu.uw.tcss450.kylerr10.chatapp.ui.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.kylerr10.chatapp.R;

/**
 * A simple {@link Fragment} subclass responsible for relaying daily forecast information to the user.
 * @author Jasper Newkirk
 */
public class DailyWeatherCardFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily_weather_card, container, false);
    }
}