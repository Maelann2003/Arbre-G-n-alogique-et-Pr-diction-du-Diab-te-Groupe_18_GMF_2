package com.cytech.base;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import smile.clustering.KMeans;
import smile.math.MathEx;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class KMeansChart extends ApplicationFrame {

    private List<Personne> famille;

    public KMeansChart(String title, List<Personne> personnes, KMeans kmeans) {
        super(title);
        this.famille = personnes;
        JFreeChart scatterPlot = createChart1(kmeans);
        ChartPanel chartPanel = new ChartPanel(scatterPlot);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);
    }

   

	public JFreeChart createChart1(KMeans kmeans) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries[] series = new XYSeries[kmeans.k];

        for (int i = 0; i < kmeans.k; i++) {
            series[i] = new XYSeries("Cluster " + i);
            dataset.addSeries(series[i]);
        }

        for (int i = 0; i < famille.size(); i++) {
            Personne p = famille.get(i);
            int cluster = kmeans.y[i];
            series[cluster].add(p.getAgeDiagnostic(), p.isAntecedentsDiabete() ? 1 : 0);
        }

        JFreeChart chart = ChartFactory.createScatterPlot(
                "K-Means Clustering",
                "Age Diagnostic",
                "Antecedents Diabete",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        plot.setRenderer(renderer);

        return chart;
    }

    public static void main(String[] args) throws Exception {
        GestionFamilles gestion = new GestionFamilles();
        List<Personne> personnes = gestion.chargerPersonnes();
        double[][] data = gestion.prepareDataForClustering(personnes);
        
        MathEx.setSeed(19650218);
        KMeans kmeans = KMeans.fit(data, 3);
        KMeansChart chart = new KMeansChart("K-Means Clustering", personnes, kmeans);
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }
}