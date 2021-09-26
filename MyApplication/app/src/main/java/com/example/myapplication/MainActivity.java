package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    public static boolean flying=false;
    public static boolean setup=false;
    public static int update=-1;
    public Button Fly;
    public Button Setup;
    public static ImageButton Up_fly;
    public static ImageButton Up_jump;
    public static ImageButton Up_energy;
    //public static int width=0;
    public static int dw,dh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GameView gw= new GameView(this);
        dw= getResources().getDisplayMetrics().widthPixels;//получаем ширину экрана
        dh= getResources().getDisplayMetrics().heightPixels;//получаем ширину экрана

        LinearLayout gameLayout = (LinearLayout) findViewById(R.id.GL); // находим gameLayout
        ConstraintLayout.LayoutParams l= (ConstraintLayout.LayoutParams) gameLayout.getLayoutParams();
        l.height=dh;
        l.width=dw;
        gameLayout.setLayoutParams(l);
        gameLayout.addView(gw); // и добавляем в него gameView


        //LinearLayout setupLayout = (LinearLayout) findViewById(R.id.SL); // находим gameLayout

        //setupLayout.addView(gw); // и добавляем в него gameView

        Fly = (Button) findViewById(R.id.B1);
        Setup = (Button) findViewById(R.id.B2);
        Up_fly = (ImageButton) findViewById(R.id.Up_fly);
        Up_jump = (ImageButton) findViewById(R.id.Up_jump);
        Up_energy = (ImageButton) findViewById(R.id.Up_energy);


        ConstraintLayout.LayoutParams par= (ConstraintLayout.LayoutParams)Fly.getLayoutParams();
        par.width=dw/4;
        par.height=dw/4;
        par.rightMargin =dw/10;
        par.bottomMargin=dw/10;
        Fly.setLayoutParams (par);

        par= (ConstraintLayout.LayoutParams)Up_energy.getLayoutParams();
        par.width=dw/4;
        par.height=dw/4;
        par.rightMargin=dw/16;
        par.topMargin=dw/10;
        Up_energy.setLayoutParams (par);

        par= (ConstraintLayout.LayoutParams)Up_jump.getLayoutParams();
        par.width=dw/4;
        par.height=dw/4;
        par.leftMargin=dw/16;
        par.topMargin=dw/10;

        Up_jump.setLayoutParams (par);

        par= (ConstraintLayout.LayoutParams)Up_fly.getLayoutParams();
        par.width=dw/4;
        par.height=dw/4;
        //par.leftMargin=dw/16;
        par.topMargin=dw/10;
        Up_fly.setLayoutParams (par);

        par= (ConstraintLayout.LayoutParams)Setup.getLayoutParams();
        par.width=dw/4;
        par.height=dw/3;
        //par.leftMargin=dw/16;
        par.bottomMargin=dw/15;
        Setup.setLayoutParams (par);




        //par.width=dw/4;
        //par.height=dw/4;
        //Fly.setLayoutParams (par);

        //Fly.setLayoutParams (new ConstraintLayout.LayoutParams(100,100));
        //Fly.getLayoutParams();
        //Fly.setLayoutParams (Fly.getLayoutParams());
        //ViewGroup.LayoutParams

        //while (width==0)
        //{}
        //int displaywidth= getResources().getDisplayMetrics().widthPixels;

        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10,20);
        //Fly.setLayoutParams(params);

        //MainActivity.Fly.setHeight(10);
        //MainActivity.Fly.setWidth(20);

       // Button btn = (Button)findViewById(R.id.B1);

        //android.widget.LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,60); // 60 is height you can set it as u need

        //btn.setLayoutParams(lp);
        //Fly.setLayoutParams (new LinearLayout.LayoutParams(50, LinearLayout.LayoutParams.WRAP_CONTENT));




        //Up_fly.setEnabled(false);
        //Up_jump.setEnabled(false);
        //Up_energy.setEnabled(false);

        Up_fly.setVisibility(View.INVISIBLE);
        Up_jump.setVisibility(View.INVISIBLE);
        Up_energy.setVisibility(View.INVISIBLE);

        Fly.setOnTouchListener(this);
        Setup.setOnTouchListener(this);

        Up_fly.setOnTouchListener(this);
        Up_jump.setOnTouchListener(this);
        Up_energy.setOnTouchListener(this);

    }


//    }

    @Override
    public boolean onTouch(View button, MotionEvent motion) {
        switch(button.getId()) { // определяем какая кнопка
            case R.id.B1:
                switch (motion.getAction()) { // определяем нажата или отпущена
                    case MotionEvent.ACTION_DOWN:
                        flying = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        flying = false;
                        break;
                }
                break;
            case R.id.B2:
                switch (motion.getAction()) { // определяем нажата или отпущена
                    case MotionEvent.ACTION_DOWN:

                        setup = !setup;
                        //Button Fly = (Button) findViewById(R.id.B1);
                        //Fly.setEnabled(!setup);
                        //Up_fly.setEnabled(setup);
                        //Up_jump.setEnabled(setup);
                        //Up_energy.setEnabled(setup);
                        if (!setup){
                            Up_fly.setVisibility(View.INVISIBLE);
                            Up_jump.setVisibility(View.INVISIBLE);
                            Up_energy.setVisibility(View.INVISIBLE);
                            Fly.setVisibility(View.VISIBLE);
                        }
                        else{
                            Up_fly.setVisibility(View.VISIBLE);
                            Up_jump.setVisibility(View.VISIBLE);
                            Up_energy.setVisibility(View.VISIBLE);
                            Fly.setVisibility(View.INVISIBLE);
                        }


                        break;
                }
                break;
            case R.id.Up_fly:

                switch (motion.getAction()) { // определяем нажата или отпущена
                    case MotionEvent.ACTION_DOWN:
                        if (update==-1 ) update=1;
                        break;
                }
                break;
            case R.id.Up_jump:

                switch (motion.getAction()) { // определяем нажата или отпущена
                    case MotionEvent.ACTION_DOWN:
                        if (update==-1) update=0;
                        break;
                }
                break;
            case R.id.Up_energy:
                switch (motion.getAction()) { // определяем нажата или отпущена
                    case MotionEvent.ACTION_DOWN:
                        if (update==-1 ) update=2;
                        break;
                }
                break;
        }
        return true;
    }







}