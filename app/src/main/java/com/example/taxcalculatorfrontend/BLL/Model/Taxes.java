package com.example.taxcalculatorfrontend.BLL.Model;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Taxes {
    double grossRevenue,amountDue,netRevenue,federalRate,provincialRate;

    public Taxes(){
    }

    public Taxes(double grossRevenue){
        this.grossRevenue = grossRevenue;
    }

    public Taxes(double grossRevenue, String apiString){
        String[] parts = apiString.split(";");

        this.grossRevenue = round(grossRevenue);

        try {this.amountDue = round(Double.parseDouble(parts[0]));}
        catch (Exception e){throw new RuntimeException(e);}

        try {this.netRevenue = round(Double.parseDouble(parts[1]));}
        catch (Exception e){throw new RuntimeException(e);}

        try {this.federalRate = round(Double.parseDouble(parts[2]));}
        catch (Exception e){throw new RuntimeException(e);}

        try {this.provincialRate = round(Double.parseDouble(parts[3]));}
        catch (Exception e){throw new RuntimeException(e);}
    }

    public Taxes(double grossRevenue, double federalRate, double provincialRate) {
        this.grossRevenue = grossRevenue;
        this.federalRate = federalRate;
        this.provincialRate = provincialRate;
        this.amountDue = federalRate+provincialRate;
    }

    public double getGrossRevenue() {
        return grossRevenue;
    }

    public void setGrossRevenue(double grossRevenue) {
        this.grossRevenue = grossRevenue;
    }

    public double getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(double amountDue) {
        this.amountDue = amountDue;
    }

    public double getNetRevenue() {
        return netRevenue;
    }

    public void setNetRevenue(double netRevenue) {
        this.netRevenue = netRevenue;
    }

    public double getFederalRate() {
        return federalRate;
    }

    public void setFederalRate(double federalRate) {
        this.federalRate = federalRate;
    }

    public double getProvincialRate() {
        return provincialRate;
    }

    public void setProvincialRate(double provincialRate) {
        this.provincialRate = provincialRate;
    }

    @Override
    public String toString() {
        return "Taxes{" +
                "grossRevenue=" + grossRevenue +
                ", amountDue=" + amountDue +
                ", netRevenue=" + netRevenue +
                ", federalRate=" + federalRate +
                ", provincialRate=" + provincialRate +
                '}';
    }

    public void setUsingApiString(String apiString){
        String[] parts = apiString.split(";");

        try {this.amountDue = Double.parseDouble(parts[0]);}
        catch (Exception e){throw new RuntimeException(e);}

        try {this.netRevenue = Double.parseDouble(parts[1]);}
        catch (Exception e){throw new RuntimeException(e);}

        try {this.federalRate = Double.parseDouble(parts[2]);}
        catch (Exception e){throw new RuntimeException(e);}

        try {this.provincialRate = Double.parseDouble(parts[3]);}
        catch (Exception e){throw new RuntimeException(e);}
    }

    private double round(double value){
        int places=2;
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
