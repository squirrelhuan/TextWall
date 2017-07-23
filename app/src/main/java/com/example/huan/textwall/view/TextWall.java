package com.example.huan.textwall.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huan.textwall.R;
import com.example.huan.textwall.model.TextItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Squirrelhuan on 2017/7/21.
 */

public class TextWall extends FrameLayout implements ViewTreeObserver.OnGlobalLayoutListener {

    private int[] colors = {R.color.red, R.color.orange, R.color.yellow, R.color.green, R.color.cyan, R.color.blue, R.color.purple};
    private int width, height;
    Random random = new Random();

    public TextWall(Context context) {
        super(context);
    }

    public TextWall(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextWall(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private boolean isFirst = true;

    public void onGlobalLayout() {
        if (isFirst) {
            height = getHeight();
            isFirst = false;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);//(API 16)
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    private static int Animination_index;
    static ScaleAnimation animation = null;
    int count;

    public void setData(List<TextItem> items, final Context context) {
        count = items.size();

        //1.优先级排序
        items = sortTextItem(items);
        //2.字体大小排序
        int[] frontSizes = generateFrontSize(items.size());
        for (int i = 0; i < count; i++) {
            TextItem temp = items.get(i);
            temp.setFrontSize(frontSizes[i]);
            temp.setFrontColor(colors[random.nextInt(7)]);
            items.set(i, temp);
        }

        for (int i = count - 1; i >= 0; i--) {
            TextView textView = new TextView(context);
            textView.setText(items.get(i).getValue());
            textView.setTextSize(items.get(i).getFrontSize());
            textView.setTextColor(context.getResources().getColor(items.get(i).getFrontColor()));
            int b = random.nextInt(2);
            if (b == 1) {
                textView.setSingleLine(true);
            } else {
                textView.setEms(1);
            }
            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
            textView.setTag(items.get(i));
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, ((TextItem) view.getTag()).getValue(), Toast.LENGTH_SHORT).show();
                }
            });
            addView(textView);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            FrameLayout.MarginLayoutParams marginParams = null;
            //获取view的margin设置参数
            if (params instanceof ViewGroup.MarginLayoutParams) {
                marginParams = (ViewGroup.MarginLayoutParams) params;
            } else {
                //不存在时创建一个新的参数
                //基于View本身原有的布局参数对象
                marginParams = new ViewGroup.MarginLayoutParams(params);
            }
            //设置margin
            int height_px = getHeight();
            int width_px = getWidth();
            int left = random.nextInt(width_px);
            int top = random.nextInt(height_px);
            left = left > (width_px / 2) ? left - width_px / 10 - getCharacterWidth(items.get(i).getValue(), items.get(i).getFrontSize()) : left;
            top = top > (height_px / 2) ? top - height_px / 10 - ((b == 0) ? getCharacterWidth(items.get(i).getValue(), items.get(i).getFrontSize()) : items.get(i).getFrontSize()) : top;
            marginParams.setMargins(left, top, 0, 0);
            textView.setLayoutParams(marginParams);
        }
        TextView tv = null;
        for (int i = 0; i < count; i++) {
            tv = (TextView) getChildAt(i);
            animation = new ScaleAnimation(0.8f, 1.3f, 0.8f, 1.3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (Animination_index + 1 == count) {
                        Animination_index = 0;
                    } else {
                        Animination_index++;
                    }
                    doAnimination();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation.setDuration(1300);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.setRepeatCount(0);
           /* tv.setAnimation(animation);*/
        }
        getChildAt(0).startAnimation(animation);
    }

    void doAnimination() {
        if (Animination_index == 0 || getChildAt(count-1).getAnimation() != null) {
            getChildAt(count-1).setAnimation(null);
        } else {
            getChildAt(Animination_index - 1).setAnimation(null);
        }
        getChildAt(Animination_index).startAnimation(animation);
    }


    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    private int[] generateFrontSize(int count) {
        int[] sizes = new int[count];
        for (int i = 0; i < count; i++) {
            random = new Random();
            sizes[i] = (random.nextInt(6) * 5 + 12);
        }
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                if (sizes[i] > sizes[j]) {
                    int c = sizes[i];
                    sizes[i] = sizes[j];
                    sizes[j] = c;
                }
            }
        }
        return sizes;
    }

    /**
     * 排序
     */
    List<TextItem> sortTextItem(List<TextItem> items) {
        int count = items.size();
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                if (items.get(i).getIndex() > items.get(j).getIndex()) {
                    TextItem item_c = items.get(i);
                    items.add(i, items.get(j));
                    items.add(j, item_c);
                }
            }
        }
        return items;
    }

    //Android获取当前文字的总体长度的方法
    public int getCharacterWidth(String text, float size) {
        if (null == text || "".equals(text)) {
            return 0;

        }
        Paint paint = new Paint();
        paint.setTextSize(size);
        int text_width = (int) paint.measureText(text);// 得到总体长度
        // int width = text_width/text.length();//每一个字符的长度
        return text_width;
    }

    public static int px2dip(Context mContext, float px) {

        float scale = mContext.getResources().getDisplayMetrics().density;

        return (int) (px / scale + 0.5f);

    }

    public static int dip2px(Context mContext, float dp) {

        float scale = mContext.getResources().getDisplayMetrics().density;

        return (int) (dp * scale + 0.5f);

    }

}
