package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        String mainDirectory = "D:\\TSP_projet\\src\\Data";
        String csvOutputFile = "D:\\TSP_projet\\results.csv";


        //String filePath = "D:\\TSP_projet\\src\\Data\\large\\n101_instance1.txt";
        try (BufferedWriter csvWriter = new BufferedWriter(new FileWriter(csvOutputFile))){

            // Écrire l'en-tête du fichier CSV
            csvWriter.write("méthode, nb_villes, chemin, distance_totale, temps_execution, catégorie, epsilon, timeout");
            csvWriter.newLine();

            Files.walk(Paths.get(mainDirectory)).filter(Files::isRegularFile)
                                                .forEach(filePath ->{
                                                    try {
                                                        processFile(filePath, csvWriter);
                                                    } catch (IOException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                });
            } catch (IOException e){
            System.err.println("Erreur lors de l'écriture du fichier CSV");
            e.printStackTrace();
        }


    }

    private static void processFile(Path filePath, BufferedWriter csvWriter) throws IOException {
        String category = filePath.getParent().getFileName().toString();
        System.out.println("categoryyy : " + category);

        DataProcessor dataProcessor = new DataProcessor(filePath.toString());

        int numCities = dataProcessor.getNumCities();
        float[][] distMatrix = dataProcessor.getDistMatrix();
        int[][] timeWindowsMatrix = dataProcessor.getTimeWindowsMatrix();

        float [][] timeMatrix = new float[numCities][numCities];

        float speed = 80; // Vitesse en km/h
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                timeMatrix[i][j] = (distMatrix[i][j] * 60 / speed); // Temps en minutes
            }
        }
        int repetitions = 10;

        Result bestResultApp1_6 = null;
        Result bestResultApp1_3 = null;

        for (int i = 0; i < repetitions; i++) {
            // Essai 1 : avec epsilon 0.6
            Result currentResultApp1_6 = TSPIncompletApproch1.tspIncompletSolution1(distMatrix, numCities, timeWindowsMatrix, timeMatrix, 0.6f);

            // Si c'est la première itération ou si on trouve une meilleure distance, on garde ce résultat
            if (bestResultApp1_6 == null || currentResultApp1_6.distance < bestResultApp1_6.distance) {
                bestResultApp1_6 = currentResultApp1_6;
            }
            // Essai 2 : avec epsilon 0.3
            Result currentResultApp1_3 = TSPIncompletApproch1.tspIncompletSolution1(distMatrix, numCities, timeWindowsMatrix, timeMatrix, 0.3f);

            // Si c'est la première itération ou si on trouve une meilleure distance, on garde ce résultat
            if (bestResultApp1_3 == null || currentResultApp1_3.distance < bestResultApp1_3.distance) {
                bestResultApp1_3 = currentResultApp1_3;
            }
        }
        writeResult(csvWriter, "Incomplet 1", numCities, bestResultApp1_6.path, bestResultApp1_6.distance, bestResultApp1_6.executionTime, category, 0.6, 0);
        writeResult(csvWriter, "Incomplet 1", numCities, bestResultApp1_3.path, bestResultApp1_3.distance, bestResultApp1_3.executionTime, category, 0.3, 0);

        Result resultApp1_full_exploit = TSPIncompletApproch1.tspIncompletSolution1(distMatrix, numCities, timeWindowsMatrix, timeMatrix, 1);
        writeResult(csvWriter, "Incomplet 1", numCities, resultApp1_full_exploit.path, resultApp1_full_exploit.distance, resultApp1_full_exploit.executionTime, category, 1, 0);

        Result resultApproach2 = TSPIncompletApproch2.tspIncompletSolution2(distMatrix, numCities, timeWindowsMatrix, timeMatrix);
        writeResult(csvWriter, "Incomplet 2", numCities, resultApproach2.path, resultApproach2.distance, resultApproach2.executionTime, category, 0, 0);

        Result resultApproach3 = TSPIncompletApproch3.tspIncompletSolution3(distMatrix, numCities, timeWindowsMatrix, timeMatrix, 20);
        writeResult(csvWriter, "Incomplet 3", numCities, resultApproach3.path, resultApproach3.distance, resultApproach3.executionTime, category, 0, 20);

    }

    private static void writeResult(BufferedWriter csvWriter, String typeApproche, int numCities, List<Integer> chemin, float distance, float executionTime, String category, double pnoise, float timeout) throws IOException {
        // Convertir la liste de villes en chaîne (par exemple "0 -> 1 -> 2")
        String cheminString = chemin.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" -> "));

        // Formater les informations à écrire dans le fichier CSV
        String resultLine = String.format("%s,%d,%s,%.2f,%f,%s,%.2f,%f",
                typeApproche, numCities, cheminString, distance, executionTime, category, pnoise, timeout);

        // Écrire la ligne dans le fichier CSV
        csvWriter.write(resultLine);
        csvWriter.newLine();
    }
}