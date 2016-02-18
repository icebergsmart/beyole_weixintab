package com.beyole.weixintab;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.beyole.view.ChangeColorIconView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;

public class MainActivity extends FragmentActivity implements OnPageChangeListener, OnClickListener {

	private ViewPager mViewPager;
	private List<Fragment> mTabs = new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;
	private String[] mTitles = new String[] { "First Fragment", "Second Fragment", "Third Fragment", "Fourth Fragment" };
	private List<ChangeColorIconView> mTabIndicator = new ArrayList<ChangeColorIconView>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setOverFlowShowingAlways();
		getActionBar().setDisplayShowHomeEnabled(false);
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		initDatas();
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
	}

	private void initDatas() {

		for (String title : mTitles) {
			TabFragment fragment = new TabFragment();
			Bundle args = new Bundle();
			args.putString("title", title);
			fragment.setArguments(args);
			mTabs.add(fragment);
		}
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return mTabs.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return mTabs.get(arg0);
			}
		};
		initTabIndicator();
	}

	private void initTabIndicator() {
		ChangeColorIconView one = (ChangeColorIconView) findViewById(R.id.id_indicator_one);
		ChangeColorIconView two = (ChangeColorIconView) findViewById(R.id.id_indicator_two);
		ChangeColorIconView three = (ChangeColorIconView) findViewById(R.id.id_indicator_three);
		ChangeColorIconView four = (ChangeColorIconView) findViewById(R.id.id_indicator_four);
		mTabIndicator.add(one);
		mTabIndicator.add(two);
		mTabIndicator.add(three);
		mTabIndicator.add(four);
		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);
		one.setIconAlpha(1.0f);
	}

	private void setOverFlowShowingAlways() {
		try {
			// true if a permanent menu key is present, false otherwise.
			ViewConfiguration configuration = ViewConfiguration.get(this);
			Field declaredField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			declaredField.setAccessible(true);
			declaredField.setBoolean(configuration, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		resetOtherMenus();
		switch (v.getId()) {
		case R.id.id_indicator_one:
			mTabIndicator.get(0).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(0, false);
			break;
		case R.id.id_indicator_two:
			mTabIndicator.get(1).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(1, false);
			break;
		case R.id.id_indicator_three:
			mTabIndicator.get(2).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(2, false);
			break;
		case R.id.id_indicator_four:
			mTabIndicator.get(3).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(3, false);
			break;
		}
	}

	private void resetOtherMenus() {
		for (int i = 0; i < mTabIndicator.size(); i++) {
			mTabIndicator.get(i).setIconAlpha(0);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		if (positionOffset > 0) {
			ChangeColorIconView left = mTabIndicator.get(position);
			ChangeColorIconView right = mTabIndicator.get(position + 1);
			left.setIconAlpha(1 - positionOffset);
			right.setIconAlpha(positionOffset);
		}
	}

	@Override
	public void onPageSelected(int arg0) {

	}
}
