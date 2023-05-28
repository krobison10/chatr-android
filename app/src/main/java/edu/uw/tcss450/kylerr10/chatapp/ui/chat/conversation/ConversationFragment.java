package edu.uw.tcss450.kylerr10.chatapp.ui.chat.conversation;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import edu.uw.tcss450.kylerr10.chatapp.ConversationActivity;
import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.ChatViewModel;

/**
 * A fragment that displays a conversation between users.
 * @author Leyla Ahmed
 */
public class ConversationFragment extends Fragment {

    // Adapter for displaying messages in the conversation
    private ConversationAdapter mAdapter;
    private EditText mMessageEditText;

    // The conversation object
    private Conversation mConversation;

    private ConversationSendViewModel mConversationSendViewModel;

    public ConversationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout file for the conversation activity
        View rootView = inflater.inflate(R.layout.fragment_conversation, container, false);

        // Instantiate the ConversationAdapter
        mAdapter = new ConversationAdapter();

        // Set the adapter for the RecyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.list_of_chat_messages);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
            if (mConversation == null) {
                // Create a new conversation object
                mConversation = new Conversation(1, "Sender", 2, "Receiver");
            }

            // Create a new message
            int conversationId = Conversation.generateConversationId();

            // Log the message before adding it to the adapter
            Log.d("ConversationFragment", "New Message: " + messageText);

            // Add the new message to the conversation
            mConversation.addMessage(conversationId, 1, "Sender", 2, "Receiver", messageText, System.currentTimeMillis());

            // Clear the message input field
            mMessageEditText.setText("");

            mAdapter.scrollToBottom();

            // Update the adapter with the updated conversation
            mAdapter.setConversation(mConversation);

        }
    }
}