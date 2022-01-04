package com.pf.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

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

/*
 коды версий
 0.0.0.0
 Х.0.0.0 максимально глобальные изменения, вносимые в код, хз какие пока, возможно, добавление новых режимов
 0.Х.0.0 добавление функционала или глобальное улучшение имеющегося - увеличение высоты, добавление магазина
 0.0.Х.0 незначительные улучшения текущего функционала например, добавленbе нескольких предметов в магазин
 0.0.0.Х исправление багов, в том числе исправление коэффициентов, мешающих прохождению, технические улучшения, не добавляющие функционал(оптимизация кода)
 */

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    public static boolean flying=false;
    public static boolean setup=false;
    public static int update=-1;
    public static ImageButton Fly;
    public static ImageButton Setup;
    public static ImageButton Up_fly;
    public static ImageButton Up_jump;
    public static ImageButton Up_energy;
    public static ImageButton Reward;
    public static ImageButton Config;

    public static int dw,dh;
    public static boolean testing=false;//true;
    public static GameView gw;
    public static Date first_date;
    public static boolean quick_down=false;
    public static boolean new_game=false;

    public static boolean energy_show=false;
    public static float end=0;
    public AdRequest adRequest;
    public static Context cont;
    public static String version="0.1.1.0";

    public static RewardedAd mRewardedAd;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dw= getResources().getDisplayMetrics().widthPixels;//получаем ширину экрана
        dh= getResources().getDisplayMetrics().heightPixels;//получаем ширину экрана
        first_date=new Date();

        setContentView(R.layout.activity_main);
        cont=this;


        Fly = (ImageButton ) findViewById(R.id.B1);
        Setup = (ImageButton ) findViewById(R.id.B2);
        Up_fly = (ImageButton) findViewById(R.id.Up_fly);
        Up_jump = (ImageButton) findViewById(R.id.Up_jump);
        Up_energy = (ImageButton) findViewById(R.id.Up_energy);
        Reward = (ImageButton) findViewById(R.id.Reward);
        Config = (ImageButton) findViewById(R.id.Back);

        ConstraintLayout.LayoutParams par= (ConstraintLayout.LayoutParams)Fly.getLayoutParams();
        par.width=dw/4;
        par.height=dw/4;
        par.rightMargin =dw/100;
        par.bottomMargin=dw*3/100+dw/4;
        Fly.setLayoutParams (par);

        par= (ConstraintLayout.LayoutParams)Setup.getLayoutParams();
        par.width=dw/4;
        par.height=dw/4;
        par.leftMargin=dw/100;
        par.bottomMargin=dw/100;
        Setup.setLayoutParams (par);

        par= (ConstraintLayout.LayoutParams)Reward.getLayoutParams();
        par.width=dw/4;
        par.height=dw/4;
        par.leftMargin=dw/100;
        par.bottomMargin=dw*3/100+dw/4;
        Reward.setLayoutParams (par);

        par= (ConstraintLayout.LayoutParams)Up_energy.getLayoutParams();
        par.width=dw/4;
        par.height=dw/4;
        par.rightMargin=dw/16;
        par.topMargin=dw/10;
        Up_energy.setLayoutParams (par);

        par= (ConstraintLayout.LayoutParams)Config.getLayoutParams();
        par.width=dw/8;
        par.height=dw/8;
        par.leftMargin=dw/100;
        par.topMargin=dw/100;
        Config.setLayoutParams (par);

        par= (ConstraintLayout.LayoutParams)Up_jump.getLayoutParams();
        par.width=dw/4;
        par.height=dw/4;
        par.leftMargin=dw/16;
        par.topMargin=dw/10;

        Up_jump.setLayoutParams (par);

        par= (ConstraintLayout.LayoutParams)Up_fly.getLayoutParams();
        par.width=dw/4;
        par.height=dw/4;
        par.topMargin=dw/10;
        Up_fly.setLayoutParams (par);



        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        MainActivity.quick_down=myPreferences.getBoolean("Config.quick_down", false);
        MainActivity.update=myPreferences.getInt("update", -1);

        gw= new GameView(this);

        LinearLayout gameLayout = (LinearLayout) findViewById(R.id.GL); // находим gameLayout
        ConstraintLayout.LayoutParams l= (ConstraintLayout.LayoutParams) gameLayout.getLayoutParams();
        l.height=dh;
        l.width=dw;
        gameLayout.setLayoutParams(l);


        gameLayout.addView(gw); // и добавляем в него gameView



        if(MainActivity.update!=-1){
            Fly.setImageResource(R.drawable.imb);
            Setup.setImageResource(R.drawable.back_b);
            MainActivity.setup=true;
            if(mRewardedAd == null) Reward.setVisibility(View.INVISIBLE);
            if(!energy_show)Up_energy.setVisibility(View.INVISIBLE);
            Config.setVisibility(View.INVISIBLE);
        }
        else {
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

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-8592750491177297/4490583854",  //ca-app-pub-3940256099942544/5224354917
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "Ad was loaded.");
                    }
                });


        Thread t = new Thread(new Runnable() {
            public void run() {
                while(gw.firstTime){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                while(true){
                    try {


                        Thread.sleep(100);
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if(gw.pen.bust>0 && !MainActivity.energy_show)MainActivity.energy_show=true;
                                if(gw.pen.bust==0 && MainActivity.energy_show)MainActivity.energy_show=false;

                                if (MainActivity.update==-1 && MainActivity.setup && MainActivity.Fly.getVisibility()==View.VISIBLE) {
                                    try {
                                        Thread.sleep(50);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    MainActivity.Fly.setVisibility(View.INVISIBLE);
                                    MainActivity.Reward.setVisibility(View.INVISIBLE);
                                }

                                if ( MainActivity.setup && MainActivity.energy_show&& MainActivity.Up_energy.getVisibility()==View.INVISIBLE) {
                                    MainActivity.Up_energy.setVisibility(View.VISIBLE);
                                }

                                if (MainActivity.update!=-1 && gw.pen.to_update!=-1.0 && MainActivity.Fly.getVisibility()==View.INVISIBLE) {
                                    MainActivity.Fly.setVisibility(View.VISIBLE);
                                    //MainActivity.Reward.setVisibility(View.INVISIBLE);
                                    if (MainActivity.mRewardedAd != null)
                                        MainActivity.Reward.setVisibility(View.VISIBLE);
                                }



                            }
                        });

                        /*if (new_game){
                            //gw.close();
                            //gw= new GameView(cont);
                            gw.new_game();
                        }*/
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();

       /* Tim = new Timer();
        TT = new TimeTask();


            Tim.schedule(TT, 1000, 100);*/

    }

   /* class TimeTask extends TimerTask {

        @Override
        public void run() {
            if (MainActivity.update==-1) {
                MainActivity.Fly.setVisibility(View.INVISIBLE);
                MainActivity.Reward.setVisibility(View.INVISIBLE);
            }
        }
    }*/


    @Override
    protected void onPause() {
        super.onPause();
        try{
            gw.pen.save();
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onTouch(View button, MotionEvent motion) {
        switch(button.getId()) { // определяем какая кнопка
            case R.id.B1:
                switch (motion.getAction()) { // определяем нажата или отпущена
                    case MotionEvent.ACTION_DOWN:
                        flying = true;
                        if(setup){
                            if(MainActivity.mRewardedAd != null && MainActivity.Reward.getVisibility()==View.INVISIBLE) MainActivity.Reward.setVisibility(View.VISIBLE);
                            if(MainActivity.mRewardedAd == null && MainActivity.Reward.getVisibility()==View.VISIBLE) MainActivity.Reward.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        flying = false;
                        break;
                }


                break;
            case R.id.B2:
                switch (motion.getAction()) { // определяем нажата или отпущена
                    case MotionEvent.ACTION_DOWN:

                        if (!setup){
                            if (gw.pen.status=="RCV"||gw.pen.status=="RTF"||gw.pen.status=="UPD") {
                                if (gw.pen.status != "STF"){
                                    if (gw.pen.status == "RCV")
                                        gw.pen.savedstatus = "RCV";
                                    else if (gw.pen.status == "RTF")
                                        gw.pen.savedstatus = "RTF";
                                    else if (gw.pen.status == "UPD")
                                        gw.pen.savedstatus = "UPD";
                                    gw.pen.status = "STF";
                                }

                                Fly.setImageResource(R.drawable.imb);
                                Setup.setImageResource(R.drawable.back_b);
                                setup = true;

                                if(gw.pen.savedstatus != "UPD")
                                    Fly.setVisibility(View.INVISIBLE);
                                else if (MainActivity.mRewardedAd != null)
                                    Reward.setVisibility(View.VISIBLE);
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
                        }
                        else /*if (update == -1)*/{
                            Fly.setImageResource(R.drawable.b1);
                            Setup.setImageResource(R.drawable.b2);
                            setup = false;
                            Config.setVisibility(View.VISIBLE);
                            Up_fly.setVisibility(View.INVISIBLE);
                            Up_jump.setVisibility(View.INVISIBLE);
                            Up_energy.setVisibility(View.INVISIBLE);
                            //Reward.setVisibility(View.INVISIBLE);
                            Fly.setVisibility(View.VISIBLE);
                            Reward.setVisibility(View.INVISIBLE);
                            //Fly.setVisibility(View.VISIBLE);
                            if (gw.pen.status == "STF"){
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
                            //MainActivity.Fly.setVisibility(View.VISIBLE);
                            //if (MainActivity.mRewardedAd != null)
                             //   MainActivity.Reward.setVisibility(View.VISIBLE);
                            //Setup.setImageResource(R.drawable.back_g);
                        }
                        break;
                }
                break;
            case R.id.Up_jump:
                switch (motion.getAction()) { // определяем нажата или отпущена
                    case MotionEvent.ACTION_DOWN:
                        if (update == -1) {
                            update = 0;
                           // MainActivity.Fly.setVisibility(View.VISIBLE);
                            //if (MainActivity.mRewardedAd != null)
                               // MainActivity.Reward.setVisibility(View.VISIBLE);
                            //Setup.setImageResource(R.drawable.back_g);
                        }
                        break;
                }
                break;
            case R.id.Up_energy:
                switch (motion.getAction()) { // определяем нажата или отпущена
                    case MotionEvent.ACTION_DOWN:
                        if (update == -1) {
                            update = 2;
                            //MainActivity.Fly.setVisibility(View.VISIBLE);
                            //if (MainActivity.mRewardedAd != null)
                               // MainActivity.Reward.setVisibility(View.VISIBLE);
                            //Setup.setImageResource(R.drawable.back_g);
                        }
                        break;
                }
                break;

            case R.id.Back:
                switch (motion.getAction()) { // определяем нажата или отпущена
                    case MotionEvent.ACTION_DOWN:
                        Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
                        startActivity(intent);
                        break;
                }
                break;

            case R.id.Reward:
                try{
                    mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when ad is shown.
                            Log.d(TAG, "Ad was shown.");
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when ad fails to show.
                            Log.d(TAG, "Ad failed to show.");
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when ad is dismissed.
                            // Set the ad reference to null so you don't show the ad a second time.
                            Log.d(TAG, "Ad was dismissed.");
                            mRewardedAd = null;
                        }
                    });
                    if (mRewardedAd != null) {
                        Activity activityContext = MainActivity.this;
                        mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                // Handle the reward.
                                Log.d(TAG, "The user earned the reward.");
                                int rewardAmount = rewardItem.getAmount();
                                String rewardType = rewardItem.getType();

                                gw.pen.short_update+=60;
                                gw.pen.shorting=new Date();
                                RewardedAd.load(cont, "ca-app-pub-8592750491177297/4490583854",
                                        adRequest, new RewardedAdLoadCallback() {
                                            @Override
                                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                                // Handle the error.
                                                Log.d(TAG, loadAdError.getMessage());
                                                mRewardedAd = null;
                                            }

                                            @Override
                                            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                                                mRewardedAd = rewardedAd;
                                                Log.d(TAG, "Ad was loaded.");
                                            }
                                        });

                            }
                        });
                    } else {
                        Log.d(TAG, "The rewarded ad wasn't ready yet.");
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return true;
    }

 /*   public void tr(){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                MainActivity.Fly.setVisibility(View.INVISIBLE);
                MainActivity.Reward.setVisibility(View.INVISIBLE);

            }
        });
    }*/


/*
чтобы лететь, быстро нажимай сюда.
чем больше концентрация, тем выше ты взлетишь.
нажми сюда чтобы открыть/закрыть меню улучшений.
ты можешь ускорить улучшение с помощью кнопки полёта.

*/






}