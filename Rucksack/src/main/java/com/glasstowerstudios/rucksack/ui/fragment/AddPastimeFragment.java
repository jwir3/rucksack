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

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.di.Injector;
import com.glasstowerstudios.rucksack.model.Pastime;
import com.glasstowerstudios.rucksack.ui.activity.BaseActivity;
import com.glasstowerstudios.rucksack.ui.adapter.PastimeIconSpinnerAdapter;
import com.glasstowerstudios.rucksack.util.data.PastimeDataProvider;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * {@link Fragment} that shows the form to add a new {@link Pastime} object to the database.
 */
public class AddPastimeFragment extends Fragment {
  @Bind(R.id.pastime_name_input) EditText mPastimeNameInput;
  @Bind(R.id.pastime_icon_spinner) Spinner mPastimeIconSpinner;

  @Inject PastimeDataProvider mPastimeDataProvider;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Injector.INSTANCE.getApplicationComponent().inject(this);
    View v = inflater.inflate(R.layout.fragment_add_pastime, container, false);

    ButterKnife.bind(this, v);

    // We don't want a floating action button for this view.
    BaseActivity act = (BaseActivity) getActivity();
    act.disableFloatingActionButton();

    setHasOptionsMenu(true);

    ArrayAdapter < Integer > adapter = new PastimeIconSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item);
    adapter.addAll(Pastime.getAvailablePastimeIconResources(getContext()));
    adapter.setDropDownViewResource(R.layout.pastime_icon_dropdown_item);
    mPastimeIconSpinner.setAdapter(adapter);
    return v;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);

    inflater.inflate(R.menu.submit_action_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {
      case R.id.confirm:
        Pastime pastime = createPastimeFromInput();
        mPastimeDataProvider.save(pastime);
        BaseActivity act = (BaseActivity) getActivity();
        act.onBackPressed();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private Pastime createPastimeFromInput() {
    String pastimeName = mPastimeNameInput.getText().toString();
    Integer pastimeIconResource = (Integer) mPastimeIconSpinner.getSelectedItem();

    return new Pastime(pastimeName,
                       Pastime.getResourceNameForId(pastimeIconResource, getContext()));
  }

}
