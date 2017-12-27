package com.cimcitech.mginscription;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.cimcitech.mginscription.activity.DataFragment;
import com.cimcitech.mginscription.activity.RealTimeFragment;
import com.cimcitech.mginscription.activity.StatisticsFragment;
import com.cimcitech.mginscription.activity.UserFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.bottom_nav_content)
    LinearLayout bottomNavContent;
    @BindView(R.id.bottom_navigation_bar_container)
    BottomNavigationBar bottomNavigationBarContainer;

    private BottomNavigationItem annexItem;
    private BottomNavigationItem homeItem;
    private BottomNavigationItem orderItem;
    private BottomNavigationItem userItem;
    private BadgeItem badgeItem; //角标
    private RealTimeFragment realTimeFragment;
    private StatisticsFragment statisticsFragment;
    private DataFragment dataFragment;
    private UserFragment userFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initBottomNavBar();
    }

    /*初始化底部导航栏*/
    private void initBottomNavBar() {
        bottomNavigationBarContainer.setAutoHideEnabled(false);//自动隐藏
        bottomNavigationBarContainer.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBarContainer.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        bottomNavigationBarContainer.setBarBackgroundColor(R.color.white);//背景颜色
        bottomNavigationBarContainer.setInActiveColor(R.color.black);//未选中时的颜色
        bottomNavigationBarContainer.setActiveColor(R.color.colorPrimaryDark);//选中时的颜色

        homeItem = new BottomNavigationItem(R.drawable.main_bottom_button_real_time, "实时");
        annexItem = new BottomNavigationItem(R.drawable.main_bottom_button_statistics, "统计");
        orderItem = new BottomNavigationItem(R.drawable.main_bottom_button_data, "数据");
        userItem = new BottomNavigationItem(R.drawable.main_bottom_button_user, "我的");
        //badgeItem = new BadgeItem().setBackgroundColor(Color.RED).setText("99").setHideOnSelect(true);//角标
        //annexItem.setBadgeItem(badgeItem);

        bottomNavigationBarContainer.addItem(homeItem).addItem(annexItem).addItem(orderItem).addItem(userItem);
        bottomNavigationBarContainer.initialise();
        bottomNavigationBarContainer.setTabSelectedListener(this);
        setDefaultActivity();//显示默认的Activity
    }

    /*设置默认Fragment*/
    private void setDefaultActivity() {
        if (realTimeFragment == null)
            realTimeFragment = new RealTimeFragment();
        addFrag(realTimeFragment);
        /*默认显示msgFrag*/
        getSupportFragmentManager().beginTransaction().show(realTimeFragment).commit();
    }

    /*添加Frag*/
    private void addFrag(Fragment frag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (frag != null && !frag.isAdded())
            ft.add(R.id.bottom_nav_content, frag);
        ft.commit();
    }

    /*隐藏所有fragment*/
    private void hideAllFrag() {
        hideFrag(realTimeFragment);
        hideFrag(statisticsFragment);
        hideFrag(dataFragment);
        hideFrag(userFragment);
    }

    /*隐藏frag*/
    private void hideFrag(Fragment frag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (frag != null && frag.isAdded())
            ft.hide(frag);
        ft.commit();
    }

    @Override
    public void onTabSelected(int position) {
        hideAllFrag();//先隐藏所有frag
        switch (position) {
            case 0:
                if (realTimeFragment == null)
                    realTimeFragment = new RealTimeFragment();
                addFrag(realTimeFragment);
                getSupportFragmentManager().beginTransaction().show(realTimeFragment).commit();
                //getSupportActionBar().setTitle("消息");
                break;
            case 1:
                if (statisticsFragment == null)
                    statisticsFragment = new StatisticsFragment();
                addFrag(statisticsFragment);
                getSupportFragmentManager().beginTransaction().show(statisticsFragment).commit();
                //getSupportActionBar().setTitle("任务");
                break;
            case 2:
                if (dataFragment == null)
                    dataFragment = new DataFragment();
                addFrag(dataFragment);
                getSupportFragmentManager().beginTransaction().show(dataFragment).commit();
                //getSupportActionBar().setTitle("公告");
                break;
            case 3:
                if (userFragment == null)
                    userFragment = new UserFragment();
                addFrag(userFragment);
                getSupportFragmentManager().beginTransaction().show(userFragment).commit();
                //getSupportActionBar().setTitle("公告");
                break;
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
