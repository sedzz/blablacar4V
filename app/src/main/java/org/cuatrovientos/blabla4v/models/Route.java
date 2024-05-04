package org.cuatrovientos.blabla4v.models;

import java.util.List;

public class Route {
    private String id;
    private String driver; // the user who creates the route
    private String start; // start location
    private String end; // end location
    private int availableSeats; // available seats
    private List<String> passengers; // users who join the route

    public Route(String id, String driver, String start, String end, int availableSeats, List<String> passengers) {
        this.id = id;
        this.driver = driver;
        this.start = start;
        this.end = end;
        this.availableSeats = availableSeats;
        this.passengers = passengers;
    }

    public Route() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public List<String> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<String> passengers) {
        this.passengers = passengers;
    }
}