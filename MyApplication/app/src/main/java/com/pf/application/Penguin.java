package com.pf.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.view.View;

import java.util.Date;

public class Penguin {
    protected float x; // координаты
    protected float y;
    protected float sy;
    protected float maxY;
    protected float saveY;
    protected float size; // размер
    protected float speed; // скорость
    //protected float money; //деньги
    protected byte energy; //энергия
    protected byte maxenergy; //энергия
    protected float bust;
    protected float jump;
    protected float grav;
    protected boolean canfly=true;
    protected int bitmapId; // id картинки
    protected Bitmap bitmap; // картинка
    protected Bitmap bitmap1; // картинка
    protected boolean isfly=false;
    protected float[] time_to_up;
    protected Date savedate;
    protected Date d;
    protected float to_update=-1;
    protected Context con;

    protected float[] bust_up;//=new float[]{(float) 0.05,(float) 0.05,(float) 0.1,(float) 0.1,1800,3600,7200};
    protected byte[] energy_up;//=new float[]{(float) 1,(float) 2,(float) 3,(float)3,3,3,7};
    protected float[] jump_up;//=new float[]{(float) 0.1,(float) 0.1,(float) 0.2,(float) 0.4,1800,3600,7200};

    protected float[] jump_record = new float[]{(float)0,(float) 0.145, (float) 0.185, (float) 0.23, (float) 0.335, (float) 0.46, (float) 0.75, (float) 1.0};
    protected float[] bust_record = new float[]{(float) 0.68, (float) 1.51, (float) 1.69, (float) 1.91, (float) 2.19, (float) 2.59, (float) 3.16,(float)3.88,(float)6.93,(float)12,19,57,133};
    protected float[] energy_record = new float[]{0, (float) 5.68, (float) 6.3, (float) 10.7, (float) 18.16, (float) 23.87, (float) 30.19, (float) 40.7};

    protected byte ml_bust=12;
    protected byte ml_jump=8;
    protected byte ml_energy=8;

    protected byte next_jump;
    protected byte next_bust;
    protected byte next_energy;
    //protected String text;
    protected byte anim_type=0;
    protected byte anim_step=0;
    protected Bitmap[] anim= new Bitmap[7]; // перечень изображений для анимации
    protected float pen_coord=0;
    protected String status="RTF";
    protected String savedstatus="NON";
    /*
    NON - отсутствует (есть только у сохраняемого статуса)
    STF - полёт заблокирован
    UPD - происходит улучшение
    RCV - восстанавливается энергия
    RTF-ready to fly когда стоит и готов лететь
    GTF - подготовка к полёту (обратный отсчёт)
    FLU - полёт вверх
    FLD - падение

     */
    //protected Animation[] fly_an=new Animation[7];

    public int dw,dh;

    /*public Penguine(Context context) {
        size = 5;
        dw= MainActivity.dw;
        dh=MainActivity.dh;
        x=dw*3/8;
        y=dh-dw/2-dw/7;
        maxY=0;
        saveY=0;
        sy=y;
        speed = 0;


        jump_up=new float[]{0,(float)0.07,(float) 0.08,(float) 0.09,(float) 0.11,(float) 0.13,(float)0.16,(float)0.19,(float)0.23};
        bust_up=new float[]{0,(float) 0.01,(float) 0.02,(float) 0.03,(float) 0.04,(float)0.05,(float)0.06,(float)0.07,(float) 0.09,(float) 0.11,(float) 0.13,(float) 0.17, (float) 0.21};
        energy_up=new byte[]{0,5, 6,7,8,10,12,15,19};
        time_to_up=new byte[]{5,5,10,15,15,30,30,30,60,60,60,90,90,120};

        if (MainActivity.testing) {
            jump_up=new float[]{0,(float)0.07,(float) 0.08,(float) 0.09,(float) 0.11,(float) 0.13,(float)0.16,(float)0.19,(float)0.23};
            bust_up=new float[]{0,(float) 0.01,(float) 0.02,(float) 0.03,(float) 0.04,(float)0.05,(float)0.06,(float)0.07,(float) 0.09,(float) 0.11,(float) 0.13,(float) 0.17, (float) 0.21};
            energy_up=new byte[]{0,5, 6,7,8,10,12,15,19};

            jump_record = new float[]{0,(float)0,(float) 0, (float) 0, (float) 0, (float) 0, (float) 0, (float) 0, (float) 0};
            bust_record = new float[]{(float) 0, (float) 0, (float) 0, (float) 0, (float) 0, (float) 0, (float) 0,0,0,0,0,0};
            energy_record = new float[]{0,(float) 0, (float) 0, (float) 0, (float) 0, (float) 0, (float) 0, (float) 0,0};

            time_to_up=new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0};

            next_jump=8;
            next_bust=12;
            next_energy=8;
        }

        bust = bust_up[next_bust];
        jump= jump_up[next_jump];
        maxenergy=energy_up[next_energy];
        energy=maxenergy;

        grav=(float) 0.015;


        //money=0;



        init(context); // инициализация


    }*/

