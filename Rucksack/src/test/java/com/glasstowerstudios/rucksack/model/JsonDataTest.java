package com.glasstowerstudios.rucksack.model;

import android.os.Build;

import com.glasstowerstudios.rucksack.BuildConfig;
import com.glasstowerstudios.rucksack.util.DataStub;
import com.glasstowerstudios.rucksack.util.RucksackGsonHelper;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public abstract class JsonDataTest<T extends BaseModel> {
  private String mJson;
  private T mData;

  public void init(Class<T> clazz) throws Exception {
    String classNameWithUnderscores = clazz.getSimpleName().toLowerCase();
    mJson = DataStub.readFile(classNameWithUnderscores + ".json");
    Assert.assertNotNull(mJson);

    mData = RucksackGsonHelper.getGson().fromJson(mJson, clazz);
  }

  public T getData() {
    if (mData == null) {
      throw new UnsupportedOperationException("init not called or file input was invalid");
    }
    return mData;
  }

  public String getJson() {
    if (mJson == null) {
      throw new UnsupportedOperationException("init not called or file input was invalid");
    }

    return mJson;
  }
}
