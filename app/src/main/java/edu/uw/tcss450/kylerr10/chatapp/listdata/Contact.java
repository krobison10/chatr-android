package edu.uw.tcss450.kylerr10.chatapp.listdata;

import java.io.Serializable;

/**
 * Stores the pertinent data for a current contact to be used by the CurrentContactsRecyclerViewAdapter.
 *
 * @author Kyler Robison
 */
public class Contact implements Serializable {
    //TODO: Encapsulate
    /**
     * Id of the connection (will be 0 for users in search)
     */
    public final int mConnectionId;
    /**
     * First name of the user.
     */
    public final String mFName;
    /**
     * Last name of the user.
     */
    public final String mLName;
    /**
     * Email of the user.
     */
    public final String mEmail;
    /**
     * Username of the user.
     */
    public final String mUsername;

    /**
     * Creates a new contact object.
     * @param connectionId Id of the connection (will be 0 for users in search)
     * @param username Username of the user.
     * @param email Email of the user.
     * @param fName First name of the user.
     * @param lName Last name of the user.
     */
    public Contact(int connectionId, String username, String email, String fName, String lName) {
        mConnectionId = connectionId;
        mFName = fName;
        mLName = lName;
        mEmail = email;
        mUsername = username;
    }
}
