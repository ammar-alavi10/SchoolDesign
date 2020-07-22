package com.ammar.shreeKrishnaNationalSchoolOfExcellence.KidsCorner.balloonpoper;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.KidsCorner.balloonpoper.utils.PixelHelper;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;

public class Balloon extends androidx.appcompat.widget.AppCompatImageView implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
    private ValueAnimator mAnimator;
    private BalloonListener mListener;
    private boolean mPopped;
    public boolean isAlphabet;
    public String text;

    public Balloon(Context context) {
        super(context);
    }

    public Balloon(Context context, int color, int rawheight, boolean isAlphabet, String text) {
        super(context);

        mListener = (BalloonListener) context;

        // this.setImageResource(R.drawable.balloon);
        if(isAlphabet)
        {
            this.isAlphabet = true;
            this.text = text;
            this.setImageDrawable(writeTextOnDrawable(context, R.drawable.balloonred, text));
        }
        else{
            this.isAlphabet = false;
            this.setImageResource(R.drawable.balloon);
            this.setColorFilter(color);
        }

        int rawWidth = rawheight / 2;

        int dpHeight = PixelHelper.pixelsToDp(rawheight, context);
        int dpWidth = PixelHelper.pixelsToDp(rawWidth, context);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dpWidth, dpHeight);
        setLayoutParams(params);
    }

    public void releaseBalloon(int screenHeight, int duration) {
        mAnimator = new ValueAnimator();
        mAnimator.setDuration(duration);
        mAnimator.setFloatValues(screenHeight,0f);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setTarget(this);
        mAnimator.addListener(this);
        mAnimator.addUpdateListener(this);
        mAnimator.start();
    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {
        if(!mPopped) {
            mListener.popBalloon(this, false);
        }
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        setY((Float) valueAnimator.getAnimatedValue());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!mPopped) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                mListener.popBalloon(this, true);
                mPopped = true;
                mAnimator.cancel();
            }
        }
        return super.onTouchEvent(event);
    }

    public void setPopped(boolean popped) {
        mPopped = popped;
        if(popped) {
            mAnimator.cancel();
        }
    }

    public interface BalloonListener {
        void popBalloon(Balloon balloon, boolean user_touch);
    }

    private BitmapDrawable writeTextOnDrawable(Context mContext, int drawableId, String text)
    {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);

        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(mContext, 110));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);

        //If the text is bigger than the canvas , reduce the font size
        if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(convertToPixels(mContext, 7));        //Scaling needs to be used for different dpi's

        //Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;

        canvas.drawText(text, xPos, yPos-120, paint);

        return new BitmapDrawable(getResources(), bm);

    }

    public static int convertToPixels(Context context, int nDP)
    {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f) ;

    }
}
