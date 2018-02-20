package com.xinle.lottery.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.xinle.lottery.R;
import com.xinle.lottery.app.BaseFragment;
import com.xinle.lottery.app.XinLeApp;
import com.xinle.lottery.base.net.RestCallback;
import com.xinle.lottery.base.net.RestRequest;
import com.xinle.lottery.base.net.RestRequestManager;
import com.xinle.lottery.base.net.RestResponse;
import com.xinle.lottery.data.Bet;
import com.xinle.lottery.data.BetListCommand;
import com.xinle.lottery.data.Lottery;
import com.xinle.lottery.data.LotteryListCommand;
import com.xinle.lottery.data.Trace;
import com.xinle.lottery.data.TraceListCommand;
import com.xinle.lottery.view.adapter.GameHistoryAdapter;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;

public class FragmentHistory extends BaseFragment implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = FragmentHistory.class.getSimpleName();
    private static final int LOTTERY_LIST_COMMAND = 3;
    private static final int BET_LIST_COMMAND = 1;
    private static final int TRACE_LIST_COMMAND = 2;

    /**
     * 服务器分页从1开始
     */
    private static final int FIRST_PAGE = 1;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.list)
    ListView listView;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    private GameHistoryAdapter adapter;
    private List items = new ArrayList();
    private int page = FIRST_PAGE;
    private boolean isLoading;
    private boolean isFirstTime = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflateView(inflater, container, false, "游戏记录", R.layout.fragment_history,true,true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        radioGroup.setOnCheckedChangeListener(this);
        refreshLayout.setColorSchemeColors(Color.parseColor("#ff0000"), Color.parseColor("#00ff00"), Color.parseColor("#0000ff"), Color.parseColor("#f234ab"));
        refreshLayout.setOnRefreshListener(() -> {
            if (isInBetList()) {
                loadBetList(FIRST_PAGE);
            } else {
                loadTraceList(FIRST_PAGE);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        adapter = new GameHistoryAdapter(view.getContext(),this);
        listView.setAdapter(adapter);
        loadLotteryList();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (page == FIRST_PAGE) {
            if (isInBetList()) {
                loadBetList(FIRST_PAGE);
            } else {
                loadTraceList(FIRST_PAGE);
            }
        }
        isFirstTime = false;
    }

    private void loadLotteryList() {
        LotteryListCommand command = new LotteryListCommand();
        command.setToken(XinLeApp.getUserCentre().getUserInfo().getToken());
        TypeToken typeToken = new TypeToken<RestResponse<ArrayList<Lottery>>>() {
        };
        RestRequest restRequest = RestRequestManager.executeCommand(getActivity(), command, typeToken, restCallback, LOTTERY_LIST_COMMAND, this);
        RestResponse response = restRequest.getCache();
        if (response != null && response.getData() instanceof ArrayList<?>) {
            XinLeApp.getUserCentre().setLotteryList((ArrayList<Lottery>) response.getData());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        refreshLayout.setRefreshing(false);
        isLoading = false;
        items.clear();
        switch (checkedId) {
            case R.id.gameRadioButton:
                loadBetList(FIRST_PAGE);
                break;
            case R.id.traceRadioButton:
                loadTraceList(FIRST_PAGE);
                break;
        }
    }

    private void loadBetList(int page) {
        this.page = page;
        BetListCommand command = new BetListCommand();
        command.setLottery_id(1);
        command.setPage(page);
        command.setToken(XinLeApp.getUserCentre().getUserInfo().getToken());
        TypeToken typeToken = new TypeToken<RestResponse<ArrayList<Bet>>>() {
        };
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), command, typeToken, restCallback, BET_LIST_COMMAND, this);
        RestResponse restResponse = restRequest.getCache();
        if (restResponse != null && restResponse.getData() instanceof ArrayList<?>) {
            items = (ArrayList<Bet>) restResponse.getData();
            adapter.setData(items);
        }
        restRequest.execute();
    }

    private void loadTraceList(int page) {
        this.page = page;
        TraceListCommand command = new TraceListCommand();
        command.setLottery_id(1);
        command.setPage(page);
        command.setToken(XinLeApp.getUserCentre().getUserInfo().getToken());
        TypeToken typeToken = new TypeToken<RestResponse<ArrayList<Trace>>>() {
        };
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), command, typeToken, restCallback, TRACE_LIST_COMMAND, this);
        RestResponse restResponse = restRequest.getCache();
        if (restResponse != null && restResponse.getData() instanceof ArrayList<?>) {
            items=(ArrayList<Trace>) restResponse.getData();
            adapter.setData(items);
        }
        restRequest.execute();
    }

    @OnItemClick(R.id.list)
    public void onItemClick(int position) {
        if (isInBetList()) {
            BetOrTraceDetailFragment.launch(this, (Bet) adapter.getItem(position));
        } else {
            BetOrTraceDetailFragment.launch(this, (Trace) adapter.getItem(position));
        }
    }

    private boolean isInBetList() {
        return radioGroup.getCheckedRadioButtonId() == R.id.gameRadioButton;
    }

    private RestCallback restCallback = new RestCallback() {
        @Override
        public boolean onRestComplete(RestRequest request, RestResponse response) {
            switch (request.getId()) {
                case LOTTERY_LIST_COMMAND: {
                    XinLeApp.getUserCentre().setLotteryList((ArrayList<Lottery>) response.getData());
                    adapter.notifyDataSetChanged();
                    break;
                }

                case BET_LIST_COMMAND: {
                    if (response != null && response.getData() instanceof ArrayList<?>) {
                        items = (ArrayList<Bet>) response.getData();
                        adapter.setData(items);
                    }
                    break;
                }

                case TRACE_LIST_COMMAND: {
                    if (response != null && response.getData() instanceof ArrayList<?>) {
                        items=(ArrayList<Trace>) response.getData();
                        adapter.setData(items);
                    }
                    break;
                }
            }
            return true;
        }

        @Override
        public boolean onRestError(RestRequest request, int errCode, String errDesc) {
            if(errCode == 3004 || errCode == 2016){
                signOutDialog(getActivity(),errCode);
                return true;
            }
            return false;
        }

        @Override
        public void onRestStateChanged(RestRequest request, @RestRequest.RestState int state) {
            if ((request.getId() == BET_LIST_COMMAND && isInBetList()) || (request.getId() == TRACE_LIST_COMMAND && !isInBetList())) {
                refreshLayout.setRefreshing(state == RestRequest.RUNNING);
                isLoading = state == RestRequest.RUNNING;
            }
        }
    };
}