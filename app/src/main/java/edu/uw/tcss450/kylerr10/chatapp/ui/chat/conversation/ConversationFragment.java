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
    private EditText mMessageEditText;
    private ConversationFragmentCallback mCallback;

    private ConversationViewModel mConversationViewModel;
    String email = ChatViewModelHelper.getEmail();

    String mEmail = email;

    // The conversation object
    private Conversation mConversation;
    private int messageId;
    private ConversationSendViewModel mConversationSendViewModel;
    private String chatId;
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
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.getDefault()).format(new Date());
            Conversation newMessage = new Conversation(messageId, messageText, mEmail, timestamp);
            mConversationViewModel.addMessage(Integer.parseInt(chatId), newMessage);
        }
    }

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


        // Call getMessage method with appropriate parameters
        mConversationSendViewModel.getMessage(chatId, new ConversationSendViewModel.ConversationCallback() {
            @Override
            public void onMessageReceived(JSONObject response) {
                parseMessagesFromResponse(response);
            }
        });

        // Observe the message list and update the adapter when it changes
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
    private void displayMessages(List<Conversation> messages) {
        // Update the adapter with the new messages
        mAdapter.setMessages(messages);

        // Scroll to the bottom of the RecyclerView
        mAdapter.scrollToBottom();

        // Notify the adapter that the data has changed
        mAdapter.notifyDataSetChanged();
    }

    // Format the timestamp string to display only the hour and minute
    String formatTimestamp(String timestamp) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a");

        try {
            Date date = inputFormat.parse(timestamp);
            String formattedTimestamp = outputFormat.format(date);
            Log.d("Timestamp", "Original: " + timestamp + ", Formatted: " + formattedTimestamp);
            return formattedTimestamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }
}