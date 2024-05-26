package com.cytech.testUnitaires;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.RefineryUtilities;

import com.cytech.base.GestionFamilles;
import com.cytech.base.KMeansChart;
import com.cytech.base.Personne;

import smile.clustering.KMeans;
import smile.math.MathEx;

import javax.swing.*;
import java.util.List;

public class TestKMeansChart {

    public static void main(String[] args) throws Exception {
        TestKMeansChart test = new TestKMeansChart();
        test.testKMeansChartCreation();
    }

    void testKMeansChartCreation() {
        System.out.println("Running testKMeansChartCreation...");

        try {
            GestionFamilles gestion = new GestionFamilles();
            List<Personne> personnes = gestion.chargerPersonnes();
            double[][] data = gestion.prepareDataForClustering(personnes);

            MathEx.setSeed(19650218);
            KMeans kmeans = KMeans.fit(data, 3);
            KMeansChart chart = new KMeansChart("K-Means Clustering", personnes, kmeans);
            chart.pack();
            RefineryUtilities.centerFrameOnScreen(chart);
            chart.setVisible(true);

            JFreeChart scatterPlot = chart.createChart1(kmeans);
            if (scatterPlot != null && scatterPlot.getPlot() instanceof XYPlot) {
                System.out.println("testKMeansChartCreation passed.");
            } else {
                System.out.println("testKMeansChartCreation failed.");
            }

            SwingUtilities.invokeLater(() -> chart.setVisible(false));

        } catch (Exception e) {
            System.out.println("testKMeansChartCreation failed with exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
