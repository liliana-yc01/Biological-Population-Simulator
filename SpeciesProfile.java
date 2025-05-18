import java.io.Serializable;

public class SpeciesProfile implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final String name;
    private final double avgLitterSize;
    private final double avgLittersPerYear;
    private final double survivalRate;
    private final int reproductionAge; // in months

    public SpeciesProfile(String name, double avgLitterSize, double avgLittersPerYear, 
                        double survivalRate, int reproductionAge) {
        if (survivalRate < 0 || survivalRate > 1) {
            throw new IllegalArgumentException("Survival rate must be between 0 and 1");
        }
        this.name = name;
        this.avgLitterSize = avgLitterSize;
        this.avgLittersPerYear = avgLittersPerYear;
        this.survivalRate = survivalRate;
        this.reproductionAge = reproductionAge;
    }

    // Getters
    public String getName() { return name; }
    public double getAvgLitterSize() { return avgLitterSize; }
    public double getAvgLittersPerYear() { return avgLittersPerYear; }
    public double getSurvivalRate() { return survivalRate; }
    public int getReproductionAge() { return reproductionAge; }

    @Override
    public String toString() {
        return String.format("%s [Litter: %.1f, Litters/Year: %.1f, Survival: %.1f%%, Repro Age: %d months]",
                name, avgLitterSize, avgLittersPerYear, survivalRate * 100, reproductionAge);
    }
}