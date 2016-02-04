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
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

        TableColumn<Company, Integer> toReceiveCol = new TableColumn<>("To receive");
        toReceiveCol.setCellValueFactory(new PropertyValueFactory<>("toReceive"));

        TableColumn<Company, Integer> receivedCol = new TableColumn<>("Received");
        receivedCol.setCellValueFactory(new PropertyValueFactory<>("received"));

        TableColumn<Company, Integer> orderedCol = new TableColumn<>("Ordered");
        orderedCol.setCellValueFactory(new PropertyValueFactory<>("ordered"));

        TableColumn<Company, Integer> stockoutCol = new TableColumn<>("Stockout");
        stockoutCol.setCellValueFactory(new PropertyValueFactory<>("stockout"));

        tableView.getColumns().addAll(timeCol, inventoryCol, replenishmentCol, toReceiveCol, receivedCol, orderedCol, stockoutCol);
        return tableView;
    }

    private XYChart.Series<Number, Number> inventorySeries;
    private XYChart.Series<Number, Number> retailerSeries;
    private XYChart.Series<Number, Number> wholesalerSeries;
    private XYChart.Series<Number, Number> manufacturerSeries;
    private XYChart.Series<Number, Number> stockoutSeries;
    private XYChart<Number, Number> xyChart;

    @Override
    protected Tab[] createTabs() {
        Tab[] tabs = new Tab[4];
        retailerTableView = createTableView();
        wholesalerTableView = createTableView();
        manufacturerTableView = createTableView();
        tabs[0] = new Tab("Retailer", retailerTableView);
        tabs[0].setClosable(false);
        tabs[1] = new Tab("Wholesaler", wholesalerTableView);
        tabs[1].setClosable(false);
        tabs[2] = new Tab("Manufacturer", manufacturerTableView);
        tabs[2].setClosable(false);

        NumberAxis timeAxis = new NumberAxis();
        timeAxis.setLabel("Time");
        NumberAxis numberAxis = new NumberAxis();
        numberAxis.setLabel("Quantity");

        xyChart = new LineChart(timeAxis, numberAxis);
        inventorySeries = new XYChart.Series<>();
        inventorySeries.setName("Total Inventory");
        retailerSeries = new XYChart.Series<>();
        retailerSeries.setName("Retailer Inventory");
        wholesalerSeries = new XYChart.Series<>();
        wholesalerSeries.setName("Wholesaler Inventory");
        manufacturerSeries = new XYChart.Series<>();
        manufacturerSeries.setName("Manufacturer Inventory");
        stockoutSeries = new XYChart.Series<>();
        stockoutSeries.setName("Retailer stockout");
        xyChart.getData().addAll(inventorySeries, retailerSeries, wholesalerSeries, manufacturerSeries,stockoutSeries);
        tabs[3] = new Tab("Inventory & Stockout", xyChart);
        tabs[3].setClosable(false);

        return tabs;
    }

    @Override
    protected LocalExecutor newExecutor() {
        return LocalExecutor.newLocalExecutor(new SupplyChain(), this);
    }

    @Override
    public void agents(Agent[] agents) {
        int totalInventory = 0;
        int retailerInventory = 0;
        int wholesalerInventory = 0;
        int totalStockout=0;
        int time = 0;
        for (Agent a : agents) {
            time = a.getTime();
            totalInventory += ((Company) a).getInventory();
            if (a instanceof Retailer) {
                retailerInventory+=((Company)a).getInventory();
                totalStockout+=((Company)a).getStockout();
                if (a.getName().contains("[1]")) {
                    Platform.runLater(() -> {
                        retailerTableView.getItems().add((Company) a);
                        retailerTableView.scrollTo((Company) a);
                    });
                }
            } else if (a instanceof Wholesaler) {
                wholesalerInventory+=((Company)a).getInventory();
                if (a.getName().contains("[1]")) {
                    Platform.runLater(() -> {
                        wholesalerTableView.getItems().add((Company) a);
                        wholesalerTableView.scrollTo((Company) a);
                    });
                }
            } else {
                Platform.runLater(() -> {
                    manufacturerTableView.getItems().add((Company) a);
                    manufacturerTableView.scrollTo((Company) a);
                    manufacturerSeries.getData().add(new XYChart.Data(a.getTime(), ((Company) a).getInventory()));
                });

            }
        }
        final int i = totalInventory;final int r=retailerInventory;
        final int t = time;final int w=wholesalerInventory;
        final int s=totalStockout;
        Platform.runLater(() -> {
            inventorySeries.getData().add(new XYChart.Data(t, i));
            retailerSeries.getData().add(new XYChart.Data(t, r));
            wholesalerSeries.getData().add(new XYChart.Data(t, w));
            stockoutSeries.getData().add(new XYChart.Data(t, s));
        });

    }

    @Override
    public void world(World world) {
    }

}
