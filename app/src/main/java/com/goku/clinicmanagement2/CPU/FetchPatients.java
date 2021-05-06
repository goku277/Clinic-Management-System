package com.goku.clinicmanagement2.CPU;

import java.util.*;
public class FetchPatients {
    static Map<String, Set<String>> mapPatientToPatientDetails= new LinkedHashMap<>();
    public Map<String, Set<String>> init(String data) {
        System.out.println("data: " + data);
        String split[]= data.split("\\}\\]\\}|\\}\\]\\}\\}|\\{");
        for (String s: split) {
            if (!s.trim().isEmpty()) {
                if (s.contains("name=")) {
                    placeData(s);
                    System.out.println();
                }
            }
        }
        System.out.println("mapPatientToPatientDetails: " + mapPatientToPatientDetails);
        return mapPatientToPatientDetails;
    }

    private static void placeData(String s) {
        Set<String> set1= new LinkedHashSet<>();
        String s1[]= s.split(","), key= "", name1="", mobile1="";
        for (String s11: s1) {
            s11= s11.trim();
            System.out.println("s11: " + s11);
            if (s11.contains("name=")) {
              //  s11= s11.replace("name=","").trim();
                name1= s11;
                if (!set1.contains(name1)) {
                    set1.add(name1);
                }
            }
            if (s11.contains("gender=")) {
               // s11= s11.replace("gender=","").trim();
                set1.add(s11);
            }
            if (s11.contains("age=")) {
             //   s11= s11.replace("age=","").trim();
                set1.add(s11);
                mobile1= s11;
            }
            set1.add(name1 + " " + mobile1);
            if (s11.contains("mobile=")) {
             //   s11= s11.replace("mobile=","").trim();
                set1.add(s11);
            }
            if (s11.contains("imageUrl=")) {
              //  s11= s11.replace("imageUrl=","").trim();
                set1.add(s11);
            }
        }
        mapPatientToPatientDetails.put(name1, set1);
    }
}