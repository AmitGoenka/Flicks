package org.agoenka.flicks.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Author: agoenka
 * Created At: 10/15/2016
 * Version: ${VERSION}
 */

@SuppressWarnings("unused")
public final class OsUtils {

    private OsUtils() {}

    public static boolean isLandscape (Context context) {
        return getOrientation(context) == Configuration.ORIENTATION_LANDSCAPE;
    }

    private static int getOrientation (Context context) {
        return context.getResources().getConfiguration().orientation;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

}