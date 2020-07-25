package com.company;

import java.util.*;

// Класс Graph представляет ориентированный граф

public class Graph {

    private Map<Long, Vertex> vertices;
    private Map<Long, Set<Long>> edges;

    public Graph() {
        vertices = new HashMap<>();
        edges = new HashMap<>();
    }

    public void addVertex(Vertex v) {
        vertices.putIfAbsent(v.getId(), v);
        edges.putIfAbsent(v.getId(), new HashSet<>());
    }

    public void addVertex(Long id) {
        vertices.putIfAbsent(id, new Vertex(id));
        edges.putIfAbsent(id, new HashSet<>());
    }

    public Set<Vertex> getVertices() {
        return new HashSet<>(vertices.values());
    }

    public void addEdge(Vertex from, Vertex to) {

        if (!vertices.containsKey(from.getId())) {
            addVertex(from);
        }
        if (!vertices.containsKey(to.getId())) {
            addVertex(to);
        }
        edges.get(from.getId()).add(to.getId());
    }

    public void addEdge(Long fromId, Long toId) {
        if (!vertices.containsKey(fromId)) {
            addVertex(fromId);
        }
        if (!vertices.containsKey(toId)) {
            addVertex(toId);
        }
        edges.get(fromId).add(toId);
    }

    public void addEdge(Integer fromId, Integer toId) {
        Long a = Integer.toUnsignedLong(fromId);
        Long b = Integer.toUnsignedLong(toId);

        if (!vertices.containsKey(a)) {
            addVertex(a);
        }
        if (!vertices.containsKey(b)) {
            addVertex(b);
        }
        edges.get(a).add(b);
    }

    // Метод получения смежных вершин графа для вершины from
    public Set<Vertex> getAdjVertices(Vertex from) {
        Set<Vertex> adjVertices = new HashSet<>();
        edges.get(from.getId()).forEach(id -> adjVertices.add(vertices.get(id)));
        return adjVertices;
    }
}



