package com.glasstowerstudios.rucksack.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * A helper class that allows us to retrieve {@link Gson} objects that can be used to parse models.
 */
public class RucksackGsonHelper {
  /**
   * A convertor for joda {@link DateTime} objects.
   */
  private static class DateTimeTypeConverter
    implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
    @Override
    public JsonElement serialize(DateTime src, Type srcType, JsonSerializationContext context) {
      return new JsonPrimitive(src.toString());
    }

    @Override
    public DateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context)
      throws JsonParseException {
      try {
        return new DateTime(json.getAsString());
      } catch (IllegalArgumentException e) {
        // May be it came in formatted as a java.util.Date, so try that
        Date date = context.deserialize(json, Date.class);
        return new DateTime(date);
      }
    }
  }

  /**
   * Retrieve the {@link Gson} object from a builder after having configured it appropriately for
   * the Rucksack application.
   *
   * @return A {@link Gson} object that can be used to serialize and deserialize Rucksack model
   *         objects.
   */
  public static final Gson getGson() {
    return new GsonBuilder()
      .setFieldNamingStrategy(new LowerMFieldNamingStrategy())
      .registerTypeAdapter(DateTime.class, new DateTimeTypeConverter())
      .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
      .create();
  }
}
