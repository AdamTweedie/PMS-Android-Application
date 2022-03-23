package com.deitel.pms.student.kanban;

import java.util.ArrayList;

public class Task {

    public ArrayList<String> newEmptyTask() {
        ArrayList<String> emptyTask = new ArrayList<String>();
        emptyTask.add("");
        return emptyTask;
    }
}
