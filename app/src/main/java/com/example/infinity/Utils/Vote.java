package com.example.infinity.Utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public  class Vote {
    private static final String TAG = "Vote";
    private static final DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();

    private  ImageView greenVote , greyVote ;

    public Vote(ImageView greenVote, ImageView greyVote) {
        this.greenVote = greenVote;
        this.greyVote = greyVote;
    }

    public void toggleVote(){
        Log.d(TAG, "toggleVote: Toggling Vote.");
        AnimatorSet animatorSet = new AnimatorSet();

        if (greenVote.getVisibility() == View.VISIBLE){
            Log.d(TAG, "toggleVote: toggling green vote button off");
            greenVote.setScaleX(0f);
            greenVote.setScaleY(0f);


            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(greenVote , "scaleY" ,1f,0f);
            scaleDownY.setDuration(300);
            scaleDownY.setInterpolator(ACCELERATE_INTERPOLATOR);


            ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(greenVote , "scaleX" ,1f,0f);
            scaleDownX.setDuration(300);
            scaleDownX.setInterpolator(ACCELERATE_INTERPOLATOR);



            greenVote.setVisibility(View.GONE);
            greyVote.setVisibility(View.VISIBLE);

            animatorSet.playTogether(scaleDownY ,scaleDownX);

        }else {
            Log.d(TAG, "toggleVote: toggling green vote button on");
            greenVote.setScaleX(0f);
            greenVote.setScaleY(0f);


            ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(greenVote , "scaleX" ,0f,1f);
            scaleDownX.setDuration(300);
            scaleDownX.setInterpolator(DECELERATE_INTERPOLATOR);


            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(greenVote , "scaleY" ,0f,1f);
            scaleDownY.setDuration(300);
            scaleDownY.setInterpolator(DECELERATE_INTERPOLATOR);

            greenVote.setVisibility(View.VISIBLE);
            greyVote.setVisibility(View.GONE);

            animatorSet.playTogether(scaleDownY ,scaleDownX);

        }

        animatorSet.start();

    }
}
