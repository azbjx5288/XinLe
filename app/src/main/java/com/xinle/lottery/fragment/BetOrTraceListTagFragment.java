package com.xinle.lottery.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.xinle.lottery.material.RecordType;
import com.xinle.lottery.view.adapter.GameHistoryAdapter;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import cn.qqtheme.framework.picker.DoublePicker;
import in.srain.cube.util.LocalDisplay;
import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by ACE-PC on 2016/7/26.
 */
public class BetOrTraceListTagFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = BetOrTraceListTagFragment.class.getSimpleName();

    @BindView(R.id.lotteryButton)
    TextView lotteryButton;
    @BindView(R.id.stateButton)
    TextView stateButton;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.empty)
    TextView empty;
    @BindView(R.id.refreshLayout)
    PtrClassicFrameLayout refreshLayout;
    @BindView(R.id.loadMoreContainer)
    LoadMoreListViewContainer loadMoreContainer;

    private static final int BET_LIST_COMMAND = 1;//请求状态ID 投注记录
    private static final int TRACE_LIST_COMMAND = 2;//请求状态ID 追号记录
    private static final int LOTTERY_TRACE_ID = 3; //请求状态ID 彩种列表
    private static final int RECORD_TYPE_SELECT = 0; //记录类型
    private static final int FIRST_PAGE = 1;  //默认页数
    private int currentLotteryQueryConditionType = -1; //当前彩种ID
    private int currentRecordQueryConditionType = RECORD_TYPE_SELECT;    //0 投注记录 1 追号记录
    private int page = FIRST_PAGE;         //记录页数
    private boolean isLoading = false;     //加载状态

    private Lottery initLottery = null;
    private GameHistoryAdapter adapter;    //记录布局器
    private List items = new ArrayList();    //记录查询数据
    private static ArrayList<String> firstData = new ArrayList<>();
    private static ArrayList<String> secondData = new ArrayList<>(Arrays.asList("投注记录", "追号记录"));

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflateView(inflater, container, false, "游戏记录", R.layout.fragment_bet_or_trace_list, true, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lotteryButton.setOnClickListener(this);
        stateButton.setOnClickListener(this);

        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshLayout.setVisibility(View.VISIBLE);
                refreshLayout.autoRefresh();
            }
        });

        // 为listview的创建一个headerview,注意，如果不加会影响到加载的footview的显示！
        View headerMarginView = new View(getContext());
        headerMarginView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LocalDisplay.dp2px(20)));
        listView.addHeaderView(headerMarginView);

        adapter = new GameHistoryAdapter(getContext(), this);
        listView.setAdapter(adapter);
        refreshLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, listView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = FIRST_PAGE;
                        autoRefresh(page);
                    }
                }, 300);
            }
        });
        // the following are default settings
        refreshLayout.autoRefresh(true);
        refreshLayout.setResistance(1.7f);
        refreshLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        refreshLayout.setDurationToClose(200);
        refreshLayout.setDurationToCloseHeader(1000);
        refreshLayout.setLoadingMinTime(1000);
        refreshLayout.setPullToRefresh(true);
        refreshLayout.setLastUpdateTimeRelateObject(this);
        refreshLayout.setKeepHeaderWhenRefresh(true);

        loadMoreContainer.setAutoLoadMore(true);//设置是否自动加载更多
        loadMoreContainer.useDefaultHeader();
        //5.添加加载更多的事件监听
        loadMoreContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer moreContainer) {
                //模拟加载更多的业务处理
                loadMoreContainer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = page + 1;
                        autoRefresh(page);
                    }
                }, 300);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initData() {
        firstData.clear();
        listView.setSelection(0);
        listView.smoothScrollToPosition(0);
        if (getLotteryModel().getLotteryInfos() != null && getLotteryModel().getLotteryInfos().size() > 0) {
            InitData();
            defaultChoice();
        } else {
            lotteryListLoad();
        }
    }

    /**
     * 初使化数据
     */
    private void InitData() {
        for (int i = 0; i < getLotteryModel().getLotteryInfos().size(); i++) {
            Lottery l = getLotteryModel().getLotteryInfo(i).lottery;
            if (l.getGameType() == 1) {
                firstData.add(l.getName());
            }
        }
    }

    /**
     * 初始化显示彩种
     */
    private void defaultChoice() {
        if (getLotteryModel().getLotteryInfos().size() == 0) {
            return;
        }
        initLottery = getLotteryModel().getLotteryInfo(0).lottery;
        if (initLottery != null) {
            currentLotteryQueryConditionType = initLottery.getId();
            lotteryButton.setText(initLottery.getName());
            stateButton.setText(secondData.get(currentRecordQueryConditionType));
            refreshLayout.autoRefresh(true);
        }
    }

    private void autoRefresh(int page) {
        switch (currentRecordQueryConditionType) {
            case 0:
                loadBetList(page);
                break;
            case 1:
                loadTraceList(page);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        InitData();
        onLotteryPicker();
    }

    /**
     * 显示选择
     */
    private void onLotteryPicker() {
        final DoublePicker picker = new DoublePicker(getActivity(), firstData, secondData);
        picker.setDividerVisible(true);
        picker.setCycleDisable(true);
        picker.setSelectedIndex(0, currentRecordQueryConditionType);
        picker.setFirstLabel(null, null);
        picker.setSecondLabel(null, null);
        picker.setTextSize(14);
        picker.setTextColor(getResources().getColor(R.color.app_main));
        picker.setDividerColor(getResources().getColor(R.color.app_main));
        picker.setContentPadding(15, 10);
        picker.setOnPickListener(new DoublePicker.OnPickListener() {
            @Override
            public void onPicked(int selectedFirstIndex, int selectedSecondIndex) {
                if (firstData.size() > 0 && secondData.size() > 0) {
                    showToast(firstData.get(selectedFirstIndex) + " " + secondData.get(selectedSecondIndex));
                    Lottery currentLottery = getLotteryModel().getLotteryInfo(selectedFirstIndex).lottery;
                    lotteryButton.setText(currentLottery.getName());
                    stateButton.setText(secondData.get(selectedSecondIndex));
                    currentLotteryQueryConditionType = currentLottery.getId();
                    currentRecordQueryConditionType = selectedSecondIndex;
                    if (refreshLayout != null) {
                        refreshLayout.autoRefresh();
                    }
                }
            }
        });
        picker.show();
    }

    private void loadBetList(int page) {
        if (isLoading) {
            return;
        }
        BetListCommand command = new BetListCommand();
        command.setLottery_id(currentLotteryQueryConditionType);
        command.setPage(page);
        command.setToken(XinLeApp.getUserCentre().getUserInfo().getToken());
        TypeToken typeToken = new TypeToken<RestResponse<ArrayList<Bet>>>() {
        };
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), command, typeToken, restCallback, BET_LIST_COMMAND, this);
        restRequest.execute();
    }

    private void loadTraceList(int page) {
        if (isLoading) {
            return;
        }
        TraceListCommand command = new TraceListCommand();
        command.setLottery_id(currentLotteryQueryConditionType);
        command.setPage(page);
        command.setToken(XinLeApp.getUserCentre().getUserInfo().getToken());
        TypeToken typeToken = new TypeToken<RestResponse<ArrayList<Trace>>>() {
        };
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), command, typeToken, restCallback, TRACE_LIST_COMMAND, this);
        restRequest.execute();
    }

    private void lotteryListLoad() {
        LotteryListCommand lotteryListCommand = new LotteryListCommand();
        lotteryListCommand.setToken(XinLeApp.getUserCentre().getUserInfo().getToken());
        TypeToken typeToken = new TypeToken<RestResponse<ArrayList<Lottery>>>() {
        };
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), lotteryListCommand, typeToken, restCallback, LOTTERY_TRACE_ID, this);
        restRequest.execute();
    }

    private RestCallback restCallback = new RestCallback() {
        @Override
        public boolean onRestComplete(RestRequest request, RestResponse response) {
            switch (request.getId()) {
                case LOTTERY_TRACE_ID:
                    List<Lottery> lotterys = (ArrayList<Lottery>) response.getData();
                    getLotteryModel().setLotteryList(lotterys);
                    InitData();
                    defaultChoice();
                    break;
                case BET_LIST_COMMAND:
                case TRACE_LIST_COMMAND:
                    if (response.getData() == null) {
                        items.clear();
                    } else {
                        if (page == FIRST_PAGE) {
                            items.clear();
                        }
                        List data = (ArrayList) response.getData();
                        items.addAll(data);
                        adapter.setData(items);
                        if (data.size() == 0 || items.size() >= 100) {
                            loadMoreContainer.loadMoreFinish(false, false);
                        } else {
                            loadMoreContainer.loadMoreFinish(false, true);
                        }
                        if (adapter.getCount() > 0) {
                            empty.setVisibility(View.GONE);
                        } else {
                            empty.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
            }
            return true;
        }

        @Override
        public boolean onRestError(RestRequest request, int errCode, String errDesc) {
            if (errCode == 3004 || errCode == 2016) {
                signOutDialog(getActivity(), errCode);
                return true;
            }
            return false;
        }

        @Override
        public void onRestStateChanged(RestRequest request, @RestRequest.RestState int state) {
            if (request.getId() == BET_LIST_COMMAND || request.getId() == TRACE_LIST_COMMAND) {
                isLoading = state == RestRequest.RUNNING;
                refreshLayout.refreshComplete();
            }
        }
    };

    private RecordType getLotteryModel() {
        return RecordType.get(getActivity(), "lottery_model_history");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
