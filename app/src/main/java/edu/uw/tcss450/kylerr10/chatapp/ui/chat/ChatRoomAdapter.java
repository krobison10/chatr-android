package edu.uw.tcss450.kylerr10.chatapp.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_members.ChatMember;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_members.ChatMemberAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView Adapter that displays a list of chat rooms.
 * @author Leyla Ahmed
 */
public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {

    // The list of ChatRooms to display in the RecyclerView
    private List<ChatRoom> mChatRooms;

    // The list of selected members
    private static List<ChatMember> mSelectedMembers = new ArrayList<>();

    // The listener to handle click events on the items in the RecyclerView
    private OnChatRoomClickListener mListener;

    // Indicates whether the member list view is visible
    private static boolean mIsMemberListViewVisible = false;

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
     * Constructs a ChatRoomAdapter with the provided list of ChatRooms and selected members.
     * @param chatRooms        The list of ChatRooms to display
     * @param selectedMembers  The list of selected members
     */
    public ChatRoomAdapter(List<ChatRoom> chatRooms, List<ChatMember> selectedMembers) {
        mChatRooms = chatRooms;
        mSelectedMembers = selectedMembers;
    }


    /**
     * Constructs a ChatRoomAdapter with the provided list of ChatRooms.
     * @param chatRooms  The list of ChatRooms to display
     */
    public ChatRoomAdapter(List<ChatRoom> chatRooms) {
        mChatRooms = chatRooms;

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

        //The TextView that displays the name of the chat room in the ConversationActivity layout

        private TextView mRoomNameTextView;

        //The TextView that displays the last message sent in the chat room in the ConversationActivity layout

        private TextView mLastMessageTextView;


        /**
         * Constructor for the ViewHolder.
         *
         * @param itemView The View for the ViewHolder
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // TextView used to display the name of the chat room
            mRoomNameTextView = itemView.findViewById(R.id.chat_room_name);

            // TextView used to display the last message sent in the chat room
            mLastMessageTextView = itemView.findViewById(R.id.chat_last_message);

            ImageButton buttonMore = itemView.findViewById(R.id.button_more);

            buttonMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleMemberRecyclerView();
                }

                /**
                 * Toggles the visibility of the member list RecyclerView and updates the UI accordingly.
                 */
                private void toggleMemberRecyclerView() {

                    // Retrieve the necessary UI components
                    ListView listViewMembers = itemView.findViewById(R.id.listView_members);
                    TextView memberTextView = itemView.findViewById(R.id.chat_room_members);

                    // Toggle the visibility of the member list and update the UI components
                    if (listViewMembers.getVisibility() == View.VISIBLE) {
                        listViewMembers.setVisibility(View.GONE);
                        memberTextView.setVisibility(View.GONE);
                        buttonMore.setImageResource(R.drawable.ic_chat_less_black_24dp);
                        mIsMemberListViewVisible = false;
                    } else {
                        listViewMembers.setVisibility(View.VISIBLE);
                        memberTextView.setVisibility(View.VISIBLE);
                        buttonMore.setImageResource(R.drawable.ic_chat_more_black_24dp);
                        mIsMemberListViewVisible = true;
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
            // Bind the chat room data to the UI components
            mRoomNameTextView.setText(chatRoom.getRoomName());
            mLastMessageTextView.setText(chatRoom.getLastMessage());

            // Retrieve the selected members for the current chat room
            List<ChatMember> selectedMembers = chatRoom.getSelectedMembers();

            if (selectedMembers != null && !selectedMembers.isEmpty()) {
                // Create an ArrayAdapter to display the selected members in the ListView
                ArrayAdapter<ChatMember> adapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_list_item_1, selectedMembers);
                ListView listViewMembers = itemView.findViewById(R.id.listView_members);
                listViewMembers.setAdapter(adapter);

                // Calculate the total height of the ListView and set its layout params accordingly
                int totalHeight = 0;
                for (int i = 0; i < adapter.getCount(); i++) {
                    View listItem = adapter.getView(i, null, listViewMembers);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }
                ViewGroup.LayoutParams params = listViewMembers.getLayoutParams();
                params.height = totalHeight + (listViewMembers.getDividerHeight() * (adapter.getCount() - 1));
                listViewMembers.setLayoutParams(params);
            }

            // Set the click listener for the ViewHolder
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onChatRoomClick(chatRoom);
                }
            });
        }
    }

    /**
     * Removes an item from the adapter's list and notifies the RecyclerView of the change.
     * @param position The position of the item to remove
     */
    public void removeItem(int position) {
        mChatRooms.remove(position);
        notifyItemRemoved(position);
    }


    /**
     * Updates the selected members in the ViewHolder and notifies the adapter of the changes.
     * @param selectedMembers The new list of selected members
     */
    public void updateSelectedMembers(List<ChatMember> selectedMembers) {
        mSelectedMembers = selectedMembers;
        notifyDataSetChanged();
    }
}
