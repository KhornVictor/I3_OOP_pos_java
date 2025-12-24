package main.com.pos.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTime {
    private LocalDate date;
    private LocalTime time;

    public DateTime() {
        this.date = LocalDate.now();
        this.time = LocalTime.now();
    }

    public DateTime(LocalDate date, LocalTime time){
        setDate(date);
        setTime(time);
    }

    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }

    public final void setDate(LocalDate date) { 
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        this.date = date; 
    }
    public final void setTime(LocalTime time) { 
        if (time == null) {
            throw new IllegalArgumentException("Time cannot be null");
        }
        this.time = time; 
    }

    public LocalDateTime getDateTime() {
        return LocalDateTime.of(date, time);
    }

    @Override
    public String toString() {
        return "DateTime{" +
                "date=" + date +
                ", time=" + time +
                '}';
    }
}