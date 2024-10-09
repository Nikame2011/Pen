package com.pf.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.preference.PreferenceManager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Penguin {

    public interface MainListener {
        void onConcentrationChanged(float concentration);

        void onBustChanged(double bust);

        void onEnergyChanged(double energy, double maxEnergy);

        void onStatusChanged(String status, String savedStatus);

        void onRecordChanged(double record);
    }

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

    protected Bitmap fone_red; // картинка
    protected Bitmap fone_green; // картинка
    protected Bitmap fone_white; // картинка
    protected Bitmap bod_2;
    protected Bitmap body;
    protected Bitmap head;
    protected Bitmap header;
    protected Bitmap glases;
    protected Bitmap hand;
    protected Bitmap legs;

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

    Map<String, Double[]> bod2_grad = new HashMap<>();
    Map<String, Double[]> head_grad = new HashMap<>();
    Map<String, Double[]> hand_grad = new HashMap<>();
    Map<String, Double[]> legs_grad = new HashMap<>();
    Map<String, Double[]> hand2_grad = new HashMap<>();
    Map<String, Double[]> legs2_grad = new HashMap<>();

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
            status = "STF";
            savedstatus = "UPD";
            switch (MainActivity.update) {
                case 0 -> update_time = time_to_up[next_jump];
                case 1 -> update_time = time_to_up[next_bust + 5];
                case 2 -> update_time = time_to_up[next_energy + 10];
            }
        }
        listener.onStatusChanged(status, savedstatus);

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
        status = "RTF";
        listener.onStatusChanged(status, savedstatus);
        anima_type = "standing";
        anim_step = 0;
    }

    Point[] centralPoints;
    Matrix[] moveMatrix;

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

        centralPoints = new Point[]{
                new Point(convert(61), convert(1)),
                new Point(convert(61), convert(1)),
                new Point(convert(6), convert(61)),
                new Point(convert(29), convert(24)),
                new Point(convert(102), convert(47)),
                new Point(0, 0),
                new Point(convert(29), convert(24)),
                new Point(convert(27), convert(88)),
                new Point(convert(46), convert(24))
        };

        Point[] movePoints = new Point[]{
                new Point(convert(154), convert(368)),
                new Point(convert(154), convert(368)),
                new Point(convert(168), convert(66)),
                new Point(convert(207), convert(114)),
                new Point(convert(101), 0),
                new Point(convert(143), 1),//todo wtf? почему единица?
                new Point(convert(207), convert(114)),
                new Point(convert(176), convert(-41)),
                new Point(convert(159), convert(17))
        };
        moveMatrix = new Matrix[movePoints.length];
        for (int i = 0; i < movePoints.length; i++) {
            moveMatrix[i] = new Matrix();
            moveMatrix[i].preTranslate(movePoints[i].x, movePoints[i].y);
        }


        body = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.body_new),
                convert(192), convert(370), false);

        bod_2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bod2_new),
                convert(44), convert(69), false);

        hand = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.hand_new),
                convert(89), convert(207), false);

        legs = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.legs_new),
                convert(103), convert(32), false);

        head = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.head_new),
                convert(151), convert(95), false);

        header = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.h_ny_2022_new),
                convert(89), convert(101), false);

        glases = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.glases),
                convert(66), convert(22), false);

        fone_red = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.fone_red),
                boxAndSide, boxAndSide, false);

        fone_green = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.fone_green),
                boxAndSide, boxAndSide, false);

        fone_white = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.fone_white),
                boxAndSide, boxAndSide, false);
    }

    double localDp;

    private int convert(int size) {
        return (int) (localDp * size);
    }

    private int convert(double size) {
        return (int) (localDp * size);
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
                case "RTF" -> {
                    start_date = new Date();
                    status = "GTF";
                    listener.onStatusChanged(status, savedstatus);
                }
                //strong = (byte) (85);
                case "GTF" -> {
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
                case "FLU" -> {
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
                case "STF" -> {
                    if (savedstatus.equals("UPD")) {
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
            }
            MainActivity.flying = false; //отключаем событие, чтобы кнопка вновь могла сработать
        } else if (lastTap != null && concentration > 0 && new Date().getTime() - lastTap.getTime() > 1000) {
            concentration -= 3;
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
                        if (concentration > 0) {
                            status = "FLU";
                            listener.onStatusChanged(status, savedstatus);
                            start_date = MainActivity.first_date;
                        } else {
                            status = "RCV";
                            listener.onStatusChanged(status, savedstatus);
                            break_date = new Date();
                        }
                    }
                    concentration = (float) Math.max(concentration - 0.75, 0);
                    listener.onConcentrationChanged(concentration);
                    break;

                case "FLU":
                    switch (anima_type) {
                        case "jump" -> {
                            if (anim_step == 6)
                                speed += (float) (jump * concentration / 100f);
                        }
                        case "bust" -> {
                            if (anim_step == 2) {
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
                    if (!anima_type.equals("jump") || anim_step > 6) {
//if(true){}
                        speed -= grav; //уменьшаем скорость на притяжение
                        if (speed < 0) {
                            status = "FLD";
                            listener.onStatusChanged(status, savedstatus);
                            concentration = 0;
                        }
                    }
                    listener.onConcentrationChanged(concentration);
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
                            listener.onRecordChanged(maxY);
                            save();
                        }
                        saveY = 0;
                        status = "RCV";
                        listener.onStatusChanged(status, savedstatus);
                        break_date = new Date();
                    }
                    break;
                case "RCV":
                    switch (anima_type) {
                        case "up_en" -> {
                            x -= (float) (jump * dw / 3f);
                            if (x < -box) {
                                x = dw;
                            }
                        }
                        case "up_jmp" -> {
                            if (anim_step < 2) pen_coord += (float) (jump / (anim_step + 1));
                            if (anim_step > 2) pen_coord -= (float) (jump / (5 - anim_step));
                        }
                        case "up_bst" -> {
                            if (anim_step < 3)
                                pen_coord += (float) ((bust + jump) / (anim_step + 1));
                            if (anim_step > 3)
                                pen_coord -= (float) ((bust + jump) / (7 - anim_step));
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
                                    status = "RTF";
                                    listener.onStatusChanged(status, savedstatus);
                                }
                            }
                        }
                    }
                    break;

                case "UPD":
                    switch (anima_type) {
                        case "up_en" -> {
                            x -= (float) (jump * dw / 3);
                            if (x < -box) {
                                x = dw;
                            }
                        }
                        case "up_jmp" -> {
                            if (anim_step < 2) pen_coord += (float) (jump / (anim_step + 1));
                            if (anim_step > 2) pen_coord -= (float) (jump / (5 - anim_step));
                        }
                        case "up_bst" -> {
                            if (anim_step > 3)
                                pen_coord += (float) ((bust + jump) / (anim_step - 3));
                            if (anim_step < 3 && pen_coord > 0)
                                pen_coord -= (float) ((bust + jump) / (3 - anim_step));
                        }
                    }
                    break;
                case "STF":
                    if (savedstatus.equals("RCV")) {
                        concentration = 0;
                        listener.onConcentrationChanged(concentration);

                        switch (anima_type) {
                            case "up_en" -> {
                                x -= (float) (jump * dw / 3);
                                if (x < (float) -box) {
                                    x = dw;
                                }
                            }
                            case "up_jmp" -> {
                                if (anim_step < 2) pen_coord += (float) (jump / (anim_step + 1));
                                if (anim_step > 2) pen_coord -= (float) (jump / (5 - anim_step));
                            }
                            case "up_bst" -> {
                                if (anim_step > 3)
                                    pen_coord += (float) ((bust + jump) / (anim_step - 3));
                                if (anim_step < 3 && pen_coord > 0)
                                    pen_coord -= (float) ((bust + jump) / (3 - anim_step));
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
                                        savedstatus = "RTF";
                                        listener.onStatusChanged(status, savedstatus);
                                    }
                                }
                            }
                        }

                    } else if (savedstatus.equals("UPD")) {
                        concentration = (float) Math.max(concentration - 0.75, 5);
                        listener.onConcentrationChanged(concentration);

                        switch (anima_type) {
                            case "up_en" -> {
                                x -= (float) (jump * dw / 3);
                                if (x < (float) -box) {
                                    x = dw;
                                }
                            }
                            case "up_jmp" -> {
                                if (anim_step < 2) pen_coord += (float) (jump / (anim_step + 1));
                                if (anim_step > 2) pen_coord -= (float) (jump / (5 - anim_step));
                            }
                            case "up_bst" -> {
                                if (anim_step > 3)
                                    pen_coord += (float) ((bust + jump) / (anim_step + 1));
                                if (anim_step < 3 && pen_coord > 0)
                                    pen_coord -= (float) ((bust + jump) / (7 - anim_step));
                            }
                        }
                        break;
                    }
                    break;
            }

            y = sy - pen_coord * dw / 3;
        }

        if (MainActivity.update != -1) {

            if (to_update == -1) {
                if (!savedstatus.equals("RCV")) {
                    switch (MainActivity.update) {
                        case 0 -> {
                            if (next_jump < ml_jump) {
                                if (jump_record[next_jump] <= maxY) {
                                    to_update = time_to_up[next_jump];
                                    update_time = time_to_up[next_jump];
                                    savedate = new Date();
                                    d = new Date();
                                    savedstatus = "UPD";
                                    listener.onStatusChanged(status, savedstatus);
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
                                    savedstatus = "UPD";
                                    listener.onStatusChanged(status, savedstatus);
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
                                    savedstatus = "UPD";
                                    listener.onStatusChanged(status, savedstatus);
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
                            if (status.equals("STF")) {
                                savedstatus = "RCV";
                            } else {
                                status = "RCV";
                            }
                            listener.onStatusChanged(status, savedstatus);
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
                            if (status.equals("STF")) {
                                savedstatus = "RCV";
                            } else {
                                status = "RCV";
                            }
                            listener.onStatusChanged(status, savedstatus);
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
                            if (status.equals("STF")) {
                                savedstatus = "RCV";
                            } else {
                                status = "RCV";
                            }
                            listener.onStatusChanged(status, savedstatus);
                            break_date = new Date();
                        }
                    }
                }
            }
        }
    }

    Matrix matrix = new Matrix();

//    private class bodyPart{
//
//        Bitmap bitmap;
//        Point centralPoint;
//        Matrix matrix = new Matrix();
//        HashMap<Double,Matrix> preRotateMatrix;
//        HashMap<String, Double[]> rotationsAnima;
//
//        public Bitmap getBitmap() {
//            return bitmap;
//        }
//
//        public Matrix getMatrix() {
//            return matrix;
//        }
//
//        public void prepareMatrix(String animaType,byte animStep, float x, float y) {
//            double grad = rotationsAnima.containsKey(animaType)?rotationsAnima.get(anima_type)[anim_step]:0;
//            if(!preRotateMatrix.containsKey(grad)){
//
//            }
//            else{
//
//            }
//        }
//    }
//
//    HashMap<String,bodyPart> parts=new HashMap<>();

    Matrix translationMatrix = new Matrix();

//    void draw_penguin(Paint paint, Canvas canvas) {
//        float draw_y = y;
//        if (y <= dh / 8.0) draw_y = (float) (dh / 8.0);
//
//        double grad;
//
//        matrix.setTranslate(x, draw_y);
//
//        if (legs2_grad.containsKey(anima_type)) {
//            grad = legs2_grad.get(anima_type)[anim_step];
//        } else {
//            grad = 0;
//        }
//
//        matrix.preRotate((float) grad, centralPoints[0].x, centralPoints[0].y);//(float) (dw * 215) / 850, (float) (dw * 369) / 850);
//        canvas.drawBitmap(legs, matrix, paint);
//
//
//        if (legs_grad.containsKey(anima_type)) {
//            grad = legs_grad.get(anima_type)[anim_step];
//        } else {
//            grad = 0;
//        }
//        //matrix = new Matrix();
//
//        matrix.preRotate((float) grad, centralPoints[1].x, centralPoints[1].y);//(float) (dw * 215) / 850, (float) (dw * 369) / 850);
//        canvas.drawBitmap(legs, matrix, paint);
//
//        if (bod2_grad.containsKey(anima_type)) {
//            grad = bod2_grad.get(anima_type)[anim_step];
//        } else {
//            grad = 0;
//        }
//        matrix.preRotate((float) grad, centralPoints[2].x, centralPoints[2].y);//(float) (dw * 174) / 850, (float) (dw * 127) / 850);
//        canvas.drawBitmap(bod_2, matrix, paint);
//
//        byte as = anim_step;
//        if (anima_type.equals("jump") || anima_type.equals("bust")) {
//            as = (byte) (as * 2 + exp);
//        }
//
//        if (hand2_grad.containsKey(anima_type)) {
//            grad = hand2_grad.get(anima_type)[as];
//        } else {
//            grad = 0;
//        }
//        matrix.preRotate((float) grad, centralPoints[4].x, centralPoints[4].y);//(float) (dw * 236) / 850, (float) (dw * 138) / 850);
//        canvas.drawBitmap(hand, matrix, paint);
//
//
//        if (head_grad.containsKey(anima_type)) {
//            grad = head_grad.get(anima_type)[anim_step];
//        } else {
//            grad = 0;
//        }
//        matrix.preRotate((float) grad, centralPoints[5].x, centralPoints[5].y);//(float) (dw * 203) / 850, (float) (dw * 47) / 850);
//        canvas.drawBitmap(head, matrix, paint);
//
//        matrix.setTranslate(x, draw_y);
//        canvas.drawBitmap(body, matrix, paint);
//
//
//        if (hand_grad.containsKey(anima_type)) {
//            grad = hand_grad.get(anima_type)[as];
//        } else {
//            grad = 0;
//        }
//
//        matrix.preRotate((float) grad, centralPoints[6].x, centralPoints[6].y);//(float) (dw * 236) / 850, (float) (dw * 138) / 850);
//        canvas.drawBitmap(hand, matrix, paint);
//
//
//        if (head_grad.containsKey(anima_type)) {
//            grad = head_grad.get(anima_type)[anim_step];
//        } else {
//            grad = 0;
//        }
//        //matrix = new Matrix();
//        matrix.setTranslate(x, draw_y - (float) (dw * 75) / 850);
//        if (grad != 0)
//            matrix.preRotate((float) grad, centralPoints[5].x, centralPoints[5].y);//(float) (dw * 203) / 850, (float) (dw * 122) / 850);
//        canvas.drawBitmap(header, matrix, paint);
//
//
//    }

//    void draw_penguin(Paint paint, Canvas canvas) {
//        float draw_y = y;
//        if (y <= dh / 8.0) draw_y = (float) (dh / 8.0);
//
//        double grad;
//
//        translationMatrix.setTranslate(x, draw_y);
//
//        if (legs2_grad.containsKey(anima_type)) {
//            grad = legs2_grad.get(anima_type)[anim_step];
//        } else {
//            grad = 0;
//        }
//        matrix.set(translationMatrix);
//        if (grad != 0)
//            matrix.preRotate((float) grad, centralPoints[0].x, centralPoints[0].y);//(float) (dw * 215) / 850, (float) (dw * 369) / 850);
//        canvas.drawBitmap(legs, matrix, paint);
//
//
//        if (legs_grad.containsKey(anima_type)) {
//            grad = legs_grad.get(anima_type)[anim_step];
//        } else {
//            grad = 0;
//        }
//        //matrix = new Matrix();
//        matrix.set(translationMatrix);
//        if (grad != 0)
//            matrix.preRotate((float) grad, centralPoints[1].x, centralPoints[1].y);//(float) (dw * 215) / 850, (float) (dw * 369) / 850);
//        canvas.drawBitmap(legs, matrix, paint);
//
//        if (bod2_grad.containsKey(anima_type)) {
//            grad = bod2_grad.get(anima_type)[anim_step];
//        } else {
//            grad = 0;
//        }
//
//        //matrix = new Matrix();
//        matrix.set(translationMatrix);
//        if (grad != 0)
//            matrix.preRotate((float) grad, centralPoints[2].x, centralPoints[2].y);//(float) (dw * 174) / 850, (float) (dw * 127) / 850);
//        canvas.drawBitmap(bod_2, matrix, paint);
//
//        byte as = anim_step;
//        if (anima_type.equals("jump") || anima_type.equals("bust")) {
//            as = (byte) (as * 2 + exp);
//        }
//
//        if (hand2_grad.containsKey(anima_type)) {
//            grad = hand2_grad.get(anima_type)[as];
//        } else {
//            grad = 0;
//        }
//        matrix.set(translationMatrix);
//        if (grad != 0)
//            matrix.preRotate((float) grad, centralPoints[4].x, centralPoints[4].y);//(float) (dw * 236) / 850, (float) (dw * 138) / 850);
//        canvas.drawBitmap(hand, matrix, paint);
//
//
//        if (head_grad.containsKey(anima_type)) {
//            grad = head_grad.get(anima_type)[anim_step];
//        } else {
//            grad = 0;
//        }
//        matrix.set(translationMatrix);
//        if (grad != 0)
//            matrix.preRotate((float) grad, centralPoints[5].x, centralPoints[5].y);//(float) (dw * 203) / 850, (float) (dw * 47) / 850);
//        canvas.drawBitmap(head, matrix, paint);
//
//
//        matrix.set(translationMatrix);
//        canvas.drawBitmap(body, matrix, paint);
//
//
//        if (hand_grad.containsKey(anima_type)) {
//            grad = hand_grad.get(anima_type)[as];
//        } else {
//            grad = 0;
//        }
//        matrix.set(translationMatrix);
//        if (grad != 0)
//            matrix.preRotate((float) grad, centralPoints[6].x, centralPoints[6].y);//(float) (dw * 236) / 850, (float) (dw * 138) / 850);
//        canvas.drawBitmap(hand, matrix, paint);
//
//
//        if (head_grad.containsKey(anima_type)) {
//            grad = head_grad.get(anima_type)[anim_step];
//        } else {
//            grad = 0;
//        }
//        //matrix = new Matrix();
//        matrix.setTranslate(x, draw_y - (float) (dw * 75) / 850);
//        if (grad != 0)
//            matrix.preRotate((float) grad, centralPoints[5].x, centralPoints[5].y);//(float) (dw * 203) / 850, (float) (dw * 122) / 850);
//        canvas.drawBitmap(header, matrix, paint);
//
//
//    }

    //Matrix legsMatrix=null;
//    Matrix headMatrix=null;
    Matrix transMatrix = new Matrix();

    float shiftY = 0;
    float shiftX = 0;

    float lastDrawY = sy;
    float lastY = sy;

    void draw_penguin(Paint paint, Canvas canvas, float draw_y) {


        double grad;

//        for(String part:parts.keySet()){
//            parts.get(part).prepareMatrix(anima_type, anim_step);
//            canvas.drawBitmap( parts.get(part).getBitmap(), parts.get(part).getMatrix(),paint);
//        }
        //translationMatrix.setTranslate(x, draw_y);

        transMatrix.setTranslate(x + shiftX, draw_y);

        if (legs2_grad.containsKey(anima_type)) {
            grad = legs2_grad.get(anima_type)[anim_step];
//            if(legsMatrix==null){
//                legsMatrix=new Matrix();
//                legsMatrix.preTranslate(movePoints[0].x,movePoints[0].y);
//            }
            matrix.set(transMatrix);
            matrix.preConcat(moveMatrix[0]);
            if (grad != 0)
                matrix.preRotate((float) grad, centralPoints[0].x, centralPoints[0].y);//(float) (dw * 215) / 850, (float) (dw * 369) / 850);
//            matrix.setTranslate(x, draw_y);
//            matrix.preTranslate(movePoints[0].x,movePoints[0].y);
//            if (grad != 0)
//                matrix.preRotate((float) grad, centralPoints[0].x,centralPoints[0].y);//(float) (dw * 215) / 850, (float) (dw * 369) / 850);

            canvas.drawBitmap(legs, matrix, paint);
        } else {

        }

        if (legs_grad.containsKey(anima_type)) {
            grad = legs_grad.get(anima_type)[anim_step];
        } else {
            grad = 0;
        }
        matrix.set(transMatrix);
        matrix.preConcat(moveMatrix[1]);
        if (grad != 0)
            matrix.preRotate((float) grad, centralPoints[1].x, centralPoints[1].y);//(float) (dw * 215) / 850, (float) (dw * 369) / 850);
        canvas.drawBitmap(legs, matrix, paint);

        if (bod2_grad.containsKey(anima_type)) {
            grad = bod2_grad.get(anima_type)[anim_step];
        } else {
            grad = 0;
        }

        matrix.set(transMatrix);
        matrix.preConcat(moveMatrix[2]);
        if (grad != 0)
            matrix.preRotate((float) grad, centralPoints[2].x, centralPoints[2].y);//(float) (dw * 174) / 850, (float) (dw * 127) / 850);
        canvas.drawBitmap(bod_2, matrix, paint);

        byte as = anim_step;
        if (anima_type.equals("jump") || anima_type.equals("bust")) {
            as = (byte) (as * 2 + exp);
        }

        if (hand2_grad.containsKey(anima_type)) {
            grad = hand2_grad.get(anima_type)[as];
            matrix.set(transMatrix);
            matrix.preConcat(moveMatrix[3]);
            if (grad != 0)
                matrix.preRotate((float) grad, centralPoints[3].x, centralPoints[3].y);//(float) (dw * 236) / 850, (float) (dw * 138) / 850);
            canvas.drawBitmap(hand, matrix, paint);
        } else {

        }

        if (head_grad.containsKey(anima_type)) {
            grad = head_grad.get(anima_type)[anim_step];
        } else {
            grad = 0;
        }
        matrix.set(transMatrix);
        matrix.preConcat(moveMatrix[4]);
        if (grad != 0)
            matrix.preRotate((float) grad, centralPoints[4].x, centralPoints[4].y);//(float) (dw * 215) / 850, (float) (dw * 369) / 850);
//        matrix = new Matrix();
//        matrix.setTranslate(x, draw_y);
//        matrix.preTranslate(movePoints[4].x,movePoints[4].y);
//        if (grad != 0)
//            matrix.preRotate((float) grad, centralPoints[4].x,centralPoints[4].y);//(float) (dw * 203) / 850, (float) (dw * 47) / 850);
        canvas.drawBitmap(head, matrix, paint);

        matrix.set(transMatrix);
        matrix.preConcat(moveMatrix[5]);
        canvas.drawBitmap(body, matrix, paint);


        if (hand_grad.containsKey(anima_type)) {
            grad = hand_grad.get(anima_type)[as];
        } else {
            grad = 0;
        }

        matrix.set(transMatrix);
        matrix.preConcat(moveMatrix[6]);
        if (grad != 0)
            matrix.preRotate((float) grad, centralPoints[6].x, centralPoints[6].y);//(float) (dw * 236) / 850, (float) (dw * 138) / 850);
        canvas.drawBitmap(hand, matrix, paint);


        if (head_grad.containsKey(anima_type)) {
            grad = head_grad.get(anima_type)[anim_step];
        } else {
            grad = 0;
        }

        matrix.set(transMatrix);
        matrix.preConcat(moveMatrix[8]);
        if (grad != 0)
            matrix.preRotate((float) grad, centralPoints[8].x, centralPoints[8].y);//(float) (dw * 203) / 850, (float) (dw * 122) / 850);
        canvas.drawBitmap(glases, matrix, paint);

//        matrix.set(transMatrix);
//            matrix.preConcat(moveMatrix[7]);
//            if (grad != 0)
//                matrix.preRotate((float) grad, centralPoints[7].x,centralPoints[7].y);//(float) (dw * 203) / 850, (float) (dw * 122) / 850);
//        canvas.drawBitmap(header, matrix, paint);


    }

//        void draw_penguin(Paint paint, Canvas canvas) {
//        float draw_y = y;
//        if (y <= dh / 8.0) draw_y = (float) (dh / 8.0);
//
//        double grad;
//
////        for(String part:parts.keySet()){
////            parts.get(part).prepareMatrix(anima_type, anim_step);
////            canvas.drawBitmap( parts.get(part).getBitmap(), parts.get(part).getMatrix(),paint);
////        }
//        //translationMatrix.setTranslate(x, draw_y);
//
//        if (legs2_grad.containsKey(anima_type)) {
//            grad = legs2_grad.get(anima_type)[anim_step];
//            matrix.setTranslate(x, draw_y);
//
//            if (grad != 0)
//                matrix.preRotate((float) grad, centralPoints[0].x,centralPoints[0].y);//(float) (dw * 215) / 850, (float) (dw * 369) / 850);
//            canvas.drawBitmap(legs, matrix, paint);
//        } else {
//
//        }
//
//        if (legs_grad.containsKey(anima_type)) {
//            grad = legs_grad.get(anima_type)[anim_step];
//        } else {
//            grad = 0;
//        }
//        matrix = new Matrix();
//        matrix.setTranslate(x, draw_y);
//        if (grad != 0)
//            matrix.preRotate((float) grad, centralPoints[1].x,centralPoints[1].y);//(float) (dw * 215) / 850, (float) (dw * 369) / 850);
//        canvas.drawBitmap(legs, matrix, paint);
//
//        if (bod2_grad.containsKey(anima_type)) {
//            grad = bod2_grad.get(anima_type)[anim_step];
//        } else {
//            grad = 0;
//        }
//
//        matrix = new Matrix();
//        matrix.setTranslate(x, draw_y);
//        if (grad != 0)
//            matrix.preRotate((float) grad, centralPoints[2].x,centralPoints[2].y);//(float) (dw * 174) / 850, (float) (dw * 127) / 850);
//        canvas.drawBitmap(bod_2, matrix, paint);
//
//        byte as = anim_step;
//        if (anima_type.equals("jump") || anima_type.equals("bust")) {
//            as = (byte) (as * 2 + exp);
//        }
//
//        if (hand2_grad.containsKey(anima_type)) {
//            grad = hand2_grad.get(anima_type)[as];
//            matrix = new Matrix();
//            matrix.setTranslate(x, draw_y);
//            if (grad != 0)
//                matrix.preRotate((float) grad, centralPoints[4].x,centralPoints[4].y);//(float) (dw * 236) / 850, (float) (dw * 138) / 850);
//            canvas.drawBitmap(hand, matrix, paint);
//        } else {
//
//        }
//
//        if (head_grad.containsKey(anima_type)) {
//            grad = head_grad.get(anima_type)[anim_step];
//        } else {
//            grad = 0;
//        }
//        matrix = new Matrix();
//        matrix.setTranslate(x, draw_y);
//        if (grad != 0)
//            matrix.preRotate((float) grad, centralPoints[5].x,centralPoints[5].y);//(float) (dw * 203) / 850, (float) (dw * 47) / 850);
//        canvas.drawBitmap(head, matrix, paint);
//
//
//        matrix = new Matrix();
//        matrix.setTranslate(x, draw_y);
//        canvas.drawBitmap(body, matrix, paint);
//
//
//        if (hand_grad.containsKey(anima_type)) {
//            grad = hand_grad.get(anima_type)[as];
//        } else {
//            grad = 0;
//        }
//
//        matrix = new Matrix();
//        matrix.setTranslate(x, draw_y);
//        if (grad != 0)
//            matrix.preRotate((float) grad, centralPoints[6].x,centralPoints[6].y);//(float) (dw * 236) / 850, (float) (dw * 138) / 850);
//        canvas.drawBitmap(hand, matrix, paint);
//
//
//        if (head_grad.containsKey(anima_type)) {
//            grad = head_grad.get(anima_type)[anim_step];
//        } else {
//            grad = 0;
//        }
//        matrix = new Matrix();
//        matrix.setTranslate(x, draw_y - (float) (dw * 75) / 850);
//        if (grad != 0)
//            matrix.preRotate((float) grad, centralPoints[5].x,centralPoints[5].y);//(float) (dw * 203) / 850, (float) (dw * 122) / 850);
//        canvas.drawBitmap(header, matrix, paint);
//
//
//    }

//    private class Animator {
//        enum DefineBool {
//
//        }
//
//        enum Part {
//            leftHand,
//            rightHand,
//            head,
//            body,
//            leftLeg,
//            rightLeg,
//            dress,
//            neck {
//                int centralX;
//                int centalY;
//                int shiftX;
//                int shiftY;
//
//                Part(int centralX,
//                int centalY,
//                int shiftX,
//                int shiftY) {
//
//                }            }
//        }
//
//        HashMap<DefineBool, Boolean> statuses = new HashMap<>();
//
//        private class Animation {
//            Part centralPart;
//
//        }
//
//    }

    void anima_switch() {
        if (exp == 1) {
            anim_step += 1;
            exp = 0;
        } else {
            exp = 1;
        }

        switch (anima_type) {
            case "jump" -> {
                if (anim_step == 8)
                    if (bust > 0) {
                        anima_type = "bust";
                        anim_step = 0;
                    } else {
                        anima_type = "fly_up";
                        anim_step = 0;
                    }
            }
            case "bust" -> {
                if (anim_step == 5) {
                    if (speed > 0) {
                        if (energy > 0 && concentration > 0)
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
            }
            case "fly_up" -> {
                if (anim_step == 1) {
                    if (speed < 0) {
                        anima_type = "fly_down";
                        anim_step = 0;
                    } else
                        anim_step = 0;
                }
            }
            case "fly_down" -> {
                if (anim_step == 3) {
                    if (pen_coord > 0) {
                        anim_step = 2;
                    }
                } else if (anim_step == 4) {
                    anima_type = "recovery";
                    anim_step = 0;
                }
            }
            case "recovery" -> {
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
            }
            case "standing" -> {
                if (anim_step == 6) {
                    switch (status) {
                        case "GTF" -> anim_step = 5;
                        case "FLU" -> {
                            anima_type = "jump";
                            anim_step = 0;
                        }
                        case "RCV" -> {
                            anima_type = "recovery";
                            anim_step = 0;
                        }
                        case "UPD" -> {
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
                        }
                        case "STF" -> {
                            if (savedstatus.equals("RCV")) {
                                anima_type = "recovery";
                                anim_step = 0;
                            } else if (savedstatus.equals("UPD")) {
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
                        }
                        default -> anim_step = 0;
                    }
                }
            }
            case "up_en" -> {
                if ((x < sx + (float) dw / 8 && x > sx - (float) dw / 8) && x - jump * dw / 3 <= sx) {
                    switch (status) {
                        case "RCV" -> {
                            anima_type = "recovery";
                            anim_step = 0;
                        }
                        case "RTF" -> {
                            anima_type = "standing";
                            anim_step = 0;
                        }
                        case "STF" -> {
                            if (savedstatus.equals("RCV")) {
                                anima_type = "recovery";
                                anim_step = 0;
                            } else if (savedstatus.equals("RTF")) {
                                anima_type = "standing";
                                anim_step = 0;
                            } else {
                                if (anim_step == 8) anim_step = 0;
                            }
                        }
                        default -> {
                            if (anim_step == 8) anim_step = 0;
                        }
                    }

                } else if (anim_step == 8) {
                    anim_step = 0;
                }
            }
            case "up_jmp" -> {
                if (anim_step == 5) {
                    switch (status) {
                        case "RCV" -> {
                            anima_type = "recovery";
                            anim_step = 0;
                        }
                        case "RTF" -> {
                            anima_type = "standing";
                            anim_step = 0;
                        }
                        case "STF" -> {
                            if (savedstatus.equals("RCV")) {
                                anima_type = "recovery";
                                anim_step = 0;
                            } else if (savedstatus.equals("RTF")) {
                                anima_type = "standing";
                                anim_step = 0;
                            } else {
                                anim_step = 0;
                            }
                        }
                        default -> anim_step = 0;
                    }
                }
            }
            case "up_bst" -> {
                if (anim_step == 7) {
                    switch (status) {
                        case "RCV" -> {
                            anima_type = "recovery";
                            anim_step = 0;
                        }
                        case "RTF" -> {
                            anima_type = "standing";
                            anim_step = 0;
                        }
                        case "STF" -> {
                            if (savedstatus.equals("RCV")) {
                                anima_type = "recovery";
                                anim_step = 0;
                            } else if (savedstatus.equals("RTF")) {
                                anima_type = "standing";
                                anim_step = 0;
                            } else {
                                anim_step = 0;
                            }
                        }
                        default -> anim_step = 0;
                    }
                }
            }
        }
    }

    void drow(Paint paint, Canvas canvas) { // рисуем картинку
        float draw_y = lastDrawY;
        if (y <= dh / 8.0) {
            if (status == "FLU") {
                shiftY = (dh / 8f - y);
                draw_y = (float) (dh / 8.0);
            } else {
                if (lastDrawY != sy)
                    draw_y = (float) Math.min(lastDrawY - lastY + y, sy);
                else {
                    shiftY = (sy - y);
                }
            }
        } else {
            if (status == "FLU" || status == "STF") {
                draw_y = y;
                shiftY = 0;
            } else {
                if (lastDrawY != sy)
                    draw_y = (float) Math.min(lastDrawY - lastY + y, sy);
                else {
                    shiftY = (int) (sy - y);
                }
            }
        }

        lastDrawY = draw_y;
        lastY = y;

        if (status != "GTF" && status != "FLU" && status != "RCV" && status != "FLD" && savedstatus != "UPD") {
            if (shiftX != box) {
                shiftX = Math.min(shiftX + box / 8, box);
            }
        } else {
            if (shiftX != 0) {
                shiftX = Math.max(shiftX - box / 8, 0);
            }
        }


        //canvas.drawBitmap(bitmap, x, draw_y, paint);

        //paint.setColor(Color.BLACK);

        float strtY = MainActivity.end - boxAndSide;


        paint.setColor(Color.BLACK);

        paint.setTextSize((float) (dw / 30.0));


        draw_penguin(paint, canvas, draw_y);
        if (MainActivity.ask_on) {
            d = new Date();
        /*if (MainActivity.ask_number!=-1 &&  MainActivity.ask_number!=7){
            if (MainActivity.ask_status[MainActivity.ask_number]==status ){//если подсказку нужно отобразить*/
            if (status.equals("GTF")) {
                //if (start_date.getTime() != MainActivity.first_date.getTime()) {
                start_date = new Date();
                // }
            }
            if (status.equals("RCV")) {
                // if (break_date.getTime() != MainActivity.first_date.getTime()) {
                break_date = new Date();
                //}
            }
            if (status.equals("STF")) {
                if (savedstatus.equals("UPD"))
                    savedate = new Date();
            }
        } else {
            anima_switch();
        }


        if (MainActivity.energy_show) {
            paint.setColor(Color.GREEN);
            canvas.drawRect(boxAndSide, strtY + (float) dw / 8 + (float) dw / 200, boxAndSide + (float) (((float) dw / 2 - (float) dw / 25) * energy) / maxenergy, strtY + box - (float) dw / 200, paint);
            paint.setColor(Color.BLACK);
            canvas.drawText(con.getString(R.string.energy) + ": " + String.valueOf(energy) + " / " + String.valueOf(maxenergy), (float) dw / 2 - (float) dw / 8, strtY + (float) (dw * 3) / 16, paint);

        } else {

        }

        if (!MainActivity.setup) {
            paint.setColor(Color.BLACK);
            paint.setTextSize((float) (dw / 15.0));

            if ((status.equals("FLU") || status.equals("FLD")) && pen_coord > 0) {
                if (pen_coord < 1)
                    canvas.drawText(roundd(pen_coord * 100, 2) + con.getString(R.string.santimeter), (float) dw / 2 - (float) dh / 20, draw_y - (float) dw / 15, paint);
                else if (pen_coord < 1000)
                    canvas.drawText(roundd(pen_coord, 2) + con.getString(R.string.meter), (float) dw / 2 - (float) dh / 20, draw_y - (float) dw / 15, paint);
                else if (pen_coord < 1000000)
                    canvas.drawText(roundd(pen_coord / 1000, 2) + con.getString(R.string.kilometer), (float) dw / 2 - (float) dh / 20, draw_y - (float) dw / 15, paint);
            } else if (status.equals("GTF")) {
                if (start_date.getTime() != MainActivity.first_date.getTime()) {
                    canvas.drawText(con.getString(R.string.start) + ": " + (5 - (new Date().getTime() - start_date.getTime()) / 1000) + con.getString(R.string.sec), (float) dw / 2 - (float) (4 * dw) / 15, draw_y - (float) dw / 15, paint);
                }
            } else if (status.equals("RTF")) {
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
//        if (MainActivity.Fly.getVisibility() == View.VISIBLE) {
//            canvas.drawText(con.getString(R.string.skip), MainActivity.Fly.getX() + shiftX, (float) (MainActivity.Fly.getY() - dw * 1.5 / 25), paint);
//            canvas.drawText(con.getString(R.string.small_skip), MainActivity.Fly.getX() + shiftX, (float) (MainActivity.Fly.getY() - dw * 0.5 / 25), paint);
//        }
//        if (MainActivity.Reward.getVisibility() == View.VISIBLE) {
//            canvas.drawText(con.getString(R.string.skip), MainActivity.Reward.getX() + shiftX, MainActivity.Reward.getY() + (float) (dw * 1) / 25, paint);
//            canvas.drawText(con.getString(R.string.big_skip), MainActivity.Reward.getX() + shiftX, MainActivity.Reward.getY() + (float) (dw * 2) / 25, paint);
//            canvas.drawText(con.getString(R.string.rew_allert), MainActivity.Reward.getX() + shiftX, MainActivity.Reward.getY() + (float) (dw * 3) / 25, paint);
//            canvas.drawText(con.getString(R.string.rew_allert2), MainActivity.Reward.getX() + shiftX, MainActivity.Reward.getY() + (float) (dw * 4) / 25, paint);
//        }
//        float strtY = MainActivity.end - dw / 4f - dw / 50f;//MainActivity.Setup.getY();
//        paint.setColor(Color.GREEN);
//        canvas.drawRect((float) dw / 4 + (float) dw / 50, draw_y-dw/8, (float) dw / 4 + (float) dw / 50 + ((float) dw / 2 - (float) dw / 25) * (60 - 25 +  1) / 60, draw_y-dw/16, paint);
//
//        paint.setColor(Color.YELLOW);
//        canvas.drawRect((float) dw / 4 + (float) dw / 50 + ((float) dw / 2 - (float) dw / 25) * (60 - 25 + 1) / 60, draw_y-dw/8, (float) dw / 4 + (float) dw / 50 + ((float) dw / 2 - (float) dw / 25) * (60 - 25 + 15 + 1) / 60, draw_y-dw/16, paint);
//
//        paint.setColor(Color.BLACK);


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
