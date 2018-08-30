package com.example.yehyunee.tagapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    RelativeLayout mImageWholeLayout;
    RelativeLayout mTextWholeLayout;

    Handler mhandler;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageWholeLayout = (RelativeLayout)findViewById(R.id.image_whole_layout);
        mTextWholeLayout = (RelativeLayout)findViewById(R.id.text_whole_layout);

        mContext = this;

        mhandler = new Handler();
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mImageWholeLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.ani_img));
                mTextWholeLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.ani_text));
                mTextWholeLayout.setVisibility(View.VISIBLE);
            }
        }, 2000);
    }
}
