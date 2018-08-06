package org.cmucreatelab.flutter_android.helpers;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by Steve on 6/5/2017.
 */

public class LayeringAnimation
{

    private static final int DURATION = 250;
    private View left, right;


    // inFromRightAnimation
    private Animation inFromRightAnimation() {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, +1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        inFromRight.setDuration(DURATION);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        inFromRight.setFillAfter(true);
        inFromRight.setAnimationListener(inFromRightListener);
        return inFromRight;
    }

    // outToLeftAnimation
    private Animation outToLeftAnimation() {
        Animation outToLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        outToLeft.setDuration(DURATION);
        outToLeft.setInterpolator(new AccelerateInterpolator());
        outToLeft.setFillAfter(true);
        return outToLeft;
    }

    // inFromLeftAnimation
    private Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        inFromLeft.setDuration(DURATION);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        inFromLeft.setFillAfter(true);
        inFromLeft.setAnimationListener(inFromLeftListener);
        return inFromLeft;
    }

    // outToRightAnimation
    private Animation outToRightAnimation() {
        Animation outToRight = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, +1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        outToRight.setDuration(DURATION);
        outToRight.setInterpolator(new AccelerateInterpolator());
        outToRight.setFillAfter(true);
        return outToRight;
    }


    // animation listeners


    private Animation.AnimationListener inFromLeftListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            // empty
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            left.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // empty
        }
    };


    private Animation.AnimationListener inFromRightListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            // empty
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            right.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // empty
        }
    };


    public LayeringAnimation(View left, View middle, View right) {
        this.left = left;
        this.right = right;

        // ensure this view is on top of the left and right views
        middle.bringToFront();
    }

    public LayeringAnimation(View left, View middle) {
        this.left = left;

        // ensure this view is on top of the left and right views
        middle.bringToFront();
    }


    public void showLeftView() {
        left.setVisibility(View.VISIBLE);
        left.startAnimation(outToLeftAnimation());
    }


    public void showRightView() {
        right.setVisibility(View.VISIBLE);
        right.startAnimation(outToRightAnimation());
    }


    public void hideLeftView() {
        left.setVisibility(View.VISIBLE);
        left.startAnimation(inFromLeftAnimation());
    }


    public void hideRightView() {
        right.setVisibility(View.VISIBLE);
        right.startAnimation(inFromRightAnimation());
    }

}
