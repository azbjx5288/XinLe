package com.xinle.lottery.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinle.lottery.BuildConfig;
import com.xinle.lottery.R;
import com.xinle.lottery.app.BaseFragment;
import com.xinle.lottery.app.FragmentLauncher;
import com.xinle.lottery.app.XinLeApp;
import com.xinle.lottery.base.Preferences;
import com.xinle.lottery.base.net.GsonHelper;
import com.xinle.lottery.base.net.RestCallback;
import com.xinle.lottery.base.net.RestRequest;
import com.xinle.lottery.base.net.RestRequestManager;
import com.xinle.lottery.base.net.RestResponse;
import com.xinle.lottery.data.Lottery;
import com.xinle.lottery.data.Method;
import com.xinle.lottery.data.MethodList;
import com.xinle.lottery.data.MethodListCommand;
import com.xinle.lottery.data.MethodType;
import com.xinle.lottery.game.Game;
import com.xinle.lottery.game.GameConfig;
import com.xinle.lottery.game.MenuController;
import com.xinle.lottery.game.OnSelectedListener;
import com.xinle.lottery.material.ConstantInformation;
import com.xinle.lottery.material.MethodQueue;
import com.xinle.lottery.material.ShoppingCart;
import com.xinle.lottery.material.Ticket;
import com.xinle.lottery.pattern.TitleTimingView;
import com.xinle.lottery.user.UserCentre;
import com.xinle.lottery.util.SharedPreferencesUtils;
import com.xinle.lottery.view.TableMenu;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选号界面
 * Created by Alashi on 2016/2/17.
 */
public class GameFragment extends BaseFragment implements OnSelectedListener, TableMenu.OnClickMethodListener {
    private static final String TAG = GameFragment.class.getSimpleName();

    private static final int ID_METHOD_LIST = 1;

    WebView webView;
    @BindView(android.R.id.title)
    TextView titleView;
    @BindView(R.id.pick_notice)
    TextView pickNoticeView;
    @BindView(R.id.pick_game_layout)
    LinearLayout pickGameLayout;
    @BindView(R.id.choose_done_button)
    Button chooseDoneButton;
    @BindView(R.id.lottery_choose_bottom)
    RelativeLayout chooseBottomLayout;
    @BindView(R.id.pick_random)
    Button pickRandom;

    private TitleTimingView timingView;
    private Lottery lottery;
    private Game game;
    private MenuController menuController;
    private ShoppingCart shoppingCart;
    private UserCentre userCentre;
    private ArrayList<MethodList> methodList;
    private String[] array = new String[2];
    private MethodQueue mRegularMethods;//常用玩法

    public static void launch(BaseFragment fragment, Lottery lottery) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("lottery", lottery);
        FragmentLauncher.launch(fragment.getActivity(), GameFragment.class, bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyArguments();
        initMenu();
        loadMethodFromXml();
        loadMenu();
        mRegularMethods=getRegularMethods();
    }

    /**从本地获取或者新建常用玩法**/
    private MethodQueue getRegularMethods()
    {
        MethodQueue regularMethods=null;
        try{
            regularMethods = (MethodQueue) SharedPreferencesUtils.getObject(getActivity(),
                    ConstantInformation.REGULAR_METHODS, XinLeApp.getUserCentre().getUserID() + "_" + lottery
                            .getId());
        }catch (Exception e)
        {
            Log.d(TAG, "getRegularMethods: fail to load methods.");
        }
        if (regularMethods == null){
            regularMethods = new MethodQueue(9);
        }
        return regularMethods;
    }

    private void applyArguments() {
        shoppingCart = ShoppingCart.getInstance();
        userCentre = XinLeApp.getUserCentre();
        lottery = (Lottery) getArguments().getSerializable("lottery");
    }

    private void loadWebViewIfNeed() {
        if (webView != null) {
            return;
        }
        if (chooseBottomLayout != null) {
            chooseBottomLayout.postDelayed(() -> {
                synchronized (chooseBottomLayout) {
                    if (isFinishing()) {
                        return;
                    }
                    if (webView != null) {
                        update2WebView();
                        return;
                    }
                    webView = new WebView(getActivity());
                    chooseBottomLayout.addView(webView, 1, 1);
                    initWebView();
                    webView.loadUrl("file:///android_asset/web/game.html");
                }
            }, 400);
        }
    }

    private void loadMethodFromXml() {
        String key = "game_config_method_" + XinLeApp.getUserCentre().getUserID() + "_" + lottery.getId();
        String gamemethod = Preferences.getString(getContext(), key, null);
        if (gamemethod != null) {
            array = gamemethod.split("-");
            if (array.length == 2) {
                MethodType methodType = GsonHelper.fromJson(array[0], MethodType.class);
                Method method = GsonHelper.fromJson(array[1], Method.class);
                if (methodType != null && method != null) {
                    changeGameMethod(methodType, method);
                }
            }
        }
    }

