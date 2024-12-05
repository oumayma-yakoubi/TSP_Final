package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataProcessor {

    private int numCities;
    private float[][] distMatrix;
    private int[][] timeWindowsMatrix;

    public DataProcessor() {
    }
    public DataProcessor(String filePath) throws IOException{
        processFile(filePath);
    }

    public void processFile(String filePath) throws  IOException{
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            numCities = Integer.parseInt(br.readLine().trim());
            distMatrix = new float[numCities][numCities];

            for (int i = 0; i < numCities; i++){
                String[] distances = br.readLine().trim().split("\\s+");
                for (int j = 0 ; j < numCities; j++){
                    distMatrix[i][j] = Float.parseFloat(distances[j]);
                }
            }

            timeWindowsMatrix = new int[numCities][2];
            for (int i = 0; i < numCities; i++){
               String[] timeWindows = br.readLine().trim().split("\\s+");
               timeWindowsMatrix[i][0] = Integer.parseInt(timeWindows[0]);
               timeWindowsMatrix[i][1] = Integer.parseInt(timeWindows[1]);
            }
        }
    }
    public int getNumCities() {
        return numCities;
    }
    public float[][] getDistMatrix(){
        return distMatrix;
    }
    public int[][] getTimeWindowsMatrix() {
        return timeWindowsMatrix;
    }

}
