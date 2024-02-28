package org.cuatrovientos.blabla4v.models;

import java.util.List;

public class Site {
    private String nombre;
    private List<String> conductores;

    public Site(String nombre, List<String> conductores) {
        this.nombre = nombre;
        this.conductores = conductores;
    }

    public String getNombre() {
        return nombre;
    }

    public List<String> getConductores() {
        return conductores;
    }
}
