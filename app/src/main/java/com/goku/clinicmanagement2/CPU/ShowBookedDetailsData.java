package com.goku.clinicmanagement2.CPU;
import java.util.*;

public class ShowBookedDetailsData {

    public Set<Set<String>> init(String data, String name, String mobile) {
        Set<Set<String>> aList= new LinkedHashSet<>();
        //  System.out.println("data: " + data);
        String split[]= data.split("patient_Details=");
        for (String s: split) {
            Set<String> set1= new LinkedHashSet<>();
            if (!s.trim().isEmpty()) {
                s= s.trim();
                if (s.contains(name) && s.contains(mobile)) {
                    System.out.println("split: " + s);
                    if (s.contains("DoctorName:")) {
                        set1.add(s.substring(s.indexOf("DoctorName:"), s.indexOf("Doctor fees:")).trim());
                    }
                    if (s.contains("Doctor fees:")) {
                        set1.add(s.substring(s.indexOf("Doctor fees:"), s.indexOf("Doctor Schedule:")).replace("Doctor Fees:","").trim());
                    }
                    if (s.contains("Doctor Schedule:")) {
                        set1.add(s.substring(s.indexOf("Doctor Schedule:"), s.indexOf("Docto ImageUrl:")).trim());
                    }
                    aList.add(set1);
                }
            }
        }
        //  System.out.println("aList: " + aList);
        return aList;
    }
}