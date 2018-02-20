package com.xinle.lottery.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.xinle.lottery.R;
import com.xinle.lottery.app.BaseFragment;
import com.xinle.lottery.app.XinLeApp;
import com.xinle.lottery.base.net.RestCallback;
import com.xinle.lottery.base.net.RestRequest;
import com.xinle.lottery.base.net.RestRequestManager;
import com.xinle.lottery.base.net.RestResponse;
import com.xinle.lottery.component.CustomDialog;
import com.xinle.lottery.component.DialogLayout;
import com.xinle.lottery.data.Lottery;
import com.xinle.lottery.data.LotteryCode;
import com.xinle.lottery.data.LotteryCodeCommand;
import com.xinle.lottery.material.TrendData;
import com.xinle.lottery.pattern.CodeView;
import com.xinle.lottery.pattern.TrendView;
import com.xinle.lottery.util.NumbericUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ACE-PC on 2016/3/7.
 */
public class ChartTrendFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = ChartTrendFragment.class.getSimpleName();
    private static final int LIST_HISTORY_CODE_ID = 1;
    private static final int FIRST_PAGE = 1;
    private int page = FIRST_PAGE;

    @BindView(R.id.linear_codelayout)
    LinearLayout codelayout;
    @BindView(R.id.linear_chartlayout)
    LinearLayout chartlayout;
    @BindView(R.id.isNeedLink)
    CheckBox isNeedLink;
    @BindView(R.id.spinner)
    Spinner spinner;

    private Lottery lottery;
    private TrendData trend;
    private ArrayList<CodeView> codeListView = new ArrayList<>();
    private ArrayList<TrendView> trendListView = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;
    private List<String> datalist = Arrays.asList("20", "40", "50");

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_charttrend, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyArguments();

        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, datalist);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        isNeedLink.setChecked(true);
        isNeedLink.setOnCheckedChangeListener(this);
        loadCodeList(FIRST_PAGE, 20);
    }

    private void applyArguments() {
        lottery = (Lottery) getArguments().getSerializable("lottery");
    }

    private void displayStyle() {
        if (trend == null) {
            showToast("暂无开奖数据");
            return;
        }
        codeListView.clear();
        trendListView.clear();
        createCodeLayout(new String[]{"期号", "开奖号码"});
        switch (lottery.getSeriesId()) {
            case 2://山东11选5
                createSyxwlayout(new String[]{"万位", "千位", "百位", "十位", "个位"});
                break;
            case 1://重庆时时彩
            case 3: //F3D
                if (trend.getTrendData().length > 3)
                    createSsclayout(new String[]{"万位", "千位", "百位", "十位", "个位"});
                else
                    createSsclayout(new String[]{"百位", "十位", "个位"});
                break;
            case 4: //快三
                createKslayout(new String[]{"百位", "十位", "个位"});
                break;
            case 5://PK拾
                createSyxwlayout(new String[]{"冠", "亚", "季", "四", "五", "六", "七", "八", "九", "十"});
                break;
        }
    }

    private void addCodeView(View v, String title) {
        CodeView codeView = new CodeView(v, title);
        codeListView.add(codeView);
    }

    private void addTrendView(View v, String title) {
        TrendView trendView = new TrendView(v, title);
        trendListView.add(trendView);
    }

    private void createCodeLayout(String[] name) {
        codelayout.removeAllViews();
        View[] views = new View[name.length];
        for (int i = 0; i < name.length; i++) {
            View view = LayoutInflater.from(codelayout.getContext()).inflate(R.layout.view_code_column, null, false);
            addCodeView(view, name[i]);
            views[i] = view;
        }

        for (View view : views) {
            codelayout.addView(view);
        }
    }

    private void createSsclayout(String[] name) {
        chartlayout.removeAllViews();
        View[] views = new View[name.length];
        for (int i = 0; i < name.length; i++) {
            View view = LayoutInflater.from(chartlayout.getContext()).inflate(R.layout.view_trend_column, null, false);
            addTrendView(view, name[i]);
            views[i] = view;
        }

        for (View view : views) {
            chartlayout.addView(view);
        }
    }

    private void createKslayout(String[] name) {
        chartlayout.removeAllViews();
        View[] views = new View[name.length];
        for (int i = 0; i < name.length; i++) {
            View view = LayoutInflater.from(chartlayout.getContext()).inflate(R.layout.view_trend_column3, null, false);
            addTrendView(view, name[i]);
            views[i] = view;
        }

        for (View view : views) {
            chartlayout.addView(view);
        }
    }

    private void createSyxwlayout(String[] name) {
        chartlayout.removeAllViews();
        View[] views = new View[name.length];
        for (int i = 0; i < name.length; i++) {
            View view = LayoutInflater.from(chartlayout.getContext()).inflate(R.layout.view_trend_column2, null, false);
            addTrendView(view, name[i]);
            views[i] = view;
        }

        for (View view : views) {
            chartlayout.addView(view);
        }
    }

    private void setData() {
        if (trend != null) {
            for (int i = 0, size = codeListView.size(); i < size; i++) {
                CodeView codeView = codeListView.get(i);
                if (i == 0) {
                    codeView.setCodeData(trend.getIssue());
                } else {
                    codeView.setCodeData(trend.getCodeData());
                }
                codeView.requestLayout();
            }
            for (int i = 0, size = trendListView.size(); i < size; i++) {
                TrendView trendView = trendListView.get(i);
                trendView.setTrendData(trend.getTrendData()[i]);
                trendView.requestLayout();
            }
        }
    }

    private void loadCodeList(int page, int number) {
        this.page = page;
        LotteryCodeCommand command = new LotteryCodeCommand();
        command.setLottery_id(lottery.getId());
        command.setCount(number);
        command.setToken(XinLeApp.getUserCentre().getUserInfo().getToken());
        TypeToken typeToken = new TypeToken<RestResponse<ArrayList<LotteryCode>>>() {
        };
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), command, typeToken, restCallback, LIST_HISTORY_CODE_ID, this);
        restRequest.execute();
    }

    /**
     * @param data
     */
    private void resolveCode(List<LotteryCode> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        int item = 0;
        switch (lottery.getSeriesId()) {
            case 2://山东11选5
                item = 5;
                break;
            case 5://PK拾
                item = 10;
                break;
            case 8://快乐十二
                item = 5;
                break;
            case 9://快乐十分
                item = 8;
                break;
            case 1://时时彩
                item = 5;
                break;
            case 3: //F3D
            case 4: //快三
                item = 3;
                break;
        }

        int issueBit = 0;
        switch (lottery.getSeriesId()) {
            case 2://山东11选5
                issueBit = 2;
                break;
            case 5://PK拾
            case 8://快乐十二
            case 9://快乐十分
                issueBit = 4;
                break;
            case 1://时时彩
            case 3: //F3D
            case 4: //快三
                issueBit = 3;
                break;
        }

        String[][] issues = new String[data.size()][1];
        String[][] codeData = new String[data.size()][item];
        String[][] trendData = new String[item][data.size()];
        for (int i = 0, size = data.size(); i < size; i++) {
            LotteryCode code = data.get(i);
            int index = code.getIssue().indexOf("-");
            String issuestr = "";
            if (index != -1) {
                issuestr = code.getIssue().substring(index + 1, code.getIssue().length());
            } else {
                if (code.getIssue().length() > issueBit) {
                    issuestr = code.getIssue().substring(code.getIssue().length() - issueBit, code.getIssue().length());
                } else {
                    issuestr = code.getIssue();
                }
            }
            issues[i][0] = issuestr;
            switch (lottery.getSeriesId()) {
                case 2://11选5
                case 5://PK拾
                case 8://快乐十二
                case 9://快乐十分
                    String[] syxwitem = code.getWnNumber().split(" ");
                    if (syxwitem.length > 1) {
                        for (int j = 0; j < syxwitem.length; j++) {
                            codeData[i][j] = NumbericUtils.isNumeric(syxwitem[j]) ? syxwitem[j] : "0";
                            trendData[j][i] = NumbericUtils.isNumeric(syxwitem[j]) ? syxwitem[j] : "0";
                        }
                    } else {
                        for (int j = 0; j < item; j++) {
                            codeData[i][j] = "0";
                            trendData[j][i] = "0";
                        }
                    }
                    break;
                case 1://时时彩
                case 3: //F3D
                case 4: //快三
                    String[] sscitem = code.getWnNumber().split("");
                    StringBuffer sb = new StringBuffer();
                    for (int s = 0, length = sscitem.length; s < length; s++) {
                        if ("".equals(sscitem[s])) {
                            continue;
                        }
                        sb.append(sscitem[s]);
                        if (s != sscitem.length - 1) {
                            sb.append(";");
                        }
                    }
                    //用String的split方法分割，得到数组
                    sscitem = sb.toString().split(";");
                    if (sscitem.length > 1) {
                        for (int j = 0, length = sscitem.length; j < length; j++) {
                            codeData[i][j] = NumbericUtils.isNumeric(sscitem[j]) ? sscitem[j] : "0";
                            trendData[j][i] = NumbericUtils.isNumeric(sscitem[j]) ? sscitem[j] : "0";
                        }
                    } else {
                        for (int j = 0; j < item; j++) {
                            codeData[i][j] = "0";
                            trendData[j][i] = "0";
                        }
                    }
                    break;
            }
        }

        trend = new TrendData();
        trend.setIssue(issues);
        trend.setCodeData(codeData);
        trend.setTrendData(trendData);
    }

    private RestCallback restCallback = new RestCallback() {
        @Override
        public boolean onRestComplete(RestRequest request, RestResponse response) {
            if (request.getId() == LIST_HISTORY_CODE_ID) {
                resolveCode((ArrayList<LotteryCode>) response.getData());
                displayStyle();
                setData();
                showHideLines(isNeedLink.isChecked());
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
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        showHideLines(isChecked);
    }

    private void showHideLines(boolean isChecked) {
        if (trend != null) {
            for (int i = 0, size = trendListView.size(); i < size; i++) {
                TrendView view = trendListView.get(i);
                view.showHideLines(isChecked);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int number = Integer.parseInt(datalist.get(position));
        loadCodeList(FIRST_PAGE, number);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * 错误参数与成功参数提示窗
     *
     * @param title
     * @param msg
     */
    private void tipDialog(String title, String msg) {
        CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
        builder.setMessage(msg);
        builder.setTitle(title);
        builder.setLayoutSet(DialogLayout.SINGLE);
        builder.setPositiveButton("知道了", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.create().show();
    }
}
