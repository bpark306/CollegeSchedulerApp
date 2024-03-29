package com.example.collegeschedulerapp.internalfiles;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Exam implements Comparable<Exam>{
    public Date getExamDateTime() {
        return examDateTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExamDateTime(Date examDateTime) {
        this.examDateTime = examDateTime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    String name;
    Date examDateTime;
    String location;
    Course course;
    public Course getCourse() {
        return course;
    }

    public String getTimeDateExamString() {

        SimpleDateFormat dateForm = new SimpleDateFormat("EEE, MMM dd");
        SimpleDateFormat timeForm = new SimpleDateFormat("h:mm a");


        return "Exam on " + dateForm.format(examDateTime) + " at " + timeForm.format(examDateTime);
    }

    public String getLocation() {

        return (location == null || location.isEmpty()) ? "Online" : location;
    }

    public String getName() {
        return name;
    }


    public Exam(String name, Date examDateTime, Course course, String location) {
        this.name = name;
        this.examDateTime = examDateTime;
        this.course = new Course(course.name);
        this.location = location;
    }

    @Override
    public int compareTo(Exam o) {

        return this.examDateTime.compareTo(o.examDateTime);

    }
}
