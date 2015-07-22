package com.sun.training.animation;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by sunxiaoling on 15/7/22.
 */
public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static final String TAG = ZoomOutPageTransformer.class.getSimpleName();

    private final float MIN_SCALE = 0.85f;
    private final float MIN_ALPHA = 0.5f;

    @Override
    public void transformPage(View page, float position) {
        Log.d(TAG, "transformPage page:" + page + " position:" + position);
        int pageWidth = page.getWidth();
        int pageHeight = page.getHeight();

        if (position < -1) {//off-screen to the left
            page.setAlpha(0);
        } else if (position <= 1) {
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));//between MIN_SCALE and 1
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {//[-1, 0)
                page.setTranslationX(horzMargin - vertMargin / 2);
            } else {//[0, 1]
                page.setTranslationX(-horzMargin + vertMargin / 2);
            }

            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);

            page.setAlpha(
                    MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else {//off-screen to the right
            page.setAlpha(0);
        }

    }
}
