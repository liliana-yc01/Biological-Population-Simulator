public class AnimalProfile {
    public String name;
    public double avgLitterSize;
    public double avgLittersPerYear;
    public double survivalRate;
    public int reproductionAge;

    public AnimalProfile(String name, double avgLitterSize, double avgLittersPerYear, double survivalRate, int reproductionAge) {
        this.name = name;
        this.avgLitterSize = avgLitterSize;
        this.avgLittersPerYear = avgLittersPerYear;
        this.survivalRate = survivalRate;
        this.reproductionAge = reproductionAge;
    }

}
