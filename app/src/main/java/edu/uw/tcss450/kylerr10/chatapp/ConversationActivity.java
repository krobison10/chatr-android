package edu.uw.tcss450.kylerr10.chatapp;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import edu.uw.tcss450.kylerr10.chatapp.model.NewMessageCountViewModel;
import edu.uw.tcss450.kylerr10.chatapp.services.PushReceiver;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.conversation.Conversation;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.conversation.ConversationAdapter;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.conversation.ConversationFragment;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.conversation.ConversationSendViewModel;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.conversation.ConversationViewModel;


import edu.uw.tcss450.kylerr10.chatapp.ui.ThemeManager;

/**
 * An activity that displays the conversation between users in a chat room.
 * @author Leyla Ahmed
 */

public class ConversationActivity extends AppCompatActivity implements ConversationFragment.ConversationFragmentCallback {
    private ConversationSendViewModel mSendViewModel;
    private MainPushMessageReceiver mMainPushMessageReceiver;
    private ConversationViewModel mConversationViewModel;
    private ConversationAdapter mAdapter;
    private String chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.applyTheme(this); // Required to apply user's chosen theme to activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        // Get the chat room name from the intent extra
        String chatRoomName = getIntent().getStringExtra("chatRoomName");
        System.out.println("name: "+chatRoomName);

        // Set the conversation name to the chat room name
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView conversationNameTextView = findViewById(R.id.title_chat_room_name);
        conversationNameTextView.setText(chatRoomName);

        // Instantiate the ConversationSendViewModel
        mSendViewModel = new ViewModelProvider(this).get(ConversationSendViewModel.class);

        // Instantiate the ConversationViewModel
        mConversationViewModel = new ViewModelProvider(this).get(ConversationViewModel.class);

        // Get the initial message from the intent extras and send it
        String initialMessage = getIntent().getStringExtra("message");
        chatId = getIntent().getStringExtra("chatId");
        String jwt = getIntent().getStringExtra("jwt");
        sendMessage(chatId, jwt, initialMessage);
        System.out.println("message: "+initialMessage);
        System.out.println("chat: "+ chatId);
        System.out.println("jwt: "+ jwt);
        // Create the MainPushMessageReceiver and pass the ConversationViewModel instance
        mMainPushMessageReceiver = new MainPushMessageReceiver(mConversationViewModel, chatId, jwt);
    }

    @Override
    public void onSendMessage(String message) {
        // Send the message
        String chatId = getIntent().getStringExtra("chatId");
        String jwt = getIntent().getStringExtra("jwt");
        sendMessage(chatId, jwt, message);
    }

    private void sendMessage(String chatId, String jwt, String message) {
        // Check if the required data is available
        if (chatId != null && jwt != null && message != null) {
            mSendViewModel.sendMessage(chatId, jwt, message);
        } else {
            // Handle the case when the required data is missing
            Toast.makeText(this, "Failed to send message. Required data missing.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ConversationActivity", "onResume: Registering PushReceiver");
        IntentFilter intentFilter = new IntentFilter(PushReceiver.RECEIVED_NEW_MESSAGE);

        Log.d("ConversationActivity", String.valueOf(intentFilter));
        registerReceiver(mMainPushMessageReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("ConversationActivity", "onPause: Unregistering PushReceiver");
        unregisterReceiver(mMainPushMessageReceiver);
    }

    public static class MainPushMessageReceiver extends BroadcastReceiver {
        private ConversationViewModel mConversationViewModel;
        private String mChatId;
        private String mJwt;

        public static final String RECEIVED_NEW_MESSAGE = "new message from pushy";

        public MainPushMessageReceiver(ConversationViewModel conversationViewModel, String chatId, String jwt) {
            mConversationViewModel = conversationViewModel;
            mChatId = chatId;
            mJwt = jwt;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("MainPushMessageReceiver", "Received push notification "+context );
            Log.d("MainPushMessageReceiver", "Received push notification "+intent );

            if (intent.hasExtra("chatMessage")) {
                Log.d("MainPushMessageReceiver", "Received chatMessage extra");
                Conversation message = (Conversation) intent.getSerializableExtra("chatMessage");
                int chatId = intent.getIntExtra("chatid", -1);
                Log.d("MainPushMessageReceiver", "chatId: " + chatId);

                // If the received message belongs to the current chat room
                if (mChatId.equals(String.valueOf(chatId))) {
                    Log.d("MainPushMessageReceiver", "Message belongs to the current chat room");
                    mConversationViewModel.addMessage(chatId,message);
                }
            }
        }
    }
}