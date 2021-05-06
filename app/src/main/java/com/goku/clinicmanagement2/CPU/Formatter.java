package com.goku.clinicmanagement2.CPU;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formatter {
    public Set<String> init(String s) {
        String splitter[]= s.split("]");
        Set<String> scheduleAndImageUrl= new LinkedHashSet<>();
        for (int i=0;i<splitter.length;i++) {
            scheduleAndImageUrl.add(extract(splitter[i]));
        }
        if (scheduleAndImageUrl.contains("")) {
            scheduleAndImageUrl.remove("");
        }
        return scheduleAndImageUrl;
    }

    private String extract(String s) {
        String splitter[]= s.split("accurateDetails=|\\d+/\\d+/\\d+|,|null"), concat="";
        String Name="", Mobile="", Fees="";
        for (String s1: splitter) {
            String splitter11[]= s1.split("\\[|\\]|\\{|\\}|\\ +");
            for (String s11: splitter11) {
                if (!s11.isEmpty()) {
                    s11 = s11.trim();
                    concat+= s11;
                }
                else {
                    continue;
                }
                concat+=" ";
            }
        }
        String concatSplit[]= concat.split("\\s+");
        try {
            Name = concatSplit[0];
            Mobile = concatSplit[1];
            Fees = concatSplit[2];
            if (Fees.contains("=")) {
                Fees= Fees.replace("=","");
            }
        } catch (Exception e){}
        concat="";
        for (int i=3;i<concatSplit.length;i++) {
            if (!concatSplit[i].isEmpty()) {
                concat += concatSplit[i] + " ";
            }
            else if (concatSplit[i].isEmpty()) {
                continue;
            }
        }
        concat= concat.trim();
        Set<String> scheduleAndImageUrl= new LinkedHashSet<>();
        if (!concat.isEmpty()) {
            String concatSplit11[]= concat.split("\\s+");
            for (int i=0;i<concatSplit11.length;i++) {
                String join="";
                if (!concatSplit11[i].isEmpty()) {
                    if (isTime(concatSplit11[i]) && isDay(concatSplit11[i+1])) {
                        join+= concatSplit11[i] + " " + concatSplit11[i+1];
                    }
                    scheduleAndImageUrl.add(join);
                }
            }
            for (String s1: concatSplit11) {
                if (s1.contains("urlImage=")) {
                    scheduleAndImageUrl.add(s1);
                }
            }
        }
        if (scheduleAndImageUrl.contains("")) {
            scheduleAndImageUrl.remove("");
        }
        String joiner="";
        for (String s5: scheduleAndImageUrl) {
            if (!s5.isEmpty()) {
                joiner += s5 + " ";
            }
        }
        joiner+=" " + Name + " " + Mobile + " " + Fees;
        joiner= joiner.trim();

        return joiner;
    }

    public String slidingWindow(String s[]) {
        int start=0, end=0;
        String join="";
        while (end <= s.length) {
            end++;
            if (end < s.length && end-start+1==2) {
                join+= s[start] + " " + s[end] + "\n";
                start= end+1;
                end++;
            }
        }
        return join;
    }

    public boolean isTime(String s) {
        String regex= "\\d+:\\d+";
        Pattern pattern= Pattern.compile(regex);
        Matcher matcher= pattern.matcher(s);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public boolean isDay(String s) {
        String Days[]= {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String s1: Days) {
            if (s1.equals(s)) {
                return true;
            }
        }
        return false;
    }
}
