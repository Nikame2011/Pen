package com.pf.application;

import static com.pf.application.Penguin.Status.FLD;
import static com.pf.application.Penguin.Status.FLU;
import static com.pf.application.Penguin.Status.GTF;
import static com.pf.application.Penguin.Status.RCV;
import static com.pf.application.Penguin.Status.RTF;
import static com.pf.application.Penguin.Status.UPD;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.preference.PreferenceManager;

import com.pf.application.GameView.Coordinate;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Penguin {

    public interface MainListener {
        void onConcentrationChanged(float concentration);

        void onBustChanged(double bust);

        void onEnergyChanged(double energy, double maxEnergy);

        void onStatusChanged(Status status);

        void onRecordChanged(double record);

        void onDrawPositionChanged(double x, double y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    private float x;// координаты
    protected float sx;// координаты нуля
    private float y;// координаты
    protected float sy;// координаты нуля
    protected float pen_coord = 0;//игровые координаты
    protected float saveY;//местный рекорд во время полёта
    protected float speed; // скорость
    protected byte energy; //текущая энергия
    protected byte max_strong = 90; //ограничение концентрации
    public int dw, dh; //восота и ширина экрана
    protected float downspeed = (float) -0.5; //ограничитель скорости падения
    protected float grav = (float) 0.015;
    //private String header_type="ny_2022";

    protected Bitmap fone_red; // картинка
    protected Bitmap fone_green; // картинка
    protected Bitmap fone_white; // картинка

    protected Date savedate;
    protected Date break_date;
    protected Date d; //текущее время

    protected float to_update = -1;
    protected float update_time = 0;//для сохранения длительности текущего улучшения

    protected Context con;


  /* protected float[] jump_up=new float[]{0,(float)0.07,(float) 0.08,(float) 0.09,(float) 0.11,(float) 0.13,(float)0.16,(float)0.19,(float)0.23};
    protected float[] jump_record = new float[]{(float)0,(float) 0.145, (float) 0.185, (float) 0.23, (float) 0.335, (float) 0.46, (float) 0.75, (float) 1.0};

    protected float[]  bust_up=new float[]{0,(float) 0.01,(float) 0.02,(float) 0.03,(float) 0.04,(float)0.05,(float)0.06,(float)0.07,(float) 0.09,(float) 0.11,(float) 0.13,(float) 0.17, (float) 0.21};
    protected float[] bust_record = new float[]{(float) 0.68, (float) 1.51, (float) 1.69, (float) 1.91, (float) 2.19, (float) 2.59, (float) 3.16,(float)3.88,(float)6.93,(float)12,19,57,133};
    
    protected byte[]energy_up=new byte[]{0,5, 6,7,8,10,12,15,19};
    protected float[] energy_record = new float[]{0, (float) 5.68, (float) 6.3, (float) 10.7, (float) 18.16, (float) 23.87, (float) 30.19, (float) 40.7};
*/

    //protected float[] time_to_up=new float[]{5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5};
    //protected float[] time_to_up = new float[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    protected float[] time_to_up = new float[]{5, 5, 10, 15, 15, 30, 30, 30, 60, 60, 60, 90, 90, 120, 150, 180, 180, 210, 210, 210, 240, 240, 300, 360, 360, 420, 480, 540, 600, 660, 720, 780};
    //перечень времён для улучшения параметров

    protected float[] jump_up = new float[]{0, (float) 0.05, (float) 0.06, (float) 0.07, (float) 0.08, (float) 0.09, (float) 0.10, (float) 0.11, (float) 0.12, (float) 0.13, (float) 0.14, (float) 0.15, (float) 0.16, (float) 0.17, (float) 0.18, (float) 0.19, (float) 0.20, (float) 0.21, (float) 0.22, (float) 0.23, (float) 0.24};
    protected float[] jump_record = new float[]{(float) 0, (float) 0.125, (float) 0.164, (float) 0.207, (float) 0.257, (float) 0.31, (float) 0.37, (float) 0.467, (float) 0.87, (float) 0.98, (float) 4.45, (float) 23.685, (float) 32.7, (float) 55.4, (float) 56.3, (float) 65.0, (float) 77.6, (float) 103.1, (float) 153.3, (float) 195.2};

    protected float[] bust_up = new float[]{0, (float) 0.01, (float) 0.02, (float) 0.03, (float) 0.04, (float) 0.05, (float) 0.06, (float) 0.07, (float) 0.09, (float) 0.11, (float) 0.12, (float) 0.13, (float) 0.14, (float) 0.15, (float) 0.16, (float) 0.17, (float) 0.18, (float) 0.19, (float) 0.20, (float) 0.21, (float) 0.22};
    protected float[] bust_record = new float[]{(float) 0.43, (float) 0.54, (float) 0.58, (float) 0.63, (float) 0.68, (float) 0.74, (float) 0.807, (float) 1.47, (float) 1.88, (float) 3.396, (float) 3.9, (float) 4.729, (float) 13.08, (float) 24.3, (float) 28.3, (float) 33.4, (float) 57.3, (float) 78.8, (float) 119.9, (float) 175.0};

    protected byte[] energy_up = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    protected float[] energy_record = new float[]{0, (float) 1.09, (float) 2.366, (float) 5.339, (float) 7.57, (float) 10.155, (float) 15.27, (float) 19.25, (float) 38.1, (float) 46.4, (float) 66.0, (float) 88.7, (float) 104.4, (float) 134.4, (float) 154.8, (float) 200.0, (float) 200.0, (float) 200.0, (float) 200.0, (float) 200.0};

    protected byte ml_bust = (byte) (bust_up.length - 1);
    protected byte ml_jump = (byte) (jump_up.length - 1);
    protected byte ml_energy = (byte) (energy_up.length - 1);

    //protected float money; //деньги
    protected byte maxenergy; //энергия
    protected double bust;
    protected double jump;
    protected float maxY;//рекорд

    protected byte next_jump;
    protected byte next_bust;
    protected byte next_energy;

    public Status status = RTF;

    public enum Status {
        UPD,// - происходит улучшение
        RCV,// - восстанавливается энергия
        RTF,// - ready to fly когда стоит и готов лететь
        GTF,// - подготовка к полёту (обратный отсчёт)
        FLU,// - полёт вверх
        FLD,// - падение
    }
    /*
  ----  NON - отсутствует (есть только у сохраняемого статуса)
  ----  STF - полёт заблокирован открытым меню
    UPD - происходит улучшение
    RCV - восстанавливается энергия
    RTF - ready to fly когда стоит и готов лететь
    GTF - подготовка к полёту (обратный отсчёт)
    FLU - полёт вверх
    FLD - падение
    */

//    protected String anima_type = "standing";
    /*
    standing
    jump
    bust
    fly_up
    fly_down
    recovery
     */
//int framesMultiple=1;//==9/targetFrames логика работы: при увеличении количества кадров не нужно пересматривать коэффициенты сторости и падения параметров, они пересчитаются

    MainListener listener;

    int boxAndSide;
    int box;
    int side;

    public Penguin(Context context, byte n_j, byte n_b, byte n_e, float rec, Date time, float tu, MainListener listener) {
        this.listener = listener;

        dw = MainActivity.dw;
        dh = MainActivity.dh;
        localDp = dw / 850d;
        boxAndSide = convert(229.5);
        box = convert(212.5);
        side = convert(17);
        con = context;

        x = box;
        sx = x;

        y = MainActivity.end - box * 3 - 4 * side;
        sy = y;

        maxY = 0;
        listener.onRecordChanged(maxY);
        saveY = 0;

        speed = 0;
        concentration = 0;

        /*if (MainActivity.testing) {
            jump_up=new float[]{0,(float)0.07,(float) 0.08,(float) 0.09,(float) 0.11,(float) 0.13,(float)0.16,(float)0.19,(float)0.23};
            bust_up=new float[]{0,(float) 0.01,(float) 0.02,(float) 0.03,(float) 0.04,(float)0.05,(float)0.06,(float)0.07,(float) 0.09,(float) 0.11,(float) 0.13,(float) 0.17, (float) 0.21};
            energy_up=new byte[]{0,5, 6,7,8,10,12,15,19};

            jump_record = new float[]{0,(float)0,(float) 0, (float) 0, (float) 0, (float) 0, (float) 0, (float) 0, (float) 0};
            bust_record = new float[]{(float) 0, (float) 0, (float) 0, (float) 0, (float) 0, (float) 0, (float) 0,0,0,0,0,0};
            energy_record = new float[]{0,(float) 0, (float) 0, (float) 0, (float) 0, (float) 0, (float) 0, (float) 0,0};
            time_to_up=new float[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
            //time_to_up=new float[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0};

            next_jump=8;
            next_bust=12;
            next_energy=8;
        }
        else
        {*/
        next_jump = n_j;
        next_bust = n_b;
        next_energy = n_e;
        maxY = rec;
        listener.onRecordChanged(maxY);


        savedate = time;
        to_update = tu;
        if (MainActivity.update != -1) {
            status = UPD;
            switch (MainActivity.update) {
                case 0 -> update_time = time_to_up[next_jump];
                case 1 -> update_time = time_to_up[next_bust + 5];
                case 2 -> update_time = time_to_up[next_energy + 10];
            }
        }
        listener.onStatusChanged(status);

        bust = bust_up[next_bust];
        listener.onBustChanged(bust);
        jump = jump_up[next_jump];
        maxenergy = energy_up[next_energy];
        energy = maxenergy;
        listener.onEnergyChanged(energy, maxenergy);

        init(context); // инициализация ресурсов

    }

    public void new_game() {
        x = box;
        sx = x;

        y = MainActivity.end - box * 3 - 4 * side;
        sy = y;

        saveY = 0;
        speed = 0;
        concentration = 0;
        next_jump = 1;
        next_bust = 0;
        next_energy = 1;
        maxY = 0;
        listener.onRecordChanged(maxY);
        savedate = MainActivity.first_date;
        to_update = -1;
        bust = bust_up[next_bust];
        listener.onBustChanged(bust);
        jump = jump_up[next_jump];
        maxenergy = energy_up[next_energy];
        energy = maxenergy;
        listener.onEnergyChanged(energy, maxenergy);
        start_date = MainActivity.first_date;
        //status=new String();
        status = RTF;
        listener.onStatusChanged(status);
        animator.targetAnimation = "standing";
        animator.step = 0;
    }

    void init(Context context) { // сжимаем картинку до нужных размеров
        start_date = MainActivity.first_date;

        fone_red = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.fone_red),
                boxAndSide, boxAndSide, false);

        fone_green = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.fone_green),
                boxAndSide, boxAndSide, false);

        fone_white = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.fone_white),
                boxAndSide, boxAndSide, false);

        animator = new Animator();
        animator.init(context);
    }

    Animator animator;

    double localDp;

    private int convert(int size) {
        return (int) (localDp * size);
    }

    private int convert(double size) {
        return (int) (localDp * size);
    }

    public void update() { // тут будут вычисляться новые координаты
        if (!MainActivity.ask_on) {
            newfly();
        }
    }

    private float concentration = 0;
    private boolean start = true;
    public Date start_date;
    public float short_update = 0;
    public Date shorting;
    public Date lastTap;

    void newfly() {
        //strong=50;
        if (MainActivity.flying) {//если включено событие - нажата кнопка
            lastTap = new Date();
            switch (status) {
                case RTF -> {
                    start_date = new Date();
                    status = GTF;
                    listener.onStatusChanged(status);
                }
                //strong = (byte) (85);
                case GTF -> {
                    if (concentration < 75)
                        concentration = (float) Math.min(concentration + 5.5, max_strong);  //прибавляем силу
                    else if (concentration < 80)
                        concentration = (float) Math.min(concentration + 3.5, max_strong);  //прибавляем силу
                    else if (concentration < 85)
                        concentration = (float) Math.min(concentration + 3.0, max_strong);  //прибавляем силу
                    else
                        concentration = (float) (Math.min(concentration + 1.5, max_strong));  //прибавляем силу


                    //strong = (float) Math.min(strong + 3.5, max_strong);
                    listener.onConcentrationChanged(concentration);
                }
                case FLU -> {
                    if (concentration > 0) {
                        if (concentration < 75)
                            concentration = (float) Math.min(concentration + 4.0, max_strong);  //прибавляем силу
                        else if (concentration < 80)
                            concentration = (float) Math.min(concentration + 3.5, max_strong);  //прибавляем силу
                        else if (concentration < 85)
                            concentration = (float) Math.min(concentration + 3.0, max_strong);  //прибавляем силу
                        else
                            concentration = (float) (Math.min(concentration + 2.5, max_strong));  //прибавляем силу
                    }
                    //if (strong>0) strong = (float) Math.min(strong + 3.5, max_strong);
                    listener.onConcentrationChanged(concentration);
                }
                case UPD -> {
                    if (to_update != -1) {
//                            short_update += 0.1f;
//                            shorting = new Date();

                        if (concentration < 75)
                            concentration = (float) Math.min(concentration + 5.5, max_strong);  //прибавляем силу
                        else if (concentration < 80)
                            concentration = (float) Math.min(concentration + 3.5, max_strong);  //прибавляем силу
                        else if (concentration < 85)
                            concentration = (float) Math.min(concentration + 3.0, max_strong);  //прибавляем силу
                        else
                            concentration = (float) (Math.min(concentration + 1.5, max_strong));  //прибавляем силу


                        //strong = (float) Math.min(strong + 3.5, max_strong);
                        listener.onConcentrationChanged(concentration);
                    }
                }
            }
            MainActivity.flying = false; //отключаем событие, чтобы кнопка вновь могла сработать
        } else if (lastTap != null && concentration > 0 && new Date().getTime() - lastTap.getTime() > 1000) {
            concentration -= 3;
        }

        switch (status) {
            case RTF:
                break;

            case GTF:
                d = new Date();
                if (5 <= (float) (d.getTime() - start_date.getTime()) / 1000) {
                    //anima_type="jump";
                    //anim_step=0;
                    if (concentration > 0) {
                        status = FLU;
                        listener.onStatusChanged(status);
                        start_date = MainActivity.first_date;
                    } else {
                        status = RCV;
                        listener.onStatusChanged(status);
                        break_date = new Date();
                    }
                }
                concentration = (float) Math.max(concentration - 0.75, 0);
                listener.onConcentrationChanged(concentration);
                break;

            case FLU:
                switch (animator.targetAnimation) {
                    case "jump" -> {
                        if (animator.step == 6)
                            speed += (float) (jump * concentration / 100f);
                    }
                    case "bust" -> {
                        if (animator.step == 2) {
                            speed += (float) (bust * concentration / 100f);
                            energy -= 1;
                            if (energy == 0) {
                                concentration = 0;
                            }
                            listener.onEnergyChanged(energy, maxenergy);
                        }
                    }
                }
                concentration = (float) Math.max(concentration - 0.75, 0);
                pen_coord += speed; //меняем высоту
                if (saveY < pen_coord) {
                    saveY = pen_coord;
                    if (maxY < saveY) {
                        listener.onRecordChanged(saveY);
                    }
                }
                if (!animator.targetAnimation.equals("jump") || animator.step > 6) {
//if(true){}
                    speed -= grav; //уменьшаем скорость на притяжение
                    if (speed < 0) {
                        status = FLD;
                        listener.onStatusChanged(status);
                        concentration = 0;
                    }
                }
                listener.onConcentrationChanged(concentration);
                break;


            case FLD:

                pen_coord += speed; //меняем высоту
                if (speed > downspeed) {
                    speed -= grav; //уменьшаем скорость на притяжение
                } else if (MainActivity.quick_down) {
                    pen_coord = 0;
                }
                if (pen_coord <= 0) {
                    pen_coord = 0; //если будем ниже земли, встаём на землю
                    speed = 0;
                    if (maxY < saveY) {
                        maxY = saveY;
                        listener.onRecordChanged(maxY);
                        save();
                    }
                    saveY = 0;
                    status = RCV;
                    listener.onStatusChanged(status);
                    break_date = new Date();
                }
                break;
            case RCV:
                concentration = 0;
                listener.onConcentrationChanged(concentration);
                switch (animator.targetAnimation) {
                    case "up_en" -> {
                        x -= (float) (jump * dw / 3f);
                        if (x < -box) {
                            x = dw;
                        }
                    }
                    case "up_jmp" -> {
                        if (animator.step < 2) pen_coord += (float) (jump / (animator.step + 1));
                        if (animator.step > 2) pen_coord -= (float) (jump / (5 - animator.step));
                    }
                    case "up_bst" -> {
//todo ужно в анимации это как-то обыграть?                        if (animator.step < 3)
//                            pen_coord += (float) ((bust + jump) / (animator.step + 1));
//                        if (animator.step > 3)
//                            pen_coord -= (float) ((bust + jump) / (7 - animator.step));
                    }
                    default -> {
                        if (x != sx) x = sx;
                        if (pen_coord != 0) pen_coord = 0;
                        d = new Date();
                        if (3 <= (float) (d.getTime() - break_date.getTime()) / 1000) {
                            if (energy < maxenergy) {
                                energy += 1;
                                listener.onEnergyChanged(energy, maxenergy);
                            } else {
                                status = RTF;
                                listener.onStatusChanged(status);
                            }
                        }
                    }
                }
                break;

            case UPD:
                concentration = (float) Math.max(concentration - 0.75, 5);
                listener.onConcentrationChanged(concentration);
                switch (animator.targetAnimation) {
                    case "up_en" -> {
                        x -= (float) (jump * dw / 3);
                        if (x < -box) {
                            x = dw;
                        }
                    }
                    case "up_jmp" -> {
                        if (animator.step < 2) pen_coord += (float) (jump / (animator.step + 1));
                        if (animator.step > 2) pen_coord -= (float) (jump / (5 - animator.step));
                    }
                    case "up_bst" -> {
//                        if (animator.step > 3)
//                            pen_coord += (float) ((bust + jump) / (animator.step - 3));
//                        if (animator.step < 3 && pen_coord > 0)
//                            pen_coord -= (float) ((bust + jump) / (3 - animator.step));
                    }
                }
                break;
        }

        y = sy - pen_coord * dw / 3;

        if (MainActivity.update != -1) {
            if (to_update == -1) {
                if (status != RCV) {
                    switch (MainActivity.update) {
                        case 0 -> {
                            if (next_jump < ml_jump) {
                                if (jump_record[next_jump] <= maxY) {
                                    to_update = time_to_up[next_jump];
                                    update_time = time_to_up[next_jump];
                                    savedate = new Date();
                                    d = new Date();
                                    status = UPD;
                                    listener.onStatusChanged(status);
                                } else MainActivity.update = -1;
                            } else MainActivity.update = -1;
                        }
                        case 1 -> {
                            if (next_bust < ml_bust) {
                                if (bust_record[next_bust] <= maxY) {
                                    to_update = time_to_up[next_bust + 5];
                                    update_time = time_to_up[next_bust + 5];
                                    savedate = new Date();
                                    d = new Date();
                                    status = UPD;
                                    listener.onStatusChanged(status);
                                } else MainActivity.update = -1;
                            } else MainActivity.update = -1;
                        }
                        case 2 -> {
                            if (next_energy < ml_energy) {
                                if (energy_record[next_energy] <= maxY) {
                                    to_update = time_to_up[next_energy + 10];
                                    update_time = time_to_up[next_energy + 10];
                                    savedate = new Date();
                                    d = new Date();
                                    status = UPD;
                                    listener.onStatusChanged(status);
                                } else MainActivity.update = -1;
                            } else MainActivity.update = -1;
                        }
                    }
                    save();
                } else MainActivity.update = -1;
            } else {
                if (concentration > 5 && d.getTime() > savedate.getTime()) {
                    shorting = new Date();
                    short_update += (shorting.getTime() - d.getTime()) * (concentration - 5) / 100 / 1000;
                }
                d = new Date();
//                short_update += 0.1f;
//                shorting = new Date();


                if (short_update > 0) {
                    if (1 <= (float) (d.getTime() - shorting.getTime()) / 1000) {
                        to_update -= Math.min(1, short_update);
                        short_update -= Math.min(1, short_update);
                    }
                }

                if (to_update - short_update <= (float) (d.getTime() - savedate.getTime()) / 1000) {
                    switch (MainActivity.update) {
                        case 0 -> {
                            next_jump++;
                            jump = jump_up[next_jump];
                            to_update = -1;
                            short_update = 0;
                            MainActivity.update = -1;
                            save();
                            status = RCV;
                            listener.onStatusChanged(status);
                            break_date = new Date();
                        }
                        case 1 -> {
                            next_bust++;
                            bust = bust_up[next_bust];
                            listener.onBustChanged(bust);
                            to_update = -1;
                            short_update = 0;
                            MainActivity.update = -1;
                            save();
                            status = RCV;
                            listener.onStatusChanged(status);
                            break_date = new Date();
                        }
                        case 2 -> {
                            next_energy++;
                            maxenergy = energy_up[next_energy];
                            listener.onEnergyChanged(energy, maxenergy);
                            to_update = -1;
                            short_update = 0;
                            MainActivity.update = -1;
                            save();
                            status = RCV;
                            listener.onStatusChanged(status);
                            break_date = new Date();
                        }
                    }
                }
            }
        }
    }


    float shiftX = 0;

    private class Animator {

        HashMap<String, Object> boolConditions = new HashMap<>();
        String targetAnimation;
        int step;
        HashMap<String, Animation> animations;
        Matrix transMatrix = new Matrix();

        public void switchStep() {
            step++;
            Animation targetAnim = animations.get(targetAnimation);
            if (targetAnim != null) {
                List<SwitchCondition> conditions = targetAnim.conditions.get(step);
                if (conditions != null) {
                    for (SwitchCondition condition : conditions) {
                        boolean allConditionsIsTrue = true;
                        for (String boolName : condition.conditions.keySet()) {
                            Object bool = boolConditions.get(boolName);
                            if (bool == null || (bool != condition.conditions.get(boolName))) {
                                allConditionsIsTrue = false;
                                break;
                            }
                        }

                        if (allConditionsIsTrue) {
                            targetAnimation = condition.targetAnimation;
                            step = condition.targetStep;
                            break;
                        }

                    }
                }
            }
        }

        public void updateBooleans() {
            boolConditions.put("bustLargerZero", bust > 0);

            boolConditions.put("speedLargerZero", speed > 0);

            boolConditions.put("energyLargerZero", energy > 0);

            boolConditions.put("concentrationLargerZero", concentration > 0);

            boolConditions.put("coordLargerZero", pen_coord > 0);

            if (d != null && break_date != null)
                boolConditions.put("isRecoveryTime", 3000 > d.getTime() - break_date.getTime());
            else boolConditions.put("isRecoveryTime", false);

            boolConditions.put("status", status);

            boolConditions.put("training", MainActivity.update);

            boolConditions.put("default", true);
        }

        public void draw(Paint paint, Canvas canvas, float drawX, float drawY) {

            Animation targetAnim = animations.get(targetAnimation);

            if (targetAnim != null) {
                if( targetAnim.preshift!=null){
                    drawX+= (float) targetAnim.preshift[step].getX();
                    drawY+= (float) targetAnim.preshift[step].getY();
                }
                transMatrix.setTranslate(drawX, drawY);

                for (Part part : Part.values()) {
                    double grad = 0;
                    if (targetAnim.partsAnimations.containsKey(part) && targetAnim.partsAnimations.get(part) != null) {
                        grad = targetAnim.partsAnimations.get(part).angles[step];
                    }
                    part.lastGrad = grad;
                }

                for (Part part : Part.values()) {
                    Matrix matrix = new Matrix();
                    matrix.set(transMatrix);
                    matrix.preConcat(part.getShiftMatrix());
                        if (part.lastGrad != 0)
                            matrix.preRotate((float) part.lastGrad, (float) part.center.x, (float) part.center.y);

                    canvas.drawBitmap(part.bitmap, matrix, paint);
                }
            }
        }

        public void init(Context context) {
            targetAnimation = "standing";
            Bitmap bod_2;
            Bitmap body;
            Bitmap head;
//            Bitmap header;
            Bitmap glases;
//            Bitmap hand;
            Bitmap hand0;
            Bitmap hand1;
            Bitmap rhand0;
            Bitmap rhand1;
            Bitmap legs;

            body = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.body_new),
                    convert(192), convert(370), false);

            bod_2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bod2_new),
                    convert(44), convert(69), false);

