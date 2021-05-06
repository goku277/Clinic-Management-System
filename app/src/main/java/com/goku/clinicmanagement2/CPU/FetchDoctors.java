package com.goku.clinicmanagement2.CPU;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class FetchDoctors {
    public Map<String, Set<String>> init(String data) {
        String split[] = data.split("\\{|\\}");
        Set<String> DoctorName = new LinkedHashSet<>();
        for (String s : split) {
            if (s.trim().equals(",")) {
                s = s.replace(",", "");
            }
            if (!s.trim().isEmpty()) {
                if (s.contains("referenceId=")) {
                    String name = s.substring(s.indexOf("referenceId="), s.indexOf("urlImage=")).replace("referenceId=", "").replace(",", "").trim();
                    String name11[] = name.split("\\s+");
                    DoctorName.add(name11[0]);
                }
            }
        }
        Map<String, Set<String>> mapDoctorToDoctorDetails= new LinkedHashMap<>();
        Set<String> fetchDoctorDetails = null;
        for (String s : DoctorName) {
            fetchDoctorDetails= new LinkedHashSet<>();
            ArrayList<String> daysList= new ArrayList<>();
            ArrayList<String> timeList= new ArrayList<>();
            String tim = "", refId = "", urlImage = "";
            for (String s1 : split) {
                if (s1.trim().equals(",")) {
                    s1 = s1.replace(",", "");
                }
                if (!s1.trim().isEmpty()) {
                    if (s1.contains(s)) {
                        if (s1.contains("urlImage=")) {
                            urlImage= s1.substring(s1.indexOf("urlImage=")).trim();
                        }
                        if (s1.contains("accurateDetails=")) {
                            String str = s1.substring(s1.indexOf("accurateDetails="), s1.indexOf("referenceId=")).replace(",", "").trim();
                            String s11[] = str.split("\\s+");
                            for (String s5 : s11) {
                                String day = checkIfDay(s5);
                                String timings = checkIfTimings(s5);
                                if (day != null) {
                                    daysList.add(day);
                                }
                                if (timings != null) {
                                    timeList.add(timings);
                                }
                            }
                        }
                        if (s1.contains("referenceId=")) {
                            s1= s1.substring(s1.indexOf("referenceId="), s1.indexOf("urlImage=")).replace(",","").trim();
                            fetchDoctorDetails.add(s1);
                        }
                    }
                }
            }
            tim= concatanateSchedules(daysList, timeList);
            tim= tim.trim();
            fetchDoctorDetails.add(urlImage);
            fetchDoctorDetails.add("Schedule=" + tim);
            mapDoctorToDoctorDetails.put(s, fetchDoctorDetails);
        }
        return mapDoctorToDoctorDetails;
    }

    private static String concatanateSchedules(ArrayList<String> daysList, ArrayList<String> timeList) {
        String tim="";
        for (int i=0, i1=0; (i<daysList.size() && i1<timeList.size()); i++, i1++) {
            String concatSchedule= daysList.get(i) + " " + timeList.get(i1);
            if (!tim.contains(concatSchedule)) {
                tim+= concatSchedule + " ";
            }
        }
        return tim;
    }

    private static String checkIfTimings(String s5) {
        String regex= "\\d+:\\d+";
        Pattern pattern= Pattern.compile(regex);
        Matcher matcher= pattern.matcher(s5);
        if (matcher.matches()) {
            return s5;
        }
        else {
            return null;
        }
    }

    private static String checkIfDay(String s5) {
        if (s5.trim().equals("Mon") || s5.trim().equals("Tue") ||
                s5.trim().equals("Wed") || s5.trim().equals("Thr") ||
                s5.trim().equals("Fri") || s5.trim().equals("Sat") || s5.trim().equals("Sun")) {
            return s5;
        }
        return null;
    }
}