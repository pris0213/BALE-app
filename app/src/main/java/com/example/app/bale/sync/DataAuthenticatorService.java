package com.example.app.bale.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by 13108306 on 18/10/2016.
 */
public class DataAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private DataAuthenticator mAuthenticator;
    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new DataAuthenticator(this);
    }
    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}