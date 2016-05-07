package com.glasstowerstudios.rucksack.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.model.PackItem;
import com.glasstowerstudios.rucksack.ui.fragment.PackItemRecyclerFragment;

import butterknife.ButterKnife;

/**
 * A top-level menu {@link Activity} that will show fragments having operations related to
 * {@link PackItem}s.
 */
public class PackableItemsActivity extends BaseActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ButterKnife.bind(this);

    showRootFragment(PackItemRecyclerFragment.class, null);
  }

  @Override
  public int getFragmentContainerID() {
    return R.id.fragment_container_root;
  }

  @Override
  public void onFloatingActionButtonClick(View view) {
     // Do nothing right now, since we're not implementing a FAB on this view.
  }

  @Override
  protected void setupActivityTitle() {
    setTitle(getString(R.string.packable_items));
  }

  @Override
  protected int getNavItemId() {
    return R.id.nav_items;
  }
}
