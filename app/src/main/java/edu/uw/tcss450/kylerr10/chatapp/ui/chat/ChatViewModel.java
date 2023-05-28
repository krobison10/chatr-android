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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import edu.uw.tcss450.kylerr10.chatapp.io.RequestQueueSingleton;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_members.ChatMember;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_room.ChatRoom;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_room.ChatRoomAdapter;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_room.ChatRoomMemberAdapter;

/**
 * ViewModel class for managing chat-related data and operations.
 * @author Leyla Ahmed
 */
public class ChatViewModel extends AndroidViewModel {
    //List of chat rooms used by the ChatRoomAdapter to display the list of chat rooms in a RecyclerView
    private List<ChatRoom> mChatRooms = new ArrayList<>();

    //The ChatRoomAdapter instance used to populate the RecyclerView with chat room data
    private ChatRoomAdapter mAdapter = new ChatRoomAdapter();

    //The ChatRoomAdapter instance used to populate the RecyclerView with chat room data
    private ChatRoomMemberAdapter mMAdapter = new ChatRoomMemberAdapter();

    private final MutableLiveData<JSONObject> mCreatChatResponse;
    private final MutableLiveData<JSONObject> mDeleteChatResponse;
    private final MutableLiveData<JSONObject> mGetAllChatResponse;
    private final MutableLiveData<JSONObject> mAddMembersToChatResponse;
    private final MutableLiveData<JSONObject> mAddUserToChatResponse;
    private final MutableLiveData<JSONObject> mRemoveUserFromChatResponse;
    private final MutableLiveData<JSONObject> mRemoveMemberFromChat;
    private final MutableLiveData<JSONObject> mGetChatMembers;
    MutableLiveData<List<ChatRoom>> chatRoomsLiveData;
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

        mAddMembersToChatResponse = new MutableLiveData<>();
        mAddMembersToChatResponse.setValue(new JSONObject());

        mGetChatMembers = new MutableLiveData<>();
        mGetChatMembers.setValue(new JSONObject());

        mAddUserToChatResponse = new MutableLiveData<>();
        mAddUserToChatResponse.setValue(new JSONObject());

        mRemoveUserFromChatResponse = new MutableLiveData<>();
        mRemoveUserFromChatResponse.setValue(new JSONObject());

        mRemoveMemberFromChat = new MutableLiveData<>();
        mRemoveMemberFromChat.setValue(new JSONObject());

    }


    /**
     * Creates a new chat room.
     * @param chatName The name of the chat room to create
     */
    public void createChatRoom(String chatName, List<String> emails) {
        Log.d("CreateChatRoom", "Creating chat room: " + chatName);

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
                        String chatId = response.getString("chatID");
                        addUserToChat(chatId);

                        for (String email : emails) {
                            addMembersToChat(chatId, email);
                            Log.d("CreateChatRoom", "Added user " + email + " to chat ID: " + chatId);
                        }

                        getChatRoomsLiveData();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    handleError(error, mCreatChatResponse);
                    error.printStackTrace();
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

        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
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

                        // Update the chatRoomsLiveData with the new chat rooms list
                        chatRoomsLiveData.setValue(chatRooms);

                        Log.d("ChatViewModel", "Chat rooms retrieved successfully");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    handleError(error, mGetAllChatResponse);
                    Log.e("ChatViewModel", "Error retrieving chat rooms: " + error.getMessage());
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

        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
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
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    /**
     * Adds the current user to a chat room identified by the given chat ID.
     * @param chatId The ID of the chat room.
     */
    public void addUserToChat(String chatId) {
        String url = "http://10.0.2.2:5000/chats/" + chatId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                null,
                response -> {
                    // User added successfully
                    Log.d("AddUserToChat", "User added to chat");
                },
                error -> {
                    // Handle error
                    handleError(error, mAddUserToChatResponse);
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

        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    /**
     * Adds members to a chat room identified by the given chat ID.
     * @param chatId The ID of the chat room
     * @param email  The email of the member to add
     */
    public void addMembersToChat(String chatId, String email) {
        String url = "http://10.0.2.2:5000/chats/" + chatId + "/" + email;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                null,
                response -> {
                    Log.d("AddMembersToChat", "Members added to chat");
                },
                error -> handleError(error, mAddMembersToChatResponse)
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

        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    /**
     * Retrieves the members of a chat room identified by the given chat ID.
     * @param chatId   The ID of the chat room
     * @param callback The callback interface to handle the retrieval result
     */
    public void getChatRoomMembers(String chatId, ChatRoomMembersCallback callback) {

        String url = "http://10.0.2.2:5000/chats/" + chatId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        int rowCount = response.getInt("rowCount");
                        JSONArray rows = response.getJSONArray("rows");

                        List<String> memberEmails = new ArrayList<>();
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject row = rows.getJSONObject(i);
                            String email = row.getString("email");
                            memberEmails.add(email);
                        }

                        // Create ChatMember objects from memberEmails
                        List<ChatMember> members = new ArrayList<>();
                        for (String email : memberEmails) {
                            ChatMember member = new ChatMember(email);
                            members.add(member);
                        }

                        // Invoke the onSuccess callback with the member list
                        callback.onSuccess(members);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    error.printStackTrace();

                    Log.e("GetChatRoomMembers4", "Error getting chat room members: " + error.getMessage());

                    // Invoke the onError callback
                    callback.onError();
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

        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    /**
     * Deletes a member from a chat room identified by the given chat ID and email.
     * @param chatId The ID of the chat room
     * @param email  The email of the member to delete
     */
    public void deleteChatMember(String chatId, String email) {
        String url = "http://10.0.2.2:5000/chats/" + chatId + "/" + email;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                response -> {
                    Log.d("DeleteChatMember", "Chat member deleted: " + email);
                    Log.d("DeleteChatMember", "From ChatId: " + chatId);
                },
                error -> {
                    handleError(error, mRemoveMemberFromChat);
                    Log.e("ChatViewModel", "Error retrieving chat rooms: " + error.getMessage());
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

        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
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
     * Sets the JWT for authentication.
     */
    public void setJWT(String jwt) {
        this.mJwt = jwt;
    }

    /**
     * Gets the JWT for authentication.
     */
    public String getJWT() {
        return mJwt;
    }

    /**
     * Returns a LiveData object that represents the list of chat rooms.
     * The chat rooms are updated in the LiveData.
     * @return The LiveData object containing the list of chat rooms
     */
    public MutableLiveData<List<ChatRoom>> getChatRoomsLiveData() {
        chatRoomsLiveData = new MutableLiveData<>();
        Log.d("ChatViewModel", "Getting chat rooms...");
        getChatRooms();
        return chatRoomsLiveData;
    }

    /**
     * Callback interface for retrieving the members of a chat room.
     */
    public interface ChatRoomMembersCallback {
        void onSuccess(List<ChatMember> members);
        void onError();
    }

}