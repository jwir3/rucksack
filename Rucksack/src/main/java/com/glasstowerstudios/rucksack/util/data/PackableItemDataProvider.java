package com.glasstowerstudios.rucksack.util.data;

import android.app.Application;
import android.support.annotation.NonNull;

import com.glasstowerstudios.rucksack.model.PackableItem;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import au.com.gridstone.rxstore.StoreProvider;
import au.com.gridstone.rxstore.converters.GsonConverter;

/**
 * A data provider for instances of the {@link PackableItem} class so they can be saved/restored from local
 * storage using RxStore.
 */
public class PackableItemDataProvider {
  private static final String PACKABLE_ITEM_LIST_STORAGE_KEY = "PackableItems";

  @Inject Application mApplication;
  @Inject Gson mGson;

  public PackableItemDataProvider(Application app, Gson gson) {
    mApplication = app;
    mGson = gson;
  }

  @NonNull
  private StoreProvider.ListStore<PackableItem> getListStore() {
    StoreProvider storeProvider = StoreProvider.withContext(mApplication)
                                               .inDir("rucksackData")
                                               .using(new GsonConverter(mGson));
    return storeProvider.listStore(PACKABLE_ITEM_LIST_STORAGE_KEY, PackableItem.class);
  }

  /**
   * @return A {@link List} of all {@link PackableItem} objects in the database.
   */
  public synchronized List<PackableItem> getAll() {
    StoreProvider.ListStore<PackableItem> packItemStore = getListStore();
    return packItemStore.getBlocking();
  }

  public synchronized void save(@NonNull PackableItem packItem) {
    StoreProvider.ListStore<PackableItem> packItemStore = getListStore();
    packItemStore.addToList(packItem);
  }

  public synchronized void saveAll(@NonNull List<PackableItem> packableItems) {
    StoreProvider.ListStore<PackableItem> packItemStore = getListStore();
    packItemStore.put(packableItems);
  }

  public synchronized void delete(@NonNull PackableItem packItem) {
    StoreProvider.ListStore<PackableItem> packItemStore = getListStore();
    packItemStore.removeFromList(packItem);
  }
}