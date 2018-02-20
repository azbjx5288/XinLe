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
import com.xinle.lottery.data.Trace;
import com.xinle.lottery.data.TraceListCommand;
import com.xinle.lottery.game.PromptManager;
import com.xinle.lottery.view.adapter.GameHistoryAdapter;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class TracePanelFragment extends BaseFragment {
    private static final int TRACE_LIST_COMMAND = 1;
//    private static final int REFRESH_COMPLETE = 2017;
    private PtrClassicFrameLayout ptrFrame;
    private TextView viewEmpty;
    private ListView listView;

    private GameHistoryAdapter adapter;
    private static final int FIRST_PAGE = 1;
    private int page = FIRST_PAGE;
    private List items = new ArrayList();
    private boolean isLoading;
    private int type = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tracepanel, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getInt("traceType");

        ptrFrame=(PtrClassicFrameLayout)findViewById(R.id.refreshTraceLayout);
        viewEmpty = (TextView) findViewById(R.id.emptyTrace);
        listView=(ListView)findViewById(R.id.traceListView);

        viewEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ptrFrame.setVisibility(View.VISIBLE);
                ptrFrame.autoRefresh();
            }
        });

        adapter = new GameHistoryAdapter(getContext(),this);
        listView.setAdapter(adapter);

        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, listView, header);
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
        ptrFrame.autoRefresh(true);
        ptrFrame.setResistance(1.7f);
        ptrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        ptrFrame.setDurationToClose(200);
        ptrFrame.setDurationToCloseHeader(1000);
        ptrFrame.setPullToRefresh(true);
        ptrFrame.setLastUpdateTimeRelateObject(this);
        ptrFrame.setKeepHeaderWhenRefresh(true);
    }

    @Override
    public void onResume() {
        if(ptrFrame!=null){
            ptrFrame.autoRefresh();
        }
        super.onResume();
    }

    private void loadTraceList(int page) {
        if (isLoading) {
            return;
        }
        this.page = page;
        TraceListCommand command = new TraceListCommand();
        command.setLottery_id(type);
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

    private void handle(){
        adapter.setData(items);
        if (items.size() > 0) {
            viewEmpty.setVisibility(View.GONE);
        } else {
            viewEmpty.setVisibility(View.VISIBLE);
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
                ptrFrame.refreshComplete();
            }
        }
    };
}



