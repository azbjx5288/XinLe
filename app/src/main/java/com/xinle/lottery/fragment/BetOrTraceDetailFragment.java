package com.xinle.lottery.fragment;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.xinle.lottery.BuildConfig;
import com.xinle.lottery.R;
import com.xinle.lottery.app.BaseFragment;
import com.xinle.lottery.app.FragmentLauncher;
import com.xinle.lottery.app.XinLeApp;
import com.xinle.lottery.base.net.GsonHelper;
import com.xinle.lottery.base.net.JsonString;
import com.xinle.lottery.base.net.RestCallback;
import com.xinle.lottery.base.net.RestRequest;
import com.xinle.lottery.base.net.RestRequestManager;
import com.xinle.lottery.base.net.RestResponse;
import com.xinle.lottery.data.Bet;
import com.xinle.lottery.data.BetDetailCommand;
import com.xinle.lottery.data.CancelPackageCommand;
import com.xinle.lottery.data.CancelTraceCommand;
import com.xinle.lottery.data.Lottery;
import com.xinle.lottery.data.Trace;
import com.xinle.lottery.data.TraceDetailCommand;
import com.xinle.lottery.user.UserCentre;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Alashi on 2016/1/15.
 */
public class BetOrTraceDetailFragment extends BaseFragment {
    private static final String TAG = BetOrTraceDetailFragment.class.getSimpleName();

    private static final int BET_DETAIL_ID = 1;
    private static final int TRACE_DETAIL_ID = 2;
    private static final int CANCEL_PACKAGE_ID = 3;
    private static final int CANCEL_TRACE_ID = 4;

    @BindView(R.id.webView) WebView webView;
    @BindView(R.id.button) Button button;

    private boolean isBet;
    private Bet bet;
    private Trace trace;
    private Lottery lottery;
    private String[] pkids;
    private UserCentre userCentre;
    private String jsonDataForWeb;

