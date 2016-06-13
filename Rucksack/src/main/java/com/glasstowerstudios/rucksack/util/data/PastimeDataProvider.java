package com.glasstowerstudios.rucksack.util.data;

import android.app.Application;
import android.support.annotation.NonNull;

import com.glasstowerstudios.rucksack.model.Pastime;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import au.com.gridstone.rxstore.StoreProvider;
import au.com.gridstone.rxstore.converters.GsonConverter;

/**
 * A data provider for instances of the {@link Pastime} class so they can be saved/restored from
 * local storage using RxStore.
 */
public class PastimeDataProvider {
  private static final String PASTIME_LIST_STORAGE_KEY = "Pastimes";

  @Inject Application mApplication;
  @Inject Gson mGson;

  public PastimeDataProvider(Application app, Gson gson) {
    mApplication = app;
    mGson = gson;
  }

  @NonNull
  private StoreProvider.ListStore<Pastime> getListStore() {
    StoreProvider storeProvider = StoreProvider.withContext(mApplication)
                                               .inDir("rucksackData")
                                               .using(new GsonConverter(mGson));
    return storeProvider.listStore(PASTIME_LIST_STORAGE_KEY, Pastime.class);
  }

  public synchronized List<Pastime> getAll() {
    StoreProvider.ListStore<Pastime> pastimeStore = getListStore();
    return pastimeStore.getBlocking();
  }

  public synchronized void save(@NonNull Pastime aPastime) {
    StoreProvider.ListStore<Pastime> pastimeStore = getListStore();
    pastimeStore.addToList(aPastime);
  }

  public synchronized void saveAll(@NonNull List<Pastime> aPastimes) {
    StoreProvider.ListStore<Pastime> pastimeStore = getListStore();
    pastimeStore.put(aPastimes);
  }

  public synchronized void delete(@NonNull Pastime aPastime) {
    StoreProvider.ListStore<Pastime> pastimeStore = getListStore();
    pastimeStore.removeFromList(aPastime);
  }

  public synchronized void update(Pastime pastime) {
    StoreProvider.ListStore<Pastime> pastimeStore = getListStore();
    List<Pastime> pastimes = pastimeStore.getBlocking();
    for (int i = 0; i < pastimes.size(); i++) {
      Pastime nextPastime = pastimes.get(i);
      if (nextPastime.getName().equals(pastime.getName())) {
        pastimes.remove(i);
        pastimes.add(i-1, pastime);
        break;
      }
    }

    pastimeStore.put(pastimes);
  }
}
