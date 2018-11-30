package com.jone.gwgfproject.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.jone.gwgfproject.Acts.MainActivity;
import com.jone.gwgfproject.R;
import com.json.basewebview.Web.WebAct;

import java.util.List;

public class GuideAdapter extends PagerAdapter implements View.OnClickListener {
        private Activity mAct;
        private int[] imgArray;
        private Button btnEnter;

        public GuideAdapter(Activity mAct,int[] imgArray) {
            this.imgArray = imgArray;
            this.mAct = mAct;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(mAct, R.layout.item_guide, null);
            ImageView ivGuide=view.findViewById(R.id.iv_guide);
            ivGuide.setImageResource(imgArray[position]);
            btnEnter = view.findViewById(R.id.btn_enter);
            if (position == imgArray.length-1) {
                btnEnter.setVisibility(View.VISIBLE);
                btnEnter.setOnClickListener(this);
            }else {
                btnEnter.setVisibility(View.INVISIBLE);
            }
            container.addView(view);
            return view;

        }

        @Override
        public int getCount() {
            return imgArray.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void onClick(View v) {
            Intent intent =new Intent(mAct, MainActivity.class);
            intent.putExtra(WebAct.FULL_URL,"http://gdnh.cytx360.com/Customer/Customer");
            mAct.startActivity(intent);
            mAct.finish();
            mAct=null;
        }
}
