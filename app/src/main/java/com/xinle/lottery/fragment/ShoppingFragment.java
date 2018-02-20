package com.xinle.lottery.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xinle.lottery.R;
import com.xinle.lottery.app.BaseFragment;
import com.xinle.lottery.app.FragmentLauncher;
import com.xinle.lottery.app.XinLeApp;
import com.xinle.lottery.base.net.GsonHelper;
import com.xinle.lottery.base.net.RestCallback;
import com.xinle.lottery.base.net.RestRequest;
import com.xinle.lottery.base.net.RestRequestManager;
import com.xinle.lottery.base.net.RestResponse;
import com.xinle.lottery.component.CustomDialog;
import com.xinle.lottery.component.DialogLayout;
import com.xinle.lottery.data.Bet;
import com.xinle.lottery.data.BetData;
import com.xinle.lottery.data.Lottery;
import com.xinle.lottery.data.OrderFind;
import com.xinle.lottery.data.PayMoneyCommand;
import com.xinle.lottery.data.RegChildRebate;
import com.xinle.lottery.data.RegChildRebateCommand;
import com.xinle.lottery.data.Trace;
import com.xinle.lottery.data.TraceIssue;
import com.xinle.lottery.data.TraceIssueCommand;
import com.xinle.lottery.data.UserInfo;
import com.xinle.lottery.data.UserInfoCommand;
import com.xinle.lottery.material.ConstantInformation;
import com.xinle.lottery.material.ShoppingCart;
import com.xinle.lottery.pattern.ChooseTips;
import com.xinle.lottery.pattern.ShroudView;
import com.xinle.lottery.pattern.TitleTimingSalesView;
import com.xinle.lottery.user.UserCentre;
import com.xinle.lottery.view.adapter.ShoppingAdapter;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 购物车
 * Created on 2016/02/17.
 *
 * @author ACE
 */
public class ShoppingFragment extends BaseFragment {
    private static final String TAG = ShoppingFragment.class.getSimpleName();

    private static final int BUY_TRACE_ID = 1;
    private static final int ID_USER_INFO = 2;
    private static final int BUY_REBATE_ID = 3;
    private static final int BUY_TRACE_ISSUE_ID = 4;
    private static final int TRACK_TURNED_PAGE_LOGIN = 1;
    private static final int TRACK_TURNED_PAGE_RECHARGE = 2;
    private static final int TRACK_TURNED_PAGE_PICK = 3;


    @BindView(R.id.shopping_list)
    ListView shoppingList;
    @BindView(R.id.lottery_shopping_balance)
    TextView balanceText;
    @BindView(R.id.lottery_shopping_buy)
    Button shopping_buy;
    @BindView(R.id.chaseadd_number_layout)
    View chaseaddNumberLayout;
    @BindView(R.id.shopping_append_settings)
    CheckBox appendSettings;

    private TitleTimingSalesView timingView;
    private ShoppingAdapter adapter;
    private ShroudView shroudView;
    private ChooseTips chooseTips;
    private Lottery lottery;
    private ShoppingCart cart;
    private UserCentre userCentre;
    private boolean isInTraceState;

    /**
     * 辅助用，投注异常时，上报到服务器的错误信息
     */
    private String unusualInfo;

