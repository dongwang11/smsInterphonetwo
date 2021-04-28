package com.sms.app.interphone.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sms.app.interphone.R;
import com.sms.app.interphone.adapter.WelcomeViewpageAdapter;


public class WelcomeViewpageActivity extends BaseActivity implements ViewPager.OnPageChangeListener{
	 private WelcomeViewpageAdapter adapter; // viewpage适配器
	    private ViewPager pager;  //viewPager对象
	    private List<View> views; // 视图数据

	    private ImageView[] point; // 底部小圆点
	    private int currentId = 0; // 当前ID
	    private ImageView btnExperience;  // 体验按钮
	    private LinearLayout ll_btn_welcome_experience;

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.setContentView(R.layout.welcome_layout);
	       // AppManager.getAppManager().addActivity(WelcomeActivity.this);
	        initViews();
	        initEvents();
	        setPoint();
	    }

	    protected void initViews() {

	        pager = (ViewPager) this.findViewById(R.id.vp_welcome);
	        views = new ArrayList<View>();

	        LayoutInflater mLi = LayoutInflater.from(this);
	        View view1 = mLi.inflate(R.layout.welcome_what_one, null);
	        View view2 = mLi.inflate(R.layout.welcome_what_two, null);
	        View view3 = mLi.inflate(R.layout.welcome_what_three, null);
	        btnExperience = (ImageView)view3.findViewById(R.id.btn_welcome_experience);
			ll_btn_welcome_experience= (LinearLayout) view3.findViewById(R.id.ll_btn_welcome_experience);

	        views.add(view1);
	        views.add(view2);
	        views.add(view3);

	        adapter = new WelcomeViewpageAdapter(WelcomeViewpageActivity.this, views);
	        pager.setAdapter(adapter);
	        pager.setOnPageChangeListener(this);

	    }

	    public void initEvents() {
	        super.initEvents();
			ll_btn_welcome_experience.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                startActivity(new Intent(WelcomeViewpageActivity.this, SilpLoginActivity.class));
	                finish();
	            }
	        });
	    }

	    @Override
	    public void onPageScrolled(int i, float v, int i2) {

	    }

	    @Override
	    public void onPageSelected(int i) {
	        currentId = i;
	        setPoint();
	    }

	    @Override
	    public void onPageScrollStateChanged(int i) {

	    }

	    /**
	     * 设置小圆点
	     */
	    private void setPoint() {
	        LinearLayout linear = (LinearLayout) this.findViewById(R.id.ll_point_welcome);
	        point = new ImageView[linear.getChildCount()];
	        for (int i = 0; i < linear.getChildCount(); i++) {
	            if (currentId == i) {
	                point[i] = (ImageView) linear.getChildAt(i);
	                point[i].setImageResource(R.drawable.welcome_point_pressed);
	            } else {
	                point[i] = (ImageView) linear.getChildAt(i);
	                point[i].setImageResource(R.drawable.welcome_point_normal);
	            }
	        }
	    }

	}
