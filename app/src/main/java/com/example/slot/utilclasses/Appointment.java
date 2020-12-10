package com.example.slot.utilclasses;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Appointment implements Serializable {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Duration interval;
    private Duration totalDuration;
//  private Duration slotDuration;
    private Map<String, Boolean> slotsMap;

    public Appointment() {
        // Default constructor required for calls to DataSnapshot.getValue(Appointment.class)
    }

    public Appointment(int startHour, int startMinute, int endHour, int endMinute, int interval, int day, int month, int year) {
        this.date = LocalDate.of(year, month, day);
        this.startTime = LocalTime.of(startHour, startMinute);
        this.endTime = LocalTime.of(endHour, endMinute);
        this.interval = Duration.ofMinutes(interval);
        this.totalDuration = calculateDuration(startTime, endTime);
        this.slotsMap = new LinkedHashMap<>();
        calculateSlotsMap();
/*
        Uncomment and run see whats happening
        private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT,FormatStyle.SHORT);
        private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
        private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        System.out.println("Appointment date: " + this.localDate.format(dateFormatter));
        System.out.println("Start time: " + this.startTime.format(timeFormatter));
        System.out.println("EndTime: " + this.endTime.format(timeFormatter));
        System.out.println("Duration: " + this.totalDuration.toMinutes());
        System.out.println("Interval: " + this.interval);
        System.out.println("slots: " + this.slotDuration.toMinutes());
        System.out.println("slotsMap: " + this.slotsMap);
*/
    }


    private Duration calculateDuration(Temporal start, Temporal end) {
        return Duration.between(start, end);
    }
    //    Calculate the interval between slots
//    Map
//    {
//    { 'Time interval' : true}
//    { 'Time Interval 2' : true}
//      ...
//     }

    private void calculateSlotsMap() {
        LocalTime startTime = LocalTime.from(this.startTime);
        LocalTime endTime = LocalTime.from(this.endTime);

        while (startTime.isBefore(endTime)) {
            LocalTime slotEnd = startTime.plusMinutes(this.interval.toMinutes());
            String key = "";
            if (slotEnd.isBefore(endTime))
                key = startTime.toString() + " - " + slotEnd.toString();
            else
                key = startTime.toString() + " - " + endTime.toString();
            this.slotsMap.put(key, true);
            startTime = slotEnd;
        }
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("date", this.date.toString());
        result.put("startTime", this.startTime.toString());
        result.put("endTime", this.endTime.toString());
        result.put("totalDuration", this.totalDuration.toString());
        result.put("interval", this.interval.toString());
        result.put("slots", this.slotsMap);
        return result;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Duration getInterval() {
        return interval;
    }

    public void setInterval(Duration interval) {
        this.interval = interval;
    }

    public Duration getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Duration totalDuration) {
        this.totalDuration = totalDuration;
    }

    public Map<String, Boolean> getSlotsMap() {
        return slotsMap;
    }

    public void setSlotsMap(Map<String, Boolean> slotsMap) {
        this.slotsMap = slotsMap;
    }

    public static void main(String[] args) {
        Appointment appointment = new Appointment(7, 30, 8, 30, 22, 1, 1, 1);
        System.out.println(appointment.toMap());
    }
}

