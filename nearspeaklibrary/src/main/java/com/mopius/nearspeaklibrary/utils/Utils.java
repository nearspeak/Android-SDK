package com.mopius.nearspeaklibrary.utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by Appaya on 02.07.2015.
 */
public class Utils {

    /**
     * this methode checks if a specific service (e.g. the beacon scanning service) is running or not
     * @param context
     * @param serviceClass
     * @return
     */
    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
