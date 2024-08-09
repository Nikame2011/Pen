package com.pf.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
//
//import com.google.android.gms.ads.AdError;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.FullScreenContentCallback;
//import com.google.android.gms.ads.LoadAdError;
//import com.google.android.gms.ads.MobileAds;
//import com.google.android.gms.ads.OnUserEarnedRewardListener;
//import com.google.android.gms.ads.initialization.InitializationStatus;
//import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
//import com.google.android.gms.ads.rewarded.RewardItem;
//import com.google.android.gms.ads.rewarded.RewardedAd;
//import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.Date;

/*
требования к демо-версии, пригодной для публикации:
+ улучшения запускаются только на 0-й высоте
+ деньги несчитаются и не отображаются
+ скорость не отображается
+ время улучшений возрастает
+ рекорды для улучшений возрастают и их выполнение возможно
+ увеличение характеристик не резкое
+ первая картинка двигается от 2/3 высоты экрана и до своей высоты
+ вторая картинка двигается,когда первая перестала закрывать весь экран и до своей высоты
+ два языка
+ сохранение прогресса
+ сохранение улучшений
+ улучшения ускоряются кликаньем
+ прыжки усиливаются кликаньем
+ анимация и движение синхронизированы(взмах и ускорение в один момент)
+ анимация на разные виды деятельности

на будущее:
- с изменением высоты картинки заменяются на другие подгружаясь из памяти, ненужные картинки не хранятся
- подсказки в начале игры
- новый вид меню: кнопка прыжка кругом без надписей, улучшения стрелкой влево с анимацией +++ когда доступно
улучшение,над ним кнопка настроек, концентрация и энергия правее чем сейчас, слева  рекорд и количество монет



 */


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    public static boolean flying = false;
    public static boolean setup = false;
    public static int update = -1;
    public ImageButton Fly;
    public ImageButton Setup;
    public ImageButton Up_fly;
    public ImageButton Up_jump;
    public ImageButton Up_energy;
    public ImageButton Reward;
    public ImageButton Config;
    public ImageButton Ask_yes;
    public ImageButton Ask_no;
    ConstraintLayout Ask_l;
    ImageView Ask_image;


    public static int dw, dh;
    public static boolean testing = false;//true;
    public static GameView gw;
    public static Date first_date;
    public static boolean quick_down = false;
    public static boolean new_game = false;

    public static boolean energy_show = false;
    public static float end = 0;
    //public AdRequest adRequest;
    public static Context cont;
    public static String version = "0.1.2.0";

    //public static RewardedAd mRewardedAd;
    private final String TAG = "MainActivity";

    public static byte ask_number;
    public static String[] ask_status;
    public static String[] ask_savedstatus;
    private byte shure = -1;
    TextView ask;
    TextView ask_no;
    TextView ask_yes;
    private boolean need_rew = true;
    private int rew_error = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dw = getResources().getDisplayMetrics().widthPixels;//получаем ширину экрана
        dh = getResources().getDisplayMetrics().heightPixels;//получаем ширину экрана
        first_date = new Date();

        setContentView(R.layout.activity_main);
        cont = this;


        Fly = (ImageButton) findViewById(R.id.B1);
        Setup = (ImageButton) findViewById(R.id.B2);
        Up_fly = (ImageButton) findViewById(R.id.Up_fly);
        Up_jump = (ImageButton) findViewById(R.id.Up_jump);
        Up_energy = (ImageButton) findViewById(R.id.Up_energy);
        Reward = (ImageButton) findViewById(R.id.Reward);
        Config = (ImageButton) findViewById(R.id.Config);
        Ask_yes = (ImageButton) findViewById(R.id.ask_select_b);
        Ask_no = (ImageButton) findViewById(R.id.ask_stop_b);
        Ask_image = findViewById(R.id.iv_ask);

        ask = (TextView) findViewById(R.id.ask_tv);
        ask_no = (TextView) findViewById(R.id.ask_stop_tv);
        ask_yes = (TextView) findViewById(R.id.ask_select_tv);

        ConstraintLayout.LayoutParams par = (ConstraintLayout.LayoutParams) Fly.getLayoutParams();
        par.width = dw / 4;
        par.height = dw / 4;
        par.rightMargin = dw / 100;
        par.bottomMargin = dw * 3 / 100 + dw / 4;
        Fly.setLayoutParams(par);

        par = (ConstraintLayout.LayoutParams) Setup.getLayoutParams();
        par.width = dw / 4;
        par.height = dw / 4;
        par.leftMargin = dw / 100;
        par.bottomMargin = dw / 100;
        Setup.setLayoutParams(par);

        par = (ConstraintLayout.LayoutParams) Reward.getLayoutParams();
        par.width = dw / 4;
        par.height = dw / 4;
        par.leftMargin = dw / 100;
        par.bottomMargin = dw * 3 / 100 + dw / 4;
        Reward.setLayoutParams(par);

        par = (ConstraintLayout.LayoutParams) Up_energy.getLayoutParams();
        par.width = dw / 4;
        par.height = dw / 4;
        par.rightMargin = dw / 16;
        par.topMargin = dw / 10;
        Up_energy.setLayoutParams(par);

        par = (ConstraintLayout.LayoutParams) Config.getLayoutParams();
        par.width = dw / 8;
        par.height = dw / 8;
        par.leftMargin = dw / 100;
        par.topMargin = dw / 100;
        Config.setLayoutParams(par);

        par = (ConstraintLayout.LayoutParams) Up_jump.getLayoutParams();
        par.width = dw / 4;
        par.height = dw / 4;
        par.leftMargin = dw / 16;
        par.topMargin = dw / 10;

        Up_jump.setLayoutParams(par);

        par = (ConstraintLayout.LayoutParams) Up_fly.getLayoutParams();
        par.width = dw / 4;
        par.height = dw / 4;
        par.topMargin = dw / 10;
        Up_fly.setLayoutParams(par);

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        MainActivity.quick_down = myPreferences.getBoolean("Config.quick_down", false);
        MainActivity.update = myPreferences.getInt("update", -1);
        MainActivity.ask_number = (byte) myPreferences.getInt("ask_number", 0);

        ask_status = new String[]{"RTF", "RTF", "RTF", "GTF", "FLU", "RCV", "RTF", "STF", "STF", "STF", "STF", "STF"};
        ask_savedstatus = new String[]{"NON", "NON", "NON", "NON", "NON", "NON", "NON", "RTF", "RTF", "RTF", "UPD", "UPD"};
        byte ask_stop = (byte) ask_status.length;
        gw = new GameView(this);

        ConstraintLayout gameLayout = (ConstraintLayout) findViewById(R.id.GL); // находим gameLayout
        ConstraintLayout.LayoutParams l = (ConstraintLayout.LayoutParams) gameLayout.getLayoutParams();
        l.height = dh;
        l.width = dw;
        gameLayout.setLayoutParams(l);

        gameLayout.addView(gw); // и добавляем в него gameView

        if (MainActivity.update != -1) {
            Fly.setImageResource(R.drawable.bust_training);
            Setup.setBackgroundResource(R.drawable.back_b);
            MainActivity.setup = true;
            //if(mRewardedAd == null)
            Reward.setVisibility(View.INVISIBLE);
            if (!energy_show) Up_energy.setVisibility(View.INVISIBLE);
            Config.setVisibility(View.INVISIBLE);
        } else {
            Up_fly.setVisibility(View.INVISIBLE);
            Up_jump.setVisibility(View.INVISIBLE);
            Up_energy.setVisibility(View.INVISIBLE);
            Reward.setVisibility(View.INVISIBLE);
        }
        Fly.setVisibility(View.VISIBLE);

        Fly.setOnTouchListener(this);
        Setup.setOnTouchListener(this);
        Up_fly.setOnTouchListener(this);
        Up_jump.setOnTouchListener(this);
        Up_energy.setOnTouchListener(this);
        Reward.setOnTouchListener(this);
        Config.setOnTouchListener(this);
        Ask_yes.setOnTouchListener(this);
        Ask_no.setOnTouchListener(this);

        Ask_l = findViewById(R.id.ask_layout);
        Ask_l.setVisibility(View.INVISIBLE);


