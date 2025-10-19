package com.example.routingengine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Graph {
    private Map<Integer, Node> nodes = new HashMap<>();
    private Map<String, Edge> edges = new HashMap<>();
    private int nextNodeId = 0;

    public void loadGraph(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(filePath));

        ArrayNode nodesArray = (ArrayNode) rootNode.get("nodes");
        ArrayNode edgesArray = (ArrayNode) rootNode.get("links");

        for (JsonNode node : nodesArray) {
            int id = node.get("id").asInt();
            double lat = node.get("y").asDouble();
            double lon = node.get("x").asDouble();
            int streetCount = node.get("street_count").asInt();
            Node newNode = new Node(id, lat, lon, streetCount);
            nodes.put(id, newNode);
            System.out.println("Loaded node: " + newNode);
            if (id >= nextNodeId) {
                nextNodeId = id + 1;
            }
        }

        for (JsonNode edge : edgesArray) {
            int sourceId = edge.get("source").asInt();
            int targetId = edge.get("target").asInt();
            double weight = calculateDistance(nodes.get(sourceId), nodes.get(targetId));
            Edge edgeObject = new Edge(nodes.get(sourceId), nodes.get(targetId), weight);
            edges.put(sourceId + "-" + targetId, edgeObject);
            nodes.get(sourceId).addNeighbor(nodes.get(targetId), weight);
            nodes.get(targetId).addNeighbor(nodes.get(sourceId), weight);
        }
    }

    public Map<Integer, Node> getNodes() {
        return nodes;
    }

    public Map<String, Edge> getEdges() {
        return edges;
    }

    public Node findClosestNode(double lat, double lon) {
        Node closestNode = null;
        double minDistance = Double.MAX_VALUE;
        Node tempNode = new Node(-1, lat, lon, 0);
        for (Node node : nodes.values()) {
            double distance = node.distanceTo(tempNode);
            if (distance < minDistance) {
                minDistance = distance;
                closestNode = node;
            }
        }
        return closestNode;
    }

    public List<Node> findTwoClosestNodes(double lat, double lon) {
        PriorityQueue<NodeDistancePair> pq = new PriorityQueue<>(Comparator.comparingDouble(NodeDistancePair::getDistance));
        Node tempNode = new Node(-1, lat, lon, 0);

        for (Node node : nodes.values()) {
            double distance = node.distanceTo(tempNode);
            pq.add(new NodeDistancePair(node, distance));
        }

        List<Node> closestNodes = new ArrayList<>();
        if (!pq.isEmpty()) {
            closestNodes.add(pq.poll().getNode());
        }
        if (!pq.isEmpty()) {
            closestNodes.add(pq.poll().getNode());
        }

        return closestNodes;
    }

    public Node findNode(double lat, double lon) {
        for (Node node : nodes.values()) {
            if (node.lat == lat && node.lon == lon) {
                return node;
            }
        }
        return null;
    }

    public Node addNode(double lat, double lon) {
        Node newNode = new Node(nextNodeId++, lat, lon, 0);
        nodes.put(newNode.id, newNode);

        List<Node> closestNodes = findTwoClosestNodes(lat, lon);
        for (Node closestNode : closestNodes) {
            if (closestNode != null) {
                double distance = calculateDistance(newNode, closestNode);
                newNode.addNeighbor(closestNode, distance);
                closestNode.addNeighbor(newNode, distance);
                edges.put(newNode.id + "-" + closestNode.id, new Edge(newNode, closestNode, distance));
                edges.put(closestNode.id + "-" + newNode.id, new Edge(closestNode, newNode, distance));
            }
        }

        return newNode;
    }

    public double calculateDistance(Node source, Node target) {
        final int R = 6371;
        double latDistance = Math.toRadians(target.lat - source.lat);
        double lonDistance = Math.toRadians(target.lon - source.lon);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(source.lat)) * Math.cos(Math.toRadians(target.lat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000;
        return distance;
    }

    private static class NodeDistancePair {
        private final Node node;
        private final double distance;

        public NodeDistancePair(Node node, double distance) {
            this.node = node;
            this.distance = distance;
        }

        public Node getNode() {
            return node;
        }

        public double getDistance() {
            return distance;
        }
    }
}
