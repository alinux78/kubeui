package com.mc.study.debug.k8s.utils;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

public class PodUtils {

    public static String getAgeFromStartTime(OffsetDateTime startTime) {
        if (startTime == null) {
            return "<unknown>";
        }

        // Make sure both times are in the same offset (UTC by default)
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        Duration duration = Duration.between(startTime, now);

        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return seconds + "s";
        }

        long minutes = TimeUnit.SECONDS.toMinutes(seconds);
        seconds %= 60;

        if (minutes < 60) {
            return minutes + "m" + (seconds > 0 ? seconds + "s" : "");
        }

        long hours = TimeUnit.MINUTES.toHours(minutes);
        minutes %= 60;

        if (hours < 24) {
            return hours + "h" + (minutes > 0 ? minutes + "m" : "");
        }

        long days = TimeUnit.HOURS.toDays(hours);
        hours %= 24;

        return days + "d" + (hours > 0 ? hours + "h" : "");
    }

}