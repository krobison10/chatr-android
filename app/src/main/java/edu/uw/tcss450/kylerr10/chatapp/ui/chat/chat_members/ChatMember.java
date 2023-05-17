package edu.uw.tcss450.kylerr10.chatapp.ui.chat.chat_members;

public class ChatMember {
    private int mMemberId;
    private String mName;
    private boolean mSelected;

    public ChatMember(int id, String name) {
        this.mMemberId = id;
        this.mName = name;
        this.mSelected = false;
    }


    public int getId() {
        return mMemberId;
    }

    public String getName() {
        return mName;
    }

    public int getMemberId() {
        return mMemberId;
    }
    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }

    @Override
    public String toString() {
        return mName;
    }

}
