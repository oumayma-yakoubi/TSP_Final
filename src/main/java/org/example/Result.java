package org.example;

import java.util.List;
import java.util.Objects;

public class Result {
    List<Integer> path;
    float distance;
    float executionTime;


    Result(List<Integer> path, float distance, float executionTime) {
        this.path = path;
        this.distance = distance;
        this.executionTime = executionTime;

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return Objects.equals(path, result.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}