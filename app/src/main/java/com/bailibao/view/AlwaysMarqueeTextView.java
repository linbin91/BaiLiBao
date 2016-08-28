package com.bailibao.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/8/23.
 */
public class AlwaysMarqueeTextView extends TextView{
    public AlwaysMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
