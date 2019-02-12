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
    //Tab的基础moudle
    public static class Tab{
        private boolean isSelected;
        private int imgSel = -1;
        private int imgNor;
        private int textSelColor;
        private int textNorColor;
        private String text;
        private boolean isShow=true;
        private int textSize=14;

        public boolean isSelected() {
            return isSelected;
        }
        public int getTextColor(){
            return isSelected?getTextSelColor():getTextNorColor();
        }
        public int getTabIcon(){
            return isSelected?getImgSel():getImgNor();
        }

        public Tab setSelected(boolean selected) {
            isSelected = selected;
            return this;
        }

        public int getImgSel() {
            return imgSel==-1?imgNor:imgSel;
        }

        public Tab setImgSel(int imgSel) {
            this.imgSel = imgSel;
            return this;
        }

        public int getImgNor() {
            return imgNor;
        }

        public Tab setImgNor(int imgNor) {
            this.imgNor = imgNor;
            return this;
        }

        public int getTextSelColor() {
            return textSelColor;
        }

        public Tab setTextSelColor(int textSelColor) {
            this.textSelColor = textSelColor;
            return this;
        }

        public int getTextNorColor() {
            return textNorColor;
        }

        public Tab setTextNorColor(int textNorColor) {
            this.textNorColor = textNorColor;
            return this;
        }

        public String getText() {
            return text;
        }

        public Tab setText(String text) {
            this.text = text;
            return this;
        }

        public boolean isShow() {
            return isShow;
        }

        public Tab setShow(boolean show) {
            isShow = show;
            return this;
        }

        public int getTextSize() {
            return textSize;
        }

        public Tab setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }
    }
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
    private List<MenuTab.Tab> tabs;

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
    public void setTabs(List<MenuTab.Tab> tabs, TabClickBack tabClickBack){
        this.tabs=tabs;
        this.mTabClickBack = tabClickBack;
        renderMoudleTabs();
    }
    private MenuTab.Tab indexTab;
    private void renderMoudleTabs(){
        for (int i = 0; i < tabs.size(); i++) {
            indexTab = tabs.get(i);
            mTabTextView = new TextView(mContext);
            mTabTextView.setId(i);
            mTabTextView.setText(indexTab.getText());
            mTabTextView.setTextSize(indexTab.getTextSize());
            mTabTextView.setPadding(0, DensityUtil.dp2px(mContext, 5), 0, 0);
            mTabTextView.setTextColor(indexTab.getTextColor());
            mTabTextView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(indexTab.getTabIcon()), null, null);
            mTabTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            mTabTextView.setLayoutParams(getTabParams());
            if (indexTab.isSelected()){
                mSelIndex =i;
            }
            mTabTextView.setOnClickListener(this);
            addView(mTabTextView);
        }
    }

    public void hintMoudleTab(int... hintTab){
        if (tabs==null){
            return;
        }
        for (int hTab:hintTab) {
            if (hTab>0&&hTab<tabs.size()){
                tabs.get(hTab).setShow(false);
                getChildAt(hTab).setVisibility(GONE);
            }
        }
    }
    public void showHintMoudleTab(){
        if (tabs==null){
            return;
        }
        for (int i = 0; i <tabs.size() ; i++) {
            if (tabs.get(i).isShow){
                getChildAt(i).setVisibility(VISIBLE);
            }
        }
    }
    public void setSelMoudleIndex(int index){
        if (tabs==null||index<0||index>tabs.size()){
            return;
        }
        turnMoudleTabStatus(index);

    }
    private void turnMoudleTabStatus(int newSel){

        if (newSel!=mSelIndex){
            for (int i = 0; i < tabs.size(); i++) {
                tabs.get(i).setSelected(i==newSel);
            }
            TextView changeTextview = (TextView) getChildAt(newSel);
            changeTextview.setTextColor(tabs.get(newSel).getTextColor());
            changeTextview.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(tabs.get(newSel).getTabIcon()), null, null);
            TextView oldTextview = (TextView) getChildAt(mSelIndex);
            oldTextview.setTextColor(tabs.get(mSelIndex).getTextColor());
            oldTextview.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(tabs.get(mSelIndex).getTabIcon()), null, null);
            mSelIndex=newSel;
            mTabClickBack.onTabClick(mSelIndex);
        }

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
        if (tabs!=null&&tabs.size()>0){
            turnMoudleTabStatus(cId);
        }else {
            if (cId != mSelIndex) {
                mOldSelIndex = mSelIndex;
                mSelIndex = cId;
                turnTabStatus(mOldSelIndex, mSelIndex);
                mTabClickBack.onTabClick(mSelIndex);
            }
        }
    }

    public interface TabClickBack {
        void onTabClick(int currentIndex);
    }
}
