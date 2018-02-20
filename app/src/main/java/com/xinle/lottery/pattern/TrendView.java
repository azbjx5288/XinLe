package com.xinle.lottery.pattern;

import android.view.View;
import android.widget.TextView;

import com.xinle.lottery.R;
import com.xinle.lottery.view.DrawCodeView;
import com.xinle.lottery.view.DrawTrendView;

/**
 * Created by ACE-PC on 2016/3/11.
 */
public class TrendView{

    private static final String TAG = TrendView.class.getSimpleName();
    private DrawTrendView drawTrendView;
    public TrendView(View trendView,String title){
        ((TextView) trendView.findViewById(R.id.trend_column_title)).setText(title);
        drawTrendView = (DrawTrendView) trendView.findViewById(R.id.trendview);
    }

    public void setTrendData(Object trendArray){
        drawTrendView.setData(trendArray);
    }

    /**
     * 设置显示线条显示隐藏
     * @param isChecked
     */
    public void showHideLines(boolean isChecked){
        drawTrendView.setNeedLinkLine(isChecked);
    }

    public void requestLayout() {
        if (drawTrendView != null && !drawTrendView.isLayoutRequested()) {
            drawTrendView.requestLayout();
        }
    }

}
