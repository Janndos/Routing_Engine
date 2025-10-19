module com.example.routingengine {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.gluonhq.maps;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;


    opens com.example.routingengine to javafx.fxml;
    exports com.example.routingengine;
}