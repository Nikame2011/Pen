package com.pf.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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


public class MainActivity extends AppCompatActivity implements View.OnClickListener, Penguin.MainListener {
    public static boolean flying = false;
    public static boolean setup = false;
    public static int update = -1;
    public static ImageButton Fly;
    public static ImageButton Setup;
    public static ImageButton Up_fly;
    public static ImageButton Up_jump;
    public static ImageButton Up_energy;
    private ProgressBar concentration, pbEnergy;
    //public static ImageButton Reward;
    public ImageButton Config;
    public ImageButton Ask_yes;
    public ImageButton Ask_no;
    ConstraintLayout Ask_l;
    ImageView Ask_image;
    TextView tv;

    public static int dw, dh;
    //public static boolean testing = false;//true;
    public static GameView gw;
    public static Date first_date;
    public static boolean quick_down = false;
    public static boolean new_game = false;

    public static boolean energy_show = false;
    public static float end = 0;
    //public AdRequest adRequest;
    public Context cont;
    public static String version = "0.1.2.0";

    //public static RewardedAd mRewardedAd;
//    private final String TAG = "MainActivity";

    public static byte ask_number;
//    public static String[] ask_status;
//    public static String[] ask_savedstatus;


    private static Teaching[] teachings;
    private byte shure = -1;
    TextView ask;
    TextView ask_no;
    TextView ask_yes;
    private boolean need_rew = true;
    private int rew_error = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        dw = getResources().getDisplayMetrics().widthPixels;//получаем ширину экрана
        dh = getResources().getDisplayMetrics().heightPixels;//получаем ширину экрана
        end = dh;
        first_date = new Date();

        cont = this;

        Fly = findViewById(R.id.btnFlight);
        Setup = findViewById(R.id.B2);
        Up_fly = findViewById(R.id.Up_fly);
        Up_jump = findViewById(R.id.Up_jump);
        Up_energy = findViewById(R.id.Up_energy);
        //Reward = findViewById(R.id.Reward);
        Config = findViewById(R.id.Config);
        Ask_yes = findViewById(R.id.ask_select_b);
        Ask_no = findViewById(R.id.ask_stop_b);
        Ask_image = findViewById(R.id.iv_ask);
        concentration = findViewById(R.id.progressBar);
        pbEnergy = findViewById(R.id.progressBar2);
        tv = findViewById(R.id.textView);

        ask = findViewById(R.id.ask_tv);
        ask_no = findViewById(R.id.ask_stop_tv);
        ask_yes = findViewById(R.id.ask_select_tv);

//        ConstraintLayout.LayoutParams par = (ConstraintLayout.LayoutParams) Fly.getLayoutParams();
//        par.width = dw / 4;
//        par.height = dw / 4;
//        par.rightMargin = dw / 100;
//        par.bottomMargin = dw * 3 / 100 + dw / 4;
//        Fly.setLayoutParams(par);

        ConstraintLayout.LayoutParams par = (ConstraintLayout.LayoutParams) Setup.getLayoutParams();
        par.width = dw / 4;
        par.height = dw / 4;
        par.leftMargin = dw / 100;
        par.bottomMargin = dw / 100;
        Setup.setLayoutParams(par);

//        par = (ConstraintLayout.LayoutParams) Reward.getLayoutParams();
//        par.width = dw / 4;
//        par.height = dw / 4;
//        par.leftMargin = dw / 100;
//        par.bottomMargin = dw * 3 / 100 + dw / 4;
//        Reward.setLayoutParams(par);

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

//        ask_status = new String[]{"RTF", "RTF", "RTF", "GTF", "FLU", "RCV", "RTF", "STF", "STF", "STF", "STF", "STF"};
//        ask_savedstatus = new String[]{"NON", "NON", "NON", "NON", "NON", "NON", "NON", "RTF", "RTF", "RTF", "UPD", "UPD"};
        teachings = new Teaching[]{
                new Teaching(R.string.ask_hello, null, "RTF", "NON"),
                new Teaching(R.string.ask_0, null, "RTF", "NON"),
                new Teaching(R.string.ask_1, R.drawable.b1, "RTF", "NON"),
                new Teaching(R.string.ask_2, R.drawable.b1, "GTF", "NON"),
                new Teaching(R.string.ask_3, null, "FLU", "NON"),
                new Teaching(R.string.ask_4, null, "RCV", "NON"),
                new Teaching(R.string.ask_5, R.drawable.b2, "RTF", "NON"),
                new Teaching(R.string.ask_6, null, "STF", "RTF"),
                new Teaching(R.string.ask_7, null, "STF", "RTF"),
                new Teaching(R.string.ask_8, null, "STF", "RTF"),
                new Teaching(R.string.ask_9, null, "STF", "UPD"),
                new Teaching(R.string.ask_10, R.drawable.bust_training, "STF", "UPD"),
                new Teaching(R.string.ask_11, R.drawable.back_b, "STF", "RCV"),
                new Teaching(R.string.ask_12, null, "RCV", "NON")
        };


