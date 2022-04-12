package com.deitel.pms;

import static org.junit.Assert.*;

import org.junit.Test;

public class ActiveUnisStudentsTest {


    @Test
    public void getKey() {

    }

    @Test
    public void getValue() {
    }

    @Test
    public void values() {
    }

    @Test
    public void valueOf() {
        assertEquals(ActiveUnisStudents.valueOf("@exeter.ac.uk"), ActiveUnisStudents.EXETER_STUDENTS);
    }
}