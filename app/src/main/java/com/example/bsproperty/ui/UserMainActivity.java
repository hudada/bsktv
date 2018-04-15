package com.example.bsproperty.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.eventbus.LoginEvent;
import com.example.bsproperty.fragment.UserFragment01;
import com.example.bsproperty.fragment.UserFragment02;
import com.example.bsproperty.fragment.UserFragment03;
import com.example.bsproperty.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;

public class UserMainActivity extends BaseActivity {


    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.tb_bottom)
    TabLayout tbBottom;



    private long backTime;
    private UserFragment01 fragment01;
    private UserFragment02 fragment02;
    private UserFragment03 fragment03;
    private ArrayList<Fragment> fragments;
    private MyFragmentPagerAdapter adapter;
    private String[] tabs = new String[]{
            "歌单", "关注", "我的"
    };
    private int[] tabIcons = {
            R.drawable.ic_home_grey_400_24dp,
            R.drawable.ic_format_list_bulleted_grey_400_24dp,
            R.drawable.ic_person_grey_400_24dp
    };
    private int[] tabIconsPressed = {
            R.drawable.ic_home_white_24dp,
            R.drawable.ic_format_list_bulleted_white_24dp,
            R.drawable.ic_person_white_24dp
    };

    @Override
    protected void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        MyApplication.getInstance().setUserBean(SpUtils.getUserBean(this));

        fragment01 = new UserFragment01();
        fragment02 = new UserFragment02();
        fragment03 = new UserFragment03();
        fragments = new ArrayList<>();
        fragments.add(fragment01);
        fragments.add(fragment02);
        fragments.add(fragment03);


        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        vpContent.setAdapter(adapter);
        tbBottom.setTabMode(TabLayout.MODE_FIXED);
        tbBottom.setupWithViewPager(vpContent);

        for (int i = 0; i < fragments.size(); i++) {
            tbBottom.getTabAt(i).setCustomView(getTabView(i));
        }

        tbBottom.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTabSelect(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                changeTabNormal(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_nav, null);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setText(tabs[position]);
        ImageView img_title = (ImageView) view.findViewById(R.id.img_title);
        img_title.setImageResource(tabIcons[position]);

        if (position == 0) {
            txt_title.setTextColor(Color.WHITE);
            img_title.setImageResource(tabIconsPressed[position]);
        } else {
            txt_title.setTextColor(getResources().getColor(R.color.tab_nav_grey));
            img_title.setImageResource(tabIcons[position]);
        }
        return view;
    }

    private void changeTabSelect(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        ImageView img_title = (ImageView) view.findViewById(R.id.img_title);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setTextColor(Color.WHITE);
        vpContent.setCurrentItem(tbBottom.getSelectedTabPosition());
        img_title.setImageResource(tabIconsPressed[tbBottom.getSelectedTabPosition()]);
    }

    private void changeTabNormal(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        ImageView img_title = (ImageView) view.findViewById(R.id.img_title);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setTextColor(getResources().getColor(R.color.tab_nav_grey));
        img_title.setImageResource(tabIcons[tbBottom.getSelectedTabPosition()]);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



    @Override
    protected int getRootViewId() {
        return R.layout.activity_user_main;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - backTime < 2000) {
            super.onBackPressed();
        } else {
            showToast(this, "再按一次，退出程序");
            backTime = System.currentTimeMillis();
        }
        backTime = System.currentTimeMillis();
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

}