//            hand = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.hand_new),
//                    convert(89), convert(207), false);

//            hand0 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.hand0),
//                    convert(75), convert(123), false);
//
//            hand1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.hand1),
//                    convert(70), convert(135), false);

            hand0 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.left_swing),
                    convert(88), convert(136), false);

            hand1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.left_swing2),
                    convert(65), convert(122), false);

            rhand0 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.right_swing),
                    convert(88), convert(136), false);

            rhand1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.right_swing2),
                    convert(65), convert(122), false);
//            hand0 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.hand00),
//                    convert(80), convert(123), false);
//
//            hand1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.hand11),
//                    convert(70), convert(135), false);

            legs = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.legs_new),
                    convert(103), convert(32), false);

            head = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.head_new),
                    convert(151), convert(95), false);

//            header = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.h_ny_2022_new),
//                    convert(89), convert(101), false);

            glases = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.glases),
                    convert(66), convert(22), false);

//            Part.body.init(new Coordinate(0, 0), new Coordinate(convert(143), convert(0)), body,null);
//            Part.leftLeg.init(new Coordinate(convert(61), convert(1)), new Coordinate(convert(154), convert(368)), legs,Part.body);
//            Part.rightLeg.init(new Coordinate(convert(61), convert(1)), new Coordinate(convert(154), convert(368)), legs,Part.body);
//            Part.leftHand.init(new Coordinate(convert(29), convert(24)), new Coordinate(convert(207), convert(114)), hand,Part.body);
//            Part.rightHand.init(new Coordinate(convert(29), convert(24)), new Coordinate(convert(207), convert(114)), hand,Part.body);
//            Part.neck.init(new Coordinate(convert(6), convert(61)), new Coordinate(convert(168), convert(66)), bod_2,Part.body);
//            Part.head.init(new Coordinate(convert(102), convert(47)), new Coordinate(convert(101), 0), head,Part.body);
//            Part.dress.init(new Coordinate(convert(46), convert(24)), new Coordinate(convert(159), convert(17)), glases, Part.head);


            Part.body.init(new Coordinate(76, 342), new Coordinate(convert(143), convert(0)), body, null);
            Part.leftLeg.init(new Coordinate(convert(61), convert(1)), new Coordinate(convert(154), convert(368)), legs, Part.body);
            Part.rightLeg.init(new Coordinate(convert(61), convert(1)), new Coordinate(convert(154), convert(368)), legs, Part.body);
