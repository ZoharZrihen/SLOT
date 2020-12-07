package com.example.slot;

import java.util.HashMap;

public class Appointment {

    private int startHour, startMinute, endHour, endMinute, interval;
    private int day,month,year;
   // private HashMap<> slot;
    public Appointment(){

    }

    public Appointment(int startHour, int startMinute, int endHour, int endMinute, int interval, int day, int month, int year) {
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.interval = interval;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public int getInterval() {
        return interval;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}
