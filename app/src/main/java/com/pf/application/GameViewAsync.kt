package com.pf.application

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.preference.PreferenceManager
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.pf.application.Penguin.MainListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.math.max

class GameViewAsync(context: Context?, listener: MainListener?) : SurfaceView(context) {
    //инициализируем обьекты для рисования
    private val surfaceHolder: SurfaceHolder = holder

    //MainActivity.width=surfaceHolder.getSurfaceFrame().width();
    private val paint = Paint()

    // private val gameThread: Thread
    var pen: Penguin
    var firstTime: Boolean = true

    protected var fone0: Bitmap // картинка
    protected var fone1: Bitmap // картинка

    //protected Bitmap back2; // картинка
    protected var back3: Bitmap? = null // картинка
    protected var back4: Bitmap? // картинка
    protected var false_button: Bitmap // картинка
    protected var menu_box: Bitmap // картинка
    protected var qu: Bitmap // картинка
    protected var menuBack: Bitmap // картинка

    protected var bitmapId: Int // id картинки

    // if (firstTime) { // инициализация при первом запуске
    //MainActivity.width=surfaceHolder.getSurfaceFrame().width();
    var dw: Int = MainActivity.dw
    var dh: Int = MainActivity.dh
    var rec: Float = 0f
    var time: Date? = null

    var decFone0: BitmapRegionDecoder? = null
    var decFone1: BitmapRegionDecoder? = null

    fun run() {
        CoroutineScope(Dispatchers.Default).launch {
            control_date = Date()


            //        while (MainActivity.Setup.getY() == 0) {
//            control();
//        }
//        MainActivity.end = MainActivity.Setup.getY() + dw / 4f + dw / 100f;
            firstTime = false
            //boolean gameRunning = true;
            while (true /*gameRunning*/) {
                //MainActivity.end=MainActivity.Setup.getY()+dw/4+dw/100;

                val startDate = Date()
                update()
                draw()

                // if (quest!=0)question();
                //control();
                tick += 2
                if (tick >= 90) tick = 0

                val finishDate = Date()
                superControl(startDate, finishDate)


                if (MainActivity.new_game) {
                    new_game()
                    MainActivity.new_game = false
                }
            }
            //close();
        }
    }

    private suspend fun update() {
        if (!firstTime) {
            pen.update()
        }
        prepareBitmaps()
    }

    fun new_game() {
        pen.new_game()
    }

    fun close() {
        //pen.close();
        //back1 = null;
        //back2 = null;
        back3 = null
        back4 = null
    }

    var lastPenY: Float=0.0f

    var defFone0: Deferred<Bitmap>? = null
    var defFone1: Deferred<Bitmap>? = null

    private suspend fun prepareBitmaps() {

            defFone0 = CoroutineScope(Dispatchers.Default).async {
                if (pen.y != lastPenY) {
                    if (pen.y <= dh / 8f && pen.y >= dh - dw * 10.2) {
                        val he = decFone0!!.height
                        val wi = decFone0!!.width
                        //he==10*dw
                        val x = (he * dh * 0.85f / 10 / dw).toInt()

                        val move = (he * (dh / 8f - pen.y) / 10 / dw).toInt()
                        val tempBit = decFone0!!.decodeRegion(
                            Rect(
                                wi / 4,
                                he - x - move,
                                wi * 3 / 4,
                                he - move
                            ), BitmapFactory.Options()
                        )
                        fone0 = Bitmap.createScaledBitmap(
                            tempBit, dw, (dh * 0.85f).toInt(), false
                        )
                    } else if (pen.y > dh / 8f) {
                        if (lastPenY <= dh / 8f) {
                            val he = decFone0!!.height
                            val wi = decFone0!!.width
                            //he==10*dw
                            val x = (he * dh * 0.85f / 10 / dw).toInt()

                            val tempBit = decFone0!!.decodeRegion(
                                Rect(wi / 4, he - x, wi * 3 / 4, he),
                                BitmapFactory.Options()
                            )
                            fone0 = Bitmap.createScaledBitmap(
                                tempBit, dw, (dh * 0.85f).toInt(), false
                            )
                        }
                    }
                }

                return@async fone0
            }

            defFone1 = CoroutineScope(Dispatchers.Default).async {
                if (pen.y != lastPenY) {
                    if (pen.y > dh - dw * 87.2 && pen.y <= dh - dw * 5.4) {
                        //canvas.drawBitmap(back2, 0, (float) ((dh / 8f - pen.y) / 8 + dh - dw * 10.7), paint);

                        val he = decFone1!!.height
                        val wi = decFone1!!.width
                        //he==10*dw
                        val x = (he * dh * 0.085f / dw).toInt()

                        val move = (he * (dh / 8f - pen.y) / 80 / dw).toInt()
                        val tempBit = decFone1!!.decodeRegion(
                            Rect(
                                wi / 4,
                                he - x - move,
                                wi * 3 / 4,
                                he - move
                            ), BitmapFactory.Options()
                        )
                        fone1 = Bitmap.createScaledBitmap(
                            tempBit, dw, (dh * 0.85f).toInt(), false
                        )
                    }
                }
                return@async fone1
            }

    }

