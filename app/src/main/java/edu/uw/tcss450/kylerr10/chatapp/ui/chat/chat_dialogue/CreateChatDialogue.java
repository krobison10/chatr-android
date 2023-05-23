package edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_dialogue;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.appcompat.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.ChatRoom;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.ChatViewModel;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_members.ChatMember;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_members.ChatMemberAdapter;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_members.ChatSelectedMembersAdapter;

/**
 * A DialogFragment for creating a new chat room.
 * Allows the user to input a chat room name, add members, and create the chat room.
 * @author Leyla Ahmed
 */
public class CreateChatDialogue extends DialogFragment {
    private List<ChatMember> allMembers = new ArrayList<>();
    private List<ChatMember> filteredMembers = new ArrayList<>();
    private List<ChatMember> selectedMembers = new ArrayList<>();
    private OnCreateChatRoomListener mListener;
    private ChatMemberAdapter adapter;
    private ChatSelectedMembersAdapter selectedMembersAdapter;

    private ChatViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public interface OnCreateChatRoomListener {
        void onCreateChatRoom(ChatRoom chatRoom, List<ChatMember> selectedMembers);
    }

    public void setOnCreateChatRoomListener(OnCreateChatRoomListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_create_chat, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText chatRoomNameInput = view.findViewById(R.id.chat_room_name_input);
        MaterialButton createButton = view.findViewById(R.id.create_button);
        MaterialButton cancelButton = view.findViewById(R.id.cancel_button);
        SearchView searchView = view.findViewById(R.id.search_view);
        RecyclerView memberList = view.findViewById(R.id.member_list);
        RecyclerView selectedMembersList = view.findViewById(R.id.added_members);
        selectedMembersList.setLayoutManager(new LinearLayoutManager(requireContext()));
        selectedMembersAdapter = new ChatSelectedMembersAdapter(selectedMembers);
        selectedMembersList.setAdapter(selectedMembersAdapter);


        // Create a list of all chat members
        allMembers.add(new ChatMember("test1@test.com "));
        allMembers.add(new ChatMember("test2@test.com"));
        allMembers.add(new ChatMember("test3@test.com"));
        // Add more members as needed

        // Create an empty list for filtered members
        filteredMembers = new ArrayList<>();

        // Create and set up the adapter
        adapter = new ChatMemberAdapter(filteredMembers, new ChatMemberAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ChatMember member) {
                selectMember(member);
            }
        });
        memberList.setHasFixedSize(true);
        memberList.setLayoutManager(new LinearLayoutManager(requireContext()));
        memberList.setAdapter(adapter);

        // Set up search functionality
// Set up search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterMembers(newText);
                return true;
            }
        });

// Set up search close button listener
// Set up search close button listener
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // Clear the filtered members list and notify the adapter
                filteredMembers.clear();
                adapter.notifyDataSetChanged();

                // Clear the text in the SearchView
                searchView.setQuery("", false);
                searchView.clearFocus();

                return false;
            }
        });

// Set up focus change listener for the SearchView
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // Clear the text in the SearchView when focus is lost
                    searchView.setQuery("", false);
                }
            }
        });

// OnClickListener for the createButton
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the new chat room
                String chatRoomName = chatRoomNameInput.getText().toString();
                List<ChatMember> selectedMembers = getSelectedMembers();

                // Extract emails from selected members
                List<String> emails = new ArrayList<>();
                for (ChatMember member : selectedMembers) {
                    String email = member.getName();
                    emails.add(email);
                }

                // Call the createChatRoom method of ChatViewModel
                mViewModel.createChatRoom(chatRoomName, emails);

                // Observe the chatIdLiveData
                mViewModel.getChatIdLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String chatId) {
                        if (chatId != null) {
                            // Add selected members to the chat room
                            for (ChatMember member : selectedMembers) {
                                String email = member.getName();
                                mViewModel.addUserToChat(chatId, email);
                                Log.d("CreateChatRoom", "Added user " + email + " to chat ID: " + chatId);

                            }
                        } else {
                            // Handle the case when the chatId is null or not available
                            Log.e("CreateChatRoom", "Failed to retrieve chatId from server");
                            // Show an error message or take appropriate action
                        }
                    }
                });
                // Dismiss the dialog
                dismiss();
            }

        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void filterMembers(String query) {
        filteredMembers.clear();
        for (ChatMember member : allMembers) {
            if (member.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredMembers.add(member);
            }
        }
        adapter.notifyDataSetChanged();  // Add this line
    }

    private void selectMember(ChatMember member) {
        if (!member.isSelected()) {
            if (!selectedMembers.contains(member)) {
                member.setSelected(true);
                selectedMembers.add(member);
                selectedMembersAdapter.notifyDataSetChanged();
            }
        } else {
            member.setSelected(false);
            selectedMembersAdapter.notifyDataSetChanged();
        }
    }

    private List<ChatMember> getSelectedMembers() {
        return selectedMembers;
    }

}