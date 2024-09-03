package com.pf.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;


public class ConfigActivity extends AppCompatActivity implements View.OnClickListener {

    //private AppBarConfiguration appBarConfiguration;
    //private ActivityConfigBinding binding;
    public Button Back;
    public Button New_game;
    //public static Switch Down_toggle;
    public Button Skip_fall;
    public Button Ask_brok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_config);
        //binding = ActivityConfigBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());

        Back = findViewById(R.id.btnCloseConfigs);
        New_game = findViewById(R.id.btnNewGame);
        Skip_fall = findViewById(R.id.btnSkipFall);
        Ask_brok = findViewById(R.id.btnResetTutorial);
//        TableLayout.LayoutParams par= (TableLayout.LayoutParams)Back.getLayoutParams();
        //       par.width=dw/8;
        //       par.height=dw/8;
        /*par.leftMargin=dw/100;
        par.topMargin=dw/100;*/
        //       Back.setLayoutParams (par);
        Back.setOnClickListener(ConfigActivity.this);

        New_game.setOnClickListener(ConfigActivity.this);

        Skip_fall.setOnClickListener(ConfigActivity.this);
        Ask_brok.setOnClickListener(ConfigActivity.this);




        //Down_toggle = (Switch) findViewById(R.id.toggle_down);
        //Down_toggle.setOnTouchListener(ConfigActivity.this);

        if (MainActivity.quick_down) {
            //Down_toggle.setChecked(true);

            Skip_fall.setText(getString(R.string.toggle_down_1));
        } else {
            //Down_toggle.setChecked(false);
            Skip_fall.setText(getString(R.string.toggle_down_0));
        }

    }
    Bundle extras =new Bundle();
    @Override
    public void onClick(View view) {
        int id = view.getId();// определяем какая кнопка
        if (id == R.id.btnCloseConfigs) {
            Intent data=new Intent();
            data.putExtras(extras);
            setResult(Activity.RESULT_OK, data);
            finish();
        } else if (id == R.id.btnNewGame) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                    builder.setTitle(R.string.new_game);
                    builder.setMessage(R.string.shure);
                    builder.setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                        //MainActivity.new_game = true;

                        extras.putBoolean("newGame",true);

                        Intent data=new Intent();
                        data.putExtras(extras);
                        setResult(Activity.RESULT_OK, data);
                        finish();
                    });

                    builder.setNegativeButton(R.string.No, (dialogInterface, i) -> dialogInterface.cancel());
                    builder.create().show();

        } else if (id == R.id.btnResetTutorial) {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle(R.string.ask_brok);
            builder.setMessage(R.string.shure);
            builder.setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                //MainActivity.ask_number = 1;
                extras.putBoolean("resetTutorial",true);
                dialogInterface.cancel();
            });

            builder.setNegativeButton(R.string.No, (dialogInterface, i) -> dialogInterface.cancel());
            builder.create().show();
        } else if (id == R.id.btnSkipFall) {

                if (MainActivity.quick_down == false) {
                    Skip_fall.setText(getString(R.string.toggle_down_1));
                   // MainActivity.quick_down = true;
                    extras.putBoolean("skipFall",true);
                } else {
                    Skip_fall.setText(getString(R.string.toggle_down_0));
                    //MainActivity.quick_down = false;
                    extras.putBoolean("skipFall",false);
                }
                //MainActivity.gw.pen.save();
            }
    }
}