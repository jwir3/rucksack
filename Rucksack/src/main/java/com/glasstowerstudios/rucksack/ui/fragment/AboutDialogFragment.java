package com.glasstowerstudios.rucksack.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.glasstowerstudios.rucksack.BuildConfig;
import com.glasstowerstudios.rucksack.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A {@link DialogFragment} displaying the "About" information for the Rucksack application.
 */
public class AboutDialogFragment extends DialogFragment {
  @Bind(R.id.releaseNotesButton) Button releaseNotesButton;
  @Bind(R.id.bugButton) Button bugButton;
  @Bind(R.id.rateButton) Button rateButton;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    // Get the layout inflater
    LayoutInflater inflater = getActivity().getLayoutInflater();

    // Inflate and set the layout for the dialog
    // Pass null as the parent view because its going in the dialog layout
    View v = inflater.inflate(R.layout.dialog_about, null);

    ButterKnife.bind(this, v);

    TextView versionView = (TextView) v.findViewById(R.id.rucksackVersion);
    String textualVersion = getString(R.string.app_version, BuildConfig.VERSION_NAME);
    versionView.setText(textualVersion);
    builder.setView(v);

    return builder.create();
  }

  @OnClick(R.id.rateButton)
  void rateOnGooglePlay() {
    Toast.makeText(getContext(), R.string.cannot_rate_yet, Toast.LENGTH_LONG).show();
  }

  @OnClick(R.id.bugButton)
  void submitBugReport() {
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.url_new_bug)));
    startActivity(browserIntent);
  }

  @OnClick(R.id.releaseNotesButton)
  void viewReleaseNotes() {
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.url_release_notes)));
    startActivity(browserIntent);
  }
}
