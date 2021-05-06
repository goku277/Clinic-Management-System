package com.goku.clinicmanagement2.CPU;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JoinData {
    public static class Node {
        String data;
        Node left, right;
        public Node(String data) {
            this.data= data;
            left= right= null;
        }
    }
    Node head, tail;
    public void insert(String data, JoinData jd) {
        Node new_node= new Node(data);
        if (jd.head== null && jd.tail== null) {
            jd.head=new_node;
            jd.tail= new_node;
            jd.head.left= null;
            jd.tail.right= null;
        }
        else {
            Node curr= jd.tail;
            jd.tail.right= new_node;
            jd.tail= new_node;
            jd.tail.left= curr;
        }
    }
    public void traverse(JoinData jd) {
        Node curr= jd.head;
        while (curr!=null) {
            System.out.print("curr.data: "+ curr.data + "->");
            curr= curr.right;
        }
    }
    public void traverseReverseOrder(JoinData jd) {
        Node last= jd.tail;
        while (last!=null) {
            System.out.print(last.data + "->");
            last= last.left;
        }
    }
    public String matchAndConcat(JoinData jd) {
        Node last= jd.tail;
        String getDoctorName= "", getDoctorUrlImage="";
        // if (last!=null) {
        while (last.left != null) {
            if (!isUrl(last.data)) {
                getDoctorName += last.data + " ";
            } else {
                getDoctorUrlImage += last.data;
            }
            last = last.left;
        }
        // }
        String revName[]= getDoctorName.split(" "), concatDoctorName="";

        for (int i=revName.length-1; i>=0; i--) {
            concatDoctorName+= revName[i] + " ";
        }
        return concatDoctorName + "\t\t" + getDoctorUrlImage;
    }

    private boolean isUrl(String data) {
        System.out.println("isUrl() called:");
        String regex="urlImage=https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";
        Pattern pattern= Pattern.compile(regex);
        Matcher matcher= pattern.matcher(data);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public String initJoinData() {
        Scanner sc= new Scanner(System.in);
        String data= "";
        String splitData[]= data.split(" ");
        JoinData jdt= new JoinData();
        for (String s: splitData) {
            if (!s.isEmpty()) {
                System.out.println("splitData: " + s);
                jdt.insert(s,jdt);
            }

        }

        return jdt.matchAndConcat(jdt);


        // return "";
        // System.out.println(jdt.tail.left.data);
    }
}
