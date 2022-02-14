package com.deitel.pms;

public enum ActiveUnis {
    EXETER("exeter.ac.uk", 12345),
    DERBY("derby.ac.uk", 66666),
    OTHER("otheremail", 33333);


    private final String key;
    private final Integer value;

    ActiveUnis(String key, Integer value) {
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
