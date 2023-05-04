package edu.uw.tcss450.kylerr10.chatapp.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentDailyWeatherCardBinding;
import edu.uw.tcss450.kylerr10.chatapp.ui.weather.DailyForecast;

/**
 * RecyclerView Adapter for the {@link edu.uw.tcss450.kylerr10.chatapp.ui.weather.DailyWeatherCardFragment}
 * in the {@link edu.uw.tcss450.kylerr10.chatapp.ui.weather.WeatherFragment}.
 *
 * @author Jasper Newkirk
 */
public class DailyWeatherCardRecyclerViewAdapter extends
        RecyclerView.Adapter<DailyWeatherCardRecyclerViewAdapter.DailyWeatherCardViewHolder> {
    /**
     * List of notifications
     */
    private List<DailyForecast> mDailyForecasts;

    public DailyWeatherCardRecyclerViewAdapter(List<DailyForecast> dailyForecasts) {
        mDailyForecasts = dailyForecasts;
    }

    @NonNull
    @Override
    public DailyWeatherCardViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        return new DailyWeatherCardViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.fragment_daily_weather_card, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull DailyWeatherCardViewHolder holder, int position) {
        //Sets the notification for a notification view
        holder.setDailyForecast(mDailyForecasts.get(position));
    }

    @Override
    public int getItemCount() {
        return mDailyForecasts.size();
    }

    public class DailyWeatherCardViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentDailyWeatherCardBinding mBinding;

        public DailyWeatherCardViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mBinding = FragmentDailyWeatherCardBinding.bind(itemView);
        }

        public void setDailyForecast(final DailyForecast dailyForecast) {
            // TODO: implement code to actually set values in the daily forecast
            mBinding.textTemperatureHigh.setText(dailyForecast.getTemperatureHigh());
            mBinding.textTemperatureLow.setText(dailyForecast.getTemperatureLow());
            mBinding.imageWeatherPreview.setImageIcon(dailyForecast.getCondition(mView));
            mBinding.textDay.setText(dailyForecast.getDay());
        }

    }
}
