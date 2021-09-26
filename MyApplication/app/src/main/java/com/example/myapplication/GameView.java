package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable{

    private SurfaceHolder surfaceHolder;
    private Paint paint;
    private Thread gameThread = null;
    private boolean gameRunning = true;
    private Penguine pen;
    private boolean firstTime = true;
    private Canvas canvas;
    public static int maxX = 20; // размер по горизонтали
    public static int maxY = 28; // размер по вертикали
    public static float unitW = 0; // пикселей в юните по горизонтали
    public static float unitH = 0; // пикселей в юните по вертикали
    protected Bitmap back1; // картинка
    protected Bitmap back2; // картинка
    protected Bitmap back3; // картинка
    protected Bitmap back4; // картинка
    protected int bitmapId; // id картинки
    public int dw,dh;

    public GameView(Context context) {
        super(context);
        //инициализируем обьекты для рисования
        surfaceHolder = getHolder();
        //MainActivity.width=surfaceHolder.getSurfaceFrame().width();
        paint = new Paint();




        // инициализируем поток
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (gameRunning) {
            update();
            draw();
            control();
        }
    }

    private void update() {
        if(!firstTime) {
            pen.update();
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {  //проверяем валидный ли surface

            if(firstTime){ // инициализация при первом запуске
                firstTime = false;
                //MainActivity.width=surfaceHolder.getSurfaceFrame().width();
                unitW = surfaceHolder.getSurfaceFrame().width()/maxX; // вычисляем число пикселей в юните
                unitH = surfaceHolder.getSurfaceFrame().height()/maxY;
                dw= MainActivity.dw;
                dh=MainActivity.dh;

                bitmapId = R.drawable.back1;
                Bitmap cBitmap = BitmapFactory.decodeResource(getContext().getResources(), bitmapId);
                back1 = Bitmap.createScaledBitmap(
                        cBitmap, dw, dw*10, false);
                cBitmap.recycle();

                bitmapId = R.drawable.back2;
                cBitmap = BitmapFactory.decodeResource(getContext().getResources(), bitmapId);
                back2 = Bitmap.createScaledBitmap(
                        cBitmap, dw, dw*10, false);
                cBitmap.recycle();

                bitmapId = R.drawable.back3;
                cBitmap = BitmapFactory.decodeResource(getContext().getResources(), bitmapId);
                back3 = Bitmap.createScaledBitmap(
                        cBitmap, dw, dw*10, false);
                cBitmap.recycle();

                bitmapId = R.drawable.back4;
                cBitmap = BitmapFactory.decodeResource(getContext().getResources(), bitmapId);
                back4 = Bitmap.createScaledBitmap(
                        cBitmap, dw, dw*10, false);
                cBitmap.recycle();

                pen = new Penguine(getContext()); // добавляем корабль
                //MainActivity.Up_jump.setText();
            }

            canvas = surfaceHolder.lockCanvas(); // закрываем canvas
            canvas.drawColor(Color.BLACK); // заполняем фон чёрным
            //if (17-pen.y<15) canvas.drawBitmap(seen, -surfaceHolder.getSurfaceFrame().width(), (float) (surfaceHolder.getSurfaceFrame().height()-surfaceHolder.getSurfaceFrame().width()*10), paint);

            canvas.drawBitmap(back4, 0, (float) (dh-dw*10.2), paint);

            if (pen.y>dh/8) canvas.drawBitmap(back3, 0, (float) (dh-dw*10.2), paint);
            else canvas.drawBitmap(back3, 0, (float) ((dh/8-pen.y)/32+dh-dw*10.2), paint);

            if (pen.y>dh/8) canvas.drawBitmap(back2, 0, (float) (dh-dw*10.2), paint);
            else canvas.drawBitmap(back2, 0, (float) ((dh/8-pen.y)/8+dh-dw*10.2), paint);


            if (pen.y>dh/8) canvas.drawBitmap(back1, 0, (float) (dh-dw*10.2), paint);
            else canvas.drawBitmap(back1, 0, (float) ((dh/8-pen.y)+dh-dw*10.2), paint);


            pen.drow(paint, canvas); // рисуем корабль

            surfaceHolder.unlockCanvasAndPost(canvas); // открываем canvas
        }
    }

    private void control() { // пауза на 17 миллисекунд
        try {
            gameThread.sleep(16);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
