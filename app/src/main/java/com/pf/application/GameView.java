package com.pf.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


public class GameView extends SurfaceView implements Runnable {

    public interface MainListener {
        void onRoomChanged(Room room);
    }

    private final SurfaceHolder surfaceHolder;
    private final Paint paint;
    private Thread gameThread;
    public Penguin pen;
    public boolean firstTime = true;
    private Canvas canvas;

//    protected Bitmap fone0; // картинка
    //    protected Bitmap ocean; // картинка
//    protected Bitmap fone1; // картинка
    protected Bitmap cloud1; // картинка
    protected Bitmap cloud2; // картинка

    //    protected Bitmap back1; // картинка
//    protected Bitmap back2; // картинка
//    protected Bitmap back3; // картинка
//    protected Bitmap back4; // картинка
//    protected Bitmap false_button; // картинка
//    protected Bitmap menu_box; // картинка
//    protected Bitmap qu; // картинка
//    protected Bitmap menuBack; // картинка

    protected int bitmapId; // id картинки
    public int dw, dh;
    public static int tick = 0;

    Bitmap[] fones0;
    Bitmap[] fones1;
    Bitmap[] fonesLR;

    float rec;
    Date time;

//            , decOcean;

    //    private Bitmap decodeFone(BitmapRegionDecoder decoder, int fullTargetHeight, int fullTargetWidth, int){
//
//    }
    MainListener listener;
    Context context;
    Penguin.MainListener penListener;

