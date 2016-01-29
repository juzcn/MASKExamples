/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.schelling;

import mask.executor.Monitor;
import mask.executor.LocalExecutor;
import mask.world.World;
import mask.agent.Agent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 *
 * @author zj
 */
public class SchellingMonitor extends Monitor {

    private Canvas canvas;

    public SchellingMonitor() {
    }
    String newLine = System.getProperty("line.separator");

    public static void main(String[] args) {
// Launch the JavaFX application
        Application.launch(args);
    }
    private TextArea textArea;

    @Override
    public void agents(Agent[] agents) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (Agent p : agents) {
            if (((Person) p).getMoved()) {
                if (((Person) p).getOldRow() != -1) {
                    int posY = ((Person) p).getOldRow() * 10;
                    int posX = ((Person) p).getColumn() * 10;
                    Platform.runLater(() -> {
                        gc.setFill(Color.GREY);
                        gc.fillRect(posX, posY, 10, 10);
                    });
                }
                int posY = ((Person) p).getRow() * 10;
                int posX = ((Person) p).getColumn() * 10;
                int color = ((Person) p).getColor();

                Platform.runLater(() -> {
                    if (color == 0) {
                        gc.setFill(Color.RED);
                    } else {
                        gc.setFill(Color.YELLOW);
                    }
                    gc.fillRect(posX, posY, 10, 10);
                });
            }
        }
    }

    @Override
    public void world(World world) {
    }

    public static class Console extends OutputStream {

        private TextArea output;

        public Console(TextArea ta) {
            this.output = ta;
        }

        @Override
        public void write(int i) throws IOException {
            Platform.runLater(() -> {
                output.appendText(String.valueOf((char) i));
            });
        }
    }

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
        textArea = new TextArea();
        tabs[1] = new Tab("Debug", textArea);

        tabs[1].setClosable(false);
        Console console = new Console(textArea);
        PrintStream ps = new PrintStream(console, true);
//        System.setOut(ps);
//        System.setErr(ps);
        return tabs;
    }

    @Override
    protected LocalExecutor newExecutor() {
        return LocalExecutor.newLocalExecutor(new SchellingConfig(), this);
    }

}
