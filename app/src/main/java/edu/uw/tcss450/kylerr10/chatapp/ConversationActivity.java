package edu.uw.tcss450.kylerr10.chatapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import edu.uw.tcss450.kylerr10.chatapp.ui.ThemeManager;

/**
 * An activity that displays the conversation between users in a chat room.
 * @author Leyla Ahmed
 */

public class ConversationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.applyTheme(this); // Required to apply user's chosen theme to activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        // Get the chat room name from the intent extra
        String chatRoomName = getIntent().getStringExtra("chatRoomName");

        // Set the conversation name to the chat room name
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView conversationNameTextView = findViewById(R.id.title_chat_room_name);
        conversationNameTextView.setText(chatRoomName);
    }
}