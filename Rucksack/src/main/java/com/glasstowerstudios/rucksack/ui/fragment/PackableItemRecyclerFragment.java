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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.di.Injector;
import com.glasstowerstudios.rucksack.model.PackableItem;
import com.glasstowerstudios.rucksack.ui.activity.BaseActivity;
import com.glasstowerstudios.rucksack.ui.view.PackableItemRecyclerView;
import com.glasstowerstudios.rucksack.util.data.PackableItemDataProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A {@link Fragment} containing a {@link RecyclerView} of {@link PackableItem} objects.
 */
public class PackableItemRecyclerFragment
  extends Fragment
  implements SwipeRefreshLayout.OnRefreshListener {

  private static final int REQUEST_CODE = 1234;

  @Inject PackableItemDataProvider mPackableItemDataProvider;

  @Bind(R.id.packable_item_recycler_view)
  protected PackableItemRecyclerView mRecyclerView;

  @Bind(R.id.packitems_swipe_refresh)
  protected SwipeRefreshLayout mSwipeRefreshLayout;

  @Bind(R.id.add_item_voice_input)
  protected ImageButton mSpeakButton;

  @Bind(R.id.add_item_edittext)
  protected EditText mAddItemInput;

  @Bind(R.id.empty_view)
  protected View mEmptyView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Injector.INSTANCE.getApplicationComponent().inject(this);
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
    View createdView = inflater.inflate(R.layout.fragment_packableitem_recycler, container, false);
    ButterKnife.bind(this, createdView);

    BaseActivity act = (BaseActivity) getContext();
    act.disableFloatingActionButton();

    ActionBar appBar = act.getSupportActionBar();
    appBar.setTitle(R.string.packable_items);

    mSwipeRefreshLayout.setOnRefreshListener(this);

    // Disable button if no recognition service is present
    PackageManager pm = getActivity().getPackageManager();
    List<ResolveInfo> activities = pm.queryIntentActivities(
      new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
    if (activities.size() == 0) {
      mSpeakButton.setEnabled(false);
    } else {
      mSpeakButton.setOnClickListener(v -> onSpeakButtonClicked(v));
    }

    mAddItemInput.setOnEditorActionListener((v, actionId, event) -> {
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        PackableItem newItem = new PackableItem(v.getText().toString());
        mPackableItemDataProvider.save(newItem);
        mRecyclerView.addItem(newItem);
        v.setText("");

        refreshVisibility();

        return true;
      }

      return false;
    });

    RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
      @Override
      public void onChanged() {
        refreshVisibility();
      }
    };

    mRecyclerView.registerAdapterDataObserver(observer);

    return createdView;
  }

  @Override
  public void onRefresh() {
    mSwipeRefreshLayout.setRefreshing(true);

    mRecyclerView.refresh();
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
    if (mRecyclerView.getItemCount() > 0) {
      mRecyclerView.setVisibility(View.VISIBLE);
      mEmptyView.setVisibility(View.GONE);
    } else {
      mRecyclerView.setVisibility(View.GONE);
      mEmptyView.setVisibility(View.VISIBLE);
    }
  }
}
