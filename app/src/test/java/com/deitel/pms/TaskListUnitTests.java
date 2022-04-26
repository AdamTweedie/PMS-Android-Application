package com.deitel.pms;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.deitel.pms.student.kanban.KanbanRecyclerViewAdapter;
import com.deitel.pms.student.kanban.TaskList;

import org.junit.Test;

import java.util.ArrayList;

public class TaskListUnitTests {

    String user_kanban_prefs_id = "user_id";
    ArrayList<String> taskListData = new ArrayList<>();
    int tabPosition = 1;

    @Test
    public void getTaskListData() {
        TaskList taskList = new TaskList(taskListData, tabPosition, user_kanban_prefs_id);
        assertEquals(taskListData, taskList.getTaskListData());
    }

    @Test
    public void setTaskListData() {
        TaskList taskList = new TaskList(taskListData, tabPosition, user_kanban_prefs_id);
        ArrayList<String> newArray = new ArrayList<>();
        taskList.setTaskListData(newArray);
        assertEquals(newArray, taskList.getTaskListData());

    }

    @Test
    public void getTabPosition() {
        TaskList taskList = new TaskList(taskListData, tabPosition, user_kanban_prefs_id);
        assertEquals(1, taskList.getTabPosition());
    }
}
