import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PopulationSimulation implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final SpeciesProfile speciesProfile;
    private final int initialPopulation;
    private final int simulationYears;
    private final List<Integer> populationHistory = new ArrayList<>();
    private double averageGrowthRate;

    public PopulationSimulation(SpeciesProfile speciesProfile, int initialPopulation, int simulationYears) {
        this.speciesProfile = speciesProfile;
        this.initialPopulation = initialPopulation;
        this.simulationYears = simulationYears;
    }

    public void run() {
        populationHistory.clear();
        int currentPopulation = initialPopulation;
        populationHistory.add(currentPopulation);

        for (int year = 1; year <= simulationYears; year++) {
            // Calculate breeding population (only adults)
            int breedingPopulation = calculateBreedingPopulation(year);
            
            // Calculate new offspring with random variation
            double variation = 0.9 + Math.random() * 0.2; // ±10% variation
            int newOffspring = (int) Math.round(
                breedingPopulation * 
                speciesProfile.getAvgLittersPerYear() * 
                speciesProfile.getAvgLitterSize() * 
                speciesProfile.getSurvivalRate() * 
                variation
            );

            // Apply natural limits (carrying capacity)
            newOffspring = applyCarryingCapacity(currentPopulation, newOffspring);
            
            currentPopulation += newOffspring;
            populationHistory.add(currentPopulation);
        }
        
        calculateGrowthRate();
    }

    private int calculateBreedingPopulation(int currentYear) {
        int breedingPopulation = 0;
        // Only animals that have reached reproduction age can breed
        int minBirthYear = currentYear - (speciesProfile.getReproductionAge() / 12);
        if (minBirthYear < 0) return 0;
        
        // Simple model: all animals from previous generations that are still alive
        for (int year = 0; year < currentYear; year++) {
            if (year <= minBirthYear) {
                breedingPopulation += populationHistory.get(year);
            }
        }
        
        return (int) (breedingPopulation * speciesProfile.getSurvivalRate());
    }

    private int applyCarryingCapacity(int currentPopulation, int newOffspring) {
        // Simple carrying capacity model
        final int CARRYING_CAPACITY = 1000000; // Environment limit
        if (currentPopulation + newOffspring > CARRYING_CAPACITY) {
            return Math.max(0, CARRYING_CAPACITY - currentPopulation);
        }
        return newOffspring;
    }

    private void calculateGrowthRate() {
        if (populationHistory.size() < 2) {
            averageGrowthRate = 0;
            return;
        }
        
        double totalGrowth = 0;
        for (int i = 1; i < populationHistory.size(); i++) {
            double growth = (double)(populationHistory.get(i) - populationHistory.get(i-1)) / 
                           populationHistory.get(i-1);
            totalGrowth += growth;
        }
        
        averageGrowthRate = (totalGrowth / (populationHistory.size() - 1)) * 100;
    }

    public int getInitialPopulation() {
        return initialPopulation;
    }

    public int getSimulationYears() {
        return simulationYears;
    }

    // Getters
    public SpeciesProfile getSpeciesProfile() { return speciesProfile; }
    public List<Integer> getPopulationHistory() { return populationHistory; }
    public int getFinalPopulation() { return populationHistory.get(populationHistory.size()-1); }
    public double getAverageGrowthRate() { return averageGrowthRate; }
}