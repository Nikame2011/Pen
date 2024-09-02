package com.pf.application;

import java.util.ArrayList;
import java.util.List;

public class TrainingDescription {
    ArrayList<TrainingDescription> childsDescription=new ArrayList<>();

    public int getChildsSize(){
        return childsDescription.size();
    }
}
