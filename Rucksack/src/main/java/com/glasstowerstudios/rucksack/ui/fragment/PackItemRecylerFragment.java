package com.glasstowerstudios.rucksack.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.model.PackItem;
import com.glasstowerstudios.rucksack.ui.activity.BaseActivity;
import com.glasstowerstudios.rucksack.ui.activity.TripsActivity;
import com.glasstowerstudios.rucksack.ui.adapter.PackItemRecyclerAdapter;
import com.glasstowerstudios.rucksack.ui.base.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 */
public class PackItemRecylerFragment
  extends Fragment
  implements SwipeRefreshLayout.OnRefreshListener {

  private static final int REQUEST_CODE = 1234;

  @Bind(R.id.pack_item_recycler_view)
  protected RecyclerView mRecyclerView;

  @Bind(R.id.packitems_swipe_refresh)
  protected SwipeRefreshLayout mSwipeRefreshLayout;

  @Bind(R.id.add_item_voice_input)
  protected ImageButton mSpeakButton;

  @Bind(R.id.add_item_edittext)
  protected EditText mAddItemInput;

  private PackItemRecyclerAdapter mAdapter;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onResume() {
    super.onResume();
    Activity act = getActivity();
    BaseActivity baseAct = (BaseActivity) act;
    baseAct.unlockNavigationDrawer();

    onRefresh();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View createdView = inflater.inflate(R.layout.fragment_packitem_recycler, container, false);
    ButterKnife.bind(this, createdView);

    TripsActivity act = (TripsActivity) getContext();
//    act.enableFloatingActionButton();
    act.disableFloatingActionButton();

    ActionBar appBar = act.getSupportActionBar();
    appBar.setTitle("Packable Items");

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(layoutManager);

    List<PackItem> tempItems = new ArrayList<>();
    tempItems.add(new PackItem("Hello"));
    tempItems.add(new PackItem("World"));

    mAdapter = new PackItemRecyclerAdapter(tempItems);
    mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
    mRecyclerView.setAdapter(mAdapter);

    mSwipeRefreshLayout.setOnRefreshListener(this);

    // Disable button if no recognition service is present
    PackageManager pm = getActivity().getPackageManager();
    List<ResolveInfo> activities = pm.queryIntentActivities(
      new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
    if (activities.size() == 0) {
      mSpeakButton.setEnabled(false);
    } else {
      mSpeakButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          onSpeakButtonClicked(v);
        }
      });
    }

    return createdView;
  }

//  private List<PackItem> getItems() {
//    // Get all items from the database.
//    List<Trip> trips = Trip.getAll();
//    return trips;
//  }

  @Override
  public void onRefresh() {
    mSwipeRefreshLayout.setRefreshing(true);

//    List<Trip> trips = getTrips();
//    mAdapter.setTrips(trips);

    mSwipeRefreshLayout.setRefreshing(false);
  }

  public void onSpeakButtonClicked(View v) {
    startVoiceRecognitionActivity();
  }

  private void startVoiceRecognitionActivity() {
    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak item description");
    startActivityForResult(intent, REQUEST_CODE);
  }

  /**
   * Handle the results from the voice recognition activity.
   */
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      String[] inputData = data.getStringArrayExtra(RecognizerIntent.EXTRA_RESULTS);
      StringBuilder inputBuilder = new StringBuilder();
      inputBuilder.append(inputData);

      mAddItemInput.setText(inputBuilder.toString());
    }

    super.onActivityResult(requestCode, resultCode, data);
  }
}
