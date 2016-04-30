package com.glasstowerstudios.rucksack;

import android.os.Build;
import android.test.ApplicationTestCase;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

/**
 *
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class ApplicationTest extends ApplicationTestCase<RucksackApplication> {
  public ApplicationTest() {
    super(RucksackApplication.class);
  }

  @Test
  public void testApplicationSetupProperly() {
    Assert.assertTrue(true);
  }
}
