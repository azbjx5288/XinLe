package com.xinle.lottery.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xinle.lottery.R;
import com.xinle.lottery.app.BaseFragment;
import com.xinle.lottery.base.net.RestCallback;
import com.xinle.lottery.base.net.RestRequest;
import com.xinle.lottery.base.net.RestResponse;
import com.xinle.lottery.data.NormalRebateOptions;
import com.xinle.lottery.data.RegChildCommand;
import com.xinle.lottery.data.Register;
import com.xinle.lottery.view.adapter.LowerRebateAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ACE-PC on 2016/5/3.
 */
public class LowerRebateSetting extends BaseFragment {
    private static final String TAG = LowerRebateSetting.class.getSimpleName();
    private static final int RECBATE_TRACE_ID = 1;
    private static final int REG_TRACE_ID = 2;

    @BindView(R.id.rebatessett_list)
    ListView rebateList;

    private Register register;
    private List items = new ArrayList();
    private Map<Integer, Double> rebateArray = new HashMap<Integer, Double>();
    private LowerRebateAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflateView(inflater, container, "调整返点", R.layout.lower_rebate_setting,true,true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        register = (Register) getArguments().getSerializable("reg");
        adapter=new LowerRebateAdapter(items);
        adapter.setOnChooseItemListener((String rebate,int position) -> {
            NormalRebateOptions rebateLottery=(NormalRebateOptions)items.get(position);
            Double rebates=Double.parseDouble(rebate.substring(0,rebate.length()-1));
            rebateArray.put(rebateLottery.getPropertyId(),rebates);
        });
        rebateList.setAdapter(adapter);
        rebateLoad();
    }
    @OnClick(R.id.submitbut)
    public void registerBut() {
        String rebateStr="";
        for (Integer key : rebateArray.keySet()) {
            double rebate=rebateArray.get(key)/100;
            rebateStr+=key+":"+rebate+",";
        }
        register.setRebateStr(rebateStr.substring(0,rebateStr.length()-1));
        submit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void submit(){
        RegChildCommand command=new RegChildCommand();
        command.setOp("Register");
        command.setType(register.getType());
        command.setUsername(register.getUsername());
        command.setPassword(register.getPassword());
        command.setPassword2(register.getPassword2());
        command.setNormal_rebate(register.getRebateStr());
        executeCommand(command, restCallback, REG_TRACE_ID);
    }

    private void rebateLoad() {
        /*RegChildRebateCommand command=new RegChildRebateCommand();
        command.setOp("getRebateData");
        TypeToken typeToken = new TypeToken<RestResponse<RegChildRebate>>() {};
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), command, typeToken, restCallback, RECBATE_TRACE_ID, this);
        RestResponse restResponse = restRequest.getCache();
        if (restResponse != null && restResponse.getData() instanceof RegChildRebate) {
            items.addAll(((RegChildRebate) restResponse.getData()).getNormalRebateOptions());
            adapter.setData(items);
        }
        restRequest.execute();*/
    }

    private RestCallback restCallback = new RestCallback() {
        @Override
        public boolean onRestComplete(RestRequest request, RestResponse response) {
            if(request.getId()==REG_TRACE_ID){
//                items.addAll(((RegChildRebate) response.getData()).getNormalRebateOptions());
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
        }
    };
}