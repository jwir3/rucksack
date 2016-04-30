package com.glasstowerstudios.rucksack.di;

import android.support.annotation.VisibleForTesting;

import com.glasstowerstudios.rucksack.RucksackApplication;

/**
 * This is the handler for the lifecycle of the {@link ApplicationComponent} which is used to inject
 * dependencies.
 */
public enum Injector {
  INSTANCE;

  private ApplicationComponent mApplicationComponent;

  Injector() {}

  public ApplicationComponent initializeApplicationComponent(RucksackApplication app) {
    mApplicationComponent = DaggerApplicationComponent.builder()
                                                      .applicationModule(new ApplicationModule(app))
                                                      .gsonModule(new GsonModule())
                                                      .build();
    return mApplicationComponent;
  }

  /**
   * Setting the application component will override a previously constructed component. This is
   * useful for testing when mock dependencies need to be injected.
   */
  @VisibleForTesting
  public void setApplicationComponent(ApplicationComponent component) {
    this.mApplicationComponent = component;
  }

  public ApplicationComponent getApplicationComponent() {
    return mApplicationComponent;
  }
}
