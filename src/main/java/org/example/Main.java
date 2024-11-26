package org.example;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        String filePath = "D:\\TSP_projet\\src\\Data\\small\\n4_instance1.txt";
        try{
            DataProcessor dataProcessor = new DataProcessor(filePath);

            int numCities = dataProcessor.getNumCities();
            float[][] distMatrix = dataProcessor.getDistMatrix();
            int[][] timeWindowsMatrix = dataProcessor.getTimeWindowsMatrix();

            float [][] timeMatrix = new float[numCities][numCities];

            // Vitesse (en km/h) == (km/ 60min)
            float speed = 80;
            for (int i = 0; i < numCities; i++) {
                for (int j = 0; j < numCities; j++) {
                    // Calculer le temps = distance / vitesse
                    timeMatrix[i][j] =  (distMatrix[i][j] * 60 / speed);
                }
            }

            System.out.println("La matrice Distance");

            for (float[] row : distMatrix) {
                for (double value : row) {
                    System.out.printf("%.2f ", value); // Affichage avec 2 décimales
                }
                System.out.println();
            }
            System.out.println("La matrice Time Correspondante");
            for (float[] row : timeMatrix) {
                for (double value : row) {
                    System.out.printf("%.2f ", value); // Affichage avec 2 décimales
                }
                System.out.println();
            }


//            float pn = 0.6f;
//            System.out.println("################## Exploitation #####################");
//            TSPIncompletApproch1.tspIncompletSolution1(distMatrix, numCities, timeWindowsMatrix, timeMatrix, pn);

            float pn = 1f;
            TSPIncompletApproch3.tspIncompletSolution3(distMatrix, numCities, timeWindowsMatrix, timeMatrix, 20);

        } catch (IOException e){
            System.err.println("Erreur lors de la lecture du fichier");
        }


    }
}