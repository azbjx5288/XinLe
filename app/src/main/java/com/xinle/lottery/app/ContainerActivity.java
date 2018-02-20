package com.xinle.lottery.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xinle.lottery.R;
import com.xinle.lottery.component.TintableImageView;
import com.xinle.lottery.fragment.BetOrTraceListTagFragment;
import com.xinle.lottery.fragment.FragmentHome;
import com.xinle.lottery.fragment.FragmentLotteryTrend;
import com.xinle.lottery.fragment.FragmentUser;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.umeng.analytics.MobclickAgent;

/**
 * Created on 2016/01/04.
 *
 * @author ACE
 * @功能描述: 界面容器
 */

public class ContainerActivity extends AppCompatActivity {
    private static final String TAG = ContainerActivity.class.getSimpleName();

    private ViewPager mViewPager;
    private ViewGroup tabLayout;
    private FragmentPagerItems pages = new FragmentPagerItems(this);

    {
         pages.add(FragmentPagerItem.of("FragmentHome", FragmentHome.class));
         pages.add(FragmentPagerItem.of("FragmentLotteryTrend", FragmentLotteryTrend.class));
         pages.add(FragmentPagerItem.of("BetOrTraceListTagFragment", BetOrTraceListTagFragment.class));
         pages.add(FragmentPagerItem.of("FragmentUser", FragmentUser.class));
     }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_fragment);
        initView();
    }

    private void initView() {
        tabLayout = (ViewGroup) findViewById(R.id.tagLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        tabLayout.addView(LayoutInflater.from(this).inflate(R.layout.tab_icon_notification_mark, tabLayout, false));

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), pages);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(pages.size()-1);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewPagerTab);
        viewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider(){

            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                LayoutInflater inflater = LayoutInflater.from(container.getContext());
                Resources res = container.getContext().getResources();
                View tab = inflater.inflate(R.layout.tab_icon_layout, container, false);
                View mark = tab.findViewById(R.id.custom_tab_notification_mark);
                mark.setVisibility(View.GONE);
                TintableImageView icon = tab.findViewById(R.id.custom_tab_icon);
                TextView text=tab.findViewById(R.id.custom_tab_text);
                switch (position) {
                    case 0:
                        icon.setImageDrawable(res.getDrawable(R.drawable.ic_tab_home));
                        text.setText("购彩大厅");
                        break;
                    case 1:
                        icon.setImageDrawable(res.getDrawable(R.drawable.ic_tab_classify));
                        text.setText("开奖走势");
                        break;
                    case 2:
                        icon.setImageDrawable(res.getDrawable(R.drawable.ic_tab_discover));
                        text.setText("游戏记录");
                        break;
                    case 3:
                        icon.setImageDrawable(res.getDrawable(R.drawable.ic_tab_me));
                        text.setText("帐号中心");
                        break;
                    default:
                        throw new IllegalStateException("Invalid position: " + position);
                }
                return tab;
            }
        });

        viewPagerTab.setViewPager(mViewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"onPause");
        MobclickAgent.onPause(this);
    }

}
