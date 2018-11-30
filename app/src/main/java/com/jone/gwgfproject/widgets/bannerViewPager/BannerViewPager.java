package com.jone.gwgfproject.widgets.bannerViewPager;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;

/**
 * A BannerViewPager owns its indicators and it can roll automatically.
 * BannerViewPager支持指示器以及自动轮播。
 *
 * @author Chen Yu
 * @version 1.1.0
 * @date 2017-01-20
 */
public class BannerViewPager extends FrameLayout implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private ViewPagerIndicator mIndicator;
    private PagerAdapter mAdapter;
    private Context mContext;
    private int mCurrentPosition;

    //viewpager's rolling state
    private int mViewPagerScrollState;
    //自动轮播 默认 不轮播
    private boolean isAutoRolling = false;
    //轮播间隔
    private int mAutoRollingTime = 4000;
    //指示器具底部距离
    private int marginBottom = 40;
    private int mReleasingTime = 0;
    private boolean isFirstVisible;
    private static final int MESSAGE_AUTO_ROLLING = 0X1001;
    private static final int MESSAGE_AUTO_ROLLING_CANCEL = 0X1002;

    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initViews();
    }

    private void initViews() {
        //initialize the viewpager
        mViewPager = new ViewPager(mContext);
        ViewPager.LayoutParams lp = new ViewPager.LayoutParams();
        lp.width = ViewPager.LayoutParams.MATCH_PARENT;
        lp.height = ViewPager.LayoutParams.MATCH_PARENT;
        mViewPager.setLayoutParams(lp);

        //initialize the indicator
        mIndicator = new ViewPagerIndicator(mContext);
        LayoutParams indicatorlp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        indicatorlp.gravity = Gravity.BOTTOM | Gravity.CENTER;
        indicatorlp.bottomMargin = marginBottom;
        mIndicator.setLayoutParams(indicatorlp);
        isFirstVisible = true;
    }

    /**
     * 设置是否自动轮播
     * @param isAutoRolling
     */
    public void setAutoRolling(boolean isAutoRolling){
        this.isAutoRolling = isAutoRolling;
    }
    /**
     * 设置是否自动轮播间隔时间 单位ms
     * @param time
     */
    public void setAutoRollingTime(int time){
        this.mAutoRollingTime = time;
    }

    /**
     * 设置底部指示器的距底部高度 单位 px
     * @param marginBottom
     */
    public void setIndexMarginBottom(int marginBottom){
        this.marginBottom = marginBottom;
        LayoutParams mL= (LayoutParams) mIndicator.getLayoutParams();
        mL.bottomMargin=marginBottom;
        mIndicator.setLayoutParams(mL);
    }

    /**
     *
     * @param
     */
    public void setIndicatorColor(int bgColor,int selColor){
            mIndicator.setColor(bgColor,selColor);
    }

    public void setAdapter(PagerAdapter adapter){
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(this);

        mAdapter = adapter;
        mIndicator.setItemCount(mAdapter.getCount());

        //add the viewpager and the indicator to the container.
        addView(mViewPager);
        addView(mIndicator);

        //start the auto-rolling task if needed
        if(isAutoRolling){
            postDelayed(mAutoRollingTask,mAutoRollingTime);
        }

    }

    /**
     * If the current page is being dragged by the user,this method will be invoke.
     * And then it will call {@link ViewPagerIndicator#setPositionAndOffset}.
     * @param position Position index of the first page currently being displayed.
     * @param offset Value from [0, 1) indicating the offset from the page at position.
     */
    private void setIndicator(int position,float offset){
        mIndicator.setPositionAndOffset(position,offset);
    }

    /**
     * This runnable decides the viewpager should roll to next page or wait.
     */
    private Runnable mAutoRollingTask = new Runnable() {
        @Override
        public void run() {
            int now = (int) System.currentTimeMillis();
            int timediff = mAutoRollingTime;
            if(mReleasingTime != 0){
                timediff = now - mReleasingTime;
            }

            if(mViewPagerScrollState == ViewPager.SCROLL_STATE_IDLE){
                //if user's finger just left the screen,we should wait for a while.
                if(timediff >= mAutoRollingTime * 0.8){
                    if(mCurrentPosition == mAdapter.getCount() - 1){
                        mViewPager.setCurrentItem(0,true);
                    }else {
                        mViewPager.setCurrentItem(mCurrentPosition + 1,true);
                    }
                    postDelayed(mAutoRollingTask,mAutoRollingTime);
                }else {
                    postDelayed(mAutoRollingTask,mAutoRollingTime);
                }
            }else if(mViewPagerScrollState == ViewPager.SCROLL_STATE_DRAGGING){
                postDelayed(mAutoRollingTask,mAutoRollingTime);
            }

        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        setIndicator(position,positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(state == ViewPager.SCROLL_STATE_DRAGGING){
            mViewPagerScrollState = ViewPager.SCROLL_STATE_DRAGGING;
        }else if(state == ViewPager.SCROLL_STATE_IDLE){
            mReleasingTime = (int) System.currentTimeMillis();
            mViewPagerScrollState = ViewPager.SCROLL_STATE_IDLE;
        }

    }

    /**
     * 每当当前窗口被隐藏、至于后台时，Runnable就会停止并被取消，防止内存泄漏。
     * @param visibility
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (!isAutoRolling) return;
        if (visibility != VISIBLE){
            Log.d("cylog","remove callbacks");
            removeCallbacks(mAutoRollingTask);
        }else{
            if (isFirstVisible){
                isFirstVisible = false;
            }else {
                Log.d("cylog","post runnable");
                postDelayed(mAutoRollingTask,mAutoRollingTime);
            }
        }


    }

    /**
     * Save the state of this BannerViewPager.The current position will be saved.
     * @return Parcelable
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        SavedState ss = new SavedState(parcelable);
        ss.currentPosition = mCurrentPosition;
        return ss;
    }

    /**
     * Restore the BannerViewPager from the previous state.
     * @param state
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mViewPager.setCurrentItem(ss.currentPosition);
    }

    static class SavedState extends BaseSavedState {

        int currentPosition;

        public SavedState(Parcel source) {
            super(source);
            currentPosition = source.readInt();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>(){

            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
    public void setData(List<View> mItems){
        setAdapter(new ViewPagerAdapter(mItems));
    }
    public class ViewPagerAdapter extends PagerAdapter {

        private List<View> mDataViews;

        /**
         * 构造函数
         * @param mDataViews view列表
         */
        public ViewPagerAdapter(List<View> mDataViews) {
            this.mDataViews = mDataViews;
        }

        public View getView(int location){
            return this.mDataViews.get(location);
        }

        @Override
        public int getCount() {
            return mDataViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            View view = mDataViews.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
