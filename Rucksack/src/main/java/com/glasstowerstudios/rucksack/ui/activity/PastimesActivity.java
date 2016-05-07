package com.glasstowerstudios.rucksack.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.model.Pastime;
import com.glasstowerstudios.rucksack.ui.fragment.AddPastimeFragment;
import com.glasstowerstudios.rucksack.ui.fragment.PastimeRecyclerFragment;

import butterknife.ButterKnife;

/**
 * A top-level menu {@link Activity} that will show fragments having operations related to
 * {@link Pastime}s.
 */
public class PastimesActivity extends BaseActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ButterKnife.bind(this);

    showRootFragment(PastimeRecyclerFragment.class, null);
  }

  @Override
  public int getFragmentContainerID() {
    return R.id.fragment_container_root;
  }

  @Override
  public void onFloatingActionButtonClick(View view) {
    showNonRootFragment(AddPastimeFragment.class, new Bundle());
  }

  @Override
  protected void setupActivityTitle() {
    setTitle(getString(R.string.pastimes));
  }

  @Override
  protected int getNavItemId() {
    return R.id.nav_pastimes;
  }
}
