package com.glasstowerstudios.rucksack.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A helper class that allows us to retrieve {@link Gson} objects that can be used to parse models.
 */
public class RucksackGsonHelper {
  /**
   * Retrieve the {@link Gson} object from a builder after having configured it appropriately for
   * the Rucksack application.
   *
   * @return A {@link Gson} object that can be used to serialize and deserialize Rucksack model
   *         objects.
   */
  public static final Gson getGson() {
    GsonBuilder builder = new GsonBuilder();
    builder.setFieldNamingStrategy(new LowerMFieldNamingStrategy());
    return builder.create();
  }
}
