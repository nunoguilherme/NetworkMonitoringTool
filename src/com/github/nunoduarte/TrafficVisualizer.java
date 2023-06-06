package com.github.nunoduarte;

import java.util.LinkedList;
import java.util.List;

import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;


public class TrafficVisualizer {
    private final NetworkMonitor networkMonitor;
    private XYChart chart;
    private SwingWrapper<XYChart> sw;
    private JPanel chartPanel;

    private double[] convertToDoubleArray(List<Double> list) {
        if (list != null && !list.isEmpty()) {
            return list.stream().mapToDouble(Double::doubleValue).toArray();
        } else {
            return new double[] {0};  // default data
        }
    }

    public TrafficVisualizer(NetworkMonitor networkMonitor) {
    this.networkMonitor = networkMonitor;

    // Create Chart
    chart = new XYChartBuilder().width(800).height(600).title("Network Traffic").xAxisTitle("Time").yAxisTitle("Data").build();

    // Customize Chart
    chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
    chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
    chart.getStyler().setPlotMargin(0);
    chart.getStyler().setPlotContentSize(.95);

    // Convert List<Double> to double[]
    List<Double> networkDataList = networkMonitor.getTrafficData();
    double[] trafficData;
    if (networkDataList != null && !networkDataList.isEmpty()) {
        trafficData = networkDataList.stream().mapToDouble(Double::doubleValue).toArray();
    } else {
        // initialize with default data if trafficData is null or empty
        trafficData = new double[] {0};
    }

    chart.addSeries("network traffic", null, trafficData);

    sw = new SwingWrapper<>(chart);
    JFrame window = sw.displayChart();
    chartPanel = (JPanel) window.getContentPane().getComponent(0);
}


    public void displayChart() {
        sw.displayChart();
    }

    public JPanel getChartPanel() {
        return chartPanel;
    }

 public void updateData() {
    SwingUtilities.invokeLater(() -> {
        // Convert List<Double> to double[]
        List<Double> networkDataList = networkMonitor.getTrafficData();
        double[] trafficData;
        if (networkDataList != null && !networkDataList.isEmpty()) {
            trafficData = convertToDoubleArray(networkMonitor.getTrafficData());
        } else {
            // initialize with default data if trafficData is null or empty
            trafficData = new double[] {0};
        }
        
        // Get the series by its name. If it doesn't exist, create a new series.
        XYSeries series = chart.getSeriesMap().get("network traffic");
        if (series == null) {
            chart.addSeries("network traffic", null, trafficData);
        } else {
            chart.updateXYSeries("network traffic", null, trafficData, null);
        }
        sw.repaintChart();
    });
}

}




