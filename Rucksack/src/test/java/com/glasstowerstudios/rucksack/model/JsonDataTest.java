package com.glasstowerstudios.rucksack.model;

import android.os.Build;

import com.glasstowerstudios.rucksack.BuildConfig;
import com.glasstowerstudios.rucksack.util.DataStub;
import com.glasstowerstudios.rucksack.util.RucksackGsonHelper;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public abstract class JsonDataTest<T> {
  private String mJson;
  private T mPrimaryData;
  private T mSecondaryData;

  public void init(Class<T> clazz) throws Exception {
    String classNameCamelCase = clazz.getSimpleName().toLowerCase();
    mJson = DataStub.readFile(classNameCamelCase + ".json");
    Assert.assertNotNull(mJson);

    String secondaryJson;
    try {
      secondaryJson = DataStub.readFile(classNameCamelCase + "_2.json");
    } catch (IOException e) {
      // There _is_ no secondary json file. We don't need to do anything other than just know
      // that there was an error and make sure secondaryJson is null.
      secondaryJson = null;
    }

    mPrimaryData = RucksackGsonHelper.getGson().fromJson(mJson, clazz);

    if (secondaryJson != null) {
      mSecondaryData = RucksackGsonHelper.getGson().fromJson(secondaryJson, clazz);
    }
  }

  public T getPrimaryData() {
    if (mPrimaryData == null) {
      throw new UnsupportedOperationException("init not called or file input was invalid");
    }
    return mPrimaryData;
  }

  public T getSecondaryData() {
    if (mSecondaryData == null) {
      throw new UnsupportedOperationException("init not called or file input was invalid");
    }

    return mSecondaryData;
  }

  public String getJson() {
    if (mJson == null) {
      throw new UnsupportedOperationException("init not called or file input was invalid");
    }

    return mJson;
  }
}
