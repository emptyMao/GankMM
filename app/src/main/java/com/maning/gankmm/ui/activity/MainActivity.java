package com.maning.gankmm.ui.activity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.maning.gankmm.R;
import com.maning.gankmm.bean.AppUpdateInfo;
import com.maning.gankmm.bean.GankEntity;
import com.maning.gankmm.bean.WeatherBeseEntity;
import com.maning.gankmm.bean.mob.MobUserInfo;
import com.maning.gankmm.constant.Constants;
import com.maning.gankmm.skin.SkinBroadcastReceiver;
import com.maning.gankmm.skin.SkinManager;
import com.maning.gankmm.ui.activity.login.LoginActivity;
import com.maning.gankmm.ui.activity.login.UserInfoActivity;
import com.maning.gankmm.ui.base.BaseActivity;
import com.maning.gankmm.ui.fragment.CategoryFragment;
import com.maning.gankmm.ui.fragment.HistoryFragment;
import com.maning.gankmm.ui.fragment.WelFareFragment;
import com.maning.gankmm.ui.fragment.collect.CollectFragment;
import com.maning.gankmm.ui.iView.IMainView;
import com.maning.gankmm.ui.presenter.impl.MainPresenterImpl;
import com.maning.gankmm.utils.DialogUtils;
import com.maning.gankmm.utils.IntentUtils;
import com.maning.gankmm.utils.MySnackbar;
import com.maning.gankmm.utils.NetUtils;
import com.maning.gankmm.utils.NotifyUtil;
import com.maning.gankmm.utils.PermissionUtils;
import com.maning.gankmm.utils.SharePreUtil;
import com.maning.gankmm.utils.UserUtils;
import com.maning.updatelibrary.InstallUtils;
import com.socks.library.KLog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements IMainView, View.OnClickListener {

    @Bind(R.id.navigationView)
    NavigationView navigationView;
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    //头部布局
    private ImageView header_iv_weather;
    private RelativeLayout rl_weather;
    private TextView header_tv_temperature;
    private TextView header_tv_other;
    private TextView header_tv_city_name;
    private LinearLayout header_ll_choose_city;

    private Context context;
    private WelFareFragment welFareFragment;
    private CollectFragment collectFragment;
    private CategoryFragment categoryFragment;
    private HistoryFragment timeFragment;

    private int navigationCheckedItemId = R.id.nav_fuli;
    private String navigationCheckedTitle = "福利";
    private static final String savedInstanceStateItemId = "navigationCheckedItemId";
    private static final String savedInstanceStateTitle = "navigationCheckedTitle";

    private long exitTime = 0;

    private MainPresenterImpl mainPresenter;
    //夜间模式的广播
    private SkinBroadcastReceiver skinBroadcastReceiver;
    private MaterialDialog dialogUpdate;
    private static final int citysChooseRequestCode = 10001;

    private NotifyUtil notifyUtils;

    private WeatherBeseEntity.WeatherBean weatherEntity;

    private List<GankEntity> welFareList;
    private String provinceName;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        context = this;
        mainPresenter = new MainPresenterImpl(this, this);

        initMyToolBar();

        initNavigationView();

        initIntent();

        mainPresenter.initDatas();
        mainPresenter.initAppUpdate();
        mainPresenter.initFeedBack();
        mainPresenter.getCitys();
        mainPresenter.getLocationInfo();

        initOtherDatas(savedInstanceState);

        setDefaultFragment();

        //注册夜间模式广播监听
        registerSkinReceiver();

    }

    public void refreshLocationInfo() {
        if (mainPresenter != null) {
            mainPresenter.getLocationInfo();
        }
    }

    private void initOtherDatas(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getInt(savedInstanceStateItemId) != 0) {
            navigationCheckedItemId = savedInstanceState.getInt(savedInstanceStateItemId);
            navigationCheckedTitle = savedInstanceState.getString(savedInstanceStateTitle);
        }
    }

    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(toolbar, Constants.FlagWelFare, R.drawable.gank_icon_menu_white);
        } else {
            initToolBar(toolbar, Constants.FlagWelFare, R.drawable.gank_icon_menu_night);
        }
    }

    private void initIntent() {
        Intent intent = getIntent();
        String pushMessage = intent.getStringExtra(IntentUtils.PushMessage);
        if (!TextUtils.isEmpty(pushMessage)) {
            DialogUtils.showMyDialog(this,
                    getString(R.string.gank_dialog_title_notify),
                    pushMessage,
                    getString(R.string.gank_dialog_confirm),
                    "", null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.item_search:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                return true;
            case R.id.item_login:
                //判断是不是登录了
                MobUserInfo userCache = UserUtils.getUserCache();
                if (userCache != null && !TextUtils.isEmpty(userCache.getUid())) {
                    //跳转资料页面
                    startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);
        return true;
    }


    /**
     * 设置默认的Fragment显示：如果savedInstanceState不是空，证明activity被后台销毁重建了，之前有fragment，就不再创建了
     */
    private void setDefaultFragment() {
        setMenuSelection(navigationCheckedItemId);
    }

    private void setMenuSelection(int flag) {
        toolbar.setTitle(navigationCheckedTitle);

        // 开启一个Fragment事务
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(fragmentTransaction);
        switch (flag) {
            case R.id.nav_fuli:
                if (welFareFragment == null) {
                    welFareFragment = WelFareFragment.newInstance();
                    fragmentTransaction.add(R.id.frame_content, welFareFragment);
                } else {
                    fragmentTransaction.show(welFareFragment);
                }
                break;
            case R.id.nav_history:
                if (timeFragment == null) {
                    timeFragment = HistoryFragment.newInstance();
                    fragmentTransaction.add(R.id.frame_content, timeFragment);
                } else {
                    fragmentTransaction.show(timeFragment);
                }
                break;
            case R.id.nav_category:
                if (categoryFragment == null) {
                    categoryFragment = CategoryFragment.newInstance();
                    fragmentTransaction.add(R.id.frame_content, categoryFragment);
                } else {
                    fragmentTransaction.show(categoryFragment);
                }
                break;
            case R.id.nav_collect:
                if (collectFragment == null) {
                    collectFragment = CollectFragment.newInstance();
                    fragmentTransaction.add(R.id.frame_content, collectFragment);
                } else {
                    fragmentTransaction.show(collectFragment);
                }
                break;

        }
        fragmentTransaction.commit();

    }

    private void hideFragments(FragmentTransaction transaction) {
        if (welFareFragment != null) {
            transaction.hide(welFareFragment);
        }
        if (collectFragment != null) {
            transaction.hide(collectFragment);
        }
        if (categoryFragment != null) {
            transaction.hide(categoryFragment);
        }
        if (timeFragment != null) {
            transaction.hide(timeFragment);
        }

    }


    /**
     * -----------------------------------------
     */
    private void initNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                setTitle(menuItem.getTitle()); // 改变页面标题，标明导航状态
                drawerLayout.closeDrawers(); // 关闭导航菜单
                switch (menuItem.getItemId()) {
                    case R.id.nav_fuli:
                    case R.id.nav_history:
                    case R.id.nav_category:
                    case R.id.nav_collect:
                        navigationCheckedItemId = menuItem.getItemId();
                        navigationCheckedTitle = menuItem.getTitle().toString();
                        setMenuSelection(menuItem.getItemId());
                        break;
                    case R.id.nav_codes:
                        menuItem.setCheckable(false);
                        //泡在网上的日子
                        Intent intent01 = new Intent(MainActivity.this, CodesActivity.class);
                        intent01.putExtra(CodesActivity.IntentType, CodesActivity.IntentTypeName_Jcode);
                        startActivity(intent01);
                        break;
                    case R.id.nav_cocoa_china:
                        menuItem.setCheckable(false);
                        //CocoaChina
                        Intent intent = new Intent(MainActivity.this, CodesActivity.class);
                        intent.putExtra(CodesActivity.IntentType, CodesActivity.IntentTypeName_CocoaChina);
                        startActivity(intent);
                        break;
                    case R.id.nav_more:
                        menuItem.setCheckable(false);
                        //更多功能
                        IntentUtils.startMoreActivity(context);
                        break;
                    case R.id.about:
                        menuItem.setChecked(false); // 改变item选中状态
                        //跳转
                        IntentUtils.startAboutActivity(context);
                        break;
                    case R.id.setting:
                        menuItem.setChecked(false); // 改变item选中状态
                        //跳转
                        IntentUtils.startSettingActivity(context);
                        break;
                    case R.id.share_app:
                        menuItem.setChecked(false); // 改变item选中状态
                        //分享
                        IntentUtils.startAppShareText(context, "干货营", "干货营Android客户端：" + getString(R.string.download_url));
                        break;
                    case R.id.my_qr_code:
                        menuItem.setChecked(false); // 改变item选中状态
                        //我的二维码
                        IntentUtils.startQRCodeActivity(context);
                        break;
                    case R.id.my_support_pay:
                        menuItem.setChecked(false); // 改变item选中状态
                        //打赏作者
                        IntentUtils.startSupportPayActivity(context);
                        break;
                }
                return true;
            }
        });

        View headerLayout = navigationView.inflateHeaderView(R.layout.drawer_header);
        header_iv_weather = (ImageView) headerLayout.findViewById(R.id.header_iv_weather);
        rl_weather = (RelativeLayout) headerLayout.findViewById(R.id.rl_weather);
        header_tv_temperature = (TextView) headerLayout.findViewById(R.id.header_tv_temperature);
        header_tv_other = (TextView) headerLayout.findViewById(R.id.header_tv_other);
        header_tv_city_name = (TextView) headerLayout.findViewById(R.id.header_tv_city_name);
        header_ll_choose_city = (LinearLayout) headerLayout.findViewById(R.id.header_ll_choose_city);
        header_ll_choose_city.setOnClickListener(this);
        rl_weather.setOnClickListener(this);
    }

    private void registerSkinReceiver() {
        if (skinBroadcastReceiver == null) {
            skinBroadcastReceiver = new SkinBroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    welFareFragment = null;
                    collectFragment = null;
                    categoryFragment = null;
                    timeFragment = null;
                    recreate();
                }
            };
            SkinManager.registerSkinReceiver(MainActivity.this, skinBroadcastReceiver);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(savedInstanceStateItemId, navigationCheckedItemId);
        outState.putString(savedInstanceStateTitle, navigationCheckedTitle);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawers();
            return;
        }
        long currtTime = System.currentTimeMillis();
        if (currtTime - exitTime > 2000) {
            MySnackbar.makeSnackBarBlack(toolbar, getString(R.string.gank_hint_exit_app));
            exitTime = currtTime;
            return;
        }

        //退出程序
        this.finish();

        // 不退出程序，进入后台
