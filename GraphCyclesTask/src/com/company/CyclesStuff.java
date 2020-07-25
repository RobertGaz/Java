package com.company;

import java.util.*;

// Реализация алгоритма Джонсона поиска элементарных циклов в ориентированном графе

// Для работы алгоритма требуется реализация алгоритма поиска компонент сильной
// связности (Strongly connected components) - реализован в классе StronglyConnectedComponents

public class CyclesStuff {
    private Stack<Vertex> stack;
    private Set<Vertex> blockedSet;
    private Map<Vertex, Set<Vertex>> blockedMap;
    private List<List<Vertex>> cycles;
    private long totalVerticesAmount;

    //текущий подграф исходного графа
    private Graph currentGraph;

    //подграф графа currentGraph, множество узлов которого - Strongly Connected Component
    private Graph currentSubGraph;


    public List<List<Vertex>> findCycles(Graph graph) {
        stack = new Stack<>();
        blockedSet = new HashSet<>();
        blockedMap = new HashMap<>();
        cycles = new ArrayList<>();
        totalVerticesAmount = graph.getVertices().size();
        long startIndex = 1;

        StronglyConnectedComponents SCC = new StronglyConnectedComponents();

        while (startIndex <= graph.getVertices().size()) {
            currentGraph = getCurrentGraph(startIndex, graph);
            List<Set<Vertex>> components = SCC.getComponents(currentGraph);
            Vertex minVertex = getMinVertex(components);
            if (minVertex != null) {
                blockedSet.clear();
                blockedMap.clear();
                processComponent(minVertex, minVertex);
                startIndex = minVertex.getId() + 1;
            } else {
                break;
            }
        }
        return cycles;
    }

    private Vertex getMinVertex(List<Set<Vertex>> components) {
        long minNum = totalVerticesAmount+1;
        Vertex minVertex = null;
        Set<Vertex> minComponent = null;
        for (Set<Vertex> component : components) {
            if (component.size() > 1) {
                for (Vertex v : component) {
                    if (v.getId() < minNum) {
                        minNum = v.getId();
                        minVertex = v;
                        minComponent = component;
                    }
                }
            }
        }

        if (minVertex == null) {
            return null;
        }

        currentSubGraph = new Graph();

        for (Vertex from : currentGraph.getVertices()) {
            if (minComponent.contains(from)) {
                for (Vertex to : currentGraph.getAdjVertices(from)) {
                    if (minComponent.contains(to)) {
                        currentSubGraph.addEdge(from, to);
                    }
                }
            }
        }

        return minVertex;
    }


    private boolean processComponent(Vertex start, Vertex current) {
        boolean foundCycle = false;
        stack.push(current);
        blockedSet.add(current);

        for (Vertex dest : currentSubGraph.getAdjVertices(current)) {
            if (dest == start) {
                List<Vertex> cycle = new ArrayList<>();
                stack.push(start);
                cycle.addAll(stack);
                stack.pop();
                cycles.add(cycle);
                foundCycle = true;
            } else if (!blockedSet.contains(dest)) {
                boolean moreCycles = processComponent(start, dest);
                foundCycle = foundCycle || moreCycles;
            }
        }

        if (foundCycle) {
            unblock(current);
        } else {
            for (Vertex v : currentSubGraph.getAdjVertices(current)) {
                Set<Vertex> blockedByVertex = blockedMap.computeIfAbsent(v, (key) -> new HashSet<>());
                blockedByVertex.add(current);
            }
        }
        stack.pop();
        return foundCycle;
    }

    private void unblock(Vertex vertex) {
        blockedSet.remove(vertex);
        if (blockedMap.get(vertex) != null) {
            blockedMap.get(vertex).forEach( v -> {
                if(blockedSet.contains(v)) {
                    unblock(v);
                }
            });
            blockedMap.remove(vertex);
        }
    }

    private Graph getCurrentGraph(Long startVertexNum, Graph graph) {
        Graph subGraph = new Graph();
        for (Vertex from : graph.getVertices()) {
            for (Vertex to : graph.getAdjVertices(from)) {
                if (from.getId() >= startVertexNum && from.getId() >= startVertexNum) {
                    subGraph.addEdge(from, to);
                }
            }
        }
        return subGraph;
    }

}
