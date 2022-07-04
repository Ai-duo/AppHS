package com.add.appxm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.add.appxm.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.add.appxm.BR.backIndex;
import static com.add.appxm.BR.dm5d;
import static com.add.appxm.BR.dmrd;
import static com.add.appxm.BR.live;

public class MainActivity extends FragmentActivity {
    ActivityMainBinding mainBinding;
    FirstFragment firstFragment;
    SecondFragment secondFragment;
    ThirdFragment thirdFragment;
    ImgFragment imgFragment;
    VideoFragment videoFragment;
    FourthFragment fourthFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Log.i("TAG", "onCreate");
        startService();
        initFragment();
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initTask();
        changFragment();
    }

    public void startService() {
        Intent intent = new Intent(this, ElementsService.class);
        startService(intent);
    }

    public void initFragment() {
        firstFragment = new FirstFragment();
        secondFragment = new SecondFragment();
        thirdFragment = new ThirdFragment();
        imgFragment = new ImgFragment();
        videoFragment = new VideoFragment();
        fourthFragment = new FourthFragment();
    }

    Timer timer, timer1, timer2;
    int index =0;
    int page = 4;
    public void changFragment() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                if (index == page*10000) index = 0;
                int result = index % page;
                EventBus.getDefault().post(result);
                if (result == 0) {
                    transaction.replace(R.id.contenter, firstFragment);
                    transaction.commit();
                } else if (result == 1) {
                    transaction.replace(R.id.contenter, secondFragment);
                    transaction.commit();
                } else if (result == 2) {
                    transaction.replace(R.id.contenter, thirdFragment);
                    transaction.commit();
                } else if (result == 5) {
                    transaction.replace(R.id.contenter, fourthFragment);
                    transaction.commit();
                    try {
                        Thread.sleep(20 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (result == 4) {
                    transaction.replace(R.id.contenter, imgFragment);
                    transaction.commit();
                    try {
                        Thread.sleep(25 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (result == 3) {
                    transaction.replace(R.id.contenter, videoFragment);
                    transaction.commit();
                    try {
                        Thread.sleep(110 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                index++;
            }
        }, 0, 10 * 1000);
        timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                final Request live = new Request.Builder().url("http://115.220.4.68:8081/qxdata/QxService.svc/getnewzdzhourdata/K2159").build();
                Request zhish = new Request.Builder().url("http://115.220.4.68:8081/qxdata/QxService.svc/gettszsybdata/K2159").build();

                Request wea = new Request.Builder().url("http://115.220.4.68:8081/qxdata/QxService.svc/getdayybdata/K2159").build();
                Request seven = new Request.Builder().url("http://115.220.4.68:8081/qxdata/QxService.svc/geths7dayybdata/K2159").build();

                client.newCall(live).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String body = response.body().string();
                        if (TextUtils.isEmpty(body) || body.length() < 5) return;
                        Gson gson = new Gson();
                        Live lives = gson.fromJson(body, Live.class);
                        EventBus.getDefault().post(lives);
                    }
                });
                client.newCall(zhish).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String body = response.body().string();
                        if (TextUtils.isEmpty(body) || body.length() < 5) return;
                        Gson gson = new Gson();
                        Zhishu lives = gson.fromJson(body, Zhishu.class);
                        EventBus.getDefault().post(lives);
                    }

                });
                client.newCall(wea).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String body = response.body().string();
                        if (TextUtils.isEmpty(body) || body.length() < 5) return;
                        Gson gson = new Gson();
                        Wea lives = gson.fromJson(body, Wea.class);
                        EventBus.getDefault().post(lives);
                    }

                });
                client.newCall(seven).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String body = response.body().string();
                        Log.i("TAG", "body:" + body);
                        if (TextUtils.isEmpty(body) || body.length() < 5) return;
                        Gson gson = new Gson();
                        SevenWea lives = gson.fromJson(body, SevenWea.class);
                        lives.build();
                        EventBus.getDefault().post(lives);
                    }

                });
            }
        }, 0, 5 * 60 * 1000);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changBackImage(Integer index) {
        mainBinding.setBackIndex(index);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTime(UpdateTime index) {
        Log.i("TAG", "收到：" + index.toString());
        mainBinding.setUpdate(index.time);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateWea(Wea index) {
        Log.i("TAG", "收到：" + index.toString());
        mainBinding.setWea(index);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateSeven(SevenWea index) {
        Log.i("TAG", "收到：" + index.toString());
        thirdFragment.updateInfo(index);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateZhishu(Zhishu index) {
        Log.i("TAG", "收到：" + index.toString());
        secondFragment.updateInfo(index);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateLive(Live live) {

        firstFragment.updateInfo(live);
    }

    public TimerTask dates;
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年M月dd日 HH:mm");

    public void initTask() {

        dates = new TimerTask() {
            @Override
            public void run() {
                Lunar lunar = new Lunar(Calendar.getInstance());
                EventBus.getDefault().post(dateFormat.format(new Date()) + "\n" + lunar.getAllDate());
            }
        };
        timer1 = new Timer();

        timer1.schedule(dates, 0, 40 * 1000);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updataDate(String bean) {
        Log.i("TAG", "时间：" + bean);
        mainBinding.setDate(bean);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}