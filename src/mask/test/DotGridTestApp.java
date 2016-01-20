/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.test;
 
/**
* Test app for DotGrid
*/
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
 
/**
 * Test app for DotGrid
 */
public class DotGridTestApp extends Application {
 
    @Override public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(new DotGrid(), Color.WHITE));
        stage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}