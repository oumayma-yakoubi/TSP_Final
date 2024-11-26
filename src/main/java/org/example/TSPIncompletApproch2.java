package org.example;


// ################## Approche 2 ########################################"
// Ici, on choisit la ville à visiter selon la disponibilité
// Si plusieurs villes sont disponibles à la fois ; on choisit la ville ayant une fenêtre de temps plus petite.
// Si plusieurs villes ont la même largeur de la fenêtre de temps, on choisit la ville la plus proche.
// Nous avons implémenté un paramètre epsilon noise qui permet d'explorer
// en choisissant la ville prochaine aléatoirement parmi les villes disponibles.

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TSPIncompletApproch2 {

    public static void tspIncompletSolution2(float[][] distMatrix, int numCities, int[][] timeWindowsMatrix , float[][] timeMatrix, float pnoise){

        boolean[] visited = new boolean[numCities];
        List<Integer> path = new ArrayList<>();
        int currentCity = 0;
        float currentTime = 0;
        float totalDistance = 0;
        Random random = new Random();

        path.add(currentCity);
        visited[currentCity] = true;

        while(path.size() < numCities){
            List<Integer> availableCities = new ArrayList<>();
            int nextCity = -1;
            float minDistance = Float.MAX_VALUE;

            for(int city = 0 ; city < numCities; city++){
                if(!visited[city]){
                    float arrivalTime = currentTime + timeMatrix[currentCity][city];
                    int openTime = timeWindowsMatrix[city][0];

                    if(arrivalTime >= openTime){
                        availableCities.add(city);
                    }
                }
            }
            if(!availableCities.isEmpty()) {
                int minWidth = Integer.MAX_VALUE;
                for (int city : availableCities) {
                    int width = timeWindowsMatrix[city][1] - timeWindowsMatrix[city][0];
                    if (width < minWidth) {
                        nextCity = city;
                        minWidth = width;
                        minDistance = distMatrix[currentCity][nextCity];
                    } else if (width == minWidth) {
                        float currentDistance = distMatrix[currentCity][city];
                        if (currentDistance < minDistance) {
                            nextCity = city;
                            minDistance = currentDistance;
                        }
                    }
                }
            }
                if (availableCities.isEmpty()){
                    // Aucune ville disponible, attendre jusqu'à la prochaine fenêtre
                    float nextAvailableTime = Float.MAX_VALUE;
                    for (int i = 0; i < numCities; i++) {
                        if (!visited[i]) {
                            nextAvailableTime = Math.min(nextAvailableTime, timeWindowsMatrix[i][0]);
                        }
                    }
                    currentTime = nextAvailableTime;
                    continue; // Recommencer la boucle avec le nouveau temps courant
                }
                if (nextCity == -1){ // Aucune ville n'est disponible.
                    System.out.println("Aucune ville atteignable à partir de la ville :" + currentCity);
                    break;
                }
                visited[nextCity] = true;
                path.add(nextCity);
                currentTime += timeMatrix[currentCity][nextCity];

                currentCity = nextCity;
                totalDistance += minDistance;
            }
            totalDistance += distMatrix[currentCity][path.get(0)];

            System.out.println("Parcours : " + path);
            System.out.println("Distance Totale : " + totalDistance);

    }
}
