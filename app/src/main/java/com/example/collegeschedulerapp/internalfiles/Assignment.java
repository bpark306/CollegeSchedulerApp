package com.example.collegeschedulerapp.internalfiles;

import android.widget.Adapter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class Assignment implements Comparable<Assignment> {


    String name;
    Date dueDateTime;
    boolean completeted;
    String courseName;

    public boolean isCompleteted() {
        return completeted;
    }
    public String getName() {
        return name;
    }


    public Assignment(String name, Date dueDateTime, boolean completeted, String courseName) {
        this.name = name;
        this.dueDateTime = dueDateTime;
        this.completeted = completeted;
        this.courseName = courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Date getTimeDateDue() {
        return dueDateTime;
    }

    public String getCourseName() {
        return courseName;
    }


    @Override
    public int compareTo(Assignment o) {
        return this.dueDateTime.compareTo(o.dueDateTime);
    }
}