//       MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//
//        adRequest = new AdRequest.Builder().build();


        Thread t = new Thread(() -> {
            while (gw.firstTime) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (true) {
                try {
                    Thread.sleep(100);
                    runOnUiThread(() -> {
                        if (gw.pen.bust > 0 && !MainActivity.energy_show)
                            MainActivity.energy_show = true;
                        if (gw.pen.bust == 0 && MainActivity.energy_show)
                            MainActivity.energy_show = false;

                        if (MainActivity.update == -1 && MainActivity.setup && Fly.getVisibility() == View.VISIBLE) {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Fly.setVisibility(View.INVISIBLE);
                            Reward.setVisibility(View.INVISIBLE);
                        }

                        if (MainActivity.setup && MainActivity.energy_show && Up_energy.getVisibility() == View.INVISIBLE) {
                            Up_energy.setVisibility(View.VISIBLE);
                        }

                        if (MainActivity.setup) {
                            if (gw.pen.savedstatus == "UPD") {
                                if (Fly.getVisibility() == View.INVISIBLE)
                                    Fly.setVisibility(View.VISIBLE);

//                                        if (MainActivity.mRewardedAd != null) {
//                                            if (MainActivity.Reward.getVisibility() == View.INVISIBLE) {
//                                                MainActivity.Reward.setVisibility(View.VISIBLE);
//                                            }
//                                        } else {
//                                            if (MainActivity.Reward.getVisibility() == View.VISIBLE) {
//                                                MainActivity.Reward.setVisibility(View.INVISIBLE);
//                                            }
//                                        }
                            } else {
                                if (Reward.getVisibility() == View.VISIBLE) {
                                    Reward.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                        if (!ask_on)
                            if (MainActivity.ask_number != -1 && MainActivity.ask_number != ask_stop) {
                                if (shure == -1)
                                    if (MainActivity.ask_status[MainActivity.ask_number] == gw.pen.status)
                                        if (MainActivity.ask_savedstatus[MainActivity.ask_number] == gw.pen.savedstatus)
                                            set_ask();
                            }

                    /*    if(setup&& update!=-1){
                            if(MainActivity.mRewardedAd != null && MainActivity.Reward.getVisibility()==View.INVISIBLE) MainActivity.Reward.setVisibility(View.VISIBLE);
                            if(MainActivity.mRewardedAd == null && MainActivity.Reward.getVisibility()==View.VISIBLE) MainActivity.Reward.setVisibility(View.INVISIBLE);
                        }*/

//                                if(need_rew){
//                                    need_rew=false;
//                                    if (rew_error<100){
//                                        reward_load();
//                                    }
//                                    //else {
//                                        //if (ads_timeout > 0) ads_timeout -= 1;
//                                        //else rew_error-=5;
//                                   // }
//
//                                }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public short ads_timeout = 0;
    public static boolean ask_on = false;

    private void set_ask() {
        ask_on = true;
        TableRow.LayoutParams l;
        switch (ask_number) {
            case 0:
                ask.setText(R.string.ask_hello);
                ask_no.setText(R.string.No);
                ask_yes.setText(R.string.Yes);
                Ask_image.setImageResource(0);
                l = (TableRow.LayoutParams) Ask_image.getLayoutParams();
                l.height = 0;
                l.width = 0;
                break;
            case 1:
                ask.setText(R.string.ask_0);
                ask_no.setText(R.string.ask_stop);
                ask_yes.setText(R.string.ask_next);
                Ask_image.setImageResource(0);
                l = (TableRow.LayoutParams) Ask_image.getLayoutParams();
                l.height = 0;
                l.width = 0;
                break;
            case 2:
                ask.setText(R.string.ask_1);
                ask_no.setText(R.string.ask_stop);
                ask_yes.setText(R.string.ask_next);

                Ask_image.setImageResource(R.drawable.b1);
                l = (TableRow.LayoutParams) Ask_image.getLayoutParams();
                l.height = dw / 4;
                l.width = dw / 4;
                Ask_image.setLayoutParams(l);
                break;
            case 3:
                ask.setText(R.string.ask_2);
                ask_no.setText(R.string.ask_stop);
                ask_yes.setText(R.string.ask_next);


                Ask_image.setImageResource(R.drawable.b1);
                l = (TableRow.LayoutParams) Ask_image.getLayoutParams();
                l.height = dw / 4;
                l.width = dw / 4;
                Ask_image.setLayoutParams(l);
                break;
            case 4:
                ask.setText(R.string.ask_3);
                ask_no.setText(R.string.ask_stop);
                ask_yes.setText(R.string.ask_next);

                Ask_image.setImageResource(0);
                l = (TableRow.LayoutParams) Ask_image.getLayoutParams();
                l.height = 0;
                l.width = 0;
                Ask_image.setLayoutParams(l);
                break;
            case 5:
                ask.setText(R.string.ask_4);
                ask_no.setText(R.string.ask_stop);
                ask_yes.setText(R.string.ask_next);
                Ask_image.setImageResource(0);
                l = (TableRow.LayoutParams) Ask_image.getLayoutParams();
                l.height = 0;
                l.width = 0;
                break;
            case 6:
                ask.setText(R.string.ask_5);
                ask_no.setText(R.string.ask_stop);
                ask_yes.setText(R.string.ask_next);

                Ask_image.setImageResource(R.drawable.b2);
                l = (TableRow.LayoutParams) Ask_image.getLayoutParams();
                l.height = dw / 4;
                l.width = dw / 4;
                Ask_image.setLayoutParams(l);
                break;
            case 7:
                ask.setText(R.string.ask_6);
                ask_no.setText(R.string.ask_stop);
                ask_yes.setText(R.string.ask_next);

                Ask_image.setImageResource(0);
                l = (TableRow.LayoutParams) Ask_image.getLayoutParams();
                l.height = 0;
                l.width = 0;
                Ask_image.setLayoutParams(l);
                break;
            case 8:
                ask.setText(R.string.ask_7);
                ask_no.setText(R.string.ask_stop);
                ask_yes.setText(R.string.ask_next);

                Ask_image.setImageResource(0);
                l = (TableRow.LayoutParams) Ask_image.getLayoutParams();
                l.height = 0;
                l.width = 0;
                Ask_image.setLayoutParams(l);
                break;
            case 9:
                ask.setText(R.string.ask_8);
                ask_no.setText(R.string.ask_stop);
                ask_yes.setText(R.string.ask_next);

                Ask_image.setImageResource(0);
                l = (TableRow.LayoutParams) Ask_image.getLayoutParams();
                l.height = 0;
                l.width = 0;
                Ask_image.setLayoutParams(l);
                break;
            case 10:
                ask.setText(R.string.ask_9);
                ask_no.setText(R.string.ask_stop);
                ask_yes.setText(R.string.ask_next);

                Ask_image.setImageResource(0);
                l = (TableRow.LayoutParams) Ask_image.getLayoutParams();
                l.height = 0;
                l.width = 0;
                Ask_image.setLayoutParams(l);
                break;
            case 11:
                ask.setText(R.string.ask_10);
                ask_no.setText(R.string.ask_stop);
                ask_yes.setText(R.string.ask_next);

                Ask_image.setImageResource(R.drawable.bust_training);
                l = (TableRow.LayoutParams) Ask_image.getLayoutParams();
                l.height = dw / 4;
                l.width = dw / 4;
                Ask_image.setLayoutParams(l);
                break;
        }
        Ask_l.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            gw.pen.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View button, MotionEvent motion) {
        switch (button.getId()) { // определяем какая кнопка
            case R.id.B1:
                switch (motion.getAction()) { // определяем нажата или отпущена
                    case MotionEvent.ACTION_DOWN:
                        flying = true;

                        break;
                    case MotionEvent.ACTION_UP:
                        flying = false;
                        break;
                }


                break;
            case R.id.B2:
                switch (motion.getAction()) { // определяем нажата или отпущена
                    case MotionEvent.ACTION_DOWN:

                        if (!setup) {
                            if (gw.pen.status == "RCV" || gw.pen.status == "RTF" || gw.pen.status == "UPD") {
                                if (gw.pen.status != "STF") {
                                    if (gw.pen.status == "RCV")
                                        gw.pen.savedstatus = "RCV";
                                    else if (gw.pen.status == "RTF")
                                        gw.pen.savedstatus = "RTF";
                                    else if (gw.pen.status == "UPD")
                                        gw.pen.savedstatus = "UPD";
                                    gw.pen.status = "STF";
                                }
                                Fly.setImageResource(R.drawable.bust_training);
                                //Fly.setImageResource(R.drawable.imb);
                                //Setup.setImageResource();
                                //Setup.setBackground(R.drawable.back_b);
                                Setup.setBackgroundResource(R.drawable.back_b);
                                setup = true;

                                if (gw.pen.savedstatus != "UPD")
                                    Fly.setVisibility(View.INVISIBLE);
//                                else if (MainActivity.mRewardedAd != null)
//                                    Reward.setVisibility(View.VISIBLE);
                                Config.setVisibility(View.INVISIBLE);
                                Up_fly.setVisibility(View.VISIBLE);
                                Up_jump.setVisibility(View.VISIBLE);
                                if (energy_show) Up_energy.setVisibility(View.VISIBLE);
                                else {
                                    if (gw.pen.next_bust > 0) {
                                        MainActivity.energy_show = true;
                                        Up_energy.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        } else /*if (update == -1)*/ {
                            Fly.setImageResource(R.drawable.b1);
                            //Setup.setImageResource(R.drawable.b2);
                            //Setup.setBackgroundResource(R.drawable.upgrade_menu);
                            Setup.setBackgroundResource(R.drawable.b2);
                            setup = false;
                            Config.setVisibility(View.VISIBLE);
                            Up_fly.setVisibility(View.INVISIBLE);
                            Up_jump.setVisibility(View.INVISIBLE);
                            Up_energy.setVisibility(View.INVISIBLE);
                            //Reward.setVisibility(View.INVISIBLE);
                            Fly.setVisibility(View.VISIBLE);
                            Reward.setVisibility(View.INVISIBLE);
                            //Fly.setVisibility(View.VISIBLE);
                            if (gw.pen.status == "STF") {
                                if (gw.pen.savedstatus == "RCV")
                                    gw.pen.status = "RCV";
                                else if (gw.pen.savedstatus == "UPD")
                                    gw.pen.status = "UPD";
                                else if (gw.pen.savedstatus == "RTF")
                                    gw.pen.status = "RTF";
                                gw.pen.savedstatus = "NON";
                            }
                        }
                        break;
                }
                break;
            case R.id.Up_fly:
                switch (motion.getAction()) { // определяем нажата или отпущена
                    case MotionEvent.ACTION_DOWN:
                        if (update == -1) {
                            update = 1;

                        }
                        break;
                }
                break;
            case R.id.Up_jump:
                switch (motion.getAction()) { // определяем нажата или отпущена
                    case MotionEvent.ACTION_DOWN:
                        if (update == -1) {
                            update = 0;

                        }
                        break;
                }
                break;
            case R.id.Up_energy:
                switch (motion.getAction()) { // определяем нажата или отпущена
                    case MotionEvent.ACTION_DOWN:
                        if (update == -1) {
                            update = 2;
                        }
                        break;
                }
                break;

            case R.id.Config:
                switch (motion.getAction()) { // определяем нажата или отпущена
                    case MotionEvent.ACTION_DOWN:
                        Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
                        startActivity(intent);
                        break;
                }
                break;

            case R.id.Reward:
                switch (motion.getAction()) { // определяем нажата или отпущена
                    case MotionEvent.ACTION_DOWN:
                        try {
//                            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                                @Override
//                                public void onAdShowedFullScreenContent() {
//                                    // Called when ad is shown.
//                                    Log.d(TAG, "Ad was shown.");
//                                }
//
//                                @Override
//                                public void onAdFailedToShowFullScreenContent(AdError adError) {
//                                    // Called when ad fails to show.
//                                    Log.d(TAG, "Ad failed to show.");
//                                }
//
//                                @Override
//                                public void onAdDismissedFullScreenContent() {
//                                    // Called when ad is dismissed.
//                                    // Set the ad reference to null so you don't show the ad a second time.
//                                    Log.d(TAG, "Ad was dismissed.");
//                                    mRewardedAd = null;
//                                }
//                            });
//                            if (mRewardedAd != null) {
//                                Activity activityContext = MainActivity.this;
//                                mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
//                                    @Override
//                                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
//                                        // Handle the reward.
//                                        Log.d(TAG, "The user earned the reward.");
//                                        int rewardAmount = rewardItem.getAmount();
//                                        String rewardType = rewardItem.getType();
//
//                                        gw.pen.short_update+=60;
//                                        gw.pen.shorting=new Date();
//
//                                        reward_load();
//
//                                    }
//                                });
//                            } else {
//                                Log.d(TAG, "The rewarded ad wasn't ready yet.");
//                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        need_rew = true;
                        break;
                }
                break;
            case R.id.ask_stop_b:
                switch (motion.getAction()) { // определяем нажата или отпущена
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        if (shure == -1) {
                            shure = 0;
                            ask.setText(R.string.ask_exit);
                            ask_no.setText(R.string.ret);
                            ask_yes.setText(R.string.okay);
                            Ask_image.setImageResource(R.drawable.config_btn);
                            TableRow.LayoutParams l;
                            l = (TableRow.LayoutParams) Ask_image.getLayoutParams();
                            l.height = dw / 4;
                            l.width = dw / 4;
                            Ask_image.setLayoutParams(l);
                        } else if (shure == 0) {
                            shure = -1;
                            ask_on = false;
                        }
                        break;
                }

                break;
            case R.id.ask_select_b:
                switch (motion.getAction()) { // определяем нажата или отпущена
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_UP:
                        if (shure == -1) {
                            ask_number += 1;
                            Ask_l.setVisibility(View.INVISIBLE);
                        } else if (shure == 0) {
                            shure = -1;
                            ask_number = -1;
                            Ask_l.setVisibility(View.INVISIBLE);
                        }
                        gw.pen.save_ask();
                        ask_on = false;
                        break;
                }
                break;
        }
        return true;
    }

//    private void reward_load(){
//    // в манифест оригинал "ca-app-pub-8592750491177297~1345545766"
//        //в объявление оригинал "ca-app-pub-8592750491177297/4490583854"
//        RewardedAd.load(cont, "ca-app-pub-8592750491177297/4490583854"
//                /*в объявление тестовый "ca-app-pub-3940256099942544/5224354917"*/,
//                adRequest, new RewardedAdLoadCallback() {
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        // Handle the error.
//                        Log.d(TAG, loadAdError.getMessage());
//                        mRewardedAd = null;
//                        need_rew=true;
//                        rew_error+=1;
//                        if (rew_error==100) {
//
//                            MobileAds.initialize(cont, new OnInitializationCompleteListener() {
//                                @Override
//                                public void onInitializationComplete(InitializationStatus initializationStatus) {
//                                }
//                            });
//
//                        adRequest = new AdRequest.Builder().build();
//                            rew_error=90;
//                            //ads_timeout=100;
//                         }
//                    }
//
//                    @Override
//                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
//                        mRewardedAd = rewardedAd;
//                        Log.d(TAG, "Ad was loaded.");
//                        rew_error=0;
//                    }
//                });
//    }


}