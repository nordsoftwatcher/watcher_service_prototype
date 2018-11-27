package ru.nord.siwatch.backend.facade.device.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public final class MapHelper
{
    public static Long getLong(Map<String, Object> map, String key) {
        Long result = null;
        if(map.containsKey(key)) {
            Object value = map.get(key);
            if(value != null) {
                try {
                    result = Long.parseLong(value.toString());
                }
                catch (NumberFormatException ex) {
                    log.warn("Failed parsing '"+value+"' as long");
                }
            }
        }
        return result;
    }

    public static Double getDouble(Map<String, Object> map, String key) {
        Double result = null;
        if(map.containsKey(key)) {
            Object value = map.get(key);
            if(value != null) {
                try {
                    result = Double.parseDouble(value.toString());
                }
                catch (NumberFormatException ex) {
                    log.warn("Failed parsing '"+value+"' as double");
                }
            }
        }
        return result;
    }
}
