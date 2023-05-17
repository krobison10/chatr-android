package edu.uw.tcss450.kylerr10.chatapp.ui.chat.conversation;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a conversation between users.
 *  @author Leyla Ahmed
 */
public class Conversation {

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

    // Timestamp of the message
    private long mTimestamp;

    private static int mConversationIdCounter = 0;

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
    public Conversation(int conversationId, int senderId, String senderName, int receiverId, String receiverName, String content, long timestamp) {
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

    public static int generateConversationId() {
        return ++mConversationIdCounter;
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
    public void addMessage(int conversationId, int senderId, String senderName, int receiverId, String receiverName, String content, long timestamp) {
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
     * @return the timestamp
     */
    public long getTimestamp() {
        Date currentDate = new Date();
        return mTimestamp = currentDate.getTime();
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

}