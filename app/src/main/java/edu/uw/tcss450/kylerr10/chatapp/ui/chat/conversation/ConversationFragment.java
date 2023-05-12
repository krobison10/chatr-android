package edu.uw.tcss450.kylerr10.chatapp.ui.chat.conversation;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.uw.tcss450.kylerr10.chatapp.R;

/**
 * A fragment that displays a conversation between users.
 * @author Leyla Ahmed
 */
public class ConversationFragment extends Fragment {

    // Adapter for displaying messages in the conversation
    private ConversationAdapter mAdapter;


    public ConversationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
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

        // Add mock data to the conversation
        addMockData();

        return rootView;
    }

    /**
     * Add mock data to the conversation.
     */
    private void addMockData() {
        // Create a conversation between sender with ID 1 and receiver with ID 2
        Conversation conversation = new Conversation(1, "Sender", 2, "Receiver");

        // Create 10 mock messages alternating between sender and receiver
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                // Add a message from the sender
                conversation.addMessage(1, "Sender", 2, "Receiver", "Sender message #" + (i + 1), System.currentTimeMillis());
            } else {
                // Add a message from the receiver
                conversation.addMessage(2, "Receiver", 1, "Sender", "Receiver message #" + (i + 1), System.currentTimeMillis());
            }
        }

        // Set the conversation in the adapter
        mAdapter.setConversation(conversation);
    }

}