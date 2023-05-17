package edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_members;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.uw.tcss450.kylerr10.chatapp.R;
import java.util.List;


/**
 * Adapter class for displaying selected chat members in a RecyclerView.
 *
 */
public class ChatSelectedMembersAdapter extends RecyclerView.Adapter<ChatSelectedMembersAdapter.SelectedMemberViewHolder> {
    private List<ChatMember> mSelectedMembers;

    /**
     * Constructor for the ChatSelectedMembersAdapter class.
     * @param selectedMembers The list of selected chat members
     */
    public ChatSelectedMembersAdapter(List<ChatMember> selectedMembers) {
        this.mSelectedMembers = selectedMembers;
    }

    @NonNull
    @Override
    public SelectedMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chat_remove_member, parent, false);
        return new SelectedMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedMemberViewHolder holder, int position) {
        // Bind the data to the ViewHolder at the specified position.
        ChatMember member = mSelectedMembers.get(position);
        holder.bind(member);
    }

    @Override
    public int getItemCount() {
        // Returns the total number of selected members.
        return mSelectedMembers.size();
    }
    /**
     * ViewHolder class for displaying selected chat members.
     */
    public class SelectedMemberViewHolder extends RecyclerView.ViewHolder {
        //The TextView for displaying the member's name
        private TextView nameTextView;

        //The ImageView for the remove button
        private ImageView removeButton;

        /**
         * Constructor for the SelectedMemberViewHolder class.
         * @param itemView The inflated view for this ViewHolder
         */
        public SelectedMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.memberName);
            removeButton = itemView.findViewById(R.id.removeMemberImageView);
        }
        /**
         * Binds the chat member data to the ViewHolder.
         * @param member The chat member object
         */
        public void bind(ChatMember member) {
            nameTextView.setText(member.getName());
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeMember(getAdapterPosition());
                }
            });
        }
    }
    /**
     * Removes a selected member from the list and updates the RecyclerView.
     * @param position The position of the member to remove
     */
    public void removeMember(int position) {
        mSelectedMembers.remove(position);
        notifyItemRemoved(position);
    }
}