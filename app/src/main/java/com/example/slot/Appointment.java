package com.example.slot;

import java.util.HashMap;
import java.util.Map;

public class Appointment {

    private int startHour, startMinute, endHour, endMinute, interval;
    private int day,month,year;
    // {date, isAvailable}
    private Map<String, Boolean> slot;

    public Appointment(int startHour, int startMinute, int endHour, int endMinute, int interval, int day, int month, int year) {
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.interval = interval;
        this.day = day;
        this.month = month;
        this.year = year;
        this.slot = new HashMap<>();

        initSlotMAp();
    }

    private void initSlotMAp() {
        int startH = this.startHour;
        int startM = this.startMinute;
        String startTime = startHour + ":" + startMinute;

        int numberOfSlots = calculateTime();
//        HH:mm - HH:mm + interval
        int hh = startH;
        int mm = startM ;
        for (int i = 0; i < numberOfSlots ; i++) {
            int temp = mm + interval;
            if (temp >= 60){
                temp = temp - 60;
                hh++;
                mm = temp;
            }
            else
            {
                mm += interval;
            }

            String endTime = hh + ":" + mm;
            slot.put(startTime + "-" + endTime, true);
            startTime = endTime;
        }
        System.out.println(slot);
    }

    private int calculateTime() {
        int minOfApp = (endHour - startHour) * 60 + (endMinute - startMinute);
        return minOfApp / interval;


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

    public static void main(String[] args) {
        Appointment appointment = new Appointment(7,30,8,30,15,1,1,1);

    }
}