//            Part.leftHand.init(new Coordinate(convert(29), convert(24)), new Coordinate(convert(207), convert(114)), hand,null);
            Part.leftHand.init(new Coordinate(convert(29), convert(24)), new Coordinate(convert(201), convert(110)), hand0, Part.body);
            Part.leftHand2.init(new Coordinate(convert(23), convert(15)), new Coordinate(convert(231), convert(196)), hand1, Part.leftHand);
//            Part.leftHand2.init(new Coordinate(convert(26), convert(26)), new Coordinate(convert(226), convert(185)), hand1, Part.leftHand);
//            Part.leftHand2.init(new Coordinate(convert(26), convert(26)), new Coordinate(convert(231), convert(185)), hand1,Part.leftHand);
//            Part.rightHand.init(new Coordinate(convert(29), convert(24)), new Coordinate(convert(207), convert(114)), hand, Part.body);
            Part.rightHand.init(new Coordinate(convert(29), convert(24)), new Coordinate(convert(211), convert(110)), rhand0, Part.body);
            Part.rightHand2.init(new Coordinate(convert(23), convert(15)), new Coordinate(convert(241), convert(196)), rhand1, Part.rightHand);
            Part.neck.init(new Coordinate(convert(6), convert(61)), new Coordinate(convert(168), convert(66)), bod_2, Part.body);
            Part.head.init(new Coordinate(convert(102), convert(47)), new Coordinate(convert(101), 0), head, Part.body);
            Part.dress.init(new Coordinate(convert(46), convert(24)), new Coordinate(convert(159), convert(17)), glases, Part.head);

