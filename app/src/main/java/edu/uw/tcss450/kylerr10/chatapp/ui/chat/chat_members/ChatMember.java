package edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_members;

/**
 * This class Represents a chat member.
 * @author Leyla Ahmed
 */
public class ChatMember {
    //The ID of the chat member
    private int mMemberId;

    //The name of the chat member
    private String mName;

    //Indicates whether the chat member is selected
    private boolean mSelected;

    /**
     * Constructs a ChatMember object with the provided ID and name.
     * @param memberId   The ID of the chat member
     * @param name The name of the chat member
     */
    public ChatMember(int memberId, String name) {
        this.mMemberId = memberId;
        this.mName = name;
        this.mSelected = false;
    }

    public ChatMember(String name) {
        this.mName = name;
    }


    /**
     * Returns the ID of the chat member.
     * @return The ID of the chat member
     */
    public int getMemberId() {
        return mMemberId;
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

    @Override
    public String toString() {
        return mName;
    }

}
