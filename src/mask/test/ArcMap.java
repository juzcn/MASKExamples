/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.test;

import com.esri.map.ArcGISTiledMapServiceLayer;
import com.esri.map.FXMap;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author zj
 */
public class ArcMap extends Application {

    FXMap map;
    ArcGISTiledMapServiceLayer tiledLayer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {

            // Create an instance of the JavaFX Map component
            map = new FXMap();

            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(map);

            Scene scene = new Scene(stackPane);

            primaryStage.setTitle("JavaFX Map");
            primaryStage.setWidth(1000);
            primaryStage.setHeight(700);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Create a tiled map service layer with an ArcGIS Online map service URL
            tiledLayer = new ArcGISTiledMapServiceLayer(
                    "http://services.arcgisonline.com/arcgis/rest/services/NatGeo_World_Map/MapServer");

            // Add the layer to the list of layers in the map
            map.getLayerList().add(tiledLayer);

        } catch (Exception e) {
            // handle exception as desired, minimally report it to console
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
// Launch the JavaFX application
        Application.launch(args);
    }

}