//            Part.body.init(new Coordinate(0, 0), new Coordinate(convert(143), convert(0)), body,null);
//            Part.leftLeg.init(new Coordinate(convert(61), convert(1)), new Coordinate(convert(154), convert(368)), legs,Part.body);
//            Part.rightLeg.init(new Coordinate(convert(61), convert(1)), new Coordinate(convert(154), convert(368)), legs,Part.body);
////            Part.leftHand.init(new Coordinate(convert(29), convert(24)), new Coordinate(convert(207), convert(114)), hand,null);
//            Part.leftHand.init(new Coordinate(convert(29), convert(24)), new Coordinate(convert(207), convert(114)), hand0,Part.body);
//            Part.leftHand2.init(new Coordinate(convert(26), convert(26)), new Coordinate(convert(226), convert(185)), hand1,Part.leftHand);
////            Part.leftHand2.init(new Coordinate(convert(26), convert(26)), new Coordinate(convert(231), convert(185)), hand1,Part.leftHand);
//            Part.rightHand.init(new Coordinate(convert(29), convert(24)), new Coordinate(convert(207), convert(114)), hand,Part.body);
//            Part.neck.init(new Coordinate(convert(6), convert(61)), new Coordinate(convert(168), convert(66)), bod_2,Part.body);
//            Part.head.init(new Coordinate(convert(102), convert(47)), new Coordinate(convert(101), 0), head,Part.body);
//            Part.dress.init(new Coordinate(convert(46), convert(24)), new Coordinate(convert(159), convert(17)), glases, Part.head);


//            Part.dress.init(new Coordinate(convert(27), convert(88)),new Coordinate(convert(176), convert(-41)),header);

            animations = new HashMap<>();
            animations.put("standing",
                    new Animation(
                            new HashMap<>(Map.of(
                                    Part.head, new PartAnimation(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0})
//                                    Part.dress, new PartAnimation(new double[]{5.0, 10.0, 15.0, 10.0, 5.0, 0.0})
                            )),

                            new HashMap<>(Map.of(6, Arrays.asList(
                                    new SwitchCondition(new HashMap<>(Map.of("status", GTF)), "standing", 5),
                                    new SwitchCondition(new HashMap<>(Map.of("status", FLU)), "jump", 0),
                                    new SwitchCondition(new HashMap<>(Map.of("status", RCV)), "recovery", 0),
                                    new SwitchCondition(new HashMap<>(Map.of("status", UPD, "training", 0)), "up_jmp", 0),
                                    new SwitchCondition(new HashMap<>(Map.of("status", UPD, "training", 1)), "up_bst", 0),
                                    new SwitchCondition(new HashMap<>(Map.of("status", UPD, "training", 2)), "up_en", 0),
                                    new SwitchCondition(new HashMap<>(Map.of("default", true)), "standing", 0))
                            ))
                    ));

