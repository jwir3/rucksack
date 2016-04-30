package com.glasstowerstudios.rucksack.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * A Pastime, or activity that can be performed while on a {@link Trip}. Has a one-to-many
 * relationship with packable items, and a many-to-many relationship with {@link Trip}s.
 */
@Table(name = "pastimes")
public class Pastime extends BaseModel {

  @Column(name = "pastimeName")
  private String name;

  @Column(name = "pastimeIconId")
  private int iconId;

  // Default constructor provided for ActiveAndroid initialization
  public Pastime() {

  }

  public Pastime(String aName, int aIconId) {
    name = aName;
    iconId = aIconId;
  }

  public String getName() {
    return name;
  }

  public int getIconId() {
    return iconId;
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
