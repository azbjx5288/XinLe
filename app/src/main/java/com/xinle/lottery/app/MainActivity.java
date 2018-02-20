package com.xinle.lottery.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.xinle.lottery.BuildConfig;
import com.xinle.lottery.R;
import com.xinle.lottery.base.Preferences;
import com.xinle.lottery.base.net.RestRequestManager;
import com.xinle.lottery.fragment.GoldenLogin;
import com.xinle.lottery.util.WindowUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alashi on 2016/1/12.
 */
public class MainActivity extends AppCompatActivity
{
    
    @BindView(R.id.count_down)
    TextView countDown;
    
    private static final int REQUEST_CODE = 1001;
    /**
     * 在cache与BuildConfig.VERSION_CODE版本不一致时，需要重新登录
     */
    private static Boolean isSameVersion;
    
    private CountDownTimer countDownTimer;
    private int count = 3;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        WindowUtils.hideSystemUI(this);
        // 判断是否是第一次开启应用  如果是第一次启动，则先进入功能引导页
        /*boolean isFirstOpen = SharedPreferencesUtils.getBoolean(this, SharedPreferencesUtils.FIRST_OPEN, true);
        if (isFirstOpen) {
            startActivityForResult(new Intent(this, WelcomeGuideActivity.class), REQUEST_CODE);
            finish();
            return;
        }*/
        
        //countDown.setTextColor(ContextCompat.getColor(this, R.color.white));
        countDown.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                skip();
            }
        });
        countDownTimer = new CountDownTimer(4000, 1000)
        {
            @Override
            public void onTick(long l)
            {
                countDown.setText("跳过" + count--);
            }
            
            @Override
            public void onFinish()
            {
                skip();
            }
        };
        countDownTimer.start();
    }
    
    /**
     * 屏蔽物理返回按钮
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    private void skip()
    {
        if (countDownTimer != null)
            countDownTimer.cancel();
        if (!isInSameVersion())
        {
            XinLeApp.getUserCentre().logout();
            RestRequestManager.cancelAll();
            Preferences.saveInt(this, "app-version-code", BuildConfig.VERSION_CODE);
            isSameVersion = true;
        }
        
        if (XinLeApp.getUserCentre().isLogin())
        {
            startActivityForResult(new Intent(MainActivity.this, ContainerActivity.class), REQUEST_CODE);
        } else
        {
            FragmentLauncher.launchForResult(MainActivity.this, GoldenLogin.class.getName(), null, REQUEST_CODE);
        }
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    
    @Override
    protected void onPause()
    {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        finish();
    }
    
    public boolean isInSameVersion()
    {
        if (isSameVersion == null)
        {
            isSameVersion = Preferences.getInt(this, "app-version-code", 0) == BuildConfig.VERSION_CODE;
        }
        return isSameVersion;
    }
}
