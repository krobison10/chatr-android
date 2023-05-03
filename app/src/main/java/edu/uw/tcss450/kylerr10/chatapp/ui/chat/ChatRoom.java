package edu.uw.tcss450.kylerr10.chatapp.ui.chat;



/**
 * Represents a chat room with a name and last message
 * @author Leyla Ahmed
 */

public class ChatRoom {
    /**
     * The name of the chat room.
     */
    private String mName;

    /**
     * The last message sent in the chat room.
     */
    private String mLastMessage;

    /**
     * Constructs a new ChatRoom object with the given name and last message.
     * @param name the name of the chat room
     * @param lastMessage the last message sent in the chat room
     */
    public ChatRoom(String name, String lastMessage) {
        this.mName = name;
        this.mLastMessage = lastMessage;
    }

    /**
     * Returns the name of the chat room.
     * @return the name of the chat room
     */
    public String getRoomName() {
        return mName;
    }

    /**
     * Returns the last message sent in the chat room.
     * @return the last message sent in the chat room
     */
    public String getLastMessage() {
        return mLastMessage;
    }

    /**
     * Sets the name of the chat room.
     * @param name the new name of the chat room
     */

    public void setRoomName(String name) {
        this.mName = name;
    }

    /**
     * Sets the last message sent in the chat room.
     * @param lastMessage the last message sent in the chat roo
     */
    public void setLastMessage(String lastMessage) {
        this.mLastMessage = lastMessage;
    }

}
