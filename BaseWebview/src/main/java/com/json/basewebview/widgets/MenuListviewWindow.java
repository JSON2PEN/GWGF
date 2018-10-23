package com.json.basewebview.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.json.basewebview.R;
import com.json.basewebview.bean.MenuShowBean;

import java.util.List;

/**
 * 菜单弹窗popwindow
 */

public class MenuListviewWindow {
    public static final int HOME = 1;
    public static final int SEARCH = 2;
    public static final int CARE = 3;
    public static final int SHARE = 4;
    public static final int NONE = 0;
    public static int careStatus =-1;
    private PopupWindow window;
    private int carePosition = -1;
    private ListView lvContent;
    private Context mActivity;

    public static MenuListviewWindow newInstance() {
        return new MenuListviewWindow();
    }

    public void showFourOption(Context mActivity, View signView, List<MenuShowBean> mData, MenuJSCode menuJSCode) {//type :1代表4个的 2代表2个带有搜索,3代表2个都带有分享
        this.mActivity = mActivity;
        showList(signView, mData, menuJSCode);
    }

    private void showList(View signView, final List<MenuShowBean> mData, final MenuJSCode menuJSCode) {
        View v1 = View.inflate(mActivity, R.layout.view_menu_recycle1, null);
        lvContent = (ListView) v1.findViewById(R.id.lv_content);
        lvContent.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mData.size();
            }

            @Override
            public Object getItem(int i) {
                return mData.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(final int i, View view, ViewGroup viewGroup) {
                ViewHolder holder = null;
                if (view == null) {
                    holder = new ViewHolder();
                    view = View.inflate(mActivity, R.layout.item_menu_button, null);
                    holder.tvItemMenu = (TextView) view.findViewById(R.id.tv_item_menu);
                    holder.flHome = (FrameLayout) view.findViewById(R.id.fl_home);
                    holder.vDivider = view.findViewById(R.id.v_divider);
                    view.setTag(holder);
                } else {
                    holder = (ViewHolder) view.getTag();
                }
                holder.flHome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        menuJSCode.codeBack(mData.get(i).click);
                    }
                });
                holder.vDivider.setVisibility(i == 0 ? View.GONE : View.VISIBLE);
                MenuShowBean menuShowBean = mData.get(i);
                holder.tvItemMenu.setText(menuShowBean.title);
                Drawable drawable = null;
                if (menuShowBean.title.equals("首页")) {
                    drawable = mActivity.getResources().getDrawable(R.drawable.pic_menu_home);
                } else if (menuShowBean.title.equals("搜索")) {
                    drawable = mActivity.getResources().getDrawable(R.drawable.pic_menu_search);
                } else if (menuShowBean.title.equals("关注")) {
                    carePosition = i;
                    if (careStatus==1){
                        drawable = mActivity.getResources().getDrawable(R.drawable.pic_menu_cared);
                    }else {
                        drawable = mActivity.getResources().getDrawable(R.drawable.pic_menu_care);
                    }
                } else if (menuShowBean.title.equals("分享")) {
                    drawable = mActivity.getResources().getDrawable(R.drawable.pic_menu_share);
                }
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.tvItemMenu.setCompoundDrawables(drawable, null, null, null);
                return view;
            }
        });
        window = new PopupWindow(v1,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setTouchable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_menu_gray));
        window.setAnimationStyle(R.style.popwindow_anim_menu);
        window.showAsDropDown(signView);
        //popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                clean();
            }
        });
    }

    public void setCareBg(int careStatus) {
        if (carePosition == -1) {
            return;
        }
        FrameLayout careLayout = (FrameLayout) lvContent.getChildAt(carePosition);
        TextView careItemMenu = (TextView) careLayout.findViewById(R.id.tv_item_menu);
        Drawable mDrawable =null;
        if (careStatus==1){//关注
            mDrawable = mActivity.getResources().getDrawable(R.drawable.pic_menu_cared);
        }else if (careStatus==0){//未关注
            mDrawable = mActivity.getResources().getDrawable(R.drawable.pic_menu_care);
        }
      if (mDrawable!=null){
          careItemMenu.setCompoundDrawables(mDrawable, null, null, null);
      }
    }

    static class ViewHolder {
        public TextView tvItemMenu;
        public FrameLayout flHome;
        public View vDivider;
    }

    public interface MenuJSCode {
        void codeBack(String code);
    }

    private void clean() {
        if (window != null) {
            window = null;
        }
    }

    public void relase() {
        if (window != null && window.isShowing()) {
            window.dismiss();
            window = null;
        }
    }

}