    private void saveMethod2Xml(MethodType methodType, Method method) {
        String key = "game_config_method_" + XinLeApp.getUserCentre().getUserID() + "_" + lottery.getId();
        Preferences.saveString(getContext(), key, GsonHelper.toJson(methodType) + "-" + GsonHelper.toJson(method));
    }

    private void initMenu() {
        menuController = new MenuController(getActivity(), lottery);
        menuController.setOnClickMethodListener(this);
    }

    private void loadMenu() {
        MethodListCommand methodListCommand = new MethodListCommand();
        methodListCommand.setLottery_id(lottery.getId());
        methodListCommand.setToken(userCentre.getUserInfo().getToken());
        TypeToken typeToken = new TypeToken<RestResponse<ArrayList<MethodList>>>() {
        };
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), methodListCommand, typeToken, restCallback, ID_METHOD_LIST, this);
        restRequest.execute();
    }

    private void updateMenu(ArrayList<MethodList> methodList) {
        menuController.setMethodList(methodList);
    }

    private void loadTimingView(int methodID) {
        timingView = new TitleTimingView(getActivity(), findViewById(R.id.pick_game_timing_view), lottery, methodID);
    }

    @Override
    public void onPause() {
        if (webView != null) {
            webView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timingView != null) {
            timingView.stop();
        }
        if (game != null) {
            game.destroy();
        }
        if (webView != null) {
            webView.destroy();
        }

        try
        {
            SharedPreferencesUtils.putObject(getActivity(), ConstantInformation.REGULAR_METHODS, XinLeApp
                    .getUserCentre().getUserID() + "_" + lottery.getId(), mRegularMethods);
        } catch (Exception e)
        {
            Log.d(TAG, "onDestroyView: fail to save methods.");
        }
    }

    @OnClick(android.R.id.home)
    public void finishFragment() {
        getActivity().finish();
    }

    @OnClick({R.id.title_text_layout, android.R.id.title, R.id.title_image})
    public void showOrHideMenu(View v) {
        inputkey(v);
        Log.d(TAG, "showOrHideMenu: ");
        if (menuController.isShowing()) {
            menuController.hide();
        } else {
            menuController.show(titleView);
        }
    }

    @OnClick(R.id.help)
    public void showHelp() {
        //TODO:点击“帮助”按钮，显示帮助信息
    }

    @OnClick(R.id.pick_random)
    public void onRandom() {
        if (game != null) {
            game.onRandomCodes();
        }
    }

    @OnClick(R.id.pick_notice)
    public void calculate() {
        if (game == null) {
            return;
        }
        pickNoticeView.setText(String.format("选择%d注", game.getSingleNum()));
        if (game.getSingleNum() > 0) {
            chooseDoneButton.setEnabled(true);
        } else {
            chooseDoneButton.setEnabled(!shoppingCart.isEmpty());
        }
    }

    @OnClick(R.id.choose_done_button)
    public void onChooseDone() {
        if (game == null) {
            return;
        }
        if (game.getSingleNum() > 0) {
            String codes = game.getSubmitCodes();
            Ticket ticket = new Ticket();
            ticket.setMethodType(GsonHelper.fromJson(array[0], MethodType.class));
            ticket.setChooseMethod(game.getMethod());
            ticket.setChooseNotes(game.getSingleNum());
            ticket.setCodes(codes);

            shoppingCart.init(lottery);
            shoppingCart.addTicket(ticket);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("lottery", lottery);
        ShoppingFragment.launch(GameFragment.this,lottery);
//        launchFragmentForResult(ShoppingFragment.class, bundle, 1);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (game != null) {
            game.reset();
        }
    }

    @Override
    public void onClickMethod(Method method) {
        menuController.hide();
        MethodType methodType = defaultMethodType(method);
        changeGameMethod(methodType, method);

        saveLastMethod(method);
    }

    private void saveLastMethod(Method method) {
        if (method != null) {
            String fileName = XinLeApp.getUserCentre().getUserID() + " lastPlay";
            try {
                SharedPreferencesUtils.putObject(getActivity(), fileName, lottery.getName()+lottery.getId(), method);


                SharedPreferencesUtils.putObject(getActivity(), ConstantInformation.REGULAR_METHODS, XinLeApp
                        .getUserCentre().getUserID() + "_" + lottery.getId(), mRegularMethods);
            } catch (Exception e) {
            }
        }
    }

    private class JsInterface {
        @JavascriptInterface
        public String getData() {
            if (game == null) {
                return "";
            }
            return game.getWebViewCode();
        }

        @JavascriptInterface
        public String getSubmitCodes() {
            if (game == null) {
                return "";
            }
            return game.getSubmitCodes();
        }

        @JavascriptInterface
        public String getMethodName() {
            if (game == null) {
                return "";
            }
            return game.getMethod().getNameEn();
        }

        @JavascriptInterface
        public int getLotteryId() {
            if (lottery == null) {
                return -1;
            }
            return lottery.getId();
        }

        @JavascriptInterface
        public int getMethodId() {
            if (game == null) {
                return -1;
            }
            return game.getMethod().getId();
        }

        @JavascriptInterface
        public void result(int singleNum) {
            Log.d(TAG, "result() called with: " + "singleNum = [" + singleNum + "]");
            if (game == null) {
                return;
            }
            game.setSingleNum(singleNum);
            webView.post(updatePickNoticeRunnable);
        }
    }

    private void update2WebView() {
        if (webView == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("calculate();", null);
        } else {
            webView.loadUrl("javascript:calculate();");
        }
    }

    private Runnable updatePickNoticeRunnable = new Runnable() {
        @Override
        public void run() {
            calculate();
        }
    };

    private void changeGameMethod(MethodType methodType, Method method) {
        if (method == null) {
            return;
        }
        if (game == null) {
            pickGameLayout.removeAllViews();
        } else {
            if (methodType.getId() == game.getMethod().getPid() && method.getId() == game.getMethod().getId()) {
                //同一个玩法，不用切换
                return;
            }
            game.destroy();
            menuController.addPreference(method);
            saveMethod2Xml(methodType, method);
            addRegularMethods(method);
            array = new String[]{GsonHelper.toJson(methodType), GsonHelper.toJson(method)};
        }
        if (method.getId() == 178)
            loadTimingView(178);
        else
            loadTimingView(0);
        menuController.setCurrentMethod(method);
        titleView.setText(methodType.getNameCn() + "-" + method.getNameCn());
        pickNoticeView.setText("选择0注");
        chooseDoneButton.setEnabled(!shoppingCart.isEmpty());
        game = GameConfig.createGame(getActivity(), method, lottery);
        game.inflate(pickGameLayout);
        game.setOnSelectedListener(this);
        if (game.isInputStatus())
            pickRandom.setVisibility(View.GONE);
        else
            pickRandom.setVisibility(View.VISIBLE);

        loadWebViewIfNeed();
    }

    private void addRegularMethods(Method method)
    {
        mRegularMethods.addFirst(method);
    }

    private Method defaultGameMethod(ArrayList<MethodList> methodList) {
        int id = 0;
        switch (lottery.getSeriesId()) {
            case 2://11选5
                id = 112;
                break;
            case 1://重庆时时彩
                id = 65;
                break;
            case 4://苹果快三分分彩
                id = 160;
                break;
            case 5://苹果极速PK10
                id = 175;
                break;
            case 3://苹果极速3D
                id = 136;
                break;
            case 9://快乐十分
                id = 456;
            default:
                break;
        }

        if (id == 0) {
            return methodList.get(0).getChildren().get(0).getChildren().get(0);
        }

        for (MethodList methods : methodList) {
            for (MethodList.ChildrenBean methodGroup : methods.getChildren()) {
                for (Method method : methodGroup.getChildren()) {
                    if (id == method.getId()) {
                        return method;
                    }
                }
            }
        }
        return methodList.get(0).getChildren().get(0).getChildren().get(0);
    }

    private MethodType defaultMethodType(Method methodChoose) {
        for (MethodList methods : methodList) {
            for (MethodList.ChildrenBean methodGroup : methods.getChildren()) {
                if (methodChoose.getPid() == methodGroup.getId()) {
                    for (Method method : methodGroup.getChildren()) {
                        if (methodChoose.getId() == method.getId()) {
                            return new MethodType(methods.getId(), methods.getPid(), methods.getNameCn(), methods.getNameEn());
                        }
                    }
                }
            }
        }
        return null;
    }

    //game.setOnSelectedListener(this)的回调
    @Override
    public void onChanged(Game game) {
        loadWebViewIfNeed();
        update2WebView();
    }

    private void inputkey(View v) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private RestCallback restCallback = new RestCallback<ArrayList<?>>() {
        @Override
        public boolean onRestComplete(RestRequest request, RestResponse response) {
            if (request.getId() == ID_METHOD_LIST) {
                methodList = (ArrayList<MethodList>) response.getData();
                if (methodList != null && game == null) {

                    //获取上一次点击的玩法
                    Method method = (Method) SharedPreferencesUtils.getObject(getActivity(), XinLeApp
                            .getUserCentre().getUserID() + " lastPlay", lottery.getName()+lottery.getId());

                    if(method==null) {
                        method = defaultGameMethod(methodList);
                    }

//                    Method method=defaultGameMethod(methodList);

                    MethodType methodType = defaultMethodType(method);
                    saveMethod2Xml(methodType, method);
                    menuController.addPreference(method);
                    changeGameMethod(methodType, method);
                }
                updateMenu(methodList);
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
}
