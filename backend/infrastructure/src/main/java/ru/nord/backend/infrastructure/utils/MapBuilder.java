package ru.nord.backend.infrastructure.utils;

import java.util.HashMap;
import java.util.Map;

public final class MapBuilder
{
    public static final class MapBuilder_<K1, V1>
    {
        private Map<K1, V1> map;

        private MapBuilder_() { }

        private Map<K1, V1> getMap() {
            if(map == null) {
                map = new HashMap<>();
            }
            return map;
        }

        public final MapBuilder_<K1, V1> put(K1 key, V1 value) {
            getMap().put(key, value);
            return this;
        }

        @SafeVarargs
        public final MapBuilder_<K1, V1> put(Map.Entry<K1, V1> ...entries) {
            for(Map.Entry<K1, V1> entry : entries) {
                getMap().put(entry.getKey(), entry.getValue());
            }
            return this;
        }

        public Map<K1, V1> build()
        {
            return map;
        }
    }

    private MapBuilder() { }

    public static <K, V> MapBuilder_<K, V> put(K key, V value) {
        return new MapBuilder_<K, V>().put(key, value);
    }

    @SafeVarargs
    public static <K, V> MapBuilder_<K, V> put(Map.Entry<K, V> ...entries) {
        MapBuilder_<K, V> mb = new MapBuilder_<>();
        for(Map.Entry<K, V> entry : entries) {
            mb.put(entry.getKey(), entry.getValue());
        }
        return mb;
    }
}
