package edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_members.ChatMember;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class for displaying chat rooms in a RecyclerView.
 * As well as the members in each chat room.
 * @author Leyla Ahmed
 */
public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {
    //List of chat rooms used by the ChatRoomAdapter
    private List<ChatRoom> mChatRooms = new ArrayList<>();

    //Listener interface to handle click events on chat rooms
    private OnChatRoomClickListener mListener;

    //Flag indicating whether the member RecyclerView is visible or not
    private static boolean mIsMemberRecyclerViewVisible = false;

    public ChatRoomAdapter() {
        // Required empty public constructor
    }

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
     * Constructs a ChatRoomAdapter with the provided list of ChatRooms.
     * @param chatRooms The list of ChatRooms to display
     */
    public ChatRoomAdapter(List<ChatRoom> chatRooms) {
        mChatRooms = chatRooms != null ? chatRooms : new ArrayList<>();
    }


    /**
     * Sets the listener for chat room clicks.
     * @param listener The listener to set
     */
    public void setOnChatRoomClickListener(OnChatRoomClickListener listener) {
        mListener = listener;
    }

    /**
     * ViewHolder class for the adapter.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mRoomNameTextView;
        private TextView mLastMessageTextView;
        private ImageButton buttonMore;
        private RecyclerView recyclerViewMembers;


        /**
         * Constructs a ViewHolder for a chat room item view.
         * @param itemView The item view for the chat room
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRoomNameTextView = itemView.findViewById(R.id.chat_room_name);
            mLastMessageTextView = itemView.findViewById(R.id.chat_last_message);
            buttonMore = itemView.findViewById(R.id.button_more);
            recyclerViewMembers = itemView.findViewById(R.id.recyclerView_members);

            buttonMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleMemberRecyclerView();
                }

                /**
                 * Toggles the visibility of the member RecyclerView and updates the UI accordingly.
                 */
                private void toggleMemberRecyclerView() {
                    TextView memberTextView = itemView.findViewById(R.id.chat_room_members);

                    if (recyclerViewMembers.getVisibility() == View.VISIBLE) {
                        recyclerViewMembers.setVisibility(View.GONE);
                        memberTextView.setVisibility(View.GONE);
                        buttonMore.setImageResource(R.drawable.ic_chat_more_onsurface_24dp);
                        mIsMemberRecyclerViewVisible = false;
                    } else {
                        recyclerViewMembers.setVisibility(View.VISIBLE);
                        memberTextView.setVisibility(View.VISIBLE);
                        buttonMore.setImageResource(R.drawable.ic_chat_less_onsurface_24dp);
                        mIsMemberRecyclerViewVisible = true;
                    }
                }
            });
        }

        /**
         * Binds the chat room data to the ViewHolder and sets the click listener.
         * @param chatRoom The chat room to display
         * @param listener The listener for chat room clicks
         */
        public void bind(final ChatRoom chatRoom, final OnChatRoomClickListener listener) {
            mRoomNameTextView.setText(chatRoom.getRoomName());
            mLastMessageTextView.setText(chatRoom.getLastMessage());

            List<ChatMember> selectedMembers = chatRoom.getSelectedMembers();

            if (selectedMembers != null && !selectedMembers.isEmpty()) {
                ChatRoomMemberAdapter mAdapter = new ChatRoomMemberAdapter(selectedMembers);
                recyclerViewMembers.setAdapter(mAdapter);
                recyclerViewMembers.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            } else {
                recyclerViewMembers.setVisibility(View.GONE); // Hide the RecyclerView if there are no selected members
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onChatRoomClick(chatRoom);
                    }
                }
            });
        }
    }
    /**
     * Updates the members of a chat room identified by the specified chatRoomId.
     * @param chatRoomId The ID of the chat room to update
     * @param members  The list of ChatMember objects representing the new members of the chat room
     */
    public void updateChatRoomMembers(int chatRoomId, List<ChatMember> members) {
        for (ChatRoom chatRoom : mChatRooms) {
            if (chatRoom.getChatId() == chatRoomId) {
                chatRoom.setSelectedMembers(members);
                break;
            }
        }
        notifyDataSetChanged();
    }
    /**
     * Updates the list of chat rooms with the provided chatRooms.
     * @param chatRooms The list of ChatRoom objects representing the updated chat rooms
     */
    public void updateChatRooms(List<ChatRoom> chatRooms) {
        mChatRooms.clear();
        mChatRooms.addAll(chatRooms);
        notifyDataSetChanged();
    }

    /**
     * Retrieves the ID of the ChatRoom at the specified position.
     * @param position The position of the ChatRoom
     * @return The ID of the ChatRoom at the specified position
     * or null if the position is out of range
     */
    public String getChatRoomId(int position) {
        if (position >= 0 && position < mChatRooms.size()) {
            ChatRoom chatRoom = mChatRooms.get(position);
            return String.valueOf(chatRoom.getChatId());
        }
        return null;
    }

}