    public Penguin(Context context, byte n_j, byte n_b, byte n_e, float rec,Date time,float tu ) {
        size = 5;
        dw= MainActivity.dw;
        dh=MainActivity.dh;
        x= (float) (dw*3/8.0);
        x=dw/2-dw/4;
        //y= (float) (dh-dw/2.0-dw/7.0-dw/7);
        y=MainActivity.end-dw/4-dw/25-dw/2;//MainActivity.Setup.getY()+dh/2;
        maxY=0;
        saveY=0;
        sy=y;
        speed = 0;
        con=context;
        strong=0;
        jump_up=new float[]{0,(float)0.07,(float) 0.08,(float) 0.09,(float) 0.11,(float) 0.13,(float)0.16,(float)0.19,(float)0.23};
        bust_up=new float[]{0,(float) 0.01,(float) 0.02,(float) 0.03,(float) 0.04,(float)0.05,(float)0.06,(float)0.07,(float) 0.09,(float) 0.11,(float) 0.13,(float) 0.17, (float) 0.21};
        energy_up=new byte[]{0,5, 6,7,8,10,12,15,19};
        time_to_up=new float[]{5,5,10,15,15,30,30,30,60,60,60,90,90,120,150,180,180,210,210,210,240,240,300,360,360};

        if (MainActivity.testing) {
            jump_up=new float[]{0,(float)0.07,(float) 0.08,(float) 0.09,(float) 0.11,(float) 0.13,(float)0.16,(float)0.19,(float)0.23};
            bust_up=new float[]{0,(float) 0.01,(float) 0.02,(float) 0.03,(float) 0.04,(float)0.05,(float)0.06,(float)0.07,(float) 0.09,(float) 0.11,(float) 0.13,(float) 0.17, (float) 0.21};
            energy_up=new byte[]{0,5, 6,7,8,10,12,15,19};

            jump_record = new float[]{0,(float)0,(float) 0, (float) 0, (float) 0, (float) 0, (float) 0, (float) 0, (float) 0};
            bust_record = new float[]{(float) 0, (float) 0, (float) 0, (float) 0, (float) 0, (float) 0, (float) 0,0,0,0,0,0};
            energy_record = new float[]{0,(float) 0, (float) 0, (float) 0, (float) 0, (float) 0, (float) 0, (float) 0,0};

            time_to_up=new float[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0};

            next_jump=8;
            next_bust=12;
            next_energy=8;
        }
        else
        {
            next_jump=n_j;
            next_bust=n_b;
            next_energy=n_e;
            maxY=rec;
            savedate=time;
            to_update=tu;
        }

        bust = bust_up[next_bust];
        jump= jump_up[next_jump];
        maxenergy=energy_up[next_energy];
        energy=maxenergy;
        if(next_bust>0)MainActivity.energy_show=true;

        grav=(float) 0.015;


        //money=0;



        init(context); // инициализация


    }

    void init(Context context) { // сжимаем картинку до нужных размеров
        start_date=MainActivity.first_date;
        bitmapId = R.drawable.p0;//pen1; // определяем начальные параметры

        Bitmap cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        bitmap = Bitmap.createScaledBitmap(
                cBitmap, dw/2, dw/2, false);
        cBitmap.recycle();

        bitmapId = R.drawable.pen1_fly; // определяем начальные параметры
        cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        bitmap1 = Bitmap.createScaledBitmap(
                cBitmap, dw/2, dw/2, false);
        cBitmap.recycle();

        bitmapId = R.drawable.p0;//f0;// определяем начальные параметры
        cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        anim[0] = Bitmap.createScaledBitmap(
                cBitmap, dw/2, dw/2, false);
        cBitmap.recycle();

        bitmapId = R.drawable.p1;//f1;// определяем начальные параметры
        cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        anim[1] = Bitmap.createScaledBitmap(
                cBitmap, dw/2, dw/2, false);
        cBitmap.recycle();
        anim[6] = anim[1];
        //cBitmap.recycle();

        bitmapId = R.drawable.p1;//f2;// определяем начальные параметры
        cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        anim[2] = Bitmap.createScaledBitmap(
                cBitmap, dw/2, dw/2, false);
        cBitmap.recycle();
        anim[5] = anim[2];
        //cBitmap.recycle();

        bitmapId = R.drawable.p2;//f3;// определяем начальные параметры
        cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        anim[3] = Bitmap.createScaledBitmap(
                cBitmap, dw/2, dw/2, false);
        cBitmap.recycle();
        anim[4] = anim[3];

        bitmapId = R.drawable.fone_red;
        cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        fone_red = Bitmap.createScaledBitmap(
                cBitmap, dw/4+dw/50, dw/4+dw/50, false);
        cBitmap.recycle();

        bitmapId = R.drawable.fone_green;
        cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        fone_green = Bitmap.createScaledBitmap(
                cBitmap, dw/4+dw/50, dw/4+dw/50, false);
        cBitmap.recycle();

        bitmapId = R.drawable.fone_white;
        cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        fone_white = Bitmap.createScaledBitmap(
                cBitmap, dw/4+dw/50, dw/4+dw/50, false);
        cBitmap.recycle();
    }

    public void update(){ // тут будут вычисляться новые координаты
       // if (rg==0)justfly();
        //else newfly();
        newfly();
        //justfly();
       // anima_fly();
    }

    private byte strong=0;
    private boolean start=true;
    public Date start_date;

