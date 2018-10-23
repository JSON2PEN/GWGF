package com.jone.gwgfproject.Acts;

import android.widget.TextView;

import com.jone.gwgfproject.R;
import com.json.basewebview.Base.BaseAct;
import com.json.basewebview.Web.NoTitle.JhjInterface;
import com.json.basewebview.Web.NoTitle.NoTitleWebFrag;
import com.json.basewebview.Web.Title.TitleWebFrag;
import com.json.basewebview.bean.ShareBean;

/**
 * Created by user on 2018/10/16.
 */

public class OwnAct extends BaseAct  {

    public TitleWebFrag mFrag;

    @Override
    protected int getResID() {
        return R.layout.act_own;
    }

    @Override
    protected boolean getIsDropBG() {
        return true;
    }

    @Override
    protected boolean getIsRegister() {
        return false;
    }

    @Override
    protected void init() {
        TextView tvAdd=findViewById(R.id.tv_add);
        tvAdd.setOnClickListener(v->{
            NoTitleWebFrag mNoTitle=new  NoTitleWebFrag();
            mNoTitle.setParams("http://gdnh.cytx360.com/Audit/Audit");
            getSupportFragmentManager().beginTransaction().add(R.id.fl_act_root, mNoTitle).addToBackStack(null).commitAllowingStateLoss();
        });
        mFrag = new TitleWebFrag();

        mFrag.setParams("http://gdnh.cytx360.com/Customer/Customer");

        getSupportFragmentManager().beginTransaction().add(R.id.fl_act_root, mFrag).commitAllowingStateLoss();
    }

}
