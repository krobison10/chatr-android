package edu.uw.tcss450.kylerr10.chatapp.listdata;

import java.io.Serializable;

/**
 * Stores the pertinent data for a current contact to be used by the CurrentContactsRecyclerViewAdapter.
 *
 * @author Kyler Robison
 */
public class Contact implements Serializable {
    //TODO: Encapsulate
    public final int mConnectionId;
    public final String mFName;
    public final String mLName;
    public final String mEmail;
    public final String mUsername;

    public Contact(int connectionId, String username, String email, String fName, String lName) {
        mConnectionId = connectionId;
        mFName = fName;
        mLName = lName;
        mEmail = email;
        mUsername = username;
    }
}
