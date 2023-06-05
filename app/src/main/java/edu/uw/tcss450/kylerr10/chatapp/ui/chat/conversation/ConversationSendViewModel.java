package edu.uw.tcss450.kylerr10.chatapp.ui.chat.conversation;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

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

import edu.uw.tcss450.kylerr10.chatapp.BuildConfig;
import edu.uw.tcss450.kylerr10.chatapp.io.RequestQueueSingleton;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.ChatViewModelHelper;

/**
 * ViewModel class that handles sending and receiving messages in a conversation.
 * @author Leyla Ahmed
 */
public class ConversationSendViewModel extends AndroidViewModel {

    // MutableLiveData to hold the send message response
    private final MutableLiveData<JSONObject> mSendMessage;

    // MutableLiveData to hold the get message response
    private final MutableLiveData<JSONObject> mGetMessage;

    // JWT token obtained from ChatViewModelHelper
    String jwt = ChatViewModelHelper.getJWT();

    // JWT token
    public String mJwt = jwt;


    /**
     * Constructor for ConversationSendViewModel.
     * Initializes the MutableLiveData objects.
     * @param application The application context
     */
    public ConversationSendViewModel(@NonNull Application application) {
        super(application);
        mSendMessage = new MutableLiveData<>();
        mSendMessage.setValue(new JSONObject());

        mGetMessage = new MutableLiveData<>();
        mGetMessage.setValue(new JSONObject());
    }

    /**
     * Sends a message in the specified chat.
     * @param chatId  The ID of the chat to send the message in
     * @param jwt     The JWT token for authentication
     * @param message The message to send
     */
    public void sendMessage(String chatId, String jwt, String message) {
        // Build the URL for the API endpoint
        String url = BuildConfig.BASE_URL + "/messages";

        // Create the JSON body for the request
        JSONObject body = new JSONObject();
        try {
            body.put("message", message);
            body.put("chatId", chatId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create the JsonObjectRequest for the POST request
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
                return headers;
            }
        };

        // Set the retry policy for the request
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

    /**
     * Retrieves messages for the specified chat.
     * It provides a callback implementation to handle the received message response.
     * @param chatId   The ID of the chat to retrieve messages for
     * @param callback The callback to handle the received message response
     */
    public void getMessage(String chatId, ConversationCallback callback) {
        String url = BuildConfig.BASE_URL + "/messages/" + chatId;

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
                    Log.e("GETMessages", "Error response: " + error);
                    // Pass the error response back to the callback
                    callback.onMessageReceived(null);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", mJwt);
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

    /**
     * Callback interface for receiving the API response.
     */
    public interface ConversationCallback {
        void onMessageReceived(JSONObject response);
    }
}
