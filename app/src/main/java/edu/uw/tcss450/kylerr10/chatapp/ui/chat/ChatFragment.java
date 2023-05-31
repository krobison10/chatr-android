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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import edu.uw.tcss450.kylerr10.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.kylerr10.chatapp.ConversationActivity;
import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_dialogue.CreateChatDialogue;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_members.ChatMember;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_room.ChatRoom;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_room.ChatRoomAdapter;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_room.ChatRoomDelete;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.conversation.ConversationSendViewModel;


/**
 * A simple {@link Fragment} subclass. This fragment displays a list of chat rooms
 * and allows the user to click on a room to view its conversation.
 * @author Leyla Ahmed
 */
public class ChatFragment extends Fragment implements ChatRoomAdapter.OnChatRoomClickListener{

    //List of chat rooms used by the ChatRoomAdapter to display the list of chat rooms in a RecyclerView
    private List<ChatRoom> mChatRooms;

    //The ChatRoomAdapter instance used to populate the RecyclerView with chat room data
    private ChatRoomAdapter mAdapter;

    //The ViewModel associated with the chat
    private ChatViewModel mViewModel;

    private ConversationSendViewModel mSendViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
        mChatRooms = new ArrayList<>();

        //Access JWT
        UserInfoViewModel model = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        String jwt = model.getJWT().toString();
        String email = model.getEmail();
        mViewModel.setJWT(jwt);
        mViewModel.setEmail(email);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Set up the RecyclerView with the chat rooms
        RecyclerView recyclerView = view.findViewById(R.id.list_chat_room);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ChatRoomAdapter(mChatRooms);
        recyclerView.setAdapter(mAdapter);

        //Sets the ChatViewModel instance to be used by the helper
        ChatViewModelHelper.setViewModel(mViewModel);

        // Attach a swipe-to-delete callback to the RecyclerView using an ItemTouchHelper
        ChatRoomDelete swipeToDeleteCallback = new ChatRoomDelete(mAdapter, mViewModel);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Find the ExtendedFloatingActionButton view and set a click listener on it
        ExtendedFloatingActionButton fab = view.findViewById(R.id.floating_action_button);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateChatDialogue dialog = new CreateChatDialogue();
                dialog.show(getParentFragmentManager(), "CreateChatDialog");
            }
        });



        // Observe the chatRoomsLiveData and update the adapter when the data changes
        mViewModel.getChatRoomsLiveData().observe(getViewLifecycleOwner(), new Observer<List<ChatRoom>>() {
            @Override
            public void onChanged(List<ChatRoom> chatRooms) {
                mAdapter.updateChatRooms(chatRooms);

                // Iterate through each chat room
                for (ChatRoom chatRoom : chatRooms) {

                    // Retrieve the chat room's ID
                    String chatId = String.valueOf(chatRoom.getChatId());


                    // Call the method to get the chat room members
                    mViewModel.getChatRoomMembers(chatId, new ChatViewModel.ChatRoomMembersCallback() {
                        @Override
                        public void onSuccess(List<ChatMember> members) {

                            for (ChatMember member : members) {
                                member.setChatId(chatId); // Set the chat ID for each member
                            }

                            chatRoom.setSelectedMembers(members); // Set the members for the chat room
                            mAdapter.updateChatRoomMembers(Integer.parseInt(chatId), members);

                        }

                        @Override
                        public void onError() {
                            // Handle error if necessary
                        }
                    });
                }
            }
        });

        mAdapter.setOnChatRoomClickListener(this);

        return view;
    }
    @Override
    public void onChatRoomClick(ChatRoom chatRoom) {
        // Retrieve the chat's ID
        String chatId = String.valueOf(chatRoom.getChatId());
        String jwt = mViewModel.getJWT();
        // Handle the click event for the chat room
        Intent intent = new Intent(getActivity(), ConversationActivity.class);
        intent.putExtra("chatRoomName", chatRoom.getRoomName());
        intent.putExtra("chatId", chatId);
        intent.putExtra("jwt", jwt);
        startActivity(intent);
    }

}