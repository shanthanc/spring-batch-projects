package com.shanthan.customerdetailsbatch.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum NotificationPreference {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);

    private final Integer preference;

    NotificationPreference(Integer preference) {
        this.preference = preference;
    }

    public Integer getPreference() {
        return preference;
    }

    private static final Map<Integer, Integer> preferenceMap = new HashMap<>();
    static {
        Arrays.stream(NotificationPreference.values())
                .forEach(p -> preferenceMap.put(p.getPreference(), p.getPreference()));
    }

    public static Integer getNotificationPreference(Integer preference) {
        return preferenceMap.get(preference);
    }
}
