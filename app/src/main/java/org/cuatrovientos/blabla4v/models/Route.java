package org.cuatrovientos.blabla4v.models;

import java.io.Serializable;
import java.util.List;

public class Route implements Serializable {
    private String id;
    private String driver; // the user who creates the route
    private String start; // start location
    private String end; // end location
    private String startCoordinates;
    private String endCoordinates;
    private int availableSeats; // available seats
    private List<String> passengers; // users who join the route
    private String date; // date of the route
    private String time; // time of the route

    public Route(String id, String driver, String start, String startCoordinates, String end, String endCoordinates, int availableSeats, List<String> passengers, String date, String time) {
        this.id = id;
        this.driver = driver;
        this.start = start;
        this.start = startCoordinates;
        this.end = end;
        this.end = endCoordinates;
        this.availableSeats = availableSeats;
        this.passengers = passengers;
        this.date = date;
        this.time = time;
    }

    public Route() {

    }

    public Route(String startCoordinates, String endCoordinates){
        this.startCoordinates = startCoordinates;
        this.endCoordinates = endCoordinates;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStartCoordinates() {
        return startCoordinates;
    }

    public void setStartCoordinates(String startCoordinates) {
        this.startCoordinates = startCoordinates;
    }

    public String getEndCoordinates() {
        return endCoordinates;
    }

    public void setEndCoordinates(String endCoordinates) {
        this.endCoordinates = endCoordinates;
    }
}