package com.example.collegeschedulerapp.internalfiles;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Course {
    public String getInstructor() {
        return instructor == null ? "N/A" : room;
    }

    public String getRoom() {
        return room == null ? "N/A" : room;
    }

    public String getLocation() {
        return location == null ? "N/A" : location;
    }

    public String getMeetingDays() {
        return meetingDays == null ? "N/A" : meetingDays;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public ArrayList<Assignment> assignments;
    public ArrayList<Exam> exams;

    public String name;
    public long id;
    public String section;
    public String instructor;
    public String room;
    public String location;

    String meetingDays;
    Date startDateTime;
    Date endDateTime;

    public Course(String name) {
        assignments = new ArrayList<>();
        exams = new ArrayList<>();
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Course(String name,
                  String instructor,
                  String meetingTime,
                  Date startDateTime,
                  Date endDateTime) {

        assignments = new ArrayList<>();
        exams = new ArrayList<>();

        this.name = name;
        this.instructor = instructor;
        this.meetingDays = meetingTime;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
    public String getTimeDateDueString() {
;
        SimpleDateFormat timeForm = new SimpleDateFormat("h:mm a");


        return timeForm.format(startDateTime) + " - " + timeForm.format(endDateTime);
    }


    public String toString() {
        return name;
    }

    public boolean equals(Course obj) {
        return this.name.equals(obj.name);

    }



}
