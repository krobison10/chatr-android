package edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_room;



import java.util.List;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_members.ChatMember;

/**
 * Represents a chat room with a name and last message
 * @author Leyla Ahmed
 */

public class ChatRoom {

    //The name of the chat room
    private String mName;

    //The last message sent in the chat room
    private String mLastMessage;

    //The ID of the chat room
    private int mChatId;

    //The list of selected chat members
    private List<ChatMember> mSelectedMembers;



    /**
     * Constructs a new ChatRoom object with the given chat ID and name.
     * @param chatId The ID of the chat room
     * @param name   The name of the chat room
     */
    public ChatRoom(int chatId, String name, String lastMessage) {
        this.mChatId = chatId;
        this.mName = name;
        this.mLastMessage = lastMessage;

    }

    /**
     * Returns the chat ID of the chat room.
     * @return The chat ID.
     */
    public int getChatId() {
        return mChatId;
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
     * Sets the list of selected members for the chat room.
     * @param selectedMembers The list of selected chat members
     */
    public void setSelectedMembers(List<ChatMember> selectedMembers) {
        this.mSelectedMembers = selectedMembers;
    }


    /**
     * Retrieves the list of selected chat members.
     * @return The list of selected chat members
     */
    public List<ChatMember> getSelectedMembers() {
        return mSelectedMembers;
    }

}
