package com.may;

import java.nio.CharBuffer;

/**
 * Created by dongpan on 2016/1/13.
 */
public class Nio {
    public static void main(String[] argv) throws Exception {
        CharBuffer buffer = CharBuffer.allocate(100);
        System.out.println("limit:"+buffer.limit());
        System.out.println("capacity:"+buffer.capacity());
        System.out.println("position:"+buffer.position());
        System.out.println("mark:"+buffer.mark());
        while (fillBuffer(buffer)) {
            buffer.flip();
            drainBuffer(buffer);
            buffer.clear();
        }
    }

    private static void drainBuffer(CharBuffer buffer) {
        while (buffer.hasRemaining()) {
            System.out.print(buffer.get());
        }
        System.out.println("");
    }

    private static boolean fillBuffer(CharBuffer buffer) {
        if (index >= strings.length) {
            return (false);
        }
        String string = strings[index++];
        for (int i = 0; i < string.length(); i++) {
            buffer.put(string.charAt(i));
        }
        return (true);
    }

    private static int index = 0;
    private static String[] strings = {"A random string value", "The product of an infinite number of monkeys", "Hey hey we're the Monkees",
            "Opening act for the Monkees: Jimi Hendrix", "'Scuse me while I kiss this fly", // Sorry Jimi ;-)
             "Help Me! Help Me!", };
    }