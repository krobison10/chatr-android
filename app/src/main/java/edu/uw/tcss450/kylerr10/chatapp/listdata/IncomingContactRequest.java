package edu.uw.tcss450.kylerr10.chatapp.listdata;

import java.io.Serializable;

/**
 * Stores the pertinent data for an incoming contact request to be used by the
 * IncomingRequestsRecyclerViewAdapter.
 *
 * @author Kyler Robison
 */
public class IncomingContactRequest implements Serializable {
    public final int mConnectionId;
    public final String mFName;
    public final String mLName;
    public final String mEmail;
    public final String mUsername;

    public IncomingContactRequest(int connectionId, String username, String email, String fName, String lName) {
        mConnectionId = connectionId;
        mFName = fName;
        mLName = lName;
        mEmail = email;
        mUsername = username;
    }
}
