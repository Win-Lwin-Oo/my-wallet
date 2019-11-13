package com.winlwinoo.mywallet.DataModel;

public class ItemData {
    String usage_name,category_name,usage_date;
    int usage_amount;

    public String getUsage_name() {
        return usage_name;
    }

    public void setUsage_name(String usage_name) {
        this.usage_name = usage_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getUsage_date() {
        return usage_date;
    }

    public void setUsage_date(String usage_date) {
        this.usage_date = usage_date;
    }

    public int getUsage_amount() {
        return usage_amount;
    }

    public void setUsage_amount(int usage_amount) {
        this.usage_amount = usage_amount;
    }
}
