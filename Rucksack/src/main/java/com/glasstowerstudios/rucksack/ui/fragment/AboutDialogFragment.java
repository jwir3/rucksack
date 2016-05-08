package com.glasstowerstudios.rucksack.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.glasstowerstudios.rucksack.BuildConfig;
import com.glasstowerstudios.rucksack.R;

/**
 * A {@link DialogFragment} displaying the "About" information for the Rucksack application.
 */
public class AboutDialogFragment extends DialogFragment {
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    // Get the layout inflater
    LayoutInflater inflater = getActivity().getLayoutInflater();

    // Inflate and set the layout for the dialog
    // Pass null as the parent view because its going in the dialog layout
    View v = inflater.inflate(R.layout.dialog_about, null);
    TextView versionView = (TextView) v.findViewById(R.id.rucksackVersion);
    String textualVersion = getString(R.string.app_version, BuildConfig.VERSION_NAME);
    versionView.setText(textualVersion);
    builder.setView(v);

    return builder.create();
  }
}
