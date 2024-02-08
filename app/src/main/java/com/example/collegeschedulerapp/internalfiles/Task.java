package com.example.collegeschedulerapp.internalfiles;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Comparable<Task> {
    Assignment assignmentTask;

    Exam examTask;

    String customTask;


    public String getName() {
        return name;
    }

    public Date getDueDateTime() {
        return dueDateTime;
    }

    public boolean isCompleted() {
        return completed;
    }

    public Course getCourse() {
        return course;
    }

    String name;

    Date dueDateTime;
    boolean completed;
    Course course;


    public Task(Assignment assignmentTask) {
        this.assignmentTask = assignmentTask;
        name = "Get Started on " + assignmentTask.getName();
        completed = assignmentTask.isCompleted();
        dueDateTime = assignmentTask.getDueDateTime();
        completed = assignmentTask.isCompleted();
        course = assignmentTask.getCourse();
    }

    public Task(Exam examTask) {

        this.examTask = examTask;
        completed = false;
        name = "Study for " + examTask.getName();
        dueDateTime = examTask.getExamDateTime();
        course = examTask.getCourse();
    }

    public Task(String customTask, Date dueDateTime, boolean completed, Course course) {
        name = customTask;
        this.customTask = customTask;
        this.dueDateTime = dueDateTime;
        this.completed = completed;
        this.course = course;
    }
    public String getTimeDateDueString() {

        SimpleDateFormat dateForm = new SimpleDateFormat("EEE, MMM yy");
        SimpleDateFormat timeForm = new SimpleDateFormat("h:mm a");


        return "Complete by " + dateForm.format(dueDateTime) + " at " + timeForm.format(dueDateTime);
    }

    public Exam returnExam() {
        return examTask;
    }

    public Assignment returnAssignment() {
        return assignmentTask;
    }

    public String returnCustomTask() {
        return customTask;
    }

    public void switchStatus(boolean bool) {
        completed = bool;
        if (assignmentTask != null) {
            assignmentTask.completed = bool;
        }
    }
    @Override
    public int compareTo(Task o) {
        return this.dueDateTime.compareTo(o.dueDateTime);
    }
}
