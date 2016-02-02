/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.scm;

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
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 *
 * @author zj
 */
public class SCMMonitor extends Monitor {

    private Canvas canvas;

    public SCMMonitor() {
    }
    String newLine = System.getProperty("line.separator");

    public static void main(String[] args) {
        Application.launch(args);
    }
    private XYChart<Number, Number> xyChart;

    private TableView<Company> retailerTableView;
    private TableView<Company> wholesalerTableView;
    private TableView<Company> manufacturerTableView;

    protected TableView<Company> createTableView() {
        TableView<Company> tableView = new TableView();
        TableColumn<Company, Long> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<Company, Integer> inventoryCol = new TableColumn<>("Inventory");
        inventoryCol.setCellValueFactory(new PropertyValueFactory<>("inventory"));

        TableColumn<Company, Integer> replenishmentCol = new TableColumn<>("Replenishment");
        replenishmentCol.setCellValueFactory(new PropertyValueFactory<>("replenishments"));

        TableColumn<Company, Integer> receivedCol = new TableColumn<>("Received");
        receivedCol.setCellValueFactory(new PropertyValueFactory<>("received"));

        TableColumn<Company, Integer> orderedCol = new TableColumn<>("Ordered");
        orderedCol.setCellValueFactory(new PropertyValueFactory<>("ordered"));

        TableColumn<Company, Integer> stockoutCol = new TableColumn<>("Stockout");
        stockoutCol.setCellValueFactory(new PropertyValueFactory<>("stockout"));

        tableView.getColumns().addAll(timeCol, inventoryCol, replenishmentCol, receivedCol, orderedCol, stockoutCol);
        return tableView;
    }

    @Override
    protected Tab[] createTabs() {
        Tab[] tabs = new Tab[3];
        retailerTableView = createTableView();
        wholesalerTableView = createTableView();
        manufacturerTableView = createTableView();
        tabs[0] = new Tab("Retailer", retailerTableView);
        tabs[0].setClosable(false);
        tabs[1] = new Tab("Wholesaler", wholesalerTableView);
        tabs[1].setClosable(false);
        tabs[2] = new Tab("Manufacturer", manufacturerTableView);
        tabs[2].setClosable(false);
        return tabs;
    }
    private XYChart.Series<Number, Number> dataSeries;

    @Override
    protected LocalExecutor newExecutor() {
        return LocalExecutor.newLocalExecutor(new SupplyChain(), this);
    }

    @Override
    public void agents(Agent[] agents) {
        for (Agent a : agents) {
            if (a instanceof Retailer) {
                Platform.runLater(() -> {
                    retailerTableView.getItems().add((Company) a);
                    retailerTableView.scrollTo((Company) a);
                });
            } else if (a instanceof Wholesaler) {
                Platform.runLater(() -> {
                    wholesalerTableView.getItems().add((Company) a);
                    wholesalerTableView.scrollTo((Company) a);
                });
            } else {
                Platform.runLater(() -> {
                    manufacturerTableView.getItems().add((Company) a);
                    manufacturerTableView.scrollTo((Company) a);
                });

            }
        }
    }

    @Override
    public void world(World world) {
    }

}
