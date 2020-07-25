package com.company;

import java.util.*;

// Реализация алгоритма Косарайю

// Данный алгоритм используется для поиска компонент сильной
// связности (Strongly connected components) в ориентированном графе

public class StronglyConnectedComponents {
    Set<Vertex> visited;
    Stack<Vertex> stack;
    Graph graph;
    Graph reversedGraph;

    public StronglyConnectedComponents() {
        visited = new HashSet<>();
        stack = new Stack<>();
    }

    public List<Set<Vertex>> getComponents(Graph graph) {
        visited.clear();
        stack.clear();
        this.graph = graph;
        reversedGraph = reverseGraph(this.graph);

        // первый проход по графу
        for (Vertex v : graph.getVertices()) {
            if (!visited.contains(v)) {
                DFS(v);
            }
        }

        visited.clear();

        List<Set<Vertex>> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            Vertex vertex = stack.pop();
            if (!visited.contains(vertex)) {
                Set<Vertex> component = new HashSet<>();
                reverseDFS(vertex, component);
                result.add(component);
            }
        }
        return result;
    }

    private void DFS(Vertex vertex) {
        visited.add(vertex);
        for (Vertex v : graph.getAdjVertices(vertex)) {
            if (!visited.contains(v)) {
                DFS(v);
            }
        }
        stack.push(vertex);
    }

    private void reverseDFS(Vertex vertex, Set<Vertex> currentComponent) {
        visited.add(vertex);
        currentComponent.add(vertex);

        for (Vertex v : reversedGraph.getAdjVertices(vertex)) {
            if (!visited.contains(v)) {
                reverseDFS(v, currentComponent);
            }
        }
    }

    private Graph reverseGraph(Graph graph) {
        Graph result = new Graph();
        for (Vertex from : graph.getVertices()) {
            for (Vertex to : graph.getAdjVertices(from)) {
                result.addEdge(to, from);
            }
        }

        return result;
    }


}
