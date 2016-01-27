package pl.edu.agh.librarian.tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {

    private static SharedPreferences preferences;

    public SettingsManager(Context c) {
        preferences = c.getSharedPreferences("LIBRARIAN_PREF", Activity.MODE_PRIVATE);
    }

    /**
     * Change default launcher of app (ADMIN, USER, LOGIN)
     * @param launch - launcher type
     */
    public static void setAppLauncher(LauncherTypes launch) {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putInt("launcher", launch.ordinal());
        preferencesEditor.apply();
    }

    public static LauncherTypes getAppLauncher() {
        return LauncherTypes.values()[preferences.getInt("launcher", 0)];
    }

    public static void setToken(String token) {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString("token", token);
        preferencesEditor.apply();
    }

    public static String getToken(){
        return preferences.getString("token", "0");
    }

    public static void setUserID(String id) {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString("userID", id);
        preferencesEditor.apply();
    }

    public static String getUserID(){
        return preferences.getString("userID", null);
    }


}
