package ru.nord.backend.infrastructure.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class StringUtils {
    private StringUtils() {
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isNullOrWhitespace(String s) {
        return s == null || s.chars().allMatch(Character::isSpaceChar);
    }

    public static List<String> split(String s, String delimiter) {
        if (isNullOrEmpty(s))
            return Collections.singletonList(s);

        final List<String> parts = new ArrayList<>();
        int index = 0;
        do {
            int i = s.indexOf(delimiter, index);
            i = i < 0 ? s.length() : i;
            final String value = s.substring(index, i);
            parts.add(value);
            index = i + delimiter.length();
        } while (index <= s.length());

        return Collections.unmodifiableList(parts);
    }

    public static String trimTail(String s, String tail, boolean ignoreCase) {
        if (s == null || tail == null)
            return s;
        if (tail.isEmpty())
            return s;
        if (s.length() < tail.length())
            return s;

        final int idx = s.length() - tail.length();
        final String suffix = s.substring(idx);
        if (ignoreCase ? suffix.equalsIgnoreCase(tail) : suffix.equals(tail)) {
            return s.substring(0, idx);
        }
        return s;
    }

    public static String trimHead(String s, String head, boolean ignoreCase) {
        if (s == null || head == null)
            return s;
        if (head.isEmpty())
            return s;
        if (s.length() < head.length())
            return s;

        final String prefix = s.substring(0, head.length());
        if (ignoreCase ? prefix.equalsIgnoreCase(head) : prefix.equals(head)) {
            return s.substring(head.length());
        }
        return s;
    }

    public static String wrap(String s, CharSequence prefix, CharSequence suffix) {
        return s == null ? null : (prefix != null ? prefix : "") + s + (suffix != null ? suffix : "");
    }

    public static String wrap(String s, CharSequence p) {
        return wrap(s, p, p);
    }

    /**
     * Заменяет часть символов строки указанным символом
     * @param s                Исходная строка
     * @param startCount       Сколько символов оставить незамененными в начале строки
     * @param maskChar         Символ-замена
     * @param endCount         Сколько символов оставить незамененными в конце строки
     * @return Замаскированная строка
     */
    public static String disguise(String s, int startCount, char maskChar, int endCount) {
        if (isNullOrEmpty(s))
            return s;

        if (startCount < 0)
            startCount = 0;
        if (startCount > s.length())
            startCount = s.length();

        if (endCount < 0)
            endCount = 0;
        if (endCount > s.length())
            endCount = s.length();

        if(endCount + startCount > s.length()) {
            startCount = s.length()/2;
            endCount = s.length() - startCount;
        }

        final StringBuilder result = new StringBuilder();
        result.append(s, 0, startCount);

        final char[] mask = new char[s.length() - endCount - startCount];
        Arrays.fill(mask, maskChar);

        result.append(mask);
        result.append(s, s.length() - endCount, s.length());

        return result.toString();
    }

    /**
     * Заменяет часть строки на указанную строку
     * @param s                Исходная строка
     * @param startCount       Сколько символов оставить в начале строки
     * @param mask             Строка-замена
     * @param endCount         Сколько символов оставить в конце строки
     * @return Замаскированная строка
     */
    public static String disguise(String s, int startCount, CharSequence mask, int endCount) {
        if (isNullOrEmpty(s))
            return s;

        if (startCount < 0)
            startCount = 0;
        if (startCount > s.length())
            startCount = s.length();

        if (endCount < 0)
            endCount = 0;
        if (endCount > s.length())
            endCount = s.length();

        if(endCount + startCount > s.length()) {
            startCount = s.length()/2;
            endCount = s.length() - startCount;
        }

        final StringBuilder result = new StringBuilder();
        result.append(s, 0, startCount);
        result.append(mask);
        result.append(s, s.length() - endCount, s.length());
        return result.toString();
    }

    public static String ensureEndsWith(String s, String end) {
        return s == null || end == null || s.endsWith(end) ? s : s + end;
    }

    public static String ensureStartsWith(String s, String start) {
        return s == null || start == null || s.startsWith(start) ? s : start + s;
    }

    public static boolean containsAnyChar(String s, char ...chars) {
        if(s == null || s.isEmpty()) return false;
        if(chars == null || chars.length == 0) return false;

        for(char c : chars) {
            if(s.indexOf(c) >= 0) return true;
        }
        return false;
    }
}