//            animations.put("glasses",
//                    new Animation(
//                            new HashMap<>(Map.of(
//                                    Part.head, new PartAnimation(new double[]{0.0, -2.0,-5.0, -5.0, -5.0,-5.0, -5.0, -5.0,-5.0, -5.0, -5.0, -5.0, -5.0,  -2.0 -2.0, -0.0}),
//                                    Part.leftHand, new PartAnimation(new double[]{0.0, 0.0, 0.0, 0.0, 30.0, 60.0, 90.0, 90.0,110.0,110.0,110.0,110.0,90.0,60.0,30.0}),
//                                    Part.leftHand2, new PartAnimation(new double[]{0.0,30.0,60.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0,60.0,30.0,0.0}),
//                                    Part.dress, new PartAnimation(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,20.0,20.0,20.0,20.0,0.0,0.0,0.0})
//                            )),
//
//                            new HashMap<>(Map.of(15, Arrays.asList(
//                                    new SwitchCondition(new HashMap<>(Map.of("status", GTF)), "standing", 14),
//                                    new SwitchCondition(new HashMap<>(Map.of("status", FLU)), "jump", 0),
//                                    new SwitchCondition(new HashMap<>(Map.of("status", RCV)), "recovery", 0),
//                                    new SwitchCondition(new HashMap<>(Map.of("status", UPD, "training", 0)), "up_jmp", 0),
//                                    new SwitchCondition(new HashMap<>(Map.of("status", UPD, "training", 1)), "up_bst", 0),
//                                    new SwitchCondition(new HashMap<>(Map.of("status", UPD, "training", 2)), "up_en", 0),
//                                    new SwitchCondition(new HashMap<>(Map.of("default", true)), "standing", 0))
//                            ))
//                    ));

            animations.put("jump",
                    new Animation(
                            new HashMap<>(Map.of(
                                    Part.head, new PartAnimation(new double[]{5.0, 10.0, 15.0, 20.0, 25.0, 30.0, 35.0, 45.0}),
//                                    Part.dress, new PartAnimation(new double[]{5.0, 10.0, 15.0, 20.0, 25.0, 30.0, 35.0, 45.0}),
                                    Part.neck, new PartAnimation(new double[]{-1.25, -2.5, -3.25, -5.0, -6.25, -7.5, -8.75, -10.0}),
                                    Part.leftHand, new PartAnimation(new double[]{-5.0, -15.0, -25.0, -35.0, -45.0, -55.0, -65.0, -55.0}),
                                    Part.leftLeg, new PartAnimation(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -5.0, -10.0})
                            )),
                            new HashMap<>(Map.of(8, Arrays.asList(
                                    new SwitchCondition(new HashMap<>(Map.of("bustLargerZero", true)), "bust", 0),
                                    new SwitchCondition(new HashMap<>(Map.of("bustLargerZero", false)), "fly_up", 0))
                            ))
                    ));

            animations.put("bust",
                    new Animation(
                            new HashMap<>(Map.of(
                                    Part.head, new PartAnimation(new double[]{45.0, 45.0, 45.0, 45.0, 45.0, 45.0}),
                                    Part.neck, new PartAnimation(new double[]{-10.0, -10.0, -10.0, -10.0, -10.0, -10.0}),
                                    Part.leftHand, new PartAnimation(new double[]{-80.0, -120.0, -80.0, -45.0, -15.0, -35.0}),
                                    Part.leftHand2, new PartAnimation(new double[]{-40.0, -50.0, -40.0, -20.0, 20.0, 0.0}),
                                    Part.rightHand, new PartAnimation(new double[]{-93.0, -145.0, -93.0, -51.0, -15.0, -39.0}),
                                    Part.rightHand2, new PartAnimation(new double[]{-40.0, -50.0, -40.0, -20.0, 20.0, 0.0}),
                                    Part.leftLeg, new PartAnimation(new double[]{-10.0, -10.0, -10.0, -10.0, -10.0, -10.0})
                            )),
                            new HashMap<>(Map.of(5, Arrays.asList(
                                    new SwitchCondition(new HashMap<>(Map.of(
                                            "speedLargerZero", true,
                                            "energyLargerZero", true,
                                            "concentrationLargerZero", true
                                    )), "bust", 0),
                                    new SwitchCondition(new HashMap<>(Map.of(
                                            "speedLargerZero", true,
                                            "energyLargerZero", false
                                    )), "fly_up", 0),
                                    new SwitchCondition(new HashMap<>(Map.of(
                                            "speedLargerZero", true,
                                            "concentrationLargerZero", false
                                    )), "fly_up", 0),
                                    new SwitchCondition(new HashMap<>(Map.of("speedLargerZero", false)), "fly_down", 0))
                            ))
                    ));

            animations.put("fly_up",
                    new Animation(
                            new HashMap<>(Map.of(
                                    Part.head, new PartAnimation(new double[]{45.0}),
//                                    Part.dress, new PartAnimation(new double[]{45.0}),
                                    Part.neck, new PartAnimation(new double[]{-10.0}),
                                    Part.leftHand, new PartAnimation(new double[]{-15.0}),
                                    Part.leftLeg, new PartAnimation(new double[]{-10.0})
                            )),
                            new HashMap<>(Map.of(1, Arrays.asList(
                                    new SwitchCondition(new HashMap<>(Map.of("speedLargerZero", false)), "fly_down", 0),
                                    new SwitchCondition(new HashMap<>(Map.of("speedLargerZero", true)), "fly_up", 0))
                            ))
                    ));

            animations.put("fly_down",
                    new Animation(
                            new HashMap<>(Map.of(
                                    Part.head, new PartAnimation(new double[]{30.0, 15.0, 0.0, -15.0}),
//                                    Part.dress, new PartAnimation(new double[]{30.0, 15.0, 0.0, -15.0}),
                                    Part.neck, new PartAnimation(new double[]{-7.5, -3.25, 0.0, 3.25}),
                                    Part.leftHand, new PartAnimation(new double[]{-55.0, -75.0, -100.0, -120.0}),
                                    Part.rightHand, new PartAnimation(new double[]{-55.0, -80.0, -110.0, -145.0}),
                                    Part.leftLeg, new PartAnimation(new double[]{-5.0, 0.0, 0.0, 0.0})
                            )),
                            new HashMap<>(Map.of(3, Arrays.asList(
                                            new SwitchCondition(new HashMap<>(Map.of("coordLargerZero", true)), "fly_down", 2)),
                                    4, Arrays.asList(
                                            new SwitchCondition(new HashMap<>(Map.of("coordLargerZero", false)), "recovery", 0))
                            ))
                    ));

            animations.put("recovery",
                    new Animation(
                            new HashMap<>(Map.of(
                                    Part.head, new PartAnimation(new double[]{-18.0, -20.0, -22.0, -21.0, -20.0, -19.0, -18.0, -17.0, -16.0}),
//                                    Part.dress, new PartAnimation(new double[]{-18.0, -20.0, -22.0, -21.0, -20.0, -19.0, -18.0, -17.0, -16.0}),
                                    Part.neck, new PartAnimation(new double[]{4.75, 5.0, 5.25, 5.0, 4.75, 4.5, 4.0, 3.75, 3.5})
                            )),
                            new HashMap<>(Map.of(8, Arrays.asList(
                                    new SwitchCondition(new HashMap<>(Map.of("isRecoveryTime", true)), "recovery", 0),
                                    new SwitchCondition(new HashMap<>(Map.of("isRecoveryTime", false)), "standing", 0))
                            ))
                    ));

            animations.put("up_en",
                    new Animation(
                            new HashMap<>(Map.of(
                                    Part.leftHand, new PartAnimation(new double[]{-10.0, -20.0, -30.0, -40.0, -40.0, -30.0, -20.0, -10.0}),
                                    Part.rightHand, new PartAnimation(new double[]{-40.0, -30.0, -20.0, -10.0, -10.0, -20.0, -30.0, -40.0}),
                                    Part.leftLeg, new PartAnimation(new double[]{5.0, 10.0, 5.0, 0.0, -5.0, -10.0, -5.0, 0.0}),
                                    Part.rightLeg, new PartAnimation(new double[]{-5.0, -10.0, -5.0, 0.0, 5.0, 10.0, 5.0, 0.0})
                            )),
                            new HashMap<>(Map.of(8, Arrays.asList(
                                    new SwitchCondition(new HashMap<>(Map.of("status", RCV)), "recovery", 0),
                                    new SwitchCondition(new HashMap<>(Map.of("status", RTF)), "standing", 0),
                                    new SwitchCondition(new HashMap<>(Map.of("default", true)), "up_en", 0))
                            ))
                    ));

            animations.put("up_jmp",
                    new Animation(
                            new HashMap<>(Map.of(
                                    Part.head, new PartAnimation(new double[]{2.5, 2.5, 2.5, 2.5, 2.5}),
//                                    Part.dress, new PartAnimation(new double[]{2.5, 2.5, 2.5, 2.5, 2.5}),
                                    Part.leftHand, new PartAnimation(new double[]{-5.0, -10.0, -30.0, -10.0, -5.0}),
                                    Part.leftLeg, new PartAnimation(new double[]{-5.0, -10.0, -20.0, -10.0, -5.0})
                            )),
                            new HashMap<>(Map.of(5, Arrays.asList(
                                    new SwitchCondition(new HashMap<>(Map.of("status", RCV)), "recovery", 0),
                                    new SwitchCondition(new HashMap<>(Map.of("status", RTF)), "standing", 0),
                                    new SwitchCondition(new HashMap<>(Map.of("default", true)), "up_jmp", 0))
                            ))
                    ));

            animations.put("up_bst",
                    new Animation(
                            Coordinate.arrayOf(0,-dw/32,0,-dw/32,0,-dw/32,0,-dw/32,0,-dw/32,0,-dw/32,0,-dw/32 ),
                            new HashMap<>(Map.of(

                                    Part.head, new PartAnimation(new double[]{25.0,45.0, 45.0, 45.0, 45.0, 45.0, 45.0}),
                                    Part.neck, new PartAnimation(new double[]{-5.0,-10.0, -10.0, -10.0, -10.0, -10.0, -10.0}),
                                    Part.leftHand, new PartAnimation(new double[]{-20.0, -60.0, -100.0, -60.0, -25.0, 0.0, -15.0}),
                                    Part.leftHand2, new PartAnimation(new double[]{-20.0,-40.0, -50.0, -40.0, -20.0, 20.0, 0.0}),
                                    Part.rightHand, new PartAnimation(new double[]{-30.0, -70.0, -110.0, -70.0, -35.0, -10.0, -25.0}),
                                    Part.rightHand2, new PartAnimation(new double[]{-20.0, -40.0, -50.0, -40.0, -20.0, 20.0, 0.0}),

//                                    Part.head, new PartAnimation(new double[]{10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0}),
//                                    Part.leftHand, new PartAnimation(new double[]{-30.0, -60.0, -90.0, -120.0, -90.0, -60.0, -30.0}),
                                    Part.leftLeg, new PartAnimation(new double[]{-30.0, -30.0, -30.0, -30.0, -30.0, -30.0, -30.0}),
                                    Part.rightLeg, new PartAnimation(new double[]{-30.0, -30.0, -30.0, -30.0, -30.0, -30.0, -30.0})
                            )),
                            new HashMap<>(Map.of(7, Arrays.asList(
                                    new SwitchCondition(new HashMap<>(Map.of("status", RCV)), "recovery", 0),
                                    new SwitchCondition(new HashMap<>(Map.of("status", RTF)), "standing", 0),
                                    new SwitchCondition(new HashMap<>(Map.of("default", true)), "up_bst", 2))
                            ))
                    ));

