package com.glasstowerstudios.rucksack.di;

import com.glasstowerstudios.rucksack.ui.adapter.PastimeRecyclerAdapter;
import com.glasstowerstudios.rucksack.ui.adapter.TripRecyclerAdapter;
import com.glasstowerstudios.rucksack.ui.fragment.AddPastimeFragment;
import com.glasstowerstudios.rucksack.ui.fragment.AddTripFragment;
import com.glasstowerstudios.rucksack.ui.fragment.PastimeRecyclerFragment;
import com.glasstowerstudios.rucksack.ui.fragment.TripRecyclerFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger {@link Component} containing the main application instance and related modules.
 */
@Singleton
@Component(modules={ApplicationModule.class, GsonModule.class, DataProviderModule.class})
public interface ApplicationComponent {
  void inject(PastimeRecyclerFragment fragment);

  void inject(TripRecyclerAdapter tripRecyclerAdapter);

  void inject(AddTripFragment addTripFragment);

  void inject(TripRecyclerFragment tripRecyclerFragment);

  void inject(PastimeRecyclerAdapter pastimeRecyclerAdapter);

  void inject(AddPastimeFragment addPastimeFragment);
}
