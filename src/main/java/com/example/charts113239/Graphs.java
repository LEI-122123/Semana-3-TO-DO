package com.example.charts113239;

import jakarta.persistence.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jspecify.annotations.NullMarked;

import javax.swing.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Entity
public class Graphs {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "task_id")
    private Long id;


    private final String type;
    private final String title;

    public Graphs(String type, String title) {
        this.type = type;
        this.title = title;
    }

    public JFreeChart getLineGraph() {
        LocalDate today = LocalDate.now();
        List<YearMonth> months = new ArrayList<>();
        for (int i = 12; i >= 0; i--) {
            months.add(YearMonth.from(today.minusMonths(i)));
        }

        // Simulate raw grades assigned to random months
        Map<YearMonth, List<Double>> bucket = new HashMap<>();
        months.forEach(m -> bucket.put(m, new ArrayList<>()));

        int N = 120;
        for (int i = 0; i < N; i++) {
            YearMonth m = months.get(ThreadLocalRandom.current().nextInt(months.size()));
            double grade = Math.round(ThreadLocalRandom.current().nextDouble(0, 20) * 10.0) / 10.0;
            bucket.get(m).add(grade);
        }

        // Build CategoryDataset with monthly averages
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String seriesKey = "Avg Grade";
        for (YearMonth m : months) {
            List<Double> g = bucket.get(m);
            double avg = g.isEmpty() ? 0 : g.stream().mapToDouble(x -> x).average().orElse(0);
            dataset.addValue(avg, seriesKey, m.toString()); // e.g., "2024-09"
        }

        // Line chart (or use createBarChart for bars)

        return ChartFactory.createLineChart(
                this.title,
                "Month",
                "Grade (0–20)",
                dataset
        );
    }

    public JFreeChart getPieChart() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        // Add random slices
        for (int i = 1; i <= 5; i++) {
            dataset.setValue("Category " + i, ThreadLocalRandom.current().nextInt(1, 100));
        }


        return ChartFactory.createPieChart(
                this.title,
                dataset,
                true,
                true,
                false
        );
    }

    private JFreeChart getBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 1; i <= 5; i++) {
            dataset.addValue(ThreadLocalRandom.current().nextInt(0, 100), "Series 1", "Category " + i);
        }

        return ChartFactory.createBarChart(
                this.title,
                "Category",
                "Value",
                dataset
        );
    }

    public JFreeChart getGraph() {
        return switch (this.type) {
            case "line" -> getLineGraph();
            case "pie" -> getPieChart();
            case "bar" -> getBarChart();
            default -> getLineGraph();
        };
    }

}
