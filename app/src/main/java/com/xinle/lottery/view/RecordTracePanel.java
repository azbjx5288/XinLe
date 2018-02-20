package com.xinle.lottery.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.xinle.lottery.R;
import com.xinle.lottery.app.BaseFragment;
import com.xinle.lottery.app.XinLeApp;
import com.xinle.lottery.base.net.RestCallback;
import com.xinle.lottery.base.net.RestRequest;
import com.xinle.lottery.base.net.RestRequestManager;
import com.xinle.lottery.base.net.RestResponse;
import com.xinle.lottery.component.CustomDialog;
import com.xinle.lottery.data.Lottery;
import com.xinle.lottery.data.Trace;
import com.xinle.lottery.data.TraceListCommand;
import com.xinle.lottery.game.PromptManager;
import com.xinle.lottery.view.adapter.GameHistoryAdapter;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by Ace.Dong on 2017/11/17.
 */

public class RecordTracePanel extends BaseFragment implements View.OnClickListener{

    private static final int TRACE_LIST_COMMAND = 1;
    private static final int LOTTERY_SELECT = 1;//选择彩种查询
    private static final int STATE_SELECT = 2; //状态查询
    private static final int FIRST_PAGE = 1;
    @BindView(R.id.lotteryButton)
    TextView lotteryButton;
    @BindView(R.id.stateButton)
    TextView stateButton;
    @BindView(R.id.traceListView)
    ListView traceListView;
    @BindView(R.id.emptyTrace)
    TextView emptyTrace;
    @BindView(R.id.refreshTraceLayout)
    PtrClassicFrameLayout refreshTraceLayout;
    Unbinder unbinder;

    private int mCurrentQueryConditionType = -1;
    private int page = FIRST_PAGE;
    private boolean isLoading = false;

    private GameHistoryAdapter adapter;
    private List items = new ArrayList();
    private List<Lottery> lotteries = XinLeApp.getUserCentre().getLotteryList();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recordtracepanel, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lotteryButton.setOnClickListener(this);
        stateButton.setOnClickListener(this);

        emptyTrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshTraceLayout.setVisibility(View.VISIBLE);
                refreshTraceLayout.autoRefresh();
            }
        });

        adapter = new GameHistoryAdapter(getContext(), this);
        traceListView.setAdapter(adapter);

        refreshTraceLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, traceListView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        items.clear();
                        loadTraceList(FIRST_PAGE);
                    }
                }, 300);
            }
        });
        // the following are default settings
        refreshTraceLayout.autoRefresh(true);
        refreshTraceLayout.setResistance(1.7f);
        refreshTraceLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        refreshTraceLayout.setDurationToClose(200);
        refreshTraceLayout.setDurationToCloseHeader(1000);
        refreshTraceLayout.setPullToRefresh(true);
        refreshTraceLayout.setLastUpdateTimeRelateObject(this);
        refreshTraceLayout.setKeepHeaderWhenRefresh(true);

        onInit();
    }

    @Override
    public void onResume() {
        if (refreshTraceLayout != null) {
            refreshTraceLayout.autoRefresh();
        }
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 初始化显示彩种
     */
    private void onInit() {
        Lottery initLottery = lotteries.get(0);
        mCurrentQueryConditionType = initLottery.getId();
        lotteryButton.setText(initLottery.getName());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lotteryButton:
                onLotteryPicker();
                break;
            case R.id.stateButton:
                break;
        }
    }

    /**
     * 显示选择
     */
    private void onLotteryPicker() {
        String[] lotter = new String[lotteries.size()];
        for (int i = 0; i < lotteries.size(); i++) {
            lotter[i] = lotteries.get(i).getName();
        }

        OptionPicker picker = new OptionPicker(getActivity(), lotter);
        picker.setCycleDisable(true);//不禁用循环
        picker.setTopBackgroundColor(0xFFEEEEEE);
        picker.setTopHeight(30);
        picker.setTopLineColor(0xFFEE0000);
        picker.setTopLineHeight(1);
        picker.setTitleText("请选择");
        picker.setTitleTextColor(0xFF999999);
        picker.setTitleTextSize(12);
        picker.setCancelTextColor(0xFFEE0000);
        picker.setCancelTextSize(13);
        picker.setSubmitTextColor(0xFFEE0000);
        picker.setSubmitTextSize(13);
        picker.setTextColor(0xFFEE0000, 0xFF999999);
        WheelView.DividerConfig config = new WheelView.DividerConfig();
        config.setColor(0xFFEE0000);//线颜色
        config.setAlpha(140);//线透明度
        config.setRatio((float) (1.0 / 8.0));//线比率
        picker.setDividerConfig(config);
        picker.setBackgroundColor(0xFFE1E1E1);
        //picker.setSelectedItem(isChinese ? "处女座" : "Virgo");
        picker.setSelectedIndex(7);
        picker.setCanceledOnTouchOutside(true);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                showToast("index=" + index + ", item=" + item);
                lotteryButton.setText(item);
                Lottery currentLottery=lotteries.get(index);
                mCurrentQueryConditionType=currentLottery.getId();
                if (refreshTraceLayout != null) {
                    refreshTraceLayout.autoRefresh();
                }
            }
        });
        picker.show();
    }

    private void loadTraceList(int page) {
        if (isLoading) {
            return;
        }
        this.page = page;
        TraceListCommand command = new TraceListCommand();
        command.setLottery_id(mCurrentQueryConditionType);
        command.setPage(page);
        command.setToken(XinLeApp.getUserCentre().getUserInfo().getToken());
        TypeToken typeToken = new TypeToken<RestResponse<ArrayList<Trace>>>() {
        };
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), command, typeToken, restCallback, TRACE_LIST_COMMAND, this);
        RestResponse restResponse = restRequest.getCache();
        if (restResponse != null && restResponse.getData() instanceof ArrayList<?>) {
            items = (ArrayList<Trace>) restResponse.getData();
            handle();
        }
        restRequest.execute();
    }

    private void handle() {
        adapter.setData(items);
        if (items.size() > 0) {
            emptyTrace.setVisibility(View.GONE);
        } else {
            emptyTrace.setVisibility(View.VISIBLE);
        }
    }

    private RestCallback restCallback = new RestCallback() {
        @Override
        public boolean onRestComplete(RestRequest request, RestResponse response) {
            switch (request.getId()) {
                case TRACE_LIST_COMMAND:
                    items = (ArrayList<Trace>) response.getData();
                    handle();
                    break;
            }
            return true;
        }

        @Override
        public boolean onRestError(RestRequest request, int errCode, String errDesc) {
            if (errCode == 3004) {
                CustomDialog dialog = PromptManager.showCustomDialog(getActivity(), "重新登录", errDesc, "重新登录", errCode);
                dialog.setCancelable(false);
                dialog.show();
                return true;
            }
            return false;
        }

        @Override
        public void onRestStateChanged(RestRequest request, @RestRequest.RestState int state) {
            if (request.getId() == TRACE_LIST_COMMAND) {
                isLoading = state == RestRequest.RUNNING;
                refreshTraceLayout.refreshComplete();
            }
        }
    };
}
