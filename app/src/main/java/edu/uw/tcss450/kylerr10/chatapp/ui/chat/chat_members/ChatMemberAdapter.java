package edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_members;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.uw.tcss450.kylerr10.chatapp.R;
import java.util.List;

/**
 * Adapter for managing chat members in the search for CreateChatDialogue member_list RecyclerView.
 * @author Leyla Ahmed
 */
public class ChatMemberAdapter extends RecyclerView.Adapter<ChatMemberAdapter.ChatMemberViewHolder> {

    //The list of chat members
    private List<ChatMember> mMembers;

    //The item click listener for handling member item clicks
    private OnItemClickListener mListener;

    /**
     * Interface for defining item click behavior in the adapter.
     */
    public interface OnItemClickListener {

        //Called when a chat member item in member_list RecyclerView is clicked
        void onItemClick(ChatMember member);
    }

    /**
     * Constructs a ChatMemberAdapter with the provided list of members and item click listener.
     * @param members  The list of chat members to be displayed
     * @param listener The item click listener for handling member item clicks
     */
    public ChatMemberAdapter(List<ChatMember> members, OnItemClickListener listener) {
        this.mMembers = members;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ChatMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_single_contact, parent, false);
        return new ChatMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMemberViewHolder holder, int position) {
        ChatMember member = mMembers.get(position);
        holder.bind(member);
    }

    @Override
    public int getItemCount() {
        return mMembers.size();
    }

    /**
     * ViewHolder class for displaying individual chat members.
     */
    public class ChatMemberViewHolder extends RecyclerView.ViewHolder {
        //The TextView for displaying the member's name
        private TextView nameTextView;

        /**
         * Constructor for the ChatMemberViewHolder class.
         * @param itemView The inflated view for this ViewHolder
         */
        public ChatMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textView);
        }

        /**
         * Binds a chat member to the ViewHolder.
         * @param member The chat member to bind
         */
        public void bind(ChatMember member) {
            nameTextView.setText(member.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(member);
                    }
                }
            });
        }
    }
}