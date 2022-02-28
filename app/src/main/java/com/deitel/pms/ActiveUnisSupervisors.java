package com.deitel.pms;

public enum ActiveUnisSupervisors {
    EXETER_SUPERVISOR("exeter.ac.uk", 123450000);

    private final String key;
    private final Integer value;

    ActiveUnisSupervisors(String key, Integer value) {
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
