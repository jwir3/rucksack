package com.glasstowerstudios.rucksack;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;

/**
 *
 */
public class RucksackApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    ActiveAndroid.initialize(this);
  }
}
