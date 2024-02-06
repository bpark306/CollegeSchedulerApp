package com.example.collegeschedulerapp.internalfiles;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Course {
    public ArrayList<Assignment> assignments;

    public String name;
    public long id;
    public String section;
    public String instructor;
    public String room;
    public String location;

    String meetingTime;

    public Course(String name) {
        assignments = new ArrayList<>();
        this.name = name;
    }
    public String toString() {
        return name;
    }

    public boolean equals(Course obj) {
        return this.name.equals(obj.name);

    }



}
