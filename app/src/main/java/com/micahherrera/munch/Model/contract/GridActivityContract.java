package com.micahherrera.munch.Model.contract;

import android.os.Bundle;

/**
 * Created by micahherrera on 11/11/16.
 */

public interface GridActivityContract {
    void getNearby();
    void unregister();
    boolean checkConnection();
    void checkPermissions();
    void onPermissionGranted();
    void setupYelp();
    void oauthYelp();
    void goToSettings();
    void newSettings(Bundle bundle);
}
