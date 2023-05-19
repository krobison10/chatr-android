package edu.uw.tcss450.kylerr10.chatapp.ui.chat;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * ViewModel class for managing chat-related data and operations.
 * @author Leyla Ahmed
 */
public class ChatViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mResponse;


    private String mJwt;

    /**
     * Constructor for ChatViewModel.
     * @param application The application context
     */
    public ChatViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }


    /**
     * Creates a new chat room.
     * @param chatName The name of the chat room to create
     */
    public void createChatRoom(String chatName) {
        String url = "http://10.0.2.2:5000/chats";
        JSONObject body = new JSONObject();
        try {
            body.put("name", chatName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                mResponse::setValue,
                this::handleError
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + mJwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * Retrieves the list of chat rooms.
     */
    public void getChatRooms() {
        String url = "http://10.0.2.2:5000/chats";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        JSONArray chatRoomsArray = response.getJSONArray("chatRooms");
                        int rowCount = response.getInt("rowCount");
                        JSONObject responseData = new JSONObject();
                        responseData.put("rowCount", rowCount);
                        responseData.put("chatRooms", chatRoomsArray);

                        // Update the MutableLiveData with the chat rooms response
                        mResponse.setValue(responseData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                this::handleError
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + mJwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * Deletes a chat room.
     * @param chatId The ID of the chat room to delete
     */
    public void deleteChatRoom(String chatId) {
        String url = "http://10.0.2.2:5000/chats";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                response -> mResponse.setValue(response),
                this::handleError
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + mJwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

//Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * Adds a user to a chat room.
     * @param chatId The ID of the chat room to add the user to
     */
    public void addUserToChat(String chatId) {
        String url = "http://10.0.2.2:5000/chats/";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                null,
                response -> mResponse.setValue(response),
                this::handleError
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + mJwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * Handles errors that occur during API requests.
     * @param error The VolleyError object representing the error
     */
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleChatRoomsError");
            }
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                JSONObject response = new JSONObject();
                response.put("code", error.networkResponse.statusCode);
                response.put("data", new JSONObject(data));
                mResponse.setValue(response);
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleChatRoomsError");
            }
        }
    }

    /**
     * Sets the JWT (JSON Web Token) for authentication.
     * @param jwt The JWT to set
     */
    public void setJWT(String jwt) {
        this.mJwt = jwt;
    }
}