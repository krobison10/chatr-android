package edu.uw.tcss450.kylerr10.chatapp.listdata;

import java.io.Serializable;

/**
 * Stores the pertinent data for a current contact to be used by the CurrentContactsRecyclerViewAdapter.
 *
 * @author Kyler Robison
 */
public class CurrentContact implements Serializable {
    public final int mConnectionId;
    public final String mFName;
    public final String mLName;
    public final String mEmail;
    public final String mUsername;

    public CurrentContact(int connectionId, String username, String email, String fName, String lName) {
        mConnectionId = connectionId;
        mFName = fName;
        mLName = lName;
        mEmail = email;
        mUsername = username;
    }
}
