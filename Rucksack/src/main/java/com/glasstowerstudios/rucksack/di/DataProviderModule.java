package com.glasstowerstudios.rucksack.di;

import com.glasstowerstudios.rucksack.RucksackApplication;
import com.glasstowerstudios.rucksack.util.data.PastimeDataProvider;
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
  private PastimeDataProvider mPastimeDataProvider;

  public DataProviderModule(RucksackApplication app, Gson gson) {
    mTripDataProvider = new TripDataProvider(app, gson);
    mPastimeDataProvider = new PastimeDataProvider(app, gson);
  }

  @Provides
  @Singleton
  public TripDataProvider providesTripDataProvider() {
    return mTripDataProvider;
  }

  @Provides
  @Singleton
  public PastimeDataProvider providerPastimeDataProvider() {
    return mPastimeDataProvider;
  }
}
