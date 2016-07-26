package com.bailibao.view.customeview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bailibao.R;

/**
 * Created by Administrator on 2016/4/12.
 */
public class DigetFlipper extends FrameLayout{

    private  Context mCxt;
    private TextView mDefaultImage;
    private TextView mFirstImage;
    private TextView mSecondImage;
    private int animateTo = 0;
    private int animateFrom = 0;
    private Interpolator mAccelerate = new AccelerateInterpolator();
    private  ObjectAnimator animator1;
    private  ObjectAnimator animator2;

    private String [] mDatas = {"0",
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9"
    };
    public DigetFlipper(Context context) {
        super(context);
        mCxt = context;
        initView();
    }



    public DigetFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCxt = context;
        initView();
    }

    public DigetFlipper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCxt = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mCxt).inflate(R.layout.rotating_text,this);
        mDefaultImage = (TextView) findViewById(R.id.text_default);
        mFirstImage = (TextView) findViewById(R.id.text_frist);
        mSecondImage = (TextView) findViewById(R.id.text_second);


    }

    public  void setDigit(int digit){
        if (digit < 0){
            digit = 0;
        }
        if (digit > 9){
            digit = 9;
        }
        animateTo = digit;
        mFirstImage.setText(mDatas[animateFrom]);
        animator1 = ObjectAnimator.ofFloat(mFirstImage,"rotationX", new float[]{0.0F, 90.0F});
        animator1.setDuration(200);
        animator1.setInterpolator(mAccelerate);
        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mFirstImage.setText(mDatas[animateFrom]);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (animateFrom == animateTo) {
                    mSecondImage.setText(mDatas[animateTo]);
                } else {
                    animateFrom++;
                    animator2.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animator2 = ObjectAnimator.ofFloat(mSecondImage,"rotationX", new float[]{-90.0F, 0.0F});
        animator2.setDuration(200);
        animator2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mSecondImage.setText(mDatas[animateFrom]);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (animateFrom == animateTo) {
                    mSecondImage.setText(mDatas[animateTo]);
                } else {
                    animator1.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator1.start();
    }

}
