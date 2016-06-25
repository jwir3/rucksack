package com.glasstowerstudios.rucksack.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.glasstowerstudios.rucksack.util.prefs.AppPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provides access to {@link SharedPreferences} as part of Dagger injection.
 */
@Module
public class AppPreferencesModule {
  AppPreferences mPreferences;

  public AppPreferencesModule(Context aContext) {
    mPreferences = new AppPreferences(aContext);
  }

  @Provides
  @Singleton
  public AppPreferences providesAppPreferences() {
    return mPreferences;
  }
}