//            animations.put("up_bst",
//                    new Animation(
//                            Coordinate.arrayOf(dw/4,-dw/8,dw/4,-dw/8,dw/4,-dw/8,dw/4,-dw/8,dw/4,-dw/8,dw/4,-dw/8,dw/4,-dw/8,dw/4,-dw/8,dw/4,-dw/8,dw/4,-dw/8,dw/4,-dw/8 ),
//                            new HashMap<>(Map.of(
//                                    Part.head, new PartAnimation(new double[]{50.0,45.0, 40.0, 35.0, 30.0, 30.0, 30.0, 35.0, 40.0,45.0, 50.0}),
////                                    Part.body, new PartAnimation(new double[]{0.0, -20.0, -40.0, -60.0, -80.0, -60.0, -40.0})
//                                    Part.body, new PartAnimation(new double[]{-80.0,-77.0, -75.0, -73.0, -70.0, -70.0, -70.0, -73.0, -75.0,-77.0, -80.0}),
//                                    Part.leftHand, new PartAnimation(new double[]{45.0,55.0,72.5, 80.0, 90.0, 90.0, 90.0, 80.0, 72.5, 55.0, 45.0}),
//                                    Part.leftHand2, new PartAnimation(new double[]{90.0,70.0,45.0, 20.0, 0.0, 0.0, 0.0, 20.0,45.0,70.0, 90.0}),
//                                    Part.rightHand, new PartAnimation(new double[]{55.0,65.0,82.5, 90.0, 100.0, 100.0, 100.0, 90.0, 82.5, 65.0, 55.0}),
//                                    Part.rightHand2, new PartAnimation(new double[]{90.0,70.0,45.0, 20.0, 0.0, 0.0, 0.0, 20.0,45.0,70.0, 90.0})
//                            )),
//                            new HashMap<>(Map.of(10, Arrays.asList(
//                                    new SwitchCondition(new HashMap<>(Map.of("status", RCV)), "recovery", 0),
//                                    new SwitchCondition(new HashMap<>(Map.of("status", RTF)), "standing", 0),
//                                    new SwitchCondition(new HashMap<>(Map.of("default", true)), "up_bst", 0))
//                            ))
//                    ));

        }

        enum Part {
            rightLeg,
            rightHand,
            rightHand2,
            neck,
            head,
            body,
            dress,
            leftLeg,
            leftHand,
            leftHand2;
            Coordinate center;
            Coordinate shift;
            Matrix shiftMatrix;
            Bitmap bitmap;
            double lastGrad;
            Part tPart;

            public void init(Coordinate center, Coordinate shift, Bitmap bitmap, Part tPart) {
                this.bitmap = bitmap;
                this.center = center;
                this.tPart = tPart;

                this.shift = tPart == null ? shift : shift.sub(tPart.getFullShift());
                shiftMatrix = new Matrix();
                if (tPart == null) shiftMatrix.preTranslate((float) shift.x, (float) shift.y);
            }

            public Coordinate getFullShift() {
                return tPart == null ? shift : shift.add(tPart.getFullShift());
            }

            public Matrix getShiftMatrix() {
                if (tPart != null) {
                    Coordinate finCoord = tPart.getCoordByShifting(shift);
                    shiftMatrix = new Matrix();
                    shiftMatrix.preTranslate((float) finCoord.x, (float) finCoord.y);
                    shiftMatrix.preRotate((float) tPart.getFullGrad());
                }
                return shiftMatrix;
            }

            public double getFullGrad() {
                return tPart == null ? lastGrad : lastGrad+(tPart.getFullGrad());
            }

            public Coordinate getCoordByShifting(Coordinate coord) {
                double cos = Math.cos(Math.toRadians(lastGrad));
                double sin = Math.sin(Math.toRadians(lastGrad));
                Coordinate subDot = coord.sub(center);
                Coordinate rotatedCoordinate = new Coordinate(center.getX() + (subDot.getX()) * cos - (subDot.getY()) * sin, center.getY() + (subDot.getX()) * sin + (subDot.getY()) * cos);
                return  tPart!=null?tPart.getCoordByShifting(rotatedCoordinate.add(shift)):rotatedCoordinate.add(shift);

                //return  rotatedCoordinate.add(tPart!=null?tPart.getCoordByShifting(shift):shift);
           //todo должен учитываться сдвиг и поворот всех частей-родителей     // return tPart == null ? rotatedCoordinate.add(shift) : rotatedCoordinate.add(tPart.getCoordByShifting(shift));
            }



//            public Matrix getShiftMatrix() {
//                if (tPart != null) {
//                    Coordinate finCoord = tPart.getCoordByShifting(shift);
//                    shiftMatrix = new Matrix();
//                    shiftMatrix.preTranslate((float) finCoord.x, (float) finCoord.y);
//                    shiftMatrix.preRotate((float) tPart.lastGrad);
//                }
//                return shiftMatrix;
//            }
//
//
//            public Coordinate getCoordByShifting(Coordinate coord) {
//                double cos = Math.cos(Math.toRadians(lastGrad));
//                double sin = Math.sin(Math.toRadians(lastGrad));
//                Coordinate subDot = coord.sub(center);
//                Coordinate rotatedCoordinate = new Coordinate(center.getX() + (subDot.getX()) * cos - (subDot.getY()) * sin, center.getY() + (subDot.getX()) * sin + (subDot.getY()) * cos);
//                return tPart == null ? rotatedCoordinate.add(shift) : rotatedCoordinate.add(tPart.getCoordByShifting(shift));
//            }

        }

        private class Animation {
            public Animation(HashMap<Part, PartAnimation> partsAnimations, HashMap<Integer, List<SwitchCondition>> conditions) {
                this.partsAnimations = partsAnimations;
                this.conditions = conditions;
            }

            public Animation(Coordinate[] preshift, HashMap<Part, PartAnimation> partsAnimations, HashMap<Integer, List<SwitchCondition>> conditions) {
                this.preshift=preshift;
                this.partsAnimations = partsAnimations;
                this.conditions = conditions;
            }
Coordinate[] preshift;

            HashMap<Part, PartAnimation> partsAnimations;
            HashMap<Integer, List<SwitchCondition>> conditions;
        }

        private class PartAnimation {
            int steps;

            public PartAnimation(double[] angles) {
                this.angles = angles;
                steps = angles.length;
            }

            double[] angles;
        }

        private class SwitchCondition {
            public SwitchCondition(HashMap<String, Object> conditions, String targetAnimation, int targetStep) {
                this.targetAnimation = targetAnimation;
                this.targetStep = targetStep;
                this.conditions = conditions;
            }

            HashMap<String, Object> conditions;
            String targetAnimation;
            int targetStep;
        }
    }

    void draw(Paint paint, Canvas canvas, GameView.Room room, Coordinate camera) { // рисуем картинку

        float draw_y = (float) (y + camera.getY());

        if (status != GTF && status != FLU && status != RCV && status != FLD && status != UPD) {
            if (shiftX != box) {
                shiftX = Math.min(shiftX + box / 8, box);
            }
        } else {
            if (shiftX != 0) {
                shiftX = Math.max(shiftX - box / 8, 0);
            }
        }

        listener.onDrawPositionChanged(shiftX, draw_y);

        //canvas.drawBitmap(bitmap, x, draw_y, paint);

        //paint.setColor(Color.BLACK);

        float strtY = MainActivity.end - boxAndSide;


        paint.setColor(Color.BLACK);

        paint.setTextSize((float) (dw / 30.0));


        animator.draw(paint, canvas, x + shiftX, draw_y);

        if (MainActivity.ask_on) {
            d = new Date();
        /*if (MainActivity.ask_number!=-1 &&  MainActivity.ask_number!=7){
            if (MainActivity.ask_status[MainActivity.ask_number]==status ){//если подсказку нужно отобразить*/
            if (status == GTF) {
                //if (start_date.getTime() != MainActivity.first_date.getTime()) {
                start_date = new Date();
                // }
            }
            if (status == RCV) {
                // if (break_date.getTime() != MainActivity.first_date.getTime()) {
                break_date = new Date();
                //}
            }
            if (status == UPD) {
                savedate = new Date();
            }
        } else {
            animator.updateBooleans();
            animator.switchStep();
        }

        if (room != GameView.Room.Training) {
            paint.setColor(Color.BLACK);
            paint.setTextSize((float) (dw / 15.0));

            if ((status == FLU || status == FLD) && pen_coord > 0) {
                if (pen_coord < 1)
                    canvas.drawText(roundd(pen_coord * 100, 2) + con.getString(R.string.santimeter), (float) dw / 2 - (float) dh / 20, draw_y - (float) dw / 15, paint);
                else if (pen_coord < 1000)
                    canvas.drawText(roundd(pen_coord, 2) + con.getString(R.string.meter), (float) dw / 2 - (float) dh / 20, draw_y - (float) dw / 15, paint);
                else if (pen_coord < 1000000)
                    canvas.drawText(roundd(pen_coord / 1000, 2) + con.getString(R.string.kilometer), (float) dw / 2 - (float) dh / 20, draw_y - (float) dw / 15, paint);
            } else if (status == GTF) {
                if (start_date.getTime() != MainActivity.first_date.getTime()) {
                    canvas.drawText(con.getString(R.string.start) + ": " + (5 - (new Date().getTime() - start_date.getTime()) / 1000) + con.getString(R.string.sec), (float) dw / 2 - (float) (4 * dw) / 15, draw_y - (float) dw / 15, paint);
                }
            } else if (status == RTF) {
                boolean can_update = false;
                if (next_jump < ml_jump)
                    if (jump_record[next_jump] <= maxY)
                        can_update = true;
                if (next_bust < ml_bust)
                    if (bust_record[next_bust] <= maxY)
                        can_update = true;
                if (MainActivity.energy_show && next_energy < ml_energy)
                    if (energy_record[next_energy] <= maxY)
                        can_update = true;
                if (can_update) {
//                    if (GameView.tick < 45)
//                        paint.setAlpha(GameView.tick * 5);
//                    else
//                        paint.setAlpha((90 - GameView.tick) * 5);
//
//                    //canvas.drawBitmap(fone_green, MainActivity.Setup.getX() - (float) dw / 100,  MainActivity.end  - dw / 4f - dw / 100f - (float) dw / 100, paint);
//                    paint.setAlpha(255);
                }
            }
        } else {
            draw_setup(paint, canvas, draw_y);
        }
    }

    private void draw_setup(Paint paint, Canvas canvas, float draw_y) {
        paint.setTextSize((float) dw / 28);//dh/50);
        float shiftX = (float) dh / 180;

        canvas.drawText(con.getString(R.string.jump_power), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + (float) dw / 25, paint);
        canvas.drawText(/*(int) roundd(jump * 100, 0) + " (" + */String.valueOf(next_jump) + " lvl", MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + (float) (dw * 2) / 25, paint);

        canvas.drawText(con.getString(R.string.boost_power), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + (float) dw / 25, paint);
        canvas.drawText(/*(int) roundd(bust * 100, 0) + " (" + */String.valueOf(next_bust) + " lvl", MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + (float) (dw * 2) / 25, paint);
        if (MainActivity.energy_show) {
            canvas.drawText(con.getString(R.string.energy), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + (float) dw / 25, paint);
            canvas.drawText(/*(int) roundd(maxenergy, 0) + " (" +*/ String.valueOf(next_energy) + " lvl", MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + (float) (dw * 2) / 25, paint);
        }

        if (MainActivity.update != -1 && to_update != -1) {
            //float strtY = MainActivity.end - dw / 4f - dw / 50f;//MainActivity.Setup.getY();

            canvas.drawRect(boxAndSide - dw / 200, draw_y - dw / 8 - dw / 200, 3 * box - (3 * side) / 4, draw_y - dw / 16 + dw / 200, paint);

            paint.setColor(Color.GREEN);
            canvas.drawRect(boxAndSide, draw_y - dw / 8, (float) dw / 4 + (float) dw / 50 + ((float) dw / 2 - (float) dw / 25) * (update_time - to_update + (float) (d.getTime() - savedate.getTime()) / 1000) / update_time, draw_y - dw / 16, paint);

            paint.setColor(Color.YELLOW);
            canvas.drawRect((float) dw / 4 + (float) dw / 50 + ((float) dw / 2 - (float) dw / 25) * (update_time - to_update + (float) (d.getTime() - savedate.getTime()) / 1000) / update_time, draw_y - dw / 8, (float) dw / 4 + (float) dw / 50 + ((float) dw / 2 - (float) dw / 25) * (update_time - to_update + short_update + (float) (d.getTime() - savedate.getTime()) / 1000) / update_time, draw_y - dw / 16, paint);

            paint.setColor(Color.BLACK);
        }

        if (MainActivity.update == 0 && to_update != -1) {
            canvas.drawBitmap(fone_white, MainActivity.Up_jump.getX() - (float) dw / 100, MainActivity.Up_jump.getY() - (float) dw / 100, paint);
            canvas.drawText(con.getString(R.string.updating), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + (float) (dw * 3) / 25, paint);
            canvas.drawText(con.getString(R.string.progress), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + (float) (dw * 4) / 25, paint);
            canvas.drawText(roundd(to_update - (float) (d.getTime() - savedate.getTime()) / 1000, 2) + con.getString(R.string.sec), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + (float) (dw * 5) / 25, paint);
            canvas.drawText(con.getString(R.string.left), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + (float) (dw * 6) / 25, paint);
            paint.setColor(Color.BLACK);
        } else {
            if (next_jump < ml_jump) {
                if (jump_record[next_jump] <= maxY) {
                    if (GameView.tick < 45)
                        paint.setAlpha(GameView.tick * 5);
                    else
                        paint.setAlpha((90 - GameView.tick) * 5);
                    canvas.drawBitmap(fone_green, MainActivity.Up_jump.getX() - (float) dw / 100, MainActivity.Up_jump.getY() - (float) dw / 100, paint);
                    paint.setAlpha(255);

                    canvas.drawText(con.getString(R.string.updating), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + (float) (dw * 3) / 25, paint);
                    canvas.drawText(con.getString(R.string.available), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + (float) (dw * 4) / 25, paint);
                    canvas.drawText(con.getString(R.string.take), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + (float) (dw * 5) / 25, paint);
                    canvas.drawText(String.valueOf(time_to_up[next_jump]) + con.getString(R.string.sec), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + (float) (dw * 6) / 25, paint);
                } else {
                    canvas.drawBitmap(fone_red, MainActivity.Up_jump.getX() - (float) dw / 100, MainActivity.Up_jump.getY() - (float) dw / 100, paint);
                    canvas.drawText(con.getString(R.string.updating), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + (float) (dw * 3) / 25, paint);
                    canvas.drawText(con.getString(R.string.not_available), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + (float) (dw * 4) / 25, paint);
                    canvas.drawText(con.getString(R.string.record), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + (float) (dw * 5) / 25, paint);

                    if (jump_record[next_jump] < 1)
                        canvas.drawText(roundd(jump_record[next_jump] * 100, 2) + con.getString(R.string.santimeter), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + (float) (dw * 6) / 25, paint);
                    else if (jump_record[next_jump] < 1000)
                        canvas.drawText(roundd(jump_record[next_jump], 2) + con.getString(R.string.meter), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + (float) (dw * 6) / 25, paint);
                    else if (jump_record[next_jump] < 1000000)
                        canvas.drawText(roundd(jump_record[next_jump] / 1000, 2) + con.getString(R.string.kilometer), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + (float) (dw * 6) / 25, paint);
                }
                paint.setColor(Color.BLACK);
            }
        }
        if (MainActivity.update == 1 && to_update != -1) {
            canvas.drawBitmap(fone_white, MainActivity.Up_fly.getX() - (float) dw / 100, MainActivity.Up_fly.getY() - (float) dw / 100, paint);
            canvas.drawText(con.getString(R.string.updating), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + (float) (dw * 3) / 25, paint);
            canvas.drawText(con.getString(R.string.progress), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + (float) (dw * 4) / 25, paint);
            canvas.drawText(roundd(to_update - (float) (d.getTime() - savedate.getTime()) / 1000, 2) + con.getString(R.string.sec), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + (float) (dw * 5) / 25, paint);
            canvas.drawText(con.getString(R.string.left), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + (float) (dw * 6) / 25, paint);
            paint.setColor(Color.BLACK);
        } else {
            if (next_bust < ml_bust) {
                if (bust_record[next_bust] <= maxY) {
                    if (GameView.tick < 45)
                        paint.setAlpha(GameView.tick * 5);
                    else
                        paint.setAlpha((90 - GameView.tick) * 5);
                    canvas.drawBitmap(fone_green, MainActivity.Up_fly.getX() - (float) dw / 100, MainActivity.Up_fly.getY() - (float) dw / 100, paint);
                    paint.setAlpha(255);
                    canvas.drawText(con.getString(R.string.updating), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + (float) (dw * 3) / 25, paint);
                    canvas.drawText(con.getString(R.string.available), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + (float) (dw * 4) / 25, paint);
                    canvas.drawText(con.getString(R.string.take), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + (float) (dw * 5) / 25, paint);
                    canvas.drawText(String.valueOf(time_to_up[next_bust + 5]) + con.getString(R.string.sec), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + (float) (dw * 6) / 25, paint);
                } else {
                    canvas.drawBitmap(fone_red, MainActivity.Up_fly.getX() - (float) dw / 100, MainActivity.Up_fly.getY() - (float) dw / 100, paint);
                    canvas.drawText(con.getString(R.string.updating), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + (float) (dw * 3) / 25, paint);
                    canvas.drawText(con.getString(R.string.not_available), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + (float) (dw * 4) / 25, paint);
                    canvas.drawText(con.getString(R.string.record), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + (float) (dw * 5) / 25, paint);

                    if (bust_record[next_bust] < 1)
                        canvas.drawText(roundd(bust_record[next_bust] * 100, 2) + con.getString(R.string.santimeter), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + (float) (dw * 6) / 25, paint);
                    else if (bust_record[next_bust] < 1000)
                        canvas.drawText(roundd(bust_record[next_bust], 2) + con.getString(R.string.meter), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + (float) (dw * 6) / 25, paint);
                    else if (bust_record[next_bust] < 1000000)
                        canvas.drawText(roundd(bust_record[next_bust] / 1000, 2) + con.getString(R.string.kilometer), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + (float) (dw * 6) / 25, paint);
                }
                paint.setColor(Color.BLACK);
            }
        }

        if (MainActivity.update == 2 && to_update != -1) {
            canvas.drawBitmap(fone_white, MainActivity.Up_energy.getX() - (float) dw / 100, MainActivity.Up_energy.getY() - (float) dw / 100, paint);
            canvas.drawText(con.getString(R.string.updating), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + (float) (dw * 3) / 25, paint);
            canvas.drawText(con.getString(R.string.progress), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + (float) (dw * 4) / 25, paint);
            canvas.drawText(roundd(to_update - (float) (d.getTime() - savedate.getTime()) / 1000, 2) + con.getString(R.string.sec), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + (float) (dw * 5) / 25, paint);
            canvas.drawText(con.getString(R.string.left), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + (float) (dw * 6) / 25, paint);
            paint.setColor(Color.BLACK);
        } else {
            if (MainActivity.energy_show) {
                if (next_energy < ml_energy) {
                    if (energy_record[next_energy] <= maxY) {
                        if (GameView.tick < 45)
                            paint.setAlpha(GameView.tick * 5);
                        else
                            paint.setAlpha((90 - GameView.tick) * 5);
                        canvas.drawBitmap(fone_green, MainActivity.Up_energy.getX() - (float) dw / 100, MainActivity.Up_energy.getY() - (float) dw / 100, paint);
                        paint.setAlpha(255);

                        canvas.drawText(con.getString(R.string.updating), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + (float) (dw * 3) / 25, paint);
                        canvas.drawText(con.getString(R.string.available), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + (float) (dw * 4) / 25, paint);
                        canvas.drawText(con.getString(R.string.take), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + (float) (dw * 5) / 25, paint);
                        canvas.drawText(String.valueOf(time_to_up[next_energy + 10]) + con.getString(R.string.sec), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + (float) (dw * 6) / 25, paint);
                    } else {
                        canvas.drawBitmap(fone_red, MainActivity.Up_energy.getX() - (float) dw / 100, MainActivity.Up_energy.getY() - (float) dw / 100, paint);
                        canvas.drawText(con.getString(R.string.updating), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + (float) (dw * 3) / 25, paint);
                        canvas.drawText(con.getString(R.string.not_available), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + (float) (dw * 4) / 25, paint);
                        canvas.drawText(con.getString(R.string.record), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + (float) (dw * 5) / 25, paint);

                        if (energy_record[next_energy] < 1)
                            canvas.drawText(roundd(energy_record[next_energy] * 100, 2) + con.getString(R.string.santimeter), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + (float) (dw * 6) / 25, paint);
                        else if (energy_record[next_energy] < 1000)
                            canvas.drawText(roundd(energy_record[next_energy], 2) + con.getString(R.string.meter), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + (float) (dw * 6) / 25, paint);
                        else if (energy_record[next_energy] < 1000000)
                            canvas.drawText(roundd(energy_record[next_energy] / 1000, 2) + con.getString(R.string.kilometer), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + (float) (dw * 6) / 25, paint);
                    }
                    paint.setColor(Color.BLACK);

                }

            }
        }
        paint.setColor(Color.BLACK);
    }

    public float roundd(float a, int b) {
        // return (float) (((float)Math.round(a * Math.pow(10,b))) / Math.pow(10,b));
        //return (float) ((a * Math.pow(10,b)) / Math.pow(10,b));
        //int e=(int)(a * Math.pow(10,b));
        //return (float) (e / Math.pow(10,b));
        return (float) (((float) Math.round(a * 10 * Math.pow(10, b))) / (10 * Math.pow(10, b)));
    }

    public float roundd(double a, int b) {
        //return (float) (((float)Math.round(a * Math.pow(10,b))) / Math.pow(10,b));
        //return (float) ((a * Math.pow(10,b)) / Math.pow(10,b));
        //int e=(int)(a * Math.pow(10,b));
        //return (float) (e / Math.pow(10,b));
        return (float) (((float) Math.round(a * 10 * Math.pow(10, b))) / (10 * Math.pow(10, b)));
    }

    public void save() {
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(con);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putString("version", BuildConfig.VERSION_NAME);
        myEditor.putInt("jump", next_jump);
        myEditor.putInt("energy", next_energy);
        myEditor.putInt("bust", next_bust);
        myEditor.putFloat("record", maxY);
        myEditor.putLong("strt_date", savedate.getTime());
        myEditor.putInt("update", MainActivity.update);
        myEditor.putFloat("time_to_up", to_update);
        myEditor.putBoolean("Config.quick_down", MainActivity.quick_down);
        myEditor.putInt("ask_number", MainActivity.tutorialNumber);
        myEditor.apply();
    }

    public void save_param() {
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(con);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putInt("jump", next_jump);
        myEditor.putInt("energy", next_energy);
        myEditor.putInt("bust", next_bust);
        myEditor.apply();
    }

    public void save_upd() {
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(con);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putLong("strt_date", savedate.getTime());
        myEditor.putInt("update", MainActivity.update);
        myEditor.putFloat("time_to_up", to_update);
        myEditor.apply();
    }

    public void save_cfg() {
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(con);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putString("version", BuildConfig.VERSION_NAME);
        myEditor.putBoolean("Config.quick_down", MainActivity.quick_down);
        myEditor.apply();
    }

    public void save_ask() {
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(con);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putInt("ask_number", MainActivity.tutorialNumber);
        myEditor.apply();
    }


}
