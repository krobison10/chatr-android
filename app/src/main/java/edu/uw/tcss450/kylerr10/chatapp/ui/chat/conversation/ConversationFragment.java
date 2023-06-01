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
import java.util.Locale;

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
        Log.d("ConversationFragment_chatid",chatId);

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
            String chatId = response.getString("chatId");
            JSONArray rowsJsonArray = response.getJSONArray("rows");

            for (int i = 0; i < rowsJsonArray.length(); i++) {
                JSONObject rowJson = rowsJsonArray.getJSONObject(i);

                messageId = rowJson.getInt("messageid");
                System.out.println("messageId: " + messageId);

                String email = rowJson.getString("email");
                String messageText = rowJson.getString("message");
                String timestamp = rowJson.getString("timestamp");

                Conversation message = new Conversation();
                message.setConversationId(messageId);
                message.setContent(messageText);
                message.setTimestamp(timestamp);

                System.out.println("Email from JSON: " + email);
                System.out.println("mEmail: " + mEmail);

                // Assign the correct sender and receiver based on the email
                if (email.equals(mEmail)) {
                    System.out.println("Setting sender as 'Sender'");
                    System.out.println("Setting receiver as 'Receiver'");
                    message.setName(mEmail);
                    message.setSender(1, mEmail);
                    message.setReceiver(2, "Receiver");
                    message.setViewType(mAdapter.VIEW_TYPE_SENDER);
                    mAdapter.addMessage(message, mEmail, "Receiver");
                } else {
                    System.out.println("Setting sender as 'Receiver'");
                    System.out.println("Setting receiver as 'Sender'");
                    message.setName(email);
                    message.setSender(2, email);
                    message.setReceiver(1, "Sender");
                    message.setViewType(mAdapter.VIEW_TYPE_RECEIVER);
                    mAdapter.addMessage(message, "Receiver", mEmail);
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

        // Set up the SwipeRefreshLayout
        SwipeRefreshLayout swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setRefreshing(true);

        mConversationViewModel.getFirstMessages(Integer.parseInt(chatId), mConversationSendViewModel.mJwt);

        // Set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.list_of_chat_messages);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Attach a scroll listener to the RecyclerView
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Check if scrolled to the top
                if (!recyclerView.canScrollVertically(-1)) {
                    loadNextMessages();
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

                // Hide the refreshing state of the SwipeRefreshLayout
                swipeContainer.setRefreshing(false);
            }
        });
    }

    /**
     * Loads the next set of messages in the conversation.
     */
    private void loadNextMessages() {
        isLoadingMessages = true;

        List<Conversation> messages = mConversationViewModel.getMessageListByChatId(Integer.parseInt(chatId));
        if (!messages.isEmpty()) {
            int lastMessageId = messages.get(messages.size() - 1).getConversationId();
            mConversationViewModel.getNextMessages(chatId, String.valueOf(lastMessageId), mConversationSendViewModel.mJwt);
        }
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
        // Define an array of input patterns to handle different timestamp formats
        String[] inputPatterns = {
                "yyyy-MM-dd HH:mm:ss.SSSSSS",
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                // Add more patterns as needed for different formats
        };

        SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a");

        for (String inputPattern : inputPatterns) {
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);

            try {
                Date date = inputFormat.parse(timestamp);
                String formattedTimestamp = outputFormat.format(date);
                Log.d("Timestamp", "Original: " + timestamp + ", Formatted: " + formattedTimestamp);
                return formattedTimestamp;
            } catch (ParseException e) {
                // Ignore and try the next pattern if parsing fails
            }
        }

        return "";
    }
}