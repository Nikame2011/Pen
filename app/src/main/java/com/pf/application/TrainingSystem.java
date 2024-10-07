package com.pf.application;

import android.view.View;

import com.pf.application.TrainingSystem.Training.Level;

import java.util.Map;

public class TrainingSystem {

    interface TrainingListener{
        public abstract void OnDescriptionChanged();
    }


public void updateValues(View parent){

}

public View.OnClickListener trainingListener=new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        int id=  view.getId();
        for(int training:trainings.keySet()){
            if(id==training){

                break;
            }
        }
    }
};
    //protected float[] time_to_up = new float[]{5, 5, 10, 15, 15, 30, 30, 30, 60, 60, 60, 90, 90, 120, 150, 180, 180, 210, 210, 210, 240, 240, 300, 360, 360, 420, 480, 540, 600, 660, 720, 780};

    static Map<Integer, Training> trainings = Map.of(
            R.id.btnTrJump, new Training(TypeT.Legs, R.string.jump,
                    new Level[]{
                            new Level(0.01,5,0.125),
                            new Level(0.01,10,0.164),
                            new Level(0.01,15,0.207),
                            new Level(0.01,15,0.257),
                            new Level(0.01,30,0.31),
                            new Level(0.01,30,0.37),
                            new Level(0.01,30,0.467),
                            new Level(0.01,60,0.87),
                            new Level(0.01,60,0.98),
                            new Level(0.01,60,4.45),
                            new Level(0.01,90,23.685),
                            new Level(0.01,90,32.7),
                            new Level(0.01,120,55.4),
                            new Level(0.01,150,56.3),
                            new Level(0.01,180,65.0),
                            new Level(0.01,180,77.6),
                            new Level(0.01,210,103.1),
                            new Level(0.01,210,153.3),
                            new Level(0.01,210,195.2)
            }),

            R.id.btnTrFlap, new Training(TypeT.Wing,R.string.flap,
                    new Level[]{
                            new Level(0.01,30,0.43),
                            new Level(0.01,30,0.54),
                            new Level(0.01,60,0.58),
                            new Level(0.01,60,0.63),
                            new Level(0.01,60,0.68),
                            new Level(0.01,90,0.74),
                            new Level(0.01,90,0.807),
                            new Level(0.01,120,1.47),
                            new Level(0.01,150,1.88),
                            new Level(0.01,180,3.396),
                            new Level(0.01,180,3.9),
                            new Level(0.01,210,4.729),
                            new Level(0.01,210,13.08),
                            new Level(0.01,210,24.3),
                            new Level(0.01,240,28.3),
                            new Level(0.01,240,33.4),
                            new Level(0.01,300,57.3),
                            new Level(0.01,360,78.8),
                            new Level(0.01,360,119.9),
                            new Level(0.01,420,175.0)
            }),

            R.id.btnTrJogging, new Training(TypeT.Energy,R.string.jogging,
                    new Level[]{
                            new Level(1,60,1.09,R.id.btnTrFlap,1),
                            new Level(1,90,2.366),
                            new Level(1,90,5.339),
                            new Level(1,120,7.57),
                            new Level(1,150,10.155),
                            new Level(1,180,15.27),
                            new Level(1,180,19.25),
                            new Level(1,210,38.1),
                            new Level(1,210,46.4),
                            new Level(1,210,66.0),
                            new Level(1,240,88.7),
                            new Level(1,240,104.4),
                            new Level(1,300,134.4),
                            new Level(1,360,154.8),
                            new Level(1,360,200.0),
//                            new Level(1,420,175.0),
//                            new Level(1,480,0.43),
//                            new Level(1,540,0.54),
//                            new Level(1,600,0.58),
//                            new Level(1,660,0.63)
            })
    );

    enum TypeT {
        Legs,
        Wing,
        Energy,
        Concentration
    }

    public static class Training {
        TypeT trainingType;
        Level[] levels;
        int name;

        public Training(TypeT type, int name, Level[] levels){
            this.trainingType=type;
            this.name = name;
            this.levels=levels;
        }

        public static class Level {
            double bustValue;
            double time;
            double needRecord;
            int needTraining = -1;
            int needTrainingLevel = -1;

            public Level(double bustValue, double time, double needRecord) {
                this.bustValue = bustValue;
                this.needRecord = needRecord;
                this.time=time;
            }

            public Level(double bustValue, double time, double needRecord, int needTraining, int needTrainingLevel) {
                this.bustValue = bustValue;
                this.needRecord = needRecord;
                this.time=time;
                this.needTraining = needTraining;
                this.needTrainingLevel = needTrainingLevel;
            }
        }
    }
}
