package com.sun.training.animation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.sun.training.R;

/**
 * Created by sunxiaoling on 2015/7/23.
 */
public class CardFlipActivity extends Activity {
    private static final String TAG = CardFlipActivity.class.getSimpleName();
    private boolean mShowingBack;
    private View frontView, backView;
    private Animator leftIn, leftOut, rightIn, rightOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_flip);

        frontView = findViewById(R.id.front);
        backView = findViewById(R.id.back);
        initAnimators();

//        if (savedInstanceState == null) {
//            getFragmentManager().beginTransaction().add(R.id.container, new CardFrontFragment()).commit();
//        }
//        frontView.setVisibility(View.GONE);
//        backView.setVisibility(View.GONE);
        findViewById(R.id.btn_flip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
            }
        });
    }

    private void initAnimators(){
        leftIn = AnimatorInflater.loadAnimator(this, R.animator.card_flip_left_in);
        leftOut = AnimatorInflater.loadAnimator(this, R.animator.card_flip_left_out);
        rightIn = AnimatorInflater.loadAnimator(this, R.animator.card_flip_right_in);
        rightOut = AnimatorInflater.loadAnimator(this, R.animator.card_flip_right_out);
    }

    private void flipCard() {
        if (mShowingBack) {
//            getFragmentManager().popBackStack();
            mShowingBack = false;

            leftIn.setTarget(frontView);
            leftOut.setTarget(backView);
            leftOut.start();
            leftIn.setStartDelay(getResources().getInteger(R.integer.card_flip_time_half));
            leftIn.start();

            return;
        }

        mShowingBack = true;

        rightIn.setTarget(backView);
        rightOut.setTarget(frontView);
        rightOut.start();
        rightIn.setStartDelay(getResources().getInteger(R.integer.card_flip_time_half));
        rightIn.start();

//        getFragmentManager().beginTransaction().
//                setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out,
//                        R.animator.card_flip_left_in, R.animator.card_flip_left_out).
//                replace(R.id.container, new CardBackFragment()).
//                addToBackStack(null).
//                commit();
    }
}
