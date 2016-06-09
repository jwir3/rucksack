package com.glasstowerstudios.rucksack.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.di.Injector;
import com.glasstowerstudios.rucksack.model.Pastime;
import com.glasstowerstudios.rucksack.ui.activity.BaseActivity;
import com.glasstowerstudios.rucksack.ui.adapter.PastimeIconSpinnerAdapter;
import com.glasstowerstudios.rucksack.ui.view.PackableItemRecyclerView;
import com.glasstowerstudios.rucksack.util.data.PastimeDataProvider;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * {@link Fragment} that shows the form that allows the creation, editing, or deletion of a
 * {@link Pastime}.
 */
public class PastimeDetailFragment extends Fragment {
  public static final String PASTIME_KEY = "Pastime";
  private static final String LOGTAG = PastimeDetailFragment.class.getSimpleName();

  @Bind(R.id.pastime_name_input) EditText mPastimeNameInput;
  @Bind(R.id.pastime_icon_spinner) Spinner mPastimeIconSpinner;
  @Bind(R.id.pastime_check_items) PackableItemRecyclerView mPackableItemRecyclerView;

  private Pastime mPastime;

  @Inject PastimeDataProvider mPastimeDataProvider;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bundle args = getArguments();

    if (args.containsKey(PASTIME_KEY)) {
      mPastime = args.getParcelable(PASTIME_KEY);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Injector.INSTANCE.getApplicationComponent().inject(this);

    View v = inflater.inflate(R.layout.fragment_add_pastime, container, false);

    ButterKnife.bind(this, v);

    // We don't want a floating action button for this view.
    BaseActivity act = (BaseActivity) getActivity();
    act.disableFloatingActionButton();

    setHasOptionsMenu(true);

    ArrayAdapter <Integer> adapter = new PastimeIconSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item);
    adapter.addAll(Pastime.getAvailablePastimeIconResources(getContext()));
    adapter.setDropDownViewResource(R.layout.pastime_icon_dropdown_item);
    mPastimeIconSpinner.setAdapter(adapter);

    populateFromArguments();

    return v;
  }

  private void populateFromArguments() {
    if (mPastime != null) {
      mPastimeNameInput.setText(mPastime.getName());

      int positionOfIcon = getSpinnerPositionOf(mPastime.getIconResourceId(getContext()));
      if (positionOfIcon != -1) {
        mPastimeIconSpinner.setSelection(positionOfIcon);
      }

      if (mPastime.getPackableItems() != null) {
        mPackableItemRecyclerView.setSelectedItems(mPastime.getPackableItems());
      }
    }
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);

    inflater.inflate(R.menu.submit_with_delete, menu);

    if (mPastime == null) {
      // Don't allow the deletion of a nonexistent pastime.
      MenuItem deleteItem = menu.findItem(R.id.delete);
      deleteItem.setVisible(false);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    BaseActivity act = (BaseActivity) getActivity();

    switch(item.getItemId()) {
      case R.id.confirm:
        if (mPastime != null) {
          // Delete the old pastime first, then.
          mPastimeDataProvider.delete(mPastime);
        }

        Pastime pastime = createPastimeFromInput();
        mPastimeDataProvider.save(pastime);
        act.onBackPressed();
        return true;

      case R.id.delete:
        mPastimeDataProvider.delete(mPastime);
        act.onBackPressed();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private Pastime createPastimeFromInput() {
    String pastimeName = mPastimeNameInput.getText().toString();
    Integer pastimeIconResource = (Integer) mPastimeIconSpinner.getSelectedItem();

    return new Pastime(pastimeName,
                       Pastime.getResourceNameForId(pastimeIconResource, getContext()),
                       mPackableItemRecyclerView.getSelectedItems());
  }

  private int getSpinnerPositionOf(int iconResource) {
    SpinnerAdapter adapter = mPastimeIconSpinner.getAdapter();
    for (int i = 0; i < adapter.getCount(); i++) {
      if (Integer.valueOf(iconResource).equals(adapter.getItem(i))) {
        return i;
      }
    }

    return -1;
  }
}
