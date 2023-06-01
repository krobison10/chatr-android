package edu.uw.tcss450.kylerr10.chatapp.ui.chat.conversation;


import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a conversation between users.
 *  @author Leyla Ahmed
 */
public class Conversation implements Serializable {
    private Map<Integer, String> users;
    // Unique ID of the conversation
    private int mConversationId;

    // List of messages in the conversation
    private List<Conversation> mMessages;

    // ID of the message's sender
    private int mSenderId;

    // ID of the message's receiver
    private int mReceiverId;

    // Name of the message's sender
    private String mSenderName;

    // Name of the message's receiver
    private String mReceiverName;

    // Content of the message
    private String mContent;
    private int viewType;
    // Timestamp of the message
    private String mTimestamp;

    //Counter for generating conversation IDs
    private int mConversationIdCounter;

    /**
     * Constructs a new Conversation object.
     */
    public Conversation() {
        // Required empty public constructor
    }

    /**
     * Constructs a new Conversation object with the given conversation ID, sender and receiver IDs and names.
     * @param conversationId the ID of the conversation
     * @param senderId   the ID of the message's sender
     * @param senderName the name of the message's sender
     * @param receiverId the ID of the message's receiver
     * @param receiverName the name of the message's receiver
     * @param content    the content of the message
     * @param timestamp  the timestamp of the message
     */
    public Conversation(int conversationId, int senderId, String senderName, int receiverId, String receiverName, String content, String timestamp) {
        this.mConversationId = conversationId;
        this.mSenderId = senderId;
        this.mSenderName = senderName;
        this.mReceiverId = receiverId;
        this.mReceiverName = receiverName;
        this.mContent = content;
        this.mTimestamp = timestamp;
        mMessages = new ArrayList<>();
    }


    /**
     * Constructs a new Conversation object with the given sender and receiver IDs and names.
     * @param senderId   the ID of the message's sender
     * @param senderName the name of the message's sender
     * @param receiverId the ID of the message's receiver
     * @param receiverName the name of the message's receiver
     */
    public Conversation(int senderId, String senderName, int receiverId, String receiverName) {
        this.mSenderId = senderId;
        this.mReceiverId = receiverId;
        mMessages = new ArrayList<>();
    }

    public Conversation(int conversationId, String content, String senderName, String timestamp) {
        this.mConversationId = conversationId;
        this.mSenderName = senderName;
        this.mContent = content;
        this.mTimestamp = timestamp;
        mMessages = new ArrayList<>();
    }


    /**
     * Returns the ID of the conversation.
     * @return the conversation ID
     */
    public int getConversationId() {
        return mConversationId;
    }

    /**
     * Returns the ID of the message's sender.
     * @return the sender ID
     */
    public int getSenderId() {
        return mSenderId;
    }

    /**
     * Returns the name of the message's sender.
     * @return the sender name
     */
    public String getSenderName() {
        return mSenderName;
    }

    /**
     * Returns the ID of the message's receiver.
     * @return the receiver ID
     */
    public int getReceiverId() {
        return mReceiverId;
    }

    /**
     * Returns the name of the message's receiver.
     * @return the receiver name
     */
    public String getReceiverName() {
        return mReceiverName;
    }

    /**
     * Adds a new message to the conversation.
     * @param senderId   the ID of the message's sender
     * @param senderName the name of the message's sender
     * @param receiverId the ID of the message's receiver
     * @param receiverName the name of the message's receiver
     * @param content    the content of the message
     * @param timestamp  the timestamp of the message
     */
    public void addMessage(int conversationId, int senderId, String senderName, int receiverId, String receiverName, String content, String timestamp) {
        Conversation message = new Conversation(conversationId, senderId, senderName, receiverId, receiverName, content, timestamp);
        mMessages.add(message);
    }

    /**
     * Returns the content of the conversation.
     * @return the content
     */
    public String getContent() {
        return mContent;
    }

    /**
     * Returns the timestamp of the conversation.
     *
     * @return the timestamp
     */
    public String getTimestamp() {
        return mTimestamp;
    }

    /**
     * Returns the list of messages in the conversation.
     * @return the list of messages
     */
    public List<Conversation> getMessages() {
        return mMessages;
    }

    /**
     * Checks if a message in the conversation was sent by a given user ID.
     * @param userId the ID of the user
     * @return true if the message was sent by the given user ID, false otherwise
     */
    public boolean isFromSender(int userId) {
        return this.mSenderId == userId;
    }

    /**
     * Static factory method to turn a properly formatted JSON String into a
     * ChatMessage object.
     * @param cmAsJson the String to be parsed into a ChatMessage Object.
     * @return a ChatMessage Object with the details contained in the JSON String.
     * @throws JSONException when cmAsString cannot be parsed into a ChatMessage.
     */
    public static Conversation createFromJsonString(final String cmAsJson) throws JSONException {
        final JSONObject msg = new JSONObject(cmAsJson);
        int conversationId = msg.getInt("mConversationId");
        String content = msg.getString("mContent");
        String senderName = msg.getString("mSenderName");
        String timestamp = msg.getString("mTimestamp");
        return new Conversation(conversationId, content, senderName, timestamp);
    }

    public void setMessages(List<Conversation> messages) {
        this.mMessages = messages;
    }

    public void setTimestamp(String timestamp) {
        this.mTimestamp = timestamp;
    }

    public void setContent(String messageText) {
        this.mContent = messageText;
    }

    public void setName(String email) {
        this.mSenderName = email;
    }

    public void setConversationId(int messageId) {
        this.mConversationId = messageId;
    }

    public void setSender(int senderId, String senderName) {
        if (users != null) {
            users.put(senderId, senderName);
        } else {
            // Initialize the users map
            users = new HashMap<>();
            users.put(senderId, senderName);
        }
    }

    public void setReceiver(int receiverId, String receiverName) {
        if (users != null) {
            users.put(receiverId, receiverName);
        } else {
            // Initialize the users map
            users = new HashMap<>();
            users.put(receiverId, receiverName);
        }
    }

    public String getName() {
        return mSenderName;
    }

    public Map<Integer, String> getUsers() {
        return users;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }


}