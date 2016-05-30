package com.glasstowerstudios.rucksack.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.exception.MissingAttributeException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A small card object with rounded corners used to select a Pastime within the UI of the app. It
 * contains an icon as well as some text for a description of the Pastime.
 */
public class PastimeCard extends RelativeLayout implements Checkable {
  private static final String LOGTAG = PastimeCard.class.getSimpleName();
  private String mName;
  private Drawable mIcon;
  private int mColor;
  private boolean mSelected;

  private AttributeSet mAttrs;
  private int mDefStyleAttr;

  @Bind(R.id.pastime_name) TextView mPastimeNameView;
  @Bind(R.id.pastime_icon) ImageView mPastimeIconView;

  public PastimeCard(Context context) {
    super(context);
    init();
  }

  public PastimeCard(Context context, AttributeSet attrs) {
    super(context, attrs);
    mAttrs = attrs;
    init();
  }

  public PastimeCard(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mDefStyleAttr = defStyleAttr;
    mAttrs = attrs;
    init();
  }

  private void init() {
    Resources res = getContext().getResources();

    inflate(getContext(), R.layout.pastime_card_internal_layout, this);

    ButterKnife.bind(this, this);

    if (mAttrs == null) {
      throw new MissingAttributeException("pastimeName");
    }

    TypedArray a = getContext().obtainStyledAttributes(mAttrs, R.styleable.PastimeCard,
                                                       mDefStyleAttr, 0);

    if (!a.hasValue(R.styleable.PastimeCard_pastimeName)) {
      throw new MissingAttributeException("pastimeName");
    }

    if (!a.hasValue(R.styleable.PastimeCard_pastimeColor)) {
      throw new MissingAttributeException("pastimeColor");
    }

    if (!a.hasValue(R.styleable.PastimeCard_pastimeIcon)) {
      throw new MissingAttributeException("pastimeIcon");
    }

    mName = a.getString(R.styleable.PastimeCard_pastimeName);
    int iconResource = a.getResourceId(R.styleable.PastimeCard_pastimeIcon, 0);
    mIcon = res.getDrawable(iconResource);
    mColor = getResources().getColor(a.getResourceId(R.styleable.PastimeCard_pastimeColor, 0));

    a.recycle();

    // Set the name and icon appropriately
    mIcon.setColorFilter(new PorterDuffColorFilter(mColor, PorterDuff.Mode.SRC));
    mPastimeIconView.setImageDrawable(mIcon);
    mPastimeNameView.setTextColor(mColor);
    mPastimeNameView.setText(mName);

    // Set the background color
    Drawable background = getResources().getDrawable(R.drawable.pastime_card_background);
    setBackgroundDrawable(background);
  }

  @Override
  public void setChecked(boolean checked) {
    mSelected = checked;
  }

  @Override
  public boolean isChecked() {
    return mSelected;
  }

  @Override
  public void toggle() {
    setChecked(!isChecked());
  }

  private static final int[] STATE_CHECKABLE = {android.R.attr.state_checked};

  @Override
  protected int[] onCreateDrawableState(int extraSpace)
  {
    int[] drawableState = super.onCreateDrawableState(extraSpace + 1);

    if (isChecked()) {
      mergeDrawableStates(drawableState, STATE_CHECKABLE);
    }

    return drawableState;
  }
}
