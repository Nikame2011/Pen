package com.pf.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
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

на будущее:
+ улучшения ускоряются кликаньем
+ прыжки усиливаются кликаньем
- с изменением высоты картинки заменяются на другие подгружаясь из памяти, ненужные картинки не хранятся
+ анимация и движение синхронизированы(взмах и ускорение в один момент)
- анимация на разные виды деятельности
- подсказки в начале игры
- новый вид меню: кнопка прыжка кругом без надписей, улучшения стрелкой влево с анимацией +++ когда доступно
улучшение,над ним кнопка настроек, концентрация и энергия правее чем сейчас, слева  рекорд и количество монет



 */

/*
 коды версий
 0.0.0.0
 Х.0.0.0 максимально глобальные изменения, вносимые в код, хз какие пока, возможно, добавление новых режимов
 0.Х.0.0 добавление функционала или глобальное улучшение имеющегося - увеличение высоты, добавление магазина
 0.0.Х.0 незначительные улучшения текущего функционала например, добавленте нескольких предметов в магазин
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
    public static int dw,dh;
    public static boolean testing=false;//true;
    public GameView gw;
    public static Date first_date;
    public static boolean energy_show=false;
    public static float end=0;
    public AdRequest adRequest;
    public Context cont;

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

        Fly = (ImageButton ) findViewById(R.id.B1);
        Setup = (ImageButton ) findViewById(R.id.B2);
        Up_fly = (ImageButton) findViewById(R.id.Up_fly);
        Up_jump = (ImageButton) findViewById(R.id.Up_jump);
        Up_energy = (ImageButton) findViewById(R.id.Up_energy);
        Reward = (ImageButton) findViewById(R.id.Reward);

        ConstraintLayout.LayoutParams par= (ConstraintLayout.LayoutParams)Fly.getLayoutParams();
        par.width=dw/4;
        par.height=dw/4;
        par.rightMargin =dw/100;
        par.bottomMargin=dw*3/100+dw/4;
        Fly.setLayoutParams (par);

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

        par= (ConstraintLayout.LayoutParams)Up_jump.getLayoutParams();
        par.width=dw/4;
        par.height=dw/4;
        par.leftMargin=dw/16;
        par.topMargin=dw/10;

        Up_jump.setLayoutParams (par);

        par= (ConstraintLayout.LayoutParams)Up_fly.getLayoutParams();
        par.width=dw/4;
        par.height=dw/4;
        //par.leftMargin=dw/16;
        par.topMargin=dw/10;
        Up_fly.setLayoutParams (par);

        par= (ConstraintLayout.LayoutParams)Setup.getLayoutParams();
        par.width=dw/4;
        par.height=dw/4;
        par.leftMargin=dw/100;
        par.bottomMargin=dw/100;
        Setup.setLayoutParams (par);

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        MainActivity.update=myPreferences.getInt("update", -1);
        if(MainActivity.update!=-1){
            Fly.setImageResource(R.drawable.imb);
            Setup.setImageResource(R.drawable.back_b);
            MainActivity.setup=true;
            if(mRewardedAd == null) Reward.setVisibility(View.INVISIBLE);
            if(!energy_show)Up_energy.setVisibility(View.INVISIBLE);
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

        gw= new GameView(this);



        LinearLayout gameLayout = (LinearLayout) findViewById(R.id.GL); // находим gameLayout
        ConstraintLayout.LayoutParams l= (ConstraintLayout.LayoutParams) gameLayout.getLayoutParams();
        l.height=dh;
        l.width=dw;
        gameLayout.setLayoutParams(l);


        gameLayout.addView(gw); // и добавляем в него gameView

    }

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

                        if (gw.pen.pen_coord != 0 && gw.pen.start_date != first_date) {

                        } else {
                            if (!setup) {
                                Fly.setImageResource(R.drawable.imb);
                                Setup.setImageResource(R.drawable.back_b);
                                setup = true;
                                Fly.setVisibility(View.INVISIBLE);
                                Up_fly.setVisibility(View.VISIBLE);
                                Up_jump.setVisibility(View.VISIBLE);
                                if (energy_show) Up_energy.setVisibility(View.VISIBLE);
                                else {
                                    if (gw.pen.next_bust > 0) {
                                        MainActivity.energy_show = true;
                                        Up_energy.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else if (update == -1) {
                                Fly.setImageResource(R.drawable.b1);
                                Setup.setImageResource(R.drawable.b2);
                                setup = false;

                                Up_fly.setVisibility(View.INVISIBLE);
                                Up_jump.setVisibility(View.INVISIBLE);
                                Up_energy.setVisibility(View.INVISIBLE);
                                //Reward.setVisibility(View.INVISIBLE);
                                Fly.setVisibility(View.VISIBLE);

                                //Fly.setVisibility(View.VISIBLE);
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
                            MainActivity.Fly.setVisibility(View.VISIBLE);
                            if (MainActivity.mRewardedAd != null)
                                MainActivity.Reward.setVisibility(View.VISIBLE);
                            Setup.setImageResource(R.drawable.back_g);
                        }
                        break;
                }
                break;
            case R.id.Up_jump:
                switch (motion.getAction()) { // определяем нажата или отпущена
                    case MotionEvent.ACTION_DOWN:
                        if (update == -1) {
                            update = 0;
                            MainActivity.Fly.setVisibility(View.VISIBLE);
                            if (MainActivity.mRewardedAd != null)
                                MainActivity.Reward.setVisibility(View.VISIBLE);
                            Setup.setImageResource(R.drawable.back_g);
                        }
                        break;
                }
                break;
            case R.id.Up_energy:
                switch (motion.getAction()) { // определяем нажата или отпущена
                    case MotionEvent.ACTION_DOWN:
                        if (update == -1) {
                            update = 2;
                            MainActivity.Fly.setVisibility(View.VISIBLE);
                            if (MainActivity.mRewardedAd != null)
                                MainActivity.Reward.setVisibility(View.VISIBLE);
                            Setup.setImageResource(R.drawable.back_g);
                        }
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

                                gw.pen.to_update-=60;

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


/*
чтобы лететь, быстро нажимай сюда.
чем больше концентрация, тем выше ты взлетишь.
нажми сюда чтобы открыть/закрыть меню улучшений.
ты можешь ускорить улучшение с помощью кнопки полёта.

*/






}