package com.glasstowerstudios.rucksack.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.glasstowerstudios.rucksack.R;

import java.util.List;

/**
 * An adapter for a {@link Spinner} that allows us to display just icons.
 */
public class PastimeIconSpinnerAdapter extends ArrayAdapter<Integer> {

  public PastimeIconSpinnerAdapter(Context context, int resource) {
    super(context, resource);
  }

  public PastimeIconSpinnerAdapter(Context context, int resource, int textViewResourceId) {
    super(context, resource, textViewResourceId);
  }

  public PastimeIconSpinnerAdapter(Context context, int resource, Integer[] objects) {
    super(context, resource, objects);
  }

  public PastimeIconSpinnerAdapter(Context context, int resource, int textViewResourceId, Integer[] objects) {
    super(context, resource, textViewResourceId, objects);
  }

  public PastimeIconSpinnerAdapter(Context context, int resource, List<Integer> objects) {
    super(context, resource, objects);
  }

  public PastimeIconSpinnerAdapter(Context context, int resource, int textViewResourceId, List<Integer> objects) {
    super(context, resource, textViewResourceId, objects);
  }

  @Override
  public View getDropDownView(int position, View convertView,ViewGroup parent) {
    return getView(position, convertView, parent);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater =
      (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View row = inflater.inflate(R.layout.pastime_icon_dropdown_item, parent, false);

    ImageView icon = (ImageView) row.findViewById(R.id.dropdown_image1);
    icon.setImageResource(getItem(position));

    return row;
  }
}
