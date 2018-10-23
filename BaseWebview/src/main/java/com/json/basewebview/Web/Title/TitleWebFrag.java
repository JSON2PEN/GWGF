package com.json.basewebview.Web.Title;

import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.json.basewebview.R;
import com.json.basewebview.Utils.MyToast;
import com.json.basewebview.Utils.SteepStatusBarUtils;
import com.json.basewebview.Utils.UpdateUI;
import com.json.basewebview.Web.NoTitle.NoTitleWebFrag;
import com.json.basewebview.bean.MenuShowBean;
import com.json.basewebview.widgets.MenuListviewWindow;

import java.util.List;

/**
 * 包含头布局的实例
 */

public class TitleWebFrag extends NoTitleWebFrag implements MenuListviewWindow.MenuJSCode{
    /**
     * 需要手动设置,0 代表默认 1代表临时修改的背景色
     */
    private int titleStyle;
    public TextView mTvTitle;
    public FrameLayout mRight;
    public ImageView mImageRight;
    private boolean showBack =false;//home页面是否展示back,默认不展示
    private boolean isSteep =false;//是否为沉浸式状态栏
    public MenuListviewWindow newPop;
    private String mSaticTitle ="";
    public ImageView mIvTitleLeft;
    private int backImg;
    private int titleBg;

    public void setParams(String loadUrl,boolean isSteep) {
        super.setParams(loadUrl);
        this.isSteep =isSteep;
    }

    @Override
    protected void initExtraView() {
        mTitleView = LayoutInflater.from(mActivity).inflate(R.layout.view_title,mFlTitle,true);
        FrameLayout mBack = mTitleView.findViewById(R.id.fl_back);
        mIvTitleLeft = mTitleView.findViewById(R.id.iv_title_left);
        mRight = mTitleView.findViewById(R.id.fl_right_icon);
        mTvTitle = mTitleView.findViewById(R.id.tv_web_title);
        mImageRight = mTitleView.findViewById(R.id.iv_right_icon);
        mTvTitle.setText(mSaticTitle);
        mBack.setOnClickListener((v)->{
            if (isMain) {
                mActivity.finish();
            } else {
                webBack();
            }
        });
        if (isSteep){
            SteepStatusBarUtils.setImmerseLayout(mFlTitle,mActivity);
        }
    }

    @Override
    public void showButton(int buttonType, String buttonClickFunction) {
        UpdateUI.doOnUI(() -> {
            switch (buttonType) {
                case 0://不显示任何
                    mRight.setVisibility(View.GONE);
                    break;
                case 1://分享按钮
                    mRight.setVisibility(View.VISIBLE);
                    mImageRight.setImageResource(R.drawable.pic_menu_share);
                    mRight.setOnClickListener(v -> loadJsCode(buttonClickFunction));
                    break;
                case 2://菜单按钮
                    mRight.setVisibility(View.VISIBLE);
                    mImageRight.setImageResource(R.drawable.pic_menu);
                    mRight.setOnClickListener(v -> showMenuButon(buttonClickFunction));
                    break;
                    default:
                        break;
            }
        });
    }

    @Override
    public void setTitleBg(String color) {
        UpdateUI.doOnUI(()->{
            if (mFlTitle!=null){
                mFlTitle.setBackgroundColor(Color.parseColor(color));
                setTitleStyle(1);
            }
        });
    }
    /**
     * 修改头部背景之后返回或跳转都回复成原有背景
     */
    @Override
    public void resetBG() {
       /* if (titleStyle == 1) {
            mFlTitle.setBackgroundColor(Color.parseColor("#ffffff"));
            titleStyle =0;
        }*/
    }

    @Override
    public void setWebTitle(String title) {
        UpdateUI.doOnUI(() -> mTvTitle.setText(title));
    }
    /**
     * 设置背景的style
     * @param titleStyle
     */
    public void setTitleStyle(int titleStyle) {
        this.titleStyle = titleStyle;
    }

    public void setShowBack(boolean showBack){
        this.showBack =showBack;
    }
    //显示菜单
    private void showMenuButon(final String buttonClickFunction) {
        List<MenuShowBean> menuShowBeen = new Gson().fromJson(buttonClickFunction, new TypeToken<List<MenuShowBean>>() {
        }.getType());
        if (menuShowBeen == null) {
            Toast.makeText(mActivity,"获取网络数据错误",Toast.LENGTH_SHORT).show();
            return;
        }
        //适应性填充
        newPop = MenuListviewWindow.newInstance();
        newPop.showFourOption(mActivity, mRight, menuShowBeen, this);
    }
    @Override
    public void codeBack(String code) {
        loadJsCode(code);
        if (newPop != null) {
            newPop.relase();
        }
    }
    public void setStaticTitle(String defaultTitle){
        this.mSaticTitle=defaultTitle;
    }
    //设置头部的背景图片
    public void setTitleBg(@DrawableRes int backImg, @ColorRes int titleBg){
        this.backImg=backImg;
        this.titleBg =titleBg;
        if (mIvTitleLeft!=null){
            mIvTitleLeft.setImageDrawable(getResources().getDrawable(backImg));
        }
        if (mFlTitle!=null){
            mFlTitle.setBackgroundColor(getResources().getColor(titleBg));
        }
    }
    @Override
    public void isMain(boolean isMain) {
        UpdateUI.doOnUI(() -> {
            /*if (isMain) {
                if (buttomView != null) {
                    buttomView.setVisibility(View.VISIBLE);
                }
                if (!showBack){
                    flBack.setVisibility(View.GONE);
                }
            } else {
                if (buttomView != null) {
                    buttomView.setVisibility(View.GONE);
                }
                if (!showBack){
                    flBack.setVisibility(View.VISIBLE);
                }
            }*/

        });
    }

}
