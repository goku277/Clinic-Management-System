package com.goku.clinicmanagement2.CPU;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Cardiologist_Formatter {
        public ArrayList<ArrayList<String>> init(String data) {
            String split[] = data.split("^[0-3]?[0-9]\\/[0-3]?[0-9]\\/(?:[0-9]{2})?[0-9]{2}$|\\{|\\[|null,|,|\n|\\}|\\]"), Specialist = "", DoctorName = "", DoctorMobile = "", DoctorFees = "", DoctorSchedule = "", DoctorImageUrl = "";

            ArrayList<String> join = new ArrayList<>();
            for (String s : split) {
                if (!s.isEmpty()) {
                    s = s.trim();
                    if (s.contains("accurateDetails=")) {
                        join.add(s);
                    }
                    if (s.contains("referenceId=")) {
                        //  join.add(s);
                    }
                    if (s.contains("urlImage=")) {
                        //  join.add(s);
                    }
                }
            }
            System.out.println("\n\n");

            System.out.println("join: " + join);

            Set<String> fetchSchedule = new LinkedHashSet<>();

            String concat = "";
            for (String s1 : join) {
                String s5[] = s1.split("\\s+|\\d+/\\d+/\\d+");
                for (String s11 : s5) {
                    if (!s11.isEmpty()) {
                        //  System.out.println("s11: " + s11);
                        concat += s11 + "";
                    }
                    concat += " ";
                }
                concat += "\t";
            }

            System.out.println("concat: " + concat);

            String splitConcat[] = concat.split("accurateDetails=");

            for (String s : splitConcat) {
                if (!s.isEmpty()) {
                    s = s.trim();
                    System.out.println("s: " + s);
                    fetchSchedule.add(s);
                }
            }

            System.out.println("\n\n\n\n");

            System.out.println("fetchSchedule: " + fetchSchedule);

            ArrayList<String> setToList = new ArrayList<>();

            setToList.addAll(fetchSchedule);

            ArrayList<ArrayList<String>> sendData = new ArrayList<ArrayList<String>>();
            ArrayList<String> NameList = new ArrayList<>();
            ArrayList<String> MobileList = new ArrayList<>();
            ArrayList<String> FeesList = new ArrayList<>();
            ArrayList<String> UrlImage = new ArrayList<>();
            for (String s : split) {
                if (!s.isEmpty()) {
                    s = s.trim();
                    //  System.out.println("s: " + s);
                    if (s.contains("referenceId=")) {
                        s = s.replace("referenceId=", "");
                        s = s.trim();
                        String reference1[] = s.split(" ");
                        NameList.add(reference1[0]);
                        MobileList.add(reference1[1]);
                        FeesList.add(reference1[2]);
                    }
                    if (s.contains("urlImage=")) {
                        s = s.replace("urlImage=", "");
                        UrlImage.add(s);
                    }
                }
            }
            sendData.add(0, setToList);
            sendData.add(1, NameList);
            sendData.add(2, MobileList);
            sendData.add(3, FeesList);
            sendData.add(4, UrlImage);
            return sendData;
        }

        private boolean checkTime(String time) {
            Pattern pattern = Pattern.compile("\\d+:\\d+");
            java.util.regex.Matcher matcher = pattern.matcher(time);
            if (matcher.matches()) {
                return true;
            }
            return false;
        }

        private boolean check(String date) {
            Pattern pattern = Pattern.compile("\\d+/\\d+/\\d+");
            Matcher matcher = pattern.matcher(date);
            if (matcher.matches()) {
                return true;
            }
            return false;
        }

        private boolean isDay(String days) {
            String day[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
            for (String s : day) {
                if (s.equals(days)) return true;
            }
            return false;
        }
    }