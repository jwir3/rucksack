package com.glasstowerstudios.rucksack.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * A Pastime, or activity that can be performed while on a {@link Trip}. Has a one-to-many
 * relationship with packable items, and a many-to-many relationship with {@link Trip}s.
 */
@Table(name = "pastimes")
public class Pastime extends BaseModel {

  @Column(name = "pastimeName")
  private String mName;

  @Column(name = "pastimeIconId")
  private int mIconId;

  public Pastime(String aName, int aIconId) {
    mName = aName;
    mIconId = aIconId;
  }

  public String getName() {
    return mName;
  }

  public int getIconId() {
    return mIconId;
  }
}
