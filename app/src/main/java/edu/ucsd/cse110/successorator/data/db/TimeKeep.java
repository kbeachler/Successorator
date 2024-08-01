package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.MutableLiveData;


import java.sql.Time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;

import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.util.MutableLiveDataSubjectAdapter;

public class TimeKeep implements TimeKeeper {
    private MutableLiveData<LocalDateTime> dateTimeLiveData;
    private int currDay;
    public TimeKeep() {
        dateTimeLiveData = new MutableLiveData<>();
        dateTimeLiveData.setValue(LocalDateTime.now());
    }
    @Override
    public Subject<LocalDateTime> getDateTime() {
        return new MutableLiveDataSubjectAdapter<>(dateTimeLiveData);
    }
    @Override
    public void setDateTime(LocalDateTime dateTime) {
        currDay = dateTime.getDayOfMonth();
        dateTimeLiveData.setValue(LocalDateTime.now());
    }
    public int getDay(TimeKeep timekeep) {
        return timekeep.currDay;
    }
    public String getCurrDate() {

        return dateTimeLiveData.getValue().format(DateTimeFormatter.ofPattern("EEE MM/dd", Locale.US));
    }

    public LocalDateTime getTomorrow(){
        return this.dateTimeLiveData.getValue().plusDays(1);
    }
    public String getNextDay() {
        var date = this.dateTimeLiveData.getValue();
        date = date.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MM/dd", Locale.US);
        String formattedNextDay = date.format(formatter);
        return ("Tomorrow, "+ formattedNextDay);

    }

    public void forwardDateTime() {
        dateTimeLiveData.setValue(dateTimeLiveData.getValue().plusDays(1));
    }

    public int getCurrentYear() {
        LocalDateTime dateTime = dateTimeLiveData.getValue();
        return dateTime.getYear();
    }

    public int getCurrentMonth() {
        LocalDateTime dateTime = dateTimeLiveData.getValue();
        return dateTime.getMonthValue();
    }

    public int getCurrentDayOfMonth() {
        LocalDateTime dateTime = dateTimeLiveData.getValue();
        return dateTime.getDayOfMonth();
    }
    public String getDayOfWeek(){
        return dateTimeLiveData.getValue().format(DateTimeFormatter.ofPattern("EEEE", Locale.US));
    }

    public String getMonthly(){
        LocalDate date = dateTimeLiveData.getValue().toLocalDate();;
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);

        // Iterate through the days of the month
        for (int i = 1; i <= firstDayOfMonth.lengthOfMonth(); i++) {
            LocalDate currentDate = firstDayOfMonth.withDayOfMonth(i);
            if (currentDate.getDayOfWeek().equals(date.getDayOfWeek()) && currentDate.getDayOfMonth() == date.getDayOfMonth()) {
                int num = (i + 7 - 1) / 7;
                if(num == 1){
                    return num + "st " + dateTimeLiveData.getValue().format(DateTimeFormatter.ofPattern("EEEE", Locale.US));
                } else if(num == 2){
                    return num + "nd " + dateTimeLiveData.getValue().format(DateTimeFormatter.ofPattern("EEEE", Locale.US));
                } else if(num == 3){
                    return num + "rd " + dateTimeLiveData.getValue().format(DateTimeFormatter.ofPattern("EEEE", Locale.US));
                } else {
                    return num + "th " + dateTimeLiveData.getValue().format(DateTimeFormatter.ofPattern("EEEE", Locale.US));
                }
            }
        }
        return "";
    }

    public String getYearly(){
        return dateTimeLiveData.getValue().format(DateTimeFormatter.ofPattern("M/d", Locale.US));
    }

    public String getYearlyTmr(){
        return getTomorrow().format(DateTimeFormatter.ofPattern("M/d", Locale.US));
    }

    public String getDayOfWeekTmr(){
        return getTomorrow().format(DateTimeFormatter.ofPattern("EEEE", Locale.US));
    }

    public String getMonthlyTmr(){
        LocalDate date = getTomorrow().toLocalDate();
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);

        // Iterate through the days of the month
        for (int i = 1; i <= firstDayOfMonth.lengthOfMonth(); i++) {
            LocalDate currentDate = firstDayOfMonth.withDayOfMonth(i);
            if (currentDate.getDayOfWeek().equals(date.getDayOfWeek()) && currentDate.getDayOfMonth() == date.getDayOfMonth()) {
                int num = (i + 7 - 1) / 7;
                if(num == 1){
                    return num + "st " + getTomorrow().format(DateTimeFormatter.ofPattern("EEEE", Locale.US));
                } else if(num == 2){
                    return num + "nd " + getTomorrow().format(DateTimeFormatter.ofPattern("EEEE", Locale.US));
                } else if(num == 3){
                    return num + "rd " + getTomorrow().format(DateTimeFormatter.ofPattern("EEEE", Locale.US));
                } else {
                    return num + "th " + getTomorrow().format(DateTimeFormatter.ofPattern("EEEE", Locale.US));
                }
            }
        }
        return "";
    }
}
