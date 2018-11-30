package com.jone.gwgfproject.widgets.bannerViewPager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * ViewPagerIndicator helps the user recognize the position of current page.
 * 指示器帮助用户分辨出当前页所在的位置
 *
 * @author Chen Yu
 * @version 1.0.0
 * @date 2016-10-03
 */
public class ViewPagerIndicator extends LinearLayout {

    private Context mContext;
    private Paint mPaint;
    private MoveView mMoveView;
    private int mCurrentPosition = 0;
    private float mPositionOffset;
    private int mItemCount = DRFAULT_ITEMCOUNT;
    private int mPadding = DEFAULT_PADDING;
    private int mRadius = DEFAULT_RADIUS;
    //the distance from the left side of the previous item to the left side of the next item.
    private int mDistanceBtwItem = mRadius * 2 + mPadding;

    private static final int DRFAULT_ITEMCOUNT = 5;
    private static final int DEFAULT_RADIUS = 10;
    private static final int DEFAULT_PADDING = 10;
    private int selColor = Color.parseColor("#ff0000");
    private int bgColor = Color.parseColor("#000000");

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        setWillNotDraw(false);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(bgColor);

        mMoveView = new MoveView(mContext);
        addView(mMoveView);
        Log.e("jhj","初始化");
    }

    public void setItemCount(int count){
        this.mItemCount = count;
        requestLayout();
    }

    public void setRadius(int radius){
        this.mRadius = radius;
        this.mDistanceBtwItem = mRadius * 2 + mPadding;
        requestLayout();
    }

    public void setPadding(int padding){
        this.mPadding = padding;
        this.mDistanceBtwItem = mRadius * 2 + mPadding;
        requestLayout();
    }

    public void setPositionAndOffset(int position,float offset){
        this.mCurrentPosition = position;
        this.mPositionOffset  =offset;
        requestLayout();
    }
    public void setColor(int bgColor,int selColor){
        this.bgColor=bgColor;
        this.selColor=selColor;
        mPaint.setColor(bgColor);
        if (mMoveView!=null){
            mMoveView.setColor(selColor);
        }
        Log.e("jhj","改变"+bgColor+"---"+selColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mPadding + (mRadius*2 + mPadding) * mItemCount,2*mRadius + 2*mPadding);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mMoveView.layout(
                (int) (mPadding + mDistanceBtwItem * (mCurrentPosition + mPositionOffset) ),
                mPadding,
                (int) (mDistanceBtwItem * ( 1 + mCurrentPosition + mPositionOffset) ),
                mPadding+mRadius*2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i = 0;i < mItemCount;i++){
            canvas.drawCircle(mRadius + mPadding + mRadius * i *2 + mPadding * i,
                    mRadius + mPadding,mRadius,mPaint);
        }

    }

    private class MoveView extends View {
        private Paint mPaint;

        public MoveView(Context context) {
            super(context);
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(selColor);
        }
        public void setColor(int color){
            mPaint.setColor(color);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(mRadius*2,mRadius*2);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawCircle(mRadius,mRadius,mRadius,mPaint);
        }
    }
}
