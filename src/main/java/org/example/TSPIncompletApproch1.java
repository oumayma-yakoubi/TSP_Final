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
    public  static Result tspIncompletSolution1(float[][] distMatrix, int numCities, int[][] timeWindowsMatrix , float[][] timeMatrix, float pnoise ){

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
        visited[currentCity] = true ;

        while(path.size() < numCities){
            int nextCity = -1;
            float minDistance = Float.MAX_VALUE;

            List<Integer> availableCities = new ArrayList<>();

            for(int city = 0 ; city < numCities; city++){
                if(!visited[city]){
                    float arrivalTime = currentTime + timeMatrix[currentCity][city];
                    int openTime =  timeWindowsMatrix[city][0];
                    int closeTime = timeWindowsMatrix[city][1];

                    if(arrivalTime >= openTime && arrivalTime <= closeTime ){
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
                            // Cas 1 : La ville est non visitée mais pas encore disponible
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
            if (pnoise < random.nextFloat()){
                nextCity = availableCities.get(random.nextInt(availableCities.size()));
                minDistance = distMatrix[currentCity][nextCity]; // minDistance garde la valeur de la distance entre currentCity et la ville prochaine choisie aléatoirement
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
            visitTimes.add(currentTime);

            currentCity = nextCity;
            totalDistance +=minDistance;


        }
        totalDistance += distMatrix[currentCity][path.get(0)];

        long executionEndTime = System.currentTimeMillis();

        long execution_time = executionEndTime - executionStartTime;

        //System.out.println("Parcours : " + path);

        // Affichage structuré
        //System.out.println("Visites des villes :");
//        for (int i = 0; i < path.size(); i++) {
//            System.out.println("Ville " + path.get(i) + " --> " + visitTimes.get(i) + " unités de temps");
//        }
        //System.out.println("Distance totale : " + totalDistance + "Km");

        return new Result(path, totalDistance, execution_time);
    }

}
