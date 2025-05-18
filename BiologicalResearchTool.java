import java.util.Scanner;

public class BiologicalResearchTool {
    private static final Scanner scanner = new Scanner(System.in);
    private static SimulationManager simulationManager = new SimulationManager();

    public static void main(String[] args) {
        System.out.println("=== Biological Population Simulator ===");
        
        while (true) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ", 1, 5);
            
            switch (choice) {
                case 1 -> runNewSimulation();
                case 2 -> viewSavedSimulations();
                case 3 -> displayHelp();
                case 4 -> configureSettings();
                case 5 -> {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Run New Simulation");
        System.out.println("2. View Saved Simulations");
        System.out.println("3. Help");
        System.out.println("4. Settings");
        System.out.println("5. Exit");
    }

    private static void runNewSimulation() {
        System.out.println("\n=== New Simulation ===");
        
        // Get simulation parameters
        String speciesName = getStringInput("Enter species name: ");
        double avgLitterSize = getDoubleInput("Enter average litter size: ", 0.1, 100);
        double avgLittersPerYear = getDoubleInput("Enter average litters per year: ", 0.1, 50);
        double survivalRate = getDoubleInput("Enter survival rate (0.0-1.0): ", 0, 1);
        int reproductionAge = getIntInput("Enter reproduction age (months): ", 1, 1200);
        int initialPopulation = getIntInput("Enter initial population: ", 1, Integer.MAX_VALUE);
        int simulationYears = getIntInput("Enter years to simulate: ", 1, 1000);
        
        // Create and run simulation
        SpeciesProfile profile = new SpeciesProfile(speciesName, avgLitterSize, avgLittersPerYear, 
                                                 survivalRate, reproductionAge);
        PopulationSimulation simulation = new PopulationSimulation(profile, initialPopulation, simulationYears);
        simulation.run();
        
        // Display results
        displayResults(simulation);
        
        // Save option
        if (getYesNoInput("Save this simulation? (y/n): ")) {
            String saveName = getStringInput("Enter save name: ");
            simulationManager.saveSimulation(simulation, saveName);
        }
    }

    private static void displayResults(PopulationSimulation simulation) {
        System.out.println("\n=== Simulation Results ===");
        System.out.println("Species: " + simulation.getSpeciesProfile().getName());
        System.out.printf("Final Population: %,d%n", simulation.getFinalPopulation());
        System.out.printf("Growth Rate: %.2f%% per year%n", simulation.getAverageGrowthRate());
    
    // Adjust chart width for large populations
        int finalPop = simulation.getFinalPopulation();
        if (finalPop > 10000) {
            ConsoleChart.setDefaultWidth(80);
            ConsoleChart.setDefaultHeight(30);
        }
    
    // Display visualization
        System.out.println("\nSelect visualization option:");
        System.out.println("1. Console Chart");
        System.out.println("2. HTML Chart");
        System.out.println("3. Both");
        System.out.println("4. None");
    
    int vizChoice = getIntInput("Enter choice: ", 1, 4);
    
        switch (vizChoice) {
            case 1 -> ConsoleChart.display(simulation.getPopulationHistory(), 
                                      simulation.getSpeciesProfile().getName());
            case 2 -> HTMLChartGenerator.generate(simulation.getPopulationHistory(), 
                                           simulation.getSpeciesProfile().getName());
            case 3 -> {
                ConsoleChart.display(simulation.getPopulationHistory(), 
                               simulation.getSpeciesProfile().getName());
                HTMLChartGenerator.generate(simulation.getPopulationHistory(), 
                                    simulation.getSpeciesProfile().getName());
            }
        }
    }

    private static void viewSavedSimulations() {
        System.out.println("\n=== Saved Simulations ===");
        simulationManager.listSavedSimulations();
        
        if (simulationManager.getSavedCount() == 0) {
            return;
        }
        
        System.out.println("\nOptions:");
        System.out.println("1. View a simulation");
        System.out.println("2. Delete a simulation");
        System.out.println("3. Back to main menu");
        
        int choice = getIntInput("Enter your choice: ", 1, 3);
        
        switch (choice) {
            case 1 -> viewSingleSimulation();
            case 2 -> deleteSimulation();
            // case 3 returns to main menu
        }
    }

    private static void viewSingleSimulation() {
        String simName = getStringInput("Enter simulation name to view: ");
        PopulationSimulation sim = simulationManager.loadSimulation(simName);
    
        if (sim == null) {
            System.out.println("Simulation not found!");
            return;
        }
    
        System.out.println("\n=== Simulation Details ===");
        System.out.println("Name: " + simName);
        System.out.println(sim.getSpeciesProfile());
        System.out.printf("Initial Population: %,d%n", sim.getInitialPopulation());
        System.out.printf("Simulation Years: %,d%n", sim.getSimulationYears());
        System.out.printf("Final Population: %,d%n", sim.getFinalPopulation());
        System.out.printf("Average Growth Rate: %.2f%% per year%n", sim.getAverageGrowthRate());
    
        // Display visualization options
        System.out.println("\nVisualization Options:");
        System.out.println("1. Console Chart");
        System.out.println("2. HTML Chart");
        System.out.println("3. Both");
        System.out.println("4. Back");
    
        int vizChoice = getIntInput("Enter choice: ", 1, 4);
    
        switch (vizChoice) {
            case 1 -> ConsoleChart.display(sim.getPopulationHistory(), simName);
            case 2 -> HTMLChartGenerator.generate(sim.getPopulationHistory(), simName);
            case 3 -> {
                ConsoleChart.display(sim.getPopulationHistory(), simName);
                HTMLChartGenerator.generate(sim.getPopulationHistory(), simName);
            }   
        }
    }

    private static void deleteSimulation() {
        String simName = getStringInput("Enter simulation name to delete: ");
        if (simulationManager.deleteSimulation(simName)) {
            System.out.println("Simulation deleted successfully");
        } else {
            System.out.println("Failed to delete simulation");
        }
    }

    private static void displayHelp() {
        System.out.println("\n=== Biological Research Tool Help ===");
        System.out.println("\nThis tool simulates population growth based on biological parameters.");
        System.out.println("Key Features:");
        System.out.println("- Create simulations with custom species parameters");
        System.out.println("- View population growth over time");
        System.out.println("- Save and load simulations for later analysis");
        System.out.println("- Generate visualizations (console and HTML charts)");
    
        System.out.println("\nParameter Definitions:");
        System.out.println("1. Average Litter Size: Typical number of offspring per litter");
        System.out.println("2. Litters per Year: How many litters each female produces annually");
        System.out.println("3. Survival Rate: Percentage of offspring that survive to adulthood (0-1)");
        System.out.println("4. Reproduction Age: Age when animals can start reproducing (in months)");
    
        System.out.println("\nNavigation:");
        System.out.println("- Use numbers to select menu options");
        System.out.println("- Follow prompts to enter parameter values");
        System.out.println("- Press Enter after each input");
    
        System.out.println("\nFor more advanced features, see the documentation.");
        getStringInput("\nPress Enter to return to main menu...");
    }

    private static void configureSettings() {
        System.out.println("\n=== Application Settings ===");
        System.out.println("Current Settings:");
        System.out.println("1. Default Chart Width: " + ConsoleChart.getDefaultWidth());
        System.out.println("2. Default Chart Height: " + ConsoleChart.getDefaultHeight());
        System.out.println("3. HTML Chart Theme: " + HTMLChartGenerator.getCurrentTheme());
        System.out.println("4. Back to Main Menu");
    
        int choice = getIntInput("\nSelect setting to change: ", 1, 4);
    
        switch (choice) {
            case 1 -> {
                int width = getIntInput("Enter new chart width (30-100): ", 30, 100);
                ConsoleChart.setDefaultWidth(width);
                System.out.println("Chart width updated");
            }
            case 2 -> {
                int height = getIntInput("Enter new chart height (10-30): ", 10, 30);
                ConsoleChart.setDefaultHeight(height);
                System.out.println("Chart height updated");
            }
            case 3 -> {
                System.out.println("Available Themes:");
                System.out.println("1. Light");
                System.out.println("2. Dark");
                System.out.println("3. Blue");
                int theme = getIntInput("Select theme: ", 1, 3);
                HTMLChartGenerator.setTheme(theme);
                System.out.println("Theme updated");
            }
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int getIntInput(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Please enter a number between %d and %d%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    private static double getDoubleInput(String prompt, double min, double max) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Please enter a number between %.1f and %.1f%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static boolean getYesNoInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            }
            if (input.equals("n") || input.equals("no")) {
                return false;
            }
            System.out.println("Please enter 'y' or 'n'");
        }
    }
}