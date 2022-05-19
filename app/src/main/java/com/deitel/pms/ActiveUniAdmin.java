package com.deitel.pms;

public enum ActiveUniAdmin {
    EXETER_ADMIN("exeter.ac.uk", 135789);

    private final String key;
    private final Integer value;

    ActiveUniAdmin(String key, Integer value) {
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
