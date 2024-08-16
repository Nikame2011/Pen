package com.pf.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Date;


public class GameView extends SurfaceView implements Runnable {

    private final SurfaceHolder surfaceHolder;
    private final Paint paint;
    private final Thread gameThread;
    public Penguin pen;
    public boolean firstTime = true;
    private Canvas canvas;

    protected Bitmap back1; // картинка
    protected Bitmap back2; // картинка
    protected Bitmap back3; // картинка
    protected Bitmap back4; // картинка
    protected Bitmap false_button; // картинка
    protected Bitmap menu_box; // картинка
    protected Bitmap qu; // картинка
    protected Bitmap menuBack; // картинка

    protected int bitmapId; // id картинки
    public int dw, dh;
    public static int tick = 0;


    float rec;
    Date time;

    public GameView(Context context, Penguin.MainListener listener) {
        super(context);

        //инициализируем обьекты для рисования
        surfaceHolder = getHolder();
        //MainActivity.width=surfaceHolder.getSurfaceFrame().width();
        paint = new Paint();

        // if (firstTime) { // инициализация при первом запуске
        //MainActivity.width=surfaceHolder.getSurfaceFrame().width();

        dw = MainActivity.dw;
        dh = MainActivity.dh;

        bitmapId = R.drawable.back1;
        Bitmap cBitmap = BitmapFactory.decodeResource(getContext().getResources(), bitmapId);
        back1 = Bitmap.createScaledBitmap(
                cBitmap, dw, dw * 10, false);
        cBitmap.recycle();

        bitmapId = R.drawable.back2;
        cBitmap = BitmapFactory.decodeResource(getContext().getResources(), bitmapId);
        back2 = Bitmap.createScaledBitmap(
                cBitmap, dw, dw * 10, false);
        cBitmap.recycle();

               /* bitmapId = R.drawable.back3;
                cBitmap = BitmapFactory.decodeResource(getContext().getResources(), bitmapId);
                back3 = Bitmap.createScaledBitmap(
                        cBitmap, dw, dw*20, false);
                cBitmap.recycle();
*/
        bitmapId = R.drawable.back4;
        cBitmap = BitmapFactory.decodeResource(getContext().getResources(), bitmapId);
        back4 = Bitmap.createScaledBitmap(
                cBitmap, dw, dh, false);
        cBitmap.recycle();

        bitmapId = R.drawable.imb;
        cBitmap = BitmapFactory.decodeResource(getContext().getResources(), bitmapId);
        false_button = Bitmap.createScaledBitmap(
                cBitmap, dw / 4, dw / 4, false);

        menu_box = Bitmap.createScaledBitmap(
                cBitmap, dw / 2 - dw / 25, dw / 8, false);
        cBitmap.recycle();

        bitmapId = R.drawable.quest;
        cBitmap = BitmapFactory.decodeResource(getContext().getResources(), bitmapId);
        qu = Bitmap.createScaledBitmap(
                cBitmap, dw * 7 / 4, dw * 15 / 4, false);
        cBitmap.recycle();

        bitmapId = R.drawable.img_btns;
        cBitmap = BitmapFactory.decodeResource(getContext().getResources(), bitmapId);
        menuBack = Bitmap.createScaledBitmap(
                cBitmap, dw, dw / 4 + dw / 50, false);
        cBitmap.recycle();

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        int n_j = myPreferences.getInt("jump", 1);

        int n_b = myPreferences.getInt("bust", 0);
        //n_b=1;

        int n_e;
        if (n_b <= 1) {
            n_e = 1;
        } else {
            n_e = myPreferences.getInt("energy", 1);
        }

        String vers = myPreferences.getString("version", "0.1.0.0");

        if (vers.equals("0.1.0.0"))
            rec = 0;
        else
            rec = myPreferences.getFloat("record", 0);


        //int headers= myPreferences.getInt("headers_count", 0);
        // if (headers>0){
        //myPreferences.getAll("header");
        // }
        float tu;
        if (MainActivity.update == -1) {
            tu = -1;
            time = MainActivity.first_date;
        } else {
            long rd_time = myPreferences.getLong("strt_date", MainActivity.first_date.getTime());
            time = new Date(rd_time);
            tu = myPreferences.getFloat("time_to_up", -1);
        }


        //SharedPreferences  mPrefs = getPreferences(MODE_PRIVATE);
        // n_j = 10;
        // n_e = 5;
        //rec = 0;
        // n_b = 10;
        // rec =0;
          /*  int n_j = 1;
            int n_e = 1;*/
          /*  int n_b = 0;
            float rec =0;
            time=MainActivity.first_date;
            tu=-1;*/


        // }
        pen = new Penguin(getContext(), (byte) n_j, (byte) n_b, (byte) n_e, rec, time, tu, listener); // добавляем пингвина

        // инициализируем поток
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        control_date = new Date();

//        while (MainActivity.Setup.getY() == 0) {
//            control();
//        }
//        MainActivity.end = MainActivity.Setup.getY() + dw / 4f + dw / 100f;


        firstTime = false;
        //boolean gameRunning = true;
        while (true/*gameRunning*/) {

            //MainActivity.end=MainActivity.Setup.getY()+dw/4+dw/100;
            Date startDate=new Date();
            update();
            draw();

            // if (quest!=0)question();
            //control();
            tick += 2;
            if (tick >= 90)
                tick = 0;

            Date finishDate=new Date();
            superControl(startDate, finishDate);


            if (MainActivity.new_game) {
                new_game();
                MainActivity.new_game = false;
            }
        }
        //close();
    }

