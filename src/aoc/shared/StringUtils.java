/*
 * Copyright (c) 2026 Ruben Saunders. All rights reserved.
 */

package aoc.shared;

public class StringUtils
{
    /**
     * Pad a string on the right to ensure it is of a certain length
     * @param s String to pad
     * @param length Desired string length
     * @param c Character to pad the string with
     * @return The padded string
     */
    public static String padRight(String s, int length, char c)
    {
        StringBuilder sBuilder = new StringBuilder(s);
        while (sBuilder.length() < length) sBuilder.append(c);
        return sBuilder.toString();
    }

    /**
     * Pad a string on the left to ensure it is of a certain length
     * @param s String to pad
     * @param length Desired string length
     * @param c Character to pad the string with
     * @return The padded string
     */
    public static String padLeft(String s, int length, char c)
    {
        StringBuilder sBuilder = new StringBuilder(s);
        while (sBuilder.length() < length) sBuilder.insert(0, c);
        return sBuilder.toString();
    }

    /**
     * Repeat a string by the desired amount
     * @param s String to repeat
     * @param count Number of times to repeat the string
     * @return The repeated string
     */
    public static String repeat(String s, int count) {
        if (count < 1) return "";
        if (count == 1) return s;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) sb.append(s);
        return sb.toString();
    }

    /**
     * Centre a string, so the resulting string is of the desired length
     * @param s String to centre
     * @param length Desired final length
     * @param c Character to pad with
     * @return Centred string
     */
    public static String center(String s, int length, char c) {
        int totalPad = Math.max(0, length - s.length());
        int leftPad = totalPad / 2;
        int rightPad = totalPad - leftPad;
        return repeat(String.valueOf(c), leftPad) + s + repeat(String.valueOf(c), rightPad);
    }

    /**
     * Same as the normal substring, but ensures no error is thrown
     * @return substring of `s` in [start, end)
     */
    public static String safeSubstring(String s, int start, int end) {
        if (s == null) return null;
        start = Math.max(0, start);
        end = Math.min(s.length(), end);
        if (start >= end) return "";
        return s.substring(start, end);
    }

    /**
     * Same as the normal substring, but ensures no error is thrown and pads the string to include the bounds
     * @param c Character to pad with
     * @return substring of `s` in [start, end)
     */
    public static String safeSubstring(String s, int start, int end, char c) {
        if (s == null) return null;
        if (start >= end) return "";
        if (start >= 0 && end < s.length()) return s.substring(start, end);

        StringBuilder sb = new StringBuilder();
        for (; start < 0; start++) sb.insert(0, c);
        sb.append(s);

        while (sb.length() < end) sb.append(c);

        return sb.substring(start, end);
    }
}
