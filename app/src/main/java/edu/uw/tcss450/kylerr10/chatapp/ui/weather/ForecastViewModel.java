package edu.uw.tcss450.kylerr10.chatapp.ui.weather;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;

import edu.uw.tcss450.kylerr10.chatapp.BuildConfig;
import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.model.UserInfoViewModel;

/**
 * A ViewModel that stores the forecast data for the weather page.
 * @author Jasper Newkirk
 */
public class ForecastViewModel extends AndroidViewModel {
    /**
     * The forecast data.
     */
    private final MutableLiveData<Forecast> mForecast;

    /**
     * The ViewModel's constructor.
     * @param application the application.
     */
    public ForecastViewModel(@NonNull Application application) {
        super(application);
        mForecast = new MutableLiveData<>();
    }

    /**
     * Gets the forecast data from the API.
     * @param owner the owner of the ViewModel.
     * @param observer the observer of the ViewModel.
     */
    public void addForecastObserver(
            @NonNull LifecycleOwner owner,
            @NonNull Observer<? super Forecast> observer
    ) {
        mForecast.observe(owner, observer);
    }

    /**
     * Logs any error that occurs retrieving the forecast data from the API.
     * @param error the error that occurred.
     */
    public void handleError(final VolleyError error) {
        Log.e("CONNECTION ERROR", "Error connecting to API");
    }

    /**
     * Handles the result of the API call. Ultimately responsible for storing the forecast data
     * in the ViewModel.
     * @param result the result of the API call.
     */
    public void handleResult(final JSONObject result, double latitude, double longitude) {
        IntFunction<String> getString = getApplication().getResources()::getString;
        try {
            JSONObject root = result;
            // Check response has valid format
            if (root.has(getString.apply(R.string.keys_json_forecast_city))
                && root.has(getString.apply(R.string.keys_json_forecast_state))
                && root.has(getString.apply(R.string.keys_json_forecast_daily))
                && root.has(getString.apply(R.string.keys_json_forecast_hourly))) {
                String city = root.getString(getString.apply(R.string.keys_json_forecast_city));
                String state = root.getString(getString.apply(R.string.keys_json_forecast_state));
                // Initialize mForecast
                mForecast.setValue(new Forecast(city, state, new ArrayList<>(), new ArrayList<>(), latitude, longitude));
                JSONArray dailyForecast = root.getJSONArray(getString.apply(R.string.keys_json_forecast_daily));
                for (int i = 0; i < dailyForecast.length(); i++) {
                    JSONObject current = dailyForecast.getJSONObject(i);
                    // Check daily forecast has valid format
                    if (current.has(getString.apply(R.string.keys_json_forecast_daily_day))
                        && current.has(getString.apply(R.string.keys_json_forecast_daily_temperature_high))
                        && current.has(getString.apply(R.string.keys_json_forecast_daily_temperature_low))
                        && current.has(getString.apply(R.string.keys_json_forecast_daily_temperature_unit))
                        && current.has(getString.apply(R.string.keys_json_forecast_daily_short_forecast))
                    ) {
                        Objects.requireNonNull(mForecast.getValue()).getDailyList().add(
                            new DailyForecast(
                                current.getString(getString.apply(R.string.keys_json_forecast_daily_day)),
                                current.getInt(getString.apply(R.string.keys_json_forecast_daily_temperature_high)),
                                current.getInt(getString.apply(R.string.keys_json_forecast_daily_temperature_low)),
                                current.getString(getString.apply(R.string.keys_json_forecast_daily_temperature_unit)),
                                current.getString(getString.apply(R.string.keys_json_forecast_daily_short_forecast))
                            )
                        );
                    } else {
                        Log.e("JSON_PARSE_ERROR", "daily forecast has invalid format");
                    }
                }
                JSONArray hourlyForecast = root.getJSONArray(getString.apply(R.string.keys_json_forecast_hourly));
                for (int i = 0; i < hourlyForecast.length(); i++) {
                    JSONObject current = hourlyForecast.getJSONObject(i);
                    // Check hourly forecast has valid format
                    if (current.has(getString.apply(R.string.keys_json_forecast_hourly_time))
                        && current.has(getString.apply(R.string.keys_json_forecast_hourly_is_daytime))
                        && current.has(getString.apply(R.string.keys_json_forecast_hourly_temperature))
                        && current.has(getString.apply(R.string.keys_json_forecast_hourly_temperature_unit))
                        && current.has(getString.apply(R.string.keys_json_forecast_hourly_short_forecast))
                    ) {
                        Objects.requireNonNull(mForecast.getValue()).getHourlyList().add(
                            new HourlyForecast(
                                current.getString(getString.apply(R.string.keys_json_forecast_hourly_time)),
                                current.getBoolean(getString.apply(R.string.keys_json_forecast_hourly_is_daytime)),
                                current.getInt(getString.apply(R.string.keys_json_forecast_hourly_temperature)),
                                current.getString(getString.apply(R.string.keys_json_forecast_hourly_temperature_unit)),
                                current.getString(getString.apply(R.string.keys_json_forecast_hourly_short_forecast))
                            )
                        );
                    } else {
                        Log.e("JSON_PARSE_ERROR", "hourly forecast has invalid format");
                    }
                }
            } else {
                Log.e("JSON_PARSE_ERROR", "forecast GET response has invalid format");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSON_PARSE_ERROR", e.getMessage());
        }
        mForecast.setValue(mForecast.getValue()); // necessary to trigger observers to update
    }

    /**
     * Connects to the API to get the forecast data.
     * @param activity the activity that is connecting to the API.
     * @param latitude the latitude to get the forecast for.
     * @param longitude the longitude to get the forecast for.
     */
    public void connectGet(ViewModelStoreOwner activity, double latitude, double longitude) {
        String url = BuildConfig.BASE_URL + "/forecast/"
                + String.format(Locale.US, "%.4f,%.4f", latitude, longitude);
        Request<JSONObject> request = new JsonObjectRequest(Request.Method.GET, url, null,
                (r) -> handleResult(r, latitude, longitude), this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                UserInfoViewModel model = new ViewModelProvider(activity).get(UserInfoViewModel.class);
                headers.put("Authorization", model.getJWT().toString()); // JSON request body
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }
}
