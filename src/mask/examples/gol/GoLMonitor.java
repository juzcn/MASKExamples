/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.gol;

import mask.executor.Monitor;
import mask.executor.LocalExecutor;
import mask.world.World;
import mask.agent.Agent;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 *
 * @author zj
 */
public class GoLMonitor extends Monitor {

    private Canvas canvas;

    public GoLMonitor() {
        tickMills = 500;
        nTicks = 120;
    }
    String newLine = System.getProperty("line.separator");

    private long time = -1;
    private int lives = 0;

    public static void main(String[] args) {
// Launch the JavaFX application
        Application.launch(args);
    }
    private XYChart<Number, Number> xyChart;

    @Override
    protected Tab[] createTabs() {
        Tab[] tabs = new Tab[2];
        canvas = new Canvas(1000, 1000);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(canvas);
        ScrollPane pane = new ScrollPane(stackPane);
        pane.setPrefSize(800, 600);
        stackPane.setStyle("-fx-background-color: grey");
        tabs[0] = new Tab("Schelling", pane);
        tabs[0].setClosable(false);
        NumberAxis timeAxis = new NumberAxis();
        timeAxis.setLabel("Time");
        NumberAxis numberAxis = new NumberAxis();
        timeAxis.setLabel("Number of lived cells");

        xyChart = new ScatterChart(timeAxis, numberAxis);
        dataSeries = new XYChart.Series<>();
        dataSeries.setName("Evolution of lived cells in Game of Life");
        xyChart.getData().add(dataSeries);
        tabs[1] = new Tab("Chart", xyChart);
        tabs[1].setClosable(false);

        return tabs;
    }
    private XYChart.Series<Number, Number> dataSeries;

    @Override
    protected LocalExecutor newExecutor() {
        return LocalExecutor.newLocalExecutor(new GoLConfig(), this);
    }

    @Override
    public void logAgents(Agent[] agents) {

        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (Agent lr : agents) {
            long newTime = ((CellAgent) lr).getTime();
            if (newTime != time) {
                time = newTime;
                lives = 0;
            }
            if (((CellAgent) lr).getAlive()) {
                lives++;
            }
            if (((CellAgent) lr).isChanged()) {
                int posY = ((CellAgent) lr).getRow() * 10;
                int posX = ((CellAgent) lr).getColumn() * 10;
                Color color = ((CellAgent) lr).getAlive() ? Color.BLUE : Color.GREY;
                Platform.runLater(() -> {
                    gc.setFill(color);
                    gc.fillRect(posX, posY, 10, 10);
                });
            }
        }
        Platform.runLater(() -> {
            dataSeries.getData().add(new Data(time, lives));
        });

    }

    @Override
    public void logWorld(World world) {
    }

}
