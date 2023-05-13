package edu.uw.tcss450.kylerr10.chatapp.ui.chat;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A callback class for handling swipe gestures
 * on items in a RecyclerView and deleting the swiped item.
 *
 * @author Leyla Ahmed
 */
public class ChatRoomDelete extends ItemTouchHelper.SimpleCallback {

    // The ChatRoomAdapter instance used to remove items from the RecyclerView
    private ChatRoomAdapter mAdapter;

    /**
     * Constructor for ChatRoomDelete that sets the swipe directions and the ChatRoomAdapter
     * @param adapter the ChatRoomAdapter used to remove items from the RecyclerView
     */
    public ChatRoomDelete(ChatRoomAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
    }
    /**
     * Unused method for onMove event
     * @param recyclerView the RecyclerView being moved
     * @param viewHolder the ViewHolder being moved
     * @param target the target ViewHolder
     * @return always returns false since onMove is unused
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        // No action on move
        return false;
    }
    /**
     * Method called when an item is swiped away
     * @param viewHolder the ViewHolder being swiped
     * @param direction the direction of the swipe (LEFT or RIGHT)
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // Get the position of the item being swiped
        int position = viewHolder.getAdapterPosition();
        // Remove the item from the adapter
        mAdapter.removeItem(position);
    }
}
