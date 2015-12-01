package com.glasstowerstudios.rucksack.ui.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.model.Trip;
import com.glasstowerstudios.rucksack.ui.activity.BaseActivity;
import com.glasstowerstudios.rucksack.ui.activity.TripsActivity;
import com.glasstowerstudios.rucksack.util.TemporalFormatter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A {@link Fragment} displayed when the user decides to add a new trip to the system. This will
 * prompt the user for the basic information about the trip and place the new trip in the
 * {@link TripRecyclerFragment}.
 */
public class AddTripFragment
  extends Fragment
  implements DatePickerDialog.OnDateSetListener {

  private static final String LOGTAG = AddTripFragment.class.getSimpleName();

  private boolean mStartCalendarChooserOpen = false;
  private boolean mEndCalendarChooserOpen = false;
  private DateTime mChosenStartDate;
  private DateTime mChosenEndDate;

  @Bind(R.id.destinationInput) protected EditText mDestinationInput;
  @Bind(R.id.startDateInput) protected EditText mStartDateInput;
  @Bind(R.id.endDateInput) protected EditText mEndDateInput;

  public AddTripFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    TripsActivity act = (TripsActivity) getActivity();
    act.disableFloatingActionButton();

    ActionBar appBar = act.getSupportActionBar();
    appBar.setDisplayHomeAsUpEnabled(true);
    appBar.setDisplayShowHomeEnabled(false);

    appBar.setTitle(R.string.addTrip);

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

    mEndDateInput.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        ((TripsActivity) getActivity()).dismissKeyboardIfOpen();
        if (!mEndCalendarChooserOpen) {
          if (mChosenEndDate != null) {
            openCalendarChooser(mEndDateInput, mChosenEndDate);
          } else if (mChosenStartDate != null) {
            openCalendarChooser(mEndDateInput, mChosenStartDate);
          } else {
            openCalendarChooser(mEndDateInput, DateTime.now());
          }
        }
        return true;
      }
    });

    return v;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);

    inflater.inflate(R.menu.add_trip, menu);
  }

  @Override
  public void onResume() {
    super.onResume();
    Activity act = getActivity();
    BaseActivity baseAct = (BaseActivity) act;
    baseAct.lockNavigationDrawer();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {
      case R.id.confirm:
        Trip trip = createTripFromInput();
        trip.save();
        TripsActivity act = (TripsActivity) getActivity();
        act.onBackPressed();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private Trip createTripFromInput() {
    String destName = mDestinationInput.getText().toString();
    return new Trip(destName, mChosenStartDate, mChosenEndDate);
  }

  /**
   * Open the calendar chooser {@link DatePickerDialog} for a specific {@link View}.
   *
   * @param tappedView The {@link View} that was tapped to display the calendar chooser. Must be
   *                   either the startDateInput or endDateInput.
   * @param dateTime The {@link DateTime} to start the calender chooser out with.
   */
  public void openCalendarChooser(View tappedView, DateTime dateTime) {
    DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this, dateTime.getYear(),
                                                       dateTime.getMonthOfYear() - 1,
                                                       dateTime.getDayOfMonth());
    if (tappedView.getId() == R.id.startDateInput) {
      mStartCalendarChooserOpen = true;
    } else {
      mEndCalendarChooserOpen = true;
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
    } else {
      mEndDateInput.setText(dateTimeFormatted);
      mChosenEndDate = chosenDateTime;
      mEndCalendarChooserOpen = false;
    }
  }
}
