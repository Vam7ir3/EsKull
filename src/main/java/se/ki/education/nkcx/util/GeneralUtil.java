package se.ki.education.nkcx.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Random;

public class GeneralUtil {
    public static String generateRandomText() {
        String pool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        while (stringBuilder.length() < 8) {
            int index = random.nextInt(61);
            stringBuilder.append(pool.charAt(index));
        }
        return stringBuilder.toString();
    }

    public static String generateRandomDigit() {
        String pool = "0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        while (stringBuilder.length() < 4) {
            int index = random.nextInt(9);
            stringBuilder.append(pool.charAt(index));
        }
        return stringBuilder.toString();
    }

    public static String generateRandomDigit(int length) {
        String pool = "0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        while (stringBuilder.length() < length) {
            int index = random.nextInt(9);
            stringBuilder.append(pool.charAt(index));
        }
        return stringBuilder.toString();
    }

    public static boolean matchPassword(String rawPassword, String encodedPassword) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public static String sha256(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] encodedHash = messageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedHash);
    }

    public static String bytesToHex(byte[] hash) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                stringBuilder.append("0");
            }
            stringBuilder.append(hex);
        }
        return stringBuilder.toString();
    }

    public static String convertSecToHourMinuteSecond(long sec) {
        long hour = sec / (60 * 60);
        long remainingSec = sec % (60 * 60);

        long minute = remainingSec / 60;
        remainingSec = remainingSec % 60;

        return (hour < 10 ? ("0" + hour) : hour) + ":" + (minute < 10 ? ("0" + minute) : minute) + ":" + (remainingSec < 10 ? ("0" + remainingSec) : remainingSec);
    }

    public static String formatDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.getYear()
                + "-"
                + (localDateTime.getMonthValue() < 10 ? ("0" + localDateTime.getMonthValue()) : localDateTime.getMonthValue())
                + "-"
                + (localDateTime.getDayOfMonth() < 10 ? ("0" + localDateTime.getDayOfMonth()) : localDateTime.getDayOfMonth())

                + " "
                + (localDateTime.getHour() < 10 ? ("0" + localDateTime.getHour()) : localDateTime.getHour())
                + ":"
                + (localDateTime.getMinute() < 10 ? ("0" + localDateTime.getMinute()) : localDateTime.getMinute())
                + ":"
                + (localDateTime.getSecond() < 10 ? ("0" + localDateTime.getSecond()) : localDateTime.getSecond());
    }

    public static String formatString(String unformattedString) {

        if (unformattedString == null) {
            return null;
        }
        if (unformattedString.trim().isEmpty()) {
            return unformattedString.trim();
        }

        String[] splitted = unformattedString.split("_");
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : splitted) {
            stringBuilder.append(str, 0, 1);
            stringBuilder.append(str.toLowerCase().substring(1));
            stringBuilder.append(" ");
        }
        return stringBuilder.toString().trim();
    }
}
