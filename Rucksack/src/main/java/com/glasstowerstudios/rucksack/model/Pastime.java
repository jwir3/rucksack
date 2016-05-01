package com.glasstowerstudios.rucksack.model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.glasstowerstudios.rucksack.R;

import java.util.List;

/**
 * A Pastime, or activity that can be performed while on a {@link Trip}. Has a one-to-many
 * relationship with packable items, and a many-to-many relationship with {@link Trip}s.
 */
@Table(name = "pastimes")
public class Pastime extends BaseModel {

  @Column(name = "pastimeName")
  private String name;

  @Column(name = "pastimeIconResource")
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

  /**
   * Convenience method for retrieving all {@link Pastime} objects in the database.
   *  Equivalent to {@link BaseModel#getAll(Pastime.class)}.
   *
   * @return A {@link List} of all {@link Pastime} objects in the database.
   */
  public static List<Pastime> getAll() {
    return BaseModel.getAll(Pastime.class);
  }
}
