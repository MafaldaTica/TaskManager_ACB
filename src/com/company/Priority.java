package com.company;

import java.util.Locale;

public enum Priority {
    LOW,
    MEDIUM,
    HIGH;

    public int getValueFromName(String p) {
        int value;
        p=p.toLowerCase(Locale.ROOT);
        switch (p) {
            case "low":
                value=3;
            case "medium":
                value=2;
            case "high":
                value=1;
                break;
            default:
                value=3;
        }
        return value;
    }
}

