package com.json.basewebview.Base;


import android.app.Activity;

import java.util.Stack;

/**
 * 控制activity的管理器
 */

public class ActManger {
    private static Stack<BaseAct> activityStack;
    private static ActManger instance;

    private ActManger() {
    }

    public static ActManger getInstance() {
        if (instance == null) {
            instance = new ActManger();
        }
        return instance;
    }

    //退出栈顶Activity
    public void removeAct(BaseAct activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    //获得当前栈顶Activity,必须保证栈顶有数据
    public BaseAct currentAct() {
        if (activityStack != null && activityStack.size() > 0) {
            return activityStack.lastElement();
        }
        return null;
    }

    //堆栈中是否有指定activity的类型
    public boolean hasTagClass(Class clazz) {
        if (activityStack != null && activityStack.size() > 1) {//最少为2
            int size = activityStack.size();
            BaseAct mAct = null;
            for (int i = 0; i <size-1; i++) {
                mAct = activityStack.get(i);
                if (mAct != null && mAct.getClass().equals(clazz)) {
                    return true;
                }
            }
        }
        return false;
    }

    //将当前Activity推入栈中
    public void pushAct(BaseAct activity) {
        if (activityStack == null) {
            activityStack = new Stack<BaseAct>();
        }
        activityStack.add(activity);
    }

    //退出栈中所有Activity除了指定
    public void removeAllActivityExceptOne(Class cls) {
        if (activityStack != null && activityStack.size() > 0) {
            int size = activityStack.size();
            BaseAct activity = null;
            for (int i = size - 1; i >= 0; i--) {
                activity = activityStack.get(i);
                if (activity == null) {
                    return;
                }
                if (activity.getClass().equals(cls)) {
                    continue;
                }
                removeAct(activity);
            }
        }
    }

    //退出所有activiy
    public void removeAllAct() {
        removeAllActivityExceptOne(null);
    }
}
