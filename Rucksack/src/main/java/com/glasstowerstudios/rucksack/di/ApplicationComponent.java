package com.glasstowerstudios.rucksack.di;

import com.glasstowerstudios.rucksack.ui.fragment.PastimeRecyclerFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger {@link Component} containing the main application instance and related modules.
 */
@Singleton
@Component(modules={ApplicationModule.class, GsonModule.class})
public interface ApplicationComponent {
  void inject(PastimeRecyclerFragment fragment);
}
