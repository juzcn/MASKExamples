/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.beergame;

import mask.executor.Monitor;
import mask.executor.LocalExecutor;
import mask.world.World;
import mask.agent.Agent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 *
 * @author zj
 */
public class BeerGameMonitor extends Monitor {

    private Canvas canvas;

    public BeerGameMonitor() {
    }
    String newLine = System.getProperty("line.separator");

    public static void main(String[] args) {
        Application.launch(args);
    }
    private XYChart<Number, Number> xyChart;

    private TableView<Company> tableView;

    @Override
    protected Tab[] createTabs() {
        Tab[] tabs = new Tab[2];
        tableView = new TableView<>();
        TableColumn<Company, Long> timeCol = new TableColumn<>("Time");
        Callback<CellDataFeatures<Company, Long>, ObservableValue<Long>> timeCellFactory
                = new Callback<CellDataFeatures<Company, Long>, ObservableValue<Long>>() {
            @Override
            public ObservableValue call(CellDataFeatures<Company, Long> cellData) {
                long time = (long) cellData.getValue().getTime();
                return new SimpleLongProperty(time);
            }
        };
        timeCol.setCellValueFactory(timeCellFactory);

        TableColumn<Company, Integer> inventoryCol = new TableColumn<>("Inventory");
        Callback<CellDataFeatures<Company, Integer>, ObservableValue<Integer>> inventoryCellFactory
                = new Callback<CellDataFeatures<Company, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue call(CellDataFeatures<Company, Integer> cellData) {
                int inventory = (Integer) cellData.getValue().getInventory();
                return new SimpleIntegerProperty(inventory);
            }
        };
        inventoryCol.setCellValueFactory(inventoryCellFactory);

        tableView.getColumns().addAll(timeCol, inventoryCol);
        tabs[0] = new Tab("Manufacturer", tableView);
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
        return LocalExecutor.newLocalExecutor(new BeerGameConfig(), this,new BeerGameXSLLogger());
    }

    @Override
    public void process(Agent[] agents) {
        for (Agent a : agents) {
            if (a instanceof Manufacturer) {
                Platform.runLater(() -> {
                    tableView.getItems().add((Company) a);
                    tableView.scrollTo((Company) a);
                });
            }
        }
    }

    @Override
    public void process(World world) {
    }

}