    private void update() {
        if (!firstTime) {
            pen.update();
        }
    }

    public void new_game() {
        pen.new_game();
    }

    public void close() {
        //pen.close();
        back1 = null;
        back2 = null;
        back3 = null;
        back4 = null;
    }


    private void draw() {
        try {
            if (surfaceHolder.getSurface().isValid()) {  //проверяем валидный ли surface


                canvas = surfaceHolder.lockCanvas(); // закрываем canvas

                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                //canvas.drawColor(Color.BLACK); // заполняем фон чёрным
                //if (17-pen.y<15) canvas.drawBitmap(seen, -surfaceHolder.getSurfaceFrame().width(), (float) (surfaceHolder.getSurfaceFrame().height()-surfaceHolder.getSurfaceFrame().width()*10), paint);

                //canvas.drawBitmap(back4, 0, 0, paint);

/*            if (pen.y>dh/8) canvas.drawBitmap(back3, 0, (float) (dh-dw*20.2), paint);
            else canvas.drawBitmap(back3, 0, (float) ((dh/8-pen.y)/32+dh-dw*20.2), paint);
*/
                if (pen.y > dh - (dw * 0.2 + dw * 10.7) * 8 & pen.y <= dh - dw * 0.2 - dw * 5.2)
                    canvas.drawBitmap(back2, 0, (float) ((dh / 8f - pen.y) / 8 + dh - dw * 10.7), paint);
                //else  if (pe9n.y<=-dh/8) canvas.drawBitmap(back2, 0, (float) ((-dh/8-pen.y)/8+dh-dw*10.2), paint);
                //canvas.drawBitmap(back2, 0, (float) (dh-dw*10.2), paint);

                //float st=MainActivity.Setup.getY()+dw/4;
                //MainActivity.end=st;
                float st = MainActivity.end - dw / 100f;

                if (pen.y <= dh / 8f & pen.y >= dh - dw * 10.2)
                    canvas.drawBitmap(back1, 0, (float) ((dh / 8f - pen.y) +/*dh*/st - dw * 10.2), paint);
                else if (pen.y > dh / 8f)
                    canvas.drawBitmap(back1, 0, (float) (/*dh*/st - dw * 10.2), paint);


                //else  if (pen.y<=-dh/8& pen.y<=-dw*10.2)  canvas.drawBitmap(back1, 0, (float) ((dh/8-pen.y)+dh-dw*10.2), paint);

                canvas.drawBitmap(menuBack, 0, st - dw / 4f - dw / 100f, paint);

                pen.drow(paint, canvas); // рисуем пингвина и меню

                //canvas.drawBitmap(false_button, dw * 3 / 4f - dw / 100f, st - dw / 4f, paint);
                //canvas.drawBitmap(menu_box, dw / 4f + dw / 50f, st - dw / 4f, paint);
                //canvas.drawBitmap(menu_box, dw / 4f + dw / 50f, st - dw / 8f, paint);
                canvas.drawText("incr: " + increment, 100, 100, paint);
                canvas.drawText("frames: " + frames, 100, 200, paint);
            /*if (quest!=0){
                canvas.drawBitmap(qu, -dw*3/4, -dw/7/4, paint);

            }*/
                surfaceHolder.unlockCanvasAndPost(canvas); // открываем canvas
            }
        } catch (Exception e) {

        }
    }

