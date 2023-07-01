package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class MyRatingBar extends androidx.appcompat.widget.AppCompatRatingBar implements ViewGroup.OnHierarchyChangeListener {
    private ImageView[] mLeaves;

    public MyRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        Drawable leafDrawable = getResources().getDrawable(R.drawable.rating_leaf);

        mLeaves = new ImageView[getMax()];
        for (int i = 0; i < getMax(); i++) {
            mLeaves[i] = new ImageView(context);
            mLeaves[i].setImageDrawable(leafDrawable);
            this.addView(mLeaves[i], new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
        }
    }

    private void addView(ImageView mLeaf, LinearLayout.LayoutParams layoutParams) {
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int leafWidth = width / getMax();

        for (int i = 0; i < getMax(); i++) {
            ImageView leaf = mLeaves[i];
            ViewGroup.LayoutParams lp = leaf.getLayoutParams();
            lp.width = leafWidth;
            lp.height = height;
            leaf.setLayoutParams(lp);
        }
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int rating = getProgress();
        for (int i = 0; i < getMax(); i++) {
            mLeaves[i].setAlpha(i < rating ? 255 : 128);
        }
    }

    @Override
    public void onChildViewAdded(View parent, View child) {

    }

    @Override
    public void onChildViewRemoved(View parent, View child) {

    }
}