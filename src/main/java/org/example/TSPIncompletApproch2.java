package org.example;


// ################## Approche 2 ########################################"
// Ici, on choisit la ville à visiter selon la disponibilité
// Si plusieurs villes sont disponibles à la fois ; on choisit la ville ayant une fenêtre de temps plus petite.
// Si plusieurs villes ont la même largeur de la fenêtre de temps, on choisit la ville la plus proche.

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TSPIncompletApproch2 {

    public static Result tspIncompletSolution2(float[][] distMatrix, int numCities, int[][] timeWindowsMatrix , float[][] timeMatrix){

        boolean[] visited = new boolean[numCities];
        List<Integer> path = new ArrayList<>();
        List<Float> visitTimes = new ArrayList<>();

        int currentCity = 0;
        float currentTime = 0;
        float totalDistance = 0;
        Random random = new Random();

        long executionStartTime = System.currentTimeMillis();


        path.add(currentCity);
        visitTimes.add(currentTime);
        visited[currentCity] = true;

        while(path.size() < numCities){
            List<Integer> availableCities = new ArrayList<>();
            int nextCity = -1;
            float minDistance = Float.MAX_VALUE;

            for(int city = 0 ; city < numCities; city++){
                if(!visited[city]){
                    float arrivalTime = currentTime + timeMatrix[currentCity][city];
                    int openTime = timeWindowsMatrix[city][0];
                    int closeTime = timeWindowsMatrix[city][1];

                    if(arrivalTime >= openTime && arrivalTime <= closeTime){
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
                // Aucune ville n'est actuellement disponible, analyser les deux cas.
                // Cas 1 : ville non visitée, mais n'est pas encore disponible ? (arrival time < openTime)
                // Cas 2 : ville non visitée, mais elle est déjà fermée ? (arrival time > closeTime)

                float nextAvailabilityTime = Float.MAX_VALUE;
                boolean allClosed = true;
                for (int city = 0 ; city < numCities; city++){
                    if (!visited[city]){
                        float arrivalTime = currentTime + timeMatrix[currentCity][city];
                        int openTime = timeWindowsMatrix[city][0];
                        int closeTime = timeWindowsMatrix[city][1];

                        if(arrivalTime < openTime){
                            // Cas 1 : La ville est non visitée mais pas encore disponible
                            nextAvailabilityTime = Math.min(nextAvailabilityTime, openTime);
                            allClosed = false; // Il y a au moins une ville qui ouvrira plus tard
                        }
                    }
                }
                if (!allClosed){
                    // Si une ville devient disponible plus tard, avancer le temps à son ouverture
                    currentTime = Math.max(currentTime, nextAvailabilityTime);
                    System.out.println("Avancement du temps au prochain créneau disponible : " + currentTime);
                }else{
                    // Si toutes les villes sont fermées, réinitialiser
                    System.out.println("Toutes les villes sont fermées ou inaccessibles pour cette journée.");
                    currentTime = 0; // Réinitialiser pour le prochain jour
                }
                continue; // Recalculer les villes disponibles avec le temps mis à jour
            }
            if (nextCity == -1){ // Aucune ville n'est disponible.
                System.out.println("Aucune ville atteignable à partir de la ville :" + currentCity);
                break;
            }
            //System.out.println("I'm in the city : " + nextCity );
            visited[nextCity] = true;
            path.add(nextCity);

            currentTime += timeMatrix[currentCity][nextCity];
            visitTimes.add(currentTime);


            currentCity = nextCity;
            totalDistance += minDistance;
        }
        totalDistance += distMatrix[currentCity][path.get(0)];

        long executionEndTime = System.currentTimeMillis();

        long execution_time = executionEndTime - executionStartTime;


        System.out.println("Parcours : " + path);
        System.out.println("Distance Totale : " + totalDistance);

        // Affichage structuré
        System.out.println("Visites des villes :");
        for (int i = 0; i < path.size(); i++) {
            System.out.println("Ville " + path.get(i) + " --> " + visitTimes.get(i) + " unités de temps");
        }
        System.out.println("Distance totale : " + totalDistance + "Km");

        return new Result(path, totalDistance, execution_time);

    }
}