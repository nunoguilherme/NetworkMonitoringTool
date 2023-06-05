package com.github.nunoduarte;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XYSeries;

import java.util.LinkedList;
import java.util.List;

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
        sw.displayChart();
    }

    public void updateChart(long time, double dataPoint) {
        // Add data
        this.times.add(time);
        this.dataPoints.add(dataPoint);

        // Limit the size
        if (this.times.size() > 100) {
            this.times.remove(0);
            this.dataPoints.remove(0);
        }

        chart.updateXYSeries("network traffic", this.times, this.dataPoints, null);
        sw.repaintChart();
    }
}
