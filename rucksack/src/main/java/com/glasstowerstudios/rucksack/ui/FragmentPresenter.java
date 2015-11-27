package com.glasstowerstudios.rucksack.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.glasstowerstudios.rucksack.R;

import static android.support.v4.app.Fragment.instantiate;

/**
 * A utility for displaying fragments. Use this utility when needing more fine grained control of
 * fragment displaying such as when needing to change the default animations or fragment container.
 */
public class FragmentPresenter {

  private static final String LOGTAG = FragmentPresenter.class.getSimpleName();

  static Animations DEFAULT_ANIMATIONS = new Animations(R.anim.slide_in_right,
                                                        R.anim.slide_out_left,
                                                        android.R.anim.slide_in_left,
                                                        android.R.anim.slide_out_right);

  private Context context;
  private FragmentManager manager;
  private Animations animations;

  public FragmentPresenter(@NonNull Context context, @NonNull FragmentManager manager) {
    this.context = context;
    this.manager = manager;
    animations = DEFAULT_ANIMATIONS;
  }

  /**
   * Replaces an existing fragment that was added to a container.
   *
   * @param type           The class type of the fragment.
   * @param args           Optional Bundle arguments set through {@link Fragment#setArguments(Bundle)}
   * @param addToBackStack Set to True to include the {@link FragmentTransaction} in the backstack.
   * @param container      The id resource of the {@link Fragment} container.
   * @return The new fragment that was placed in the container.
   */
  public <T extends Fragment> T showFragment(@NonNull Class<T> type, @Nullable Bundle args,
                                             boolean addToBackStack, @IdRes int container) {

    animations = null; /** Remove any previous animation. */
    return replaceFragment(type, args, addToBackStack, container);
  }

  /**
   * Replaces an existing fragment that was added to a container with a slide in from right
   * animation.
   * <p>
   * To add custom fragment transition animations call {@link #setCustomAnimations(int, int, int,
   * int)} first.
   *
   * @param type           The class type of the fragment.
   * @param args           Optional Bundle arguments set through {@link Fragment#setArguments(Bundle)}
   * @param addToBackStack Set to True to include the {@link FragmentTransaction} in the backstack.
   * @param container      The id resource of the {@link Fragment} container.
   * @return The new fragment that was placed in the container.
   */
  public <T extends Fragment> T showFragmentAnimated(@NonNull Class<T> type, @Nullable Bundle args,
                                                     boolean addToBackStack, @IdRes int container) {
    if (animations == null) {
      animations = DEFAULT_ANIMATIONS;
    }

    return replaceFragment(type, args, addToBackStack, container);
  }

  @SuppressWarnings("unchecked")
  private <T extends Fragment> T replaceFragment(@NonNull Class<T> type, @Nullable Bundle args,
                                                 boolean addToBackStack, @IdRes int container) {
    FragmentTransaction transaction = manager.beginTransaction();

    if (animations != null) {
      transaction.setCustomAnimations(animations.enter, animations.exit, animations.popEnter,
                                      animations.popExit);
    }

    String fName = type.getName();
    Fragment fragment = args == null ? instantiate(context, fName)
                                     : instantiate(context, fName, args);
    /**
     * Warning: This tag name is used by convention throughout the app. Do not change unless it is
     * changed in all places calling {@link FragmentManager#findFragmentByTag(String)}
     */
    final String TAG = type.getSimpleName();
    transaction.replace(container, fragment, TAG);

    if (addToBackStack) {
      transaction.addToBackStack(TAG);
    }

    transaction.commit();

    animations = DEFAULT_ANIMATIONS;
    return (T) fragment;
  }

  /**
   * Set specific animation resources to run for the fragments that are entering and exiting in this
   * transaction. The popEnter and popExit animations will be played for enter/exit operations
   * specifically when popping the back stack.
   */
  public FragmentPresenter setCustomAnimations(@AnimRes int enter, @AnimRes int exit, @AnimRes int popEnter,
                                               @AnimRes int popExit) {
    animations = new Animations(enter, exit, popEnter, popExit);
    return this;
  }

  /** A data class describing the fragment animations. */
  static class Animations {
    int enter, exit, popEnter, popExit;

    public Animations(@AnimRes int enter, @AnimRes int exit, @AnimRes int popEnter, @AnimRes int popExit) {
      this.enter = enter;
      this.exit = exit;
      this.popEnter = popEnter;
      this.popExit = popExit;
    }
  }
}
