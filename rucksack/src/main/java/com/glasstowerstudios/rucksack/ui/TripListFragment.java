package com.glasstowerstudios.rucksack.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glasstowerstudios.rucksack.R;

/**
 *
 */
public class TripListFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View createdView = inflater.inflate(R.layout.fragment_trip_list, container, false);
    return createdView;
  }
}
