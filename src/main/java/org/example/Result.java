package org.example;

import java.util.List;

public class Result {
    List<Integer> path;
    float distance;

    Result(List<Integer> path, float distance) {
        this.path = path;
        this.distance = distance;
    }
}