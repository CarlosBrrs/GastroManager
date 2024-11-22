package com.kaiho.gastromanager.infrastructure.config.generator;

import java.util.Calendar;
import java.util.Random;

public class OrderCodeGenerator {
    private static final char[] _base36chars =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    .toCharArray();

    private static final Random _random = new Random();

    public static String getBase36(int length) {
        var sb = new StringBuilder(9 + length);

        Calendar today = Calendar.getInstance();
        String stringYear = String.valueOf(today.get(Calendar.YEAR)).substring(2, 4);
        sb.append(stringYear);
        sb.append("-");
        sb.append(today.get(Calendar.MONTH)+1);
        sb.append("-");
        sb.append(today.get(Calendar.DAY_OF_MONTH));
        sb.append("/");

        for (int i = 0; i < length; i++)
            sb.append(_base36chars[_random.nextInt(36)]);

        return sb.toString();
    }
}