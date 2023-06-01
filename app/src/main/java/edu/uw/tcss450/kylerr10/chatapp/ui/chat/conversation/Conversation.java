package edu.uw.tcss450.kylerr10.chatapp.ui.chat.conversation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import java.util.ArrayList;
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

    // Name of the message's sender
    private String mSenderName;

    // Content of the message
    private String mContent;
    private int viewType;
    // Timestamp of the message
    private String mTimestamp;

    private String mEmail;

    private int mChatId;


    /**
     * Constructs a new Conversation object.
     */
    public Conversation() {
        this.mMessages = new ArrayList<>();
    }

    /**
     * Constructs a Conversation object with the provided conversation ID, content, sender name, and timestamp.
     * @param conversationId The ID of the conversation
     * @param content The content of the conversation
     * @param senderName The name of the sender
     * @param timestamp The timestamp of the conversation
     */
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
     * Returns the name of the message's sender.
     * @return the sender name
     */
    public String getSenderName() {
        return mSenderName;
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
    // Getter method for messages
    public List<Conversation> getMessages() {
        if (mMessages == null) {
            mMessages = new ArrayList<>();
        }
        return mMessages;
    }

    /**
     * Static factory method to turn a properly formatted JSON String into a
     * ChatMessage object.
     * @param cmAsJson the String to be parsed into a ChatMessage Object
     * @return a ChatMessage Object with the details contained in the JSON String
     * @throws JSONException when cmAsString cannot be parsed into a ChatMessage
     */
    public static Conversation createFromJsonString(final String cmAsJson) throws JSONException {
        final JSONObject msg = new JSONObject(cmAsJson);
        return new Conversation(msg.getInt("messageid"),
        msg.getString("message"),
        msg.getString("email"),
        msg.getString("timestamp"));

    }

    /**
     * Set the messages in the conversation.
     * @param messages The list of messages
     */
    public void setMessages(List<Conversation> messages) {
        this.mMessages = messages;
    }

    /**
     * Set the timestamp of the message.
     * @param timestamp The timestamp
     */
    public void setTimestamp(String timestamp) {
        this.mTimestamp = timestamp;
    }

    /**
     * Set the content of the message.
     * @param messageText The content of the message
     */
    public void setContent(String messageText) {
        this.mContent = messageText;
    }

    /**
     * Set the name of the sender.
     * @param email The name/email of the sender
     */
    public void setName(String email) {
        this.mSenderName = email;
    }

    /**
     * Set the conversation ID.
     * @param messageId The conversation ID
     */
    public void setConversationId(int messageId) {
        this.mConversationId = messageId;
    }

    /**
     * Set the sender with the given sender ID and sender name.
     * @param senderId The ID of the sender
     * @param senderName The name of the sender
     */
    public void setSender(int senderId, String senderName) {
        if (users != null) {
            users.put(senderId, senderName);
        } else {
            // Initialize the users map
            users = new HashMap<>();
            users.put(senderId, senderName);
        }
    }

    /**
     * Set the receiver with the given receiver ID and receiver name.
     * @param receiverId The ID of the receiver
     * @param receiverName The name of the receiver
     */
    public void setReceiver(int receiverId, String receiverName) {
        if (users != null) {
            users.put(receiverId, receiverName);
        } else {
            // Initialize the users map
            users = new HashMap<>();
            users.put(receiverId, receiverName);
        }
    }

    /**
     * Get the name of the sender.
     * @return The name of the sender
     */
    public String getName() {
        return mSenderName;
    }

    /**
     * Get the view type.
     * @return The view type
     */
    public int getViewType() {
        return viewType;
    }

    /**
     * Set the view type.
     * @param viewType The view type
     */
    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

}