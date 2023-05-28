package edu.uw.tcss450.kylerr10.chatapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.conversation.ConversationSendViewModel;


/**
 * An activity that displays the conversation between users in a chat room.
 * @author Leyla Ahmed
 */

public class ConversationActivity extends AppCompatActivity {
    private ConversationSendViewModel mSendViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        // Get the chat room name from the intent extra
        String chatRoomName = getIntent().getStringExtra("chatRoomName");

        // Set the conversation name to the chat room name
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView conversationNameTextView = findViewById(R.id.title_chat_room_name);
        conversationNameTextView.setText(chatRoomName);


    }

    void sendMessage() {

        // Get the message and conversation ID from the intent extras
        String message = getIntent().getStringExtra("message");
        int conversationId = getIntent().getIntExtra("conversationId", -1);
        String chatId = getIntent().getStringExtra("chatId");
        System.out.println("CHATID: " + chatId);
        String jwt = getIntent().getStringExtra("jwt");

        // Instantiate the ConversationSendViewModel
        mSendViewModel = new ViewModelProvider(this).get(ConversationSendViewModel.class);
        mSendViewModel.sendMessage(chatId, jwt, message);
    }
}