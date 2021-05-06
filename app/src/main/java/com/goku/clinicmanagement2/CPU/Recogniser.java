package com.goku.clinicmanagement2.CPU;

import java.util.LinkedHashSet;
import java.util.Set;

public class Recogniser {
    public Set<String> init(String data, String specialization) {
        Set<String> getChild= new LinkedHashSet<>();
        String split[]= data.split("\\{|\\}|,|\\[|null");
        for (String s: split) {
            if (!s.isEmpty()) {
                if (s.contains(specialization)) {
                    if (!s.contains("urlImage=")) {
                        s= s.trim();
                        if (s.contains("=")) {
                            s= s.replace("=","");
                            //  System.out.println(s);
                            s= s.trim();
                            getChild.add(s);
                        }
                    }
                }
            }
        }
        return getChild;
    }
}