    long saveTicker = 0;
    float cadres = 30;
    byte frames = 30;

    float increment = 1000000 / 60;

    Date control_date;
    Date d;
    public int controlTick = 0;

    private void control() { // пауза и контроль количества кадров
//       try {
//           gameThread.sleep(12);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        d = new Date();
        controlTick++;
        if ((d.getTime() - control_date.getTime()) % 50 > 0 & saveTicker != (d.getTime() - control_date.getTime()) % 50 && controlTick > 0) {
            saveTicker = (d.getTime() - control_date.getTime()) % 50;
            frames = (byte) (controlTick * 1000 / (float) (d.getTime() - control_date.getTime()));

            increment = (Math.max(increment * ((float) frames) / (float) 30, 0.01f));
            //cadres=Math.min(Math.max(frames<40? cadres/2:frames>=60? cadres*2:cadres, 2),60);


       /*     if(frames<55)
                cadres=30;
            else if(increment>1000)
                cadres=60;*/
            if (d.getTime() - control_date.getTime() >= 1000) {
                saveTicker = 0;
                controlTick = 0;
                control_date = new Date();
            }

        }

      /*  if ((float) (d.getTime() - control_date.getTime()) / 50>=1 &tick>0) {
            frames= (byte) (tick*1000/(float) (d.getTime() - control_date.getTime()));
            control_date = new Date();
            tick=0;
            //dop_inc=increment * frames>60?60/((increment * frames) % 60):0;
            increment=Math.max(increment * frames / 60, 100);*/


            /*if (cadres-frames>cadres/6)
                if(increment>1)
                    increment--;
                else if(cadres==60){
                    cadres=30;
                    speed_coef=2;
                }
            if (cadres-frames<-cadres/6 )
                increment++;*/
        // }
        //else{

        //}

        //  else{
        //         byte fr= (byte) (tick*1000/(float) (d.getTime() - control_date.getTime()));
        //        increment=increment*fr/60>1 ? increment*fr/60 : 1;
        //   }

        try {
            //if(dop_inc!=0){
            //     if(tick%dop_inc==0)
            //       gameThread.sleep(increment*2);
            //    else
            //     gameThread.sleep(increment);}
            // else
            gameThread.sleep((int) increment / 1000, (int) increment % 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    byte framesTarget=17;
    int frameDelay= (short) (1000/framesTarget);
    int delayDebt=0;

    private void superControl(Date startDate, Date finishDate) { // пауза и контроль количества кадров

        controlTick++;

        int delay = (int)Math.max((finishDate.getTime()-startDate.getTime()+delayDebt),0);

        if(delay<frameDelay) {
            try {
                gameThread.sleep(frameDelay-delay);
                increment=frameDelay-delay;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else{
            increment=0;
            delayDebt= (delay-frameDelay);
        }
        d = new Date();
        if (d.getTime() - control_date.getTime() >= 500) {
            frames= (byte) (controlTick*2);
            if(frames>framesTarget || delayDebt>500) delayDebt=0;
            controlTick = 0;
            control_date = new Date();
        }
    }
}
