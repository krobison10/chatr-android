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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;

import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.model.UserInfoViewModel;

public class UserLocationViewModel extends AndroidViewModel {

    private MutableLiveData<List<UserLocation>> mLocationList;

    /**
     * The ViewModel's constructor.
     * @param application the application.
     */
    public UserLocationViewModel(@NonNull Application application) {
        super(application);
        mLocationList = new MutableLiveData<>();
    }

    public void addLocationObserver(@NonNull LifecycleOwner owner,
                    @NonNull Observer<? super List<UserLocation>> observer) {
        mLocationList.observe(owner, observer);
    }

    /**
     * Logs any error that occurs retrieving the location data from the API.
     * @param error the error that occurred.
     */
    public void handleError(final VolleyError error) {
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
    }

    /**
     * Handles the result of the API GET call.
     * @param result the result of the API call.
     */
    public void handleGetResult(final JSONObject result) {
        IntFunction<String> getString = getApplication().getResources()::getString;
        try {
            JSONObject root = result;
            // Check response has valid format
            if (root.has(getString.apply(R.string.keys_json_location_list))) {
                // Initialize mLocationList
                mLocationList.setValue(new ArrayList<>());
                JSONArray locations
                        = root.getJSONArray(getString.apply(R.string.keys_json_location_list));
                for (int i = 0; i < locations.length(); i++) {
                    JSONObject current = locations.getJSONObject(i);
                    // Check location has valid format
                    if (current.has(getString.apply(R.string.keys_json_location_primarykey))
                        && current.has(getString.apply(R.string.keys_json_location_nickname))
                        && current.has(getString.apply(R.string.keys_json_location_latitude))
                        && current.has(getString.apply(R.string.keys_json_location_longitude))
                    ) {
                        Objects.requireNonNull(mLocationList.getValue()).add(
                            new UserLocation(
                                current.getInt(getString.apply(R.string.keys_json_location_primarykey)),
                                current.getString(getString.apply(R.string.keys_json_location_nickname)),
                                current.getDouble(getString.apply(R.string.keys_json_location_latitude)),
                                current.getDouble(getString.apply(R.string.keys_json_location_longitude))
                            )
                        );
                    } else {
                        Log.e("JSON_PARSE_ERROR", "daily forecast has invalid format");
                    }
                }
            } else {
                Log.e("JSON_PARSE_ERROR", "forecast GET response has invalid format");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSON_PARSE_ERROR", e.getMessage());
        }
        mLocationList.setValue(mLocationList.getValue()); // necessary to trigger observers to update
        Log.d("LOCATION LIST", Objects.requireNonNull(mLocationList.getValue()).toString());
    }

    /**
     * Connects to the API to get the users saved location data.
     * @param activity the activity that is connecting to the API.
     */
    public void connectGet(ViewModelStoreOwner activity) {
        String url = "http://10.0.2.2:5000/location/"; // TODO: MAKE THIS AN ENV VARIABLE
        //+ (location == null ? "" : location.getLatitude() + "," + location.getLongitude()); AN IDEA FOR LATER
        Request<JSONObject> request = new JsonObjectRequest(Request.Method.GET, url, null,
                this::handleGetResult, this::handleError) {
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
