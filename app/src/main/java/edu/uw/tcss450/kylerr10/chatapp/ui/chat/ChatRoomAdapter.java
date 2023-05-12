package edu.uw.tcss450.kylerr10.chatapp.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.uw.tcss450.kylerr10.chatapp.R;
import java.util.List;

/**
 * RecyclerView Adapter that displays a list of chat rooms.
 * @author Leyla Ahmed
 */
public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {

    //The list of ChatRooms to display in the RecyclerView
    private List<ChatRoom> mChatRooms;

    //The listener to handle click events on the items in the RecyclerView
    private OnChatRoomClickListener mListener;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chat_room_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatRoom chatRoom = mChatRooms.get(position);
        holder.bind(chatRoom, mListener);
    }

    @Override
    public int getItemCount() {
        return mChatRooms.size();
    }

    /**
     * Interface to handle clicks on chat rooms.
     */
    public interface OnChatRoomClickListener {
        void onChatRoomClick(ChatRoom chatRoom);
    }

    /**
     * Constructor for the adapter.
     * @param chatRooms List of chat rooms to display.
     */
    public ChatRoomAdapter(List<ChatRoom> chatRooms) {
        mChatRooms = chatRooms;
    }


    /**
     * Sets the listener for chat room clicks.
     * @param listener The listener to set.
     */
    public void setOnChatRoomClickListener(OnChatRoomClickListener listener) {
        mListener = listener;
    }

    /**
     * ViewHolder class for the adapter.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        //The TextView that displays the name of the chat room in the ConversationActivity layout.

        private TextView mRoomNameTextView;

        //The TextView that displays the last message sent in the chat room in the ConversationActivity layout.

        private TextView mLastMessageTextView;


        /**
         * Constructor for the ViewHolder.
         * @param itemView The View for the ViewHolder.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // TextView used to display the name of the chat room
            mRoomNameTextView = itemView.findViewById(R.id.chat_room_name);

            // TextView used to display the last message sent in the chat room.
            mLastMessageTextView = itemView.findViewById(R.id.chat_last_message);
        }

        /**
         * Binds the chat room data to the ViewHolder and sets the click listener.
         * @param chatRoom The chat room to display.
         * @param listener The listener for chat room clicks.
         */
        public void bind(final ChatRoom chatRoom, final OnChatRoomClickListener listener) {
            mRoomNameTextView.setText(chatRoom.getRoomName());
            mLastMessageTextView.setText(chatRoom.getLastMessage());
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onChatRoomClick(chatRoom);
                }
            });
        }
    }
}
