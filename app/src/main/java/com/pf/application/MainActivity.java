package com.pf.application;

import static com.pf.application.Penguin.Status.FLD;
import static com.pf.application.Penguin.Status.FLU;
import static com.pf.application.Penguin.Status.GTF;
import static com.pf.application.Penguin.Status.RCV;
import static com.pf.application.Penguin.Status.RTF;
import static com.pf.application.Penguin.Status.UPD;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
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

import com.pf.application.Penguin.Status;

import java.text.DecimalFormat;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Penguin.MainListener, GameView.MainListener {
    public static boolean flying = false;
    public static int update = -1;
    public ImageButton Fly;
    public ImageButton btnTraining;
    public static ImageButton Up_fly;
    public static ImageButton Up_jump;
    public static ImageButton Up_energy;
    private ProgressBar pbConcentration, pbEnergy;
    //public static ImageButton Reward;
    public ImageButton Config;
    public ImageButton Ask_yes;
    public ImageButton Ask_no;
    ConstraintLayout Ask_l;
    ImageView Ask_image;
    TextView tvConcentration, tvRecord, tvEnergy;

    public static int dw, dh;
    //public static boolean testing = false;//true;
    public static GameView gw;
    public static Date first_date;
    public static boolean quick_down = false;

    public static boolean energy_show = false;
    public static float end = 0;
    //public AdRequest adRequest;
    public Context cont;

    //public static RewardedAd mRewardedAd;
//    private final String TAG = "MainActivity";

    public static byte tutorialNumber;
//    public static String[] ask_status;
//    public static String[] ask_savedstatus;

    private static Tutorial[] tutorials;
    private byte shure = -1;
    TextView ask;
    TextView ask_no;
    TextView ask_yes;

//    private boolean need_rew = true;
//    private int rew_error = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dw = getResources().getDisplayMetrics().widthPixels;//получаем ширину экрана
        dh = getResources().getDisplayMetrics().heightPixels;//получаем ширину экрана
        end = dh;
        first_date = new Date();

        cont = this;

        Fly = findViewById(R.id.btnFlight);
        btnTraining = findViewById(R.id.B2);
        Up_fly = findViewById(R.id.Up_fly);
        Up_jump = findViewById(R.id.Up_jump);
        Up_energy = findViewById(R.id.Up_energy);
        Config = findViewById(R.id.Config);
        Ask_yes = findViewById(R.id.ask_select_b);
        Ask_no = findViewById(R.id.ask_stop_b);
        Ask_image = findViewById(R.id.iv_ask);
        pbConcentration = findViewById(R.id.progressBar);
        pbEnergy = findViewById(R.id.progressBar2);
        tvConcentration = findViewById(R.id.tvConcentration);
        tvRecord = findViewById(R.id.tvRecord);
        tvEnergy = findViewById(R.id.tvEnergy);

        ask = findViewById(R.id.ask_tv);
        ask_no = findViewById(R.id.ask_stop_tv);
        ask_yes = findViewById(R.id.ask_select_tv);


//        ConstraintLayout.LayoutParams par = (ConstraintLayout.LayoutParams) Fly.getLayoutParams();
//        par.width = dw / 4;
//        par.height = dw / 4;
//        par.rightMargin = dw / 100;
//        par.bottomMargin = dw * 3 / 100 + dw / 4;
//        Fly.setLayoutParams(par);

        //  ConstraintLayout.LayoutParams par = (ConstraintLayout.LayoutParams) btnTraining.getLayoutParams();
//        par.width = dw / 4;
//        par.height = dw / 4;
//        par.leftMargin = dw / 100;
//        par.bottomMargin = dw / 100;
//        Setup.setLayoutParams(par);

//        par = (ConstraintLayout.LayoutParams) Reward.getLayoutParams();
//        par.width = dw / 4;
//        par.height = dw / 4;
//        par.leftMargin = dw / 100;
//        par.bottomMargin = dw * 3 / 100 + dw / 4;
//        Reward.setLayoutParams(par);

        ConstraintLayout.LayoutParams par = (ConstraintLayout.LayoutParams) Up_energy.getLayoutParams();
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
        MainActivity.tutorialNumber = (byte) myPreferences.getInt("ask_number", 0);

//        ask_status = new String[]{"RTF", "RTF", "RTF", "GTF", "FLU", "RCV", "RTF", "STF", "STF", "STF", "STF", "STF"};
//        ask_savedstatus = new String[]{"NON", "NON", "NON", "NON", "NON", "NON", "NON", "RTF", "RTF", "RTF", "UPD", "UPD"};
        tutorials = new Tutorial[]{
                new Tutorial(R.string.firstHello, null, RTF),
                new Tutorial(R.string.teachingMainMenu, null, RTF, GameView.Room.Flying),
                new Tutorial(R.string.teachingPrepareToFly, R.drawable.btn_fly_idle, RTF),
                new Tutorial(R.string.teachingConcentration, R.drawable.concentration, GTF),
                new Tutorial(R.string.teachingCurrentHeight, null, FLD, FLU),
                new Tutorial(R.string.teachingHeightRecord, null, FLD),
                new Tutorial(R.string.teachingRecovery, null, RCV),
                new Tutorial(R.string.teachingTrainingBtn, R.drawable.table_btn, RTF),
                new Tutorial(R.string.teachingTrainingMenu, null, RTF, GameView.Room.Training),
                new Tutorial(R.string.teachingFirstTraining, null, RTF, GameView.Room.Training),
                new Tutorial(R.string.teachingHowOpenNewTraining, null, RTF, GameView.Room.Training),
                new Tutorial(R.string.teachingTrainingProgress, null, UPD, GameView.Room.Training),
                new Tutorial(R.string.teachingConcentrationTraining, R.drawable.btn_fly_idle, UPD, GameView.Room.Training),
                new Tutorial(R.string.teachingBackToMainMenu, R.drawable.table_btn, RCV, GameView.Room.Training),
                new Tutorial(R.string.teachingKeepFly, null, RCV, FLD)
        };

        Fly.setVisibility(View.VISIBLE);

        Fly.setOnClickListener(this);

        btnTraining.setOnClickListener(this);
        Up_fly.setOnClickListener(this);
        Up_jump.setOnClickListener(this);
        Up_energy.setOnClickListener(this);

//        Reward.setOnTouchListener(this);
        Config.setOnClickListener(this);

        Ask_l = findViewById(R.id.ask_layout);
        Ask_l.setVisibility(View.GONE);
        Ask_yes.setOnClickListener(this);
        Ask_no.setOnClickListener(this);


//       MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//
//        adRequest = new AdRequest.Builder().build();
    }


    @Override
    public void onConcentrationChanged(float concentration) {
        runOnUiThread(() -> {
            ProgressBarAnimation concentrationAnimation = new ProgressBarAnimation(pbConcentration, pbConcentration.getProgress(), (int) (concentration));
            concentrationAnimation.setDuration(250);
            pbConcentration.startAnimation(concentrationAnimation);

            //pbConcentration.setProgress((int) bust);
            DecimalFormat df = new DecimalFormat("0.0");

            if (concentration >= 0)
                tvConcentration.setText(df.format(concentration));
//            else
//                tvConcentration.setText("0.0");
        });
    }

    @Override
    public void onBustChanged(double bust) {
        runOnUiThread(() -> {
            //когда пингвин может летать нужно показывать энергию
            if (bust > 0) {
                energy_show = true;
                pbEnergy.setVisibility(View.VISIBLE);
                tvEnergy.setVisibility(View.VISIBLE);
                if (gw.getSelectedRoom() == GameView.Room.Training) {
                    Up_energy.setVisibility(View.VISIBLE);
                }
            } else {
                energy_show = false;
                pbEnergy.setVisibility(View.INVISIBLE);
                tvEnergy.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onEnergyChanged(double energy, double maxEnergy) {
        runOnUiThread(() -> {
            ProgressBarAnimation energyAnimation = new ProgressBarAnimation(pbEnergy, pbEnergy.getProgress(), (int) (energy * 100 / maxEnergy));
            energyAnimation.setDuration(500);
            pbEnergy.startAnimation(energyAnimation);
            //pbEnergy.setProgress((int) (energy * 100 / maxEnergy));
            tvEnergy.setText(String.valueOf((int) energy));
        });
    }

    @Override
    public void onRoomChanged(GameView.Room room) {
        if (room == GameView.Room.Training) {
            Up_fly.setVisibility(View.VISIBLE);
            Up_jump.setVisibility(View.VISIBLE);
            if (energy_show) Up_energy.setVisibility(View.VISIBLE);
        } else {
            Up_fly.setVisibility(View.INVISIBLE);
            Up_jump.setVisibility(View.INVISIBLE);
            Up_energy.setVisibility(View.INVISIBLE);
        }
        checkStatus(status);
    }

//    private void checkRoom(GameView.Room room){
//
//    }


    public static class ProgressBarAnimation extends Animation {
        private ProgressBar progressBar;
        private float from;
        private float to;

        public ProgressBarAnimation(ProgressBar progressBar, float from, float to) {
            super();
            this.progressBar = progressBar;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            progressBar.setProgress((int) value);
        }

    }

    @Override
    public void onStatusChanged(Status status) {
        runOnUiThread(() -> {
            checkStatus(status);
        });
    }

    private void checkStatus(Status status) {
        MainActivity.this.prevStatus = MainActivity.this.status;
        MainActivity.this.status = status;
        if (gw.getSelectedRoom() == GameView.Room.Training) {
            Config.setVisibility(View.INVISIBLE);
            if (status == UPD) {
                btnTraining.setVisibility(View.INVISIBLE);
                Fly.setVisibility(View.VISIBLE);
            } else {
                Fly.setVisibility(View.INVISIBLE);
                btnTraining.setVisibility(View.VISIBLE);
            }
        } else {
            if (status == RTF) {
                btnTraining.setVisibility(View.VISIBLE);
            } else {
                btnTraining.setVisibility(View.INVISIBLE);
            }

            if (status == RCV || status == FLD) {
                Fly.setVisibility(View.INVISIBLE);
            } else {
                Fly.setVisibility(View.VISIBLE);
            }

            if (status == GTF || status == FLU || status == FLD) {
                Config.setVisibility(View.INVISIBLE);
            } else {
                Config.setVisibility(View.VISIBLE);
            }
        }
        checkTeaching();
    }

    @Override
    public void onRecordChanged(double record) {
        runOnUiThread(() -> {
            DecimalFormat df = new DecimalFormat("0.00");
            String mtric = "";
            if (record < 1)
                mtric = df.format(record * 100) + getString(R.string.santimeter);
            else if (record < 1000)
                mtric = df.format(record) + getString(R.string.meter);
            else if (record < 1000000)
                mtric = df.format(record / 1000) + getString(R.string.kilometer);


            tvRecord.setText(mtric);
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        if (gw == null) {
            gw = new GameView(this, this, this);
            gw.load_game();
            if (MainActivity.update != -1) {
                //Fly.setImageResource(R.drawable.bust_training);
                //Setup.setBackgroundResource(R.drawable.back_b);
                gw.changeRoom(GameView.Room.Training);
                //if(mRewardedAd == null)
//            Reward.setVisibility(View.INVISIBLE);
//                if (!energy_show)
//                    Up_energy.setVisibility(View.INVISIBLE);
//                Config.setVisibility(View.INVISIBLE);
//            } else {
//                Up_fly.setVisibility(View.INVISIBLE);
//                Up_jump.setVisibility(View.INVISIBLE);
//                Up_energy.setVisibility(View.INVISIBLE);
//            Reward.setVisibility(View.INVISIBLE);
            }

            ConstraintLayout gameLayout = findViewById(R.id.GL); // находим gameLayout
            ConstraintLayout.LayoutParams l = (ConstraintLayout.LayoutParams) gameLayout.getLayoutParams();
            l.height = dh;
            l.width = dw;
            gameLayout.setLayoutParams(l);

            gameLayout.addView(gw); // и добавляем в него gameView
        } else {
            gw.setCanDraw(true);
        }
    }

    Status status;
    Status prevStatus;

    private void checkTeaching() {

        if (!ask_on && shure == -1)
            if (tutorialNumber != -1 && tutorialNumber < (tutorials.length)) {
                if (tutorials[tutorialNumber].currentStatus.equals(status)
                        && (tutorials[tutorialNumber].prevStatus == null
                        || tutorials[tutorialNumber].prevStatus.equals(prevStatus))
                        && (tutorials[tutorialNumber].room == null
                        || tutorials[tutorialNumber].room == gw.getSelectedRoom())
                )
                    set_ask();
            }
    }

    //    public short ads_timeout = 0;
    public static boolean ask_on = false;

    private void set_ask() {
        ask_on = true;
        ask.setText(tutorials[tutorialNumber].text);
        if (tutorialNumber == 0) {
            ask_no.setText(R.string.No);
            ask_yes.setText(R.string.Yes);
        } else {
            ask_no.setText(R.string.ask_stop);
            ask_yes.setText(R.string.ask_next);
        }

        if (tutorials[tutorialNumber].res != null) {
            Ask_image.setVisibility(View.VISIBLE);
            Ask_image.setImageResource(tutorials[tutorialNumber].res);
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

    private static class Tutorial {
        Integer text;
        Integer res;
        Status currentStatus;
        Status prevStatus;
        GameView.Room room;

        public void setText(Integer text) {
            this.text = text;
        }

        public void setRes(Integer res) {
            this.res = res;
        }

        public void setCurrentStatus(Status currentStatus) {
            this.currentStatus = currentStatus;
        }


        public Tutorial(Integer text, Integer res, Status currentStatus) {
            this.text = text;
            this.res = res;
            this.currentStatus = currentStatus;
        }

        public Tutorial(Integer text, Integer res, Status currentStatus, GameView.Room room) {
            this.text = text;
            this.res = res;
            this.currentStatus = currentStatus;
            this.room = room;
        }

        public Tutorial(Integer text, Integer res, Status currentStatus, Status prevStatus) {
            this.text = text;
            this.res = res;
            this.prevStatus = prevStatus;
            this.currentStatus = currentStatus;
        }

        public Tutorial() {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            gw.setCanDraw(false);
            gw.pen.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Bundle extras = result.getData().getExtras();
                        if (extras.containsKey("newGame")) {
                            gw.new_game();
                        }
                        if (extras.containsKey("skipFall")) {
                            quick_down = extras.getBoolean("skipFall");
                        }
                        if (extras.containsKey("resetTutorial")) {
                            tutorialNumber = 1;
                            checkTeaching();
                        }
                        //doSomeOperations();
                    }
                }
            });

    @Override
    public void onClick(View button) {
        int id = button.getId();// определяем какая кнопка
        if (id == R.id.btnFlight) {
            flying = true;
        } else if (id == R.id.B2) {

            if (gw.getSelectedRoom() != GameView.Room.Training) {
                if (gw.pen.status.equals("RCV") || gw.pen.status.equals("RTF") || gw.pen.status.equals("UPD")) {
                    //this.onStatusChanged(gw.pen.status, gw.pen.savedstatus);
                    //Fly.setImageResource(R.drawable.bust_training);
                    //Fly.setImageResource(R.drawable.imb);
                    //Setup.setImageResource();
                    //Setup.setBackground(R.drawable.back_b);
                    //Setup.setBackgroundResource(R.drawable.back_b);
                    gw.changeRoom(GameView.Room.Training);

//                    if (!gw.pen.savedstatus.equals("UPD")) {
//                        btnTraining.setVisibility(View.VISIBLE);
//                        Fly.setVisibility(View.INVISIBLE);
//                    } else {
//                        btnTraining.setVisibility(View.INVISIBLE);
//                        Fly.setVisibility(View.VISIBLE);
//                    }
//                                else if (MainActivity.mRewardedAd != null)
//                                    Reward.setVisibility(View.VISIBLE);
                    //Config.setVisibility(View.INVISIBLE);
//                    Up_fly.setVisibility(View.VISIBLE);
//                    Up_jump.setVisibility(View.VISIBLE);
//                    if (energy_show) Up_energy.setVisibility(View.VISIBLE);
//                    else {
//                        //todo чё за дичь? похоже на костыль, разобраться
//                        if (gw.pen.next_bust > 0) {
//                            MainActivity.energy_show = true;
//                            Up_energy.setVisibility(View.VISIBLE);
//                        }
//                    }
                }
            } else /*if (update == -1)*/ {
                //Fly.setImageResource(R.drawable.b1);
                //Setup.setImageResource(R.drawable.b2);
                //Setup.setBackgroundResource(R.drawable.upgrade_menu);
                //Setup.setBackgroundResource(R.drawable.b2);
                gw.changeRoom(GameView.Room.Flying);
                //Config.setVisibility(View.VISIBLE);
//                Up_fly.setVisibility(View.INVISIBLE);
//                Up_jump.setVisibility(View.INVISIBLE);
//                Up_energy.setVisibility(View.INVISIBLE);
//                //Reward.setVisibility(View.INVISIBLE);
//                Fly.setVisibility(View.VISIBLE);
//                        Reward.setVisibility(View.INVISIBLE);
                //Fly.setVisibility(View.VISIBLE);
//                if (gw.pen.status.equals("STF")) {
//                    gw.pen.status = gw.pen.savedstatus;
//                    gw.pen.savedstatus = "NON";
//                    this.onStatusChanged(gw.pen.status, gw.pen.savedstatus);
//                }
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
            intent.putExtra("skipFall", quick_down);
            resultLauncher.launch(intent);
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
                checkTeaching();
            }
        } else if (id == R.id.ask_select_b) {
            if (shure == -1) {
                tutorialNumber += 1;
                Ask_l.setVisibility(View.GONE);
                gw.pen.save_ask();
                ask_on = false;
                checkTeaching();
            } else if (shure == 0) {
                shure = -1;
                tutorialNumber = -1;
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