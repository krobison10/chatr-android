package edu.uw.tcss450.kylerr10.chatapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentHomeBinding;
import edu.uw.tcss450.kylerr10.chatapp.listdata.Notification;
import edu.uw.tcss450.kylerr10.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.kylerr10.chatapp.ui.weather.ForecastViewModel;
import edu.uw.tcss450.kylerr10.chatapp.ui.weather.HourlyForecast;
import edu.uw.tcss450.kylerr10.chatapp.ui.weather.LocationViewModel;
import edu.uw.tcss450.kylerr10.chatapp.ui.weather.UserLocation;
import edu.uw.tcss450.kylerr10.chatapp.ui.weather.UserLocationViewModel;

/**
 * Fragment representing the main page of the Home activity for the app.
 *
 * @author Kyler Robison
 */
public class HomeFragment extends Fragment {
    FragmentHomeBinding mBinding;
    ForecastViewModel mForecastModel;
    LocationViewModel mLocationModel;
    UserLocationViewModel mUserLocationModel;
    HomeViewModel mHomeViewModel;

    NotificationsViewModel mNotificationsViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mForecastModel = new ViewModelProvider(requireActivity()).get(ForecastViewModel.class);
        mHomeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        mLocationModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        mUserLocationModel = new ViewModelProvider(requireActivity()).get(UserLocationViewModel.class);
        mNotificationsViewModel = new ViewModelProvider(requireActivity()).get(NotificationsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentHomeBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    /**
     * Events for when the fragment and its views are created.
     * Also handles building of the RecyclerView that displays notifications.
     *
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHomeViewModel.fetchUserData(new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class), mBinding);

        setWeather();

        mNotificationsViewModel.addNotificationsObserver(getViewLifecycleOwner(), notifications -> {
            mBinding.recyclerViewNotifications.setAdapter(
                    new NotificationsRecyclerViewAdapter(
                            notifications,
                            requireActivity(),
                            Navigation.findNavController(requireView()))
            );

            mBinding.labelNoNotifs.setVisibility(notifications.size() == 0 ? View.VISIBLE : View.GONE);
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                // Perform delete action for the swiped item
                NotificationsRecyclerViewAdapter adapter = (NotificationsRecyclerViewAdapter)
                        mBinding.recyclerViewNotifications.getAdapter();
                if (adapter != null) {
                    Notification deletedNotification = adapter.getNotificationAtPosition(position);
                    mNotificationsViewModel.clearNotification(deletedNotification);
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(mBinding.recyclerViewNotifications);



        mBinding.buttonClearallNotifications.setOnClickListener(
                button -> mNotificationsViewModel.clearAll());
    }

    /**
     * Populates the weather card.
     *
     * @author Jasper Newkirk
     */
    private void setWeather() {
        // Set the information for the weather card based on the forecast data from the API
        mForecastModel.addForecastObserver(getViewLifecycleOwner(), forecast -> {
            if (!forecast.getCity().isEmpty() && !forecast.getState().isEmpty()) {
                mBinding.textCitystateWeather.setText(String.format("%s, %s", forecast.getCity(), forecast.getState()));
                if (!forecast.getHourlyList().isEmpty()) {
                    HourlyForecast currentForecast = forecast.getHourlyList().get(0);
                    mBinding.textWeatherdescription.setText(currentForecast.getForecast());
                    mBinding.textTemperatureHigh.setText(currentForecast.getTemperature());
                    mBinding.imageWeathericon.setImageIcon(currentForecast.getForecastIcon(mBinding.homeWeatherCard));
                    mBinding.imageWeathericon.setVisibility(View.VISIBLE);
                    mLocationModel.addLocationObserver(getViewLifecycleOwner(), location -> {
                        if (location != null) {
                            // Determine if the location is the devices current location or a marked location
                            mBinding.textCurrentLocation.setText(
                                    location.getLatitude() == forecast.getLatitude()
                                            && location.getLongitude() == forecast.getLongitude()
                                            ? R.string.title_current_location
                                            : R.string.title_marked_location
                            );
                            // Determine if the current or marked location is a saved location
                            mUserLocationModel.addLocationObserver(getViewLifecycleOwner(), savedLocations -> {
                                if (savedLocations != null && savedLocations.size() > 0) {
                                    for (UserLocation savedLocation : savedLocations) {
                                        if (savedLocation.getLatitude() == forecast.getLatitude()
                                                && savedLocation.getLongitude() == forecast.getLongitude()) {
                                            mBinding.textCurrentLocation.setText(R.string.title_saved_location);
                                            return;
                                        }
                                    }
                                } else Log.e("LOCATIONINFO", "User location is null.");
                            });
                        } else Log.e("LOCATIONINFO", "Location is null.");
                    });
                } else Log.e("FORECASTINFO", "Hourly forecast list is empty.");
            } else Log.e("FORECASTINFO", "City/State for forecast is empty.");
        });
    }
}