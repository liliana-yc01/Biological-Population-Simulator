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
        List<Integer> ageCohorts = new ArrayList<>();
        
        // Initialize with mixed-age population (some adults)
        for (int i = 0; i < initialPopulation; i++) {
            // Random age between 0 and 2*reproduction age
            int randomAge = (int)(Math.random() * speciesProfile.getReproductionAge() * 2);
            ageCohorts.add(randomAge);
        }
        
        int currentPopulation = initialPopulation;
        populationHistory.add(currentPopulation);

        for (int year = 1; year <= simulationYears; year++) {
            // Age all cohorts by 12 months
            for (int i = 0; i < ageCohorts.size(); i++) {
                ageCohorts.set(i, ageCohorts.get(i) + 12);
            }

            // Calculate breeding population
            int breedingFemales = calculateBreedingPopulation(ageCohorts);
            
            // Calculate new offspring
            int newOffspring = calculateNewOffspring(breedingFemales);
            
            // Apply annual mortality
            currentPopulation = applyAnnualMortality(ageCohorts);
            
            // Add new offspring
            for (int i = 0; i < newOffspring; i++) {
                ageCohorts.add(0); // Newborns have age 0
            }
            
            currentPopulation += newOffspring;
            populationHistory.add(currentPopulation);
            
            // Prevent extinction
            if (currentPopulation <= 0) {
                ageCohorts.add(speciesProfile.getReproductionAge()); // Add one mature individual
                currentPopulation = 1;
            }
        }
        
        calculateGrowthRate();
    }

    private int calculateBreedingPopulation(List<Integer> ageCohorts) {
        int breedingFemales = 0;
        for (int age : ageCohorts) {
            if (age >= speciesProfile.getReproductionAge()) {
                breedingFemales++;
            }
        }
        return (int)(breedingFemales * 0.5); // 50% are female
    }

    private int calculateNewOffspring(int breedingFemales) {
        if (breedingFemales <= 0) return 0;
        
        int totalOffspring = 0;
        double breedingProbability = 0.8; // 80% chance to breed
        
        for (int i = 0; i < breedingFemales; i++) {
            if (Math.random() < breedingProbability) {
                // Litter size with variation
                double variation = 0.8 + Math.random() * 0.4;
                totalOffspring += (int)(speciesProfile.getAvgLitterSize() * variation);
            }
        }
        
        return (int)(totalOffspring * speciesProfile.getSurvivalRate());
    }

    private int applyAnnualMortality(List<Integer> ageCohorts) {
        List<Integer> survivors = new ArrayList<>();
        for (int age : ageCohorts) {
            double survivalRate = speciesProfile.getSurvivalRate();
            
            // Age-specific adjustments
            if (age < speciesProfile.getReproductionAge()) {
                survivalRate *= 0.7; // Higher juvenile mortality
            } else if (age > speciesProfile.getReproductionAge() * 3) {
                survivalRate *= 0.6; // Higher elder mortality
            }
            
            if (Math.random() < survivalRate) {
                survivors.add(age);
            }
        }
        
        ageCohorts.clear();
        ageCohorts.addAll(survivors);
        return survivors.size();
    }

    private void calculateGrowthRate() {
        if (populationHistory.size() < 2) {
            averageGrowthRate = 0;
            return;
        }
        
        double totalGrowth = 0;
        int validYears = 0;
        
        for (int i = 1; i < populationHistory.size(); i++) {
            int prev = populationHistory.get(i-1);
            int current = populationHistory.get(i);
            
            if (prev > 0) { // Only calculate if previous population wasn't zero
                double growth = (current - prev) / (double)prev;
                totalGrowth += growth;
                validYears++;
            }
        }
        
        averageGrowthRate = validYears > 0 ? (totalGrowth / validYears) * 100 : 0;
    }

    // Original getters remain unchanged
    public int getInitialPopulation() { return initialPopulation; }
    public int getSimulationYears() { return simulationYears; }
    public SpeciesProfile getSpeciesProfile() { return speciesProfile; }
    public List<Integer> getPopulationHistory() { return populationHistory; }
    public int getFinalPopulation() { 
        return populationHistory.isEmpty() ? 0 : populationHistory.get(populationHistory.size()-1); 
    }
    public double getAverageGrowthRate() { return averageGrowthRate; }
}