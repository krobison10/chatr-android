package edu.uw.tcss450.kylerr10.chatapp.ui.setting;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.android.jwt.JWT;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.kylerr10.chatapp.model.UserInfoViewModel;

/**
 * @author Betelhem
 * ViewModel class for the ChangePasswrodFragment handles changing password from the server.
 */

public class ChangePassViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mResponse;
    private UserInfoViewModel userInfoViewModel;

    public ChangePassViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
    }

    public void setUserInfoViewModel(UserInfoViewModel userInfoViewModel) {
        this.userInfoViewModel = userInfoViewModel;
    }

    public MutableLiveData<JSONObject> getResponse() {
        return mResponse;
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    /**
     * Change the user's password.
     *
     * @param email The user's email.
     * @param oldPassword The old password.
     * @param newPassword The new password.
     */
    public void changePassword(String email, String oldPassword, String newPassword) {
        String url = "http://10.0.2.2:5000/changePassword";
        JWT jwtToken = userInfoViewModel.getJWT();
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("email", email);
            requestBody.put("oldPassword", oldPassword);
            requestBody.put("newPassword", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestBody,
                response -> mResponse.setValue(response),
                error -> {
                    JSONObject errorResponse = new JSONObject();
                    try {
                        if (error.getMessage() != null) {
                            // Extract error message from the error response
                            String errorMessage = new JSONObject(error.getMessage()).getString("error");
                            errorResponse.put("error", errorMessage);
                        } else {

                            errorResponse.put("error", "Failed to update password incorrect email or password.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                        try {
                            errorResponse.put("error", "Failed to update password incorrect email or password.");
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                    mResponse.setValue(errorResponse);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + jwtToken);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                60_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * Handle the failure of connection
     * @param error error
     */
    private void handleError(VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                mResponse.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:\"" + data +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }
}

