package com.glasstowerstudios.rucksack.di;

import com.glasstowerstudios.rucksack.RucksackApplication;
import com.glasstowerstudios.rucksack.model.Pastime;
import com.glasstowerstudios.rucksack.util.data.PackableItemDataProvider;
import com.glasstowerstudios.rucksack.util.data.PastimeDataProvider;
import com.glasstowerstudios.rucksack.util.data.TripDataProvider;
import com.glasstowerstudios.rucksack.util.prefs.AppPreferences;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * A Dagger module that provides all the necessary accessors of backend data.
 */
@Module
public class DataProviderModule {
  private TripDataProvider mTripDataProvider;
  private PastimeDataProvider mPastimeDataProvider;
  private PackableItemDataProvider mPackableItemDataProvider;
  private AppPreferences mAppPreferences;

  public DataProviderModule(RucksackApplication app, Gson gson, AppPreferences appPreferences) {
    mTripDataProvider = new TripDataProvider(app, gson);
    mPastimeDataProvider = new PastimeDataProvider(app, gson);
    mPackableItemDataProvider = new PackableItemDataProvider(app, gson);
    mAppPreferences = appPreferences;

    populateInitialData();
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

  private void populateInitialData() {
    populatePastimes();
  }

  private void populatePastimes() {
    List<Pastime> allPastimes = mPastimeDataProvider.getAll();

    if (allPastimes.isEmpty() && mAppPreferences.isFirstRun()) {
      Pastime work = new Pastime("Work", "ic_pastime_work", new ArrayList<>());
      Pastime diving = new Pastime("Diving", "ic_pastime_diving", new ArrayList<>());
      Pastime dining = new Pastime("Dining", "ic_pastime_dining", new ArrayList<>());
      Pastime athletics = new Pastime("Athletics", "ic_pastime_athletics", new ArrayList<>());

      List<Pastime> pastimes = new ArrayList<>();
      pastimes.add(work);
      pastimes.add(diving);
      pastimes.add(dining);
      pastimes.add(athletics);
      mPastimeDataProvider.saveAll(pastimes);
    }

    mAppPreferences.setLastAppVersion();
  }
}
