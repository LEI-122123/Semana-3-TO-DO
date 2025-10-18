package com.example.charts113239.ui;

import com.example.charts113239.Graphs;
import com.example.charts113239.GraphsService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Route("graphs")
@PageTitle("Graphs")
@Menu(order = 1, icon = "vaadin:clipboard-check", title = "Graphs")
class GraphsView extends Main {

    final Button lineGraphButton;
    final Button pieGraphButton;
    final Button barGraphButton;
    private final Image lineChartImage = new Image();
    private final Image pieChartImage = new Image();
    private final Image barChartImage = new Image();

    public GraphsView(GraphsService graphsService) {

        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();
        layout.setDefaultHorizontalComponentAlignment(VerticalLayout.Alignment.CENTER); // center everything


        lineGraphButton = new Button("Generate Line Graph", event -> createLineGraph());
        pieGraphButton = new Button("Generate Pie Graph", event -> createPieGraph());
        barGraphButton = new Button("Generate Bar Graph", event -> createBarGraph());


        layout.add(lineChartImage,lineGraphButton,
                pieChartImage,pieGraphButton,
                barChartImage,barGraphButton);

        // spacing
        layout.setSpacing(true);
        layout.setPadding(true);

        add(layout);
    }

    public void createLineGraph() {
        JFreeChart lineGraph = new Graphs("line", "Line graph").getGraph();

        byte[] imageBytes;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(out, lineGraph, 600, 400);
            imageBytes = out.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        // 4️⃣ Display it as an <img> using InputStreamFactory
        var resource = new com.vaadin.flow.server.InputStreamFactory() {
            @Override
            public ByteArrayInputStream createInputStream() {
                return new ByteArrayInputStream(imageBytes);
            }
        };

        var imageSrc = new com.vaadin.flow.server.StreamResource("lineChart.png", resource);

        lineChartImage.setSrc(imageSrc);
    }

    public void createPieGraph() {
        JFreeChart lineGraph = new Graphs("pie", "Pie graph").getGraph();

        byte[] imageBytes;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(out, lineGraph, 600, 400);
            imageBytes = out.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        // 4️⃣ Display it as an <img> using InputStreamFactory
        var resource = new com.vaadin.flow.server.InputStreamFactory() {
            @Override
            public ByteArrayInputStream createInputStream() {
                return new ByteArrayInputStream(imageBytes);
            }
        };

        var imageSrc = new com.vaadin.flow.server.StreamResource("pieChart.png", resource);

        pieChartImage.setSrc(imageSrc);
    }

    public void createBarGraph() {
        JFreeChart lineGraph = new Graphs("bar", "Bar graph").getGraph();

        byte[] imageBytes;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(out, lineGraph, 600, 400);
            imageBytes = out.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        // 4️⃣ Display it as an <img> using InputStreamFactory
        var resource = new com.vaadin.flow.server.InputStreamFactory() {
            @Override
            public ByteArrayInputStream createInputStream() {
                return new ByteArrayInputStream(imageBytes);
            }
        };

        var imageSrc = new com.vaadin.flow.server.StreamResource("barChart.png", resource);

        barChartImage.setSrc(imageSrc);
    }


}
