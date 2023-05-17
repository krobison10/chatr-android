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

public class ChatSelectedMembersAdapter extends RecyclerView.Adapter<ChatSelectedMembersAdapter.SelectedMemberViewHolder> {
    private List<ChatMember> selectedMembers;

    public ChatSelectedMembersAdapter(List<ChatMember> selectedMembers) {
        this.selectedMembers = selectedMembers;
    }

    @NonNull
    @Override
    public SelectedMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chat_remove_member, parent, false);
        return new SelectedMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedMemberViewHolder holder, int position) {
        ChatMember member = selectedMembers.get(position);
        holder.bind(member);
    }

    @Override
    public int getItemCount() {
        return selectedMembers.size();
    }

    public class SelectedMemberViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private ImageView removeButton;

        public SelectedMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.memberName);
            removeButton = itemView.findViewById(R.id.removeMemberImageView);
        }

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

    public void removeMember(int position) {
        selectedMembers.remove(position);
        notifyItemRemoved(position);
    }
}