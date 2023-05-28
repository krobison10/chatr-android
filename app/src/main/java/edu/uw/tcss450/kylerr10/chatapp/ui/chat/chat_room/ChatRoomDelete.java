package edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_room;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.ChatViewModel;


/**
 * A callback class for handling swipe gestures
 * on items in a RecyclerView and deleting the swiped item.
 * @author Leyla Ahmed
 */
public class ChatRoomDelete extends ItemTouchHelper.SimpleCallback {

    // The ChatRoomAdapter instance used to remove items from the RecyclerView
    private ChatRoomAdapter mAdapter;

    //The ViewModel associated with the chat.
    private ChatViewModel mViewModel;


    /**
     * Constructor for ChatRoomDelete that sets the swipe directions and the ChatRoomAdapter.
     * @param adapter the ChatRoomAdapter used to remove items from the RecyclerView
     */
    public ChatRoomDelete(ChatRoomAdapter adapter, ChatViewModel viewModel) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
        mViewModel = viewModel;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        // No action on move
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // Get the position of the item being swiped
        int position = viewHolder.getAdapterPosition();
        // Get the chat room ID from the mAdapter using the position
        String chatId = mAdapter.getChatRoomId(position);
        // Delete the chat room from the server
        mViewModel.deleteChatRoom(chatId, position);
    }
}
