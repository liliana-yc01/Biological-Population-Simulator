import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class HTMLChartGenerator {

    private static String currentTheme = "light";

    public static String getCurrentTheme() {
        return currentTheme;
    }

    public static void setTheme(int choice) {
        switch (choice) {
            case 1 -> currentTheme = "light";
            case 2 -> currentTheme = "dark";
            case 3 -> currentTheme = "blue";
        }
    }

// Update buildHTML() to use the theme

    public static void generate(List<Integer> data, String title) {
        if (data == null || data.isEmpty()) {
            System.out.println("No data to generate chart");
            return;
        }

        String filename = title.replaceAll("[^a-zA-Z0-9]", "_") + "_chart.html";
        String html = buildHTML(data, title);

        try {
            Files.writeString(Path.of(filename), html);
            System.out.println("Chart saved as: " + filename);
            System.out.println("Open this file in a web browser to view the chart");
        } catch (IOException e) {
            System.err.println("Error saving chart: " + e.getMessage());
        }
    }

    private static String buildHTML(List<Integer> data, String title) {
        StringBuilder sb = new StringBuilder();
        String bgColor, textColor, chartColor;
    
        switch (currentTheme) {
            case "dark":
                bgColor = "#222";
                textColor = "#eee";
                chartColor = "rgb(100, 200, 100)";
                break;
            case "blue":
                bgColor = "#e6f3ff";
                textColor = "#003366";
                chartColor = "rgb(0, 100, 200)";
                break;
            default: // light
                bgColor = "#fff";
                textColor = "#333";
                chartColor = "rgb(54, 162, 235)";
        }
    
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html>\n<head>\n");
        sb.append("<title>").append(title).append(" Population</title>\n");
        sb.append("<script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script>\n");
        sb.append("<style>body { font-family: Arial, sans-serif; margin: 20px; }</style>\n");
        sb.append("</head>\n<body>\n");
        sb.append("<h1>").append(title).append(" Population Growth</h1>\n");
        sb.append("<div style=\"width: 800px; height: 400px;\">\n");
        sb.append("<canvas id=\"populationChart\"></canvas>\n");
        sb.append("</div>\n");
        sb.append("<script>\n");
        sb.append("const ctx = document.getElementById('populationChart');\n");
        sb.append("new Chart(ctx, {\n");
        sb.append("  type: 'line',\n");
        sb.append("  data: {\n");
        sb.append("    labels: ").append(generateLabels(data.size())).append(",\n");
        sb.append("    datasets: [{\n");
        sb.append("      label: 'Population',\n");
        sb.append("      data: ").append(data.toString()).append(",\n");
        sb.append("      borderColor: 'rgb(54, 162, 235)',\n");
        sb.append("      backgroundColor: 'rgba(54, 162, 235, 0.1)',\n");
        sb.append("      fill: true,\n");
        sb.append("      tension: 0.3\n");
        sb.append("    }]\n");
        sb.append("  },\n");
        sb.append("  options: {\n");
        sb.append("    responsive: true,\n");
        sb.append("    plugins: {\n");
        sb.append("      title: { display: true, text: '").append(title).append(" Population Growth' }\n");
        sb.append("    },\n");
        sb.append("    scales: {\n");
        sb.append("      y: { beginAtZero: true, title: { display: true, text: 'Population' } },\n");
        sb.append("      x: { title: { display: true, text: 'Year' } }\n");
        sb.append("    }\n");
        sb.append("  }\n");
        sb.append("});\n");
        sb.append("</script>\n");
        sb.append("</body>\n</html>");
        
        return sb.toString();
    }

    private static String generateLabels(int size) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(",");
            sb.append("\"").append(i).append("\"");
        }
        sb.append("]");
        return sb.toString();
    }
}