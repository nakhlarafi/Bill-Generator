package com.nakhla.billgenerator;

import java.io.Serializable;

public class Calculations implements Serializable {

    public String slNo, date, challan, nameOfVolgate, quantity, rate, amount, remark;

    public Calculations() {
    }

    public Calculations(String slNo, String date, String challan, String nameOfVolgate,
                        String quantity, String rate, String amount, String remark) {
        this.slNo = slNo;
        this.date = date;
        this.challan = challan;
        this.nameOfVolgate = nameOfVolgate;
        this.quantity = quantity;
        this.rate = rate;
        this.amount = amount;
        this.remark = remark;
    }

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChallan() {
        return challan;
    }

    public void setChallan(String challan) {
        this.challan = challan;
    }

    public String getNameOfVolgate() {
        return nameOfVolgate;
    }

    public void setNameOfVolgate(String nameOfVolgate) {
        this.nameOfVolgate = nameOfVolgate;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
