package com.glasstowerstudios.rucksack.util.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.glasstowerstudios.rucksack.BuildConfig;

/**
 * A wrapper for {@link SharedPreferences} that allows the retrieval of preferences specific to
 * Rucksack.
 */
public class AppPreferences {
  private static final String RUCKSACK_PREFERENCES_FILE = "Rucksack.Preferences";

  public String LAST_APP_VERSION_PREF = "Rucksack.LastVersion";

  private SharedPreferences mPreferences;

  public AppPreferences(Context aContext) {

    mPreferences = aContext.getSharedPreferences(RUCKSACK_PREFERENCES_FILE, Context.MODE_PRIVATE);
  }

  public boolean isFirstRun() {
    return getLastAppVersion().isEmpty();
  }

  public String getLastAppVersion() {
    if (mPreferences.contains(LAST_APP_VERSION_PREF)) {
      return mPreferences.getString(LAST_APP_VERSION_PREF, BuildConfig.VERSION_NAME);
    }

    return "";
  }

  public void setLastAppVersion() {
    if (!getLastAppVersion().equals(BuildConfig.VERSION_NAME)) {
      mPreferences.edit().putString(LAST_APP_VERSION_PREF, BuildConfig.VERSION_NAME).apply();
    }
  }
}
