package com.github.nunoduarte;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.SwingWrapper;


import java.util.LinkedList;
import java.util.List;


import javax.swing.SwingUtilities;

public class TrafficVisualizer {

    private List<Long> times = new LinkedList<>();
    private List<Double> dataPoints = new LinkedList<>();
    private NetworkMonitor networkMonitor;

    private XYChart chart;
    private SwingWrapper<XYChart> sw;

    public TrafficVisualizer(NetworkMonitor networkMonitor) {
    this.networkMonitor = networkMonitor;
    this.chart = new XYChartBuilder().width(600).height(400).title("Network Traffic").xAxisTitle("Time").yAxisTitle("Data Points").build();
    this.sw = new SwingWrapper<>(chart);

    // Create a new Thread to display the chart
    new Thread(() -> sw.displayChart()).start();
    }

    public void updateChart(long duration, boolean status) {
    // Add data
    long currentTime = System.currentTimeMillis();
    this.times.add(currentTime);
    this.dataPoints.add((double) duration);

    // Limit the size
    if (this.times.size() > 100) {
        this.times.remove(0);
        this.dataPoints.remove(0);
    }

    // Update the chart on the EDT
    SwingUtilities.invokeLater(() -> {
        chart.updateXYSeries("network traffic", this.times, this.dataPoints, null);
        sw.repaintChart();
    });
}


    public XYChart getChart() {
    return this.chart;
}

}

