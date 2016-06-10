package com.glasstowerstudios.rucksack;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.glasstowerstudios.rucksack.di.Injector;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

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
    Injector.INSTANCE.initializeApplicationComponent(this);

    CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                                    .setFontAttrId(R.attr.fontPath)
                                    .build());
  }
}
