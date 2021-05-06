package com.goku.clinicmanagement2.CPU;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Time_1 {
    String url="";

    public Set<String> init(String data[]) {
        System.out.println("From init():");
        String schedule="";
        for (int i=0;i<data.length-1;i++) {
            if (ifTime(data[i]) && isDay(data[i+1])) {
                schedule+= data[i] + " " + data[i+1]+ " ";
            }
            if (data[i].contains("urlImage")) {
                url= data[i];
                System.out.println("urlImage is found!: " + url);
            }
        }
        System.out.println("schedule: " + schedule + " ");
        return splitAndAppend(schedule);
    }

    public String getUrl() {
        return url;
    }

    private Set<String> splitAndAppend(String schedule) {
        String s[]= schedule.split(" ");
        for (String s1: s) {
            System.out.println(s1);
        }
        int start=0, end= 0;
        Set<String> append= new LinkedHashSet<>();
        while (end <= s.length) {
            end++;
            if (s.length > 1 && end <= s.length && end-start+1==2) {
                String join = s[start] + " " + s[end];
                append.add(join);
                start = end + 1;
                end++;
            }
        }
        System.out.println("append: " + append);
        return append;
    }
    private static boolean isDay(String s) {
        String days[]= {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String s1: days) {
            if (s1.equals(s)) {
                return true;
            }
        }
        return false;
    }
    private static boolean ifTime(String s) {
        String regex="\\d+:\\d+";
        Pattern pattern= Pattern.compile(regex);
        Matcher matcher= pattern.matcher(s);
        if (matcher.matches()) {
            System.out.println("true");
            return true;
        }
        else {
            System.out.println("false");
            return false;
        }
    }
}