package com.xinle.lottery.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinle.lottery.R;
import com.xinle.lottery.data.IssueEntity;
import com.xinle.lottery.data.LotteryCode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ACE-PC on 2016/3/1.
 */
public class HistoryCodeAdapter extends BaseAdapter {

    private List codeList;

    public HistoryCodeAdapter(List codeList){
        this.codeList=codeList;
    }

    @Override
    public int getCount() {
        return codeList!=null?codeList.size():0;
    }

    @Override
    public Object getItem(int position) {
        if (codeList == null) {
            return null;
        }
        if (position >= 0 && position < codeList.size()) {
            return codeList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public  void setData(List codeList){
        this.codeList=codeList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_history_result_item, parent, false);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LotteryCode historyCode=(LotteryCode)codeList.get(position);
        holder.issue.setText(historyCode.getIssue());
        holder.code.setText(historyCode.getWnNumber());

        return convertView;
    }
    static class ViewHolder {
        @BindView(R.id.historycode_issue)
        TextView issue;
        @BindView(R.id.historycode_code)
        TextView code;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
