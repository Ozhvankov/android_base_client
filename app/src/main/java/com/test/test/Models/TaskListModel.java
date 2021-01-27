package com.test.test.Models;

import androidx.annotation.NonNull;

public class TaskListModel {
    public String initialLPN, id;

    public TaskListModel(String id, String InitialLPN) {
        this.id = id;
        this.initialLPN = InitialLPN;
    }

    @NonNull
    @Override
    public String toString() {
        return initialLPN;
    }
}
