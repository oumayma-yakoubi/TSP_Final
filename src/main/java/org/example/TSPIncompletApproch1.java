package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// ################## Approche 1 ########################################"
// Ici, on choisit la ville à visiter selon la disponibilité
// Si plusieurs villes sont disponibles à la fois ; on choisit la ville la plus proche.
// Nous avons implémenté un paramètre epsilon noise qui permet d'explorer
// en choisissant la ville prochaine aléatoirement parmi les villes disponibles.


public class TSPIncompletApproch1 {
    public  static void tspIncompletSolution1(float[][] distMatrix, int numCities, int[][] timeWindowsMatrix , float[][] timeMatrix, float pnoise ){

        boolean[] visited = new boolean[numCities];
        List<Integer> path = new ArrayList<>();

        int currentCity = 0;
        float currentTime = 0;
        float totalDistance = 0;

        Random random = new Random();

        path.add(currentCity);
        visited[currentCity] = true ;

        while(path.size() < numCities){
            int nextCity = -1;
            float minDistance = Float.MAX_VALUE;

            List<Integer> availableCities = new ArrayList<>();

            for(int city = 0 ; city < numCities; city++){
                if(!visited[city]){
                    float arrivalTime = currentTime + timeMatrix[currentCity][city];
                    int openTime = timeWindowsMatrix[city][0];

                    if(arrivalTime >= openTime){
                        availableCities.add(city);
                    }
                }
            }
            if (availableCities.isEmpty()){ // Aucune ville disponible, attentre jusqu'à ce qu'au moins une ville soit disponible.
                float nextAvailabilityTime = Float.MAX_VALUE;
                for(int city = 0 ; city < numCities ; city++){
                    if(!visited[city]){
                        nextAvailabilityTime = Math.min(nextAvailabilityTime, timeWindowsMatrix[city][0]);
                    }
                }
                currentTime = nextAvailabilityTime;
                continue;
            }
            if (pnoise < random.nextFloat()){
                nextCity = availableCities.get(random.nextInt(availableCities.size()));
                minDistance = distMatrix[currentCity][nextCity];
            }else{
                for(int city : availableCities){
                    if(distMatrix[currentCity][city] < minDistance){
                        nextCity = city;
                        minDistance = distMatrix[currentCity][city];
                    }
                }
            }
            if (nextCity == -1){
                break;
            }
            visited[nextCity] = true;
            path.add(nextCity);

            currentTime += timeMatrix[currentCity][nextCity];

            currentCity = nextCity;
            totalDistance +=minDistance;
        }
        totalDistance += distMatrix[currentCity][path.get(0)];

        System.out.println("Parcours : " + path);
        System.out.println("Distance totale : " + totalDistance);
    }

}