    public GameView(Context context, MainListener listener, Penguin.MainListener penListener) {
        super(context);
        this.context = context;
        this.listener = listener;
        this.penListener = penListener;
        //инициализируем обьекты для рисования
        surfaceHolder = getHolder();
//        setZOrderOnTop(true);
//        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
//        setBackgroundColor(Color.TRANSPARENT);
        paint = new Paint();

        // if (firstTime) { // инициализация при первом запуске
        //MainActivity.width=surfaceHolder.getSurfaceFrame().width();

        dw = MainActivity.dw;
        dh = MainActivity.dh;

        BitmapRegionDecoder decFone0 = null;
        BitmapRegionDecoder decFone1 = null;
        try {
            InputStream is = getResources().openRawResource(+R.drawable.ff1);//+R.drawable.aice0);//
            decFone0 = BitmapRegionDecoder.newInstance(is, false);
            is = getResources().openRawResource(+R.drawable.ff2);//+R.drawable.aice0);//
            decFone1 = BitmapRegionDecoder.newInstance(is, false);
//            is = getResources().openRawResource(+R.drawable.ocean);//+R.drawable.aice0);//
//            decOcean = BitmapRegionDecoder.newInstance(is, false);
        } catch (Throwable e) {

        }

        //lastPenY = MainActivity.end - dw / 4f - dw / 25f - dw / 2f;

//        2500 вк =12,5ш   775 вк = 3,875ш
//        500 шк = 2 ш
//
//        3125 внк = 12,5ш   968,75


        int he = decFone0.getHeight();
        int wi = decFone0.getWidth();
        int x = (int) (he * dh / 12.5 / dw);

        float coeff = (float) (he / 12.5 / dw);

        int length = (int) (12.5 * dw * 2 / dh) + (12.5 * dw * 2 % dh != 0 ? 1 : 0);
        fones0 = new Bitmap[length];
        fonesLR = new Bitmap[length];
        for (int i = 0; i < length; i++) {
            int t = x / 2 * i;
            int b = t + x / 2;
            int scHe = dh / 2;

            if (b > he) {
                scHe = (int) ((x / 2 + he - b) / coeff);
                b = t + x / 2 + he - b;
            }

            Bitmap tempBit2 = decFone0.decodeRegion(new Rect(wi / 4, t, wi * 3 / 4, b), new BitmapFactory.Options());
            fones0[i] = Bitmap.createScaledBitmap(
                    tempBit2, dw, scHe, false);

            Bitmap tempBit3 = decFone0.decodeRegion(new Rect(0, t, wi / 4, b), new BitmapFactory.Options());
            fonesLR[i] = Bitmap.createScaledBitmap(
                    tempBit3, dw/4, scHe, false);
        }

//        fonesLR = new Bitmap[2];
//        int tp = (int) (coeff * 9.14 * dw - coeff * dh + coeff * dw*0.83);//(int) (coeff * 10 * dw - coeff * dh - coeff * dw / 50); //dh-dw*0.83
//        int btm = (int) (tp + coeff * dh);
//
//        Bitmap tempBit3 = decFone0.decodeRegion(new Rect(0, tp, wi / 4, btm), new BitmapFactory.Options());
//        fonesLR[0] = Bitmap.createScaledBitmap(
//                tempBit3, dw / 4, dh, false);
//
//        tempBit3 = decFone0.decodeRegion(new Rect(wi * 3 / 4, tp, wi, btm), new BitmapFactory.Options());
//        fonesLR[1] = Bitmap.createScaledBitmap(
//                tempBit3, dw / 4, dh, false);


//        he = decOcean.getHeight();
//        wi = decOcean.getWidth();
//        tempBit = decOcean.decodeRegion(new Rect(wi / 4, 0, wi * 3 / 4, wi / 4), new BitmapFactory.Options());
//        ocean = Bitmap.createScaledBitmap(
//                tempBit, dw, (int) (dh * 0.15f), false);


//        he = decFone1.getHeight();
//        wi = decFone1.getWidth();
//        x = (int) (he * dh * 0.85f / 10 / dw);
//        tempBit = decFone1.decodeRegion(new Rect(wi / 4, he - x, wi * 3 / 4, he), new BitmapFactory.Options());
//        fone1 = Bitmap.createScaledBitmap(
//                tempBit, dw, (int) (dh * 0.85f), false);

        he = decFone1.getHeight();
        wi = decFone1.getWidth();
        x = (int) (he * dh / 10.0 / dw);



        coeff = (float) (he / 10.0 / dw);

        length = (int) (10 * dw * 2 / dh) + (10 * dw * 2 % dh != 0 ? 1 : 0);
        fones1 = new Bitmap[length];
        for (int i = 0; i < length; i++) {
            int t = x / 2 * i;
            int b = t + x / 2;
            int scHe = dh / 2;

            if (b > he) {
                scHe = (int) ((x / 2 + he - b) / coeff);
                b = t + x / 2 + he - b;
            }

            Bitmap tempBit2 = decFone1.decodeRegion(new Rect(wi / 4, t, wi * 3 / 4, b), new BitmapFactory.Options());
            fones1[i] = Bitmap.createScaledBitmap(
                    tempBit2, dw, scHe, false);
        }



//        bitmapId = R.drawable.back1;
//        Bitmap cBitmap = BitmapFactory.decodeResource(getContext().getResources(), bitmapId);
//        back1 = Bitmap.createScaledBitmap(
//                cBitmap, dw, dw * 10, false);
//        cBitmap.recycle();
//
//        bitmapId = R.drawable.back2;
//        cBitmap = BitmapFactory.decodeResource(getContext().getResources(), bitmapId);
//        back2 = Bitmap.createScaledBitmap(
//                cBitmap, dw, dw * 10, false);
//        cBitmap.recycle();

               /* bitmapId = R.drawable.back3;
                cBitmap = BitmapFactory.decodeResource(getContext().getResources(), bitmapId);
                back3 = Bitmap.createScaledBitmap(
                        cBitmap, dw, dw*20, false);
                cBitmap.recycle();
*/
//        bitmapId = R.drawable.back4;
//        /*Bitmap*/
//        Bitmap cBitmap = BitmapFactory.decodeResource(getContext().getResources(), bitmapId);
//        back4 = Bitmap.createScaledBitmap(
//                cBitmap, dw, dh, false);
//        cBitmap.recycle();
//
//        bitmapId = R.drawable.imb;
//        cBitmap = BitmapFactory.decodeResource(getContext().getResources(), bitmapId);
//        false_button = Bitmap.createScaledBitmap(
//                cBitmap, dw / 4, dw / 4, false);
//
//        menu_box = Bitmap.createScaledBitmap(
//                cBitmap, dw / 2 - dw / 25, dw / 8, false);
//        cBitmap.recycle();
//
//        bitmapId = R.drawable.quest;
//        cBitmap = BitmapFactory.decodeResource(getContext().getResources(), bitmapId);
//        qu = Bitmap.createScaledBitmap(
//                cBitmap, dw * 7 / 4, dw * 15 / 4, false);
//        cBitmap.recycle();
//        bitmapId = R.drawable.img_btns;
//         cBitmap = BitmapFactory.decodeResource(getContext().getResources(), bitmapId);
//        menuBack = Bitmap.createScaledBitmap(
//                cBitmap, dw, dw / 4 + dw / 50, false);
//        cBitmap.recycle();

        bitmapId = R.drawable.cloud1;
        Bitmap cBitmap = BitmapFactory.decodeResource(getContext().getResources(), bitmapId);
        cloud1 = Bitmap.createScaledBitmap(
                cBitmap, 352 * dw / 3 / 425, 206 * dw / 3 / 425, false);
        cBitmap.recycle();

        bitmapId = R.drawable.cloud2;
        cBitmap = BitmapFactory.decodeResource(getContext().getResources(), bitmapId);
        cloud2 = Bitmap.createScaledBitmap(
                cBitmap, 352 * dw / 3 / 425, 206 * dw / 3 / 425, false);
        cBitmap.recycle();

        double localDp = dw / 850d;
        double box = (int) (localDp * 212.5);
        double side = (int) (localDp * 17);


        double y = MainActivity.end - box * 3 - 4 * side;
        camera = new Camera(dw, dh, new Coordinate(box, y));

    }

