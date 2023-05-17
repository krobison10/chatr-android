package edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_dialogue;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.appcompat.widget.SearchView;


import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.ChatRoom;
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

    public interface OnCreateChatRoomListener {
        void onCreateChatRoom(ChatRoom chatRoom, List<ChatMember> selectedMembers);
    }

    public void setOnCreateChatRoomListener(OnCreateChatRoomListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.fragment_create_chat, container, false);
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
        allMembers.add(new ChatMember(1, "John Doe"));
        allMembers.add(new ChatMember(2, "Jane Smith"));
        allMembers.add(new ChatMember(3, "David Johnson"));
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

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the new chat room
                String chatRoomName = chatRoomNameInput.getText().toString();
                List<ChatMember> selectedMembers = getSelectedMembers();
                int chatid = 2;
                ChatRoom chatRoom = new ChatRoom(chatid++, chatRoomName);

                // Call the listener to add the new chat room to the list
                if (mListener != null) {
                    mListener.onCreateChatRoom(chatRoom, selectedMembers);
                }
                createChatRoomOnServer(chatRoomName);
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

    private void createChatRoomOnServer(String chatRoomName) {
        // Create a JSON object with the chat room name
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("name", chatRoomName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a request queue
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        // Make a POST request to the backend API
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://localhost:5000/chats", jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response from the server
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                int chatID = response.getInt("chatID");
                                // Chat room created successfully with the provided chatID
                            } else {
                                // Chat room creation failed
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Error parsing the response JSON
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error response
                    }
                });

        // Add the request to the request queue
        queue.add(request);
    }


}