package edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_members;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.uw.tcss450.kylerr10.chatapp.R;
import java.util.ArrayList;
import java.util.List;

public class ChatMemberAdapter extends RecyclerView.Adapter<ChatMemberAdapter.ChatMemberViewHolder> {
    private List<ChatMember> members;
    private OnItemClickListener listener;


    public interface OnItemClickListener {
        void onItemClick(ChatMember member);
    }

    public ChatMemberAdapter(List<ChatMember> members, OnItemClickListener listener) {
        this.members = members;
        this.listener = listener;
    }

    public ChatMemberAdapter(List<ChatMember> members) {
        this.members = members;
    }

    @NonNull
    @Override
    public ChatMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_single_contact, parent, false);
        return new ChatMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMemberViewHolder holder, int position) {
        ChatMember member = members.get(position);
        holder.bind(member);
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class ChatMemberViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;

        public ChatMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textView);
        }

        public void bind(ChatMember member) {
            nameTextView.setText(member.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(member);
                    }
                }
            });
        }
    }
    public void updateMembers(List<ChatMember> updatedMembers) {
        members = updatedMembers;
        notifyDataSetChanged();
    }
}