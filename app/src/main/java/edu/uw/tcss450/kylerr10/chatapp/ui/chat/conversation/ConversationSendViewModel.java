package edu.uw.tcss450.kylerr10.chatapp.ui.chat.conversation;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.kylerr10.chatapp.io.RequestQueueSingleton;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.ChatViewModelHelper;

public class ConversationSendViewModel extends AndroidViewModel {

    private final MutableLiveData<JSONObject> mSendMessage;
    private final MutableLiveData<JSONObject> mGetMessage;

    String jwt = ChatViewModelHelper.getJWT();
    public String mJwt = jwt;


    public ConversationSendViewModel(@NonNull Application application) {
        super(application);
        mSendMessage = new MutableLiveData<>();
        mSendMessage.setValue(new JSONObject());

        mGetMessage = new MutableLiveData<>();
        mGetMessage.setValue(new JSONObject());
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mSendMessage.observe(owner, observer);
    }
    public void addResponseObserverGetMessage(@NonNull LifecycleOwner owner, @NonNull Observer<? super JSONObject> observer) {
        mGetMessage.observe(owner, observer);
    }

    public void sendMessage(String chatId, String jwt, String message) {
        String url = "http://10.0.2.2:5000/messages";

        JSONObject body = new JSONObject();
        try {
            body.put("message", message);
            Log.d("SENDChat", "message: " + message);
            body.put("chatId", chatId);
            Log.d("SENDChat", "chatid: " + chatId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                response -> {
                    Log.d("SENDChat", "Success response: " + response.toString());
                    // Handle the success response here
                },
                error -> {
                    handleError(error, mSendMessage);
                    Log.e("SENDChat", "Error response: " + error);
                    // Handle the error response here
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", jwt);
                Log.d("SENDChat1", "USER JWT: " + jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Log the request URL and body
        Log.d("SENDChat", "URL: " + url);
        Log.d("SENDChat", "Request Body: " + body.toString());

        // Add the request to the request queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    public void getMessage(String chatId, ConversationCallback callback) {
        String url = "http://10.0.2.2:5000/messages/" + chatId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Log.d("GETMessages", "Success response: " + response.toString());
                    // Pass the response back to the callback
                    callback.onMessageReceived(response);
                },
                error -> {
                    handleError(error, mGetMessage);
                    // Pass the error response back to the callback
                    callback.onMessageReceived(null);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", mJwt);
                Log.d("GETMessages", "USER JWT: " + mJwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Log the request URL
        Log.d("GETMessages", "URL: " + url);

        // Add the request to the request queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }


    private void handleError(final VolleyError error, final MutableLiveData<JSONObject> responseDestination) {
        if (Objects.isNull(error.networkResponse)) {
            Log.e("NETWORK ERROR", error.getMessage());
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    error.networkResponse.statusCode +
                            " " +
                            data);
        }
        responseDestination.postValue(null);
    }

    // Callback interface for receiving the API response
    public interface ConversationCallback {
        void onMessageReceived(JSONObject response);
    }
}
