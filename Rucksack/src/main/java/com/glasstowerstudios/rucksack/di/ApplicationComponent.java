package com.glasstowerstudios.rucksack.di;

import com.glasstowerstudios.rucksack.model.Pastime;
import com.glasstowerstudios.rucksack.ui.adapter.PackableItemRecyclerAdapter;
import com.glasstowerstudios.rucksack.ui.adapter.PackingListAdapter;
import com.glasstowerstudios.rucksack.ui.adapter.PastimeRecyclerAdapter;
import com.glasstowerstudios.rucksack.ui.adapter.TripRecyclerAdapter;
import com.glasstowerstudios.rucksack.ui.fragment.AddTripDetailsFragment;
import com.glasstowerstudios.rucksack.ui.fragment.PackableItemRecyclerFragment;
import com.glasstowerstudios.rucksack.ui.fragment.PastimeDetailFragment;
import com.glasstowerstudios.rucksack.ui.fragment.TripInteractionFragment;
import com.glasstowerstudios.rucksack.ui.fragment.TripPastimeSelectionFragment;
import com.glasstowerstudios.rucksack.ui.fragment.TripRecyclerFragment;
import com.glasstowerstudios.rucksack.ui.view.PackableItemRecyclerView;
import com.glasstowerstudios.rucksack.ui.view.PackingListView;
import com.glasstowerstudios.rucksack.ui.view.PastimeSelector;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger {@link Component} containing the main application instance and related modules.
 */
@Singleton
@Component(modules={ApplicationModule.class, GsonModule.class, DataProviderModule.class,
                    AppPreferencesModule.class})
public interface ApplicationComponent {
  void inject(TripRecyclerAdapter tripRecyclerAdapter);

  void inject(AddTripDetailsFragment addTripDetailsFragment);

  void inject(TripRecyclerFragment tripRecyclerFragment);

  void inject(PastimeRecyclerAdapter pastimeRecyclerAdapter);

  void inject(PastimeDetailFragment pastimeDetailFragment);

  void inject(PackableItemRecyclerAdapter packableItemRecyclerAdapter);

  void inject(PackableItemRecyclerFragment packableItemRecyclerFragment);

  void inject(PackableItemRecyclerView packableItemRecyclerView);

  void inject(PastimeSelector pastimeSelector);

  void inject(TripPastimeSelectionFragment tripPastimeSelectionFragment);

  void inject(Pastime pastime);

  void inject(PackingListAdapter packingListAdapter);

  void inject(PackingListView packingListView);

  void inject(TripInteractionFragment tripInteractionFragment);
}
