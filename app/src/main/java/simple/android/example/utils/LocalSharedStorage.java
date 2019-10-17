package simple.android.example.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LocalSharedStorage {
    SharedPreferences preferences;
    private static final String PREF_USER_EMAIL = "PREF_USER_EMAIL";
    private static final String PREF_HAJJ_ID = "PREF_HAJJ_ID";
    private static final String SAVE_USER_ID = "SAVE_USER_ID";
    private static final String SAVE_TOKEN = "SAVE_TOKEN";
    private static final String SAVE_ROLE = "SAVE_ROLE";

    public static SharedPreferences mInstance;

    public LocalSharedStorage(Context ctx) {

        try{
            preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void saveUserEmail(String name) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_USER_EMAIL, name);
        editor.commit();
    }



    public String getUserEmail() {
        String code = preferences.getString(PREF_USER_EMAIL, "");
        return code;
    }
    public void saveHajjId(String name) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_HAJJ_ID, name);
        editor.commit();
    }
    public String getHajjId() {
        String code = preferences.getString(PREF_HAJJ_ID, "");
        return code;
    }

    public void setSaveUserID(String name) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SAVE_USER_ID, name);
        editor.commit();
    }
    public String getUserID() {
        String code = preferences.getString(SAVE_USER_ID, "");
        return code;
    }

    public void setSaveToken(String name) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SAVE_TOKEN, name);
        editor.commit();
    }
    public String getSaveToken() {
        String code = preferences.getString(SAVE_TOKEN, "");
        return code;
    }

    public void saveRole(String name) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SAVE_ROLE, name);
        editor.commit();
    }
    public String getRole() {
        String code = preferences.getString(SAVE_ROLE, "");
        return code;
    }
}

