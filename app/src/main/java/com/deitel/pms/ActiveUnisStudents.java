package com.deitel.pms;

public enum ActiveUnisStudents {
    EXETER_STUDENTS("exeter.ac.uk", 12345);


    private final String key;
    private final Integer value;

    ActiveUnisStudents(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Integer getValue() {
        return value;
    }
}
