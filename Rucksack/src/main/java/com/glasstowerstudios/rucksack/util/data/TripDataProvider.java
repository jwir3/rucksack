package com.glasstowerstudios.rucksack.util.data;

import android.app.Application;
import android.support.annotation.NonNull;

import com.glasstowerstudios.rucksack.model.Trip;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import au.com.gridstone.rxstore.StoreProvider;
import au.com.gridstone.rxstore.converters.GsonConverter;

/**
 * A data provider for instances of the {@link Trip} class so they can be saved/restored from local
 * storage using RxStore.
 */
public class TripDataProvider {
  private static final String TRIP_LIST_STORAGE_KEY = "Trips";

  @Inject Application mApplication;
  @Inject Gson mGson;

  public TripDataProvider(Application app, Gson gson) {
    mApplication = app;
    mGson = gson;
  }

  @NonNull
  private StoreProvider.ListStore<Trip> getListStore() {
    StoreProvider storeProvider = StoreProvider.withContext(mApplication)
                                               .inDir("rucksackData")
                                               .using(new GsonConverter(mGson));
    return storeProvider.listStore(TRIP_LIST_STORAGE_KEY, Trip.class);
  }

  /**
   * @return A {@link List} of all {@link Trip} objects in the database.
   */
  public List<Trip> getAll() {
    StoreProvider.ListStore<Trip> tripStore = getListStore();
    return tripStore.getBlocking();
  }

  public void save(@NonNull Trip aTrip) {
    StoreProvider.ListStore<Trip> tripStore = getListStore();
    tripStore.addToList(aTrip);
  }

  public void delete(@NonNull Trip aTrip) {
    StoreProvider.ListStore<Trip> tripStore = getListStore();
    tripStore.removeFromList(aTrip);
  }
}
