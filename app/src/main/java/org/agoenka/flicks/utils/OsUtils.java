package org.agoenka.flicks.utils;

import android.content.Context;
import android.content.res.Configuration;

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

}
