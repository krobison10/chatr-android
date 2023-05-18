package edu.uw.tcss450.kylerr10.chatapp.ui.chat.conversation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.uw.tcss450.kylerr10.chatapp.R;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * RecyclerView adapter for displaying a conversation between users.
 * @author Leyla Ahmed
 */
public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //Holds the conversation data for the adapter
    private Conversation mConversation;

    // View type for the messages sent by the user
    private static final int VIEW_TYPE_SENDER = 0;

    // View type for the messages received by the user
    private static final int VIEW_TYPE_RECEIVER = 1;

    /**
     * Constructor for the adapter.
     */
    public ConversationAdapter() {
        mConversation = new Conversation();
    }


    /**
     * Set the conversation data in the adapter.
     * @param conversation the conversation to set
     */
    public void setConversation(Conversation conversation) {
        this.mConversation = conversation;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mConversation.getMessages().size();
    }

    @Override
    public int getItemViewType(int position) {
        Conversation message = mConversation.getMessages().get(position);
        if (message.isFromSender(1)) {
            // If the message is sent by the current user, use the sender layout
            return VIEW_TYPE_SENDER;
        } else {
            // If the message is received by the current user, use the receiver layout
            return VIEW_TYPE_RECEIVER;
        }
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
     * View holder for sender layout.
     */
    private static class SenderViewHolder extends RecyclerView.ViewHolder {
        //Displays the message content
        private TextView mMessageTextView;

        //Displays the name of the sender or receiver
        private TextView mNameTextView;

        //Displays the timestamp of the message
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
            mTimeTextView.setText(formatTimestamp(message.getTimestamp()));
        }


    }


    /**
     * View holder for receiver layout.
     */
    private static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        //Displays the message content
        private TextView mMessageTextView;

        //Displays the name of the sender or receiver
        private TextView mNameTextView;

        //Displays the timestamp of the message
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
            mTimeTextView.setText(formatTimestamp(message.getTimestamp()));
        }

    }


    /**
     * This method formats a given timestamp into a string of the format "h:mm a".
     * @param timestamp the timestamp to be formatted
     * @return the formatted timestamp string
     */
    private static String formatTimestamp(long timestamp) {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("h:mm a");
        return mDateFormat.format(new Date(timestamp));
    }

}