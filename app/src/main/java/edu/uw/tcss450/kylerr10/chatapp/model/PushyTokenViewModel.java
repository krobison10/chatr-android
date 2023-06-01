package edu.uw.tcss450.kylerr10.chatapp.model;

import android.app.Application;
import android.os.AsyncTask;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.kylerr10.chatapp.io.RequestQueueSingleton;
import edu.uw.tcss450.kylerr10.chatapp.R;
import me.pushy.sdk.Pushy;


public class PushyTokenViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mPushyToken;
    private final MutableLiveData<JSONObject> mResponse;

    public PushyTokenViewModel(@NonNull Application application) {
        super(application);
        mPushyToken = new MutableLiveData<>();
        mPushyToken.setValue("");
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    /**
     * Register as an observer to listen for the PushToken.
     * @param owner the fragments lifecycle owner
     * @param observer the observer
     */
    public void addTokenObserver(@NonNull LifecycleOwner owner,
                                 @NonNull Observer<? super String> observer) {
        mPushyToken.observe(owner, observer);
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    public void retrieveToken(PushyTokenCallback callback) {
        if (!Pushy.isRegistered(getApplication().getApplicationContext())) {
            Log.d("PUSH VIEW MODEL", "FETCHING NEW TOKEN");
            new RegisterForPushNotificationsAsync(callback).execute();
        } else {
            Log.d("PUSH VIEW MODEL", "USING OLD TOKEN");
            String deviceToken = Pushy.getDeviceCredentials(getApplication().getApplicationContext()).token;
            callback.onTokenReceived(deviceToken);
        }
    }

    private class RegisterForPushNotificationsAsync extends AsyncTask<Void, Void, String> {
        private PushyTokenCallback callback;

        public RegisterForPushNotificationsAsync(PushyTokenCallback callback) {
            this.callback = callback;
        }

        protected String doInBackground(Void... params) {
            String deviceToken;
            try {
                deviceToken = Pushy.register(getApplication().getApplicationContext());
            } catch (Exception e) {
                return e.getMessage();
            }
            return deviceToken;
        }

        @Override
        protected void onPostExecute(String token) {
            if (token.isEmpty()) {
                Log.e("ERROR RETRIEVING PUSHY TOKEN", token);
                callback.onTokenError("Token retrieval failed");
            } else {
                callback.onTokenReceived(token);
            }
        }
    }

    // Update your sendTokenToWebservice method
    public void sendTokenToWebservice(final String token, final String jwt) {

        String url = "http://10.0.2.2:5000/auth";

        JSONObject body = new JSONObject();
        try {
            body.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                body, //push token found in the JSONObject body
                mResponse::setValue,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            try {
                mResponse.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:" + data +
                        "}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    public void deleteTokenFromWebservice(final String jwt) {
        String url = "http://10.0.2.2:5000/auth";
        Request request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                mResponse::setValue,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
// add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    public interface PushyTokenCallback {
        void onTokenReceived(String token);
        void onTokenError(String errorMessage);
    }

}
