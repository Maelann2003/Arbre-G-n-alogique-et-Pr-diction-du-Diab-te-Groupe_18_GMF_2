package com.cytech.base;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.ext.JGraphXAdapter;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;

import javax.swing.*;
import java.util.List;

public class GenealogyTree extends JFrame {

    public GenealogyTree(String title, List<Personne> personnes) {
        super(title);
        Graph<String, DefaultEdge> g = createGraph(personnes);
        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(g);

        customizeLayout(graphAdapter);

        mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);
        getContentPane().add(graphComponent);
        setSize(3000, 3000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public Graph<String, DefaultEdge> createGraph(List<Personne> personnes) {
        Graph<String, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);

        for (Personne p : personnes) {
            graph.addVertex(p.getId());
            if (p.getIdPere() != null && !p.getIdPere().isEmpty()) {
                graph.addVertex(p.getIdPere());
                graph.addEdge(p.getIdPere(), p.getId());
            }
            if (p.getIdMere() != null && !p.getIdMere().isEmpty()) {
                graph.addVertex(p.getIdMere());
                graph.addEdge(p.getIdMere(), p.getId());
            }
        }
        return graph;
    }

    private void customizeLayout(JGraphXAdapter<String, DefaultEdge> graphAdapter) {
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graphAdapter);
        layout.setIntraCellSpacing(150);
        layout.setInterHierarchySpacing(600);
        layout.setParallelEdgeSpacing(500);
        layout.execute(graphAdapter.getDefaultParent());
    }
}
