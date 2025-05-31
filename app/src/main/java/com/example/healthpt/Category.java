package com.example.healthpt;

import java.util.List;

public class Category {
    private String categoryName;
    private List<Machine> machineList;

    public Category(String categoryName, List<Machine> machineList) {
        this.categoryName = categoryName;
        this.machineList = machineList;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<Machine> getMachineList() {
        return machineList;
    }
}
