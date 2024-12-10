package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


// Approche 3 consiste à choisir aléatoirement une ville parmi les villes disponibles
public class TSPIncompletApproch3 {
    public  static Result tspIncompletSolution3(float[][] distMatrix, int numCities, int[][] timeWindowsMatrix , float[][] timeMatrix, long timeoutMillis){

        long startTime = System.currentTimeMillis(); // Démarrer le chronomètre
        Random random = new Random();
        List<Result> allPaths = new ArrayList<>();
        Result bestResult = null; // Variable pour stocker le meilleur chemin

        long executionStartTime = System.currentTimeMillis();
        int nb_max_path=1;

        for(int i=1;i<numCities;i++){
            nb_max_path=nb_max_path*i;
        }
        nb_max_path = nb_max_path/2;

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
                        int closeTime = timeWindowsMatrix[city][1];

                        if(arrivalTime >= openTime && arrivalTime <= closeTime){
                            availableCities.add(city);
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
                                // Cas 1 : La ville est non visitée, mais pas encore disponible
                                nextAvailabilityTime = Math.min(nextAvailabilityTime, openTime);
                                allClosed = false; // Il y a au moins une ville qui ouvrira plus tard
                            }
                        }
                    }
                    if (!allClosed){
                        // Si une ville devient disponible plus tard, avancer le temps à son ouverture
                        currentTime = Math.max(currentTime, nextAvailabilityTime);
                        //System.out.println("Avancement du temps au prochain créneau disponible : " + currentTime);
                    }else{
                        // Si toutes les villes sont fermées, réinitialiser
                        //System.out.println("Toutes les villes sont fermées ou inaccessibles pour cette journée.");
                        currentTime = 0; // Réinitialiser pour le prochain jour
                    }
                    continue; // Recalculer les villes disponibles avec le temps mis à jour
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

            long executionEndTime = System.currentTimeMillis();

            long execution_time = executionEndTime - executionStartTime;

            // Sauvegarder le chemin et la distance dans allPaths
            Result result = new Result(new ArrayList<>(path), totalDistance, execution_time);
            if(!allPaths.contains(result)){
                allPaths.add(result);
                if(bestResult == null || result.distance < bestResult.distance){
                    bestResult = result;
                }
            }
        }

        // Optionnel : Afficher tous les résultats après la fin du timeout
//        System.out.println("Tous les chemins parcourus pendant le timeout :");
//        for (Result result : allPaths) {
//            System.out.println("Chemin : " + result.path + " Distance totale : " + result.distance);
//        }

        //System.out.println("Meilleur chemin : " + bestResult.path + " avec une distance totale de : " + bestResult.distance);
        return new Result(bestResult.path, bestResult.distance, bestResult.executionTime);
    }
}
