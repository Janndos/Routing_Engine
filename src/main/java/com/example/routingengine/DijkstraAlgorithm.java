package com.example.routingengine;

import java.util.*;

public class DijkstraAlgorithm {

    private final Graph graph;
    private Map<Node, Node> previousNodes;
    private Node intialEndNode;

    public DijkstraAlgorithm(Graph graph) {
        this.graph = graph;
    }

    public Map<Node, Double> shortestPath(Node startNode) {
        if (startNode == null) {
            System.err.println("Start node is null!");
            return Collections.emptyMap();
        }

        Map<Node, Double> distances = new HashMap<>();
        previousNodes = new HashMap<>();
        PriorityQueue<NodeDistancePair> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(NodeDistancePair::getDistance));
        Set<Node> visited = new HashSet<>();

        for (Node node : graph.getNodes().values()) {
            distances.put(node, Double.MAX_VALUE);
        }

        distances.put(startNode, 0.0);
        priorityQueue.add(new NodeDistancePair(startNode, 0.0));

        while (!priorityQueue.isEmpty()) {
            NodeDistancePair currentPair = priorityQueue.poll();
            Node currentNode = currentPair.getNode();

            if (currentNode == null) {
                System.err.println("Current node is null!");
                continue;
            }

            if (visited.contains(currentNode)) {
                continue;
            }

            visited.add(currentNode);

            for (Map.Entry<Node, Double> neighborEntry : currentNode.getNeighbors().entrySet()) {
                Node neighbor = neighborEntry.getKey();
                Double edgeWeight = neighborEntry.getValue();

                if (!visited.contains(neighbor)) {
                    double newDist = distances.get(currentNode) + edgeWeight;
                    if (newDist < distances.get(neighbor)) {
                        distances.put(neighbor, newDist);
                        previousNodes.put(neighbor, currentNode);
                        priorityQueue.add(new NodeDistancePair(neighbor, newDist));
                    }
                }
            }
        }


        return distances;
    }

    public List<Node> shortestPath(double startLat, double startLon, double endLat, double endLon) {
        Node startNode = graph.findClosestNode(startLat, startLon);
        boolean isNewStartNode = false;
        if (startNode == null || startNode.distanceTo(new Node(-1, startLat, startLon, 0)) > 50) {
            startNode = graph.addNode(startLat, startLon);
            isNewStartNode = true;
        }
        intialEndNode = new Node(-1, endLat, endLon, 0);
        Node endNode = graph.findClosestNode(endLat, endLon);
        boolean isNewEndNode = false;
        if (endNode == null || endNode.distanceTo(new Node(-1, endLat, endLon, 0)) > 50) {
            endNode = graph.addNode(endLat, endLon);
            isNewEndNode = true;
        }

        Map<Node, Double> distances = shortestPath(startNode);

        if (isNewEndNode) {
            Node closestToEndNode = graph.findClosestNode(endLat, endLon);
            double finalLegDistance = graph.calculateDistance(closestToEndNode, endNode);
            distances.put(endNode, distances.get(closestToEndNode) + finalLegDistance);
            previousNodes.put(endNode, closestToEndNode);
        }

        if (distances.containsKey(endNode)) {
            System.out.println("Distance from start to end: " + distances.get(endNode) + " meters");
            System.out.println(getPath(endNode));
            return getPath(endNode);
        } else {
            System.out.println("End node is unreachable from the start node.");
            return Collections.emptyList();
        }
    }

    private List<Node> getPath(Node endNode) {
        List<Node> path = new ArrayList<>();
        Node currentNode = endNode;

        while (currentNode != null) {
            path.add(currentNode);
            currentNode = previousNodes.get(currentNode);
        }

        Collections.reverse(path);
        path.add(intialEndNode);
        return path;
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
