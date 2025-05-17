import java.util.ArrayList;
import java.util.List;

public class PopulationSimulator {
    private AnimalProfile profile;
    private int initialPopulation; // total animals initially
    private int years;

    // Track total population at each year
    private List<Integer> populationByYear = new ArrayList<>();

    public PopulationSimulator(AnimalProfile profile, int initialPopulation, int years) {
        this.profile = profile;
        this.initialPopulation = initialPopulation;
        this.years = years;

        populationByYear.add(initialPopulation);
    }

    public void runSimulation() {
        for (int year = 1; year <= years; year++) {
            int breedingPopulation = getBreedingPopulation(year);

            // total offspring = breedingPopulation * litters/year * litter size * survival rate
            int newOffspring = (int) Math.round(
                breedingPopulation * profile.avgLittersPerYear * profile.avgLitterSize * profile.survivalRate
            );

            // Add new offspring to population
            populationByYear.add(newOffspring);

            int totalPopulation = getTotalPopulation();

            System.out.println("Year " + year + ": Breeding animals = " + breedingPopulation +
                    ", New offspring = " + newOffspring + ", Total population = " + totalPopulation);
        }
    }

    // Breeding population: sum of animals that have reached reproduction age
    private int getBreedingPopulation(int currentYear) {
        int total = 0;
        for (int i = 0; i < currentYear - profile.reproductionAge + 1; i++) {
            if (i >= 0 && i < populationByYear.size()) {
                total += populationByYear.get(i);
            }
        }
        return total;
    }

    // Sum total population over all years so far
    private int getTotalPopulation() {
        return populationByYear.stream().mapToInt(i -> i).sum();
    }

    // Optionally, provide population data to other parts of the app
    public List<Integer> getPopulationByYear() {
        return populationByYear;
    }
}


