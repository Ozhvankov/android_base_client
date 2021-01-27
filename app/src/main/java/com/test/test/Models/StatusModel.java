package com.test.test.Models;

import androidx.annotation.NonNull;

public class StatusModel {
    private String name;
    private int number;

    public StatusModel(int number, String name) {
        this.number = number;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
