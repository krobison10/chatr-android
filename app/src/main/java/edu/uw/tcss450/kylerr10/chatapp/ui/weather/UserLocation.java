package edu.uw.tcss450.kylerr10.chatapp.ui.weather;

import java.io.Serializable;

public class UserLocation implements Serializable {
    private final int mPrimaryKey;
    private final String mName;
    private final double mLatitude;
    private final double mLongitude;

    public UserLocation(
            final int mPrimaryKey,
            final String mName,
            final double mLatitude,
            final double mLongitude
    ) {
        this.mPrimaryKey = mPrimaryKey;
        this.mName = mName;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }

    public int getPrimaryKey() {
        return mPrimaryKey;
    }

    public String getName() {
        return mName;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }
}
