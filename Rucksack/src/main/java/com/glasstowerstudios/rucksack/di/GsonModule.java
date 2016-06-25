package com.glasstowerstudios.rucksack.di;

import com.glasstowerstudios.rucksack.util.RucksackGsonHelper;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger {@link Module} providing dependencies for injection of Gson.
 */
@Module
public class GsonModule {
  Gson mGson;

  public GsonModule() {
    mGson = RucksackGsonHelper.getGson();
  }

  @Provides
  @Singleton
  public Gson providesGson() {
    return mGson;
  }
}
