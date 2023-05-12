package edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_dialogue;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import org.jetbrains.annotations.Nullable;
import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.ChatRoom;

/**
 * A DialogFragment for creating a new chat room.
 * Allows the user to input a chat room name, add members, and create the chat room.
 * @author Leyla Ahmed
 */
public class CreateChatDialogue extends DialogFragment {
    public interface OnCreateChatRoomListener {
        void onCreateChatRoom(ChatRoom chatRoom);
    }

    private OnCreateChatRoomListener mListener;

    public void setOnCreateChatRoomListener(OnCreateChatRoomListener listener) {
        mListener = listener;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Set the background of the dialog window to transparent
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_chat, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind views
        MaterialCardView cardView = view.findViewById(R.id.card_view);
        TextInputLayout chatRoomNameInput = view.findViewById(R.id.chat_room_name_input);
        TextInputLayout memberNameInput = view.findViewById(R.id.member_name_input);
        RecyclerView memberList = view.findViewById(R.id.member_list);
        TextInputLayout chatRoomNameEditText = chatRoomNameInput.findViewById(R.id.chat_room_name_input);
        TextInputLayout memberNameEditText = memberNameInput.findViewById(R.id.member_name_input);
        MaterialButton createButton = view.findViewById(R.id.create_button);
        MaterialButton cancelButton = view.findViewById(R.id.cancel_button);


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the new chat room
                String chatRoomName = chatRoomNameEditText.getEditText().getText().toString();
                String memberName = memberNameEditText.getEditText().getText().toString();
                ChatRoom chatRoom = new ChatRoom(chatRoomName, memberName);

                // Call the listener to add the new chat room to the list
                if (mListener != null) {
                    mListener.onCreateChatRoom(chatRoom);
                }

                // Dismiss the dialog
                dismiss();
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });


    }
}