    public static void launch(BaseFragment fragment, Lottery lottery) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("lottery", lottery);
        FragmentLauncher.launch(fragment.getActivity(), ShoppingFragment.class, bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflateView(inflater, container, "投注", R.layout.shopping_fragment, true, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parameter();
        initInfo();
        loadTimingView();
        loadFunction();
        loadListInfo();
        loadLotteryRebate();
        planPrompt();
    }

    private void parameter() {
        lottery = (Lottery) getArguments().getSerializable("lottery");
        userCentre = XinLeApp.getUserCentre();
        cart = ShoppingCart.getInstance();
    }

    private void initInfo() {
        cart.init(lottery);
        setTitle(lottery.getName());
        balanceText.setText(String.format("余额：%.4f", userCentre.getUserInfo().getAbalance()));
        if (lottery.getId() == 12 || lottery.getId() == 11) {
            //福彩3D 和 P3p5 不允许追号
            chaseaddNumberLayout.setVisibility(View.GONE);
            appendSettings.setVisibility(View.GONE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            executeCommand(new UserInfoCommand(), restCallback, ID_USER_INFO);
        }
    }

    private void loadTimingView() {
        timingView = new TitleTimingSalesView(getActivity(), findViewById(R.id.shopping_top_timing), lottery);
        timingView.setOnSalesIssueListener((String issue) -> {
            ShoppingCart.getInstance().setIssue(issue);
            loadTraceIssue();
        });
    }

    private void loadFunction() {
        shroudView = new ShroudView(findViewById(R.id.shopping_bottom));
        chooseTips = new ChooseTips(findViewById(R.id.shopping_choosetip));
    }

    private void loadListInfo() {
        adapter = new ShoppingAdapter();
        adapter.setOnDeleteClickListener(this::planPrompt);
        shoppingList.setAdapter(adapter);
        shroudView.setModeItemListener((multiple, chaseadd, lucreMode, appendSet) -> {
            cart.setPlanBuyRule(multiple, chaseadd, appendSet);
            planPrompt();
        });
    }

    public void planPrompt() {
        chooseTips.setTipsText(String.format("订单金额:\t%.3f\t 元", cart.getPlanAmount()),
                String.format("总注数:\t%d\t 注", cart.getPlanNotes()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timingView != null) {
            timingView.stop();
        }
    }

    @OnClick(R.id.chase_button)
    public void chase() {
        if (timingView.getIssue() == null || timingView.getIssue().length() <= 0) {
            tipDialog("温馨提示", "请稍等，正在加载销售奖期信息……", 0);
            return;
        }
        if (cart.getPlanNotes() == 0) {
            tipDialog("温馨提示", "您需要选择一注", TRACK_TURNED_PAGE_PICK);
            return;
        }
    }

    @OnClick(R.id.lottery_shopping_buy)
    public void total() {
        if (timingView.getIssue() == null || timingView.getIssue().length() <= 0) {
            tipDialog("温馨提示", "请稍等，正在加载销售奖期信息……", 0);
            return;
        }
        // ①判断：购物车中是否有投注
        if (!cart.isEmpty()) {
            // ②判断：用户是否登录——被动登录
            if (userCentre.isLogin()) {
                // ③判断：用户的余额是否满足投注需求
                if (cart.getPlanAmount() <= Double.valueOf(userCentre.getUserInfo().getAbalance())) {
                    // ④界面跳转：跳转到追期和倍投的设置界面
                    verificationData();
                } else {
                    // 提示用户：充值去；界面跳转：用户充值界面
                    tipDialog("温馨提示", "请充值", TRACK_TURNED_PAGE_RECHARGE);
                }
            } else {
                // 提示用户：登录去；界面跳转：用户登录界面
                tipDialog("温馨提示", "请重新登录", TRACK_TURNED_PAGE_LOGIN);
            }
        } else {
            // 提示用户需要选择一注
            tipDialog("温馨提示", "您需要选择一注", TRACK_TURNED_PAGE_PICK);
        }
    }

    /**
     * 数据验证
     */
    private void verificationData() {
        String msg;
        String planAmount = String.format("%.3f", cart.getPlanAmount());

        if (cart.getTraceNumber() > 0) {
            msg = getActivity().getResources().getString(R.string.is_shopping_list_chasetip);
            msg = StringUtils.replaceEach(msg, new String[]{"ISSUE", "NOTE", "UNIT", "DOUBLE", "CHASENUM", "MONEY"},
                    new String[]{timingView.getIssue(),
                            String.valueOf(cart.getPlanNotes()),
                            cart.getLucreMode().getName(),
                            String.valueOf(cart.getMultiple()),
                            String.valueOf(cart.getTraceNumber()),
                            planAmount});
        } else {
            msg = getActivity().getResources().getString(R.string.is_shopping_list_tip);
            msg = StringUtils.replaceEach(msg, new String[]{"ISSUE", "NOTE", "UNIT", "DOUBLE", "MONEY"},
                    new String[]{timingView.getIssue(),
                            String.valueOf(cart.getPlanNotes()),
                            cart.getLucreMode().getName(),
                            String.valueOf(cart.getMultiple()),
                            planAmount});
        }

        if (cart.getTraceNumber() > 0) {
            if (cart.getOrdersMap().size() == 0) {
                tipDialog("温馨提示", "请稍等，网速有点慢", 0);
                return;
            }
        }

        BetData betData = new BetData();
        betData.setGameId(lottery.getId());
        betData.setIsTrace(cart.getTraceNumber() > 0 ? 1 : 0);
        betData.setTraceWinStop(cart.isStopOnWin());
        betData.setBalls(cart.getCodeData());
        betData.setOrders(cart.getOrdersMap());
        betData.setAmount(cart.getPlanAmount());

        try {
            String bet = GsonHelper.toJson(betData).toString();
            PayMoneyCommand payMoneyCommand = new PayMoneyCommand();
            payMoneyCommand.set_betdata(bet);
            payMoneyCommand.set_token(userCentre.getUserInfo().getToken());
            payMoneyCommand.setSign(DigestUtils.md5Hex(URLEncoder.encode("action=bet&betdata=" + bet + "&packet=Game&token=" + userCentre.getUserInfo().getToken(), "UTF-8") + userCentre.getKeyApiKey()));

            if (!timingView.getIssue().isEmpty()) {
                CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
                builder.setMessage(Html.fromHtml(msg).toString());
                builder.setTitle("温馨提示");
                builder.setLayoutSet(DialogLayout.SINGLE);
                builder.setPositiveButton("确认投注", (dialog, which) -> {
                    isInTraceState = cart.getTraceNumber() > 0;
                    shopping_buy.setEnabled(false);
                    shopping_buy.setBackgroundResource(R.drawable.button_type_un);
                    unusualInfo = ConstantInformation.gatherInfo(getActivity(), payMoneyCommand);
                    loadBetting(payMoneyCommand);
                    dialog.dismiss();
                });
                builder.create().show();
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
        }

    }

    /**
     * 投注成功提示窗
     *
     * @param orderFind
     */
    private void receiptOrderDialog(final OrderFind orderFind) {
        CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
        builder.setMessage(orderFind.getId() > 0 ? "投注成功" : "投注失败!请重新投注");
        builder.setTitle(orderFind.getId() > 0 ? "投注成功" : "投注失败");
        builder.setLayoutSet(DialogLayout.UP_AND_DOWN);
        builder.setNegativeButton("查看投注记录", (dialog, which) -> {
            dialog.dismiss();
            if (orderFind.getId() > 0) {
                if (isInTraceState) {
                    Trace trace = new Trace();
                    trace.setId(orderFind.getId());
                    trace.setLotteryId(lottery.getId());
                    BetOrTraceDetailFragment.launch(ShoppingFragment.this, trace);
                } else {
                    Bet bet = new Bet();
                    bet.setId(orderFind.getId());
                    bet.setLotteryId(lottery.getId());
                    BetOrTraceDetailFragment.launch(ShoppingFragment.this, bet);
                }
            }
            getActivity().finish();
        });

        builder.setPositiveButton("继续投注", (dialog, which) -> {
            getActivity().finish();
            dialog.dismiss();
        });
        CustomDialog dialog = builder.create();
        dialog.setOnDismissListener((d) -> getActivity().finish());
        dialog.show();
    }

    /**
     * 错误参数与成功参数提示窗
     *
     * @param title
     * @param msg
     * @param track
     */
    private void tipDialog(String title, String msg, final int track) {
        CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
        builder.setMessage(msg);
        builder.setTitle(title);
        builder.setLayoutSet(DialogLayout.SINGLE);
        builder.setPositiveButton("知道了", (dialog, which) -> {
            dialog.dismiss();
            if (track == TRACK_TURNED_PAGE_PICK) {
                getActivity().finish();
            }
        });
        builder.create().show();
    }

    /**
     * 投注请求
     *
     * @param command
     */
    private void loadBetting(PayMoneyCommand command) {
        TypeToken typeToken = new TypeToken<RestResponse<OrderFind>>() {
        };
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), command, typeToken, restCallback, BUY_TRACE_ID, this);
        restRequest.execute();
    }

    /**
     * 追号期数请求
     */
    private void loadTraceIssue() {
        TraceIssueCommand traceListCommand = new TraceIssueCommand();
        traceListCommand.setLottery_id(lottery.getId());
        traceListCommand.setToken(XinLeApp.getUserCentre().getUserInfo().getToken());
        TypeToken typeToken = new TypeToken<RestResponse<ArrayList<TraceIssue>>>() {
        };
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), traceListCommand, typeToken, restCallback, BUY_TRACE_ISSUE_ID, this);
        restRequest.execute();
    }

