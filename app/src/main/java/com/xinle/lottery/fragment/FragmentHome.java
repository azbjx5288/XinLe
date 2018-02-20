package com.xinle.lottery.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xinle.lottery.R;
import com.xinle.lottery.app.BaseFragment;
import com.xinle.lottery.app.XinLeApp;
import com.xinle.lottery.base.net.RestCallback;
import com.xinle.lottery.base.net.RestRequest;
import com.xinle.lottery.base.net.RestRequestManager;
import com.xinle.lottery.base.net.RestResponse;
import com.xinle.lottery.component.AdvertisementView;
import com.xinle.lottery.component.AppBarLayoutOverScrollViewBehavior;
import com.xinle.lottery.component.CommonTabLayout;
import com.xinle.lottery.component.CustomTabEntity;
import com.xinle.lottery.component.CycleViewPager;
import com.xinle.lottery.component.FragImageView;
import com.xinle.lottery.component.HomeFragmentPagerAdapter;
import com.xinle.lottery.component.OnSortClickListener;
import com.xinle.lottery.component.OnTabSelectListener;
import com.xinle.lottery.component.RoundProgressBar;
import com.xinle.lottery.component.ViewFactory;
import com.xinle.lottery.data.ActivityCommand;
import com.xinle.lottery.data.ActivityData;
import com.xinle.lottery.data.BannerListCommand;
import com.xinle.lottery.data.Lottery;
import com.xinle.lottery.data.LotteryListCommand;
import com.xinle.lottery.data.Notice;
import com.xinle.lottery.data.Series;
import com.xinle.lottery.data.SeriesCommand;
import com.xinle.lottery.material.ConstantInformation;
import com.xinle.lottery.pattern.TabEntity;
import com.xinle.lottery.pattern.VersionChecker;
import com.xinle.lottery.user.UserCentre;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

/**
 * Created on 2016/01/04.
 *
 * @author ACE
 * @功能描述: 首页
 */

public class FragmentHome extends BaseFragment {
    private static final String TAG = FragmentHome.class.getSimpleName();
    private static final int ACTIVITY_ID = 1;
    private static final int BANNER_LIST_ID = 2;
    private static final int LOTTERY_TRACE_ID = 3;
    private static final int ACTIVITY_CLICK_ID = 4;
    private static final int SERIES_ID = 5;

