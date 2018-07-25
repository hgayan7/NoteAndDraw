package com.uvstudio.him.noteproject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;


public class ImgAnimator {
    public ImgAnimator() {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void enterReveal(View v, int cx, int cy)
    {
        int starRadius=0;
        int endRadius= (int) Math.hypot(v.getWidth(),v.getHeight());

        Animator anim= ViewAnimationUtils.createCircularReveal(v,cx,cy,starRadius,endRadius);
        v.setVisibility(View.VISIBLE);
        v.getBackground().setAlpha(220);
        anim.setInterpolator(new DecelerateInterpolator(2f));
        anim.setDuration(1000);
        anim.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void exitReveal(final View v, int cx, int cy)
    {
        int startRadius=Math.max(v.getWidth(),v.getHeight());
        int endRadius=0;
        Animator anim=ViewAnimationUtils.createCircularReveal(v,cx,cy,startRadius,endRadius);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }
}
