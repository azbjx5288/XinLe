package com.xinle.lottery.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xinle.lottery.R;
import com.xinle.lottery.app.BaseFragment;
import com.xinle.lottery.app.XinLeApp;
import com.xinle.lottery.data.Lottery;
import com.xinle.lottery.material.RecordType;
import com.xinle.lottery.view.adapter.GaRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ACE-PC on 2017/7/7.
 * GA游戏列表
 */

public class GaFragment extends BaseFragment {
    private static final String TAG = GaFragment.class.getSimpleName();
    private GaRecyclerViewAdapter viewAdapter;
    private List<Lottery> item = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ga, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view instanceof RecyclerView) {
            viewAdapter = new GaRecyclerViewAdapter(this, item);
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            recyclerView.setAdapter(viewAdapter);
        }
    }

    public void notifyData(List<Lottery> item) {
        if (item.size() == 0) {
            return;
        }
        getLotteryModel().setLotteryList(item);
        XinLeApp.getUserCentre().setLotteryList(item);
        List<Lottery> lotteries = new ArrayList<>();
        for (Lottery l : item) {
            if (l.getGameType() == 5) {
                lotteries.add(l);
            }
        }
        viewAdapter.setUpdataView(lotteries);
    }

    private RecordType getLotteryModel() {
        return RecordType.get(getActivity(), "lottery_model_history");
    }
}
