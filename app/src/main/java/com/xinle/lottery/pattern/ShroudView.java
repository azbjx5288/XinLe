package com.xinle.lottery.pattern;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xinle.lottery.R;
import com.xinle.lottery.app.XinLeApp;
import com.xinle.lottery.component.DiscreteSeekBar;
import com.xinle.lottery.component.FlowRadioGroup;
import com.xinle.lottery.component.QuantityView;
import com.xinle.lottery.component.QuantityView.OnQuantityChangeListener;
import com.xinle.lottery.data.UserInfo;
import com.xinle.lottery.material.ShoppingCart;

/**
 * 购物车多倍操作
 * Created by ACE-PC on 2016/2/5.
 */
public class ShroudView {

    private static final String TAG = ShroudView.class.getSimpleName();
    private QuantityView doubleText;    //倍数
    private QuantityView chaseAddText;  // 追号
    private FlowRadioGroup viewGroup;     //无角分操作
    private CheckBox appendSettings;
    private OnModeItemClickListener modeItemListener;
    private DiscreteSeekBar discreteSeekBar1;
    private TextView rebateset;
    private TextView bonusset;
    private UserInfo userInfo;

    public ShroudView(View view) {
        this.userInfo = XinLeApp.getUserCentre().getUserInfo();
        doubleText = (QuantityView) view.findViewById(R.id.double_number_view);
        chaseAddText = (QuantityView) view.findViewById(R.id.chaseadd_number_view);
        viewGroup = (FlowRadioGroup) view.findViewById(R.id.lucremode_sett);
        appendSettings = (CheckBox) view.findViewById(R.id.shopping_append_settings);
        discreteSeekBar1 = (DiscreteSeekBar) view.findViewById(R.id.discrete1);
        rebateset = (TextView) view.findViewById(R.id.rebate_text);
        bonusset = (TextView) view.findViewById(R.id.bonus_text);
        doubleText.setMinQuantity(1);
        doubleText.setMaxQuantity(1000);
        doubleText.setQuantity(ShoppingCart.getInstance().getMultiple());
        chaseAddText.setMinQuantity(0);
        chaseAddText.setQuantity(ShoppingCart.getInstance().getTraceNumber());

        doubleText.setOnQuantityChangeListener(new OnQuantityChangeListener() {

            @Override
            public void onQuantityChanged(int newQuantity, boolean programmatically) {
                modeItemListener.onModeItemClick(newQuantity, chaseAddText.getQuantity(), setLucreMode(XinLeApp.getUserCentre().getLucreMode()), appendSettings.isChecked());
            }

            @Override
            public void onLimitReached() {

            }
        });


        chaseAddText.setOnQuantityChangeListener(new OnQuantityChangeListener() {

            @Override
            public void onQuantityChanged(int newQuantity, boolean programmatically) {
                modeItemListener.onModeItemClick(doubleText.getQuantity(), newQuantity, setLucreMode(XinLeApp.getUserCentre().getLucreMode()), appendSettings.isChecked());
            }

            @Override
            public void onLimitReached() {

            }
        });

        appendSettings.setOnCheckedChangeListener((arg0, arg1) -> {
            modeItemListener.onModeItemClick(doubleText.getQuantity(), chaseAddText.getQuantity(), setLucreMode(XinLeApp.getUserCentre().getLucreMode()), arg1);
        });
        viewGroup.setItemChecked(XinLeApp.getUserCentre().getLucreMode());
        viewGroup.setOnCheckChangedListener((FlowRadioGroup group, int position, boolean checked) -> {
            viewGroup.setItemChecked(position);
            modeItemListener.onModeItemClick(doubleText.getQuantity(), chaseAddText.getQuantity(), setLucreMode(position), appendSettings.isChecked());
        });

        discreteSeekBar1.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                double rebate = (double) (userInfo.getPrizeGroup() - value) / 20;
                rebateset.setText(String.format("%.2f%%", rebate));
                bonusset.setText(String.format("%d", value));
                XinLeApp.getUserCentre().setPrizeGroup(value);
                if (modeItemListener != null)
                    modeItemListener.onModeItemClick(doubleText.getQuantity(), chaseAddText.getQuantity(), setLucreMode(XinLeApp.getUserCentre().getLucreMode()), appendSettings.isChecked());
                return value;
            }
        });

    }

    public void setModeItemListener(OnModeItemClickListener modeItemListener) {
        this.modeItemListener = modeItemListener;
    }

    private int setLucreMode(int position) {
        XinLeApp.getUserCentre().setLucreMode(position);
        return position;
    }

    /**
     * 设置追号最大值
     *
     * @param num
     */
    public void setChaseTrace(int num) {
        chaseAddText.setMaxQuantity(num);
    }

    /**
     * 设置返点
     *
     * @param min
     * @param max
     */
    public void setRebate(int min, int max) {
        if (min < 0 && max < 0) {
            return;
        }
        discreteSeekBar1.setMin(min);
        discreteSeekBar1.setMax(max);
        discreteSeekBar1.setProgress(max);
    }

    public void getRebate() {
        discreteSeekBar1.getProgress();
    }


    /**
     * 选中监听器
     */
    public interface OnModeItemClickListener {
        void onModeItemClick(int multiple, int chaseadd, int lucreMode, boolean appendSet);
    }
}