    Camera camera;

    @Override
    public void run() {

        control_date = new Date();

        for (int i = 0; i < 100; i++) {
            int h = (int) (dh - dw * 87.2 + new Random().nextFloat() * (-dh + dw * 87.2 + dh - dw * 5.4));
            clouds.add(new Cloud(new Random().nextInt(2), new Random().nextInt(dw), h));
        }

//        while (MainActivity.Setup.getY() == 0) {
//            control();
//        }
//        MainActivity.end = MainActivity.Setup.getY() + dw / 4f + dw / 100f;


        firstTime = false;
        gameRunning = true;
        while (gameRunning) {

            //MainActivity.end=MainActivity.Setup.getY()+dw/4+dw/100;
            Date startDate = new Date();
            update();
            draw();

            // if (quest!=0)question();
            //control();
            tick += 2;
            if (tick >= 90)
                tick = 0;

            Date finishDate = new Date();
            superControl(startDate, finishDate);


//            if (MainActivity.new_game) {
//                new_game();
//                MainActivity.new_game = false;
//            }
        }
        if (needNewGame) {
            needNewGame = false;
            pen.new_game();

            gameThread = new Thread(this);
            gameThread.start();
        }
        //close();
    }

    boolean gameRunning = false;
    boolean needNewGame = false;

    private void update() {
        if (!firstTime) {
            pen.update();

            camera.update(new Coordinate(pen.getX(), pen.getY()));
        }
    }

    public void new_game() {
        needNewGame = true;
        gameRunning = false;
    }

