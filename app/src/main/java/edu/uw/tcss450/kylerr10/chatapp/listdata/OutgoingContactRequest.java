package edu.uw.tcss450.kylerr10.chatapp.listdata;

import java.io.Serializable;

/**
 * Stores the pertinent data for an outgoing contact request to be used by the
 * OutgoingRequestsRecyclerViewAdapter.
 *
 * @author Kyler Robison
 */
public class OutgoingContactRequest implements Serializable {
    public final int mConnectionId;
    public final String mFName;
    public final String mLName;
    public final String mEmail;
    public final String mUsername;

    public OutgoingContactRequest(int connectionId, String username, String email, String fName, String lName) {
        mConnectionId = connectionId;
        mFName = fName;
        mLName = lName;
        mEmail = email;
        mUsername = username;
    }
}
