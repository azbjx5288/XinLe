package com.xinle.lottery.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xinle.lottery.R;
import com.xinle.lottery.app.BaseFragment;
import com.xinle.lottery.app.XinLeApp;
import com.xinle.lottery.base.net.RestCallback;
import com.xinle.lottery.base.net.RestRequest;
import com.xinle.lottery.base.net.RestRequestManager;
import com.xinle.lottery.base.net.RestResponse;
import com.xinle.lottery.data.LotteriesHistory;
import com.xinle.lottery.data.LotteriesHistoryCommand;
import com.xinle.lottery.data.Lottery;
import com.xinle.lottery.data.LotteryListCommand;
import com.xinle.lottery.material.RecordType;
import com.xinle.lottery.view.adapter.HistoryLotteryAdapter;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created on 2016/1/19.
 *
 * @author ACE
 * @功能描述: 历史开奖
 */
public class FragmentLotteryTrend extends BaseFragment {
    private static final String TAG = FragmentLotteryTrend.class.getSimpleName();

    private static final int LOTTERY_HISTORY_TRACE_ID = 1;
    private static final int LOTTERY_TRACE_ID = 2;

    @BindView(R.id.history_listview)
    ListView listView;
    @BindView(R.id.refresh_history)
    PtrClassicFrameLayout refreshLayout;

    private List<LotteriesHistory> historyList;
    private HistoryLotteryAdapter historyLotteryAdapter;
    private ArrayList<Lottery> lotteryList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflateView(inflater, container, false, "开奖走势", R.layout.fragment_lotterytrend, true, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        historyLotteryAdapter = new HistoryLotteryAdapter();
        historyLotteryAdapter.setClickListener((int position, String cname, boolean flag) -> {
            if (lotteryList != null && lotteryList.size() > 0) {
                Lottery lottery = null;
                for (Lottery l : lotteryList) {
                    if (l.getName().equals(cname))
                        lottery = l;
                }
                if (lottery != null) {
                    if (flag) {
                        GameFragment.launch(FragmentLotteryTrend.this, lottery);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("lottery", lottery);
                        launchFragment(HistoryCodeFragment.class, bundle);
                    }
                }
            }
        });
        listView.setAdapter(historyLotteryAdapter);
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
                        LotteriesHistoryLoad();
                    }
                }, 300);
            }

        });
        // the following are default settings
        refreshLayout.autoRefresh(true);
        refreshLayout.setResistance(1.7f);
        refreshLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        refreshLayout.setDurationToClose(200); //返回到刷新的位置
        refreshLayout.setDurationToCloseHeader(1000); //关闭头部的时间
        refreshLayout.setLoadingMinTime(1000);
        refreshLayout.setPullToRefresh(false);//当下拉到一定距离时，自动刷新（true），显示释放以刷新（false）
        refreshLayout.setLastUpdateTimeRelateObject(this);
        refreshLayout.setKeepHeaderWhenRefresh(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        onInit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * 初始化显示彩种
     */
    private void onInit() {
        lotteryList.clear();
        if (getLotteryModel().getLotteryInfos() != null && getLotteryModel().getLotteryInfos().size() > 0) {
            for (int i = 0; i < getLotteryModel().getLotteryInfos().size(); i++) {
                Lottery l = getLotteryModel().getLotteryInfo(i).lottery;
                if (l.getGameType() == 1) {
                    lotteryList.add(l);
                }
            }
            refreshLayout.autoRefresh(true);
        } else {
            lotteryListLoad();
        }
    }

    private void LotteriesHistoryLoad() {
        LotteriesHistoryCommand lotteryListCommand = new LotteriesHistoryCommand();
        lotteryListCommand.setToken(XinLeApp.getUserCentre().getUserInfo().getToken());
        TypeToken typeToken = new TypeToken<RestResponse<List<LotteriesHistory>>>() {
        };
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), lotteryListCommand, typeToken, restCallback, LOTTERY_HISTORY_TRACE_ID, this);
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
                case LOTTERY_HISTORY_TRACE_ID:
                    historyList = (List<LotteriesHistory>) response.getData();
                    historyLotteryAdapter.refresh(historyList);
                    break;
                case LOTTERY_TRACE_ID:
                    List<Lottery> list = (ArrayList<Lottery>) response.getData();
                    getLotteryModel().setLotteryList(list);
                    for (int i = 0; i < list.size(); i++) {
                        Lottery l = list.get(i);
                        if (l.getGameType() == 1) {
                            lotteryList.add(l);
                        }
                    }
                    refreshLayout.autoRefresh(true);
                    break;
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
            refreshLayout.refreshComplete();
        }
    };
    private RecordType getLotteryModel() {
        return RecordType.get(getActivity(), "lottery_model_history");
    }
}