    /**
     * 彩种返点
     */
    private void loadLotteryRebate() {
        RegChildRebateCommand lotteryListCommand = new RegChildRebateCommand();
        lotteryListCommand.setLottery_id(lottery.getId());
        lotteryListCommand.setToken(XinLeApp.getUserCentre().getUserInfo().getToken());
        TypeToken typeToken = new TypeToken<RestResponse<RegChildRebate>>() {
        };
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), lotteryListCommand, typeToken, restCallback, BUY_REBATE_ID, this);
        restRequest.execute();
    }

    private RestCallback restCallback = new RestCallback() {
        @Override
        public boolean onRestComplete(RestRequest request, RestResponse response) {
            switch (request.getId()) {
                case BUY_TRACE_ID:
                    OrderFind orderFind = (OrderFind) response.getData();
                    if (orderFind != null) {
                        cart.clear();
                        initInfo();
                        shopping_buy.setEnabled(true);
                        shopping_buy.setBackgroundResource(R.drawable.button_type);
                        adapter.notifyDataSetChanged();
                        receiptOrderDialog(orderFind);
                    }
                    break;
                case ID_USER_INFO:
                    UserInfo userInfo = ((UserInfo) response.getData());
                    userCentre.setUserInfo(userInfo);
                    if (userInfo != null) {
                        balanceText.setText(String.format("余额：%.4f", userInfo.getAbalance()));
                    }
                    break;
                case BUY_REBATE_ID:
                    RegChildRebate rebate = (RegChildRebate) response.getData();
                    if (rebate != null && rebate.getGroups().size() > 0) {
                        shroudView.setRebate(Integer.parseInt(rebate.getGroups().get(0)), Integer.parseInt(rebate.getGroups().get(rebate.getGroups().size() - 1)));
                    } else {
                        shroudView.setRebate(0, 0);
                    }
                    break;
                case BUY_TRACE_ISSUE_ID:
                    ShoppingCart.getInstance().addTraceIssue((ArrayList<TraceIssue>) response.getData());
                    break;
            }
            return true;
        }

        @Override
        public boolean onRestError(RestRequest request, int errCode, String errDesc) {
            shopping_buy.setEnabled(true);
            shopping_buy.setBackgroundResource(R.drawable.button_type);
            if(errCode == 3004 || errCode == 2016){
                signOutDialog(getActivity(),errCode);
                return true;
            } if (errCode == 2220) {
                showToast(errDesc, Toast.LENGTH_LONG);
                MobclickAgent.reportError(getActivity(), unusualInfo);
                Log.e(TAG, unusualInfo);
                return true;
            }
            if (errCode == 4061){
                showToast(errDesc, Toast.LENGTH_LONG);
                return true;
            }
            return false;
        }

        @Override
        public void onRestStateChanged(RestRequest request, @RestRequest.RestState int state) {
        }
    };

}
