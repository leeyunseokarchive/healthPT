package com.example.healthpt;

public interface routineDao {

    RoutineEntry getByDate(String date);
    void insert(RoutineEntry entry);
}