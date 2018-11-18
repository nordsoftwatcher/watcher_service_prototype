package ru.nord.backend.infrastructure.utils;

import org.junit.Test;

import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class StringUtilsTest
{
    @Test
    public void testIsNullOrEmpty()
    {
        final String s1 = null;
        assertTrue(StringUtils.isNullOrEmpty(s1));

        final String s2 = "";
        assertTrue(StringUtils.isNullOrEmpty(s2));

        final String s3 = " ";
        assertFalse(StringUtils.isNullOrEmpty(s3));

        final String s4 = " AB C";
        assertFalse(StringUtils.isNullOrEmpty(s4));
    }

    @Test
    public void testIsNullOrWhitespace()
    {
        final String s1 = null;
        assertTrue(StringUtils.isNullOrWhitespace(s1));

        final String s2 = "";
        assertTrue(StringUtils.isNullOrWhitespace(s2));

        final String s3 = " ";
        assertTrue(StringUtils.isNullOrWhitespace(s3));

        final String s4 = "          ";
        assertTrue(StringUtils.isNullOrWhitespace(s4));

        final String s5 = " AB C";
        assertFalse(StringUtils.isNullOrWhitespace(s5));
    }

    @Test
    public void testSplit()
    {
        final String d = "|";

        String s = "A|B|C|D";
        assertEquals(StringUtils.split(s, d).stream().collect(Collectors.joining(d)), s);

        s = "A";
        assertEquals(StringUtils.split(s, d).stream().collect(Collectors.joining(d)), s);

        s = "|A|";
        assertEquals(StringUtils.split(s, d).stream().collect(Collectors.joining(d)), s);

        s = "";
        assertEquals(StringUtils.split(s, d).stream().collect(Collectors.joining(d)), s);

        s = "|";
        assertEquals(StringUtils.split(s, d).stream().collect(Collectors.joining(d)), s);
    }

    @Test
    public void testTrimTail()
    {
        assertEquals(StringUtils.trimTail("abc", "c", false), "ab");
        assertEquals(StringUtils.trimTail("abc", "abcd", false), "abc");
        assertEquals(StringUtils.trimTail("abc", "BC", true), "a");
        assertEquals(StringUtils.trimTail("abc", "AbC", true), "");
        assertNull(StringUtils.trimTail(null, "BC", true));
    }

    @Test
    public void testTrimHead()
    {
        assertEquals(StringUtils.trimHead("abc", "a", false), "bc");
        assertEquals(StringUtils.trimHead("abc", "abcd", false), "abc");
        assertEquals(StringUtils.trimHead("abc", "AB", true), "c");
        assertEquals(StringUtils.trimHead("abc", "AbC", true), "");
        assertNull(StringUtils.trimHead(null, "BC", true));
    }

    @Test
    public void testWrap()
    {
        assertEquals(StringUtils.wrap("ab", "12"), "12ab12");
        assertEquals(StringUtils.wrap("ab", "1", "2"), "1ab2");
        assertNull(StringUtils.wrap(null, "B"));
    }

    @Test
    public void testDisguise()
    {
        assertEquals("*", StringUtils.disguise("a", 0, '*', 0));
        assertEquals("a***e", StringUtils.disguise("abcde", 1, '*', 1));
        assertEquals("ab****", StringUtils.disguise("abcdef", 2, '*', 0));
        assertEquals("ab*f", StringUtils.disguise("abcdef", 2, "*", 1));
        assertEquals("", StringUtils.disguise("", 1, '*', 1));
        assertNull(StringUtils.disguise(null, 1, '*', 1));
    }

    @Test
    public void testEnsureStartsWith()
    {
        assertEquals("abc", StringUtils.ensureStartsWith("abc", "ab"));
        assertEquals("qabc", StringUtils.ensureStartsWith("abc", "q"));
        assertEquals("q", StringUtils.ensureStartsWith("", "q"));
        assertNull(StringUtils.ensureStartsWith(null, "q"));
    }

    @Test
    public void testEnsureEndsWith()
    {
        assertEquals("abc", StringUtils.ensureEndsWith("abc", "bc"));
        assertEquals("abcq", StringUtils.ensureEndsWith("abc", "q"));
        assertEquals("q", StringUtils.ensureEndsWith("", "q"));
        assertNull(StringUtils.ensureEndsWith(null, "q"));
    }
}