    public void load_game() {
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

//        n_j = 15;
//        n_e = 10;
//        n_b = 15;

        pen = new Penguin(getContext(), (byte) n_j, (byte) n_b, (byte) n_e, rec, time, tu, penListener); // добавляем пингвина

        // инициализируем поток
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void close() {
        //pen.close();
//        back1 = null;
//        back2 = null;
//        back3 = null;
//        back4 = null;
    }

    //float lastPenY;
    float lastShiftY;
    float lastShiftX;

//    private void draw_old() {
//        try {
//            if (surfaceHolder.getSurface().isValid()) {  //проверяем валидный ли surface
//
//                canvas = surfaceHolder.lockCanvas(); // закрываем canvas
//
//                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//
//                //canvas.drawColor(Color.BLACK); // заполняем фон чёрным
//                //if (17-pen.y<15) canvas.drawBitmap(seen, -surfaceHolder.getSurfaceFrame().width(), (float) (surfaceHolder.getSurfaceFrame().height()-surfaceHolder.getSurfaceFrame().width()*10), paint);
//
//                //canvas.drawBitmap(back4, 0, 0, paint);
//
///*            if (pen.y>dh/8) canvas.drawBitmap(back3, 0, (float) (dh-dw*20.2), paint);
//            else canvas.drawBitmap(back3, 0, (float) ((dh/8-pen.y)/32+dh-dw*20.2), paint);
//*/
//
//                //else  if (pe9n.y<=-dh/8) canvas.drawBitmap(back2, 0, (float) ((-dh/8-pen.y)/8+dh-dw*10.2), paint);
//                //canvas.drawBitmap(back2, 0, (float) (dh-dw*10.2), paint);
//
//                //float st=MainActivity.Setup.getY()+dw/4;
//                //MainActivity.end=st;
//                float st = MainActivity.end - dw / 100f;
//                if (pen.y <= dh - dw * 5.4 && pen.y > dh - dw * 87.2) {
//                    canvas.drawBitmap(back2, 0, (float) ((dh / 8f - pen.y) / 8 + dh - dw * 10.7), paint);
//                }
//                if (pen.y <= dh / 8f & pen.y >= dh - dw * 10.2)
//                    canvas.drawBitmap(back1, 0, (float) ((dh / 8f - pen.y) + st - dw * 10.2), paint);
//                else if (pen.y > dh / 8f)
//                    canvas.drawBitmap(back1, 0, (float) (st - dw * 10.2), paint);
//
//
//                //else  if (p  en.y<=-dh/8& pen.y<=-dw*10.2)  canvas.drawBitmap(back1, 0, (float) ((dh/8-pen.y)+dh-dw*10.2), paint);
//
//                //canvas.drawBitmap(menuBack, 0, st - dw / 4f - dw / 100f, paint);
//
//                pen.drow(paint, canvas); // рисуем пингвина и меню
//
//                //canvas.drawBitmap(false_button, dw * 3 / 4f - dw / 100f, st - dw / 4f, paint);
//                //canvas.drawBitmap(menu_box, dw / 4f + dw / 50f, st - dw / 4f, paint);
//                //canvas.drawBitmap(menu_box, dw / 4f + dw / 50f, st - dw / 8f, paint);
//                paint.setColor(Color.RED);
//                canvas.drawText("incr: " + increment, 100, 100, paint);
//                canvas.drawText("frames: " + frames, 100, 200, paint);
//            /*if (quest!=0){
//                canvas.drawBitmap(qu, -dw*3/4, -dw/7/4, paint);
//
//            }*/
//                surfaceHolder.unlockCanvasAndPost(canvas); // открываем canvas
//            }
//        } catch (Exception e) {
//
//        }
//    }

    int moveF0 = 0;
    int moveF1 = 0;

    boolean canDraw = true;

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }

    ArrayList<Cloud> clouds = new ArrayList();

    private class Cloud {
        int type;

        public Cloud(int type, float x, float y) {
            this.type = type;
            this.x = x;
            this.y = y;
        }

        float x;
        float y;
    }

    enum Room {
        Flying,
        Training
    }

    public void changeRoom(Room targetRoom) {
        selectedRoom = targetRoom;
        listener.onRoomChanged(selectedRoom);
    }

    public Room getSelectedRoom() {
        return selectedRoom;
    }

    Room selectedRoom = Room.Flying;

