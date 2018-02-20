package com.xinle.lottery.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xinle.lottery.R;
import com.xinle.lottery.app.BaseFragment;
import com.xinle.lottery.base.net.RestCallback;
import com.xinle.lottery.base.net.RestRequest;
import com.xinle.lottery.base.net.RestRequestManager;
import com.xinle.lottery.base.net.RestResponse;
import com.xinle.lottery.component.CalendarPopupWindows;
import com.xinle.lottery.data.WithdrawList;
import com.xinle.lottery.data.WithdrawListCommand;
import com.xinle.lottery.view.adapter.WithdrawalAdapter;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提款申请
 * Created by ACE-PC on 2016/3/17.
 */
public class WithdrawListFragment extends BaseFragment implements View.OnClickListener {
    private static final int WITHDRAW_TRACE_ID = 1;

    @BindView(R.id.refresh_withdraw_listview)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.withdraw_listview)
    ListView withdrawLV;

    @BindView(R.id.edit_startdate)
    TextView startdate;
    @BindView(R.id.edit_enddate)
    TextView enddate;

    private static final int FIRST_PAGE = 1;
    private int page = FIRST_PAGE;
    private int totalCount;
    private boolean isLoading;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private List items = new ArrayList();
    private WithdrawalAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.withdraw_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startdate.setText(df.format(new Date()).toString());
        enddate.setText(df.format(new Date()).toString());

        adapter=new WithdrawalAdapter(items);
        refreshLayout.setColorSchemeColors(Color.parseColor("#ff0000"), Color.parseColor("#00ff00"), Color.parseColor("#0000ff"), Color.parseColor("#f234ab"));
        refreshLayout.setOnRefreshListener(() -> withdrawLogLoad(true, FIRST_PAGE));

        final int endTrigger = 2; // load more content 2 items before the end
        withdrawLV.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (withdrawLV.getCount() != 0 && items.size() < totalCount && withdrawLV.getLastVisiblePosition() >= (withdrawLV.getCount() - 1) - endTrigger) {
                    withdrawLogLoad(false, page + 1);
                }
            }
        });
        View headerView = LayoutInflater.from(view.getContext()).inflate(R.layout.withdraw_list_item, null);
        withdrawLV.addHeaderView(headerView);
        withdrawLV.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        init();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        getActivity().finish();
        super.onDestroyView();
    }

    private void init(){
        refreshLayout.setRefreshing(false);
        isLoading = false;
        withdrawLogLoad(true, FIRST_PAGE);
    }

    @OnClick({R.id.edit_startdate_layout, R.id.edit_enddate_layout,R.id.find_withdraw_button})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_startdate_layout:
                CalendarPopupWindows startCalendar=new CalendarPopupWindows(getActivity(), startdate);
                startCalendar.setOnChooseDateListener((date) -> startdate.setText(date));
                break;
            case R.id.edit_enddate_layout:
                CalendarPopupWindows endCalendar=new CalendarPopupWindows(getActivity(), enddate);
                endCalendar.setOnChooseDateListener((date) -> enddate.setText(date));
                break;
            case R.id.find_withdraw_button:
                if (TextUtils.isEmpty(startdate.getText())) {
                    Toast.makeText(getActivity(), "请选择开始时间", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(enddate.getText())) {
                    Toast.makeText(getActivity(), "请选择结束时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                withdrawLogLoad(true, FIRST_PAGE);
                break;
        }
    }

    private void withdrawLogLoad(boolean withCache, int page) {
        if (isLoading) {
            return;
        }
        this.page = page;
        String startDateStr = startdate.getText().toString().trim();
        String endDateStr = enddate.getText().toString().trim();
        WithdrawListCommand command = new WithdrawListCommand();
        command.setStartDate(startDateStr.length() > 0 ? startDateStr : df.format(new Date(new Date().getTime()-24*60*60*1000)).toString());
        command.setEndDate(endDateStr.length() > 0 ? endDateStr : df.format(new Date()).toString());
        command.setCurPage(this.page);
        TypeToken typeToken = new TypeToken<RestResponse<WithdrawList>>() {};
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), command, typeToken, restCallback, WITHDRAW_TRACE_ID, this);
        if (withCache) {
            RestResponse restResponse = restRequest.getCache();
            if (restResponse != null && restResponse.getData() instanceof WithdrawList) {
                items.addAll(((WithdrawList) restResponse.getData()).getWithdraws());
                totalCount = items.size();
                adapter.setData(items);
            }else{
                adapter.setData(null);
            }
        }
        restRequest.execute();
    }

    private RestCallback restCallback = new RestCallback() {
        @Override
        public boolean onRestComplete(RestRequest request, RestResponse response) {
            if (request.getId() == WITHDRAW_TRACE_ID) {
                if (response.getData() == null || !(response.getData() instanceof WithdrawList)) {
                    items.clear();
                } else {
                    totalCount = ((WithdrawList) response.getData()).getCount();
                    if (page == FIRST_PAGE) {
                        items.clear();
                    }
                    items.addAll(((WithdrawList) response.getData()).getWithdraws());
                }
                adapter.setData(items);
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
            if (request.getId() == WITHDRAW_TRACE_ID){
                refreshLayout.setRefreshing(state == RestRequest.RUNNING);
                isLoading = state == RestRequest.RUNNING;
            }
        }
    };

}
