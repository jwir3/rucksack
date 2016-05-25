package com.glasstowerstudios.rucksack.di;

import com.glasstowerstudios.rucksack.RucksackApplication;
import com.glasstowerstudios.rucksack.util.data.TripDataProvider;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *
 */
@Module
public class DataProviderModule {
  private TripDataProvider mTripDataProvider;

  public DataProviderModule(RucksackApplication app, Gson gson) {
    mTripDataProvider = new TripDataProvider(app, gson);
  }

  @Provides
  @Singleton
  public TripDataProvider providesTripDataProvider() {
    return mTripDataProvider;
  }
}
