package com.buzzware.iridedriver.LocationServices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.preference.PreferenceManager;

import com.buzzware.iridedriver.R;

import java.text.DateFormat;
import java.util.Date;

public class ServiceUtils {

    private static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_location_updates";

    public static final String ACTION_STOP_SERVICE = "stop_location_update";

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The {@link Context}.
     */
    public static boolean requestingLocationUpdates(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context)

                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);

    }

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    public static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();

    }

    /**
     * Returns the {@code location} object as a human readable string.
     * @param location  The {@link Location}.
     */
    public static String getLocationText(Location location) {

        return location == null ? "Unknown location" :

                "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }

    @SuppressLint("StringFormatInvalid")
    static String getLocationTitle(Context context) {

        return context.getString(R.string.location_updated,

                DateFormat.getDateTimeInstance().format(new Date()));

    }
}