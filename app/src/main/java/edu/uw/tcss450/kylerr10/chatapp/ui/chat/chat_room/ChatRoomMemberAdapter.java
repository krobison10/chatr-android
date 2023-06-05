package edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_room;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.ChatViewModelHelper;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_members.ChatMember;


/**
 * Adapter for managing chat room members in a RecyclerView.
 * @author Leyla Ahmed
 */
public class ChatRoomMemberAdapter extends RecyclerView.Adapter<ChatRoomMemberAdapter.MemberViewHolder> {

    //The list of chat members
    private List<ChatMember> mMembers = new ArrayList<>();

    public ChatRoomMemberAdapter() {
        // Required empty public constructor
    }

    /**
     * Constructs a ChatRoomMemberAdapter with the provided list of members.
     * @param members The list of chat members to be displayed
     */
    public ChatRoomMemberAdapter(List<ChatMember> members) {

        this.mMembers = members;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chat_remove_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
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
    public class MemberViewHolder extends RecyclerView.ViewHolder {

        //The TextView for displaying the name of the chat member
        private TextView nameTextView;

        //The ImageView for the remove member button
        private ImageView removeButton;

        /**
         * Constructs a MemberViewHolder for the provided item view.
         * @param itemView The item view representing a chat member
         */
        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.memberName);
            removeButton = itemView.findViewById(R.id.removeMemberImageView);
        }


        /**
         * Binds a chat member to the ViewHolder.
         * @param member The chat member to bind
         */
        public void bind(ChatMember member) {
            nameTextView.setText(member.getName());
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ChatMember member = mMembers.get(position);
                        String email = member.getName();
                        String chatId = member.getChatId();

                        ChatViewModelHelper.removeMember(chatId, email);

                        removeMember(position);
                    }
                }
            });
        }
    }


    /**
     * Removes a member from the list at the specified position.
     * @param position The position of the member to remove
     */
    public void removeMember(int position) {
        mMembers.remove(position);
        notifyItemRemoved(position);
    }


}
