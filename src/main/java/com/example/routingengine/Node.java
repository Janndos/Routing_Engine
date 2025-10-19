package com.example.routingengine;

import java.util.HashMap;
import java.util.Map;

public class Node {
    int id;
    double lat;
    double lon;
    int streetCount;
    private Map<Node, Double> neighbors;

    public Node(int id, double lat, double lon, int streetCount) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.streetCount = streetCount;
        this.neighbors = new HashMap<>();
    }

    public void addNeighbor(Node neighbor, double weight) {
        neighbors.put(neighbor, weight);
    }

    public Map<Node, Double> getNeighbors() {
        return neighbors;
    }

    public double distanceTo(Node other) {
        final int R = 6371;
        double latDistance = Math.toRadians(other.lat - this.lat);
        double lonDistance = Math.toRadians(other.lon - this.lon);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.lat)) * Math.cos(Math.toRadians(other.lat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000;
        return distance;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", lat=" + lat +
                ", lon=" + lon +
                ", streetCount=" + streetCount +
                '}';
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return id == node.id;
    }
}
