package com.jone.gwgfproject;

/**
 * @author jhj
 * 该类为了作为全局配置参数使用
 */
public class AppConfigs {
    //引导界面的配置
    public static int[] getGuideImg(){
        return new int[]{R.drawable.splash,R.drawable.splash,R.drawable.splash,R.drawable.splash};
    }
    //main界面tab选中图
    public static int[] getTabSelBgs(){
        return new int[]{R.drawable.pic_financial_selected,R.drawable.pic_asset_selected,R.drawable.pic_gold_selected,R.drawable.pic_forex_selected};
    }
    //main界面tab normal图
    public static int[] getTabNorBgs(){
        return new int[]{R.drawable.pic_financial_normal,R.drawable.pic_asset_normal,R.drawable.pic_gold_normal,R.drawable.pic_forex_normal};
    }
    //main界面tab描述文字
    public static String[] getTabTexts(){
        return new String[]{"客户管理","审批","事件营销","资讯"};
    }

}
