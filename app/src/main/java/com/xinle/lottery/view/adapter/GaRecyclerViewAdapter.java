package com.xinle.lottery.view.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinle.lottery.R;
import com.xinle.lottery.app.BaseFragment;
import com.xinle.lottery.data.Lottery;
import com.xinle.lottery.fragment.GameFragment;
import com.xinle.lottery.fragment.GameGaFragment;
import com.xinle.lottery.fragment.HtmlFragment;
import com.xinle.lottery.material.ConstantInformation;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ACE-PC on 2017/07/13.
 *
 * @author ACE
 *         Ga彩种大厅 Adapter
 */

public class GaRecyclerViewAdapter extends RecyclerView.Adapter<GaRecyclerViewAdapter.ViewHolder> {

    private List<Lottery> items;
    private final BaseFragment fragment;

    public GaRecyclerViewAdapter(BaseFragment fragment, List<Lottery> items) {
        this.fragment = fragment;
        this.items = items;
    }

    public void setUpdataView(List<Lottery> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_lotto_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if (items != null && items.size() > 0) {
            Lottery lottery = items.get(position);
            holder.logo.setImageResource(ConstantInformation.getLotteryLogo(lottery.getIdentifier(), lottery.getStatus() == 3));
            holder.name.setText(lottery.getName());
        } else {
            holder.logo.setImageResource(R.drawable.jia);
            holder.name.setText("");
        }

        holder.mView.setOnClickListener((View v) -> {
            if (ConstantInformation.isFastClick()) {
                if (items.size() > 0) {
                    Lottery lottery = items.get(holder.getAdapterPosition());
                    if (lottery.getStatus() == 3) {
                        GameGaFragment.launch(fragment, lottery);
                    } else {
                        fragment.tipDialog("正在测试……");
                    }
                } else {
                    fragment.tipDialog("数据正在加载请稍等");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final View mView;
        @BindView(R.id.home_lottery_ico)
        ImageView logo;
        @BindView(R.id.recentlyplayed_name)
        TextView name;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
            view.setTag(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "'";
        }
    }
}
