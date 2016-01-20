/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.robby;

import mask.executor.Monitor;
import mask.executor.LocalExecutor;
import mask.world.World;
import mask.agent.Agent;
import mask.utils.gridSpace.GridSpace;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 *
 * @author zj
 */
public class RobbyMonitor extends Monitor {

    public RobbyMonitor() {
    }
    String newLine = System.getProperty("line.separator");

    public static void main(String[] args) {
// Launch the JavaFX application
        Application.launch(args);
    }

    private Canvas canvas;
    GraphicsContext gc;
    String title = "Robby World";
    int rowSize = 10;
    int columnSize = 10;
    int rowHeight = 60;
    int columnWidth = 60;
    Color backgroundColor = Color.GREY;
    String backgroundStyle = "-fx-background-color: grey";
    Color lineColor = Color.WHITE;
    Image robotImage = new Image("Robot_Santa_robot_48px_515795_easyicon.net.png");
    Image sodaImage = new Image("Soda_Can_16px_545363_easyicon.net.png");

    @Override
    public void process(Agent[] agents) {
        for (Agent agent : agents) {
            int row = (int) ((Robby) agent).getRow();
            int column = (int) ((Robby) agent).getColumn();
            Platform.runLater(() -> {
                setImageView(imageView, row, column, CellAlign.Center);
            });
        }
    }

    public static enum CellAlign {
        Center, Left, Right, Top, Bottom, LeftTop, LeftBottom, RigntTop, RightBottom
    }

    private void setImageView(ImageView view, int row, int column, CellAlign align) {
        double imageWidth = view.getImage().getWidth();
        double imageHeight = view.getImage().getHeight();
        double xOrigin = (columnSize * columnWidth) / 2 - imageWidth / 2;
        double yOrigin = (rowSize * rowHeight) / 2 - imageHeight / 2;
        double baseX = column * columnWidth;
        double baseY = row * rowHeight;
        double x = baseX - xOrigin;
        double y = baseY - yOrigin;
        switch (align) {
            case Center:
                x += (columnWidth - imageWidth) / 2;
                y += (rowHeight - imageHeight) / 2;
                break;
            case Left:
                y += (rowHeight - imageHeight) / 2;
                break;
            case Right:
                x += columnWidth - imageWidth;
                y += (rowHeight - imageHeight) / 2;
                break;
            case Top:
                x += (columnWidth - imageWidth) / 2;
                break;
            case Bottom:
                x += (columnWidth - imageWidth) / 2;
                y += baseY + rowHeight - imageHeight;
                break;
            case LeftTop:
                break;
            case LeftBottom:
                y += rowHeight - imageHeight;
                break;
            case RigntTop:
                x += columnWidth - imageWidth;
                break;
            case RightBottom:
                x += columnWidth - imageWidth;
                y += rowHeight - imageHeight;
                break;
        }
        view.setTranslateX(x);
        view.setTranslateY(y);
    }

    protected void writeImage(Image image, int row, int column, CellAlign align) {
        double baseX = column * columnWidth;
        double baseY = row * rowHeight;
        double x = baseX, y = baseY;
        switch (align) {
            case Center:
                x += (columnWidth - image.getWidth()) / 2;
                y += (rowHeight - image.getHeight()) / 2;
                break;
            case Left:
                y += (rowHeight - image.getHeight()) / 2;
                break;
            case Right:
                x += columnWidth - image.getWidth();
                y += (rowHeight - image.getHeight()) / 2;
                break;
            case Top:
                x += (columnWidth - image.getWidth()) / 2;
                break;
            case Bottom:
                x += (columnWidth - image.getWidth()) / 2;
                y += baseY + rowHeight - image.getHeight();
                break;
            case LeftTop:
                break;
            case LeftBottom:
                y += rowHeight - image.getHeight();
                break;
            case RigntTop:
                x += columnWidth - image.getWidth();
                break;
            case RightBottom:
                x += columnWidth - image.getWidth();
                y += rowHeight - image.getHeight();
                break;
        }
        final double xx = x;
        final double yy = y;
        Platform.runLater(() -> gc.drawImage(image, xx, yy));
    }
    ImageView imageView;

    @Override
    protected Tab[] createTabs() {
        Tab[] tabs = new Tab[1];
        canvas = new Canvas(columnSize * columnWidth, rowSize * rowHeight);
        gc = canvas.getGraphicsContext2D();
        if (lineColor != null) {
            gc.setStroke(lineColor);

            for (int i = 1; i < rowSize; i++) {
                gc.strokeLine(0, i * rowHeight, columnSize * columnWidth, i * rowHeight);
            }
            for (int i = 1; i < columnSize; i++) {
                gc.strokeLine(i * columnWidth, 0, i * columnWidth, rowSize * rowHeight);
            }
        }
        //   writeImage(robotImage, 0, 0, CellAlign.LeftTop);
        StackPane stackPane = new StackPane();
        imageView = new ImageView("Robot_Santa_robot_48px_515795_easyicon.net.png");
//        imageView.setTranslateX(-100);
//        imageView.setTranslateY(0);
        setImageView(imageView, 0, 0, CellAlign.Center);
        stackPane.getChildren().addAll(canvas, imageView);
        ScrollPane pane = new ScrollPane(stackPane);
//        pane.setPrefSize(800, 600);
        stackPane.setBackground(Background.EMPTY);
        stackPane.setStyle(backgroundStyle);
        tabs[0] = new Tab(title, pane);
        tabs[0].setClosable(false);
        return tabs;
    }

    @Override
    protected LocalExecutor newExecutor() {
        return LocalExecutor.newLocalExecutor(new RobbyConfig(), this);
    }

    private void clear(int row, int column, int margin) {
        gc.clearRect(column * columnWidth + margin, row * rowHeight + margin, columnWidth - margin, rowHeight - margin);
    }

    @Override
    public void process(World world) {
        System.out.println("World update");
        GridSpace<RobbyWorld.CellState> gridSpace = ((RobbyWorld) world).getGridSpace();
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < columnSize; j++) {
                if (gridSpace.getValue(i, j).equals(RobbyWorld.CellState.Soda)) {
                    writeImage(sodaImage, i, j, CellAlign.RightBottom);
                } else {
                    final int ii = i;
                    final int jj = j;
                    Platform.runLater(()
                            -> {
                        gc.setStroke(Color.GREY);
                        clear(ii, jj, 2);
                    });
                }
            }
        }
//        setImageView(imageView, 0, 0, CellAlign.Center);
    }
}
