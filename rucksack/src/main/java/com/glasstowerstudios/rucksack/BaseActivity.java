package com.glasstowerstudios.rucksack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.glasstowerstudios.rucksack.ui.FragmentPresenter;

/**
 *
 */
public abstract class BaseActivity extends AppCompatActivity {
  private FragmentPresenter mFragmentPresenter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mFragmentPresenter = new FragmentPresenter(this, getSupportFragmentManager());
  }

  /**
   * Show a {@link Fragment} within the application which serves as a root fragment for a given
   * activity (i.e. don't add the new {@link FragmentTransaction} to the back stack.
   *
   * Note: This method should be used to display all {@link Fragment}s after the first (root) one
   * for an activity.
   *
   * @param aType The concrete type of the {@link Fragment} you wish to display. Cannot be null.
   * @param aArguments A {@link Bundle} of arguments to pass to the newly created {@link Fragment}.
   *                   Can be null, but in the case that it is null,
   *                   {@link Fragment#setArguments(android.os.Bundle)} will not be called.
   */
  public void showNonRootFragment(Class<? extends Fragment> aType, Bundle aArguments) {
    showFragment(aType, aArguments, true);
  }


  /**
   * Show a {@link Fragment} within the application.
   *
   * This should be used to display a {@link Fragment} of a given type. If you use this method
   * instead of re-implementing it in a child class, you will automatically get the animated slide
   * behavior.
   *
   * If you choose to use something other than the default behavior in your implementing class, you
   * should override this method so that all users of your new implementation can use a standard
   * animation (unless you want it only to happen for a specific fragment).
   *
   * @param aType The concrete type of the {@link Fragment} you wish to display. Cannot be null.
   * @param aArguments A {@link Bundle} of arguments to pass to the newly created {@link Fragment}.
   *                   Can be null, but in the case that it is null,
   *                   {@link Fragment#setArguments(android.os.Bundle)} will not be called.
   * @param aAddToBackStack If true, the created {@link FragmentTransaction} will be added to the
   *                        back stack.
   */
  public void showFragment(Class<? extends Fragment> aType, Bundle aArguments,
                           boolean aAddToBackStack) {
    mFragmentPresenter.showFragmentAnimated(aType, aArguments, aAddToBackStack,
                                            getFragmentContainerID());
  }


  /**
   * Show a {@link Fragment} within the application by replacing another {@link Fragment} already on
   * the back stack.
   *
   * This will pop a {@link Fragment} already on the back stack and replace it with another
   * {@link Fragment} of a given type. This behavior is desirable, when, for example, you are
   * creating a new object and don't wish to offer the ability to go back to the creation
   * {@link Fragment} once the API has processed it.
   *
   * This should be used to display a {@link Fragment} of a given type. If you use this method
   * instead of re-implementing this in a child class, you will automatically get the animated slide
   * behavior.
   *
   * If you choose to use something other than the default behavior in your implementing class, you
   * should override this method so that all users of your new implementation can use a standard
   * animation (unless you want it only to happen for a specific fragment).
   *
   * @param aReplacedFragmentType The concrete type of the {@link Fragment} already on the back
   *                              stack that you wish to replace with a new one. Cannot be null.
   * @param aNewFragmentType      The concrete type of the {@link Fragment} you wish to place on the
   *                              back stack. Cannot be null.
   * @param aArguments A {@link Bundle} of arguments to pass to the newly created {@link Fragment}.
   *                   Can be null, but in the case that it is null,
   *                   {@link Fragment#setArguments(android.os.Bundle)} will not be called.
   */
  public void replaceFragmentOnBackStack(Class<? extends Fragment> aReplacedFragmentType,
                                         Class<? extends Fragment> aNewFragmentType,
                                         Bundle aArguments) {
    FragmentManager manager = getSupportFragmentManager();
    manager.popBackStackImmediate(aReplacedFragmentType.getSimpleName(),
                                  FragmentManager.POP_BACK_STACK_INCLUSIVE);

    showFragment(aNewFragmentType, aArguments, true);
  }

  /**
   * Show a {@link Fragment} within the application which serves as a root fragment for a given
   * activity (i.e. don't add the new {@link FragmentTransaction} to the back stack.
   *
   * Note: This method should only be called by classes implementing {@link BaseActivity} when
   * displaying the first {@link Fragment} for that activity.
   *
   * @param aType The concrete type of the {@link Fragment} you wish to display. Cannot be null.
   * @param aArguments A {@link Bundle} of arguments to pass to the newly created {@link Fragment}.
   *                   Can be null, but in the case that it is null,
   *                   {@link Fragment#setArguments(android.os.Bundle)} will not be called.
   */
  public void showRootFragment(Class<? extends Fragment> aType, Bundle aArguments) {
    showFragment(aType, aArguments, false);
  }

  /**
   * Retrieves the ID of the base fragment container (the container into which all {@link Fragment}
   * objects are loaded.
   *
   * @return The ID of the fragment container for this activity.
   */
  public abstract int getFragmentContainerID();
}
