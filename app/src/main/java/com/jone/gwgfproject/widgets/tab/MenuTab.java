package com.jone.gwgfproject.widgets.tab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.utils.DensityUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.annotations.Nullable;

public class MenuTab extends LinearLayout implements View.OnClickListener {
    private int[] iconSels, iconNor, selNorColors;
    private String[] texts;
    private List<Integer> hintList;
    private Context mContext;
    //初始化时使用的
    public TextView mTabTextView;
    private int mSelIndex = -1;
    private int mOldSelIndex = -1;
    private TabClickBack mTabClickBack;
    private int textSize = 14;
    //记录初始化是否完成
    private boolean initEnd = false;

    public MenuTab(Context context) {
        this(context, null);
    }

    public MenuTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuTab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        setOrientation(HORIZONTAL);
    }

    public void setTabs(@Nullable int[] iconSels, int[] iconNor, String[] texts, int[] selNorColors, TabClickBack tabClickBack) {
        this.iconNor = iconNor;
        this.iconSels = iconSels;
        this.texts = texts;
        this.selNorColors = selNorColors;
        this.mTabClickBack = tabClickBack;
        renderTabs();
    }

    private void renderTabs() {
        if (mSelIndex >= texts.length || mSelIndex < 0) {
            mSelIndex = 0;
        }
        for (int i = 0; i < texts.length; i++) {
            mTabTextView = new TextView(mContext);
            mTabTextView.setId(i);
            mTabTextView.setText(texts[i]);
            mTabTextView.setTextSize(textSize);
            mTabTextView.setPadding(0, DensityUtil.dp2px(mContext, 5), 0, 0);
            changeTabStatus(mTabTextView, i);
            mTabTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            mTabTextView.setLayoutParams(getTabParams());
            mTabTextView.setOnClickListener(this);
            addView(mTabTextView);
        }
      hintTab();
    }

    private LayoutParams getTabParams() {
        LayoutParams tablayoutParams = new LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        tablayoutParams.weight = 1;
        return tablayoutParams;
    }

    private void changeTabStatus(TextView textView, int position) {
        textView.setTextColor(selNorColors[position == mSelIndex ? 0 : 1]);
        if (iconSels == null) {
            if (initEnd) {
                return;
            }
            textView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(iconNor[position]), null, null);
        } else {
            textView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(position == mSelIndex ? iconSels[position] : iconNor[position]), null, null);
        }

    }

    /**
     * 设置选中的index
     *
     * @param selIndex
     */
    public void setSelIndex(int selIndex) {
        //未渲染时设置选中tab
        if (texts == null) {
            this.mSelIndex = selIndex;
        } else {
            if (selIndex >= 0 && selIndex < texts.length) {
                mOldSelIndex = mSelIndex;
                mSelIndex = selIndex;
                turnTabStatus(mOldSelIndex, mSelIndex);
            }
        }
    }

    public void setHintTab(int... hintTab) {
        if (hintList == null) {
            hintList = new ArrayList<>();
        }
        for (int hTab:hintTab) {
            hintList.add(hTab);
        }
        if (texts != null) {
            hintTab();
        }
    }
    private void hintTab(){
        if (hintList!=null&&hintList.size()>0) {
            for (int i = 0; i < hintList.size(); i++) {
                getChildAt(hintList.get(i)).setVisibility(GONE);
            }
        }
    }
    public void showHintTab(){
        if (hintList!=null&&hintList.size()>0){
            for (int i = 0; i <hintList.size() ; i++) {
                getChildAt(hintList.get(i)).setVisibility(VISIBLE);
            }
        }
    }

    private void turnTabStatus(int oldSelIndex, int selIndex) {
        changeTabStatus((TextView) getChildAt(oldSelIndex), oldSelIndex);
        changeTabStatus((TextView) getChildAt(selIndex), selIndex);
    }

    private int cId;

    @Override
    public void onClick(View v) {
        cId = v.getId();
        if (cId != mSelIndex) {
            mOldSelIndex = mSelIndex;
            mSelIndex = cId;
            turnTabStatus(mOldSelIndex, mSelIndex);
            mTabClickBack.onTabClick(cId);

        }
    }

    public interface TabClickBack {
        void onTabClick(int currentIndex);
    }
}
