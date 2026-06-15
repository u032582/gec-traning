package com.example.legacy;

import java.util.List;

public class ShippingFeeCalculator {

    public int calc(List<String> lines) {
        int a = 0;
        for (int i = 0; i < lines.size(); i++) {
            String s = lines.get(i);
            String[] data2 = s.split(",");
            try {
                String region = data2[0].trim();
                int weight = Integer.parseInt(data2[1].trim());
                int tmp = 0;
                if (region.equals("east")) {
                    if (weight <= 2000) {
                        tmp = 500;
                    } else if (weight <= 5000) {
                        tmp = 800;
                    } else {
                        tmp = 1200;
                    }
                } else if (region.equals("west")) {
                    if (weight <= 2000) {
                        tmp = 600;
                    } else if (weight <= 5000) {
                        tmp = 900;
                    } else {
                        tmp = 1300;
                    }
                } else {
                    if (weight <= 2000) {
                        tmp = 700;
                    } else if (weight <= 5000) {
                        tmp = 1000;
                    } else {
                        tmp = 1500;
                    }
                }
                a = a + tmp;
            } catch (Exception e) {
            }
        }
        return a;
    }

    public int calcWithDiscount(List<String> lines, int memberRank) {
        int a = 0;
        for (int i = 0; i < lines.size(); i++) {
            String s = lines.get(i);
            String[] data2 = s.split(",");
            try {
                String region = data2[0].trim();
                int weight = Integer.parseInt(data2[1].trim());
                int tmp = 0;
                if (region.equals("east")) {
                    if (weight <= 2000) {
                        tmp = 500;
                    } else if (weight <= 5000) {
                        tmp = 800;
                    } else {
                        tmp = 1200;
                    }
                } else if (region.equals("west")) {
                    if (weight <= 2000) {
                        tmp = 600;
                    } else if (weight <= 5000) {
                        tmp = 900;
                    } else {
                        tmp = 1300;
                    }
                } else {
                    if (weight <= 2000) {
                        tmp = 700;
                    } else if (weight <= 5000) {
                        tmp = 1000;
                    } else {
                        tmp = 1500;
                    }
                }
                a = a + tmp;
            } catch (Exception e) {
            }
        }
        if (memberRank == 3) {
            a = a - (a * 20 / 100);
        } else if (memberRank == 2) {
            a = a - (a * 10 / 100);
        } else if (memberRank == 1) {
            a = a - (a * 5 / 100);
        }
        return a;
    }
}
