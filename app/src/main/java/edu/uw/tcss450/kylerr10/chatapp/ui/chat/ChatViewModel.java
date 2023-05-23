package edu.uw.tcss450.kylerr10.chatapp.ui.chat;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.android.jwt.JWT;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_members.ChatMember;

/**
 * ViewModel class for managing chat-related data and operations.
 * @author Leyla Ahmed
 */
public class ChatViewModel extends AndroidViewModel {
    //List of chat rooms used by the ChatRoomAdapter to display the list of chat rooms in a RecyclerView
    private List<ChatRoom> mChatRooms;

    //The ChatRoomAdapter instance used to populate the RecyclerView with chat room data
    private ChatRoomAdapter mAdapter = new ChatRoomAdapter();

    private final MutableLiveData<JSONObject> mCreatChatResponse;

    private final MutableLiveData<JSONObject> mDeleteChatResponse;

    private final MutableLiveData<JSONObject> mGetAllChatResponse;

    private final MutableLiveData<JSONObject> mAddUsersToChatResponse;

    private final MutableLiveData<JSONObject> mRemoveUserFromChatResponse;

    MutableLiveData<List<ChatRoom>> chatRoomsLiveData;
    private MutableLiveData<String> chatIdLiveData = new MutableLiveData<>();

    public String mJwt;

    /**
     * Constructor for ChatViewModel.
     * @param application The application context
     */
    public ChatViewModel(@NonNull Application application) {
        super(application);
        mCreatChatResponse = new MutableLiveData<>();
        mCreatChatResponse.setValue(new JSONObject());

        mGetAllChatResponse = new MutableLiveData<>();
        mGetAllChatResponse.setValue(new JSONObject());

        mDeleteChatResponse = new MutableLiveData<>();
        mDeleteChatResponse.setValue(new JSONObject());

        mAddUsersToChatResponse = new MutableLiveData<>();
        mAddUsersToChatResponse.setValue(new JSONObject());

        mRemoveUserFromChatResponse = new MutableLiveData<>();
        mRemoveUserFromChatResponse.setValue(new JSONObject());
    }



    /**
     * Creates a new chat room.
     * @param chatName The name of the chat room to create
     */
    public void createChatRoom(String chatName, List<String> userEmails) {
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
                response -> {
                    try {
                        String chatId = response.getString("chatid");
                        String memberId = response.getString("memberid");
                        // Notify the observer with the generated chat ID
                        chatIdLiveData.setValue(chatId);

                        // Iterate over the list of userEmails and add each user to the chat
                        for (String email : userEmails) {
                            addUserToChat(chatId, email);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> handleError(error, mCreatChatResponse)
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

                        List<ChatRoom> chatRooms = new ArrayList<>();
                        for (int i = 0; i < chatRoomsArray.length(); i++) {
                            JSONObject chatRoomObj = chatRoomsArray.getJSONObject(i);
                            int id = chatRoomObj.getInt("id");
                            String name = chatRoomObj.getString("name");
                            chatRooms.add(new ChatRoom(id, name, "Last Message in " + name));
                        }

                        chatRoomsLiveData.setValue(chatRooms);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    handleError(error, mGetAllChatResponse);
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
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    public MutableLiveData<List<ChatRoom>> getChatRoomsLiveData() {
        if (chatRoomsLiveData == null) {
            chatRoomsLiveData = new MutableLiveData<>();
            getChatRooms();
        }
        return chatRoomsLiveData;
    }

    /**
     * Deletes a chat room.
     * @param chatId The ID of the chat room to delete
     */
    public void deleteChatRoom(String chatId, int position) {

        String url = "http://10.0.2.2:5000/chats/" + chatId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                response -> {

// Manually update the chat list by removing the deleted chat room
                    List<ChatRoom> chatRooms = chatRoomsLiveData.getValue();
                    if (chatRooms != null) {
                        if (position >= 0 && position < chatRooms.size()) {
                            chatRooms.remove(position);
                            // Notify observers of chatRoomsLiveData
                            chatRoomsLiveData.postValue(chatRooms);
                        }
                    }
                },
                error -> {
                    handleError(error, mDeleteChatResponse);
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
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        // Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    public void addUserToChat(String chatId, String email) {
        String url = "http://10.0.2.2:5000/chats/" + chatId + "/" + email;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                null,
                response -> {
                    // Handle the response if needed
                    // For example, you can check if the user was successfully added to the chat
                },
                error -> handleError(error, mAddUsersToChatResponse)
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
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }
    /**
     * Handles request error
     * @param error error.
     * @param responseDestination destination of the response.
     */
    private void handleError(final VolleyError error, MutableLiveData<JSONObject> responseDestination) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                responseDestination.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                JSONObject response = new JSONObject();
                response.put("code", error.networkResponse.statusCode);
                response.put("data", new JSONObject(data));
                responseDestination.setValue(response);
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    /**
     * Sets the JWT (JSON Web Token) for authentication.
     */
    public void setJWT(String jwt) {
        this.mJwt = jwt;
    }
    public LiveData<String> getChatIdLiveData() {
        return chatIdLiveData;
    }
}