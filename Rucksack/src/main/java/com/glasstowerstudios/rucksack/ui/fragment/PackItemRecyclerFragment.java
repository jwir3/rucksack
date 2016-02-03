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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.model.BaseModel;
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
public class PackItemRecyclerFragment
  extends Fragment
  implements SwipeRefreshLayout.OnRefreshListener {

  private static final int REQUEST_CODE = 1234;
  private static final String LOGTAG = PackItemRecyclerFragment.class.getSimpleName();

  @Bind(R.id.pack_item_recycler_view)
  protected RecyclerView mRecyclerView;

  @Bind(R.id.packitems_swipe_refresh)
  protected SwipeRefreshLayout mSwipeRefreshLayout;

  @Bind(R.id.add_item_voice_input)
  protected ImageButton mSpeakButton;

  @Bind(R.id.add_item_edittext)
  protected EditText mAddItemInput;

  @Bind(R.id.empty_view)
  protected View mEmptyView;

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
    act.disableFloatingActionButton();

    ActionBar appBar = act.getSupportActionBar();
    appBar.setTitle(R.string.packable_items);

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(layoutManager);

    List<PackItem> items = getItems();
    mAdapter = new PackItemRecyclerAdapter(items);

    RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
      @Override
      public void onChanged() {
        refreshVisibility();
      }
    };

    mAdapter.registerAdapterDataObserver(observer);

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

    mAddItemInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          PackItem newItem = new PackItem(v.getText().toString());
          newItem.save();
          mAdapter.add(newItem);
          v.setText("");

          refreshVisibility();

          return true;
        }

        return false;
      }
    });

    return createdView;
  }

  private List<PackItem> getItems() {
    // Get all items from the database.
    List<PackItem> items = BaseModel.getAll(PackItem.class);
    return items;
  }

  @Override
  public void onRefresh() {
    mSwipeRefreshLayout.setRefreshing(true);

    List<PackItem> items = getItems();
    for (PackItem nextItem : items) {
      Log.d(LOGTAG, "Item: " + nextItem.getName());
    }
    mAdapter.setItems(items);

    refreshVisibility();

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
    intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
    startActivityForResult(intent, REQUEST_CODE);
  }

  /**
   * Handle the results from the voice recognition activity.
   */
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      ArrayList<String> inputData = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
      StringBuilder inputBuilder = new StringBuilder();
      for (int i = 0; i < inputData.size(); i++) {
        inputBuilder.append(inputData.get(i));

        if (i < inputData.size() - 1) {
          inputBuilder.append(" ");
        }
      }

      mAddItemInput.setText(inputBuilder.toString());
    }

    super.onActivityResult(requestCode, resultCode, data);
  }

  private void refreshVisibility() {
    if (mAdapter.getItemCount() > 0) {
      mRecyclerView.setVisibility(View.VISIBLE);
      mEmptyView.setVisibility(View.GONE);
    } else {
      mRecyclerView.setVisibility(View.GONE);
      mEmptyView.setVisibility(View.VISIBLE);
    }

  }
}
