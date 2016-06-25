package com.glasstowerstudios.rucksack.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.di.Injector;
import com.glasstowerstudios.rucksack.util.prefs.AppPreferences;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A popup dialog that tells the user they must adhere to the license agreement.
 */
public class LicenseAgreementDialogFragment extends DialogFragment {
  @Inject AppPreferences prefs;

  @Bind(R.id.licenseDescription) TextView licenseDescription;
  @Bind(R.id.agree_button) Button agreeButton;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    // Get the layout inflater
    LayoutInflater inflater = getActivity().getLayoutInflater();

    // Inflate and set the layout for the dialog
    // Pass null as the parent view because its going in the dialog layout
    View v = inflater.inflate(R.layout.dialog_license, null);

    ButterKnife.bind(this, v);
    Injector.INSTANCE.getApplicationComponent().inject(this);

    builder.setView(v);

//    licenseDescription.setLinksClickable(true);
    licenseDescription.setMovementMethod(LinkMovementMethod.getInstance());
    agreeButton.setOnClickListener(clickedView -> {
      prefs.setAgreedToLicense(true);
      dismiss();
    });

    return builder.create();
  }
}
