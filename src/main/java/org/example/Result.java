package org.example;

import java.util.List;

public class Result {
    List<Integer> path;
    float distance;
    float executionTime;


    Result(List<Integer> path, float distance, float executionTime) {
        this.path = path;
        this.distance = distance;
        this.executionTime = executionTime;

    }
}