    private suspend fun draw() {
        try {
            if (surfaceHolder.surface.isValid) {  //проверяем валидный ли surface

                    val canvas: Canvas = surfaceHolder.lockCanvas() // закрываем canvas

                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

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
                    val st = MainActivity.end - dw / 100f

                    //                if (pen.y <= dh / 8f & pen.y >= dh - dw * 10.2)
//                    canvas.drawBitmap(back1, 0, (float) ((dh / 8f - pen.y) + st - dw * 10.2), paint);
//                else if (pen.y > dh / 8f)
//                    canvas.drawBitmap(back1, 0, (float) (st - dw * 10.2), paint);

//TODO add lruCache
//                if (pen.y != lastPenY) {
//                    if (pen.y > dh - dw * 87.2 && pen.y <= dh - dw * 5.4) {
//                        //canvas.drawBitmap(back2, 0, (float) ((dh / 8f - pen.y) / 8 + dh - dw * 10.7), paint);
//
//                        val he = decFone1!!.height
//                        val wi = decFone1!!.width
//                        //he==10*dw
//                        val x = (he * dh * 0.085f / dw).toInt()
//
//                        val move = (he * (dh / 8f - pen.y) / 80 / dw).toInt()
//                        val tempBit = decFone1!!.decodeRegion(
//                            Rect(
//                                wi / 4,
//                                he - x - move,
//                                wi * 3 / 4,
//                                he - move
//                            ), BitmapFactory.Options()
//                        )
//                        fone1 = Bitmap.createScaledBitmap(
//                            tempBit, dw, (dh * 0.85f).toInt(), false
//                        )
//                    }
//                    if (pen.y <= dh / 8f && pen.y >= dh - dw * 10.2) {
//                        val he = decFone0!!.height
//                        val wi = decFone0!!.width
//                        //he==10*dw
//                        val x = (he * dh * 0.85f / 10 / dw).toInt()
//
//                        val move = (he * (dh / 8f - pen.y) / 10 / dw).toInt()
//                        val tempBit = decFone0!!.decodeRegion(
//                            Rect(
//                                wi / 4,
//                                he - x - move,
//                                wi * 3 / 4,
//                                he - move
//                            ), BitmapFactory.Options()
//                        )
//                        fone0 = Bitmap.createScaledBitmap(
//                            tempBit, dw, (dh * 0.85f).toInt(), false
//                        )
//                    } else if (pen.y > dh / 8f) {
//                        if (lastPenY <= dh / 8f) {
//                            val he = decFone0!!.height
//                            val wi = decFone0!!.width
//                            //he==10*dw
//                            val x = (he * dh * 0.85f / 10 / dw).toInt()
//
//                            val tempBit = decFone0!!.decodeRegion(
//                                Rect(wi / 4, he - x, wi * 3 / 4, he),
//                                BitmapFactory.Options()
//                            )
//                            fone0 = Bitmap.createScaledBitmap(
//                                tempBit, dw, (dh * 0.85f).toInt(), false
//                            )
//                        }
//                    }
//                }

                    if (pen.y <= dh - dw * 5.4 && pen.y > dh - dw * 87.2) {
                        canvas.drawBitmap(
                            defFone1!!.await(),
                            0f,
                            0f,
                            paint
                        ) //canvas.drawBitmap(back2, 0, (float) ((dh / 8f - pen.y) / 8 + dh - dw * 10.7), paint);
                    }
                    if (pen.y >= dh - dw * 10.2) {
                        canvas.drawBitmap(defFone0!!.await(), 0f, 0f, paint)
                    }

                    //else  if (p  en.y<=-dh/8& pen.y<=-dw*10.2)  canvas.drawBitmap(back1, 0, (float) ((dh/8-pen.y)+dh-dw*10.2), paint);

                    //canvas.drawBitmap(menuBack, 0, st - dw / 4f - dw / 100f, paint);
                    pen.drow(paint, canvas) // рисуем пингвина и меню

                    //canvas.drawBitmap(false_button, dw * 3 / 4f - dw / 100f, st - dw / 4f, paint);
                    //canvas.drawBitmap(menu_box, dw / 4f + dw / 50f, st - dw / 4f, paint);
                    //canvas.drawBitmap(menu_box, dw / 4f + dw / 50f, st - dw / 8f, paint);
                    canvas.drawText("incr: $increment", 100f, 100f, paint)
                    canvas.drawText("frames: $frames", 100f, 200f, paint)
                    /*if (quest!=0){
                canvas.drawBitmap(qu, -dw*3/4, -dw/7/4, paint);

            }*/
                    surfaceHolder.unlockCanvasAndPost(canvas) // открываем canvas
                }
        } catch (e: Exception) {
        }
    }

