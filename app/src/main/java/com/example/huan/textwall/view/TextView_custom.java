package com.example.huan.textwall.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * Created by huan on 2017/7/22.
 */
@RemoteViews.RemoteView
public class TextView_custom  extends TextView{

    public TextView_custom(Context context) {
        super(context);
    }

    public TextView_custom(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextView_custom(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
