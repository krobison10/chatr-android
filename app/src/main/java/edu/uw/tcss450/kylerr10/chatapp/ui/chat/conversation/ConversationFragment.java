package edu.uw.tcss450.kylerr10.chatapp.ui.chat.conversation;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.ChatViewModelHelper;

/**
 * A fragment that displays a conversation between users.
 * @author Leyla Ahmed
 */
public class ConversationFragment extends Fragment {

    // Adapter for displaying messages in the conversation
    private ConversationAdapter mAdapter;

    // EditText for composing messages
    private EditText mMessageEditText;

    // Callback interface for handling conversation events
    private ConversationFragmentCallback mCallback;

    // ViewModel for managing conversation data
    private ConversationViewModel mConversationViewModel;

    // Email of the user
    String email = ChatViewModelHelper.getEmail();

    // Email of the user
    String mEmail = email;

    // The conversation object
    private Conversation mConversation;

    // ID of the message
    private int messageId;

    // ViewModel for sending conversation messages
    private ConversationSendViewModel mConversationSendViewModel;

    // ID of the chat
    private String chatId;

    // Flag indicating if messages are currently being loaded
    private boolean isLoadingMessages = false;

    // Define a callback interface
    public interface ConversationFragmentCallback {
        void onSendMessage(String message);
    }

    public ConversationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout file for the conversation activity
        View rootView = inflater.inflate(R.layout.fragment_conversation, container, false);

        // Retrieve chatId and messageId from the parent activity's intent
        chatId = requireActivity().getIntent().getStringExtra("chatId");


        // Instantiate the ConversationAdapter
        mAdapter = new ConversationAdapter();
        // Get reference to the message input field
        mMessageEditText = rootView.findViewById(R.id.edit_chat_input);

