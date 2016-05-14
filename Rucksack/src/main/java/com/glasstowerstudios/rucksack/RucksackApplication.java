package com.glasstowerstudios.rucksack;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;
import com.glasstowerstudios.rucksack.di.Injector;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Rucksack main entry point. Base {@link Application} that utilizes functionality from
 * activeandroid.
 *
 * Also responsible for initializing the base Dagger components.
 */
public class RucksackApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();

    Fabric.with(this, new Crashlytics());
    ActiveAndroid.initialize(this);
    Injector.INSTANCE.initializeApplicationComponent(this);
  }
}
