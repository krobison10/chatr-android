package edu.uw.tcss450.kylerr10.chatapp.listdata;

import java.io.Serializable;

/**
 * Represents the data for a notification in the homepage. Is used by the RecyclerView.
 *
 * @author Kyler Robison
 */
public class Notification implements Serializable {
    private final int mType;
    private final String mTitle;
    private final String mContent;
    private final String mTimestamp;

    public Notification(int type, String title, String content, String time) {
        mType = type;
        mTitle = title;
        mContent = content;
        mTimestamp = time;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public int getType() {
        return mType;
    }

    public static class Type {
        public static final int MESSAGE = 0;
        public static final int CHAT = 1;
        public static final int CONTACT = 2;
    }
}