    var saveTicker: Long = 0
    var cadres: Float = 30f
    var frames: Byte = 30

    var increment: Float = (1000000 / 60).toFloat()

    var control_date: Date? = null
    var d: Date? = null
    var controlTick: Int = 0

    private fun control() { // пауза и контроль количества кадров
//       try {
//           gameThread.sleep(12);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        d = Date()
        controlTick++
        if (((d!!.time - control_date!!.time) % 50 > 0) and (saveTicker != (d!!.time - control_date!!.time) % 50) && controlTick > 0) {
            saveTicker = (d!!.time - control_date!!.time) % 50
            frames =
                (controlTick * 1000 / (d!!.time - control_date!!.time).toFloat()).toInt().toByte()

            increment = (max((increment * (frames.toFloat()) / 30f).toDouble(), 0.01)
                .toFloat())


            //cadres=Math.min(Math.max(frames<40? cadres/2:frames>=60? cadres*2:cadres, 2),60);


            /*     if(frames<55)
                cadres=30;
            else if(increment>1000)
                cadres=60;*/
            if (d!!.time - control_date!!.time >= 1000) {
                saveTicker = 0
                controlTick = 0
                control_date = Date()
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
            Thread.sleep((increment.toInt() / 1000).toLong(), increment.toInt() % 1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    var framesTarget: Byte = 17
    var frameDelay: Int = (1000 / framesTarget).toShort().toInt()
    var delayDebt: Int = 0

    //    private Bitmap decodeFone(BitmapRegionDecoder decoder, int fullTargetHeight, int fullTargetWidth, int){
    //
    //    }
    init {
        try {
            var `is` = resources.openRawResource(+R.drawable.back1) //+R.drawable.aice0);//
            decFone0 = BitmapRegionDecoder.newInstance(`is`, false)
            `is` = resources.openRawResource(+R.drawable.back2) //+R.drawable.aice0);//
            decFone1 = BitmapRegionDecoder.newInstance(`is`, false)
        } catch (e: Throwable) {
        }

        lastPenY = MainActivity.end - dw / 4f - dw / 25f - dw / 2f

        val he = decFone0!!.height
        val wi = decFone0!!.width
        val x = (he * dh * 0.85f / 10 / dw).toInt()

        var tempBit =
            decFone0!!.decodeRegion(Rect(wi / 4, he - x, wi * 3 / 4, he), BitmapFactory.Options())
        fone0 = Bitmap.createScaledBitmap(
            tempBit!!, dw, (dh * 0.85f).toInt(), false
        )

        tempBit =
            decFone1!!.decodeRegion(Rect(wi / 4, he - x, wi * 3 / 4, he), BitmapFactory.Options())
        fone1 = Bitmap.createScaledBitmap(
            tempBit, dw, (dh * 0.85f).toInt(), false
        )


        //        bitmapId = R.drawable.back1;
//        Bitmap cBitmap = BitmapFactory.decodeResource(getContext().getResources(), bitmapId);
//        back1 = Bitmap.createScaledBitmap(
//                cBitmap, dw, dw * 10, false);
//        cBitmap.recycle();

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
        bitmapId = R.drawable.back4
        var cBitmap = BitmapFactory.decodeResource(getContext().resources, bitmapId)
        back4 = Bitmap.createScaledBitmap(
            cBitmap, dw, dh, false
        )
        cBitmap.recycle()

        bitmapId = R.drawable.imb
        cBitmap = BitmapFactory.decodeResource(getContext().resources, bitmapId)
        false_button = Bitmap.createScaledBitmap(
            cBitmap, dw / 4, dw / 4, false
        )

        menu_box = Bitmap.createScaledBitmap(
            cBitmap, dw / 2 - dw / 25, dw / 8, false
        )
        cBitmap.recycle()

        bitmapId = R.drawable.quest
        cBitmap = BitmapFactory.decodeResource(getContext().resources, bitmapId)
        qu = Bitmap.createScaledBitmap(
            cBitmap, dw * 7 / 4, dw * 15 / 4, false
        )
        cBitmap.recycle()

        bitmapId = R.drawable.img_btns
        cBitmap = BitmapFactory.decodeResource(getContext().resources, bitmapId)
        menuBack = Bitmap.createScaledBitmap(
            cBitmap, dw, dw / 4 + dw / 50, false
        )
        cBitmap.recycle()

        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val n_j = myPreferences.getInt("jump", 1)

        val n_b = myPreferences.getInt("bust", 0)

        //n_b=1;
        val n_e = if (n_b <= 1) {
            1
        } else {
            myPreferences.getInt("energy", 1)
        }

        val vers = myPreferences.getString("version", "0.1.0.0")

        rec = if (vers == "0.1.0.0") 0f
        else myPreferences.getFloat("record", 0f)


        //int headers= myPreferences.getInt("headers_count", 0);
        // if (headers>0){
        //myPreferences.getAll("header");
        // }
        val tu: Float
        if (MainActivity.update == -1) {
            tu = -1f
            time = MainActivity.first_date
        } else {
            val rd_time = myPreferences.getLong("strt_date", MainActivity.first_date.time)
            time = Date(rd_time)
            tu = myPreferences.getFloat("time_to_up", -1f)
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
        pen = Penguin(
            getContext(),
            n_j.toByte(),
            n_b.toByte(),
            n_e.toByte(),
            rec,
            time,
            tu,
            listener
        ) // добавляем пингвина

        // инициализируем поток
//        gameThread = Thread(this)
//        gameThread.start()
        run()
    }

    private fun superControl(
        startDate: Date,
        finishDate: Date
    ) { // пауза и контроль количества кадров

        controlTick++

        val delay = max((finishDate.time - startDate.time + delayDebt).toDouble(), 0.0)
            .toInt()

        if (delay < frameDelay) {
            try {
                Thread.sleep((frameDelay - delay).toLong())
                increment = (frameDelay - delay).toFloat()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        } else {
            increment = 0f
            delayDebt = (delay - frameDelay)
        }
        d = Date()
        if (d!!.time - control_date!!.time >= 500) {
            frames = (controlTick * 2).toByte()
            if (frames > framesTarget || delayDebt > 500) delayDebt = 0
            controlTick = 0
            control_date = Date()
        }
    }

    companion object {
        var tick: Int = 0
    }
}