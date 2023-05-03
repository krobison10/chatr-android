package edu.uw.tcss450.kylerr10.chatapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class ConversationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        // Get the chat room name from the intent
        String chatRoomName = getIntent().getStringExtra("chatRoomName");

        // Set the chat room name as the title of the activity
        TextView titleTextView = findViewById(R.id.title_chat_room_name);
        titleTextView.setText(chatRoomName);
    }
}