        // Get reference to the send button
        Button sendButton = rootView.findViewById(R.id.button_send_chat);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        return rootView;
    }

    /**
     * Send the typed message.
     */
    private void sendMessage() {
        String messageText = mMessageEditText.getText().toString().trim();

        if (!messageText.isEmpty()) {
            // Notify the parent activity to send the message
            if (mCallback != null) {
                mCallback.onSendMessage(messageText);
            }

            // Clear the message input field
            mMessageEditText.setText("");

            mAdapter.scrollToBottom();

        }
    }
    /**
     * Parses messages from the response JSON object.
     * @param response The response JSON object containing chat messages
     */
    private void parseMessagesFromResponse(JSONObject response) {
        try {
            JSONArray rowsJsonArray = response.getJSONArray("rows");

            for (int i = 0; i < rowsJsonArray.length(); i++) {
                JSONObject rowJson = rowsJsonArray.getJSONObject(i);

                messageId = rowJson.getInt("messageid");

                String email = rowJson.getString("email");
                String messageText = rowJson.getString("message");
                String timestamp = rowJson.getString("timestamp");

                Conversation message = new Conversation();
                message.setConversationId(messageId);
                message.setContent(messageText);
                message.setTimestamp(timestamp);


                // Assign the correct sender and receiver based on the email
                if (email.equals(mEmail)) {
                    System.out.println("Setting sender as 'Sender'");
                    System.out.println("Setting receiver as 'Receiver'");
                    message.setName(mEmail);
                    message.setSender(1, mEmail);
                    message.setReceiver(2, "Receiver");
                    message.setViewType(mAdapter.VIEW_TYPE_SENDER);
                    mAdapter.addMessage(message);
                } else {
                    System.out.println("Setting sender as 'Receiver'");
                    System.out.println("Setting receiver as 'Sender'");
                    message.setName(email);
                    message.setSender(2, email);
                    message.setReceiver(1, "Sender");
                    message.setViewType(mAdapter.VIEW_TYPE_RECEIVER);
                    mAdapter.addMessage(message);
                }
            }
        } catch (JSONException e) {
            // Handle JSON parsing error
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the ConversationSendViewModel
        mConversationSendViewModel = new ViewModelProvider(this).get(ConversationSendViewModel.class);
        mConversationViewModel = new ViewModelProvider(requireActivity()).get(ConversationViewModel.class);

        mConversationViewModel.getFirstMessages(Integer.parseInt(chatId), mConversationSendViewModel.mJwt);

        // Set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.list_of_chat_messages);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

// Attach a scroll listener to the RecyclerView
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private boolean userScrolledUp = false;
            private int lastLoadedMessageId = 0; // Add a variable to track the last loaded message ID

            private int initialMessagesCount = 15;// Set the initial number of messages to load

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Check if scrolled to the top
                if (!recyclerView.canScrollVertically(-1)) {
                    // User manually scrolled up
                    userScrolledUp = true;
                    // Set up the SwipeRefreshLayout
                    SwipeRefreshLayout swipeContainer = view.findViewById(R.id.swipeContainer);
                    swipeContainer.setRefreshing(true);

                    List<Conversation> messages = mConversationViewModel.getMessageListByChatId(Integer.parseInt(chatId));

                    if (messages.size() > 0) {
                        if (messages.size() <= initialMessagesCount) {
                            lastLoadedMessageId = messages.get(0).getConversationId();
                        } else {
                            lastLoadedMessageId = messages.get(0).getConversationId() - initialMessagesCount;
                        }
                    }

                    mConversationViewModel.getNextMessages(chatId, String.valueOf(lastLoadedMessageId),
                            mConversationSendViewModel.mJwt, new ConversationViewModel.pastConversationCallback() {
                        @Override
                        public void onPastMessageReceived(JSONObject response) {
                            parsePastMessagesFromResponse(response);

                            // Update the lastLoadedMessageId with the ID of the first message in the response minus 1
                            try {
                                JSONArray messagesArray = response.getJSONArray("rows");
                                if (messagesArray.length() > 0) {
                                    lastLoadedMessageId = (messagesArray.getJSONObject(0).getInt("messageid") - 15);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // Check if all past messages are displayed
                            if (messages.size() == mAdapter.getItemCount()) {
                                // Disable refreshing state of SwipeRefreshLayout
                                swipeContainer.setRefreshing(false);
                            }

                            // Reset the flag after loading new messages
                            userScrolledUp = false;
                        }
                    });
                    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            // Only load new messages if user hasn't manually scrolled up
                            if (!userScrolledUp && messages.size() > initialMessagesCount) {
                                mConversationViewModel.getNextMessages(chatId, String.valueOf(lastLoadedMessageId),
                                        mConversationSendViewModel.mJwt, new ConversationViewModel.pastConversationCallback() {
                                            @Override
                                            public void onPastMessageReceived(JSONObject response) {
                                                parsePastMessagesFromResponse(response);

                                                // Update the lastLoadedMessageId with the ID of the first message in the response minus 1
                                                try {
                                                    JSONArray messagesArray = response.getJSONArray("rows");
                                                    if (messagesArray.length() > 0) {
                                                        lastLoadedMessageId = (messagesArray.getJSONObject(0).getInt("messageid") - 15);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                // Check if all past messages are displayed
                                                if (messages.size() == mAdapter.getItemCount()) {
                                                    // Disable refreshing state of SwipeRefreshLayout
                                                    swipeContainer.setRefreshing(false);
                                                }

                                                // Reset the flag after loading new messages
                                                userScrolledUp = false;
                                                mAdapter.scrollToTop();
                                            }
                                        });
                            } else {
                                // Reset the refreshing state of SwipeRefreshLayout
                                swipeContainer.setRefreshing(false);
                            }
                        }
                    });
                }
            }
        });

        /**
         * Calls the getMessage method with the appropriate
         * parameters to retrieve messages for the specified chat.
         * @param chatId The ID of the chat to retrieve the message for
         */
        mConversationSendViewModel.getMessage(chatId, new ConversationSendViewModel.ConversationCallback() {
            @Override
            public void onMessageReceived(JSONObject response) {
                parseMessagesFromResponse(response);
            }
        });


        /**
         * Observes the message list and updates the adapter when it changes.
         * @param chatId The ID of the chat to observe
         * @param owner The LifecycleOwner for observing the message list
         * @param observer The observer to be notified when the message list changes
         */
        mConversationViewModel.addMessageObserver(Integer.parseInt(chatId), getViewLifecycleOwner(), new Observer<List<Conversation>>() {
            @Override
            public void onChanged(List<Conversation> messages) {
                mAdapter.setMessages(messages);
                mAdapter.scrollToBottom();
            }
        });
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Ensure that the parent activity implements the callback interface
        if (context instanceof ConversationFragmentCallback) {
            mCallback = (ConversationFragmentCallback) context;
        } else {
            throw new IllegalStateException("Parent activity must implement ConversationFragmentCallback");
        }
    }

    /**
     * Formats the timestamp string to display only the hour and minute.
     * @param timestamp The timestamp string to format
     * @return The formatted timestamp in "h:mm a" format
     */
    String formatTimestamp(String timestamp) {
        String[] inputPatterns = {
                "yyyy-MM-dd HH:mm:ss.SSSSSS",
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        };

        SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a");
        outputFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));

        if (timestamp.length() == 26) {
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPatterns[0]);

            try {
                Date date = inputFormat.parse(timestamp);
                String formattedTimestamp = outputFormat.format(date);
                Log.d("Timestamp", "Original: " + timestamp + ", Formatted: " + formattedTimestamp);
                return formattedTimestamp;
            } catch (ParseException e) {
                // Handle the exception
            }
        } else {
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPatterns[1]);
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            try {
                Date date = inputFormat.parse(timestamp);
                String formattedTimestamp = outputFormat.format(date);
                Log.d("Timestamp", "Original: " + timestamp + ", Formatted: " + formattedTimestamp);
                return formattedTimestamp;
            } catch (ParseException e) {
                // Handle the exception
            }
        }

        return "";
    }

    /**
     * Parses messages from the response JSON object.
     * @param response The response JSON object containing chat messages
     */
    private void parsePastMessagesFromResponse(JSONObject response) {
        try {
            JSONArray messagesArray = response.getJSONArray("rows");

            for (int i = messagesArray.length() - 1; i >= 0; i--) {
                JSONObject messageObject = messagesArray.getJSONObject(i);

                // Parse message details from the JSON object
                int messageId = messageObject.getInt("messageid");
                String email = messageObject.getString("email");
                String messageText = messageObject.getString("message");
                String timestamp = messageObject.getString("timestamp");

                Conversation message = new Conversation();
                message.setConversationId(messageId);
                message.setName(email);
                message.setContent(messageText);
                message.setTimestamp(timestamp);

                // Determine the appropriate view type and sender/receiver based on the email
                if (email.equals(mEmail)) {
                    message.setSender(1, mEmail);
                    message.setReceiver(2, "Receiver");
                    message.setViewType(mAdapter.VIEW_TYPE_SENDER);
                } else {
                    message.setSender(2, email);
                    message.setReceiver(1, "Sender");
                    message.setViewType(mAdapter.VIEW_TYPE_RECEIVER);
                }

                mAdapter.addMessage(message); // Add the message to the adapter's message list
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}