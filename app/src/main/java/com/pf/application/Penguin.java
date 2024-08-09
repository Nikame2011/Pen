package com.pf.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.view.View;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Penguin {
    protected float x;// координаты
    protected float sx;// координаты нуля
    protected float y;// координаты
    protected float sy;// координаты нуля
    protected float pen_coord = 0;//игровые координаты
    protected float saveY;//местный рекорд во время полёта
    protected float speed; // скорость
    protected byte energy; //текущая энергия
    protected byte max_strong = 90; //ограничение концентрации
    public int dw, dh; //восота и ширина экрана
    protected byte anim_step = 0; //счётчик анимационных движений
    int exp = 0; //счётчик для анимации 2
    protected float downspeed = (float) -0.5; //ограничитель скорости падения
    protected float grav = (float) 0.015;
    //private String header_type="ny_2022";

    private Bitmap fone_red; // картинка
    private Bitmap fone_green; // картинка
    private Bitmap fone_white; // картинка
    private Bitmap bod_2;
    private Bitmap body;
    private Bitmap head;
    private Bitmap header;
    private Bitmap hand;
    private Bitmap legs;

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


    public String status = "RTF";
    public String savedstatus = "NON";
    /*
    NON - отсутствует (есть только у сохраняемого статуса)
    STF - полёт заблокирован открытым меню
    UPD - происходит улучшение
    RCV - восстанавливается энергия
    RTF - ready to fly когда стоит и готов лететь
    GTF - подготовка к полёту (обратный отсчёт)
    FLU - полёт вверх
    FLD - падение
    */

    protected String anima_type = "standing";
    /*
    standing
    jump
    bust
    fly_up
    fly_down
    recovery
     */

    Map<String, Double[]> bod2_grad = new HashMap<String, Double[]>();
    Map<String, Double[]> head_grad = new HashMap<String, Double[]>();
    Map<String, Double[]> hand_grad = new HashMap<String, Double[]>();
    Map<String, Double[]> legs_grad = new HashMap<String, Double[]>();
    Map<String, Double[]> hand2_grad = new HashMap<String, Double[]>();
    Map<String, Double[]> legs2_grad = new HashMap<String, Double[]>();


    public Penguin(Context context, byte n_j, byte n_b, byte n_e, float rec, Date time, float tu) {

        dw = MainActivity.dw;
        dh = MainActivity.dh;
        con = context;

        x = dw / 2 - dw / 4;
        sx = x;

        y = MainActivity.end - dw / 4 - dw / 25 - dw / 2;
        sy = y;

        maxY = 0;
        saveY = 0;

        speed = 0;
        strong = 0;

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


        savedate = time;
        to_update = tu;
        if (MainActivity.update != -1) {
            status = "STF";
            savedstatus = "UPD";
            switch (MainActivity.update) {
                case 0:
                    update_time = time_to_up[next_jump];
                    break;
                case 1:
                    update_time = time_to_up[next_bust + 5];
                    break;
                case 2:
                    update_time = time_to_up[next_energy + 10];
                    break;
            }
        }


        bust = bust_up[next_bust];
        jump = jump_up[next_jump];
        maxenergy = energy_up[next_energy];
        energy = maxenergy;

        init(context); // инициализация ресурсов

    }

    public void new_game() {

        dw = MainActivity.dw;
        dh = MainActivity.dh;
        x = dw / 2 - dw / 4;
        y = MainActivity.end - dw / 4 - dw / 25 - dw / 2;
        maxY = 0;
        saveY = 0;
        sy = y;
        speed = 0;
        strong = 0;
        next_jump = 1;
        next_bust = 0;
        next_energy = 1;
        maxY = 0;
        savedate = MainActivity.first_date;
        to_update = -1;
        bust = bust_up[next_bust];
        jump = jump_up[next_jump];
        maxenergy = energy_up[next_energy];
        energy = maxenergy;
        start_date = MainActivity.first_date;
        //status=new String();
        status = "RTF";
        anima_type = "standing";
        anim_step = 0;
    }


    void init(Context context) { // сжимаем картинку до нужных размеров
        start_date = MainActivity.first_date;

        head_grad.put("standing", new Double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0});
        head_grad.put("jump", new Double[]{5.0, 10.0, 15.0, 20.0, 25.0, 30.0, 35.0, 45.0});
        head_grad.put("bust", new Double[]{45.0, 45.0, 45.0, 45.0, 45.0, 45.0});
        head_grad.put("fly_up", new Double[]{45.0});
        head_grad.put("fly_down", new Double[]{30.0, 15.0, 0.0, -15.0});
        head_grad.put("recovery", new Double[]{-18.0, -20.0, -22.0, -21.0, -20.0, -19.0, -18.0, -17.0, -16.0});

        bod2_grad.put("jump", new Double[]{-1.25, -2.5, -3.25, -5.0, -6.25, -7.5, -8.75, -10.0});
        bod2_grad.put("bust", new Double[]{-10.0, -10.0, -10.0, -10.0, -10.0, -10.0});
        bod2_grad.put("fly_up", new Double[]{-10.0});
        bod2_grad.put("fly_down", new Double[]{-7.5, -3.25, 0.0, 3.25});
        bod2_grad.put("recovery", new Double[]{4.75, 5.0, 5.25, 5.0, 4.75, 4.5, 4.0, 3.75, 3.5});

        //hand_grad.put("jump",new Double[] {-10.0,-20.0,-30.0,-40.0,-50.0,-60.0,-70.0,-60.0});
        hand_grad.put("jump", new Double[]{-5.0, -10.0, -15.0, -20.0, -25.0, -30.0, -35.0, -40.0, -45.0, -50.0, -55.0, -60.0, -65.0, -65.0, -55.0, -45.0});
        hand_grad.put("fly_up", new Double[]{-15.0,});
        hand_grad.put("fly_down", new Double[]{-55.0, -60.0, -65.0, -70.0});
        hand_grad.put("bust", new Double[]{-35.0, -55.0, -80.0, -100.0, -120.0, -100.0, -80.0, -60.0, -45.0, -30.0, -15.0, -15.0});

        legs_grad.put("jump", new Double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -5.0, -10.0});
        legs_grad.put("fly_up", new Double[]{-10.0});
        legs_grad.put("fly_down", new Double[]{-5.0, 0.0, 0.0, 0.0});
        legs_grad.put("bust", new Double[]{-10.0, -10.0, -10.0, -10.0, -10.0, -10.0});

        hand_grad.put("up_en", new Double[]{-10.0, -20.0, -30.0, -40.0, -40.0, -30.0, -20.0, -10.0});
        legs_grad.put("up_en", new Double[]{5.0, 10.0, 5.0, 0.0, -5.0, -10.0, -5.0, 0.0});
        hand2_grad.put("up_en", new Double[]{-40.0, -30.0, -20.0, -10.0, -10.0, -20.0, -30.0, -40.0});
        legs2_grad.put("up_en", new Double[]{-5.0, -10.0, -5.0, 0.0, 5.0, 10.0, 5.0, 0.0});

        head_grad.put("up_jmp", new Double[]{2.5, 2.5, 2.5, 2.5, 2.5});
        hand_grad.put("up_jmp", new Double[]{-5.0, -10.0, -30.0, -10.0, -5.0});
        legs_grad.put("up_jmp", new Double[]{-5.0, -10.0, -20.0, -10.0, -5.0});

        head_grad.put("up_bst", new Double[]{10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0});
        hand_grad.put("up_bst", new Double[]{-30.0, -60.0, -90.0, -120.0, -90.0, -60.0, -30.0});
        //legs_grad.put("up_bst",new Double[] {0.0,-5.0,-10.0,-5.0,0.0});


        // id картинки
        int bitmapId = R.drawable.c_body;//f0;// определяем начальные параметры
        Bitmap cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        body = Bitmap.createScaledBitmap(
                cBitmap, (int) (dw / 2), (int) (dw / 2), false);
        cBitmap.recycle();

        bitmapId = R.drawable.c_bod2;//f0;// определяем начальные параметры
        cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        bod_2 = Bitmap.createScaledBitmap(
                cBitmap, (int) (dw / 2), (int) (dw / 2), false);
        cBitmap.recycle();

        bitmapId = R.drawable.hand;//f0;// определяем начальные параметры
        cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        hand = Bitmap.createScaledBitmap(
                cBitmap, (int) (dw / 2), (int) (dw / 2), false);
        cBitmap.recycle();

        bitmapId = R.drawable.legs;//f0;// определяем начальные параметры
        cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        legs = Bitmap.createScaledBitmap(
                cBitmap, (int) (dw / 2), (int) (dw / 2), false);
        cBitmap.recycle();

        bitmapId = R.drawable.c_head;//f0;// определяем начальные параметры
        cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        head = Bitmap.createScaledBitmap(
                cBitmap, (int) (dw / 2), (int) (dw / 2), false);
        cBitmap.recycle();

        bitmapId = R.drawable.h_ny_2022;//f0;// определяем начальные параметры
        cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        header = Bitmap.createScaledBitmap(
                cBitmap, (int) (dw / 2), (int) (dw * 500 / 850), false);
        cBitmap.recycle();

        bitmapId = R.drawable.fone_red;
        cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        fone_red = Bitmap.createScaledBitmap(
                cBitmap, dw / 4 + dw / 50, dw / 4 + dw / 50, false);
        cBitmap.recycle();

        bitmapId = R.drawable.fone_green;
        cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        fone_green = Bitmap.createScaledBitmap(
                cBitmap, dw / 4 + dw / 50, dw / 4 + dw / 50, false);
        cBitmap.recycle();

        bitmapId = R.drawable.fone_white;
        cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        fone_white = Bitmap.createScaledBitmap(
                cBitmap, dw / 4 + dw / 50, dw / 4 + dw / 50, false);
        cBitmap.recycle();
    }

    public void update() { // тут будут вычисляться новые координаты

        if (MainActivity.ask_on) {
       /* if (MainActivity.ask_nu}mber!=-1 &&  MainActivity.ask_number!=7){
            if (MainActivity.ask_status[MainActivity.ask_number]==status ){
            }
            else{
                newfly();
                //balancefly();
            }*/
        } else {
            newfly();
            // balancefly();
        }

      /*  if (MainActivity.ask_number==-1 || MainActivity.ask_status[MainActivity.ask_number]!=status){
            newfly();
        }*/
        //justfly();
        // anima_fly();
    }

    private float strong = 0;
    private boolean start = true;
    public Date start_date;
    public float short_update = 0;
    public Date shorting;





   /* float balance=0;
    float accelerate=0;

    void balancefly() {
        //strong=50;
        if (MainActivity.flying) {//если включено событие - нажата кнопка
            switch (status) {
                case "RTF":
                    start_date = new Date();
                    status = "GTF";
                    //balance=0;
                    //strong = (byte) (85);
                    break;

                case "GTF":
                case "FLU":
                    accelerate= (float) Math.min(accelerate+0.5,5);//максимум 5, минимум -5
                    break;
                case "STF":
                    if (savedstatus == "UPD") {
                        if (to_update != -1) {
                            short_update += 0.1;
                            shorting = new Date();
                            //to_update-=0.1;
                        }
                    }
                    break;
            }
            MainActivity.flying = false; //отключаем событие, чтобы кнопка вновь могла сработать
        }

        if (exp == 0){
            switch (status) {
                case "RTF":

                    break;

                case "GTF":
                    d = new Date();
                    if (5 <= (float) (d.getTime() - start_date.getTime()) / 1000) {
                        //anima_type="jump";
                        //anim_step=0;
                        if(strong>0){
                            status = "FLU";
                            start_date = MainActivity.first_date;
                        }
                        else{
                            status = "RCV";
                            break_date = new Date();
                        }
                    }

                    balance+=accelerate;
                    if (balance<0){
                        balance=0;
                    accelerate=Math.max(-1,accelerate);
                    }
                    else if (balance>100){
                        balance=100;
                        accelerate=Math.min(1,accelerate);
                    }

                    accelerate-=0.15;

                    if (balance<10 | balance>90)
                        strong=20;
                    else if (balance<25 | balance>75)
                        strong=40;
                    else if (balance<45 | balance>55)
                        strong=80;
                    else
                        strong=100;

                    //strong= Math.max(strong - 1, 0);

                    break;

                case "FLU":
                    switch (anima_type) {
                        case "jump":
                            if (anim_step == 6)
                                speed += jump * strong / 100;
                            break;
                        case "bust":
                            if (anim_step == 2) {
                                speed += bust * strong / 100;
                                energy -= 1;
                                if (energy == 0) {
                                    strong = 0;
                                }
                            }
                            break;
                    }


                    balance+=accelerate;
                    if (balance<0){balance=0;}
                    else if (balance>100){balance=100;}

                    accelerate-=0.2;

                    if (balance<10 | balance>90)
                        strong=20;
                    else if (balance<25 | balance>75)
                        strong=40;
                    else if (balance<45 | balance>55)
                        strong=80;
                    else
                        strong=100;




                    pen_coord += speed; //меняем высоту
                    if (saveY < pen_coord) saveY = pen_coord;
                    if (anima_type != "jump" || anim_step > 6) {
//if(true){}
                        speed -= grav; //уменьшаем скорость на притяжение
                        if (speed < 0) {
                            status = "FLD";
                            strong = 0;
                            accelerate=0;
                            balance=0;
                        }
                    }
                    break;


                case "FLD":

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
                            save();
                        }
                        saveY = 0;
                        status = "RCV";
                        break_date = new Date();
                    }
                    break;
                case "RCV":
                    if (anima_type == "up_en"){
                        x-=jump * dw / 3;
                        if (x<-dw/4){
                            x=dw;
                        }
                    }
                    else if (anima_type == "up_jmp"){
                        if(anim_step<2)pen_coord+=jump/(anim_step+1);
                        if(anim_step>2)pen_coord-=jump/(5-anim_step);
                    }
                    else if (anima_type == "up_bst"){
                        if(anim_step<3)pen_coord+=(bust+jump)/(anim_step+1);
                        if(anim_step>3)pen_coord-=(bust+jump)/(7-anim_step);
                    }
                    else{
                        if (x!=sx) x=sx;
                        if (pen_coord!=0) pen_coord=0;
                        d = new Date();
                        if (3 <= (float) (d.getTime() - break_date.getTime()) / 1000) {
                            if (energy < maxenergy) energy += 1;
                            else {
                                status = "RTF";
                            }
                        }
                    }
                    break;

                case "UPD":
                    if (anima_type == "up_en"){
                        x-=jump * dw / 3;
                        if (x<-dw/4){
                            x=dw;
                        }
                    }
                    else if (anima_type == "up_jmp"){
                        if(anim_step<2)pen_coord+=jump/(anim_step+1);
                        if(anim_step>2)pen_coord-=jump/(5-anim_step);
                    }
                    else if (anima_type == "up_bst"){
                        if(anim_step>3)pen_coord+=(bust+jump)/(anim_step-3);
                        if(anim_step<3&& pen_coord>0)pen_coord-=(bust+jump)/(3-anim_step);
                    }
                    break;
                case "STF":
                    if (savedstatus == "RCV") {
                        if (anima_type == "up_en"){
                            x-=jump * dw / 3;
                            if (x<-dw/4){
                                x=dw;
                            }
                        }
                        else if (anima_type == "up_jmp"){
                            if(anim_step<2)pen_coord+=jump/(anim_step+1);
                            if(anim_step>2)pen_coord-=jump/(5-anim_step);
                        }
                        else if (anima_type == "up_bst"){
                            if(anim_step>3)pen_coord+=(bust+jump)/(anim_step-3);
                            if(anim_step<3 && pen_coord>0)pen_coord-=(bust+jump)/(3-anim_step);
                        }
                        else{
                            if (x!=sx) x=sx;
                            if (pen_coord!=0) pen_coord=0;
                            d = new Date();
                            if (3 <= (float) (d.getTime() - break_date.getTime()) / 1000) {
                                if (energy < maxenergy) energy += 1;
                                else {
                                    savedstatus = "RTF";
                                }
                            }
                        }

                    } else if (savedstatus == "UPD") {
                        if (anima_type == "up_en"){
                            x-=jump * dw / 3;
                            if (x<-dw/4){
                                x=dw;
                            }
                        }
                        else if (anima_type == "up_jmp"){
                            if(anim_step<2)pen_coord+=jump/(anim_step+1);
                            if(anim_step>2)pen_coord-=jump/(5-anim_step);
                        }
                        else if (anima_type == "up_bst"){
                            if(anim_step>3)pen_coord+=(bust+jump)/(anim_step+1);
                            if(anim_step<3&& pen_coord>0)pen_coord-=(bust+jump)/(7-anim_step);
                        }
                        break;
                    }
                    break;
            }

            y = sy - pen_coord * dw / 3;
        }

        if (MainActivity.update!=-1) {

            if (to_update==-1) {
                if (savedstatus != "RCV"){
                    switch (MainActivity.update) {
                        case 0:
                            if (next_jump < ml_jump) {
                                if (jump_record[next_jump] <= maxY) {
                                    to_update = time_to_up[next_jump];
                                    update_time= time_to_up[next_jump];
                                    savedate = new Date();
                                    d = new Date();
                                    savedstatus = "UPD";
                                } else MainActivity.update = -1;
                            } else MainActivity.update = -1;
                            break;
                        case 1:
                            if (next_bust < ml_bust) {
                                if (bust_record[next_bust] <= maxY) {
                                    to_update = time_to_up[next_bust + 5];
                                    update_time= time_to_up[next_bust + 5];
                                    savedate = new Date();
                                    d = new Date();
                                    savedstatus = "UPD";
                                } else MainActivity.update = -1;
                            } else MainActivity.update = -1;
                            break;
                        case 2:
                            if (next_energy < ml_energy) {
                                if (energy_record[next_energy] <= maxY) {
                                    to_update = time_to_up[next_energy + 10];
                                    update_time= time_to_up[next_energy + 10];
                                    savedate = new Date();
                                    d = new Date();
                                    savedstatus = "UPD";
                                } else MainActivity.update = -1;
                            } else MainActivity.update = -1;
                            break;
                    }
                    save();
                }
                else MainActivity.update = -1;
            }
            else {
                d=new Date();
                if (short_update>0){
                    if (1<=(float)(d.getTime()-shorting.getTime())/1000)
                    {
                        to_update-=Math.min(1,short_update);
                        short_update-=Math.min(1,short_update);
                    }
                }

                if (to_update-short_update<=(float)(d.getTime()-savedate.getTime())/1000) {
                    switch (MainActivity.update) {
                        case 0:
                            to_update=-1;
                            short_update=0;
                            next_jump++;
                            jump=jump_up[next_jump];

                            MainActivity.update=-1;
                            save();
                            if (status=="STF"){
                                savedstatus = "RCV";
                            }
                            else{
                                status="RCV";
                            }
                            break_date = new Date();
                            break;
                        case 1:
                            next_bust++;
                            bust=bust_up[next_bust];
                            to_update=-1;
                            short_update=0;
                            MainActivity.update=-1;
                            save();
                            if (status=="STF"){
                                savedstatus = "RCV";
                            }
                            else{
                                status="RCV";
                            }
                            break_date = new Date();
                            break;
                        case 2:
                            to_update=-1;
                            short_update=0;
                            next_energy++;
                            maxenergy=energy_up[next_energy];
                            MainActivity.update=-1;
                            save();
                            if (status=="STF"){
                                savedstatus = "RCV";
                            }
                            else{
                                status="RCV";
                            }
                            break_date = new Date();
                            break;
                    }




                }
            }

        }

    }*/


    void newfly() {
        //strong=50;
        if (MainActivity.flying) {//если включено событие - нажата кнопка
            switch (status) {
                case "RTF":
                    start_date = new Date();
                    status = "GTF";
                    //strong = (byte) (85);
                    break;
                case "GTF":
                    if (strong < 75)
                        strong = (float) Math.min(strong + 5.5, max_strong);  //прибавляем силу
                    else if (strong < 80)
                        strong = (float) Math.min(strong + 3.5, max_strong);  //прибавляем силу
                    else if (strong < 85)
                        strong = (float) Math.min(strong + 3.0, max_strong);  //прибавляем силу
                    else strong = (float) (Math.min(strong + 1.5, max_strong));  //прибавляем силу

                    //strong = (float) Math.min(strong + 3.5, max_strong);
                    break;
                case "FLU":
                    if (strong > 0) {
                        if (strong < 75)
                            strong = (float) Math.min(strong + 4.0, max_strong);  //прибавляем силу
                        else if (strong < 80)
                            strong = (float) Math.min(strong + 3.5, max_strong);  //прибавляем силу
                        else if (strong < 85)
                            strong = (float) Math.min(strong + 3.0, max_strong);  //прибавляем силу
                        else
                            strong = (float) (Math.min(strong + 2.5, max_strong));  //прибавляем силу
                    }
                    //if (strong>0) strong = (float) Math.min(strong + 3.5, max_strong);
                    break;
                case "STF":
                    if (savedstatus == "UPD") {
                        if (to_update != -1) {
                            short_update += 0.1;
                            shorting = new Date();
                            //to_update-=0.1;
                        }
                    }
                    break;
            }
            MainActivity.flying = false; //отключаем событие, чтобы кнопка вновь могла сработать
        }

        if (exp == 0) {
            switch (status) {
                case "RTF":

                    break;

                case "GTF":
                    d = new Date();
                    if (5 <= (float) (d.getTime() - start_date.getTime()) / 1000) {
                        //anima_type="jump";
                        //anim_step=0;
                        if (strong > 0) {
                            status = "FLU";
                            start_date = MainActivity.first_date;
                        } else {
                            status = "RCV";
                            break_date = new Date();
                        }
                    }
                    strong = (float) Math.max(strong - 0.75, 0);
                    break;

                case "FLU":
                    switch (anima_type) {
                        case "jump":
                            if (anim_step == 6)
                                speed += jump * strong / 100;
                            break;
                        case "bust":
                            if (anim_step == 2) {
                                speed += bust * strong / 100;
                                energy -= 1;
                                if (energy == 0) {
                                    strong = 0;
                                }
                            }
                            break;
                    }
                    strong = (float) Math.max(strong - 0.75, 0);
                    pen_coord += speed; //меняем высоту
                    if (saveY < pen_coord) saveY = pen_coord;
                    if (anima_type != "jump" || anim_step > 6) {
//if(true){}
                        speed -= grav; //уменьшаем скорость на притяжение
                        if (speed < 0) {
                            status = "FLD";
                            strong = 0;
                        }
                    }
                    break;


                case "FLD":

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
                            save();
                        }
                        saveY = 0;
                        status = "RCV";
                        break_date = new Date();
                    }
                    break;
                case "RCV":
                    if (anima_type == "up_en") {
                        x -= jump * dw / 3;
                        if (x < -dw / 4) {
                            x = dw;
                        }
                    } else if (anima_type == "up_jmp") {
                        if (anim_step < 2) pen_coord += jump / (anim_step + 1);
                        if (anim_step > 2) pen_coord -= jump / (5 - anim_step);
                    } else if (anima_type == "up_bst") {
                        if (anim_step < 3) pen_coord += (bust + jump) / (anim_step + 1);
                        if (anim_step > 3) pen_coord -= (bust + jump) / (7 - anim_step);
                    } else {
                        if (x != sx) x = sx;
                        if (pen_coord != 0) pen_coord = 0;
                        d = new Date();
                        if (3 <= (float) (d.getTime() - break_date.getTime()) / 1000) {
                            if (energy < maxenergy) energy += 1;
                            else {
                                status = "RTF";
                            }
                        }
                    }
                    break;

                case "UPD":
                    if (anima_type == "up_en") {
                        x -= jump * dw / 3;
                        if (x < -dw / 4) {
                            x = dw;
                        }
                    } else if (anima_type == "up_jmp") {
                        if (anim_step < 2) pen_coord += jump / (anim_step + 1);
                        if (anim_step > 2) pen_coord -= jump / (5 - anim_step);
                    } else if (anima_type == "up_bst") {
                        if (anim_step > 3) pen_coord += (bust + jump) / (anim_step - 3);
                        if (anim_step < 3 && pen_coord > 0)
                            pen_coord -= (bust + jump) / (3 - anim_step);
                    }
                    break;
                case "STF":
                    if (savedstatus == "RCV") {
                        if (anima_type == "up_en") {
                            x -= jump * dw / 3;
                            if (x < -dw / 4) {
                                x = dw;
                            }
                        } else if (anima_type == "up_jmp") {
                            if (anim_step < 2) pen_coord += jump / (anim_step + 1);
                            if (anim_step > 2) pen_coord -= jump / (5 - anim_step);
                        } else if (anima_type == "up_bst") {
                            if (anim_step > 3) pen_coord += (bust + jump) / (anim_step - 3);
                            if (anim_step < 3 && pen_coord > 0)
                                pen_coord -= (bust + jump) / (3 - anim_step);
                        } else {
                            if (x != sx) x = sx;
                            if (pen_coord != 0) pen_coord = 0;
                            d = new Date();
                            if (3 <= (float) (d.getTime() - break_date.getTime()) / 1000) {
                                if (energy < maxenergy) energy += 1;
                                else {
                                    savedstatus = "RTF";
                                }
                            }
                        }

                    } else if (savedstatus == "UPD") {
                        if (anima_type == "up_en") {
                            x -= jump * dw / 3;
                            if (x < -dw / 4) {
                                x = dw;
                            }
                        } else if (anima_type == "up_jmp") {
                            if (anim_step < 2) pen_coord += jump / (anim_step + 1);
                            if (anim_step > 2) pen_coord -= jump / (5 - anim_step);
                        } else if (anima_type == "up_bst") {
                            if (anim_step > 3) pen_coord += (bust + jump) / (anim_step + 1);
                            if (anim_step < 3 && pen_coord > 0)
                                pen_coord -= (bust + jump) / (7 - anim_step);
                        }
                        break;
                    }
                    break;
            }

            y = sy - pen_coord * dw / 3;
        }

        if (MainActivity.update != -1) {

            if (to_update == -1) {
                if (savedstatus != "RCV") {
                    switch (MainActivity.update) {
                        case 0:
                            if (next_jump < ml_jump) {
                                if (jump_record[next_jump] <= maxY) {
                                    to_update = time_to_up[next_jump];
                                    update_time = time_to_up[next_jump];
                                    savedate = new Date();
                                    d = new Date();
                                    savedstatus = "UPD";
                                } else MainActivity.update = -1;
                            } else MainActivity.update = -1;
                            break;
                        case 1:
                            if (next_bust < ml_bust) {
                                if (bust_record[next_bust] <= maxY) {
                                    to_update = time_to_up[next_bust + 5];
                                    update_time = time_to_up[next_bust + 5];
                                    savedate = new Date();
                                    d = new Date();
                                    savedstatus = "UPD";
                                } else MainActivity.update = -1;
                            } else MainActivity.update = -1;
                            break;
                        case 2:
                            if (next_energy < ml_energy) {
                                if (energy_record[next_energy] <= maxY) {
                                    to_update = time_to_up[next_energy + 10];
                                    update_time = time_to_up[next_energy + 10];
                                    savedate = new Date();
                                    d = new Date();
                                    savedstatus = "UPD";
                                } else MainActivity.update = -1;
                            } else MainActivity.update = -1;
                            break;
                    }
                    save();
                } else MainActivity.update = -1;
            } else {
                d = new Date();
                if (short_update > 0) {
                    if (1 <= (float) (d.getTime() - shorting.getTime()) / 1000) {
                        to_update -= Math.min(1, short_update);
                        short_update -= Math.min(1, short_update);
                    }
                }

                if (to_update - short_update <= (float) (d.getTime() - savedate.getTime()) / 1000) {
                    switch (MainActivity.update) {
                        case 0:
                            to_update = -1;
                            short_update = 0;
                            next_jump++;
                            jump = jump_up[next_jump];

                            MainActivity.update = -1;
                            save();
                            if (status == "STF") {
                                savedstatus = "RCV";
                            } else {
                                status = "RCV";
                            }
                            break_date = new Date();
                            break;
                        case 1:
                            next_bust++;
                            bust = bust_up[next_bust];
                            to_update = -1;
                            short_update = 0;
                            MainActivity.update = -1;
                            save();
                            if (status == "STF") {
                                savedstatus = "RCV";
                            } else {
                                status = "RCV";
                            }
                            break_date = new Date();
                            break;
                        case 2:
                            to_update = -1;
                            short_update = 0;
                            next_energy++;
                            maxenergy = energy_up[next_energy];
                            MainActivity.update = -1;
                            save();
                            if (status == "STF") {
                                savedstatus = "RCV";
                            } else {
                                status = "RCV";
                            }
                            break_date = new Date();
                            break;
                    }


                }
            }

        }

    }


    void draw_penguin(Paint paint, Canvas canvas) {
        float draw_y = y;
        if (y <= dh / 8.0) draw_y = (float) (dh / 8.0);

        Matrix matrix = new Matrix();


        double grad;
        if (legs2_grad.containsKey(anima_type)) {
            grad = legs2_grad.get(anima_type)[anim_step];
            matrix.setTranslate(x, draw_y);
            matrix.preRotate((float) grad, dw * 215 / 850, dw * 369 / 850);
            canvas.drawBitmap(legs, matrix, paint);
        } else {

        }

        if (legs_grad.containsKey(anima_type)) {
            grad = legs_grad.get(anima_type)[anim_step];
        } else {
            grad = 0;
        }
        matrix.setTranslate(x, draw_y);
        matrix.preRotate((float) grad, dw * 215 / 850, dw * 369 / 850);
        canvas.drawBitmap(legs, matrix, paint);

        if (bod2_grad.containsKey(anima_type)) {
            grad = bod2_grad.get(anima_type)[anim_step];
        } else {
            grad = 0;
        }

        matrix.reset();
        matrix.setTranslate(x, draw_y);
        matrix.preRotate((float) grad, dw * 174 / 850, dw * 127 / 850);
        canvas.drawBitmap(bod_2, matrix, paint);

        byte as = anim_step;
        if (anima_type == "jump" || anima_type == "bust") {
            as = (byte) (as * 2 + exp);
        }

        if (hand2_grad.containsKey(anima_type)) {
            grad = hand2_grad.get(anima_type)[as];
            matrix.reset();
            matrix.setTranslate(x, draw_y);
            matrix.preRotate((float) grad, dw * 236 / 850, dw * 138 / 850);
            canvas.drawBitmap(hand, matrix, paint);
        } else {

        }

        if (head_grad.containsKey(anima_type)) {
            grad = head_grad.get(anima_type)[anim_step];
        } else {
            grad = 0;
        }
        matrix.reset();
        matrix.setTranslate(x, draw_y);
        matrix.preRotate((float) grad, dw * 203 / 850, dw * 47 / 850);
        canvas.drawBitmap(head, matrix, paint);


        matrix.reset();
        matrix.setTranslate(x, draw_y);
        canvas.drawBitmap(body, matrix, paint);


        if (hand_grad.containsKey(anima_type)) {
            grad = hand_grad.get(anima_type)[as];
        } else {
            grad = 0;
        }

        matrix.reset();
        matrix.setTranslate(x, draw_y);
        matrix.preRotate((float) grad, dw * 236 / 850, dw * 138 / 850);
        canvas.drawBitmap(hand, matrix, paint);


        if (head_grad.containsKey(anima_type)) {
            grad = head_grad.get(anima_type)[anim_step];
        } else {
            grad = 0;
        }
        matrix.reset();
        matrix.setTranslate(x, draw_y - dw * 75 / 850);
        matrix.preRotate((float) grad, dw * 203 / 850, dw * 122 / 850);
        canvas.drawBitmap(header, matrix, paint);


    }

    void anima_switch() {
        if (exp == 1) {
            anim_step += 1;
            exp = 0;
        } else {
            exp = 1;
        }

        switch (anima_type) {
            case "jump":

                if (anim_step == 8)
                    if (bust > 0) {
                        anima_type = "bust";
                        anim_step = 0;
                    } else {
                        anima_type = "fly_up";
                        anim_step = 0;
                    }
                break;

            case "bust":
                if (anim_step == 5) {
                    if (speed > 0) {
                        if (energy > 0 && strong > 0)
                            anim_step = 0;
                        else {
                            anima_type = "fly_up";
                            anim_step = 0;
                        }
                    } else {
                        anima_type = "fly_down";
                        anim_step = 0;
                    }
                }
                break;

            case "fly_up":
                if (anim_step == 1) {
                    if (speed < 0) {
                        anima_type = "fly_down";
                        anim_step = 0;
                    } else
                        anim_step = 0;
                }
                break;

            case "fly_down":
                if (anim_step == 3) {
                    if (pen_coord > 0) {
                        anim_step = 2;
                    }
                } else if (anim_step == 4) {
                    anima_type = "recovery";
                    anim_step = 0;
                }
                break;

            case "recovery":
                if (anim_step == 8) {
                    if (3 <= (float) (d.getTime() - break_date.getTime()) / 1000) {
                        if (energy < maxenergy) {
                            anim_step = 0;
                        } else {
                            anima_type = "standing";
                            anim_step = 0;
                        }
                    } else {
                        anim_step = 0;
                    }
                }
                break;

            case "standing":
                if (anim_step == 6) {
                    if (status == "GTF")
                        anim_step = 5;
                    else if (status == "FLU") {
                        anima_type = "jump";
                        anim_step = 0;
                    } else if (status == "RCV") {
                        anima_type = "recovery";
                        anim_step = 0;
                    } else if (status == "UPD") {
                        if (MainActivity.update == 2) {
                            anima_type = "up_en";
                            anim_step = 0;
                        } else if (MainActivity.update == 0) {
                            anima_type = "up_jmp";
                            anim_step = 0;
                        } else if (MainActivity.update == 1) {
                            anima_type = "up_bst";
                            anim_step = 0;
                        }
                    } else if (status == "STF") {
                        if (savedstatus == "RCV") {
                            anima_type = "recovery";
                            anim_step = 0;
                        } else if (savedstatus == "UPD") {
                            if (MainActivity.update == 2) {
                                anima_type = "up_en";
                                anim_step = 0;
                            } else if (MainActivity.update == 0) {
                                anima_type = "up_jmp";
                                anim_step = 0;
                            } else if (MainActivity.update == 1) {
                                anima_type = "up_bst";
                                anim_step = 0;
                            }
                        } else {

                            anim_step = 0;
                        }
                    } else
                        anim_step = 0;
                }
                break;

            case "up_en":
                if ((x < sx + dw / 8 && x > sx - dw / 8) && x - jump * dw / 3 <= sx) {
                    if (status == "RCV") {
                        anima_type = "recovery";
                        anim_step = 0;
                    } else if (status == "RTF") {
                        anima_type = "standing";
                        anim_step = 0;
                    } else if (status == "STF") {
                        if (savedstatus == "RCV") {
                            anima_type = "recovery";
                            anim_step = 0;
                        } else if (savedstatus == "RTF") {
                            anima_type = "standing";
                            anim_step = 0;
                        } else {
                            if (anim_step == 8) anim_step = 0;
                        }
                    } else {
                        if (anim_step == 8) anim_step = 0;
                    }

                } else if (anim_step == 8) {
                    anim_step = 0;
                }
                break;
            case "up_jmp":
                if (anim_step == 5) {
                    if (status == "RCV") {
                        anima_type = "recovery";
                        anim_step = 0;
                    } else if (status == "RTF") {
                        anima_type = "standing";
                        anim_step = 0;
                    } else if (status == "STF") {
                        if (savedstatus == "RCV") {
                            anima_type = "recovery";
                            anim_step = 0;
                        } else if (savedstatus == "RTF") {
                            anima_type = "standing";
                            anim_step = 0;
                        } else {
                            anim_step = 0;
                        }
                    } else {
                        anim_step = 0;
                    }
                }


                break;
            case "up_bst":
                if (anim_step == 7) {
                    if (status == "RCV") {
                        anima_type = "recovery";
                        anim_step = 0;
                    } else if (status == "RTF") {
                        anima_type = "standing";
                        anim_step = 0;
                    } else if (status == "STF") {
                        if (savedstatus == "RCV") {
                            anima_type = "recovery";
                            anim_step = 0;
                        } else if (savedstatus == "RTF") {
                            anima_type = "standing";
                            anim_step = 0;
                        } else {
                            anim_step = 0;
                        }
                    } else {
                        anim_step = 0;
                    }
                }
                break;

        }
    }

    void drow(Paint paint, Canvas canvas) { // рисуем картинку
        float draw_y = y;
        if (y <= dh / 8.0) draw_y = (float) (dh / 8.0);


        //canvas.drawBitmap(bitmap, x, draw_y, paint);

        //paint.setColor(Color.BLACK);

        float strtY = MainActivity.Setup.getY();
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, strtY - dw / 100, dw, strtY + dw / 4 + dw / 100, paint);


        paint.setColor(Color.BLACK);

        paint.setTextSize((float) (dw / 30.0));

        if (maxY > 0) {
            //if (maxY<1000) canvas.drawText("Max: " + roundd(maxY, 2)+con.getString(R.string.meter), dw*3 /4, strtY+dw/20, paint);
            if (maxY < 1)
                canvas.drawText("Max: " + roundd(maxY * 100, 2) + con.getString(R.string.santimeter), dw * 3 / 4, strtY + dw / 20, paint);
            else if (maxY < 1000)
                canvas.drawText("Max: " + roundd(maxY, 2) + con.getString(R.string.meter), dw * 3 / 4, strtY + dw / 20, paint);
            else if (maxY < 1000000)
                canvas.drawText("Max: " + roundd(maxY / 1000, 2) + con.getString(R.string.kilometer), dw * 3 / 4, strtY + dw / 20, paint);
        }

        draw_penguin(paint, canvas);
        if (MainActivity.ask_on) {
            d = new Date();
        /*if (MainActivity.ask_number!=-1 &&  MainActivity.ask_number!=7){
            if (MainActivity.ask_status[MainActivity.ask_number]==status ){//если подсказку нужно отобразить*/
            if (status == "GTF") {
                //if (start_date.getTime() != MainActivity.first_date.getTime()) {
                start_date = new Date();
                // }
            }
            if (status == "RCV") {
                // if (break_date.getTime() != MainActivity.first_date.getTime()) {
                break_date = new Date();
                //}
            }
            if (status == "STF") {
                if (savedstatus == "UPD")
                    savedate = new Date();
                //}
            }
          /*  }
            else{
                anima_switch();
            }*/
        } else {
            anima_switch();
        }


        if (MainActivity.energy_show) {
            paint.setColor(Color.GREEN);
            canvas.drawRect(dw / 4 + dw / 50, strtY + dw / 8 + dw / 200, dw / 4 + dw / 50 + (dw / 2 - dw / 25) * energy / maxenergy, strtY + dw / 4 - dw / 200, paint);
            paint.setColor(Color.BLACK);
            canvas.drawText(con.getString(R.string.energy) + ": " + String.valueOf(energy) + " / " + String.valueOf(maxenergy), dw / 2 - dw / 8, strtY + dw * 3 / 16, paint);

        } else {

        }

        if (!MainActivity.setup) {

            paint.setColor(Color.YELLOW);
            canvas.drawRect(dw / 4 + dw / 50, strtY + dw / 200, dw / 4 + dw / 50 + (dw / 2 - dw / 25) * strong / max_strong, strtY + dw / 8 - dw / 200, paint);

            //canvas.drawRect(dw/4+dw/50-dw/100+(dw/2-dw/25)*balance/100,strtY+dw/200,dw/4+dw/50+dw/100+(dw/2-dw/25)*balance/100,strtY+dw/8-dw/200,paint);


            paint.setColor(Color.BLACK);
            canvas.drawText(con.getString(R.string.concentration) + ": " + String.valueOf(strong), dw / 2 - dw / 7, strtY + dw / 16, paint);
            paint.setTextSize((float) (dw / 15.0));

            if ((status == "FLU" || status == "FLD") && pen_coord > 0) {
                if (pen_coord < 1)
                    canvas.drawText(roundd(pen_coord * 100, 2) + con.getString(R.string.santimeter), dw / 2 - dh / 20, draw_y - dw / 15, paint);
                else if (pen_coord < 1000)
                    canvas.drawText(roundd(pen_coord, 2) + con.getString(R.string.meter), dw / 2 - dh / 20, draw_y - dw / 15, paint);
                else if (pen_coord < 1000000)
                    canvas.drawText(roundd(pen_coord / 1000, 2) + con.getString(R.string.kilometer), dw / 2 - dh / 20, draw_y - dw / 15, paint);
            } else if (status == "GTF") {
                if (start_date.getTime() != MainActivity.first_date.getTime()) {
                    canvas.drawText(con.getString(R.string.start) + ": " + (5 - (new Date().getTime() - start_date.getTime()) / 1000) + con.getString(R.string.sec), dw / 2 - 4 * dw / 15, draw_y - dw / 15, paint);
                }
            } else if (status == "RTF") {
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
                    if (GameView.tick < 45)
                        paint.setAlpha(GameView.tick * 5);
                    else
                        paint.setAlpha((90 - GameView.tick) * 5);

                    canvas.drawBitmap(fone_green, MainActivity.Setup.getX() - dw / 100, MainActivity.Setup.getY() - dw / 100, paint);
                    paint.setAlpha(255);
                }
            }
        } else {
            draw_setup(paint, canvas, draw_y);
        }
    }

    private void draw_setup(Paint paint, Canvas canvas, float draw_y) {
        paint.setTextSize(dw / 28);//dh/50);
        float shiftX = dh / 180;

        canvas.drawText(con.getString(R.string.jump_power), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw / 25, paint);
        canvas.drawText(/*(int) roundd(jump * 100, 0) + " (" + */String.valueOf(next_jump) + " lvl", MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 2 / 25, paint);

        canvas.drawText(con.getString(R.string.boost_power), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw / 25, paint);
        canvas.drawText(/*(int) roundd(bust * 100, 0) + " (" + */String.valueOf(next_bust) + " lvl", MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 2 / 25, paint);
        if (MainActivity.energy_show) {
            canvas.drawText(con.getString(R.string.energy), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw / 25, paint);
            canvas.drawText(/*(int) roundd(maxenergy, 0) + " (" +*/ String.valueOf(next_energy) + " lvl", MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 2 / 25, paint);
        }
        if (MainActivity.Fly.getVisibility() == View.VISIBLE) {
            canvas.drawText(con.getString(R.string.skip), MainActivity.Fly.getX() + shiftX, (float) (MainActivity.Fly.getY() - dw * 1.5 / 25), paint);
            canvas.drawText(con.getString(R.string.small_skip), MainActivity.Fly.getX() + shiftX, (float) (MainActivity.Fly.getY() - dw * 0.5 / 25), paint);
        }
        if (MainActivity.Reward.getVisibility() == View.VISIBLE) {
            canvas.drawText(con.getString(R.string.skip), MainActivity.Reward.getX() + shiftX, MainActivity.Reward.getY() + dw * 1 / 25, paint);
            canvas.drawText(con.getString(R.string.big_skip), MainActivity.Reward.getX() + shiftX, MainActivity.Reward.getY() + dw * 2 / 25, paint);
            canvas.drawText(con.getString(R.string.rew_allert), MainActivity.Reward.getX() + shiftX, MainActivity.Reward.getY() + dw * 3 / 25, paint);
            canvas.drawText(con.getString(R.string.rew_allert2), MainActivity.Reward.getX() + shiftX, MainActivity.Reward.getY() + dw * 4 / 25, paint);
        }
        if (MainActivity.update != -1 && to_update != -1) {
            float strtY = MainActivity.Setup.getY();

            paint.setColor(Color.GREEN);
            canvas.drawRect(dw / 4 + dw / 50, strtY + dw / 200, dw / 4 + dw / 50 + (dw / 2 - dw / 25) * (update_time - to_update + (float) (d.getTime() - savedate.getTime()) / 1000) / update_time, strtY + dw / 8 - dw / 200, paint);

            paint.setColor(Color.YELLOW);
            canvas.drawRect(dw / 4 + dw / 50 + (dw / 2 - dw / 25) * (update_time - to_update + (float) (d.getTime() - savedate.getTime()) / 1000) / update_time, strtY + dw / 200, dw / 4 + dw / 50 + (dw / 2 - dw / 25) * (update_time - to_update + short_update + (float) (d.getTime() - savedate.getTime()) / 1000) / update_time, strtY + dw / 8 - dw / 200, paint);

            paint.setColor(Color.BLACK);
        }

        if (MainActivity.update == 0 && to_update != -1) {
            canvas.drawBitmap(fone_white, MainActivity.Up_jump.getX() - dw / 100, MainActivity.Up_jump.getY() - dw / 100, paint);
            canvas.drawText(con.getString(R.string.updating), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 3 / 25, paint);
            canvas.drawText(con.getString(R.string.progress), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 4 / 25, paint);
            canvas.drawText(roundd(to_update - (float) (d.getTime() - savedate.getTime()) / 1000, 2) + con.getString(R.string.sec), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 5 / 25, paint);
            canvas.drawText(con.getString(R.string.left), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 6 / 25, paint);
            paint.setColor(Color.BLACK);
        } else {
            if (next_jump < ml_jump) {
                if (jump_record[next_jump] <= maxY) {
                    if (GameView.tick < 45)
                        paint.setAlpha(GameView.tick * 5);
                    else
                        paint.setAlpha((90 - GameView.tick) * 5);
                    canvas.drawBitmap(fone_green, MainActivity.Up_jump.getX() - dw / 100, MainActivity.Up_jump.getY() - dw / 100, paint);
                    paint.setAlpha(255);

                    canvas.drawText(con.getString(R.string.updating), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 3 / 25, paint);
                    canvas.drawText(con.getString(R.string.available), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 4 / 25, paint);
                    canvas.drawText(con.getString(R.string.take), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 5 / 25, paint);
                    canvas.drawText(String.valueOf(time_to_up[next_jump]) + con.getString(R.string.sec), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 6 / 25, paint);
                } else {

                    canvas.drawBitmap(fone_red, MainActivity.Up_jump.getX() - dw / 100, MainActivity.Up_jump.getY() - dw / 100, paint);
                    canvas.drawText(con.getString(R.string.updating), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 3 / 25, paint);
                    canvas.drawText(con.getString(R.string.not_available), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 4 / 25, paint);
                    canvas.drawText(con.getString(R.string.record), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 5 / 25, paint);

                    if (jump_record[next_jump] < 1)
                        canvas.drawText(roundd(jump_record[next_jump] * 100, 2) + con.getString(R.string.santimeter), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 6 / 25, paint);
                    else if (jump_record[next_jump] < 1000)
                        canvas.drawText(roundd(jump_record[next_jump], 2) + con.getString(R.string.meter), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 6 / 25, paint);
                    else if (jump_record[next_jump] < 1000000)
                        canvas.drawText(roundd(jump_record[next_jump] / 1000, 2) + con.getString(R.string.kilometer), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 6 / 25, paint);
                }
                paint.setColor(Color.BLACK);
            }
        }
        if (MainActivity.update == 1 && to_update != -1) {
            canvas.drawBitmap(fone_white, MainActivity.Up_fly.getX() - dw / 100, MainActivity.Up_fly.getY() - dw / 100, paint);
            canvas.drawText(con.getString(R.string.updating), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 3 / 25, paint);
            canvas.drawText(con.getString(R.string.progress), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 4 / 25, paint);
            canvas.drawText(roundd(to_update - (float) (d.getTime() - savedate.getTime()) / 1000, 2) + con.getString(R.string.sec), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 5 / 25, paint);
            canvas.drawText(con.getString(R.string.left), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 6 / 25, paint);
            paint.setColor(Color.BLACK);
        } else {
            if (next_bust < ml_bust) {
                if (bust_record[next_bust] <= maxY) {
                    if (GameView.tick < 45)
                        paint.setAlpha(GameView.tick * 5);
                    else
                        paint.setAlpha((90 - GameView.tick) * 5);
                    canvas.drawBitmap(fone_green, MainActivity.Up_fly.getX() - dw / 100, MainActivity.Up_fly.getY() - dw / 100, paint);
                    paint.setAlpha(255);
                    canvas.drawText(con.getString(R.string.updating), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 3 / 25, paint);
                    canvas.drawText(con.getString(R.string.available), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 4 / 25, paint);
                    canvas.drawText(con.getString(R.string.take), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 5 / 25, paint);
                    canvas.drawText(String.valueOf(time_to_up[next_bust + 5]) + con.getString(R.string.sec), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 6 / 25, paint);
                } else {
                    canvas.drawBitmap(fone_red, MainActivity.Up_fly.getX() - dw / 100, MainActivity.Up_fly.getY() - dw / 100, paint);
                    canvas.drawText(con.getString(R.string.updating), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 3 / 25, paint);
                    canvas.drawText(con.getString(R.string.not_available), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 4 / 25, paint);
                    canvas.drawText(con.getString(R.string.record), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 5 / 25, paint);

                    if (bust_record[next_bust] < 1)
                        canvas.drawText(roundd(bust_record[next_bust] * 100, 2) + con.getString(R.string.santimeter), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 6 / 25, paint);
                    else if (bust_record[next_bust] < 1000)
                        canvas.drawText(roundd(bust_record[next_bust], 2) + con.getString(R.string.meter), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 6 / 25, paint);
                    else if (bust_record[next_bust] < 1000000)
                        canvas.drawText(roundd(bust_record[next_bust] / 1000, 2) + con.getString(R.string.kilometer), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 6 / 25, paint);
                }
                paint.setColor(Color.BLACK);
            }
        }

        if (MainActivity.update == 2 && to_update != -1) {
            canvas.drawBitmap(fone_white, MainActivity.Up_energy.getX() - dw / 100, MainActivity.Up_energy.getY() - dw / 100, paint);
            canvas.drawText(con.getString(R.string.updating), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 3 / 25, paint);
            canvas.drawText(con.getString(R.string.progress), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 4 / 25, paint);
            canvas.drawText(roundd(to_update - (float) (d.getTime() - savedate.getTime()) / 1000, 2) + con.getString(R.string.sec), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 5 / 25, paint);
            canvas.drawText(con.getString(R.string.left), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 6 / 25, paint);
            paint.setColor(Color.BLACK);
        } else {
            if (MainActivity.energy_show) {
                if (next_energy < ml_energy) {
                    if (energy_record[next_energy] <= maxY) {
                        if (GameView.tick < 45)
                            paint.setAlpha(GameView.tick * 5);
                        else
                            paint.setAlpha((90 - GameView.tick) * 5);
                        canvas.drawBitmap(fone_green, MainActivity.Up_energy.getX() - dw / 100, MainActivity.Up_energy.getY() - dw / 100, paint);
                        paint.setAlpha(255);

                        canvas.drawText(con.getString(R.string.updating), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 3 / 25, paint);
                        canvas.drawText(con.getString(R.string.available), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 4 / 25, paint);
                        canvas.drawText(con.getString(R.string.take), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 5 / 25, paint);
                        canvas.drawText(String.valueOf(time_to_up[next_energy + 10]) + con.getString(R.string.sec), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 6 / 25, paint);
                    } else {
                        canvas.drawBitmap(fone_red, MainActivity.Up_energy.getX() - dw / 100, MainActivity.Up_energy.getY() - dw / 100, paint);
                        canvas.drawText(con.getString(R.string.updating), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 3 / 25, paint);
                        canvas.drawText(con.getString(R.string.not_available), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 4 / 25, paint);
                        canvas.drawText(con.getString(R.string.record), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 5 / 25, paint);

                        if (energy_record[next_energy] < 1)
                            canvas.drawText(roundd(energy_record[next_energy] * 100, 2) + con.getString(R.string.santimeter), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 6 / 25, paint);
                        else if (energy_record[next_energy] < 1000)
                            canvas.drawText(roundd(energy_record[next_energy], 2) + con.getString(R.string.meter), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 6 / 25, paint);
                        else if (energy_record[next_energy] < 1000000)
                            canvas.drawText(roundd(energy_record[next_energy] / 1000, 2) + con.getString(R.string.kilometer), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 6 / 25, paint);
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
        myEditor.putString("version", MainActivity.version);
        myEditor.putInt("jump", next_jump);
        myEditor.putInt("energy", next_energy);
        myEditor.putInt("bust", next_bust);
        myEditor.putFloat("record", maxY);
        myEditor.putLong("strt_date", savedate.getTime());
        myEditor.putInt("update", MainActivity.update);
        myEditor.putFloat("time_to_up", to_update);
        myEditor.putBoolean("Config.quick_down", MainActivity.quick_down);
        myEditor.putInt("ask_number", MainActivity.ask_number);
        //Gson gson = new Gson();
        //Parameter p =new Parameter();
        //int[] p=new int[]{};
        //String json = gson.toJson(p);
        myEditor.apply();
    }

    public void save_param() {
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(con);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        //myEditor.putString("version", MainActivity.version);
        myEditor.putInt("jump", next_jump);
        myEditor.putInt("energy", next_energy);
        myEditor.putInt("bust", next_bust);
        /*myEditor.putFloat("record", maxY);
        myEditor.putLong("strt_date",savedate.getTime());
        myEditor.putInt("update", MainActivity.update);
        myEditor.putFloat("time_to_up", to_update);
        myEditor.putBoolean("Config.quick_down",MainActivity.quick_down);
        myEditor.putInt("ask_number", MainActivity.ask_number);*/
        //Gson gson = new Gson();
        //Parameter p =new Parameter();
        //int[] p=new int[]{};
        //String json = gson.toJson(p);
        myEditor.apply();
    }

    public void save_upd() {
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(con);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        //myEditor.putString("version", MainActivity.version);
        /*myEditor.putInt("jump", next_jump);
        myEditor.putInt("energy", next_energy);
        myEditor.putInt("bust", next_bust);*/
        //myEditor.putFloat("record", maxY);
        myEditor.putLong("strt_date", savedate.getTime());
        myEditor.putInt("update", MainActivity.update);
        myEditor.putFloat("time_to_up", to_update);
        /*myEditor.putBoolean("Config.quick_down",MainActivity.quick_down);
        myEditor.putInt("ask_number", MainActivity.ask_number);*/
        //Gson gson = new Gson();
        //Parameter p =new Parameter();
        //int[] p=new int[]{};
        //String json = gson.toJson(p);
        myEditor.apply();
    }

    public void save_cfg() {
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(con);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putString("version", MainActivity.version);
        /*myEditor.putInt("jump", next_jump);
        myEditor.putInt("energy", next_energy);
        myEditor.putInt("bust", next_bust);*/
        //myEditor.putFloat("record", maxY);
        /*myEditor.putLong("strt_date",savedate.getTime());
        myEditor.putInt("update", MainActivity.update);
        myEditor.putFloat("time_to_up", to_update);*/
        myEditor.putBoolean("Config.quick_down", MainActivity.quick_down);
        //myEditor.putInt("ask_number", MainActivity.ask_number);*/
        //Gson gson = new Gson();
        //Parameter p =new Parameter();
        //int[] p=new int[]{};
        //String json = gson.toJson(p);
        myEditor.apply();
    }

    public void save_ask() {
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(con);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        /*myEditor.putString("version", MainActivity.version);
        myEditor.putInt("jump", next_jump);
        myEditor.putInt("energy", next_energy);
        myEditor.putInt("bust", next_bust);
        myEditor.putFloat("record", maxY);
        myEditor.putLong("strt_date",savedate.getTime());
        myEditor.putInt("update", MainActivity.update);
        myEditor.putFloat("time_to_up", to_update);
        myEditor.putBoolean("Config.quick_down",MainActivity.quick_down);*/
        myEditor.putInt("ask_number", MainActivity.ask_number);
        //Gson gson = new Gson();
        //Parameter p =new Parameter();
        //int[] p=new int[]{};
        //String json = gson.toJson(p);
        myEditor.apply();
    }


}