    @BindView(R.id.container)
    CoordinatorLayout container; //拖拽反应区域
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.uc_progressbar)
    RoundProgressBar progressBar;
    @BindView(R.id.uc_kefu_iv)
    ImageView mKefuIv;
    @BindView(R.id.uc_msg_iv)
    ImageView mMsgIv;
    @BindView(R.id.uc_tablayout)
    CommonTabLayout mTablayout;

    @BindView(R.id.tag_ocreate_ll)
    LinearLayout tagOcreate;
    @BindView(R.id.tagcontainerLayout)
    TagContainerLayout mTagContainerLayout;
    @BindView(R.id.uc_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.imageView)
    FragImageView imageView;

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private List<Fragment> fragments;
    private CycleViewPager cycleViewPager;
    private ArrayList<Notice> notices;
    private ActivityData activityData = null;
    private UserCentre userCentre = null;
    private AdvertisementView advertisementView = null;
    private int site = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflateView(inflater, container, getActivity().getResources().getString(R.string.app_name), R.layout.fragment_home,true,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new VersionChecker(this).startCheck();
        getSeries();
        initTab();
        initListener();
    }

    /**
     * 初始化tab
     */
    private void initTab() {
        userCentre = XinLeApp.getUserCentre();
        fragments = getFragments();
        HomeFragmentPagerAdapter myFragmentPagerAdapter = new HomeFragmentPagerAdapter(getFragmentManager(), fragments, getNames());
        mTablayout.setIconGravity(Gravity.LEFT);
        mTablayout.setTabData(mTabEntities);
        mViewPager.setAdapter(myFragmentPagerAdapter);
    }

    /**
     * 绑定事件
     */
    private void initListener() {
        //Banner
        cycleViewPager = (CycleViewPager) getActivity().getFragmentManager().findFragmentById(R.id.uc_zoomiv);
        //活动控键
        advertisementView = new AdvertisementView(this, R.style.Dialog);
        mAppBarLayout.addOnOffsetChangedListener((AppBarLayout appBarLayout, int verticalOffset) -> {
            /*float percent = Float.valueOf(Math.abs(verticalOffset)) / Float.valueOf(appBarLayout.getTotalScrollRange());
            if (mKefuIv != null && mMsgIv != null) {
                StatusBarUtil.setTranslucentForImageView(getActivity(), (int) (255f * percent), null);
            }*/
        });
        AppBarLayoutOverScrollViewBehavior myAppBarLayoutBehavoir = (AppBarLayoutOverScrollViewBehavior) ((CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams()).getBehavior();
        myAppBarLayoutBehavoir.setOnProgressChangeListener((float progress, boolean isRelease) -> {
            progressBar.setProgress((int) (progress * 360));
            if (progress == 1 && !progressBar.isSpinning && isRelease) {
                // 刷新viewpager里的fragment
            }
            if (mMsgIv != null) {
                if (progress == 0 && !progressBar.isSpinning) {
                    mMsgIv.setVisibility(View.VISIBLE);
                } else if (progress > 0 && mKefuIv.getVisibility() == View.VISIBLE) {
                    mMsgIv.setVisibility(View.INVISIBLE);
                }
            }
        });
        mTablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                selectPage(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        //大厅选择
        mTablayout.setOnSortClickListener(new OnSortClickListener() {
            @Override
            public void onSortClick(View v, boolean dupo) {
                if (site == 0 && dupo) {
                    tagOcreate.setVisibility(View.VISIBLE);
                } else {
                    tagOcreate.setVisibility(View.GONE);
                }
            }
        });

        //选择分类
        mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                if(site == 0){
                   List<Series> seriess= XinLeApp.getUserCentre().getSeriesList();
                   if(seriess.size()>0){
                       if(position!=0){
                           Series series=seriess.get(--position);
                           ((LottoFragment)fragments.get(0)).notifyData(series.getId(),false);
                       }else{
                           ((LottoFragment)fragments.get(0)).notifyData(0,true);
                       }
                   }
                }
            }

            @Override
            public void onTagLongClick(final int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mTablayout.setCurrentTab(position);
                selectPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        loadBanner();
    }

    public String[] getNames() {
        String[] mNames = new String[]{"彩票大厅", "GA游戏"};
        mTabEntities.add(new TabEntity(mNames[0], R.drawable.ic_tap_lottery_pressed, R.drawable.ic_tap_lottery, true, R.drawable.arrow_down));
        mTabEntities.add(new TabEntity(mNames[1], R.drawable.ic_tap_game_pressed, R.drawable.ic_tap_game, false, R.drawable.arrow_down));
        return mNames;
    }

    private List<Lottery> item=new ArrayList<>();

    public List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new LottoFragment());
        fragments.add(new GaFragment());
        return fragments;
    }

    private void updateBanner(ArrayList<Notice> notices) {
        this.notices = notices;
        if (notices == null || notices.size() == 0) {
            return;
        }
        initialize();
    }

    /**
     * 选择某页
     * @param position 页面的位置
     */
    private void selectPage(int position) {
        site = position;
        mTablayout.setSortIcon(site==0?true:false);
        tagOcreate.setVisibility(View.GONE);
        if(site==0) {
            ((LottoFragment) fragments.get(site)).notifyData(item);
        }else{
            ((GaFragment) fragments.get(site)).notifyData(item);
        }
        mViewPager.setCurrentItem(position);
    }

    private void loadBanner() {
        if(XinLeApp.getUserCentre().getUserInfo()==null){
            return;
        }
        BannerListCommand command = new BannerListCommand();
        command.setToken(XinLeApp.getUserCentre().getUserInfo().getToken());
        TypeToken typeToken = new TypeToken<RestResponse<ArrayList<Notice>>>() {
        };
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), command, typeToken, restCallback, BANNER_LIST_ID, this);
        restRequest.execute();
    }

    private void lotteryListLoad() {
        if(XinLeApp.getUserCentre().getUserInfo()==null){
            return;
        }
        LotteryListCommand lotteryListCommand = new LotteryListCommand();
        lotteryListCommand.setToken(XinLeApp.getUserCentre().getUserInfo().getToken());
        TypeToken typeToken = new TypeToken<RestResponse<ArrayList<Lottery>>>() {
        };
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), lotteryListCommand, typeToken, restCallback, LOTTERY_TRACE_ID, this);
        RestResponse restResponse = restRequest.getCache();
        if (restResponse != null && restResponse.getData() instanceof ArrayList<?>) {
            item=(ArrayList<Lottery>) restResponse.getData();
        }
        restRequest.execute();
    }

    private void getSeries() {
        if(XinLeApp.getUserCentre().getUserInfo()==null){
            return;
        }
        SeriesCommand command = new SeriesCommand();
        command.setToken(XinLeApp.getUserCentre().getUserInfo().getToken());
        TypeToken typeToken = new TypeToken<RestResponse<List<Series>>>() {
        };
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), command, typeToken, restCallback, SERIES_ID, this);
        restRequest.execute();
    }

    private void activityLoad() {
        if(XinLeApp.getUserCentre().getUserInfo()==null){
            return;
        }
        ActivityCommand command = new ActivityCommand();
        command.setUserdId(XinLeApp.getUserCentre().getUserInfo().getId());
        command.setToken(XinLeApp.getUserCentre().getUserInfo().getToken());
        TypeToken typeToken = new TypeToken<RestResponse<ActivityData>>() {
        };
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), command, typeToken, restCallback, ACTIVITY_ID, this);
        restRequest.execute();
    }

    private void activityClick() {
        if(XinLeApp.getUserCentre().getUserInfo()==null){
            return;
        }
        ActivityCommand command = new ActivityCommand();
        command.setUserdId(XinLeApp.getUserCentre().getUserInfo().getId());
        command.setToken(XinLeApp.getUserCentre().getUserInfo().getToken());
        TypeToken typeToken = new TypeToken<RestResponse<ActivityData>>() {
        };
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), command, typeToken, restCallback, ACTIVITY_CLICK_ID, this);
        restRequest.execute();
    }

    @OnClick(R.id.uc_msg_iv)
    public void onMsgClick() {
        launchFragment(NoticeListFragment.class);
    }

    @OnClick(R.id.uc_kefu_iv)
    public void onKefuClick() {
        launchFragment(ServiceCenterFragment.class);
    }

    @SuppressLint("NewApi")
    private void initialize() {
        if (notices == null || notices.size() == 0) {
            return;
        }

        String[] imageUris = new String[notices.size()];
        for (int i = 0, size = notices.size(); i < size; i++) {
            imageUris[i] = notices.get(i).getPath();
        }
        List<View> views = new ArrayList<>();
        // 将最后一个vie
        // w添加进来
        views.add(ViewFactory.getImageView(getActivity(), notices.get(notices.size() - 1).getPath()));
        for (int i = 0, size = notices.size(); i < size; i++) {
            views.add(ViewFactory.getImageView(getActivity(), notices.get(i).getPath()));
        }
        // 将第一个view添加进来
        views.add(ViewFactory.getImageView(getActivity(), notices.get(0).getPath()));

        cycleViewPager.setCycle(true);
        cycleViewPager.setData(views, mAdCycleViewListener);
        cycleViewPager.setWheel(true);
    }


    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {
        @Override
        public void onImageClick(int position, View imageView) {
            if (TextUtils.isEmpty(notices.get(position).getUrl())) {
                NoticeDetailsFragment.launch(FragmentHome.this, notices.get(position));
            } else {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(notices.get(position).getUrl()));
                startActivity(browserIntent);
            }
        }
    };

    private void initActivity() {
        if (activityData == null) {
            imageView.clearAnimation();
            imageView.setImageResource(R.color.colorTransparent);
            imageView.setVisibility(View.GONE);
            imageView.setEnabled(false);
            return;
        }
        if (advertisementView.isShowing()) {
            advertisementView.dismiss();
        }

        String url = activityData.getIcon();
        if(!TextUtils.isEmpty(url)){
            Glide.with(this).load(url).asBitmap().priority(Priority.HIGH).into(target);
        }

        if (!activityData.isPartIn()) {
            imageView.setVisibility(View.GONE);
            imageView.setEnabled(false);
            advertisementView.setActivityData(activityData);
            advertisementView.setCloseVisibility(View.GONE);
            advertisementView.setStrokeColor(activityData.getColor());
            advertisementView.setFillColor(activityData.getColor());
            advertisementView.setButtonPadding(20, 0, 20, 0);
            advertisementView.setOnAccedingListener(() -> {
                showImageView();
            });
            advertisementView.setOnCancelListener((DialogInterface dialog) -> {
                showImageView();
            });
            advertisementView.showDialog();
        } else {
            showImageView();
        }

        imageView.setOnActivityClickListener(() -> {
            if (ConstantInformation.isFastClick()) {
                activityClick();
            }
        });
    }

    private void showImageView(){
        imageView.setEnabled(true);
        imageView.setVisibility(View.VISIBLE);
    }

    private void activitylaunch() {
        if (activityData == null) {
            if (imageView != null) {
                imageView.clearAnimation();
                imageView.setImageResource(R.color.colorTransparent);
                imageView.setVisibility(View.GONE);
                imageView.setEnabled(false);
                showToast("活动已结束!谢谢参与");
            }
            return;
        }
        if (activityData.isPartIn()) {
            AdvertisementFragment.launch(this, activityData);//fragment.getActivity(), AdvertisementFragment.class, bundle
        } else {
            initActivity();
        }
    }

    SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageBitmap(resource);
                }
            });
        }
    };

    Handler mHandler=new Handler();

    @Override
    public void onResume() {
        super.onResume();
        lotteryListLoad();
        if(imageView!=null) {
            imageView.setDrag(getActivity(), imageView);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    activityLoad(); //活动方法
                }
            },3000);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (cycleViewPager != null) {
            cycleViewPager.onDestroyView();
        }
    }

    private RestCallback restCallback = new RestCallback<ArrayList<?>>() {
        @Override
        public boolean onRestComplete(RestRequest request, RestResponse response) {
            switch (request.getId()) {
                case BANNER_LIST_ID:
                    updateBanner((ArrayList<Notice>) response.getData());
                    break;
                case LOTTERY_TRACE_ID:
                    item=(ArrayList<Lottery>) response.getData();
                    selectPage(0);
                    break;
                case ACTIVITY_ID:
                    activityData = (ActivityData) response.getData();
                    initActivity();
                    break;
                case ACTIVITY_CLICK_ID:
                    activityData = (ActivityData) response.getData();
                    activitylaunch();
                    break;
                case SERIES_ID: //获取彩种信息
                    List<Series> series = (List<Series>) response.getData();
                    if (series.size() > 0) {
                        XinLeApp.getUserCentre().setSeriesList(series);
                        mTagContainerLayout.setTags(ConstantInformation.getSort());
                    }
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
            if (state == RestRequest.DONE) {
                if (activityData == null && request.getId() == ACTIVITY_ID) {
                    imageView.setVisibility(View.GONE);
                    imageView.setEnabled(false);
                }
            }
        }
    };

}