//        moveTaskToBack(true);

        //使 App 进入后台而不是关闭
//        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
//        launcherIntent.addCategory(Intent.CATEGORY_HOME);
//        startActivity(launcherIntent);

    }

    @Override
    protected void onDestroy() {
        mainPresenter.destroyLocation();
        mainPresenter.detachView();
        SkinManager.unregisterSkinReceiver(this, skinBroadcastReceiver);
        skinBroadcastReceiver = null;

        super.onDestroy();
    }

    @Override
    public void showToast(String msg) {
        MySnackbar.makeSnackBarBlack(navigationView, msg);
    }

    @Override
    public void showAppUpdateDialog(final AppUpdateInfo appUpdateInfo) {
        String title = "检测到新版本:V" + appUpdateInfo.getVersionShort();
        Double appSize = Double.parseDouble(appUpdateInfo.getBinary().getFsize() + "") / 1024 / 1024;
        DecimalFormat df = new DecimalFormat(".##");
        String resultSize = df.format(appSize) + "M";
        boolean isWifi = NetUtils.isWifiConnected(this);
        String content = appUpdateInfo.getChangelog() +
                "\n\n新版大小：" + resultSize +
                "\n当前网络：" + (isWifi ? "wifi" : "非wifi环境(注意)");

        DialogUtils.showMyDialog(this,
                title, content, "立马更新", "稍后更新",
                new DialogUtils.OnDialogClickListener() {
                    @Override
                    public void onConfirm() {
                        //更新版本
                        showDownloadDialog(appUpdateInfo);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }


    private void showDownloadDialog(AppUpdateInfo appUpdateInfo) {
        dialogUpdate = new MaterialDialog.Builder(MainActivity.this)
                .title("正在下载最新版本")
                .content("请稍等")
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .progress(false, 100, false)
                .negativeText("后台下载")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startNotifyProgress();
                    }
                })
                .show();
        InstallUtils.with(context)
                .setApkUrl(appUpdateInfo.getInstall_url())
                .setApkName("GankMM")
                .setCallBack(new InstallUtils.DownloadCallBack() {
                    @Override
                    public void onStart() {
                        KLog.i("installAPK-----onStart");
                        if (dialogUpdate != null) {
                            dialogUpdate.setProgress(0);
                        }
                    }

                    @Override
                    public void onComplete(String path) {
                        KLog.i("installAPK----onComplete:" + path);
                        /**
                         * 安装APK工具类
                         * @param context       上下文
                         * @param filePath      文件路径
                         * @param authorities   ---------Manifest中配置provider的authorities字段---------
                         * @param callBack      安装界面成功调起的回调
                         */
                        InstallUtils.installAPK(context, path, new InstallUtils.InstallCallBack() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(context, "正在安装程序", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFail(Exception e) {
                                Toast.makeText(context, "安装失败:" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        if (dialogUpdate != null && dialogUpdate.isShowing()) {
                            dialogUpdate.dismiss();
                        }
                        if (notifyUtils != null) {
                            notifyUtils.setNotifyProgressComplete();
                            notifyUtils.clear();
                        }
                    }

                    @Override
                    public void onLoading(long total, long current) {
                        KLog.i("installAPK-----onLoading:-----total:" + total + ",current:" + current);
                        int currentProgress = (int) (current * 100 / total);
                        if (dialogUpdate != null) {
                            dialogUpdate.setProgress(currentProgress);
                        }
                        if (notifyUtils != null) {
                            notifyUtils.setNotifyProgress(100, currentProgress, false);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        if (dialogUpdate != null && dialogUpdate.isShowing()) {
                            dialogUpdate.dismiss();
                        }
                        if (notifyUtils != null) {
                            notifyUtils.clear();
                        }
                    }

                    @Override
                    public void cancle() {

                    }
                })
                .startDownload();

    }

    /**
     * 开启通知栏
     */
    private void startNotifyProgress() {
        //设置想要展示的数据内容
        Intent intent = new Intent(this, SettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent rightPendIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        int smallIcon = R.mipmap.ic_launcher;
        String ticker = "正在下载干货营更新包...";
        //实例化工具类，并且调用接口
        notifyUtils = new NotifyUtil(this, 0);
        notifyUtils.notify_progress(rightPendIntent, smallIcon, ticker, "干货营 下载", "正在下载中...", false, false, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_ll_choose_city:
                //切换城市
                Intent intent = new Intent(MainActivity.this, CitysActivity.class);
                startActivityForResult(intent, citysChooseRequestCode);
                break;
            case R.id.rl_weather:
                //切换城市
                if (weatherEntity != null) {
                    Intent intent_weather = new Intent(MainActivity.this, WeatherActivity.class);
                    intent_weather.putExtra(WeatherActivity.intentKey_weatherBean, weatherEntity);
                    intent_weather.putExtra(WeatherActivity.intentKey_weatherProvinceName, provinceName);
                    intent_weather.putExtra(WeatherActivity.intentKey_weatherCityName, cityName);
                    if (welFareList != null && welFareList.size() > 0) {
                        intent_weather.putStringArrayListExtra(WeatherActivity.intentKey_bg_url, (ArrayList) welFareList);
                    }
                    startActivity(intent_weather);
                }
                break;
        }
    }

    @Override
    public void initWeatherInfo(WeatherBeseEntity.WeatherBean weatherEntity) {
        //初始化天气信息
        this.weatherEntity = weatherEntity;
        //当前温度
        String temperature = weatherEntity.getTemperature();
        //空气
        String airCondition = weatherEntity.getAirCondition();
        //天气
        String weather = weatherEntity.getWeather();
        //城市
        String cityName = weatherEntity.getCity();
        //赋值
        header_tv_temperature.setText(temperature);
        header_tv_other.setText(weather + "  空气" + airCondition);
        header_iv_weather.setImageDrawable(getResources().getDrawable(SharePreUtil.getIntData(context, weather, R.drawable.icon_weather_none)));
        header_tv_city_name.setText(cityName);
    }

    @Override
    public void updateLocationInfo(String provinceName, String cityName) {
        this.provinceName = provinceName;
        this.cityName = cityName;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == citysChooseRequestCode) {
            if (data != null) {
                provinceName = data.getStringExtra("provinceName");
                cityName = data.getStringExtra("cityName");
                mainPresenter.getCityWeather(provinceName, cityName);
            }
        }
    }

    public void setPicList(List<GankEntity> welFareList) {
        this.welFareList = welFareList;
    }

}
