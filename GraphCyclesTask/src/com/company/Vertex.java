package com.company;

// Класс Vertex представляет вершину графа

// В данной задаче нет необходимости обрабатывать и хранить данные о
// расположении точек на плоскости, однако при необходимости данный функционал можно
// реализовать путём изменения класса Vertex

public class Vertex {
    long id;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    Vertex(long id) {
        this.id = id;
    }


}