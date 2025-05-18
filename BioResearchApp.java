import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import java.util.List;

public class BioResearchApp extends Application {

    private TextArea output;
    private LineChart<Number, Number> chart;
    private GridPane inputPanel; // Made this a field to access it in clear method

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        inputPanel = createInputPanel(); // Now using the field
        output = new TextArea();
        output.setEditable(false);
        chart = createEmptyChart();

        Button runButton = new Button("Run Simulation");
        runButton.setOnAction(e -> runSimulation(inputPanel));
        
        Button clearButton = new Button("Clear All");
        clearButton.getStyleClass().add("run-button");
        clearButton.setOnAction(e -> clearAll());

        HBox buttonBox = new HBox(45, runButton, clearButton);
        buttonBox.setPadding(new Insets(10, 0, 0, 10));

        VBox leftPanel = new VBox(10, inputPanel, buttonBox);
        leftPanel.setPadding(new Insets(10));

        VBox centerPanel = new VBox(10, chart, output);
        centerPanel.setPadding(new Insets(10));

        root.setLeft(leftPanel);
        root.setCenter(centerPanel);

        Scene scene = new Scene(root, 800, 600);
        
        try {
            scene.getStylesheets().add(getClass().getResource("Theme.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("CSS file not found - using default styling");
        }

        primaryStage.setTitle("Biological Population Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void clearAll() {
        // Clear output and chart
        output.clear();
        chart.getData().clear();
        
        // Clear all text fields
        inputPanel.getChildren().forEach(node -> {
            if (node instanceof TextField) {
                ((TextField) node).clear();
            }
        });
    }

    private GridPane createInputPanel() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        
        // Define all fields in a clean structure
        String[][] fieldDefinitions = {
            {"Species:", "e.g. House Mouse"},
            {"Litter Size:", "e.g. 6"},
            {"Litters/Year:", "e.g. 5"},
            {"Survival Rate:", "0.0-1.0"},
            {"Repro Age (months):", "e.g. 2"},
            {"Initial Population:", "e.g. 10"},
            {"Years to Simulate:", "e.g. 3"}
        };
        
        // Create and add components in a loop
        for (int i = 0; i < fieldDefinitions.length; i++) {
            grid.add(new Label(fieldDefinitions[i][0]), 0, i);
            TextField field = new TextField();
            field.setPromptText(fieldDefinitions[i][1]);
            grid.add(field, 1, i);
        }
        
        return grid;
    }

    private void runSimulation(GridPane inputPanel) {
        try {
            String speciesName = ((TextField)inputPanel.getChildren().get(1)).getText();
            double avgLitterSize = Double.parseDouble(((TextField)inputPanel.getChildren().get(3)).getText());
            double avgLittersPerYear = Double.parseDouble(((TextField)inputPanel.getChildren().get(5)).getText());
            double survivalRate = Double.parseDouble(((TextField)inputPanel.getChildren().get(7)).getText());
            int reproductionAge = Integer.parseInt(((TextField)inputPanel.getChildren().get(9)).getText());
            int initialPopulation = Integer.parseInt(((TextField)inputPanel.getChildren().get(11)).getText());
            int yearsToSimulate = Integer.parseInt(((TextField)inputPanel.getChildren().get(13)).getText());

            SpeciesProfile profile = new SpeciesProfile(speciesName, avgLitterSize, 
                    avgLittersPerYear, survivalRate, reproductionAge);
            PopulationSimulation simulator = new PopulationSimulation(profile, 
                    initialPopulation, yearsToSimulate);
            simulator.run();
            
            displayResults(simulator);
            
        } catch (NumberFormatException e) {
            output.setText("Error: Please enter valid numbers in all fields");
        }
    }

    private void displayResults(PopulationSimulation simulator) {
        StringBuilder results = new StringBuilder();
        results.append("=== Simulation Results ===\n");
        results.append("Species: ").append(simulator.getSpeciesProfile().getName()).append("\n");
        results.append("Final Population: ").append(simulator.getFinalPopulation()).append("\n");
        results.append("Growth Rate: ").append(String.format("%.2f%%", simulator.getAverageGrowthRate())).append("\n");
        
        output.setText(results.toString());
        updateChart(simulator.getPopulationHistory());
    }

    private LineChart<Number, Number> createEmptyChart() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Years");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Population");
        
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Population Growth Over Time");
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        return chart;
    }

    private void updateChart(List<Integer> populationData) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int year = 0; year < populationData.size(); year++) {
            series.getData().add(new XYChart.Data<>(year, populationData.get(year)));
        }
        chart.getData().clear();
        chart.getData().add(series);
    }

    public static void main(String[] args) {
        launch(args);
    }
}