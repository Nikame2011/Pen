package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import java.util.Date;

public class Penguine {
    protected float x; // координаты
    protected float y;
    protected float sy;
    protected float maxY;
    protected float saveY;
    protected float size; // размер
    protected float speed; // скорость
    protected float money; //деньги
    protected float mass; //масса
    protected float energy; //энергия
    protected float maxenergy; //энергия
    protected float bust;
    protected float jump;
    protected boolean canfly=true;
    protected int bitmapId; // id картинки
    protected int bitmapId1; // id картинки
    protected Bitmap bitmap; // картинка
    protected Bitmap bitmap1; // картинка
    protected boolean isfly=false;
    protected boolean uping=false;
    protected int[] time_to_up=new int[]{1,1,1,1,1,1,1};
    protected Date savedate;
    protected Date d;
    protected int to_update=-1;

    protected float[] bust_up;//=new float[]{(float) 0.05,(float) 0.05,(float) 0.1,(float) 0.1,1800,3600,7200};
    protected float[] energy_up=new float[]{(float) 1,(float) 2,(float) 3,(float)3,3,3,7};
    protected float[] jump_up;//=new float[]{(float) 0.1,(float) 0.1,(float) 0.2,(float) 0.4,1800,3600,7200};
    protected float[] jump_record=new float[]{(float) 0.1,(float) 0.3,(float) 0.5,(float) 0.5,1,3,7};
    protected float[] bust_record=new float[]{(float) 0.3,(float) 0.3,(float) 0.5,(float) 0.5,1,3,7};
    protected float[] energy_record=new float[]{(float) 0.3,(float) 0.3,(float) 0.5,(float) 0.5,1,3,7};
    protected int next_jump=0;
    protected int next_bust=0;
    protected int next_energy=0;
    protected String text;
    protected int anim_type=0;
    protected int anim_step=0;
    protected Bitmap[] anim= new Bitmap[7]; // перечень изображений для анимации
    protected float pen_coord=0;

    public int dw,dh;

    public Penguine(Context context) {

        size = 5;
        dw= MainActivity.dw;
        dh=MainActivity.dh;
        x=dw*3/8;
        y=dh-dw/2-dw/7;
        maxY=0;
        saveY=0;
        sy=y;
        speed = 0;
        bust = (float) 0.0;
        jump= (float) 0.3;
        jump_up=new float[]{(float) 0.1,(float) 0.1,(float) 0.1,(float) 0.1,(float)0.1,(float)0.1,(float)0.1};
        bust_up=new float[]{(float) 0.1,(float) 0.1,(float) 0.1,(float) 0.1,(float)0.1,(float)0.1,(float)0.1};
        maxenergy=5;
        money=0;
        energy=maxenergy;


        init(context); // инициализируем корабль


    }

    void init(Context context) { // сжимаем картинку до нужных размеров
        //int dw= getResources().getDisplayMetrics().widthPixels;//получаем ширину экрана
       // int dh= getResources().getDisplayMetrics().heightPixels;//получаем ширину экрана
        bitmapId = R.drawable.pen1; // определяем начальные параметры

        Bitmap cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        bitmap = Bitmap.createScaledBitmap(
                cBitmap, dw/4, dw/3, false);
        cBitmap.recycle();

        bitmapId = R.drawable.pen1_fly; // определяем начальные параметры
        cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        bitmap1 = Bitmap.createScaledBitmap(
                cBitmap, dw/4, dw/3, false);
        cBitmap.recycle();

        bitmapId = R.drawable.f0;// определяем начальные параметры
        cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        anim[0] = Bitmap.createScaledBitmap(
                cBitmap, dw/4, dw/3, false);
        cBitmap.recycle();

        bitmapId = R.drawable.f1;// определяем начальные параметры
        cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        anim[1] = Bitmap.createScaledBitmap(
                cBitmap, dw/4, dw/3, false);
        cBitmap.recycle();
        anim[6] = anim[1];
        //cBitmap.recycle();

        bitmapId = R.drawable.f2;// определяем начальные параметры
        cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        anim[2] = Bitmap.createScaledBitmap(
                cBitmap, dw/4, dw/3, false);
        cBitmap.recycle();
        anim[5] = anim[2];
        //cBitmap.recycle();

        bitmapId = R.drawable.f3;// определяем начальные параметры
        cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        anim[3] = Bitmap.createScaledBitmap(
                cBitmap, dw/4, dw/3, false);
        cBitmap.recycle();
        anim[4] = anim[3];
        //cBitmap.recycle();
    }