 /*   void newfly(){
        //strong=50;
        if(MainActivity.flying){//если включено событие - нажата кнопка
            if(!MainActivity.setup) {
                if(canfly){//и разрешён полёт
                    if (start && (start_date.getTime() == MainActivity.first_date.getTime()) ) {
                        start_date = new Date();
                        //strong=100;
                    }//если нажатие первое, запускаем таймер
                    strong = (byte) (Math.min(strong + 7, 100));  //прибавляем силу
                }
            }
            else {
                if (MainActivity.update!=-1) {
                    if (to_update != -1){
                        to_update-=0.1;
                    }
                     }
            }
            MainActivity.flying = false; //отключаем событие, чтобы кнопка вновь могла сработать
        }

        if (strong>0) {
            //strong=100;
           if(start) {//если стоим внизу и можем взлетать
               d = new Date();
               if (5 <= (float)(d.getTime() - start_date.getTime()) / 1000) {
                   speed += jump*strong/100;
                   start=false;
                   if (bust>0){
                       anim_type=1;
                   anim_step=0;
                   }
               }
           }
           else {//если в воздухе
               if (speed>0){//если летим вверх
                   if(bust>0 && energy>0){//если можем махать крыльями
                       anim_step++;
                       if (anim_step>6){
                           //anim_type=0;
                           anim_step=1;
                       }
                       if (anim_step==3){
                           speed+=bust*strong/100;
                           energy-=1;}
                   }
                   else {//если не можем махать крыльями
                       if (anim_type==1) {//если включена анимация взмаха крыльями
                           anim_step++;
                           if (anim_step > 6) {
                               anim_type=2;//переключаемся в режим "полёт вверх"
                               anim_step = 0;
                               canfly=false;
                               strong=0;
                           }
                       }
                       if (anim_type==3) {//если включена анимация прыжка
                           anim_step++;
                           if (anim_step > 6) {
                               anim_type=2;//переключаемся в режим "полёт вверх"
                               anim_step = 0;
                               canfly=false;
                               strong=0;
                           }
                       }

                       if (anim_type==2) {//если включена анимация "полёт вверх"
                           anim_step++;
                           if (anim_step > 6) {
                               anim_step = 0;
                           }
                       }

                   }
               }
           }
            strong= (byte) (Math.max(strong - 1, 0));

        }
        else{
            if(start) {
                if(start_date.getTime() != MainActivity.first_date.getTime()){
                    d = new Date();
                    if (5 <= (float)(d.getTime() - start_date.getTime()) / 1000){
                        start_date = MainActivity.first_date;
                    }
                }
            }
        }

        if(pen_coord>0 || speed>0){ //если выше земли или скорость больше 0 (в момент прыжка)

            //if(speed<0) canfly=false;//если уже падаем, блокируем возможность полёта

            pen_coord += speed; //иначе меняем высоту
            speed-=grav; //уменьшаем скорость на притяжение

            if (pen_coord<=0) {
                pen_coord=0; //если будем ниже земли, встаём на землю
                speed=0;
            }

            y=sy-pen_coord*dw/3;

            if (saveY<pen_coord) saveY=pen_coord;
        }
        else{
            //speed=0;
            //money = money + ((float)Math.round(saveY * 100)) / 100;
            if (saveY!=0&&maxY<saveY) {
                maxY = saveY;
                save();
            }
            saveY=0;
            pen_coord=0;
            anim_type=0;
            y=sy;
            if(!start)
            {if(energy<maxenergy) energy+=1;
            else {canfly=true;
            strong=0;
            start=true;
            start_date=MainActivity.first_date;
            }}
            if (MainActivity.update!=-1) {
                if (to_update==-1){
                    switch (MainActivity.update) {
                        case 0:
                            if (next_jump < ml_jump) {
                                if (jump_record[next_jump] <= maxY) {
                                    to_update = time_to_up[next_jump];
                                    savedate = new Date();
                                    d = new Date();
                                    //MainActivity.Up_jump.setText("Ускорить");
                                }
                                else MainActivity.update = -1;
                            }
                            else MainActivity.update = -1;
                            break;
                        case 1:
                            if (next_bust<ml_bust){
                                if (bust_record[next_bust] <= maxY) {
                                    to_update = time_to_up[next_bust+5];
                                    savedate = new Date();
                                    d = new Date();

                                }
                                else MainActivity.update = -1;
                            }
                            else MainActivity.update = -1;
                            break;
                        case 2:
                            if (next_energy<ml_energy) {
                                if (energy_record[next_energy] <= maxY) {
                                    to_update = time_to_up[next_energy+10];
                                    savedate = new Date();
                                    d = new Date();
                                }
                                else MainActivity.update = -1;
                            }
                            else MainActivity.update = -1;
                            break;
                    }
                    //MainActivity.Fly.setVisibility(View.VISIBLE);
                    //if(MainActivity.mRewardedAd != null) MainActivity.Reward.setVisibility(View.VISIBLE);
                    save();

                }
                else {
                    d=new Date();
                    //to_update-=(d.getTime()-savedate.getTime())/1000;
                    if (to_update<=(float)(d.getTime()-savedate.getTime())/1000) {
                        switch (MainActivity.update) {
                            case 0:
                                to_update=-1;
                                next_jump++;
                                jump=jump_up[next_jump];
                                MainActivity.update=-1;


                                break;
                            case 1:
                                to_update=-1;
                                next_bust++;
                                bust=bust_up[next_bust];
                                MainActivity.update=-1;
                                MainActivity.energy_show=true;
                                break;
                            case 2:
                                to_update=-1;
                                next_energy++;
                                maxenergy=energy_up[next_energy];
                                MainActivity.update=-1;
                                canfly=false;
                                start=false;
                                break;
                        }
                        save();
                        try{
                        MainActivity.Fly.setVisibility(View.INVISIBLE);
                        MainActivity.Reward.setVisibility(View.INVISIBLE);
                        MainActivity.Setup.setImageResource(R.drawable.back_b);}
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
        }
    }*/

