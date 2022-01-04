package com.pf.application;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Animation {
    protected static one[] steps;
    public static byte target;

     public static void init(Context context,int dw){

steps =new one[30];
     /*
         bitmapId = R.drawable.pen1_fly; // определяем начальные параметры
         cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
         bitmap1 = Bitmap.createScaledBitmap(
                 cBitmap, dw/2, dw/2, false);
         cBitmap.recycle();

         bitmapId = R.drawable.p0;//f0;// определяем начальные параметры
         cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
         steps[0] = Bitmap.createScaledBitmap(
                 cBitmap, dw/2, dw/2, false);
         cBitmap.recycle();

         bitmapId = R.drawable.p1;//f1;// определяем начальные параметры
         cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
         steps[1] = Bitmap.createScaledBitmap(
                 cBitmap, dw/2, dw/2, false);
         cBitmap.recycle();
         steps[6] = steps[1];
         //cBitmap.recycle();

         bitmapId = R.drawable.p1;//f2;// определяем начальные параметры
         cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
         steps[2] = Bitmap.createScaledBitmap(
                 cBitmap, dw/2, dw/2, false);
         cBitmap.recycle();
         steps[5] = steps[2];
         //cBitmap.recycle();

         bitmapId = R.drawable.p2;//f3;// определяем начальные параметры
         cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
         steps[3] = Bitmap.createScaledBitmap(
                 cBitmap, dw/2, dw/2, false);
         cBitmap.recycle();
         steps[4] = steps[3];
*/



         Bitmap cBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.p0);
         Bitmap bm = Bitmap.createScaledBitmap(
                 cBitmap, dw/2, dw/2, false);
         cBitmap.recycle();
//стоит на месте:
         steps[0]=new one(bm,1,-1,false);
         steps[1]=new one(bm,2,-1,false);
         steps[2]=new one(bm,3,-1,false);
         steps[3]=new one(bm,0,-1,false);
         //прыгает
         steps[10]=new one(bm,11,-1,false);//чуть приподнял крылья
         steps[11]=new one(bm,12,-1,false);//сильнее приподнял крылья, задрал голову немного
         steps[12]=new one(bm,13,-1,false);//крылья и голова подняты максимально
         steps[13]=new one(bm,14,-1,true);//опустил крылья немного, ноги наклонены
         steps[14]=new one(bm,20,30,false);//опустил крылья ещё сильнее

         //набор высоты
         steps[20]=new one(bm,20,40,false);

         //взмах крыльями
         steps[30]=new one(bm,31,40,false);
         steps[31]=new one(bm,32,40,false);
         steps[32]=new one(bm,33,40,false);
         steps[33]=new one(bm,30,20,true);

         //падение

    }
    protected static class one{
        public byte next;
        protected Bitmap view;
        protected boolean to_do;
        protected byte alt_next;
        protected byte steps_to_switch;


       /* public one(Bitmap view,byte next,byte alt_next, boolean to_do){
            this.view=view;
            this.next=next;
            this.alt_next=alt_next;
            this.to_do=to_do;
        }*/


        public one(Bitmap bm, int next, int alt_next, boolean to_do) {
            this.view=view;
            this.next=(byte)next;
            this.alt_next=(byte)alt_next;
            this.to_do=to_do;
        }
    }

}
