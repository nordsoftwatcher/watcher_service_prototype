package ru.nord.backend.infrastructure.utils;

import java.util.Map;

public class KeyValue<K, V> implements Map.Entry<K, V>
{
    private K key;
    private V value;

    public KeyValue(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public static <K, V> KeyValue<K, V> of(K key, V value) {
        return new KeyValue<>(key, value);
    }

    @Override
    public K getKey()
    {
        return key;
    }

    @Override
    public V getValue()
    {
        return value;
    }

    @Override
    public V setValue(V value)
    {
        this.value = value;
        return value;
    }
}
