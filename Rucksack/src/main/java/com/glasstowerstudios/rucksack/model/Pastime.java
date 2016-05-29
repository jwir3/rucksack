package com.glasstowerstudios.rucksack.model;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.glasstowerstudios.rucksack.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * A Pastime, or activity that can be performed while on a {@link Trip}. Has a one-to-many
 * relationship with packable items, and a many-to-many relationship with {@link Trip}s.
 */
public class Pastime implements Comparable<Pastime> {

  private String name;
  private String mIconResource;

  // Default constructor provided for ActiveAndroid initialization
  public Pastime() {

  }

  public Pastime(String aName, String iconResource) {
    name = aName;
    mIconResource = iconResource;
  }

  public String getIconResourceName() {
    return mIconResource;
  }
  
  public String getName() {
    return name;
  }

  public int getIconResourceId(Context context) {
    int resourceId = context.getResources().getIdentifier(getIconResourceName(), "drawable",
                                                          context.getPackageName());
    if (resourceId <= 0) {
      resourceId = R.drawable.ic_pastime_default;
    }

    return resourceId;
  }

  public Drawable getIcon(Context context) {
    int iconId = getIconResourceId(context);
    return context.getResources().getDrawable(iconId);
  }

  @Override
  public int compareTo(Pastime another) {
    return getName().compareTo(another.getName());
  }

  /**
   * Retrieve the name of a resource (in the form "R.drawable.xxx"), as a string, for a given
   * numerical resource ID.
   *
   * Note: This will only work for drawable resources that represent Pastime icons.
   *
   * @param aResourceId The numerical resource ID for which to retrieve the resource name.
   * @param context The {@link Context} under which this should be retrieved.
   *
   * @return A String representation of the resource identifier.
   */
  public static String getResourceNameForId(int aResourceId, Context context) {
    Map<String, Integer> resourceMap = getAvailablePastimeIconResourceNameToIdMap(context);
    for (String nextKey : resourceMap.keySet()) {
      if (resourceMap.get(nextKey).equals(aResourceId)) {
        return nextKey;
      }
    }

    return null;
  }

  /**
   * Retrieve a mapping of resource names to numerical resource ids, for all icons specified as
   * Pastime icons.
   *
   * Note: This will only work for drawable resources that represent Pastime icons.
   *
   * @param context The {@link Context} under which this should be retrieved.
   *
   * @return A {@link Map} of {@link String}s to {@link Integer}s where the key is a resource name
   *         and the value is a resource identifier.
   */
  public static Map<String, Integer> getAvailablePastimeIconResourceNameToIdMap(Context context) {
    Map<String, Integer> returnValue = new HashMap<>();
    String pastimePrefix = context.getResources().getString(R.string.pastime_icon_prefix);

    ArrayList<String> allDrawableResourceNames = new ArrayList<>();
    for (Field field : R.drawable.class.getDeclaredFields()) {
      allDrawableResourceNames.add(field.getName());
    }

    List<String> resourceNames = Observable.from(allDrawableResourceNames)
                                                  .filter(name -> name.contains(pastimePrefix))
                                                  .toList()
                                                  .toBlocking()
                                                  .first();

    for (String nextName : resourceNames) {
      int resourceId = context.getResources().getIdentifier(nextName, "drawable", context.getPackageName());
      returnValue.put(nextName, resourceId);
    }

    return returnValue;
  }

  /**
   * Retrieve a {@link List} of resource identifiers representing Pastime icons.
   *
   * @param context The {@link Context} under which this should be retrieved.
   *
   * @return A {@link List} of {@link Integer}s where each entry represents a numerical resource id
   *         for a {@link Pastime} icon.
   */
  public static List<Integer> getAvailablePastimeIconResources(Context context) {
    String pastimePrefix = context.getResources().getString(R.string.pastime_icon_prefix);

    ArrayList<String> allDrawableResourceNames = new ArrayList<>();
    for (Field field : R.drawable.class.getDeclaredFields()) {
      allDrawableResourceNames.add(field.getName());
    }

    List<Integer> resourceIdentifiers = Observable.from(allDrawableResourceNames)
                                                  .filter(name -> name.contains(pastimePrefix))
                                                  .map(s -> context.getResources()
                                                    .getIdentifier(s, "drawable",
                                                                   context.getPackageName()))
                                                  .toList()
                                                  .toBlocking()
                                                  .first();

    return resourceIdentifiers;
  }
}