        if (MainActivity.update != -1) {
            //Fly.setImageResource(R.drawable.bust_training);
            //Setup.setBackgroundResource(R.drawable.back_b);
            MainActivity.setup = true;
            //if(mRewardedAd == null)
//            Reward.setVisibility(View.INVISIBLE);
            if (!energy_show) Up_energy.setVisibility(View.INVISIBLE);
            Config.setVisibility(View.INVISIBLE);
        } else {
            Up_fly.setVisibility(View.INVISIBLE);
            Up_jump.setVisibility(View.INVISIBLE);
            Up_energy.setVisibility(View.INVISIBLE);
//            Reward.setVisibility(View.INVISIBLE);
        }
        Fly.setVisibility(View.VISIBLE);

        Fly.setOnClickListener(this);

        Setup.setOnClickListener(this);
        Up_fly.setOnClickListener(this);
        Up_jump.setOnClickListener(this);
        Up_energy.setOnClickListener(this);
        Ask_yes.setOnClickListener(this);
        Ask_no.setOnClickListener(this);

//        Reward.setOnTouchListener(this);
        Config.setOnClickListener(this);

        Ask_l = findViewById(R.id.ask_layout);
        Ask_l.setVisibility(View.INVISIBLE);


//       MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//
//        adRequest = new AdRequest.Builder().build();
    }


    @Override
    public void onConcentrationChanged(float bust) {
        runOnUiThread(() -> {
            concentration.setProgress((int) bust);
            tv.setText(String.valueOf(bust));
        });
    }

    @Override
    public void onBustChanged(double bust) {
        runOnUiThread(() -> {
            //когда пингвин может летать нужно показывать энергию
            if (bust > 0) {
                energy_show = true;
                pbEnergy.setVisibility(View.VISIBLE);
                if (setup) {
                    Up_energy.setVisibility(View.VISIBLE);
                }
            } else {
                energy_show = false;
                pbEnergy.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onEnergyChanged(double energy, double maxEnergy) {
        runOnUiThread(() -> {
            pbEnergy.setProgress((int) (energy * 100 / maxEnergy));
        });
    }

    @Override
    public void onStatusChanged(String status, String savedStatus) {
        runOnUiThread(() -> {
            MainActivity.this.status = status;
            MainActivity.this.savedStatus = savedStatus;
            if (setup) {
                if (savedStatus.equals("UPD")) {
                    Setup.setVisibility(View.INVISIBLE);
                    Fly.setVisibility(View.VISIBLE);
                } else {
                    Fly.setVisibility(View.INVISIBLE);
                    Setup.setVisibility(View.VISIBLE);
                }
            } else {
                if (status.equals("RTF")) {
                    Setup.setVisibility(View.VISIBLE);
                } else {
                    Setup.setVisibility(View.INVISIBLE);
                }
            }
            checkTeaching();
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        if (gw == null) {
            gw = new GameView(this, this);

            ConstraintLayout gameLayout = findViewById(R.id.GL); // находим gameLayout
            ConstraintLayout.LayoutParams l = (ConstraintLayout.LayoutParams) gameLayout.getLayoutParams();
            l.height = dh;
            l.width = dw;
            gameLayout.setLayoutParams(l);

            gameLayout.addView(gw); // и добавляем в него gameView
        }
    }

    String status;
    String savedStatus;

    private void checkTeaching() {

        if (!ask_on && shure == -1)
            if (MainActivity.ask_number != -1 && MainActivity.ask_number < (teachings.length)) {
                if (teachings[ask_number].askStatus.equals(status) && teachings[ask_number].askSavedStatus.equals(savedStatus))
                    set_ask();
            }

    }

    public short ads_timeout = 0;
    public static boolean ask_on = false;

    private void set_ask() {
        ask_on = true;
        ask.setText(teachings[ask_number].text);
        if (ask_number == 0) {
            ask_no.setText(R.string.No);
            ask_yes.setText(R.string.Yes);
        } else {
            ask_no.setText(R.string.ask_stop);
            ask_yes.setText(R.string.ask_next);
        }

        if (teachings[ask_number].res != null) {
            Ask_image.setVisibility(View.VISIBLE);
            Ask_image.setImageResource(teachings[ask_number].res);
            TableRow.LayoutParams l;
            l = (TableRow.LayoutParams) Ask_image.getLayoutParams();
            l.height = dw / 4;
            l.width = dw / 4;
            Ask_image.setLayoutParams(l);
        } else {
            Ask_image.setVisibility(View.GONE);
        }

        Ask_l.setVisibility(View.VISIBLE);
    }

    private class Teaching {
        Integer text;
        Integer res;
        String askStatus;
        String askSavedStatus;

        public Teaching(Integer text, Integer res, String askStatus, String askSavedStatus) {
            this.text = text;
            this.res = res;
            this.askStatus = askStatus;
            this.askSavedStatus = askSavedStatus;
        }
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
    public void onClick(View button) {
        int id = button.getId();// определяем какая кнопка
        if (id == R.id.btnFlight) {
            flying = true;
        } else if (id == R.id.B2) {

            if (!setup) {
                if (gw.pen.status.equals("RCV") || gw.pen.status.equals("RTF") || gw.pen.status.equals("UPD")) {
                    gw.pen.savedstatus = gw.pen.status;
                    gw.pen.status = "STF";
                    this.onStatusChanged(gw.pen.status, gw.pen.savedstatus);
                    //Fly.setImageResource(R.drawable.bust_training);
                    //Fly.setImageResource(R.drawable.imb);
                    //Setup.setImageResource();
                    //Setup.setBackground(R.drawable.back_b);
                    //Setup.setBackgroundResource(R.drawable.back_b);
                    setup = true;

                    if (!gw.pen.savedstatus.equals("UPD")) {
                        Setup.setVisibility(View.VISIBLE);
                        Fly.setVisibility(View.INVISIBLE);
                    } else {
                        Setup.setVisibility(View.INVISIBLE);
                        Fly.setVisibility(View.VISIBLE);
                    }
//                                else if (MainActivity.mRewardedAd != null)
//                                    Reward.setVisibility(View.VISIBLE);
                    Config.setVisibility(View.INVISIBLE);
                    Up_fly.setVisibility(View.VISIBLE);
                    Up_jump.setVisibility(View.VISIBLE);
                    if (energy_show) Up_energy.setVisibility(View.VISIBLE);
                    else {
                        //todo чё за дичь? похоже на костыль, разобраться
                        if (gw.pen.next_bust > 0) {
                            MainActivity.energy_show = true;
                            Up_energy.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } else /*if (update == -1)*/ {
                //Fly.setImageResource(R.drawable.b1);
                //Setup.setImageResource(R.drawable.b2);
                //Setup.setBackgroundResource(R.drawable.upgrade_menu);
                //Setup.setBackgroundResource(R.drawable.b2);
                setup = false;
                Config.setVisibility(View.VISIBLE);
                Up_fly.setVisibility(View.INVISIBLE);
                Up_jump.setVisibility(View.INVISIBLE);
                Up_energy.setVisibility(View.INVISIBLE);
                //Reward.setVisibility(View.INVISIBLE);
                Fly.setVisibility(View.VISIBLE);
//                        Reward.setVisibility(View.INVISIBLE);
                //Fly.setVisibility(View.VISIBLE);
                if (gw.pen.status.equals("STF")) {
                    gw.pen.status = gw.pen.savedstatus;
                    gw.pen.savedstatus = "NON";
                    this.onStatusChanged(gw.pen.status, gw.pen.savedstatus);
                }
            }

        } else if (id == R.id.Up_fly) {
            if (update == -1) {
                update = 1;
            }
        } else if (id == R.id.Up_jump) {
            if (update == -1) {
                update = 0;
            }
        } else if (id == R.id.Up_energy) {

            if (update == -1) {
                update = 2;
            }
        } else if (id == R.id.Config) {
            Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
            startActivity(intent);
        }
//         else if (id == R.id.Reward) {
//                    try {
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

//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    need_rew = true;
//
//        }
        else if (id == R.id.ask_stop_b) {
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
        } else if (id == R.id.ask_select_b) {
            if (shure == -1) {
                ask_number += 1;
                Ask_l.setVisibility(View.GONE);
                gw.pen.save_ask();
                ask_on = false;
                checkTeaching();
            } else if (shure == 0) {
                shure = -1;
                ask_number = -1;
                Ask_l.setVisibility(View.GONE);
                gw.pen.save_ask();
                ask_on = false;
            }
        }
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