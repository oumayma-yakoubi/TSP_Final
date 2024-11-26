package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TSPIncompletApproch3 {
    public  static void tspIncompletSolution3(float[][] distMatrix, int numCities, int[][] timeWindowsMatrix , float[][] timeMatrix, long timeoutMillis){

        long startTime = System.currentTimeMillis(); // Démarrer le chronomètre
        Random random = new Random();
        List<Result> allPaths = new ArrayList<>();

        while (System.currentTimeMillis() - startTime < timeoutMillis){
            // Initialisation de la ville de départ et des variables
            boolean[] visited = new boolean[numCities];
            List<Integer> path = new ArrayList<>();
            int currentCity = 0;
            float currentTime = 0;
            float totalDistance = 0;

            // Ajouter la ville de départ
            path.add(currentCity);
            visited[currentCity] = true;

            // Effectuer le parcours tant que le nombre de villes visitées est inférieur à numCities
            while(path.size() < numCities){
                int nextCity = -1;
                List<Integer> availableCities = new ArrayList<>();

                // Trouver les villes disponibles
                for(int city = 0 ; city < numCities; city++){
                    if(!visited[city]){
                        float arrivalTime = currentTime + timeMatrix[currentCity][city];
                        int openTime = timeWindowsMatrix[city][0];

                        if(arrivalTime >= openTime){
                            availableCities.add(city);
                        }
                    }
                }

                // Si aucune ville n'est disponible, attendre que la première ville devienne disponible
                if (availableCities.isEmpty()){
                    float nextAvailabilityTime = Float.MAX_VALUE;
                    for(int city = 0 ; city < numCities ; city++){
                        if(!visited[city]){
                            nextAvailabilityTime = Math.min(nextAvailabilityTime, timeWindowsMatrix[city][0]);
                        }
                    }
                    currentTime = nextAvailabilityTime;
                    continue;
                }

                // Choisir une ville au hasard parmi celles disponibles
                nextCity = availableCities.get(random.nextInt(availableCities.size()));
                float distance = distMatrix[currentCity][nextCity];

                // Marquer la ville comme visitée et l'ajouter au chemin
                visited[nextCity] = true;
                path.add(nextCity);
                currentTime += timeMatrix[currentCity][nextCity]; // Mettre à jour le temps actuel
                totalDistance += distance; // Ajouter la distance parcourue
                currentCity = nextCity; // Passer à la ville suivante
            }

            // Ajouter la distance pour revenir à la ville de départ
            totalDistance += distMatrix[currentCity][path.get(0)];

            // Sauvegarder le chemin et la distance dans allPaths
            allPaths.add(new Result(new ArrayList<>(path), totalDistance));

            // Afficher le chemin et la distance
            System.out.println("Parcours : " + path);
            System.out.println("Distance totale : " + totalDistance);
        }

        // Optionnel : Afficher tous les résultats après la fin du timeout
        System.out.println("Tous les chemins parcourus pendant le timeout :");
        for (Result result : allPaths) {
            System.out.println("Chemin : " + result.path + " Distance totale : " + result.distance);
        }
    }
}
