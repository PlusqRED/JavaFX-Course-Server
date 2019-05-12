package ru.grape.course.server.utils;

import java.util.Random;

public class CustomHash {
    private static final Random random = new Random();
    private static String alphabet = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM0123456789";

    private CustomHash() {
    }

    public static String getHash(String input, int hashSize) {
        char[] chars = input.toCharArray();
        random.setSeed(chars[0]);
        long a = random.nextLong();
        random.setSeed(chars[chars.length - 1]);
        long result = random.nextLong();
        for (char aChar : chars) {
            result += aChar * a;
            a += aChar;
        }
        random.setSeed(result);
        random.setSeed(random.nextLong());
        StringBuilder output = new StringBuilder(hashSize);
        for (int i = 0; i < hashSize; ++i) {
            output.append(alphabet.charAt(random.nextInt(62)));
        }
        return output.toString();
    }

    public static void main(String[] args) {
        System.out.println(getHash("123", 32));
    }
}