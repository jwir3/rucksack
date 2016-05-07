package com.glasstowerstudios.rucksack.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.model.Pastime;
import com.glasstowerstudios.rucksack.ui.adapter.PastimeIconSpinnerAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

/**
 * {@link Fragment} that shows the form to add a new {@link Pastime} object to the database.
 */
public class AddPastimeFragment extends Fragment {
  @Bind(R.id.pastime_icon_spinner) Spinner mPastimeIconSpinner;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_add_pastime, container, false);

    ButterKnife.bind(this, v);

    ArrayAdapter<Integer> adapter = new PastimeIconSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item);
    adapter.addAll(getAvailablePastimeIconResources());
    adapter.setDropDownViewResource(R.layout.pastime_icon_dropdown_item);
    mPastimeIconSpinner.setAdapter(adapter);
    return v;
  }

  public List<Integer> getAvailablePastimeIconResources() {
    String pastimePrefix = getResources().getString(R.string.pastime_icon_prefix);

    ArrayList<String> allDrawableResourceNames = new ArrayList<>();
    for (Field field : R.drawable.class.getDeclaredFields()) {
      allDrawableResourceNames.add(field.getName());
    }

    List<Integer> resourceIdentifiers = Observable.from(allDrawableResourceNames)
                                                  .filter(name -> name.contains(pastimePrefix))
                                                  .map(s -> getResources()
                                                    .getIdentifier(s, "drawable",
                                                                   getContext().getPackageName()))
                                                  .toList()
                                                  .toBlocking()
                                                  .first();

    return resourceIdentifiers;
  }
}
