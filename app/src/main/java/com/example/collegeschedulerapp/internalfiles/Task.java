package com.example.collegeschedulerapp.internalfiles;

public class Task {
    Assignment assignmentTask;

    Exam examTask;

    String customTask;

    public Task(Assignment assignmentTask) {
        this.assignmentTask = assignmentTask;
    }

    public Task(Exam examTask) {
        this.examTask = examTask;
    }

    public Task(String customTask) {
        this.customTask = customTask;
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
}
