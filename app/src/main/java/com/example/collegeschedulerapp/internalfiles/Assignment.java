package com.example.collegeschedulerapp.internalfiles;

import android.widget.Adapter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class Assignment implements Comparable<Assignment> {


    public void setName(String name) {
        this.name = name;
    }

    public void setDueDateTime(Date dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    String name;
    Date dueDateTime;
    boolean completed;
    Course course;

    public boolean isCompleted() {
        return completed;
    }
    public String getName() {
        return name;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }


    public Assignment(String name, Date dueDateTime, boolean completed, Course course) {
        this.name = name;
        this.dueDateTime = dueDateTime;
        this.completed = completed;
        this.course = new Course(course.name);
    }


    public Date getDueDateTime() {
        return dueDateTime;
    }

    public String getTimeDateDueString() {

        SimpleDateFormat dateForm = new SimpleDateFormat("EEE, MMM dd");
        SimpleDateFormat timeForm = new SimpleDateFormat("h:mm a");


        return "Due on " + dateForm.format(dueDateTime) + " at " + timeForm.format(dueDateTime);
    }

    public Course getCourse() {
        return course;
    }


    @Override
    public int compareTo(Assignment o) {
        return this.dueDateTime.compareTo(o.dueDateTime);
    }


}

