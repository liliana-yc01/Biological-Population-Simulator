import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("Welcome to the Biological Research Tool");

        System.out.print("Enter species (e.g., Mouse, Rabbit, Guinea Pig): ");
        String name = in.nextLine();

        System.out.print("Enter average litter size: ");
        double avgLitterSize = in.nextDouble();

        System.out.print("Enter average litters per year: ");
        double avgLittersPerYear = in.nextDouble();

        System.out.print("Enter survival rate (0.0 to 1.0): ");
        double survivalRate = in.nextDouble();

        System.out.print("Enter reproduction age (in years): ");
        int reproductionAge = in.nextInt();

        System.out.print("Enter initial number of animals: ");
        int initialFemales = in.nextInt();

        System.out.print("Enter number of years to simulate: ");
        int years = in.nextInt();

        AnimalProfile profile = new AnimalProfile(name, avgLitterSize, avgLittersPerYear, survivalRate, reproductionAge);
        PopulationSimulator simulator = new PopulationSimulator(profile, initialFemales, years);

        simulator.runSimulation();
        in.close();
    }
}
