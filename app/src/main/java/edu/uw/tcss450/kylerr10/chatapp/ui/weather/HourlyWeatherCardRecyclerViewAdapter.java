package edu.uw.tcss450.kylerr10.chatapp.ui.home;

import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.kylerr10.chatapp.Notification;
import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentHourlyWeatherCardBinding;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentNotificationBinding;
import edu.uw.tcss450.kylerr10.chatapp.ui.weather.HourlyForecast;

/**
 * RecyclerView Adapter for the {@link edu.uw.tcss450.kylerr10.chatapp.ui.weather.HourlyWeatherCardFragment}
 * in the {@link edu.uw.tcss450.kylerr10.chatapp.ui.weather.WeatherFragment}.
 *
 * @author Jasper Newkirk
 */
public class HourlyWeatherCardRecyclerViewAdapter extends
        RecyclerView.Adapter<HourlyWeatherCardRecyclerViewAdapter.HourlyWeatherCardViewHolder> {
    /**
     * List of notifications
     */
    private List<HourlyForecast> mHourlyForecasts;

    public HourlyWeatherCardRecyclerViewAdapter(List<HourlyForecast> hourlyForecasts) {
        mHourlyForecasts = hourlyForecasts;
    }

    @NonNull
    @Override
    public HourlyWeatherCardViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        return new HourlyWeatherCardViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.fragment_hourly_weather_card, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyWeatherCardViewHolder holder, int position) {
        //Sets the notification for a notification view
        holder.setHourlyForecast(mHourlyForecasts.get(position));
    }

    @Override
    public int getItemCount() {
        return mHourlyForecasts.size();
    }

    public class HourlyWeatherCardViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentHourlyWeatherCardBinding mBinding;

        public HourlyWeatherCardViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mBinding = FragmentHourlyWeatherCardBinding.bind(itemView);
        }

        public void setHourlyForecast(final HourlyForecast hourlyForecast) {
            // TODO: implement code to actually set values in the hourly forecast
            mBinding.textTemperature.setText(hourlyForecast.getTemperature());
            mBinding.imageWeatherPreview.setImageIcon(hourlyForecast.getCondition(mView));
            mBinding.textTime.setText(hourlyForecast.getHour());
        }

    }
}
