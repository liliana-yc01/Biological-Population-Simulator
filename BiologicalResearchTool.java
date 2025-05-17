import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class BiologicalResearchTool {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Biological Research Tool ===");

        System.out.print("Enter animal name: ");
        String animalName = scanner.nextLine();

        System.out.print("Enter average litters per year: ");
        double littersPerYear = scanner.nextDouble();

        System.out.print("Enter average litter size: ");
        double litterSize = scanner.nextDouble();

        System.out.print("Enter survival rate (0.0 - 1.0): ");
        double survivalRate = scanner.nextDouble();

        System.out.print("Enter reproduction age (years): ");
        int reproductionAge = scanner.nextInt();

        System.out.print("Enter initial population size: ");
        int initialPopulation = scanner.nextInt();

        System.out.print("Enter number of years to simulate: ");
        int years = scanner.nextInt();

        AnimalProfile profile = new AnimalProfile(animalName, litterSize, littersPerYear, survivalRate, reproductionAge);
        PopulationSimulator simulator = new PopulationSimulator(profile, initialPopulation, years);

        System.out.println("\nRunning simulation...\n");
        simulator.runSimulation();

        System.out.print("\nExport results to CSV? (yes/no): ");
        String exportChoice = scanner.next();

        if (exportChoice.equalsIgnoreCase("yes")) {
            System.out.print("Enter filename (e.g., population.csv): ");
            String filename = scanner.next();
            boolean success = exportToCSV(simulator.getPopulationByYear(), filename);
            if (success) {
                System.out.println("Exported successfully to " + filename);
            } else {
                System.out.println("Export failed.");
            }
        }

        scanner.close();
    }

    private static boolean exportToCSV(List<Integer> populationData, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Year,TotalPopulation\n");
            for (int i = 0; i < populationData.size(); i++) {
                writer.write(i + "," + populationData.get(i) + "\n");
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