    public static void launch(BaseFragment fragment, Bet bet) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isBet", true);
        bundle.putString("Bet", GsonHelper.toJson(bet));
        FragmentLauncher.launch(fragment.getActivity(), BetOrTraceDetailFragment.class, bundle);
    }

    public static void launch(BaseFragment fragment, Trace trace) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isBet", false);
        bundle.putString("Trace", GsonHelper.toJson(trace));
        FragmentLauncher.launch(fragment.getActivity(), BetOrTraceDetailFragment.class, bundle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isBet = getArguments().getBoolean("isBet");
        return inflateView(inflater, container, isBet? "注单详情" : "追号详情", R.layout.bet_or_trace_detail,true,true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userCentre = XinLeApp.getUserCentre();
        initWebView();
        if (isBet) {
            webView.loadUrl("file:///android_asset/web/BetDetail.html");
            loadBetData();
        } else {
            webView.loadUrl("file:///android_asset/web/TraceDetail.html");
            loadTraceData();
        }
    }

    private void loadBetData() {
        bet = GsonHelper.fromJson(getArguments().getString("Bet"), Bet.class);
        if (bet == null){
            Toast.makeText(getActivity(), "无效注单参数", Toast.LENGTH_SHORT).show();
            getActivity().finish();
            return;
        }

        refreshBet(true);
    }

    private void loadTraceData() {
        trace = GsonHelper.fromJson(getArguments().getString("Trace"), Trace.class);
        if (trace == null){
            Toast.makeText(getActivity(), "无效追号参数", Toast.LENGTH_SHORT).show();
            getActivity().finish();
            return;
        }

        refreshTrace(true);
    }

    private void refreshTrace(boolean withCache) {
        TraceDetailCommand command = new TraceDetailCommand();
        command.setId(trace.getId());
        command.setToken(XinLeApp.getUserCentre().getUserInfo().getToken());
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), command, restCallback, TRACE_DETAIL_ID, this);
        if (withCache) {
            RestResponse restResponse = restRequest.getCache();
            if (restResponse != null) {
                JsonString jsonString=(JsonString) restResponse.getData();
                if(jsonString!=null) {
                    jsonDataForWeb =  GsonHelper.toJson(restResponse.getData());
                    update2WebView();
                }
            }
        }
        restRequest.execute();
    }
    private void refreshBet(boolean withCache) {
        BetDetailCommand command = new BetDetailCommand();
        command.setId(bet.getId());
        command.setToken(XinLeApp.getUserCentre().getUserInfo().getToken());
        TypeToken typeToken = new TypeToken<RestResponse<Bet>>() {
        };
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), command, typeToken, restCallback, BET_DETAIL_ID, this);
        if (withCache) {
            RestResponse restResponse = restRequest.getCache();
            if (restResponse != null) {
                jsonDataForWeb = GsonHelper.toJson(restResponse.getData());
                update2WebView();
            }
        }
        restRequest.execute();
    }

    private void update2WebView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("onLoaded();", null);
        } else {
            webView.loadUrl("javascript:onLoaded();");
        }
    }

    private void initWebView() {
        webView.setOverScrollMode(WebView.OVER_SCROLL_ALWAYS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        }
        WebSettings webSettings = webView.getSettings();
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JsInterface(), "androidJs");
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    public void onPause() {
        webView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        webView.destroy();
        super.onDestroyView();
    }

    private class JsInterface {
        @JavascriptInterface
        public String getData() {
            return jsonDataForWeb;
        }

        @JavascriptInterface
        public String getLottery() {
            if (isBet) {
                return GsonHelper.toJson(userCentre.getLottery(bet.getLotteryId()));
            }else{
                return GsonHelper.toJson(userCentre.getLottery(trace.getLotteryId()));
            }
        }

        @JavascriptInterface
        public void changeUi(final int lotteryId, final boolean supportCancel, final String[] pkids) {
            BetOrTraceDetailFragment.this.pkids = pkids;
            button.post(new Runnable() {
                @Override
                public void run() {
                    lottery = XinLeApp.getUserCentre().getLottery(lotteryId);
                    if (lottery == null) {
                        findViewById(R.id.bottom).setVisibility(View.GONE);
                    }

                    button.setTag(supportCancel);
                    if (isBet) {
                        button.setText(supportCancel? "撤单": "去购彩");
                    } else {

                        if (supportCancel && BetOrTraceDetailFragment.this.pkids != null && BetOrTraceDetailFragment.this.pkids.length > 0) {
                            button.setText("撤单");
                        } else {
                            button.setText("去购彩");
                        }
                    }
                }
            });
        }

        @JavascriptInterface
        public void allowCancelTrace(final boolean allow) {
            button.post(() -> {
                    if (allow) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("取消追号")
                                .setMessage("确定要取消追号？")
                                .setPositiveButton("取消追号", (dialog, which) -> {
                                    try {
                                        CancelTraceCommand command = new CancelTraceCommand();
                                        command.set_traceId(String.valueOf(trace.getId()));
                                        command.set_ids(URLEncoder.encode(Arrays.asList(pkids).toString(),"UTF-8"));
                                        command.set_token(userCentre.getUserInfo().getToken());
                                        String requestCommand= GsonHelper.toJson(command);
                                        requestCommand=requestCommand.replace(":","=").replace(",","&").replace("\"","");
                                        requestCommand= URLDecoder.decode(requestCommand.substring(1,requestCommand.length()-1),"UTF-8");
                                        Log.i(TAG,requestCommand+"&packet=Game&action=cancelTraceReserve");

                                        CancelTraceCommand commandCancel = new CancelTraceCommand();
                                        commandCancel.set_traceId(String.valueOf(trace.getId()));
                                        commandCancel.set_ids(Arrays.asList(pkids).toString());
                                        commandCancel.set_token(userCentre.getUserInfo().getToken());
                                        commandCancel.setSign(DigestUtils.md5Hex(URLEncoder.encode(requestCommand+"&packet=Game&action=cancelTraceReserve","UTF-8")+userCentre.getKeyApiKey()));
                                        executeCommand(commandCancel, restCallback, CANCEL_TRACE_ID);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }

                                })
                                .setNegativeButton("保留追号", null)
                                .create().show();
                    } else {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("提醒")
                                .setMessage("已经过了取消追号的时间，不能取消追号")
                                .setNegativeButton("确定", null)
                                .create().show();
                    }
            });
        }
    }

    @OnClick(R.id.button)
    public void onClick(View v) {
        boolean supportCancel = (boolean)v.getTag();
        if (!supportCancel) {
            GameFragment.launch(this, lottery);
            return;
        }

        if (isBet) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("取消订单")
                    .setMessage("确定要取消订单？")
                    .setPositiveButton("取消订单", (dialog, which) -> {
                        try {
                            CancelPackageCommand command = new CancelPackageCommand();
                            command.set_Id(String.valueOf(bet.getId()));
                            command.set_Token(userCentre.getUserInfo().getToken());
                            String requestCommand= GsonHelper.toJson(command);
                            requestCommand=requestCommand.replace(':','=').replace(',','&').replace("\"","");
                            command.setSign(DigestUtils.md5Hex(URLEncoder.encode(requestCommand.substring(1,requestCommand.length()-1)+"&packet=Game&action=dropProject","UTF-8")+userCentre.getKeyApiKey()));
                            executeCommand(command, restCallback, CANCEL_PACKAGE_ID);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    })
                    .setNegativeButton("保留订单", null)
                    .create().show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView.evaluateJavascript("reviewStatus();", null);
            } else {
                webView.loadUrl("javascript:reviewStatus();");
            }
        }
    }

    private RestCallback restCallback = new RestCallback() {
        @Override
        public boolean onRestComplete(RestRequest request, RestResponse response) {
            switch (request.getId()) {
                case BET_DETAIL_ID:
                    jsonDataForWeb = GsonHelper.toJson(response.getData());
                    update2WebView();
                    break;
                case TRACE_DETAIL_ID:
                    jsonDataForWeb = ((JsonString)response.getData()).getJson();
                    update2WebView();
                    break;
                case CANCEL_PACKAGE_ID:
                    refreshBet(false);
                    break;
                case CANCEL_TRACE_ID:
                    refreshTrace(false);
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
            /*if (state == RestRequest.RUNNING) {
                showProgress(request.getId() == BET_DETAIL_ID? "加载中..." : "进行中...");
            } else {
                hideProgress();
            }*/
            if (state == RestRequest.RUNNING) {
                if (request.getId() == BET_DETAIL_ID) {
                    showProgress("进行中...");
                }
            } else {
                hideWaitProgress();
            }
        }
    };
}
