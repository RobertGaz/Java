package com.company;

//Исходя из примера предполагаем, что граф в задаче ориентированный

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Main {

    public static void main(String[] args) {
        String data = "";
        try {
            data = new String(Files.readAllBytes(Paths.get("C:in.txt")));
        } catch (IOException exc) {
            System.out.println("Ошибка чтения файла");
            return;
        }

        // Извлечение данных о рёбрах графа
        // (Для решения поставленной задаче информация о расположении вершин на плоскости не требуется)
        Graph graph = retrieveEdgesData(data);


        CyclesStuff stuff = new CyclesStuff();
        List<List<Vertex>> cycles = stuff.findCycles(graph);

        System.out.println(cycles.size());
        cycles.forEach(cycle -> {
            StringJoiner joiner = new StringJoiner("-");
            cycle.forEach(vertex -> joiner.add(String.valueOf(vertex.getId())));
            System.out.println(joiner);
        });
    }

    public static Graph retrieveEdgesData(String data) {
        Pattern p = Pattern.compile("\\[([^;])*\\]");
        Matcher m = p.matcher(data);

        Graph graph = new Graph();

        if (m.find()) {
            String edgesStr = m.group(0);
            edgesStr = edgesStr.replaceAll("\\[\\s*\\(", "").replaceAll("\\)\\s*\\]", "");
            for (String edgeStr : edgesStr.split("\\)\\s*,\\s*\\(" )) {
                String nums[] = edgeStr.split("\\s*,\\s*");
                graph.addEdge(Long.valueOf(nums[0]).longValue(), Long.valueOf(nums[1]).longValue());
            }
        }

        return graph;
    }

}
