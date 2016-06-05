package com.glasstowerstudios.rucksack.ui.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.di.Injector;
import com.glasstowerstudios.rucksack.ui.activity.BaseActivity;
import com.glasstowerstudios.rucksack.ui.activity.TripsActivity;
import com.glasstowerstudios.rucksack.util.TemporalFormatter;
import com.glasstowerstudios.rucksack.util.data.TripDataProvider;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A {@link Fragment} displayed when the user decides to add a new trip to the system. This will
 * prompt the user for the basic information about the trip and allow the user to navigate to the
 * {@link TripPastimeSelectionFragment}.
 */
public class AddTripDetailsFragment
  extends Fragment
  implements DatePickerDialog.OnDateSetListener {

  private static final String LOGTAG = AddTripDetailsFragment.class.getSimpleName();

  private boolean mStartCalendarChooserOpen = false;
  private DateTime mChosenStartDate;

  @Bind(R.id.destinationInput) protected EditText mDestinationInput;
  @Bind(R.id.startDateInput) protected EditText mStartDateInput;
  @Bind(R.id.nights_seek_bar) protected DiscreteSeekBar mNightsSeekBar;
  @Bind(R.id.next_select_pastime_button) Button mNextSelectPastimesButton;

  @Inject TripDataProvider mTripDataProvider;

  public AddTripDetailsFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Injector.INSTANCE.getApplicationComponent().inject(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    TripsActivity act = (TripsActivity) getActivity();
    act.disableFloatingActionButton();

    ActionBar appBar = act.getSupportActionBar();
    if (appBar != null) {
      appBar.setDisplayHomeAsUpEnabled(true);
      appBar.setDisplayShowHomeEnabled(false);
      appBar.setTitle(R.string.addTrip);
    }


    setHasOptionsMenu(true);

    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_add_trip, container, false);
    ButterKnife.bind(this, v);

    mStartDateInput.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        ((TripsActivity) getActivity()).dismissKeyboardIfOpen();
        if (!mStartCalendarChooserOpen) {
          if (mChosenStartDate != null) {
            openCalendarChooser(mStartDateInput, mChosenStartDate);
          } else {
            openCalendarChooser(mStartDateInput, DateTime.now());
          }
        }
        return true;
      }
    });

    mNextSelectPastimesButton.setOnClickListener(v1 -> {
      Bundle args = new Bundle();
      String destination = mDestinationInput.getText().toString();
      args.putString(TripPastimeSelectionFragment.TRIP_DESTINATION, destination);
      args.putString(TripPastimeSelectionFragment.TRIP_START_DATE, getStartDateAsString());
      args.putInt(TripPastimeSelectionFragment.TRIP_NUM_NIGHTS, mNightsSeekBar.getProgress());

      BaseActivity tripsActivity = (BaseActivity) getActivity();
      tripsActivity.showFragment(TripPastimeSelectionFragment.class, args, true);
    });

    return v;
  }

  @Override
  public void onResume() {
    super.onResume();
    Activity act = getActivity();
    BaseActivity baseAct = (BaseActivity) act;
    baseAct.lockNavigationDrawer();
  }

  /**
   * Open the calendar chooser {@link DatePickerDialog} for a specific {@link View}.
   *
   * @param tappedView The {@link View} that was tapped to display the calendar chooser. Must be
   *                   either the startDateInput or endDateInput.
   * @param dateTime The {@link DateTime} to start the calendar chooser out with.
   */
  public void openCalendarChooser(View tappedView, DateTime dateTime) {
    DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this, dateTime.getYear(),
                                                       dateTime.getMonthOfYear() - 1,
                                                       dateTime.getDayOfMonth());
    if (tappedView.getId() == R.id.startDateInput) {
      mStartCalendarChooserOpen = true;
    }

    datePicker.show();
  }

  @Override
  public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    DateTime chosenDateTime = new DateTime(year, monthOfYear+1, dayOfMonth, 0, 0, 0, 0,
                                           DateTimeZone.getDefault());
    String dateTimeFormatted = TemporalFormatter.TRIP_DATES_FORMATTER.print(chosenDateTime);
    if (mStartCalendarChooserOpen) {
      mStartDateInput.setText(dateTimeFormatted);
      mChosenStartDate = chosenDateTime;
      mStartCalendarChooserOpen = false;
    }
  }

  private String getStartDateAsString() {
    return TemporalFormatter.TRIP_DATES_FORMATTER.print(mChosenStartDate);
  }
}
