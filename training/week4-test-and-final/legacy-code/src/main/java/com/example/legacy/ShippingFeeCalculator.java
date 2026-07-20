package com.example.legacy;

import java.util.List;

public class ShippingFeeCalculator {

    private static final int LIGHT_WEIGHT_LIMIT = 2000;
    private static final int HEAVY_WEIGHT_LIMIT = 5000;
    private static final int EAST_LIGHT_FEE = 500;
    private static final int EAST_MEDIUM_FEE = 800;
    private static final int EAST_HEAVY_FEE = 1200;

    private static final int WEST_LIGHT_FEE = 600;
    private static final int WEST_MEDIUM_FEE = 900;
    private static final int WEST_HEAVY_FEE = 1300;

    private static final int OTHER_LIGHT_FEE = 700;
    private static final int OTHER_MEDIUM_FEE = 1000;
    private static final int OTHER_HEAVY_FEE = 1500;

    public int calc(List<String> lines) {
        int totalFee = 0;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",");
            try {
                String region = parts[0].trim();
                int weight = Integer.parseInt(parts[1].trim());
                int lineFee = feeFor(region, weight);
                totalFee = totalFee + lineFee;
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.err.println("壊れた行をスキップ: " + line + " (" + e.getMessage() + ")");
            }
        }
        return totalFee;
    }

    public int calcWithDiscount(List<String> lines, int memberRank) {
        int totalFee = 0;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",");
            try {
                String region = parts[0].trim();
                int weight = Integer.parseInt(parts[1].trim());
                int lineFee = feeFor(region, weight);   
                totalFee = totalFee + lineFee;
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.err.println("壊れた行をスキップ: " + line + " (" + e.getMessage() + ")");
            }
        }
        if (memberRank == 3) {
            totalFee = totalFee - (totalFee * 20 / 100);
        } else if (memberRank == 2) {
            totalFee = totalFee - (totalFee * 10 / 100);
        } else if (memberRank == 1) {
            totalFee = totalFee - (totalFee * 5 / 100);
        }
        return totalFee;
    }
    private int feeFor(String region, int weight) {
        if (region.equals("east")) {
            if (weight <= LIGHT_WEIGHT_LIMIT) {
                return EAST_LIGHT_FEE;
            } else if (weight <= HEAVY_WEIGHT_LIMIT) {
                return EAST_MEDIUM_FEE;
            } else {
                return EAST_HEAVY_FEE;
            }
        } else if (region.equals("west")) {
            if (weight <= LIGHT_WEIGHT_LIMIT) {
                return WEST_LIGHT_FEE;
            } else if (weight <= HEAVY_WEIGHT_LIMIT) {
                return WEST_MEDIUM_FEE;
            } else {
                return WEST_HEAVY_FEE;
            }
        } else {
            if (weight <= LIGHT_WEIGHT_LIMIT) {
                return OTHER_LIGHT_FEE;
            } else if (weight <= HEAVY_WEIGHT_LIMIT) {
                return OTHER_MEDIUM_FEE;
            } else {
                return OTHER_HEAVY_FEE;
            }
        }
    }
}
