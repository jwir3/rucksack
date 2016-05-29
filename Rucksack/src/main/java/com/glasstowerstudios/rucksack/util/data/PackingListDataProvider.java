package com.glasstowerstudios.rucksack.util.data;

import android.app.Application;
import android.support.annotation.NonNull;

import com.glasstowerstudios.rucksack.model.PackingList;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import au.com.gridstone.rxstore.StoreProvider;
import au.com.gridstone.rxstore.converters.GsonConverter;

/**
 * A data provider for instances of the {@link PackingList} class so they can be saved/restored from
 * local storage using RxStore.
 */
public class PackingListDataProvider {
  private static final String PACKING_LIST_LIST_STORAGE_KEY = "PackingLists";

  @Inject Application mApplication;
  @Inject Gson mGson;

  public PackingListDataProvider(Application app, Gson gson) {
    mApplication = app;
    mGson = gson;
  }

  @NonNull
  private StoreProvider.ListStore<PackingList> getListStore() {
    StoreProvider storeProvider = StoreProvider.withContext(mApplication)
                                               .inDir("rucksackData")
                                               .using(new GsonConverter(mGson));
    return storeProvider.listStore(PACKING_LIST_LIST_STORAGE_KEY, PackingList.class);
  }

  public List<PackingList> getAll() {
    StoreProvider.ListStore<PackingList> packingListStore = getListStore();
    return packingListStore.getBlocking();
  }

  public void save(@NonNull PackingList packingList) {
    StoreProvider.ListStore<PackingList> packingListStore = getListStore();
    packingListStore.addToList(packingList);
  }

  public void saveAll(@NonNull List<PackingList> packingLists) {
    StoreProvider.ListStore<PackingList> packingListStore = getListStore();
    packingListStore.put(packingLists);
  }

  public void delete(@NonNull PackingList packingList) {
    StoreProvider.ListStore<PackingList> packingListStore = getListStore();
    packingListStore.removeFromList(packingList);
  }
}