    private void draw() {
        if (canDraw) {
            try {
                if (surfaceHolder.getSurface().isValid()) {  //проверяем валидный ли surface

                    canvas = surfaceHolder.lockCanvas(); // закрываем canvas
                    canvas.drawColor(Color.rgb(208, 240, 255));
//                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                    //canvas.drawColor(Color.BLACK); // заполняем фон чёрным
                    //if (17-pen.y<15) canvas.drawBitmap(seen, -surfaceHolder.getSurfaceFrame().width(), (float) (surfaceHolder.getSurfaceFrame().height()-surfaceHolder.getSurfaceFrame().width()*10), paint);

                    //canvas.drawBitmap(back4, 0, 0, paint);

/*            if (pen.y>dh/8) canvas.drawBitmap(back3, 0, (float) (dh-dw*20.2), paint);
            else canvas.drawBitmap(back3, 0, (float) ((dh/8-pen.y)/32+dh-dw*20.2), paint);
*/

                    //else  if (pe9n.y<=-dh/8) canvas.drawBitmap(back2, 0, (float) ((-dh/8-pen.y)/8+dh-dw*10.2), paint);
                    //canvas.drawBitmap(back2, 0, (float) (dh-dw*10.2), paint);

                    //float st=MainActivity.Setup.getY()+dw/4;
                    //MainActivity.end=st;
//                float st = MainActivity.end - dw / 100f;
//                if (pen.y <= dh - dw * 5.4 && pen.y > dh -  dw * 87.2){
//                    canvas.drawBitmap(back2, 0, (float) ((dh / 8f - pen.y) / 8 + dh - dw * 10.7), paint);
//                }
//                if (pen.y <= dh / 8f & pen.y >= dh - dw * 10.2)
//                    canvas.drawBitmap(back1, 0, (float) ((dh / 8f - pen.y) + st - dw * 10.2), paint);
//                else if (pen.y > dh / 8f)
//                    canvas.drawBitmap(back1, 0, (float) (st - dw * 10.2), paint);

//TODO add lruCache
// загрузка изображений должна происходить при старте приложения, нужно взять ширину экрана и высоту/половину/треть высоты и
// нарезать какое-то количество изображений, которые будут храниться в памяти постоянно. затем при
//boolean isUpd=false;

                    lastShiftY = (float) camera.cam.getY();
                    lastShiftX = pen.shiftX;

                    if (camera.cam.getY() != lastShiftY || pen.shiftX != lastShiftX) {
                        //isUpd=true;
                        if (pen.getY() > dh - dw * 87.2 && pen.getY() <= dh - dw * 5.4) {

//                            int he = decFone1.getHeight();
//                            int wi = decFone1.getWidth();
//                            //he==10*dw
//                            int x = (int) (he * dh * 0.1f / dw);
//
//                            int move = (int) (he * (lastShiftY) / 8f / 10 / dw);
//
//                            int bottom = he - move;
//                            int top = bottom - x;
//
//                            //todo!!!!! java.lang.IllegalArgumentException: rectangle is outside the image
//                            if (top >= 0 && bottom <= he) {
//                                moveF1 = 0;
//                            } else {
//                                if (top < 0) {
//                                    moveF1 = (int) (dh + lastShiftY + 0.85 * dw - 10 * dw);
//                                    top = 0;
//                                    bottom = he * (dh) / 10 / dw;
//                                } else {
//
//                                    bottom = he;
//                                    top = bottom - (he * (dh) / 10 / dw);
//                                    moveF1 = (int) (lastShiftY + 0.85 * dw);
//                                }
//                            }

//                            Bitmap tempBit = decFone1.decodeRegion(new Rect(wi / 4, top, wi * 3 / 4, bottom), new BitmapFactory.Options());
//                            fone1 = Bitmap.createScaledBitmap(
//                                    tempBit, dw, dh, false);

                        }
//                        if (pen.getY() >= dh - dw * 12.5) {
////todo * 11.2 - это костыль, нужен потому, что нужно задвинуть изображение за экран полностью и
//// только потом прекратить его рисовать, т.е. нужно сместить его на 1dw от его длины (10dw)
//// если так не сделать, то может получиться, что на взлёте отрисовка закончилась после определённого смещения,
//// а за тем при падении новое смещение не просчиталось потому что shiftY меняется рывками и в итоге
//// на один кадр мы отрисовываем картинку не там, где требовалось!! это дичь, не знаю как нормально исправить, поэтому костыль
//                            int he = decFone0.getHeight();
//                            int wi = decFone0.getWidth();
//                            //he==10*dw
//                            int x = (int) (he * dh / 12.5 / dw);
//
//                            int move = (int) (he * (lastShiftY + 2.5 * dw) / 12.5 / dw);
//
//                            int bottom = he - move;
//                            int top = bottom - x;
//
//                            int moveX = (int) (wi * lastShiftX / 2 / dw);
//
//                            if (top >= 0 && bottom <= he) {
//                                moveF0 = 0;
//                            } else {
//                                if (top < 0) {
//                                    moveF0 = (int) (dh + lastShiftY + 2.5 * dw - 12.5 * dw);
//                                    top = 0;
//                                    bottom = (int) (he * (dh) / 12.5 / dw);
//                                } else {
//                                    bottom = he;
//                                    top = (int) (bottom - (he * (dh) / 12.5 / dw));
//                                    moveF0 = (int) (lastShiftY + 2.5 * dw);
//                                }
//                            }
//
////                            Bitmap tempBit = decFone0.decodeRegion(new Rect(wi / 4 - moveX, top, wi * 3 / 4 - moveX, bottom), new BitmapFactory.Options());
////                            fone0 = Bitmap.createScaledBitmap(
////                                    tempBit, dw, dh, false);
//                        }
//                    else if (pen.shiftY == 0){
//                            int he= decFone0.getHeight();
//                            int wi= decFone0.getWidth();
//                            //he==10*dw
//                            int x= (int) (he*dh*0.85f/8.9/dw);
//
//                            moveF0=0;
//
//                            Bitmap tempBit = decFone0.decodeRegion(new Rect(wi/4,he-x,wi*3/4,he),new BitmapFactory.Options());
//                            fone0 = Bitmap.createScaledBitmap(
//                                    tempBit, dw, (int) (dh*0.85f), false);
//                    }
                    }

//                    float x1 = 0;
//                    float x2 = 0;
//                    float x3 = 0;
                    if (pen.getY() <= dh - dw * 5.4 && pen.getY() > dh - dw * 87.2) {

                        double moveY = dw * 10 - (lastShiftY) / 8f;
                        int screen = (int) (moveY * 2 / dh);
                        int shift = (int) (dh * screen / 2 - moveY);

//                        x1 = (float) shift;
//                        x2 = (float) (shift + dh / 2);
//                        x3 = (float) shift + (dh / 2) * 2;

                        if (screen - 2 >= 0 && screen - 2 < fones1.length)
                            canvas.drawBitmap(fones1[screen - 2], (float) lastShiftX, (float) shift, paint);
                        if (screen - 1 >= 0 && screen - 1 < fones1.length)
                            canvas.drawBitmap(fones1[screen - 1], (float) lastShiftX, (float) (shift + dh / 2), paint);
                        if (screen >= 0 && screen < fones1.length)
                            canvas.drawBitmap(fones1[screen], (float) lastShiftX, (float) shift + (dh / 2) * 2, paint);




//                        canvas.drawBitmap(fone1, 0, moveF1, paint);//canvas.drawBitmap(back2, 0, (float) ((dh / 8f - pen.y) / 8 + dh - dw * 10.7), paint);

                        for (Cloud cloud : clouds) {
                            int top = (int) (cloud.y + lastShiftY);
                            if (top <= dh) {
                                canvas.drawBitmap(cloud.type == 0 ? cloud1 : cloud2, cloud.x, top, paint);//canvas.drawBitmap(back2, 0, (float) ((dh / 8f - pen.y) / 8 + dh - dw * 10.7), paint);
                            }
                        }
                    }
                    if (pen.getY() >= dh - dw * 12.5 && moveF0 <= Math.abs(dh)) {
                        double moveY = dw * 10 - lastShiftY;
                        int screen = (int) (moveY * 2 / dh);
                        int shift = (int) (dh * screen / 2 - moveY);

//                        x1 = (float) shift;
//                        x2 = (float) (shift + dh / 2);
//                        x3 = (float) shift + (dh / 2) * 2;

                        if (screen - 2 >= 0 && screen - 2 < fones0.length)
                            canvas.drawBitmap(fones0[screen - 2], (float) lastShiftX, (float) shift, paint);
                        if (screen - 1 >= 0 && screen - 1 < fones0.length)
                            canvas.drawBitmap(fones0[screen - 1], (float) lastShiftX, (float) (shift + dh / 2), paint);
                        if (screen >= 0 && screen < fones0.length)
                            canvas.drawBitmap(fones0[screen], (float) lastShiftX, (float) shift + (dh / 2) * 2, paint);

//                        if (lastShiftX < 0) {
                        if (lastShiftX > 0) {
                            if (screen - 2 >= 0 && screen - 2 < fonesLR.length)
                                canvas.drawBitmap(fonesLR[screen - 2], (float) lastShiftX- dw / 4, (float) shift, paint);
                            if (screen - 1 >= 0 && screen - 1 < fonesLR.length)
                                canvas.drawBitmap(fonesLR[screen - 1], (float) lastShiftX- dw / 4, (float) (shift + dh / 2), paint);
                            if (screen >= 0 && screen < fonesLR.length)
                                canvas.drawBitmap(fonesLR[screen], (float) lastShiftX- dw / 4, (float) shift + (dh / 2) * 2, paint);


//                            canvas.drawBitmap(fonesLR[1], (float) lastShiftX + dw, lastShiftY, paint);
                        }
//                        else if (lastShiftX > 0) {
//                            canvas.drawBitmap(fonesLR[0], (float) lastShiftX - dw / 4, lastShiftY, paint);
//                        }
//                        canvas.drawBitmap(fone0, 0, moveF0, paint);
                        //canvas.drawBitmap(ocean, 0, dh-dw/4+moveF0, paint);
                    }

//                    if (dh*0.85f-lastShiftY >= 0) {
//                        //canvas.drawBitmap(fone0, 0, moveF0, paint);
//                        canvas.drawBitmap(ocean,  0, dh*0.85f+lastShiftY, paint);
//                    }

//                    if(isUpd)
//                        canvas.drawText("upd", 100, 400, paint);

                    //else  if (p  en.y<=-dh/8& pen.y<=-dw*10.2)  canvas.drawBitmap(back1, 0, (float) ((dh/8-pen.y)+dh-dw*10.2), paint);
//                    canvas.drawText("incr: " + increment, 100, 100, paint);
//                    canvas.drawText("frames: " + frames, 100, 200, paint);
//                    canvas.drawText("x1: " + x1 + " x2: " + x2 + " x3: " + x3, 0, 300, paint);
//                    canvas.drawText("spd: " + camera.getSpd().getY(), 100, 300, paint);

                    pen.draw(paint, canvas, selectedRoom, camera.getCam()); // рисуем пингвина и меню

//                    paint.setColor(Color.RED);
//                    canvas.drawText("sy: " + camera.getCam().getY(), 100, 100, paint);
//                    canvas.drawText("Top: " + (int)(pen.getY()+camera.getCam().getY())+ " bottom: "+(int)(pen.getY()+camera.getCam().getY()+dw * 425 / 850), 100, 200, paint);
//                    canvas.drawText("005dh: " + (int)(dh*0.05)+ " 095dh: "+ (int)(dh*0.95), 100, 400, paint);
//                    canvas.drawText("030dh: " + (int)(dh*0.3)+ " 060dh: "+ (int)(dh*0.6), 100, 500, paint);

                    //canvas.drawBitmap(false_button, dw * 3 / 4f - dw / 100f, st - dw / 4f, paint);
                    //canvas.drawBitmap(menu_box, dw / 4f + dw / 50f, st - dw / 4f, paint);
                    //canvas.drawBitmap(menu_box, dw / 4f + dw / 50f, st - dw / 8f, paint);
            /*if (quest!=0){
                canvas.drawBitmap(qu, -dw*3/4, -dw/7/4, paint);

            }*/
                    // открываем canvas
                }
            } catch (Exception e) {
                Log.e("pf", "gwerr", e);
            } finally {
                if (canvas != null)
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                    }
            }
        }
    }

    private byte framesTarget = 9;
    private int frameDelay = (short) (1000 / framesTarget);
    private int controlTick;
    private int delayDebt;

    private float increment;
    private byte frames;
    private Date control_date;
    private Date d;

    private void superControl(Date startDate, Date finishDate) { // пауза и контроль количества кадров

        controlTick++;

        int delay = (int) Math.max((finishDate.getTime() - startDate.getTime() + delayDebt), 0);

        if (delay < frameDelay) {
            try {
                gameThread.sleep(frameDelay - delay);
                increment = frameDelay - delay;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            increment = 0;
            delayDebt = (delay - frameDelay);
        }
        d = new Date();
        if (d.getTime() - control_date.getTime() >= 500) {
            frames = (byte) (controlTick * 2);
            if (frames > framesTarget || delayDebt > 500) delayDebt = 0;
            controlTick = 0;
            control_date = new Date();
        }
    }

    public static class Camera {
        Coordinate pen;
        Coordinate cam;
        Coordinate camCenter;
        Coordinate spd;
        //        Coordinate maxFreeSpeed;
        Coordinate screenSize;
        byte counter = 0;

        public Camera(double dw, double dh, Coordinate pen) {
            camCenter = new Coordinate();
            cam = new Coordinate(0, 0);
            spd = new Coordinate();
            this.pen = new Coordinate(pen.getX(), pen.getY());
            screenSize = new Coordinate(dw, dh);
            //this.maxFreeSpeed=new Coordinate(dw/10,dh/10);
        }

        public void update(Coordinate pen) {
            Coordinate spd = this.pen.sub(pen);
            if (spd.getY() == 0) {
//                if(counter>7) {
//                double diffY=camCenter.getY() - cam.getY();
//                if (diffY!=0) {
//                    if (diffY > 0)
//                        this.spd.setY(Math.min(diffY, this.spd.getY() + (diffY) / 18.0));
//                    else {
//                        this.spd.setY(Math.max(diffY, this.spd.getY() + (diffY) / 18.0));
//                    }
//                } else {
//
//                    if(this.spd.getY()<0.5){
//                        this.spd.setY(0);
//                    }
////                    this.spd.setY(this.spd.getY() - (this.spd.getY()) / 30.0);
//                }
//                }
//                else{
//                    this.spd.setY(this.spd.getY()*0.9);
//                    counter++;
//                }
                double diffY = camCenter.getY() - cam.getY();
                if (diffY > 0) {
                    if (counter < 1) {
                        counter++;
                    } else {
                        this.spd.setY(Math.min(diffY, this.spd.getY() + (diffY) / 45.0));
                    }
                } else {
                    if (diffY < 0 && this.spd.getY() > 0) {
                        counter = 2;
                        this.spd.setY(diffY);
                    }
                    if (diffY == 0 && counter != 0) {
                        this.spd.setY(0);
                    }
                }
            } else {//250 //1400 //height dw * 500 / 850
                counter = 0;
                Coordinate drawPen = pen.add(cam);
                if (drawPen.getY() + screenSize.getX() / 2 > screenSize.getY() * 0.85 && spd.getY() < 0 || drawPen.getY() < screenSize.getY() * 0.2 && spd.getY() > 0) {
                    if ((drawPen.getY() + screenSize.getX() / 2 > screenSize.getY() * 0.999 && spd.getY() < 0) || (drawPen.getY() < screenSize.getY() * 0.03 && spd.getY() > 0)) {
                        if (spd.getY() > 0) {
                            double needSpeed = drawPen.getY() - screenSize.getY() * 0.1;
                            this.spd.setY(Math.min(-needSpeed, this.spd.getY() - needSpeed / 18));
                        } else {
                            double needSpeed = drawPen.getY() + screenSize.getX() / 2 - screenSize.getY() * 0.95;
                            if (this.spd.getY() < needSpeed)
                                this.spd.setY(Math.max(-needSpeed, this.spd.getY() - needSpeed / 18));
                        }
                    } else {
                        if (spd.getY() > 0) {
                            this.spd.setY(Math.min(spd.getY(), this.spd.getY() + spd.getY() / 9));
                        } else {
                            if (this.spd.getY() < spd.getY())
                                this.spd.setY(Math.max(spd.getY(), this.spd.getY() + spd.getY() / 9));
                        }
                    }
                }
            }

            cam = cam.add(this.spd);

            // если скорость = 0 возвращаем камеру, чтобы пингвин был по центру
            // если скорость не = 0 проверяем, не вышел ли пингвин за границы экрана
            // если не вышел - ничего не делаем
            // если вышел - начинаем увеличивать скорость, пока не сравняется со скоростью пингвина

            //два уровня ограничений экрана: когда пингвин проходит первую границу, камера начинает ускоряться, чтобы следовать за ним, когда достигает второй границы, камера ускоряется до такой степени, чтобы вернуть пингвина к первой границе


            this.pen = pen;
        }

        public Coordinate getCam() {
            return cam;
        }
    }

    public static class Coordinate {

        public static Coordinate[] arrayOf(double... coor) {
            if (coor.length % 2 == 1)
                throw new ArrayIndexOutOfBoundsException();
            Coordinate[] array = new Coordinate[coor.length / 2];
            for (int i = 0; i < coor.length / 2; i++) {
                array[i] = new Coordinate(coor[i * 2], coor[i * 2 + 1]);

            }
            return array;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        double x;
        double y;

        public Coordinate() {
            this.x = 0.0;
            this.y = 0.0;
        }

        public Coordinate(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Coordinate sub(Coordinate coodr) {
            return new Coordinate(this.x - coodr.x, this.y - coodr.y);
        }

        public Coordinate add(Coordinate coodr) {
            return new Coordinate(this.x + coodr.x, this.y + coodr.y);
        }
    }
}