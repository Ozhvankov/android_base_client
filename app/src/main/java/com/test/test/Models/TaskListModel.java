package com.test.test.Models;

import androidx.annotation.NonNull;

public class TaskListModel {
    public String initialLPN, id, Location;

    public TaskListModel(String id, String InitialLPN, String Location) {
        this.id = id;
        this.initialLPN = InitialLPN;
        this.Location = Location;
    }

    @NonNull
    @Override
    public String toString() {
        return initialLPN;
    }
}
