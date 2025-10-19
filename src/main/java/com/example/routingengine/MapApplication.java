package com.example.routingengine;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;

import java.io.IOException;
import java.util.List;

public class MapApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        MapView mapView = new MapView();

        MapPoint mapPoint = new MapPoint(50.8558070805693, 5.69596997874179);

        mapView.setZoom(12.0);
        mapView.setCenter(mapPoint);

        Graph graph = new Graph();
        try {
            graph.loadGraph("C:\\Users\\Eugeniu\\IdeaProjects\\routing-engine\\src\\main\\resources\\com\\example\\routingengine\\maastricht_graph.json"); 
        } catch (IOException e) {
            e.printStackTrace();
        }

        //
        addPathLayer(mapView, graph, 50.853395763055964, 5.7013353459240514, 50.845145698838735, 5.705640567326615, "BLUE");

        BorderPane root = new BorderPane();
        root.setCenter(mapView);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Gluonus");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void addPathLayer(MapView mapView, Graph graph, double startLat, double startLon, double endLat, double endLon, String color) {
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        List<com.example.routingengine.Node> path = dijkstra.shortestPath(startLat, startLon, endLat, endLon);
        PathLayer pathLayer = new PathLayer(mapView, graph, path, color);
        mapView.addLayer(pathLayer);
    }
}

class PathLayer extends MapLayer {
    private final MapView mapView;
    private final Graph graph;
    private final Group pathGroup;
    private final List<com.example.routingengine.Node> path;
    private final Color pathColor;

    public PathLayer(MapView mapView, Graph graph, List<com.example.routingengine.Node> path, String color) {
        this.mapView = mapView;
        this.graph = graph;
        this.pathGroup = new Group();
        this.path = path;
        this.pathColor = Color.valueOf(color.toUpperCase());
        getChildren().add(pathGroup);
    }

    @Override
    protected void layoutLayer() {
        ObservableList<Node> children = pathGroup.getChildren();
        children.clear();

        if (path.isEmpty()) {
            return;
        }

        for (int i = 0; i < path.size() - 1; i++) {
            com.example.routingengine.Node node1 = path.get(i);
            com.example.routingengine.Node node2 = path.get(i + 1);

            Point2D screenPoint1 = getMapPoint(node1.lat, node1.lon);
            Point2D screenPoint2 = getMapPoint(node2.lat, node2.lon);

            double distance = screenPoint1.distance(screenPoint2);
            int numCircles = (int) (distance / 10);

            for (int j = 0; j <= numCircles; j++) {
                double t = (double) j / numCircles;
                double x = screenPoint1.getX() * (1 - t) + screenPoint2.getX() * t;
                double y = screenPoint1.getY() * (1 - t) + screenPoint2.getY() * t;

                Circle outerCircle = new Circle(4, pathColor);
                outerCircle.setCenterX(x);
                outerCircle.setCenterY(y);
                children.add(outerCircle);

                Circle innerCircle = new Circle(2, pathColor.darker());
                innerCircle.setCenterX(x);
                innerCircle.setCenterY(y);
                children.add(innerCircle);
            }
        }
    }

}