    void newfly(){
        //strong=50;
        if(MainActivity.flying){//если включено событие - нажата кнопка
            switch (status){
                case  "RTF":
                    start_date = new Date();
                    status="GTF";
                    break;
                case "GTF" : case "FLU":
                    strong = (byte) (Math.min(strong + 7, 100));  //прибавляем силу
                    break;
                case "UPD":
                    if (to_update != -1){
                        to_update-=0.1;
                    }
                    break;
            }
            MainActivity.flying = false; //отключаем событие, чтобы кнопка вновь могла сработать
        }

        if (strong>0) {
            //strong=100;
            if(start) {//если стоим внизу и можем взлетать
                d = new Date();
                if (5 <= (float)(d.getTime() - start_date.getTime()) / 1000) {
                    speed += jump*strong/100;
                    start=false;
                    if (bust>0){
                        anim_type=1;
                        anim_step=0;
                    }
                }
            }
            else {//если в воздухе
                if (speed>0){//если летим вверх
                    if(bust>0 && energy>0){//если можем махать крыльями
                        anim_step++;
                        if (anim_step>6){
                            //anim_type=0;
                            anim_step=1;
                        }
                        if (anim_step==3){
                            speed+=bust*strong/100;
                            energy-=1;}
                    }
                    else {//если не можем махать крыльями
                        if (anim_type==1) {//если включена анимация взмаха крыльями
                            anim_step++;
                            if (anim_step > 6) {
                                anim_type=2;//переключаемся в режим "полёт вверх"
                                anim_step = 0;
                                canfly=false;
                                strong=0;
                            }
                        }
                        if (anim_type==3) {//если включена анимация прыжка
                            anim_step++;
                            if (anim_step > 6) {
                                anim_type=2;//переключаемся в режим "полёт вверх"
                                anim_step = 0;
                                canfly=false;
                                strong=0;
                            }
                        }

                        if (anim_type==2) {//если включена анимация "полёт вверх"
                            anim_step++;
                            if (anim_step > 6) {
                                anim_step = 0;
                            }
                        }

                      /* anim_type=0;
                       anim_step=0;
                       canfly=false;
                       strong=0;*/
                    }
                }
            }
            strong= (byte) (Math.max(strong - 1, 0));

        }
        else{
            if(start) {
                if(start_date.getTime() != MainActivity.first_date.getTime()){
                    d = new Date();
                    if (5 <= (float)(d.getTime() - start_date.getTime()) / 1000){
                        start_date = MainActivity.first_date;
                    }
                }
            }
        }

        if(pen_coord>0 || speed>0){ //если выше земли или скорость больше 0 (в момент прыжка)

            //if(speed<0) canfly=false;//если уже падаем, блокируем возможность полёта

            pen_coord += speed; //иначе меняем высоту
            speed-=grav; //уменьшаем скорость на притяжение

            if (pen_coord<=0) {
                pen_coord=0; //если будем ниже земли, встаём на землю
                speed=0;
            }

            y=sy-pen_coord*dw/3;

            if (saveY<pen_coord) saveY=pen_coord;
        }
        else{
            //speed=0;
            //money = money + ((float)Math.round(saveY * 100)) / 100;
            if (saveY!=0&&maxY<saveY) {
                maxY = saveY;
                save();
            }
            saveY=0;
            pen_coord=0;
            anim_type=0;
            y=sy;
            if(!start)
            {if(energy<maxenergy) energy+=1;
            else {canfly=true;
                strong=0;
                start=true;
                start_date=MainActivity.first_date;
            }}
            if (MainActivity.update!=-1) {
                if (to_update==-1){
                    switch (MainActivity.update) {
                        case 0:
                            if (next_jump < ml_jump) {
                                if (jump_record[next_jump] <= maxY) {
                                    to_update = time_to_up[next_jump];
                                    savedate = new Date();
                                    d = new Date();
                                    //MainActivity.Up_jump.setText("Ускорить");
                                }
                                else MainActivity.update = -1;
                            }
                            else MainActivity.update = -1;
                            break;
                        case 1:
                            if (next_bust<ml_bust){
                                if (bust_record[next_bust] <= maxY) {
                                    to_update = time_to_up[next_bust+5];
                                    savedate = new Date();
                                    d = new Date();

                                }
                                else MainActivity.update = -1;
                            }
                            else MainActivity.update = -1;
                            break;
                        case 2:
                            if (next_energy<ml_energy) {
                                if (energy_record[next_energy] <= maxY) {
                                    to_update = time_to_up[next_energy+10];
                                    savedate = new Date();
                                    d = new Date();
                                }
                                else MainActivity.update = -1;
                            }
                            else MainActivity.update = -1;
                            break;
                    }
                    //MainActivity.Fly.setVisibility(View.VISIBLE);
                    //if(MainActivity.mRewardedAd != null) MainActivity.Reward.setVisibility(View.VISIBLE);
                    save();

                }
                else {
                    d=new Date();
                    //to_update-=(d.getTime()-savedate.getTime())/1000;
                    if (to_update<=(float)(d.getTime()-savedate.getTime())/1000) {
                        switch (MainActivity.update) {
                            case 0:
                                to_update=-1;
                                next_jump++;
                                jump=jump_up[next_jump];
                                MainActivity.update=-1;


                                break;
                            case 1:
                                to_update=-1;
                                next_bust++;
                                bust=bust_up[next_bust];
                                MainActivity.update=-1;
                                MainActivity.energy_show=true;
                                break;
                            case 2:
                                to_update=-1;
                                next_energy++;
                                maxenergy=energy_up[next_energy];
                                MainActivity.update=-1;
                                canfly=false;
                                start=false;
                                break;
                        }
                        save();
                        try{
                            MainActivity.Fly.setVisibility(View.INVISIBLE);
                            MainActivity.Reward.setVisibility(View.INVISIBLE);
                            MainActivity.Setup.setImageResource(R.drawable.back_b);}
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
        }
    }


/*    void justfly(){
        if(MainActivity.flying && canfly){//если включено событие - нажата кнопка и разрешён полёт
            if(pen_coord==0)  {
                if(!MainActivity.setup) speed+=jump;
                energy-=1;
            } //если стоит на земле, то ускорение прыжком
            else if (bust>0){
                speed+= bust; //если уже летит, то ускорение взмахами
                energy-=1;
            }
            MainActivity.flying=false; //отключаем событие, чтобы кнопка вновь могла сработать
            if (energy<1) canfly = false; //если энергия кончилась, блокируем полёт
            else {
                if(anim_type==1) {//если уже анимируется полёт
                    switch (anim_step) {
                        case 1:
                            anim_step = 3;
                            break;
                        case 2:
                            anim_step = 4;
                            break;
                        case 3:
                            anim_step = 2;
                            break;
                        case 4:
                            anim_step = 2;
                            break;
                        case 5:
                            anim_step = 1;
                            break;
                        case 6:
                            anim_step = 0;
                            break;
                    }
                }
                else{
                    anim_type=1;
                    anim_step=0;
                }
            }
            isfly=true;
        }
        if(pen_coord>0 || speed>0){ //если выше земли или скорость больше 0 (в момент прыжка)
            speed-=grav; //уменьшаем скорость на притяжение
            if(speed<0) canfly=false;//если уже падаем, блокируем возможность полёта
            if (speed<0 && pen_coord+speed<0) {
                pen_coord=0; //если будем ниже земли, встаём на землю
                speed=0;
            }
            else pen_coord += speed; //иначе меняем высоту

            //pen_coord=(sy-y)*3/dw;
            y=sy-pen_coord*dw/3;
            if (maxY<pen_coord&&maxY>0) {
                maxY = pen_coord; //если максимальн
                save();
            }
            if (saveY<pen_coord) saveY=pen_coord;
        }
        else{
            speed=0;
            //money = money + ((float)Math.round(saveY * 100)) / 100;
            if(maxY==0 & saveY!=0) {
                maxY = saveY;
                save();
            }
            saveY=0;
            pen_coord=0;
            y=sy;
            if(energy<maxenergy) energy+=1;
            else canfly=true;
            if (MainActivity.update!=-1) {
                if (to_update==-1){
                    switch (MainActivity.update) {
                        case 0:
                            if (next_jump < ml_jump) {
                                if (jump_record[next_jump] <= maxY) {
                                    to_update = time_to_up[next_jump];
                                    savedate = new Date();
                                    d = new Date();
                                    //MainActivity.Up_jump.setText("Ускорить");
                                }
                                else MainActivity.update = -1;
                            }
                            else MainActivity.update = -1;
                            break;
                        case 1:
                            if (next_bust<ml_bust){
                                if (bust_record[next_bust] <= maxY) {
                                    to_update = time_to_up[next_bust];
                                    savedate = new Date();
                                    d = new Date();

                                }
                                else MainActivity.update = -1;
                            }
                            else MainActivity.update = -1;
                            break;
                        case 2:
                            if (next_energy<ml_energy) {
                                if (energy_record[next_energy] <= maxY) {
                                    to_update = time_to_up[next_energy];
                                    savedate = new Date();
                                    d = new Date();
                                }
                                else MainActivity.update = -1;
                            }
                            else MainActivity.update = -1;
                            break;
                    }
                }
                else {
                    d=new Date();
                    //to_update-=(d.getTime()-savedate.getTime())/1000;
                    if (to_update<=(float)(d.getTime()-savedate.getTime())/1000) {
                        switch (MainActivity.update) {
                            case 0:
                                to_update=-1;
                                next_jump++;
                                jump=jump_up[next_jump];
                                MainActivity.update=-1;
                                break;
                            case 1:
                                to_update=-1;
                                next_bust++;
                                bust=bust_up[next_bust];
                                MainActivity.update=-1;
                                break;
                            case 2:
                                to_update=-1;
                                next_energy++;
                                maxenergy=energy_up[next_energy];
                                MainActivity.update=-1;
                                break;
                        }
                        save();
                    }
                }

            }
        }
    }
*/

    void drow(Paint paint, Canvas canvas){ // рисуем картинку
        float draw_y=y;
        if (y<=dh/8.0) draw_y= (float) (dh/8.0);


        //canvas.drawBitmap(bitmap, x, draw_y, paint);

        //paint.setColor(Color.BLACK);

        float strtY=MainActivity.Setup.getY();
        paint.setColor(Color.WHITE);
        canvas.drawRect(0,strtY-dw/100,dw,strtY+dw/4,paint);




        paint.setColor(Color.BLACK);

        paint.setTextSize((float) (dw / 30.0));

        if (maxY > 0){
            if (maxY<1000) canvas.drawText("Max: " + roundd(maxY, 2)+con.getString(R.string.meter), dw*3 /4, strtY+dw/20, paint);
            else if (maxY<1000000) canvas.drawText("Max: " + roundd(maxY/1000, 2)+con.getString(R.string.kilometer), dw*3/4, strtY+dw/20, paint);
        }
        //canvas.drawColor(Color.GREEN);




        if(!MainActivity.setup) {

            //if(isfly){
            if(anim_type==1) {//если уже анимируется полёт
                canvas.drawBitmap(anim[anim_step], x, draw_y, paint);
               /* anim_step++;
                if (anim_step>6){
                    anim_type=0;
                    anim_step=0;
                }*/
            }


                //canvas.drawBitmap(bitmap1, x, draw_y, paint);
                //isfly=false;
            //}

            else canvas.drawBitmap(bitmap, x, draw_y, paint);


            paint.setColor(Color.YELLOW);
            canvas.drawRect(dw/4+dw/50,strtY+dw/200,dw/4+dw/50+(dw/2-dw/25)*strong/100,strtY+dw/8-dw/200,paint);

            if (MainActivity.energy_show) {
                paint.setColor(Color.GREEN);
                canvas.drawRect(dw / 4 + dw / 50, strtY + dw / 8 + dw / 200, dw / 4 + dw / 50 + (dw / 2 - dw / 25) * energy / maxenergy, strtY + dw / 4-dw/200, paint);
            }
            paint.setColor(Color.BLACK);

            canvas.drawText(con.getString(R.string.concentration)+": "+String.valueOf(strong), dw / 4 +dw / 25, strtY+dw/16, paint);
            if (MainActivity.energy_show)canvas.drawText(con.getString(R.string.energy)+": "+String.valueOf(energy) + " из " + String.valueOf(maxenergy), dw / 4 +dw / 25, strtY +dw*3/16, paint);




            paint.setTextSize((float) (dw / 15.0));

            if (pen_coord >0){
                if (pen_coord<1000) canvas.drawText(roundd(pen_coord, 2)+con.getString(R.string.meter), dw / 2 - dh / 20, draw_y-dh / 50, paint);
                else if (pen_coord<1000000) canvas.drawText(roundd(pen_coord/1000, 2)+con.getString(R.string.kilometer), dw / 2 - dh / 20, draw_y-dh / 50, paint);
                //if (speed<1000) canvas.drawText(String.valueOf(roundd(speed, 2))+"м/c", dw / 2 - dh / 30, draw_y-dh *2/30, paint);
                //else if (speed<1000000) canvas.drawText(String.valueOf(roundd(speed/1000, 2))+"км/c", dw / 2 - dh / 30, draw_y-dh*2 / 30, paint);

            }
            else if(start) {
                if (start_date.getTime() != MainActivity.first_date.getTime()) {
                    //d=new Date();
                    canvas.drawText(con.getString(R.string.start)+": " + (5-(new Date().getTime() - start_date.getTime()) / 1000) + con.getString(R.string.sec), dw / 2 - 4*dw / 10, draw_y - dw / 25, paint);
                }
                else{
                    boolean can_update=false;
                    if(next_jump<ml_jump)
                        if (jump_record[next_jump] <= maxY)
                            can_update=true;
                    if(next_bust<ml_bust)
                        if (bust_record[next_bust] <= maxY)
                            can_update=true;
                    if(MainActivity.energy_show && next_energy<ml_energy)
                        if (energy_record[next_energy] <= maxY)
                            can_update=true;
                    if (can_update)
                    {
                        if (GameView.tick%90<45)canvas.drawBitmap(fone_green, MainActivity.Setup.getX()-dw/100, MainActivity.Setup.getY()-dw/100, paint);
                    }
                }
            }

            //canvas.drawText(String.valueOf(roundd(money, 1)), dw - dh / 8, dh / 15, paint);
           /* if (to_update != -1) {
                paint.setTextSize(dh / 30);

                canvas.drawText(String.valueOf(to_update - (d.getTime() - savedate.getTime()) / 1000), dw / 2 - dh / 15, dw / 2 + dw / 10, paint);
                //canvas.drawText(String.valueOf(text), dw / 2 - dh / 15, dw / 2 + dw / 10 + dh / 30, paint);


            }*/
        }
        else{
            draw_setup(paint,canvas,draw_y);
        }



        //canvas.drawText(String.valueOf(MainActivity.update),300, 450,paint);

    }

    protected Bitmap fone_red; // картинка
    protected Bitmap fone_green; // картинка
    protected Bitmap fone_white; // картинка

    private void draw_setup(Paint paint, Canvas canvas,float draw_y) {
        if (isfly) {
            isfly = false;
        }

        canvas.drawBitmap(bitmap, x, draw_y, paint);

        paint.setTextSize(dw / 28);//dh/50);
        float shiftX = dh / 180;

        canvas.drawText(con.getString(R.string.jump_power), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw / 25, paint);
        canvas.drawText((int) roundd(jump * 100, 0) + " (" + String.valueOf(next_jump) + "lvl)", MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 2 / 25, paint);

        canvas.drawText(con.getString(R.string.boost_power), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw / 25, paint);
        canvas.drawText((int) roundd(bust * 100, 0) + " (" + String.valueOf(next_bust) + "lvl)", MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 2 / 25, paint);
        if (MainActivity.energy_show) {
            canvas.drawText(con.getString(R.string.energy), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw / 25, paint);
            canvas.drawText((int) roundd(maxenergy, 0) + " (" + String.valueOf(next_energy) + "lvl)", MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 2 / 25, paint);
        }
        if ( MainActivity.Fly.getVisibility()==View.VISIBLE) {
            canvas.drawText(con.getString(R.string.skip), MainActivity.Fly.getX() + shiftX, MainActivity.Fly.getY() + dw * 1 / 25, paint);
            canvas.drawText(con.getString(R.string.small_skip), MainActivity.Fly.getX() + shiftX, MainActivity.Fly.getY() + dw * 2 / 25, paint);
        }
        if(MainActivity.Reward.getVisibility()==View.VISIBLE) {
                canvas.drawText(con.getString(R.string.skip), MainActivity.Reward.getX() + shiftX, MainActivity.Reward.getY() + dw * 1 / 25, paint);
                canvas.drawText(con.getString(R.string.big_skip), MainActivity.Reward.getX() + shiftX, MainActivity.Reward.getY() + dw * 2 / 25, paint);
                canvas.drawText(con.getString(R.string.rew_allert), MainActivity.Reward.getX() + shiftX, MainActivity.Reward.getY() + dw * 3 / 25, paint);
            canvas.drawText(con.getString(R.string.rew_allert2), MainActivity.Reward.getX() + shiftX, MainActivity.Reward.getY() + dw * 4 / 25, paint);

        }

        if (MainActivity.update==0&& to_update!=-1){
            //paint.setColor(Color.GREEN);
            canvas.drawBitmap(fone_white, MainActivity.Up_jump.getX()-dw/100, MainActivity.Up_jump.getY()-dw/100, paint);

            canvas.drawText(con.getString(R.string.updating) , MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 3 / 25, paint);
            canvas.drawText(con.getString(R.string.progress), MainActivity.Up_jump.getX()+shiftX, MainActivity.Up_jump.getY()+dw*4/25, paint);
            canvas.drawText(roundd(to_update - (float)(d.getTime() - savedate.getTime()) / 1000,2)+con.getString(R.string.sec), MainActivity.Up_jump.getX()+shiftX, MainActivity.Up_jump.getY()+dw*5/25, paint);
            canvas.drawText(con.getString(R.string.left) , MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 6 / 25, paint);
            paint.setColor(Color.BLACK);
        }
        else {

            if(next_jump<ml_jump){
                if (jump_record[next_jump] <= maxY) {
                    //paint.setColor(Color.BLUE);
                    if (GameView.tick%90<45)canvas.drawBitmap(fone_green, MainActivity.Up_jump.getX()-dw/100, MainActivity.Up_jump.getY()-dw/100, paint);

                    canvas.drawText(con.getString(R.string.updating) , MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 3 / 25, paint);
                    canvas.drawText(con.getString(R.string.available), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 4 / 25, paint);
                    canvas.drawText(con.getString(R.string.take), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 5 / 25, paint);
                    canvas.drawText(String.valueOf(time_to_up[next_jump]) + con.getString(R.string.sec), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 6 / 25, paint);
                } else {

                    canvas.drawBitmap(fone_red, MainActivity.Up_jump.getX()-dw/100, MainActivity.Up_jump.getY()-dw/100, paint);
                    //paint.setColor(Color.rgb(255,156,0));
                    canvas.drawText(con.getString(R.string.updating) , MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 3 / 25, paint);
                    canvas.drawText(con.getString(R.string.not_available), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 4 / 25, paint);
                    canvas.drawText(con.getString(R.string.record), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 5 / 25, paint);
                    canvas.drawText(jump_record[next_jump] + con.getString(R.string.meter), MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dw * 6 / 25, paint);
                }
                paint.setColor(Color.BLACK);
            }

        }
        if (MainActivity.update==1&& to_update!=-1){
            //paint.setColor(Color.GREEN);
            canvas.drawBitmap(fone_white, MainActivity.Up_fly.getX()-dw/100, MainActivity.Up_fly.getY()-dw/100, paint);
            canvas.drawText(con.getString(R.string.updating), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 3 / 25, paint);
            canvas.drawText(con.getString(R.string.progress), MainActivity.Up_fly.getX()+shiftX, MainActivity.Up_fly.getY()+dw*4/25, paint);
            canvas.drawText(roundd(to_update - (float)(d.getTime() - savedate.getTime()) / 1000,2)+con.getString(R.string.sec), MainActivity.Up_fly.getX()+shiftX, MainActivity.Up_fly.getY()+dw*5/25, paint);
            canvas.drawText(con.getString(R.string.left) , MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 6 / 25, paint);
            paint.setColor(Color.BLACK);
        }
        else {
            if(next_bust<ml_bust){
                if (bust_record[next_bust] <= maxY) {
                    if (GameView.tick%90<45)canvas.drawBitmap(fone_green, MainActivity.Up_fly.getX()-dw/100, MainActivity.Up_fly.getY()-dw/100, paint);

                    //paint.setColor(Color.BLUE);
                    canvas.drawText(con.getString(R.string.updating) , MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 3 / 25, paint);
                    canvas.drawText(con.getString(R.string.available), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 4 / 25, paint);
                    canvas.drawText(con.getString(R.string.take), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 5 / 25, paint);
                    canvas.drawText(String.valueOf(time_to_up[next_bust+5]) + con.getString(R.string.sec), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 6 / 25, paint);
                } else {
                    //paint.setColor(Color.rgb(255,156,0));
                    canvas.drawBitmap(fone_red, MainActivity.Up_fly.getX()-dw/100, MainActivity.Up_fly.getY()-dw/100, paint);
                    canvas.drawText(con.getString(R.string.updating) , MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 3 / 25, paint);
                    canvas.drawText(con.getString(R.string.not_available), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 4 / 25, paint);
                    canvas.drawText(con.getString(R.string.record), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 5 / 25, paint);
                    canvas.drawText(bust_record[next_bust] + con.getString(R.string.meter), MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dw * 6 / 25, paint);
                }
                paint.setColor(Color.BLACK);
            }
        }

        if (MainActivity.update==2&& to_update!=-1){
            //paint.setColor(Color.GREEN);
            canvas.drawBitmap(fone_white, MainActivity.Up_energy.getX()-dw/100, MainActivity.Up_energy.getY()-dw/100, paint);
            canvas.drawText(con.getString(R.string.updating), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 3 / 25, paint);
            canvas.drawText(con.getString(R.string.progress), MainActivity.Up_energy.getX()+shiftX, MainActivity.Up_energy.getY()+dw*4/25, paint);
            canvas.drawText(roundd(to_update - (float)(d.getTime() - savedate.getTime()) / 1000,2)+con.getString(R.string.sec), MainActivity.Up_energy.getX()+shiftX, MainActivity.Up_energy.getY()+dw*5/25, paint);
            canvas.drawText(con.getString(R.string.left) , MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 6 / 25, paint);
            paint.setColor(Color.BLACK);
        }
        else{
            if (MainActivity.energy_show) {
                if (next_energy < ml_energy) {
                    if (energy_record[next_energy] <= maxY) {
                        //paint.setColor(Color.BLUE);
                        if (GameView.tick%90<45)canvas.drawBitmap(fone_green, MainActivity.Up_energy.getX()-dw/100, MainActivity.Up_energy.getY()-dw/100, paint);
                        canvas.drawText(con.getString(R.string.updating), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 3 / 25, paint);
                        canvas.drawText(con.getString(R.string.available), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 4 / 25, paint);
                        canvas.drawText(con.getString(R.string.take), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 5 / 25, paint);
                        canvas.drawText(String.valueOf(time_to_up[next_energy+10]) + con.getString(R.string.sec), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 6 / 25, paint);
                    } else {
                        //paint.setColor(Color.rgb(255, 156, 0));
                        canvas.drawBitmap(fone_red, MainActivity.Up_energy.getX()-dw/100, MainActivity.Up_energy.getY()-dw/100, paint);
                        canvas.drawText(con.getString(R.string.updating), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 3 / 25, paint);
                        canvas.drawText(con.getString(R.string.not_available), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 4 / 25, paint);
                        canvas.drawText(con.getString(R.string.record), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 5 / 25, paint);
                        canvas.drawText(energy_record[next_energy] + con.getString(R.string.meter), MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dw * 6 / 25, paint);
                    }
                    paint.setColor(Color.BLACK);

                }

            }
        }

        /*if (to_update!=-1) {
            paint.setColor(Color.GREEN);
            canvas.drawRect(dw / 4 + dw / 50, strtY + dw / 8 + dw / 200, dw / 4 + dw / 50 + (dw / 2 - dw / 25) * energy / maxenergy, strtY + dw / 4-dw/200, paint);
        }*/
        paint.setColor(Color.BLACK);
    }

    public float roundd(float a,int b){

        return (float) (((float)Math.round(a * Math.pow(10,b))) / Math.pow(10,b));
    }

    public void save(){
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(con);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putInt("jump", next_jump);
        myEditor.putInt("energy", next_energy);
        myEditor.putInt("bust", next_bust);
        myEditor.putFloat("record", maxY);
        myEditor.putLong("strt_date",savedate.getTime());
        myEditor.putInt("update", MainActivity.update);
        myEditor.putFloat("time_to_up", to_update);
        myEditor.apply();
    }
}
