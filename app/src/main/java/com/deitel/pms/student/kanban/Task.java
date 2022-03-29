package com.deitel.pms.student.kanban;

import java.util.ArrayList;

public class Task {

    String taskTitle;
    public Task(String title) {
        this.taskTitle = title;
    }

    public static String newEmptyTask() {
        return "";
    }
}