    void update(){ // тут будут вычисляться новые координаты
        if(MainActivity.flying && canfly){//если включено событие - нажата кнопка и разрешён полёт
            if(pen_coord==0)  {if(!MainActivity.setup) speed+=jump;} //если стоит на земле, то ускорение прыжком
            else speed+= bust; //если уже летит, то ускорение взмахами
            energy-=1;
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
            speed-=0.1; //уменьшаем скорость на притяжение
            if(speed<0) canfly=false;//если уже падаем, блокируем возможность полёта
            if (speed<0 && pen_coord+speed<0) {
                pen_coord=0; //если будем ниже земли, встаём на землю
                speed=0;
            }
            else pen_coord += speed; //иначе меняем высоту

            //pen_coord=(sy-y)*3/dw;
            y=sy-pen_coord*dw/3;
            if (maxY<pen_coord&&maxY>0) maxY=pen_coord; //если максимальн
            if (saveY<pen_coord) saveY=pen_coord;
        }
        else{
            speed=0;
            money = money + ((float)Math.round(saveY * 100)) / 100;
            if(maxY==0)maxY=saveY;
            saveY=0;
            pen_coord=0;
            y=sy;
            if(energy<maxenergy) energy+=1;
            else canfly=true;
            if (MainActivity.update!=-1) {
                if (to_update==-1){
                    switch (MainActivity.update) {
                        case 0:
                            if (next_jump < 7) {
                                if (jump_record[next_jump] <= maxY) {
                                    to_update = time_to_up[next_jump];
                                    savedate = new Date();
                                    d = new Date();
                                    text = "+" + jump_up[next_jump] + " к силе прыжка";
                                    //MainActivity.Up_jump.setText("Ускорить");
                                }
                                else MainActivity.update = -1;
                            }
                            else MainActivity.update = -1;
                            break;
                        case 1:
                            if (next_bust<7){
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
                            if (next_energy<7) {
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
                    if (to_update<=(d.getTime()-savedate.getTime())/1000) {
                        switch (MainActivity.update) {
                            case 0:
                                to_update=-1;
                                jump+=jump_up[next_jump];
                                next_jump++;
                                MainActivity.update=-1;
                                break;
                            case 1:
                                to_update=-1;
                                bust+=bust_up[next_bust];
                                next_bust++;
                                MainActivity.update=-1;
                                break;
                            case 2:
                                to_update=-1;
                                maxenergy+=energy_up[next_energy];
                                next_energy++;
                                MainActivity.update=-1;
                                break;
                        }
                    }
                }

            }
        }

    }

    void drow(Paint paint, Canvas canvas){ // рисуем картинку
        float draw_y=y;
        if (y<=dh/8) draw_y=dh/8;


        //canvas.drawBitmap(bitmap, x, draw_y, paint);

        paint.setColor(Color.BLACK);




        if(!MainActivity.setup) {
            //if(isfly){
            if(anim_type==1) {//если уже анимируется полёт
                canvas.drawBitmap(anim[anim_step], x, draw_y, paint);
                anim_step++;
                if (anim_step>6){
                    anim_type=0;
                    anim_step=0;
                }
            }


                //canvas.drawBitmap(bitmap1, x, draw_y, paint);
                //isfly=false;
            //}

            else canvas.drawBitmap(bitmap, x, draw_y, paint);


            paint.setTextSize(dh / 40);
            canvas.drawText(String.valueOf((int) energy) + "/" + String.valueOf((int) maxenergy), dw / 2 - dh / 40, dh / 30, paint);

            paint.setTextSize(dh / 30);
            if (pen_coord >0){
                if (pen_coord<1000) canvas.drawText(String.valueOf(roundd(pen_coord, 2))+"м", dw / 2 - dh / 30, draw_y-dh / 30, paint);
                else if (pen_coord<1000000) canvas.drawText(String.valueOf(roundd(pen_coord/1000, 2))+"км", dw / 2 - dh / 30, draw_y-dh / 30, paint);
            }
            if (maxY > 0){
                if (maxY<1000) canvas.drawText("Max: " + String.valueOf(roundd(maxY, 2))+"м", dh / 50, dh / 15, paint);
                else if (maxY<1000000) canvas.drawText("Max: " + String.valueOf(roundd(maxY/1000, 2))+"км", dh / 50, dh / 15, paint);
            }

            canvas.drawText(String.valueOf(roundd(money, 1)), dw - dh / 8, dh / 15, paint);
            if (to_update != -1) {
                paint.setTextSize(dh / 30);

                canvas.drawText(String.valueOf(to_update - (d.getTime() - savedate.getTime()) / 1000), dw / 2 - dh / 15, dw / 2 + dw / 10, paint);
                canvas.drawText(String.valueOf(text), dw / 2 - dh / 15, dw / 2 + dw / 10 + dh / 30, paint);


            }
        }
        else{
            if(isfly){
                isfly=false;
            }
            canvas.drawBitmap(bitmap, x, draw_y, paint);

            paint.setTextSize(dh/50);
            float shiftX=dh/180;

            canvas.drawText("Сила прыжка ", MainActivity.Up_jump.getX()+shiftX, MainActivity.Up_jump.getY()+dh/50, paint);
            canvas.drawText(String.valueOf(jump)+" ("+String.valueOf(next_jump+1)+"lvl)", MainActivity.Up_jump.getX()+shiftX, MainActivity.Up_jump.getY()+dh*2/50, paint);

            canvas.drawText("Сила взмаха ", MainActivity.Up_fly.getX()+shiftX, MainActivity.Up_fly.getY()+dh/50, paint);
            canvas.drawText(String.valueOf(bust)+" ("+String.valueOf(next_bust+1)+"lvl)", MainActivity.Up_fly.getX()+shiftX, MainActivity.Up_fly.getY()+dh/25, paint);

            canvas.drawText("Энергия ", MainActivity.Up_energy.getX()+shiftX, MainActivity.Up_energy.getY()+dh/50, paint);
            canvas.drawText(String.valueOf(maxenergy)+" ("+String.valueOf(next_energy+1)+"lvl)", MainActivity.Up_energy.getX()+shiftX, MainActivity.Up_energy.getY()+dh/25, paint);



            if (MainActivity.update==0&& to_update!=-1){
                paint.setColor(Color.GREEN);
                canvas.drawText("Улучшение " , MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dh * 3 / 50, paint);
                canvas.drawText("запущено", MainActivity.Up_jump.getX()+shiftX, MainActivity.Up_jump.getY()+dh*4/50, paint);
                canvas.drawText(String.valueOf(to_update - (d.getTime() - savedate.getTime()) / 1000)+"сек", MainActivity.Up_jump.getX()+shiftX, MainActivity.Up_jump.getY()+dh*5/50, paint);
                canvas.drawText("осталось " , MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dh * 6 / 50, paint);
                paint.setColor(Color.BLACK);
            }
            else {
                if(next_jump<7){
                    if (jump_record[next_jump] <= maxY) {
                        paint.setColor(Color.BLUE);
                        canvas.drawText("Улучшение " , MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dh * 3 / 50, paint);
                        canvas.drawText("доступно", MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dh * 4 / 50, paint);
                        canvas.drawText(String.valueOf(time_to_up[next_jump]) + "сек", MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dh * 5 / 50, paint);
                        canvas.drawText("потребуется", MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dh * 6 / 50, paint);
                        paint.setColor(Color.BLACK);
                    } else {
                        paint.setColor(Color.RED);
                        canvas.drawText("Улучшение " , MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dh * 3 / 50, paint);
                        canvas.drawText("откроется с ", MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dh * 4 / 50, paint);
                        canvas.drawText("рекорда ", MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dh * 5 / 50, paint);
                        canvas.drawText(String.valueOf(jump_record[next_jump]) + "м", MainActivity.Up_jump.getX() + shiftX, MainActivity.Up_jump.getY() + dh * 6 / 50, paint);
                        paint.setColor(Color.BLACK);
                    }
                }

            }
            if (MainActivity.update==1&& to_update!=-1){
                paint.setColor(Color.GREEN);
                canvas.drawText("Улучшение " , MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dh * 3 / 50, paint);
                canvas.drawText("запущено", MainActivity.Up_fly.getX()+shiftX, MainActivity.Up_fly.getY()+dh*4/50, paint);
                canvas.drawText(String.valueOf(to_update - (d.getTime() - savedate.getTime()) / 1000)+"сек", MainActivity.Up_fly.getX()+shiftX, MainActivity.Up_fly.getY()+dh*5/50, paint);
                canvas.drawText("осталось " , MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dh * 6 / 50, paint);
                paint.setColor(Color.BLACK);
            }
            else {
                if(next_bust<7){
                    if (bust_record[next_bust] <= maxY) {
                        paint.setColor(Color.BLUE);
                        canvas.drawText("Улучшение " , MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dh * 3 / 50, paint);
                        canvas.drawText("доступно", MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dh * 4 / 50, paint);
                        canvas.drawText(String.valueOf(time_to_up[next_bust]) + "сек", MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dh * 5 / 50, paint);
                        canvas.drawText("потребуется", MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dh * 6 / 50, paint);
                        paint.setColor(Color.BLACK);
                    } else {
                        paint.setColor(Color.RED);
                        canvas.drawText("Улучшение " , MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dh * 3 / 50, paint);
                        canvas.drawText("откроется с ", MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dh * 4 / 50, paint);
                        canvas.drawText("рекорда ", MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dh * 5 / 50, paint);
                        canvas.drawText(String.valueOf(bust_record[next_bust]) + "м", MainActivity.Up_fly.getX() + shiftX, MainActivity.Up_fly.getY() + dh * 6 / 50, paint);
                        paint.setColor(Color.BLACK);
                    }
                }
            }

            if (MainActivity.update==2&& to_update!=-1){
                paint.setColor(Color.GREEN);
                canvas.drawText("Улучшение " , MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dh * 3 / 50, paint);
                canvas.drawText("запущено", MainActivity.Up_energy.getX()+shiftX, MainActivity.Up_energy.getY()+dh*4/50, paint);
                canvas.drawText(String.valueOf(to_update - (d.getTime() - savedate.getTime()) / 1000)+"сек", MainActivity.Up_energy.getX()+shiftX, MainActivity.Up_energy.getY()+dh*5/50, paint);
                canvas.drawText("осталось " , MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dh * 6 / 50, paint);
                paint.setColor(Color.BLACK);
            }
            else{
                if (next_energy<7){
                    if(energy_record[next_energy]<=maxY){
                        paint.setColor(Color.BLUE);
                        canvas.drawText("Улучшение " , MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dh * 3 / 50, paint);
                        canvas.drawText("доступно", MainActivity.Up_energy.getX()+shiftX, MainActivity.Up_energy.getY()+dh*4/50, paint);
                        canvas.drawText(String.valueOf(time_to_up[next_energy])+"сек", MainActivity.Up_energy.getX()+shiftX, MainActivity.Up_energy.getY()+dh*5/50, paint);
                        canvas.drawText("потребуется", MainActivity.Up_energy.getX()+shiftX, MainActivity.Up_energy.getY()+dh*6/50, paint);
                        paint.setColor(Color.BLACK);
                    }
                    else {
                        paint.setColor(Color.RED);
                        canvas.drawText("Улучшение " , MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dh * 3 / 50, paint);
                        canvas.drawText("откроется с " , MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dh * 4 / 50, paint);
                        canvas.drawText("рекорда " , MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dh * 5 / 50, paint);
                        canvas.drawText( String.valueOf(energy_record[next_energy]) + "м", MainActivity.Up_energy.getX() + shiftX, MainActivity.Up_energy.getY() + dh * 6 / 50, paint);
                        paint.setColor(Color.BLACK);
                    }

                }


            }
        }



        //canvas.drawText(String.valueOf(MainActivity.update),300, 450,paint);

    }

    public float roundd(float a,int b){

        return (float) (((float)Math.round(a * Math.pow(10,b))) / Math.pow(10,b));
    }
}
