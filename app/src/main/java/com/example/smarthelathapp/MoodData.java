package com.example.smarthelathapp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MoodData {

     private int EDA;
     private int HR;
     private long currentDate;

     public MoodData(){

     }

    public MoodData(int EDA, int HR, long currentDate) {
        this.EDA = EDA;
        this.HR = HR;
        this.currentDate = currentDate;
    }

    public int getEDA() {
        return EDA;
    }

    public void setEDA(int EDA) {
        this.EDA = EDA;
    }

    public int getHR() {
        return HR;
    }

    public void setHR(int HR) {
        this.HR = HR;
    }

    public long getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(long currentDate) {
        this.currentDate = currentDate;
    }
}
