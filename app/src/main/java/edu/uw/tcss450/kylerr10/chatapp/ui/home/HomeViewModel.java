package edu.uw.tcss450.kylerr10.chatapp.ui.home;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.android.jwt.JWT;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.uw.tcss450.kylerr10.chatapp.BuildConfig;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentHomeBinding;
import edu.uw.tcss450.kylerr10.chatapp.model.UserInfoViewModel;

/**
 * @author Betelhem
 * ViewModel class for the HomeFragment handles fetching user data from the server.
 */

public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mResponse;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
    }

    public MutableLiveData<JSONObject> getResponse() {
        return mResponse;
    }
    /**
     * Fetches user data from the server and updates the UI.
     *
     * @param userInfoViewModel The UserInfoViewModel instance for accessing the JWT token.
     * @param binding           The FragmentHomeBinding instance for accessing UI views.
     */
    public void fetchUserData(UserInfoViewModel userInfoViewModel, FragmentHomeBinding binding) {
        String url = BuildConfig.BASE_URL + "/user";
        JWT jwtToken = userInfoViewModel.getJWT();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse the JSON response and extract user information
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                String firstName = response.getString("firstName");
                                String lastName = response.getString("lastName");
                                String userName = response.getString("username");
                                String greeting = "Hello, " + firstName;

                                // Update your UI with the greeting
                                binding.textGreeting.setText(greeting);
                            } else {
                                // Handle error case where success is false
                                String message = response.getString("Not success");
                                Log.e("FETCH", "Error: " + message);
                                binding.textGreeting.setText("Hello");
                            }
                        } catch (JSONException e) {
                            Log.e("FETCH", "Error parsing JSON response: " + e.getMessage());
                            binding.textGreeting.setText("Hello");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error case where the network request fails
                        Log.e("FETCH", "Error fetching user data: " + error.getMessage());
                        binding.textGreeting.setText("Hello");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + jwtToken);
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(jsonObjectRequest);
    }


}
