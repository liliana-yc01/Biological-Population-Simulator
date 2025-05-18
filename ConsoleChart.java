import java.util.List;

public class ConsoleChart {
    private static int defaultWidth = 60;
    private static int defaultHeight = 20;

    public static void display(List<Integer> data, String title) {
        if (data == null || data.isEmpty()) {
            System.out.println("No data to display");
            return;
        }

        // Calculate scaling factors
        int maxValue = data.stream().max(Integer::compare).orElse(1);
        double scale = (double) maxValue / defaultHeight;
        
        System.out.println("\n" + title + " Population Growth");
        System.out.println("Max Population: " + maxValue);
        
        // Print Y-axis scale
        for (int y = defaultHeight; y >= 0; y--) {
            int threshold = (int)(y * scale);
            System.out.printf("%6d │ ", threshold);
            
            // Calculate how much of the bar to show
            double valuePercentage = (double)data.get(Math.min(data.size()-1, defaultWidth-1)) / maxValue;
            int barWidth = (int)(defaultWidth * valuePercentage);
            
            // Ensure we don't try to print negative width
            barWidth = Math.max(0, Math.min(barWidth, defaultWidth));
            System.out.println("█".repeat(barWidth));
        }
        
        // Print X-axis
        System.out.println("       └" + "─".repeat(defaultWidth));
        System.out.printf("%8d%"+ (defaultWidth-1) +"d%n", 0, data.size()-1);
    }

    public static int getDefaultWidth() {
        return defaultWidth;
    }

    public static void setDefaultWidth(int width) {
        defaultWidth = width;
    }

    public static int getDefaultHeight() {
        return defaultHeight;
    }

    public static void setDefaultHeight(int height) {
        defaultHeight = height;
    }
}