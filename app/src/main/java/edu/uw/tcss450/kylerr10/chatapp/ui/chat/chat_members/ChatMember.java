package edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_members;



/**
 * This class Represents a chat member.
 * @author Leyla Ahmed
 */
public class ChatMember {
    //The Id of the chat room
    private String chatId;

    //The name of the chat member
    private String mName;

    //Indicates whether the chat member is selected
    private boolean mSelected;

    /**
     * Constructs a ChatMember object with the specified name.
     * @param name The name of the chat member
     */
    public ChatMember(String name) {
        this.mName = name;
    }


    /**
     * Returns the name of the chat member.
     * @return The name of the chat member
     */
    public String getName() {
        return mName;
    }


    /**
     * Checks if the chat member is selected.
     * @return true if the chat member is selected, false otherwise
     */
    public boolean isSelected() {
        return mSelected;
    }


    /**
     * Sets the selected state of the chat member.
     * @param selected true to set the chat member as selected, false otherwise
     */
    public void setSelected(boolean selected) {
        mSelected = selected;
    }


    /**
     * Sets the chat ID in which the member is present.
     * @param chatId The chat ID
     */
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    /**
     * Returns the chat ID in which the member is present.
     * @return The chat ID
     */
    public String getChatId() {
        return chatId;
    }
}
