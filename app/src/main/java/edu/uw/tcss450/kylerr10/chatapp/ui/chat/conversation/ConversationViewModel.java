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
import org.json.JSONObject;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.kylerr10.chatapp.BuildConfig;
import edu.uw.tcss450.kylerr10.chatapp.io.RequestQueueSingleton;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.ChatViewModelHelper;

/**
 * ViewModel class that manages the list of conversations in a chat.
 * @author Leyla Ahmed
 */
public class ConversationViewModel extends AndroidViewModel {

    // MutableLiveData to hold the list of conversations
    private MutableLiveData<List<Conversation>> mMessagesLiveData;

    /**
     * A Map of Lists of Chat Messages.
     * The Key represents the Chat ID
     * The value represents the List of (known) messages for that that room.
     */
    private Map<Integer, MutableLiveData<List<Conversation>>> mMessages;

    /**
     * Constructor for ConversationViewModel.
     * Initializes the MutableLiveData and the Map.
     * @param application The application context
     */
    public ConversationViewModel(@NonNull Application application) {
        super(application);
        mMessages = new HashMap<>();
        mMessagesLiveData = new MutableLiveData<>();
    }

    /**
     * Register as an observer to listen to a specific chat room's list of messages.
     * @param chatId the chatid of the chat to observer
     * @param owner the fragments lifecycle owner
     * @param observer the observer
     */
    public void addMessageObserver(int chatId,
                                   @NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super List<Conversation>> observer) {
        getOrCreateMapEntry(chatId).observe(owner, observer);
    }

    /**
     * Return a reference to the List<> associated with the chat room. If the View Model does
     * not have a mapping for this chatID, it will be created.
     *
     * WARNING: While this method returns a reference to a mutable list, it should not be
     * mutated externally in client code. Use public methods available in this class as
     * needed.
     *
     * @param chatId the id of the chat room List to retrieve
     * @return a reference to the list of messages
     */
    public List<Conversation> getMessageListByChatId(final int chatId) {
        return getOrCreateMapEntry(chatId).getValue();
    }

    /**
     * Gets or creates a MutableLiveData entry for the specified chat ID.
     * @param chatId The ID of the chat
     * @return The MutableLiveData for the chat ID
     */
    private MutableLiveData<List<Conversation>> getOrCreateMapEntry(final int chatId) {
        if(!mMessages.containsKey(chatId)) {
            mMessages.put(chatId, new MutableLiveData<>(new ArrayList<>()));
        }
        return mMessages.get(chatId);
    }

    /**
     * Makes a request to the web service to get the first batch of messages for a given Chat Room.
     * Parses the response and adds the ChatMessage object to the List associated with the
     * ChatRoom. Informs observers of the update.
     *
     * Subsequent requests to the web service for a given chat room should be made from
     * getNextMessages()
     *
     * @param chatId the chatroom id to request messages of
     * @param jwt the users signed JWT
     */
    public void getFirstMessages(final int chatId, final String jwt) {
        String url = BuildConfig.BASE_URL + "/messages/" + chatId;

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handelSuccess,
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

        //code here will run
    }

    /**
     * Makes a request to the web service to get the next batch of messages for a given Chat Room.
     * This request uses the earliest known ChatMessage in the associated list and passes that
     * messageId to the web service.
     * Parses the response and adds the ChatMessage object to the List associated with the
     * ChatRoom. Informs observers of the update.
     * <p>
     * Subsequent calls to this method receive earlier and earlier messages.
     *
     * @param chatId        the chatroom id to request messages of
     * @param jwt           the users signed JWT
     */
    public void getNextMessages(String chatId,String lastMessageId, String jwt, pastConversationCallback callback) {
        String url = BuildConfig.BASE_URL + "/messages/" + chatId + "/"
                + lastMessageId;
        Log.d("GETNEXTChat", "URL: " + url);

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                response -> {
                    Log.d("GETNEXTChat", "Response: " + response.toString());
                    callback.onPastMessageReceived(response);
                    handelSuccess(response);
                },
                error -> {
                    Log.e("GETNEXTChat", "Volley Error: " + error.toString());
                    callback.onPastMessageReceived(null);
                    handleError(error);
                }) {

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

        //code here will run
    }

    /**
     * When a chat message is received externally to this ViewModel, add it
     * with this method.
     * @param chatId
     * @param message
     */
    public void addMessage(final int chatId, final Conversation message) {
        List<Conversation> list = getMessageListByChatId(chatId);
        int insertionIndex = findInsertionIndex(list, message);
        list.add(insertionIndex, message);
        getOrCreateMapEntry(chatId).setValue(list);
    }

    private void handelSuccess(final JSONObject response) {

    }

    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            Log.e("NETWORK ERROR", error.getMessage());
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    error.networkResponse.statusCode +
                            " " +
                            data);
        }
    }

    /**
     * Finds the correct insertion index for the new message based on its sender and receiver views.
     * @param messageList The list of messages
     * @param newMessage The new message to be inserted
     * @return The insertion index
     */
    private int findInsertionIndex(List<Conversation> messageList, Conversation newMessage) {
        int index = 0;
        boolean foundSender = false;

        for (Conversation message : messageList) {
            if (message.getName().equals(ChatViewModelHelper.getEmail())) {
                // The existing message is sent by the user
                if (newMessage.getName().equals(ChatViewModelHelper.getEmail())) {
                    // The new message is also sent by the user, set view type as sender
                    newMessage.setViewType(ConversationAdapter.VIEW_TYPE_SENDER);
                } else {
                    // The new message is received by the user, set view type as receiver
                    newMessage.setViewType(ConversationAdapter.VIEW_TYPE_RECEIVER);
                }
                foundSender = true;
            } else {
                // The existing message is received by the user, set view type as receiver
                message.setViewType(ConversationAdapter.VIEW_TYPE_RECEIVER);

                if (!foundSender && newMessage.getName().equals(ChatViewModelHelper.getEmail())) {
                    // The new message is sent by the user and no sender message is found yet, set view type as sender
                    newMessage.setViewType(ConversationAdapter.VIEW_TYPE_SENDER);
                    foundSender = true;
                }
            }

            index++;
        }

        return index;
    }
    /**
     * Callback interface for receiving the API response.
     */
    public interface pastConversationCallback {
        void onPastMessageReceived(JSONObject response);
    }
}
