package edu.uw.tcss450.kylerr10.chatapp.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.auth0.android.jwt.JWT;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import edu.uw.tcss450.kylerr10.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.kylerr10.chatapp.ConversationActivity;
import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_dialogue.CreateChatDialogue;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_members.ChatMember;

/**
 * A simple {@link Fragment} subclass. This fragment displays a list of chat rooms
 * and allows the user to click on a room to view its conversation.
 * @author Leyla Ahmed
 */
public class ChatFragment extends Fragment implements ChatRoomAdapter.OnChatRoomClickListener {

    //List of chat rooms used by the ChatRoomAdapter to display the list of chat rooms in a RecyclerView
    private List<ChatRoom> mChatRooms;

    //The ChatRoomAdapter instance used to populate the RecyclerView with chat room data
    private ChatRoomAdapter mAdapter;

    //The ViewModel associated with the chat.
    private ChatViewModel mViewModel;



    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        mChatRooms = new ArrayList<>();

        //Access JWT
        UserInfoViewModel model = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        String jwt = model.getJWT().toString();
        mViewModel.setJWT(jwt);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Set up the RecyclerView with the chat rooms
        RecyclerView recyclerView = view.findViewById(R.id.list_chat_room);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ChatRoomAdapter(mChatRooms);
        recyclerView.setAdapter(mAdapter);

        //Attach a swipe-to-delete callback to the RecyclerView using an ItemTouchHelper.

        ChatRoomDelete swipeToDeleteCallback = new ChatRoomDelete(mAdapter, mViewModel);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Find the ExtendedFloatingActionButton view and set a click listener on it
        ExtendedFloatingActionButton fab = view.findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateChatDialogue dialog = new CreateChatDialogue();
                dialog.setOnCreateChatRoomListener(new CreateChatDialogue.OnCreateChatRoomListener() {
                    @Override
                    public void onCreateChatRoom(ChatRoom chatRoom, List<ChatMember> selectedMembers) {
                        chatRoom.setSelectedMembers(selectedMembers);
                        // Add the new chat room to the list and notify the adapter
                        mChatRooms.add(chatRoom);
                        mAdapter.updateSelectedMembers(selectedMembers);
                        mAdapter.notifyDataSetChanged();

                        // Update the members' list in the RecyclerView of the new chat
                        ChatRoomMemberAdapter chatRoomMemberAdapter = chatRoom.getMemberAdapter();
                        if (chatRoomMemberAdapter != null) {
                            chatRoomMemberAdapter.setMembers(selectedMembers);
                        }

                        // Get the chat room name and convert selectedMembers to a list of emails
                        String chatRoomName = chatRoom.getRoomName();
                        List<String> emails = new ArrayList<>();
                        for (ChatMember member : selectedMembers) {
                            String email = member.getName();
                            emails.add(email);
                        }

                        // Call the createChatRoom method of ChatViewModel
                        mViewModel.createChatRoom(chatRoomName, emails);
                    }
                });
                dialog.show(getParentFragmentManager(), "CreateChatDialog");
            }
        });
        // Observe the chatRoomsLiveData and update the adapter when the data changes

        mViewModel.getChatRoomsLiveData().observe(getViewLifecycleOwner(), new Observer<List<ChatRoom>>() {
            @Override
            public void onChanged(List<ChatRoom> chatRooms) {
                if (chatRooms.size() != mChatRooms.size()) {
                    mChatRooms.clear();
                    mChatRooms.addAll(chatRooms);
                    mAdapter.notifyDataSetChanged();
                    mViewModel.getChatRooms();
                }
            }
        });

        mAdapter.setOnChatRoomClickListener(this);
        return view;
    }
    @Override
    public void onChatRoomClick(ChatRoom chatRoom) {
        // Handle the click event for the chat room
        Intent intent = new Intent(getActivity(), ConversationActivity.class);
        intent.putExtra("chatRoomName", chatRoom.getRoomName());
        startActivity(intent);
    }

}