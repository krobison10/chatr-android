package edu.uw.tcss450.kylerr10.chatapp.ui.chat.conversation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.uw.tcss450.kylerr10.chatapp.R;

import java.util.ArrayList;
import java.util.List;


/**
 * RecyclerView adapter for displaying a conversation between users.
 * @author Leyla Ahmed
 */
public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Holds the conversation data for the adapter
    private Conversation mConversation;

    // Reference to the RecyclerView
    private RecyclerView mRecyclerView;

    // View type for the messages sent by the user
    public static final int VIEW_TYPE_SENDER = 0;

    // View type for the messages received by the user
    public static final int VIEW_TYPE_RECEIVER = 1;

    // Create an instance of the ConversationFragment
    static ConversationFragment fragment = new ConversationFragment();

    /**
     * Constructor for the adapter.
     */
    public ConversationAdapter() {
        mConversation = new Conversation();
        mConversation.setMessages(new ArrayList<>());
    }
    /**
     * Adds a new message to the conversation.
     * @param message The message to add
     */
    public void addMessage(Conversation message) {
        // Add the new message at the beginning of the list
        mConversation.getMessages().add(0, message);

        // Notify the adapter that a new item has been inserted at position 0
        notifyItemInserted(0);
    }


    /**
     * Scroll to the bottom of the message list.
     */
    public void scrollToBottom() {
        if (mRecyclerView != null && mConversation.getMessages() != null && mConversation.getMessages().size() > 0) {
            mRecyclerView.smoothScrollToPosition(mConversation.getMessages().size() - 1);
        }
    }

    /**
     * Scroll to the top of the message list.
     */
    public void scrollToTop() {
        if (mRecyclerView != null && mConversation.getMessages() != null && mConversation.getMessages().size() > 0) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }
    @Override
    public int getItemCount() {
        if (mConversation != null && mConversation.getMessages() != null) {
            return mConversation.getMessages().size();
        }
        return 0;
    }
    @Override
    public int getItemViewType(int position) {
        if (mConversation != null && mConversation.getMessages() != null && position >= 0 && position < mConversation.getMessages().size()) {
            Conversation message = mConversation.getMessages().get(position);
            return message.getViewType();
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_SENDER) {
            // Inflate the sender layout
            View view = inflater.inflate(R.layout.fragment_send_chat_card, parent, false);
            return new SenderViewHolder(view);
        } else {
            // Inflate the receiver layout
            View view = inflater.inflate(R.layout.fragment_receive_chat_card, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Conversation message = mConversation.getMessages().get(position);
        if (holder instanceof SenderViewHolder) {
            // If the holder is a sender view holder, bind the data using the sender view holder
            ((SenderViewHolder) holder).bind(message);
        } else if (holder instanceof ReceiverViewHolder) {
            // If the holder is a receiver view holder, bind the data using the receiver view holder
            ((ReceiverViewHolder) holder).bind(message);
        }
    }

    /**
     * Set the list of messages in the conversation and update the adapter.
     * @param messages The list of Conversation objects representing the messages
     */
    public void setMessages(List<Conversation> messages) {
        mConversation.setMessages(messages);
        notifyDataSetChanged();
    }

    /**
     * View holder for sender layout.
     */
    private static class SenderViewHolder extends RecyclerView.ViewHolder {
        // Displays the message content
        private TextView mMessageTextView;

        // Displays the name of the sender or receiver
        private TextView mNameTextView;

        // Displays the timestamp of the message
        private TextView mTimeTextView;

        /**
         * Constructor for the ViewHolder.
         * @param itemView the item view to be held by the ViewHolder.
         */
        SenderViewHolder(View itemView) {
            super(itemView);
            mMessageTextView = itemView.findViewById(R.id.sender_chat_text);
            mNameTextView = itemView.findViewById(R.id.sender_name);
            mTimeTextView = itemView.findViewById(R.id.sender_chat_time);
        }

        /**
         * Bind data to the ViewHolder.
         * @param message the message data to bind.
         */
        void bind(Conversation message) {
            mMessageTextView.setText(message.getContent());
            mNameTextView.setText(message.getSenderName());
            mTimeTextView.setText(fragment.formatTimestamp(message.getTimestamp()));

            // Log the bound message
            Log.d("ConversationAdapter", "Bound sender message: " + message.getContent());
        }
    }

    /**
     * View holder for receiver layout.
     */
    private static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        // Displays the message content
        private TextView mMessageTextView;

        // Displays the name of the sender or receiver
        private TextView mNameTextView;

        // Displays the timestamp of the message
        private TextView mTimeTextView;

        /**
         * Constructor for the ViewHolder.
         * @param itemView the item view to be held by the ViewHolder
         */
        ReceiverViewHolder(View itemView) {
            super(itemView);
            mTimeTextView = itemView.findViewById(R.id.receiver_chat_time);
            mMessageTextView = itemView.findViewById(R.id.receiver_chat_text);
            mNameTextView = itemView.findViewById(R.id.receiver_name);
        }

        /**
         * Bind data to the ViewHolder.
         * @param message the message data to bind
         */
        void bind(Conversation message) {
            mMessageTextView.setText(message.getContent());
            mNameTextView.setText(message.getSenderName());
            mTimeTextView.setText(fragment.formatTimestamp(message.getTimestamp()));

            // Log the bound message
            Log.d("ConversationAdapter", "Bound receiver message: " + message.getContent());
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecyclerView = null;
    }

    /**
     * Adds a new message to the conversation.
     * @param message The message to add
     * @param position The position of message
     */
    public void addMessageAtTop(Conversation message, int position) {
        mConversation.getMessages().add(position, message);
        notifyItemInserted(position);
    }
}