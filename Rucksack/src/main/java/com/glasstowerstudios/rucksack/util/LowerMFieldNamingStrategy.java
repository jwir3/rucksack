package com.glasstowerstudios.rucksack.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;

import java.lang.reflect.Field;

/**
 * Specifically customization of the default {@link com.google.gson.FieldNamingStrategy}
 *
 * Since {@link com.google.gson.Gson} takes field names as default key for serialization and
 * deserialization.
 *
 * This class simply indicates not to care about a lowercase "m", as a field name prefix.
 *
 * i.e: mUserId will be converted to user_id
 */
public class LowerMFieldNamingStrategy implements FieldNamingStrategy {

  /**
   * Field member prefix, if any exists
   */
  private static final String FIELD_NAME_PREFIX = "m_";

  @Override
  public String translateName(Field field) {
    String fieldName = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES.translateName(field);

    if (fieldName.startsWith(FIELD_NAME_PREFIX)) {
      // skip first letter and underscore
      // convert first letter of remaining field name to lowercase
      fieldName = fieldName.substring(2);
    }

    return fieldName;
  }
}