package com.glasstowerstudios.rucksack.di;

import com.glasstowerstudios.rucksack.RucksackApplication;
import com.glasstowerstudios.rucksack.util.data.PackableItemDataProvider;
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
  private PackableItemDataProvider mPackableItemDataProvider;

  public DataProviderModule(RucksackApplication app, Gson gson) {
    mTripDataProvider = new TripDataProvider(app, gson);
    mPastimeDataProvider = new PastimeDataProvider(app, gson);
    mPackableItemDataProvider = new PackableItemDataProvider(app, gson);
  }

  @Provides
  @Singleton
  public TripDataProvider providesTripDataProvider() {
    return mTripDataProvider;
  }

  @Provides
  @Singleton
  public PastimeDataProvider providesPastimeDataProvider() {
    return mPastimeDataProvider;
  }

  @Provides
  @Singleton
  public PackableItemDataProvider providesPackableItemDataProvider() {
    return mPackableItemDataProvider;
  }
}
