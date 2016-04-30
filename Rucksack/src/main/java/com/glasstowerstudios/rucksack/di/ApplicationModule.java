package com.glasstowerstudios.rucksack.di;

import com.glasstowerstudios.rucksack.RucksackApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *
 */
@Module
public class ApplicationModule {
  RucksackApplication mApp;

  public ApplicationModule(RucksackApplication app) {
    this.mApp = app;
  }

  @Provides
  @Singleton
  public RucksackApplication providesApp() {
    return mApp;
  }
}
