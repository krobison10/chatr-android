package edu.uw.tcss450.kylerr10.chatapp.ui.chat;


import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_members.ChatMember;

/**
 * Represents a chat room with a name and last message
 * @author Leyla Ahmed
 */

public class ChatRoom {

    //The name of the chat room.
    private String mName;


    //The last message sent in the chat room.
    private String mLastMessage;

    private static int nextId = 1;

    private int chatId;

    private List<ChatMember> mMemberId;
    private List<ChatMember> selectedMembers;

    public List<ChatMember> getSelectedMembers() {
        return selectedMembers;
    }
    /**
     * Constructs a new ChatRoom object with the given name and last message.
     * @param name the name of the chat room
     */
    public ChatRoom(int chatId, String name) {
        this.mName = name;
        this.chatId = nextId++;
    }
    public ChatRoom(String name, List<ChatMember> memberId){
        this.mName = name;
        this.mMemberId = memberId;
    }
    public int getId() {
        return chatId;
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
     * @param lastMessage the last message sent in the chat room
     */
    public void setLastMessage(String lastMessage) {
        this.mLastMessage = lastMessage;
    }
    public void setSelectedMembers(List<ChatMember> selectedMembers) {
        this.selectedMembers = selectedMembers;
    }

}
