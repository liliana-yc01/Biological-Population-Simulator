import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SimulationManager {
    private static final String SAVE_DIRECTORY = "simulations";
    private final Map<String, PopulationSimulation> savedSimulations = new HashMap<>();

    public SimulationManager() {
        File dir = new File(SAVE_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdir();
        }
        loadSavedSimulations();
    }

    public void saveSimulation(PopulationSimulation simulation, String name) {
        try {
            File file = new File(SAVE_DIRECTORY + File.separator + name.replaceAll("[^a-zA-Z0-9]", "_") + ".sim");
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(simulation);
                savedSimulations.put(name, simulation);
            }
            System.out.println("Simulation saved successfully as: " + name);
        } catch (IOException e) {
            System.err.println("Error saving simulation: " + e.getMessage());
        }
    }

    public PopulationSimulation loadSimulation(String name) {
        return savedSimulations.get(name);
    }

    public void listSavedSimulations() {
        if (savedSimulations.isEmpty()) {
            System.out.println("No saved simulations found");
            return;
        }
        
        System.out.println("\nSaved Simulations:");
        int i = 1;
        for (String name : savedSimulations.keySet()) {
            System.out.printf("%d. %s%n", i++, name);
        }
    }

    private void loadSavedSimulations() {
        File dir = new File(SAVE_DIRECTORY);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".sim"));
        
        if (files != null) {
            for (File file : files) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    PopulationSimulation sim = (PopulationSimulation) ois.readObject();
                    String name = file.getName().replace(".sim", "");
                    savedSimulations.put(name, sim);
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Error loading simulation from " + file.getName() + ": " + e.getMessage());
                }
            }
        }
    }

    public int getSavedCount() {
        return savedSimulations.size();
    }

    public boolean deleteSimulation(String name) {
        File file = new File(SAVE_DIRECTORY + File.separator + name + ".sim");
        if (file.exists()) {
            savedSimulations.remove(name);
            return file.delete();
        }
        return false;
    }
}