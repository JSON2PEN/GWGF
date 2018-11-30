package com.jone.gwgfproject.Acts;

import com.jone.gwgfproject.Adapter.GuideAdapter;
import com.jone.gwgfproject.AppConfigs;
import com.jone.gwgfproject.R;
import com.jone.gwgfproject.widgets.bannerViewPager.BannerViewPager;
import com.json.basewebview.Base.BaseAct;

public class GuideAct extends BaseAct {
    @Override
    protected int getResID() {
        return R.layout.act_guide;
    }

    @Override
    protected boolean getIsDropBG() {
        return false;
    }

    @Override
    protected boolean getIsRegister() {
        return false;
    }

    @Override
    protected void init() {
        BannerViewPager bvp=findViewById(R.id.bvp);
        GuideAdapter guideAdapter =new GuideAdapter(this,AppConfigs.getGuideImg());
        bvp.setAdapter(guideAdapter);
    }
}
